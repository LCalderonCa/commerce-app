package pe.commerce.app.tienda;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pe.commerce.app.tienda.VentaDtos.*;

/**
 * API de la tienda para el cliente.
 * /api/tienda/publico/** es de acceso libre (catalogo y registro); el resto requiere token JWT.
 */
@RestController
@RequestMapping("/api/tienda")
public class VentaResource {

    private final VentaService service;

    public VentaResource(VentaService service) {
        this.service = service;
    }

    /* ------------------------------ publico ------------------------------ */

    @GetMapping("/publico/categorias")
    public List<CategoriaNodo> categorias() {
        return service.categorias();
    }

    @GetMapping("/publico/marcas")
    public List<MarcaItem> marcas() {
        return service.marcas();
    }

    @GetMapping("/publico/productos")
    public Pagina<ProductoCard> productos(
        @RequestParam(required = false) String categoria,
        @RequestParam(required = false) Long marca,
        @RequestParam(required = false) String q,
        @RequestParam(defaultValue = "false") boolean ofertas,
        @RequestParam(defaultValue = "recientes") String orden,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "12") int size
    ) {
        return service.productos(categoria, marca, q, ofertas, orden, page, size);
    }

    @GetMapping("/publico/productos/{slug}")
    public ProductoDetalle producto(@PathVariable String slug) {
        return service.producto(slug);
    }

    @GetMapping("/publico/tiendas")
    public List<TiendaItem> tiendas() {
        return service.tiendasRecojo();
    }

    @GetMapping("/publico/ubigeos")
    public List<UbigeoItem> ubigeos() {
        return service.ubigeos();
    }

    @PostMapping("/publico/registro")
    public ResponseEntity<Perfil> registro(@Valid @RequestBody RegistroRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.registrar(req));
    }

    /* ------------------------------ cuenta ------------------------------ */

    @GetMapping("/perfil")
    public Perfil perfil() {
        return service.perfil();
    }

    @GetMapping("/direcciones")
    public List<DireccionItem> direcciones() {
        return service.direcciones();
    }

    @PostMapping("/direcciones")
    public DireccionItem agregarDireccion(@Valid @RequestBody DireccionRequest req) {
        return service.agregarDireccion(req);
    }

    @GetMapping("/facturacion")
    public List<FacturacionItem> facturacion() {
        return service.datosFacturacion();
    }

    @PostMapping("/facturacion")
    public FacturacionItem agregarFacturacion(@Valid @RequestBody FacturacionRequest req) {
        return service.agregarDatoFacturacion(req);
    }

    /* ------------------------------ carrito ------------------------------ */

    @GetMapping("/carrito")
    public CarritoDto carrito() {
        return service.carrito();
    }

    @PostMapping("/carrito/items")
    public CarritoDto agregarItem(@Valid @RequestBody AgregarItemRequest req) {
        return service.agregarItem(req);
    }

    @PutMapping("/carrito/items/{id}")
    public CarritoDto actualizarItem(@PathVariable Long id, @Valid @RequestBody ActualizarItemRequest req) {
        return service.actualizarItem(id, req);
    }

    @DeleteMapping("/carrito/items/{id}")
    public CarritoDto eliminarItem(@PathVariable Long id) {
        return service.eliminarItem(id);
    }

    /* ------------------------------ checkout y pedidos ------------------------------ */

    @PostMapping("/checkout/resumen")
    public ResumenCheckout resumen(@Valid @RequestBody CheckoutRequest req) {
        return service.resumen(req);
    }

    @PostMapping("/checkout")
    public ResponseEntity<PedidoDetalleDto> checkout(@Valid @RequestBody CheckoutRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.checkout(req));
    }

    @PostMapping("/pedidos/{id}/pago")
    public PedidoDetalleDto pagar(@PathVariable Long id, @Valid @RequestBody PagoRequest req) {
        return service.pagar(id, req);
    }

    @GetMapping("/pedidos")
    public List<PedidoResumen> pedidos() {
        return service.misPedidos();
    }

    @GetMapping("/pedidos/{id}")
    public PedidoDetalleDto pedido(@PathVariable Long id) {
        return service.miPedido(id);
    }

    /* ------------------------------ errores de negocio ------------------------------ */

    @ExceptionHandler(VentaException.class)
    public ResponseEntity<Mensaje> reglaDeNegocio(VentaException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new Mensaje(ex.getMessage()));
    }
}
