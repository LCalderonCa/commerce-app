package pe.commerce.app.tienda;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import pe.commerce.app.domain.enumeration.MetodoPago;
import pe.commerce.app.domain.enumeration.TipoComprobantePedido;
import pe.commerce.app.domain.enumeration.TipoDocumento;
import pe.commerce.app.domain.enumeration.TipoEntrega;

/** Contratos (request/response) de la API de tienda para el cliente. */
public final class VentaDtos {

    private VentaDtos() {}

    /* ------------------------------ catalogo ------------------------------ */

    public record CategoriaNodo(Long id, String nombre, String slug, List<CategoriaNodo> hijas) {}

    public record MarcaItem(Long id, String nombre) {}

    public record ProductoCard(
        Long id,
        String sku,
        String nombre,
        String slug,
        String marca,
        String categoria,
        BigDecimal precioLista,
        BigDecimal precioOferta,
        String imagen,
        Double rating,
        Long resenas
    ) {}

    public record Pagina<T>(List<T> items, long total, int page, int size) {}

    public record StockTienda(Long tiendaId, String tienda, int disponible) {}

    public record ResenaItem(String cliente, Integer calificacion, String comentario, Instant fecha, boolean compraVerificada) {}

    public record ProductoDetalle(
        Long id,
        String sku,
        String nombre,
        String slug,
        String modelo,
        String marca,
        String categoria,
        String categoriaSlug,
        String descripcion,
        String especificaciones,
        BigDecimal precioLista,
        BigDecimal precioOferta,
        Integer garantiaMeses,
        List<String> imagenes,
        int stockOnline,
        List<StockTienda> tiendas,
        Double rating,
        List<ResenaItem> resenas
    ) {}

    public record TiendaItem(Long id, String nombre, String direccion, String distrito) {}

    public record UbigeoItem(Long id, String codigo, String departamento, String provincia, String distrito) {}

    /* ------------------------------ cuenta ------------------------------ */

    public record RegistroRequest(
        @NotBlank @Email @Size(max = 50) String email,
        @NotBlank @Size(min = 6, max = 100) String password,
        @NotBlank @Size(max = 100) String nombres,
        @NotBlank @Size(max = 100) String apellidos,
        @NotNull TipoDocumento tipoDocumento,
        @NotBlank @Size(max = 15) String numeroDocumento,
        @NotBlank @Pattern(regexp = "^9\\d{8}$", message = "El celular debe tener 9 dígitos y empezar con 9") String celular,
        boolean aceptaPublicidad
    ) {}

    public record Perfil(
        Long clienteId,
        String codigo,
        String nombres,
        String apellidos,
        String email,
        String tipoDocumento,
        String numeroDocumento,
        String celular
    ) {}

    public record DireccionItem(
        Long id,
        String alias,
        String direccion,
        String referencia,
        Long ubigeoId,
        String ubigeo,
        boolean predeterminada
    ) {}

    public record DireccionRequest(
        @NotBlank @Size(max = 30) String alias,
        @NotBlank @Size(max = 200) String direccion,
        @Size(max = 200) String referencia,
        @NotNull Long ubigeoId,
        boolean predeterminada
    ) {}

    public record FacturacionItem(Long id, String ruc, String razonSocial, String direccionFiscal, boolean predeterminado) {}

    public record FacturacionRequest(
        @NotBlank @Pattern(regexp = "^(10|20)[0-9]{9}$", message = "RUC inválido") String ruc,
        @NotBlank @Size(max = 200) String razonSocial,
        @NotBlank @Size(max = 250) String direccionFiscal,
        boolean predeterminado
    ) {}

    /* ------------------------------ carrito ------------------------------ */

    public record CarritoLinea(
        Long id,
        Long productoId,
        String sku,
        String nombre,
        String slug,
        String imagen,
        BigDecimal precioLista,
        BigDecimal precioOferta,
        BigDecimal precio,
        int cantidad,
        boolean seleccionado,
        String estado,
        int stockDisponible,
        BigDecimal subtotal
    ) {}

    public record CarritoDto(List<CarritoLinea> items, int cantidadSeleccionada, BigDecimal subtotalSeleccionado) {}

    public record AgregarItemRequest(@NotNull Long productoId, @Min(1) int cantidad) {}

    /** Campos opcionales: solo se cambia lo que viene. estado: ACTIVO o GUARDADO. */
    public record ActualizarItemRequest(@Min(1) Integer cantidad, Boolean seleccionado, String estado) {}

    /* ------------------------------ checkout ------------------------------ */

    public record CheckoutRequest(
        @NotNull TipoEntrega tipoEntrega,
        Long direccionId,
        Long tiendaId,
        @NotNull TipoComprobantePedido tipoComprobante,
        Long datoFacturacionId,
        @Size(max = 30) String cuponCodigo,
        @Size(max = 500) String observaciones
    ) {}

    public record LineaResumen(String sku, String nombre, int cantidad, BigDecimal precio, BigDecimal subtotal, String tipoPrecio) {}

    public record ResumenCheckout(
        List<LineaResumen> lineas,
        BigDecimal subtotal,
        BigDecimal descuento,
        String cupon,
        String mensajeCupon,
        BigDecimal costoEnvio,
        BigDecimal igv,
        BigDecimal total
    ) {}

    public record PagoRequest(@NotNull MetodoPago metodo, @Size(max = 30) String subMetodo, Long datoFacturacionId) {}

    /* ------------------------------ pedidos ------------------------------ */

    public record PedidoResumen(
        Long id,
        String nroPedido,
        String estado,
        String tipoEntrega,
        Instant fechaPedido,
        Instant fechaExpiracion,
        BigDecimal total,
        long items
    ) {}

    public record LineaPedido(
        String sku,
        String nombre,
        String slug,
        String imagen,
        int cantidad,
        String tipoPrecio,
        BigDecimal precioUnitario,
        BigDecimal descuento,
        BigDecimal subtotal
    ) {}

    public record EstadoItem(String estado, String comentario, Instant fecha) {}

    public record PagoItem(String metodo, String subMetodo, String estado, BigDecimal monto, Instant fecha, String nroTransaccion) {}

    public record ComprobanteItem(
        String tipo,
        String serie,
        Integer numero,
        String estadoSunat,
        BigDecimal total,
        Instant fecha,
        String receptor
    ) {}

    public record EnvioItem(String courier, String tracking, String estado, String direccion, LocalDate fechaEstimada) {}

    public record PedidoDetalleDto(
        Long id,
        String nroPedido,
        String estado,
        String tipoEntrega,
        String tipoComprobante,
        Instant fechaPedido,
        Instant fechaExpiracion,
        BigDecimal subtotal,
        BigDecimal descuento,
        BigDecimal costoEnvio,
        BigDecimal igv,
        BigDecimal total,
        String cupon,
        String tiendaRecojo,
        String observaciones,
        List<LineaPedido> lineas,
        List<EstadoItem> historial,
        List<PagoItem> pagos,
        List<ComprobanteItem> comprobantes,
        EnvioItem envio
    ) {}

    public record Mensaje(String mensaje) {}
}
