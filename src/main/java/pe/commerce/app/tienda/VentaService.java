package pe.commerce.app.tienda;

import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.TypedQuery;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.commerce.app.domain.Authority;
import pe.commerce.app.domain.Carrito;
import pe.commerce.app.domain.CarritoItem;
import pe.commerce.app.domain.Categoria;
import pe.commerce.app.domain.Cliente;
import pe.commerce.app.domain.Comprobante;
import pe.commerce.app.domain.Cupon;
import pe.commerce.app.domain.DatoFacturacion;
import pe.commerce.app.domain.Direccion;
import pe.commerce.app.domain.DocumentoIdentidad;
import pe.commerce.app.domain.Envio;
import pe.commerce.app.domain.Inventario;
import pe.commerce.app.domain.Marca;
import pe.commerce.app.domain.MedioContacto;
import pe.commerce.app.domain.Pago;
import pe.commerce.app.domain.Pedido;
import pe.commerce.app.domain.PedidoDetalle;
import pe.commerce.app.domain.PedidoEstadoHistorial;
import pe.commerce.app.domain.Persona;
import pe.commerce.app.domain.Producto;
import pe.commerce.app.domain.Resena;
import pe.commerce.app.domain.Tienda;
import pe.commerce.app.domain.Ubigeo;
import pe.commerce.app.domain.User;
import pe.commerce.app.domain.enumeration.EstadoEnvio;
import pe.commerce.app.domain.enumeration.EstadoItemCarrito;
import pe.commerce.app.domain.enumeration.EstadoPago;
import pe.commerce.app.domain.enumeration.EstadoPedido;
import pe.commerce.app.domain.enumeration.EstadoSunat;
import pe.commerce.app.domain.enumeration.Moneda;
import pe.commerce.app.domain.enumeration.TipoComprobante;
import pe.commerce.app.domain.enumeration.TipoComprobantePedido;
import pe.commerce.app.domain.enumeration.TipoDescuento;
import pe.commerce.app.domain.enumeration.TipoEntrega;
import pe.commerce.app.domain.enumeration.TipoMedioContacto;
import pe.commerce.app.domain.enumeration.TipoPrecio;
import pe.commerce.app.repository.UserRepository;
import pe.commerce.app.security.AuthoritiesConstants;
import pe.commerce.app.security.SecurityUtils;
import pe.commerce.app.tienda.VentaDtos.*;

/**
 * Reglas de negocio de la tienda para el cliente: catalogo, registro, carrito,
 * checkout (pedido PENDIENTE_PAGO con expiracion y reserva de stock), pago y pedidos.
 */
@Service
@Transactional
public class VentaService {

    /** Minutos que tiene el cliente para pagar un pedido. */
    public static final int MINUTOS_PAGO = 30;
    /** Pedidos PENDIENTE_PAGO simultaneos permitidos por cliente (evita bloqueo de stock por bots). */
    public static final int MAX_PENDIENTES = 3;
    public static final BigDecimal ENVIO_GRATIS_DESDE = new BigDecimal("299");
    public static final BigDecimal COSTO_ENVIO = new BigDecimal("15.00");
    private static final BigDecimal IGV = new BigDecimal("18");
    private static final BigDecimal CIEN_DIECIOCHO = new BigDecimal("118");
    private static final ZoneId LIMA = ZoneId.of("America/Lima");
    private static final SecureRandom RANDOM = new SecureRandom();

    private final EntityManager em;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public VentaService(EntityManager em, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.em = em;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /* ======================================================================
     * CATALOGO (publico)
     * ====================================================================== */

    @Transactional(readOnly = true)
    public List<CategoriaNodo> categorias() {
        List<Categoria> todas = em
            .createQuery(
                "select c from Categoria c left join fetch c.padre where c.activo = true order by c.orden, c.nombre",
                Categoria.class
            )
            .getResultList();
        List<CategoriaNodo> raiz = new ArrayList<>();
        for (Categoria c : todas) {
            if (c.getPadre() == null) {
                List<CategoriaNodo> hijas = todas
                    .stream()
                    .filter(h -> h.getPadre() != null && h.getPadre().getId().equals(c.getId()))
                    .map(h -> new CategoriaNodo(h.getId(), h.getNombre(), h.getSlug(), List.of()))
                    .toList();
                raiz.add(new CategoriaNodo(c.getId(), c.getNombre(), c.getSlug(), hijas));
            }
        }
        return raiz;
    }

    @Transactional(readOnly = true)
    public List<MarcaItem> marcas() {
        return em
            .createQuery(
                "select m from Marca m where m.activo = true and exists (select p.id from Producto p where p.marca = m and p.activo = true) order by m.nombre",
                Marca.class
            )
            .getResultList()
            .stream()
            .map(m -> new MarcaItem(m.getId(), m.getNombre()))
            .toList();
    }

    @Transactional(readOnly = true)
    public Pagina<ProductoCard> productos(String categoria, Long marcaId, String q, boolean soloOfertas, String orden, int page, int size) {
        int tam = Math.min(Math.max(size, 1), 60);
        int pag = Math.max(page, 0);
        StringBuilder where = new StringBuilder(
            " from Producto p join p.categoria c left join c.padre cp join p.marca m where p.activo = true"
        );
        Map<String, Object> params = new HashMap<>();
        if (categoria != null && !categoria.isBlank()) {
            where.append(" and (c.slug = :cat or cp.slug = :cat)");
            params.put("cat", categoria);
        }
        if (marcaId != null) {
            where.append(" and m.id = :marca");
            params.put("marca", marcaId);
        }
        if (q != null && !q.isBlank()) {
            where.append(" and (lower(p.nombre) like :q or lower(p.sku) like :q or lower(m.nombre) like :q)");
            params.put("q", "%" + q.trim().toLowerCase(Locale.ROOT) + "%");
        }
        if (soloOfertas) {
            where.append(" and p.precioOferta is not null");
        }
        String order = switch (orden == null ? "" : orden) {
            case "precio_asc" -> " order by coalesce(p.precioOferta, p.precioLista) asc, p.id";
            case "precio_desc" -> " order by coalesce(p.precioOferta, p.precioLista) desc, p.id";
            case "nombre" -> " order by p.nombre asc, p.id";
            default -> " order by p.createdDate desc, p.id desc";
        };
        TypedQuery<Long> cq = em.createQuery("select count(p)" + where, Long.class);
        TypedQuery<Producto> pq = em.createQuery("select p" + where + order, Producto.class);
        params.forEach((k, v) -> {
            cq.setParameter(k, v);
            pq.setParameter(k, v);
        });
        long total = cq.getSingleResult();
        List<Producto> productos = pq
            .setFirstResult(pag * tam)
            .setMaxResults(tam)
            .getResultList();
        return new Pagina<>(cards(productos), total, pag, tam);
    }

    private List<ProductoCard> cards(List<Producto> productos) {
        if (productos.isEmpty()) return List.of();
        List<Long> ids = productos.stream().map(Producto::getId).toList();
        Map<Long, String> img = primerasImagenes(ids);
        Map<Long, Object[]> rating = new HashMap<>();
        for (Object[] r : em
            .createQuery(
                "select r.producto.id, avg(r.calificacion), count(r) from Resena r where r.aprobada = true and r.producto.id in :ids group by r.producto.id",
                Object[].class
            )
            .setParameter("ids", ids)
            .getResultList()) {
            rating.put((Long) r[0], r);
        }
        return productos
            .stream()
            .map(p -> {
                Object[] r = rating.get(p.getId());
                return new ProductoCard(
                    p.getId(),
                    p.getSku(),
                    p.getNombre(),
                    p.getSlug(),
                    p.getMarca().getNombre(),
                    p.getCategoria().getNombre(),
                    p.getPrecioLista(),
                    p.getPrecioOferta(),
                    img.get(p.getId()),
                    r == null ? null : redondear1((Double) r[1]),
                    r == null ? 0L : (Long) r[2]
                );
            })
            .toList();
    }

    private Map<Long, String> primerasImagenes(List<Long> productoIds) {
        Map<Long, String> img = new HashMap<>();
        if (productoIds.isEmpty()) return img;
        for (Object[] r : em
            .createQuery(
                "select i.producto.id, i.url from ProductoImagen i where i.producto.id in :ids order by i.orden, i.id",
                Object[].class
            )
            .setParameter("ids", productoIds)
            .getResultList()) {
            img.putIfAbsent((Long) r[0], (String) r[1]);
        }
        return img;
    }

    @Transactional(readOnly = true)
    public ProductoDetalle producto(String slug) {
        Producto p = em
            .createQuery(
                "select p from Producto p join fetch p.categoria join fetch p.marca where p.slug = :slug and p.activo = true",
                Producto.class
            )
            .setParameter("slug", slug)
            .getResultStream()
            .findFirst()
            .orElseThrow(() -> new VentaException("Producto no encontrado"));
        List<String> imagenes = em
            .createQuery("select i.url from ProductoImagen i where i.producto = :p order by i.orden, i.id", String.class)
            .setParameter("p", p)
            .getResultList();
        Tienda almacen = almacen();
        int online = 0;
        List<StockTienda> tiendas = new ArrayList<>();
        for (Inventario inv : em
            .createQuery(
                "select i from Inventario i join fetch i.tienda t where i.producto = :p and t.activo = true order by t.id",
                Inventario.class
            )
            .setParameter("p", p)
            .getResultList()) {
            int disp = disponible(inv);
            if (almacen != null && inv.getTienda().getId().equals(almacen.getId())) {
                online = disp;
            }
            if (Boolean.TRUE.equals(inv.getTienda().getPermiteRecojo())) {
                tiendas.add(new StockTienda(inv.getTienda().getId(), inv.getTienda().getNombre(), disp));
            }
        }
        List<ResenaItem> resenas = em
            .createQuery(
                "select r from Resena r join fetch r.cliente c join fetch c.persona where r.producto = :p and r.aprobada = true order by r.fecha desc",
                Resena.class
            )
            .setParameter("p", p)
            .setMaxResults(20)
            .getResultList()
            .stream()
            .map(r ->
                new ResenaItem(
                    r.getCliente().getPersona().getNombres().split(" ")[0] +
                        " " +
                        r.getCliente().getPersona().getApellidos().charAt(0) +
                        ".",
                    r.getCalificacion(),
                    r.getComentario(),
                    r.getFecha(),
                    Boolean.TRUE.equals(r.getCompraVerificada())
                )
            )
            .toList();
        Double rating = em
            .createQuery("select avg(r.calificacion) from Resena r where r.producto = :p and r.aprobada = true", Double.class)
            .setParameter("p", p)
            .getSingleResult();
        return new ProductoDetalle(
            p.getId(),
            p.getSku(),
            p.getNombre(),
            p.getSlug(),
            p.getModelo(),
            p.getMarca().getNombre(),
            p.getCategoria().getNombre(),
            p.getCategoria().getSlug(),
            p.getDescripcion(),
            p.getEspecificaciones(),
            p.getPrecioLista(),
            p.getPrecioOferta(),
            p.getGarantiaMeses(),
            imagenes,
            online,
            tiendas,
            rating == null ? null : redondear1(rating),
            resenas
        );
    }

    @Transactional(readOnly = true)
    public List<TiendaItem> tiendasRecojo() {
        return em
            .createQuery(
                "select t from Tienda t join fetch t.ubigeo where t.activo = true and t.permiteRecojo = true order by t.nombre",
                Tienda.class
            )
            .getResultList()
            .stream()
            .map(t -> new TiendaItem(t.getId(), t.getNombre(), t.getDireccion(), t.getUbigeo().getDistrito()))
            .toList();
    }

    @Transactional(readOnly = true)
    public List<UbigeoItem> ubigeos() {
        return em
            .createQuery("select u from Ubigeo u order by u.departamento, u.provincia, u.distrito", Ubigeo.class)
            .getResultList()
            .stream()
            .map(u -> new UbigeoItem(u.getId(), u.getCodigo(), u.getDepartamento(), u.getProvincia(), u.getDistrito()))
            .toList();
    }

    /* ======================================================================
     * REGISTRO Y CUENTA
     * ====================================================================== */

    public Perfil registrar(RegistroRequest req) {
        String email = req.email().trim().toLowerCase(Locale.ROOT);
        if (userRepository.findOneByEmailIgnoreCase(email).isPresent() || userRepository.findOneByLogin(email).isPresent()) {
            throw new VentaException("Ya existe una cuenta con ese correo");
        }
        String numero = req.numeroDocumento().trim().toUpperCase(Locale.ROOT);
        Long existe = em
            .createQuery("select count(d) from DocumentoIdentidad d where d.tipoDocumento = :t and d.numero = :n", Long.class)
            .setParameter("t", req.tipoDocumento())
            .setParameter("n", numero)
            .getSingleResult();
        if (existe > 0) {
            throw new VentaException("Ya existe un cliente con ese documento");
        }

        User user = new User();
        user.setLogin(email);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(req.password()));
        user.setFirstName(primeraPalabra(req.nombres(), 50));
        user.setLastName(primeraPalabra(req.apellidos(), 50));
        user.setActivated(true);
        user.setLangKey("es");
        user.setAuthorities(new HashSet<>(Set.of(em.getReference(Authority.class, AuthoritiesConstants.USER))));
        em.persist(user);

        Persona persona = new Persona();
        persona.setNombres(req.nombres().trim());
        persona.setApellidos(req.apellidos().trim());
        em.persist(persona);

        DocumentoIdentidad doc = new DocumentoIdentidad();
        doc.setTipoDocumento(req.tipoDocumento());
        doc.setNumero(numero);
        doc.setPrincipal(true);
        doc.setVigente(true);
        doc.setPersona(persona);
        em.persist(doc);

        em.persist(medio(persona, TipoMedioContacto.CELULAR, req.celular(), "+51"));
        em.persist(medio(persona, TipoMedioContacto.EMAIL, email, null));

        Cliente cliente = new Cliente();
        cliente.setCodigo(siguienteCodigoCliente());
        cliente.setFechaRegistro(Instant.now());
        cliente.setAceptaPublicidad(req.aceptaPublicidad());
        cliente.setActivo(true);
        cliente.setUser(user);
        cliente.setPersona(persona);
        em.persist(cliente);

        Carrito carrito = new Carrito();
        carrito.setCliente(cliente);
        em.persist(carrito);

        return new Perfil(
            cliente.getId(),
            cliente.getCodigo(),
            persona.getNombres(),
            persona.getApellidos(),
            email,
            doc.getTipoDocumento().name(),
            numero,
            req.celular()
        );
    }

    private MedioContacto medio(Persona persona, TipoMedioContacto tipo, String valor, String codigoPais) {
        MedioContacto m = new MedioContacto();
        m.setTipo(tipo);
        m.setValor(valor);
        m.setCodigoPais(codigoPais);
        m.setPrincipal(true);
        m.setVerificado(false);
        m.setActivo(true);
        m.setPersona(persona);
        return m;
    }

    private String siguienteCodigoCliente() {
        String max = em.createQuery("select max(c.codigo) from Cliente c where c.codigo like 'CLI-%'", String.class).getSingleResult();
        int n = max == null ? 0 : Integer.parseInt(max.substring(4));
        return String.format("CLI-%06d", n + 1);
    }

    @Transactional(readOnly = true)
    public Perfil perfil() {
        Cliente c = clienteActual();
        Persona p = c.getPersona();
        Optional<DocumentoIdentidad> doc = documentoPrincipal(p);
        String celular = em
            .createQuery(
                "select m.valor from MedioContacto m where m.persona = :p and m.tipo = :t and m.activo = true order by m.principal desc, m.id",
                String.class
            )
            .setParameter("p", p)
            .setParameter("t", TipoMedioContacto.CELULAR)
            .setMaxResults(1)
            .getResultStream()
            .findFirst()
            .orElse(null);
        return new Perfil(
            c.getId(),
            c.getCodigo(),
            p.getNombres(),
            p.getApellidos(),
            c.getUser().getEmail(),
            doc.map(d -> d.getTipoDocumento().name()).orElse(null),
            doc.map(DocumentoIdentidad::getNumero).orElse(null),
            celular
        );
    }

    @Transactional(readOnly = true)
    public List<DireccionItem> direcciones() {
        return em
            .createQuery(
                "select d from Direccion d join fetch d.ubigeo where d.cliente = :c and d.activo = true order by d.predeterminada desc, d.id",
                Direccion.class
            )
            .setParameter("c", clienteActual())
            .getResultList()
            .stream()
            .map(this::direccionItem)
            .toList();
    }

    private DireccionItem direccionItem(Direccion d) {
        Ubigeo u = d.getUbigeo();
        return new DireccionItem(
            d.getId(),
            d.getAlias(),
            d.getDireccion(),
            d.getReferencia(),
            u.getId(),
            u.getDistrito() + ", " + u.getProvincia(),
            Boolean.TRUE.equals(d.getPredeterminada())
        );
    }

    public DireccionItem agregarDireccion(DireccionRequest req) {
        Cliente c = clienteActual();
        Ubigeo u = Optional.ofNullable(em.find(Ubigeo.class, req.ubigeoId())).orElseThrow(() -> new VentaException("Distrito no válido"));
        List<Direccion> actuales = em
            .createQuery("select d from Direccion d where d.cliente = :c and d.activo = true", Direccion.class)
            .setParameter("c", c)
            .getResultList();
        boolean predeterminada = req.predeterminada() || actuales.isEmpty();
        if (predeterminada) actuales.forEach(d -> d.setPredeterminada(false));
        Direccion d = new Direccion();
        d.setAlias(req.alias().trim());
        d.setDireccion(req.direccion().trim());
        d.setReferencia(vacioANull(req.referencia()));
        d.setPredeterminada(predeterminada);
        d.setActivo(true);
        d.setUbigeo(u);
        d.setCliente(c);
        em.persist(d);
        return direccionItem(d);
    }

    @Transactional(readOnly = true)
    public List<FacturacionItem> datosFacturacion() {
        return em
            .createQuery(
                "select d from DatoFacturacion d where d.cliente = :c and d.activo = true order by d.predeterminado desc, d.id",
                DatoFacturacion.class
            )
            .setParameter("c", clienteActual())
            .getResultList()
            .stream()
            .map(d ->
                new FacturacionItem(
                    d.getId(),
                    d.getRuc(),
                    d.getRazonSocial(),
                    d.getDireccionFiscal(),
                    Boolean.TRUE.equals(d.getPredeterminado())
                )
            )
            .toList();
    }

    public FacturacionItem agregarDatoFacturacion(FacturacionRequest req) {
        if (!rucValido(req.ruc())) throw new VentaException("El RUC no es válido (dígito verificador)");
        Cliente c = clienteActual();
        List<DatoFacturacion> actuales = em
            .createQuery("select d from DatoFacturacion d where d.cliente = :c and d.activo = true", DatoFacturacion.class)
            .setParameter("c", c)
            .getResultList();
        if (actuales.stream().anyMatch(d -> d.getRuc().equals(req.ruc()))) throw new VentaException("Ese RUC ya está registrado");
        boolean pred = req.predeterminado() || actuales.isEmpty();
        if (pred) actuales.forEach(d -> d.setPredeterminado(false));
        DatoFacturacion d = new DatoFacturacion();
        d.setRuc(req.ruc());
        d.setRazonSocial(req.razonSocial().trim().toUpperCase(Locale.ROOT));
        d.setDireccionFiscal(req.direccionFiscal().trim());
        d.setPredeterminado(pred);
        d.setActivo(true);
        d.setCliente(c);
        em.persist(d);
        return new FacturacionItem(d.getId(), d.getRuc(), d.getRazonSocial(), d.getDireccionFiscal(), pred);
    }

    static boolean rucValido(String ruc) {
        if (ruc == null || !ruc.matches("^(10|20)\\d{9}$")) return false;
        int[] pesos = { 5, 4, 3, 2, 7, 6, 5, 4, 3, 2 };
        int suma = 0;
        for (int i = 0; i < 10; i++) suma += (ruc.charAt(i) - '0') * pesos[i];
        int dv = 11 - (suma % 11);
        if (dv == 10) dv = 0;
        if (dv == 11) dv = 1;
        return dv == ruc.charAt(10) - '0';
    }

    /* ======================================================================
     * CARRITO
     * ====================================================================== */

    public CarritoDto carrito() {
        return carritoDto(carritoActual());
    }

    public CarritoDto agregarItem(AgregarItemRequest req) {
        Carrito carrito = carritoActual();
        Producto p = Optional.ofNullable(em.find(Producto.class, req.productoId()))
            .filter(x -> Boolean.TRUE.equals(x.getActivo()))
            .orElseThrow(() -> new VentaException("Producto no disponible"));
        CarritoItem item = em
            .createQuery("select i from CarritoItem i where i.carrito = :c and i.producto = :p and i.estado in :estados", CarritoItem.class)
            .setParameter("c", carrito)
            .setParameter("p", p)
            .setParameter("estados", List.of(EstadoItemCarrito.ACTIVO, EstadoItemCarrito.GUARDADO))
            .getResultStream()
            .findFirst()
            .orElse(null);
        int cantidad = req.cantidad() + (item == null ? 0 : item.getCantidad());
        validarStockOnline(p, cantidad);
        boolean nuevo = item == null;
        if (nuevo) {
            item = new CarritoItem();
            item.setCarrito(carrito);
            item.setProducto(p);
        }
        item.setCantidad(cantidad);
        item.setEstado(EstadoItemCarrito.ACTIVO);
        item.setSeleccionado(true);
        if (nuevo) em.persist(item);
        return carritoDto(carrito);
    }

    public CarritoDto actualizarItem(Long itemId, ActualizarItemRequest req) {
        Carrito carrito = carritoActual();
        CarritoItem item = itemDelCarrito(carrito, itemId);
        if (req.cantidad() != null) {
            validarStockOnline(item.getProducto(), req.cantidad());
            item.setCantidad(req.cantidad());
        }
        if (req.estado() != null) {
            EstadoItemCarrito e = EstadoItemCarrito.valueOf(req.estado());
            if (e != EstadoItemCarrito.ACTIVO && e != EstadoItemCarrito.GUARDADO) throw new VentaException("Estado no permitido");
            item.setEstado(e);
            if (e == EstadoItemCarrito.GUARDADO) item.setSeleccionado(false);
        }
        if (req.seleccionado() != null && item.getEstado() == EstadoItemCarrito.ACTIVO) {
            item.setSeleccionado(req.seleccionado());
        }
        return carritoDto(carrito);
    }

    public CarritoDto eliminarItem(Long itemId) {
        Carrito carrito = carritoActual();
        CarritoItem item = itemDelCarrito(carrito, itemId);
        item.setEstado(EstadoItemCarrito.ELIMINADO);
        item.setSeleccionado(false);
        return carritoDto(carrito);
    }

    private CarritoItem itemDelCarrito(Carrito carrito, Long itemId) {
        CarritoItem item = em.find(CarritoItem.class, itemId);
        if (
            item == null ||
            !item.getCarrito().getId().equals(carrito.getId()) ||
            (item.getEstado() != EstadoItemCarrito.ACTIVO && item.getEstado() != EstadoItemCarrito.GUARDADO)
        ) {
            throw new VentaException("El producto ya no está en tu carrito");
        }
        return item;
    }

    private void validarStockOnline(Producto p, int cantidad) {
        Tienda almacen = almacen();
        int disp = almacen == null ? 0 : inventario(p, almacen, false).map(this::disponible).orElse(0);
        int tiendas = em
            .createQuery(
                "select i from Inventario i where i.producto = :p and i.tienda.permiteRecojo = true and i.tienda.activo = true",
                Inventario.class
            )
            .setParameter("p", p)
            .getResultList()
            .stream()
            .mapToInt(this::disponible)
            .max()
            .orElse(0);
        if (cantidad > Math.max(disp, tiendas)) {
            throw new VentaException("Solo hay " + Math.max(disp, tiendas) + " unidades disponibles de " + p.getNombre());
        }
    }

    private CarritoDto carritoDto(Carrito carrito) {
        em.flush();
        List<CarritoItem> items = em
            .createQuery(
                "select i from CarritoItem i join fetch i.producto where i.carrito = :c and i.estado in :estados order by i.id",
                CarritoItem.class
            )
            .setParameter("c", carrito)
            .setParameter("estados", List.of(EstadoItemCarrito.ACTIVO, EstadoItemCarrito.GUARDADO))
            .getResultList();
        Map<Long, String> img = primerasImagenes(
            items
                .stream()
                .map(i -> i.getProducto().getId())
                .toList()
        );
        Tienda almacen = almacen();
        List<CarritoLinea> lineas = new ArrayList<>();
        int cant = 0;
        BigDecimal sub = BigDecimal.ZERO;
        for (CarritoItem i : items) {
            Producto p = i.getProducto();
            BigDecimal precio = precio(p);
            BigDecimal st = precio.multiply(BigDecimal.valueOf(i.getCantidad()));
            int disp = almacen == null ? 0 : inventario(p, almacen, false).map(this::disponible).orElse(0);
            boolean sel = Boolean.TRUE.equals(i.getSeleccionado()) && i.getEstado() == EstadoItemCarrito.ACTIVO;
            if (sel) {
                cant += i.getCantidad();
                sub = sub.add(st);
            }
            lineas.add(
                new CarritoLinea(
                    i.getId(),
                    p.getId(),
                    p.getSku(),
                    p.getNombre(),
                    p.getSlug(),
                    img.get(p.getId()),
                    p.getPrecioLista(),
                    p.getPrecioOferta(),
                    precio,
                    i.getCantidad(),
                    sel,
                    i.getEstado().name(),
                    disp,
                    st
                )
            );
        }
        return new CarritoDto(lineas, cant, sub);
    }

    /* ======================================================================
     * CHECKOUT, PAGO Y EXPIRACION
     * ====================================================================== */

    public ResumenCheckout resumen(CheckoutRequest req) {
        Cliente c = clienteActual();
        return calcular(c, itemsSeleccionados(carritoDe(c)), req, false).resumen();
    }

    public PedidoDetalleDto checkout(CheckoutRequest req) {
        Cliente cliente = clienteActual();
        Long pendientes = em
            .createQuery("select count(p) from Pedido p where p.cliente = :c and p.estado = :e", Long.class)
            .setParameter("c", cliente)
            .setParameter("e", EstadoPedido.PENDIENTE_PAGO)
            .getSingleResult();
        if (pendientes >= MAX_PENDIENTES) {
            throw new VentaException("Tienes " + pendientes + " pedidos pendientes de pago. Págalos o espera a que expiren.");
        }
        List<CarritoItem> items = itemsSeleccionados(carritoDe(cliente));
        Calculo calc = calcular(cliente, items, req, true);

        Tienda tiendaRecojo = null;
        Direccion direccion = null;
        if (req.tipoEntrega() == TipoEntrega.RECOJO_TIENDA) {
            tiendaRecojo = Optional.ofNullable(req.tiendaId())
                .map(id -> em.find(Tienda.class, id))
                .filter(t -> Boolean.TRUE.equals(t.getPermiteRecojo()) && Boolean.TRUE.equals(t.getActivo()))
                .orElseThrow(() -> new VentaException("Elige una tienda de recojo"));
        } else {
            direccion = Optional.ofNullable(req.direccionId())
                .map(id -> em.find(Direccion.class, id))
                .filter(d -> d.getCliente().getId().equals(cliente.getId()) && Boolean.TRUE.equals(d.getActivo()))
                .orElseThrow(() -> new VentaException("Elige una dirección de entrega"));
        }
        if (req.tipoComprobante() == TipoComprobantePedido.FACTURA) {
            datoFacturacionDe(cliente, req.datoFacturacionId());
        }

        // reserva de stock en la tienda de origen
        Tienda origen = tiendaRecojo != null ? tiendaRecojo : almacen();
        if (origen == null) throw new VentaException("No hay almacén configurado");
        for (CarritoItem i : items) {
            Inventario inv = inventario(i.getProducto(), origen, true).orElse(null);
            if (inv == null || disponible(inv) < i.getCantidad()) {
                throw new VentaException(
                    "Stock insuficiente de " +
                        i.getProducto().getNombre() +
                        " en " +
                        origen.getNombre() +
                        " (disponible: " +
                        (inv == null ? 0 : disponible(inv)) +
                        ")"
                );
            }
            inv.setStockReservado(inv.getStockReservado() + i.getCantidad());
        }

        Instant ahora = Instant.now();
        Pedido p = new Pedido();
        p.setNroPedido(siguienteNroPedido());
        p.setTipoEntrega(req.tipoEntrega());
        p.setTipoComprobante(req.tipoComprobante());
        p.setEstado(EstadoPedido.PENDIENTE_PAGO);
        p.setMoneda(Moneda.PEN);
        p.setSubtotal(calc.subtotal);
        p.setDescuento(calc.descuento);
        p.setCostoEnvio(calc.costoEnvio);
        p.setIgv(calc.igv);
        p.setTotal(calc.total);
        p.setFechaPedido(ahora);
        p.setFechaExpiracion(ahora.plus(MINUTOS_PAGO, ChronoUnit.MINUTES));
        p.setObservaciones(vacioANull(req.observaciones()));
        p.setCupon(calc.cupon);
        p.setTiendaRecojo(tiendaRecojo);
        p.setCliente(cliente);
        em.persist(p);

        for (CarritoItem i : items) {
            Producto prod = i.getProducto();
            boolean oferta = prod.getPrecioOferta() != null;
            BigDecimal desc = oferta ? prod.getPrecioLista().subtract(prod.getPrecioOferta()) : BigDecimal.ZERO;
            PedidoDetalle d = new PedidoDetalle();
            d.setSkuProducto(prod.getSku());
            d.setNombreProducto(prod.getNombre());
            d.setCantidad(i.getCantidad());
            d.setTipoPrecio(oferta ? TipoPrecio.OFERTA : TipoPrecio.LISTA);
            d.setPrecioUnitario(prod.getPrecioLista());
            d.setDescuento(desc);
            d.setSubtotal(precio(prod).multiply(BigDecimal.valueOf(i.getCantidad())));
            d.setProducto(prod);
            d.setCarritoItem(i);
            d.setPedido(p);
            em.persist(d);
            i.setEstado(EstadoItemCarrito.CONVERTIDO);
            i.setSeleccionado(false);
        }

        if (direccion != null) {
            Persona per = cliente.getPersona();
            Envio e = new Envio();
            e.setEstado(EstadoEnvio.PENDIENTE);
            e.setDireccionEntrega(direccion.getDireccion());
            e.setUbigeoEntrega(direccion.getUbigeo().getCodigo());
            e.setReferenciaEntrega(direccion.getReferencia());
            e.setNombreReceptor(
                recortar(
                    direccion.getNombreContacto() != null ? direccion.getNombreContacto() : per.getNombres() + " " + per.getApellidos(),
                    100
                )
            );
            e.setTelefonoReceptor(
                recortar(
                    direccion.getTelefonoContacto() != null
                        ? direccion.getTelefonoContacto()
                        : Optional.ofNullable(perfilCelular(per)).orElse("-"),
                    15
                )
            );
            e.setFechaEstimada(LocalDate.now(LIMA).plusDays(3));
            e.setDireccion(direccion);
            e.setPedido(p);
            em.persist(e);
        }
        historial(p, EstadoPedido.PENDIENTE_PAGO, "Pedido registrado desde la web. Tienes " + MINUTOS_PAGO + " minutos para pagar.");
        em.flush();
        return pedido(p.getId());
    }

    /** No revierte ante VentaException para que la expiracion detectada al pagar quede guardada. */
    @Transactional(noRollbackFor = VentaException.class)
    public PedidoDetalleDto pagar(Long pedidoId, PagoRequest req) {
        Cliente cliente = clienteActual();
        Pedido p = pedidoDe(cliente, pedidoId, true);
        if (p.getEstado() != EstadoPedido.PENDIENTE_PAGO) {
            throw new VentaException("El pedido " + p.getNroPedido() + " no está pendiente de pago");
        }
        if (p.getFechaExpiracion().isBefore(Instant.now())) {
            expirar(p);
            throw new VentaException(
                "El tiempo para pagar el pedido " + p.getNroPedido() + " venció. Los productos volvieron a tu carrito."
            );
        }
        DatoFacturacion fact =
            p.getTipoComprobante() == TipoComprobantePedido.FACTURA ? datoFacturacionDe(cliente, req.datoFacturacionId()) : null;
        Instant ahora = Instant.now();

        Pago pago = new Pago();
        pago.setMetodo(req.metodo());
        pago.setSubMetodo(vacioANull(req.subMetodo()));
        pago.setPasarela("SIMULADA");
        pago.setNroTransaccion(String.format("%012d", RANDOM.nextLong(1_000_000_000_000L)));
        pago.setMoneda(p.getMoneda());
        pago.setMonto(p.getTotal());
        pago.setEstado(EstadoPago.APROBADO);
        pago.setFechaPago(ahora);
        pago.setPedido(p);
        em.persist(pago);

        // la reserva pasa a salida de stock
        Tienda origen = p.getTiendaRecojo() != null ? p.getTiendaRecojo() : almacen();
        for (PedidoDetalle d : detalles(p)) {
            inventario(d.getProducto(), origen, true).ifPresent(inv -> {
                inv.setStock(Math.max(0, inv.getStock() - d.getCantidad()));
                inv.setStockReservado(Math.max(0, inv.getStockReservado() - d.getCantidad()));
            });
        }

        p.setEstado(EstadoPedido.PAGADO);
        historial(
            p,
            EstadoPedido.PAGADO,
            "Pago aprobado (" + req.metodo().name() + (pago.getSubMetodo() != null ? " " + pago.getSubMetodo() : "") + ")"
        );
        emitirComprobante(p, cliente, fact, ahora);
        em.flush();
        return pedido(p.getId());
    }

    private void emitirComprobante(Pedido p, Cliente cliente, DatoFacturacion fact, Instant fecha) {
        Comprobante c = new Comprobante();
        if (fact != null) {
            c.setTipo(TipoComprobante.FACTURA);
            c.setSerie("F001");
            c.setReceptorTipoDocumento("RUC");
            c.setReceptorNroDocumento(fact.getRuc());
            c.setReceptorNombre(fact.getRazonSocial());
            c.setReceptorDireccion(fact.getDireccionFiscal());
        } else {
            Persona per = cliente.getPersona();
            DocumentoIdentidad doc = documentoPrincipal(per).orElseThrow(() ->
                new VentaException("El cliente no tiene documento de identidad")
            );
            c.setTipo(TipoComprobante.BOLETA);
            c.setSerie("B001");
            c.setReceptorTipoDocumento(doc.getTipoDocumento().name());
            c.setReceptorNroDocumento(doc.getNumero());
            c.setReceptorNombre(recortar(per.getNombres() + " " + per.getApellidos(), 200));
        }
        Integer max = em
            .createQuery("select max(c.numero) from Comprobante c where c.serie = :s", Integer.class)
            .setParameter("s", c.getSerie())
            .getSingleResult();
        c.setNumero(max == null ? 1 : max + 1);
        c.setFechaEmision(fecha);
        c.setMoneda(p.getMoneda());
        c.setTotal(p.getTotal());
        c.setIgv(p.getIgv());
        c.setOpGravada(p.getTotal().subtract(p.getIgv()));
        c.setEstadoSunat(EstadoSunat.PENDIENTE);
        String nombre = c.getSerie() + "-" + String.format("%08d", c.getNumero());
        c.setXmlUrl("/comprobantes/" + nombre + ".xml");
        c.setPdfUrl("/comprobantes/" + nombre + ".pdf");
        c.setPedido(p);
        em.persist(c);
    }

    /** Pasa a EXPIRADO los pedidos PENDIENTE_PAGO vencidos. Lo invoca el job cada minuto. */
    public int expirarPendientes() {
        List<Pedido> vencidos = em
            .createQuery("select p from Pedido p where p.estado = :e and p.fechaExpiracion < :ahora", Pedido.class)
            .setParameter("e", EstadoPedido.PENDIENTE_PAGO)
            .setParameter("ahora", Instant.now())
            .getResultList();
        vencidos.forEach(this::expirar);
        return vencidos.size();
    }

    private void expirar(Pedido p) {
        Tienda origen = p.getTiendaRecojo() != null ? p.getTiendaRecojo() : almacen();
        for (PedidoDetalle d : detalles(p)) {
            if (origen != null) {
                inventario(d.getProducto(), origen, true).ifPresent(inv ->
                    inv.setStockReservado(Math.max(0, inv.getStockReservado() - d.getCantidad()))
                );
            }
            CarritoItem item = d.getCarritoItem();
            if (item != null) {
                d.setCarritoItem(null);
                if (item.getEstado() == EstadoItemCarrito.CONVERTIDO) {
                    item.setEstado(EstadoItemCarrito.ACTIVO);
                    item.setSeleccionado(true);
                }
            }
        }
        em.createQuery("select e from Envio e where e.pedido = :p and e.estado = :pend", Envio.class)
            .setParameter("p", p)
            .setParameter("pend", EstadoEnvio.PENDIENTE)
            .getResultList()
            .forEach(em::remove);
        p.setEstado(EstadoPedido.EXPIRADO);
        historial(p, EstadoPedido.EXPIRADO, "Sin pago dentro de los " + MINUTOS_PAGO + " minutos; stock liberado");
    }

    private record Calculo(
        BigDecimal subtotal,
        BigDecimal descuento,
        BigDecimal costoEnvio,
        BigDecimal igv,
        BigDecimal total,
        Cupon cupon,
        String mensajeCupon,
        List<LineaResumen> lineas
    ) {
        ResumenCheckout resumen() {
            return new ResumenCheckout(
                lineas,
                subtotal,
                descuento,
                cupon == null ? null : cupon.getCodigo(),
                mensajeCupon,
                costoEnvio,
                igv,
                total
            );
        }
    }

    private Calculo calcular(Cliente cliente, List<CarritoItem> items, CheckoutRequest req, boolean estricto) {
        if (items.isEmpty()) throw new VentaException("No hay productos seleccionados en tu carrito");
        BigDecimal subtotal = BigDecimal.ZERO;
        List<LineaResumen> lineas = new ArrayList<>();
        for (CarritoItem i : items) {
            Producto p = i.getProducto();
            BigDecimal st = precio(p).multiply(BigDecimal.valueOf(i.getCantidad()));
            subtotal = subtotal.add(st);
            lineas.add(
                new LineaResumen(
                    p.getSku(),
                    p.getNombre(),
                    i.getCantidad(),
                    precio(p),
                    st,
                    p.getPrecioOferta() != null ? "OFERTA" : "LISTA"
                )
            );
        }
        Cupon cupon = null;
        String mensaje = null;
        BigDecimal descuento = BigDecimal.ZERO;
        if (req.cuponCodigo() != null && !req.cuponCodigo().isBlank()) {
            String codigo = req.cuponCodigo().trim().toUpperCase(Locale.ROOT);
            Cupon cup = em
                .createQuery("select c from Cupon c where upper(c.codigo) = :c", Cupon.class)
                .setParameter("c", codigo)
                .getResultStream()
                .findFirst()
                .orElse(null);
            mensaje = validarCupon(cup, cliente, subtotal);
            if (mensaje == null) {
                cupon = cup;
                descuento =
                    cup.getTipoDescuento() == TipoDescuento.PORCENTAJE
                        ? subtotal.multiply(cup.getValor()).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP)
                        : cup.getValor().min(subtotal);
                mensaje = "Cupón aplicado: " + cup.getDescripcion();
            } else if (estricto) {
                throw new VentaException(mensaje);
            }
        }
        BigDecimal neto = subtotal.subtract(descuento);
        BigDecimal envio =
            req.tipoEntrega() == TipoEntrega.DELIVERY && neto.compareTo(ENVIO_GRATIS_DESDE) < 0 ? COSTO_ENVIO : BigDecimal.ZERO;
        BigDecimal total = neto.add(envio).setScale(2, RoundingMode.HALF_UP);
        BigDecimal igv = total.multiply(IGV).divide(CIEN_DIECIOCHO, 2, RoundingMode.HALF_UP);
        return new Calculo(
            subtotal.setScale(2, RoundingMode.HALF_UP),
            descuento.setScale(2, RoundingMode.HALF_UP),
            envio,
            igv,
            total,
            cupon,
            mensaje,
            lineas
        );
    }

    /** Devuelve null si el cupon es valido; si no, el motivo. */
    private String validarCupon(Cupon c, Cliente cliente, BigDecimal subtotal) {
        LocalDate hoy = LocalDate.now(LIMA);
        if (c == null || !Boolean.TRUE.equals(c.getActivo())) return "El cupón no existe o no está activo";
        if (hoy.isBefore(c.getFechaInicio()) || hoy.isAfter(c.getFechaFin())) return "El cupón no está vigente";
        if (c.getMontoMinimo() != null && subtotal.compareTo(c.getMontoMinimo()) < 0) {
            return "El cupón requiere una compra mínima de S/ " + c.getMontoMinimo().setScale(2, RoundingMode.HALF_UP);
        }
        List<EstadoPedido> noCuentan = List.of(EstadoPedido.EXPIRADO, EstadoPedido.ANULADO, EstadoPedido.CANCELADO);
        if (c.getUsosMaximos() != null) {
            Long usos = em
                .createQuery("select count(p) from Pedido p where p.cupon = :c and p.estado not in :e", Long.class)
                .setParameter("c", c)
                .setParameter("e", noCuentan)
                .getSingleResult();
            if (usos >= c.getUsosMaximos()) return "El cupón ya alcanzó su límite de usos";
        }
        if (c.getUsosPorCliente() != null) {
            Long usos = em
                .createQuery("select count(p) from Pedido p where p.cupon = :c and p.cliente = :cl and p.estado not in :e", Long.class)
                .setParameter("c", c)
                .setParameter("cl", cliente)
                .setParameter("e", noCuentan)
                .getSingleResult();
            if (usos >= c.getUsosPorCliente()) return "Ya usaste este cupón el máximo de veces permitido";
        }
        return null;
    }

    private String siguienteNroPedido() {
        String prefijo = "PED-" + LocalDate.now(LIMA).getYear() + "-";
        String max = em
            .createQuery("select max(p.nroPedido) from Pedido p where p.nroPedido like :pre", String.class)
            .setParameter("pre", prefijo + "%")
            .getSingleResult();
        int n = max == null ? 0 : Integer.parseInt(max.substring(prefijo.length()));
        return prefijo + String.format("%06d", n + 1);
    }

    /* ======================================================================
     * MIS PEDIDOS
     * ====================================================================== */

    @Transactional(readOnly = true)
    public List<PedidoResumen> misPedidos() {
        Cliente c = clienteActual();
        Map<Long, Long> items = new HashMap<>();
        for (Object[] r : em
            .createQuery(
                "select d.pedido.id, sum(d.cantidad) from PedidoDetalle d where d.pedido.cliente = :c group by d.pedido.id",
                Object[].class
            )
            .setParameter("c", c)
            .getResultList()) {
            items.put((Long) r[0], ((Number) r[1]).longValue());
        }
        return em
            .createQuery("select p from Pedido p where p.cliente = :c order by p.fechaPedido desc", Pedido.class)
            .setParameter("c", c)
            .getResultList()
            .stream()
            .map(p ->
                new PedidoResumen(
                    p.getId(),
                    p.getNroPedido(),
                    p.getEstado().name(),
                    p.getTipoEntrega().name(),
                    p.getFechaPedido(),
                    p.getFechaExpiracion(),
                    p.getTotal(),
                    items.getOrDefault(p.getId(), 0L)
                )
            )
            .toList();
    }

    @Transactional(readOnly = true)
    public PedidoDetalleDto miPedido(Long id) {
        pedidoDe(clienteActual(), id, false);
        return pedido(id);
    }

    private PedidoDetalleDto pedido(Long id) {
        Pedido p = em.find(Pedido.class, id);
        List<PedidoDetalle> dets = detalles(p);
        Map<Long, String> img = primerasImagenes(
            dets
                .stream()
                .map(d -> d.getProducto().getId())
                .toList()
        );
        List<LineaPedido> lineas = dets
            .stream()
            .map(d ->
                new LineaPedido(
                    d.getSkuProducto(),
                    d.getNombreProducto(),
                    d.getProducto().getSlug(),
                    img.get(d.getProducto().getId()),
                    d.getCantidad(),
                    d.getTipoPrecio().name(),
                    d.getPrecioUnitario(),
                    d.getDescuento(),
                    d.getSubtotal()
                )
            )
            .toList();
        List<EstadoItem> historial = em
            .createQuery(
                "select h from PedidoEstadoHistorial h where h.pedido = :p order by h.createdDate, h.id",
                PedidoEstadoHistorial.class
            )
            .setParameter("p", p)
            .getResultList()
            .stream()
            .map(h -> new EstadoItem(h.getEstado().name(), h.getComentario(), h.getCreatedDate()))
            .toList();
        List<PagoItem> pagos = em
            .createQuery("select g from Pago g where g.pedido = :p order by g.fechaPago, g.id", Pago.class)
            .setParameter("p", p)
            .getResultList()
            .stream()
            .map(g ->
                new PagoItem(
                    g.getMetodo().name(),
                    g.getSubMetodo(),
                    g.getEstado().name(),
                    g.getMonto(),
                    g.getFechaPago(),
                    g.getNroTransaccion()
                )
            )
            .toList();
        List<ComprobanteItem> comprobantes = em
            .createQuery("select c from Comprobante c where c.pedido = :p order by c.fechaEmision, c.id", Comprobante.class)
            .setParameter("p", p)
            .getResultList()
            .stream()
            .map(c ->
                new ComprobanteItem(
                    c.getTipo().name(),
                    c.getSerie(),
                    c.getNumero(),
                    c.getEstadoSunat().name(),
                    c.getTotal(),
                    c.getFechaEmision(),
                    c.getReceptorTipoDocumento() + " " + c.getReceptorNroDocumento() + " - " + c.getReceptorNombre()
                )
            )
            .toList();
        EnvioItem envio = em
            .createQuery("select e from Envio e where e.pedido = :p", Envio.class)
            .setParameter("p", p)
            .getResultStream()
            .findFirst()
            .map(e -> new EnvioItem(e.getCourier(), e.getTracking(), e.getEstado().name(), e.getDireccionEntrega(), e.getFechaEstimada()))
            .orElse(null);
        return new PedidoDetalleDto(
            p.getId(),
            p.getNroPedido(),
            p.getEstado().name(),
            p.getTipoEntrega().name(),
            p.getTipoComprobante().name(),
            p.getFechaPedido(),
            p.getFechaExpiracion(),
            p.getSubtotal(),
            p.getDescuento(),
            p.getCostoEnvio(),
            p.getIgv(),
            p.getTotal(),
            p.getCupon() == null ? null : p.getCupon().getCodigo(),
            p.getTiendaRecojo() == null ? null : p.getTiendaRecojo().getNombre() + " - " + p.getTiendaRecojo().getDireccion(),
            p.getObservaciones(),
            lineas,
            historial,
            pagos,
            comprobantes,
            envio
        );
    }

    /* ======================================================================
     * AUXILIARES
     * ====================================================================== */

    private Cliente clienteActual() {
        String login = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new VentaException("Debes iniciar sesión"));
        return em
            .createQuery("select c from Cliente c join fetch c.persona join fetch c.user u where u.login = :login", Cliente.class)
            .setParameter("login", login)
            .getResultStream()
            .findFirst()
            .orElseThrow(() -> new VentaException("Tu usuario no tiene un perfil de cliente"));
    }

    private Carrito carritoActual() {
        return carritoDe(clienteActual());
    }

    private Carrito carritoDe(Cliente c) {
        return em
            .createQuery("select k from Carrito k where k.cliente = :c", Carrito.class)
            .setParameter("c", c)
            .getResultStream()
            .findFirst()
            .orElseGet(() -> {
                Carrito k = new Carrito();
                k.setCliente(c);
                em.persist(k);
                return k;
            });
    }

    private List<CarritoItem> itemsSeleccionados(Carrito carrito) {
        return em
            .createQuery(
                "select i from CarritoItem i join fetch i.producto where i.carrito = :c and i.estado = :e and i.seleccionado = true order by i.id",
                CarritoItem.class
            )
            .setParameter("c", carrito)
            .setParameter("e", EstadoItemCarrito.ACTIVO)
            .getResultList();
    }

    private Pedido pedidoDe(Cliente c, Long id, boolean bloquear) {
        Pedido p = bloquear ? em.find(Pedido.class, id, LockModeType.PESSIMISTIC_WRITE) : em.find(Pedido.class, id);
        if (p == null || !p.getCliente().getId().equals(c.getId())) throw new VentaException("Pedido no encontrado");
        return p;
    }

    private List<PedidoDetalle> detalles(Pedido p) {
        return em
            .createQuery("select d from PedidoDetalle d join fetch d.producto where d.pedido = :p order by d.id", PedidoDetalle.class)
            .setParameter("p", p)
            .getResultList();
    }

    private DatoFacturacion datoFacturacionDe(Cliente c, Long id) {
        return Optional.ofNullable(id)
            .map(x -> em.find(DatoFacturacion.class, x))
            .filter(d -> d.getCliente().getId().equals(c.getId()) && Boolean.TRUE.equals(d.getActivo()))
            .orElseThrow(() -> new VentaException("Para factura elige un RUC"));
    }

    /** Almacen para despachos: la tienda activa que no atiende recojo (o la primera activa). */
    private Tienda almacen() {
        return em
            .createQuery("select t from Tienda t where t.activo = true order by t.permiteRecojo asc, t.id asc", Tienda.class)
            .setMaxResults(1)
            .getResultStream()
            .findFirst()
            .orElse(null);
    }

    private Optional<Inventario> inventario(Producto p, Tienda t, boolean bloquear) {
        TypedQuery<Inventario> q = em
            .createQuery("select i from Inventario i where i.producto = :p and i.tienda = :t", Inventario.class)
            .setParameter("p", p)
            .setParameter("t", t);
        if (bloquear) q.setLockMode(LockModeType.PESSIMISTIC_WRITE);
        return q.getResultStream().findFirst();
    }

    private int disponible(Inventario i) {
        return Math.max(0, i.getStock() - i.getStockReservado());
    }

    private void historial(Pedido p, EstadoPedido estado, String comentario) {
        PedidoEstadoHistorial h = new PedidoEstadoHistorial();
        h.setEstado(estado);
        h.setComentario(recortar(comentario, 500));
        h.setPedido(p);
        em.persist(h);
    }

    private Optional<DocumentoIdentidad> documentoPrincipal(Persona p) {
        return em
            .createQuery(
                "select d from DocumentoIdentidad d where d.persona = :p and d.vigente = true order by d.principal desc, d.id",
                DocumentoIdentidad.class
            )
            .setParameter("p", p)
            .setMaxResults(1)
            .getResultStream()
            .findFirst();
    }

    private String perfilCelular(Persona p) {
        return em
            .createQuery(
                "select m.valor from MedioContacto m where m.persona = :p and m.tipo = :t and m.activo = true order by m.principal desc",
                String.class
            )
            .setParameter("p", p)
            .setParameter("t", TipoMedioContacto.CELULAR)
            .setMaxResults(1)
            .getResultStream()
            .findFirst()
            .orElse(null);
    }

    private static BigDecimal precio(Producto p) {
        return p.getPrecioOferta() != null ? p.getPrecioOferta() : p.getPrecioLista();
    }

    private static Double redondear1(Double v) {
        return v == null ? null : Math.round(v * 10) / 10.0;
    }

    private static String vacioANull(String s) {
        return s == null || s.isBlank() ? null : s.trim();
    }

    private static String recortar(String s, int max) {
        return s == null || s.length() <= max ? s : s.substring(0, max);
    }

    private static String primeraPalabra(String s, int max) {
        return recortar(s.trim().split("\\s+")[0], max);
    }
}
