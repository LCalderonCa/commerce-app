package pe.commerce.app.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import pe.commerce.app.domain.enumeration.TipoPrecio;

/**
 * A DTO for the {@link pe.commerce.app.domain.PedidoDetalle} entity.
 */
@Schema(
    description = "Línea del pedido: copia inmutable con precio congelado.\nprecioUnitario = precioLista del producto al momento de la compra.\ndescuento      = precioLista - precioOferta (0 si tipoPrecio = LISTA).\nsubtotal       = (precioUnitario - descuento) * cantidad.\nEl cupón NO se prorratea aquí; va en Pedido.descuento."
)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PedidoDetalleDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 30)
    private String skuProducto;

    @NotNull
    @Size(max = 200)
    private String nombreProducto;

    @NotNull
    @Min(value = 1)
    private Integer cantidad;

    @NotNull
    private TipoPrecio tipoPrecio;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal precioUnitario;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal descuento;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal subtotal;

    @Size(max = 50)
    private String createdBy;

    private Instant createdDate;

    @Size(max = 50)
    private String lastModifiedBy;

    private Instant lastModifiedDate;

    private CarritoItemDTO carritoItem;

    @NotNull
    private ProductoDTO producto;

    @NotNull
    private PedidoDTO pedido;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSkuProducto() {
        return skuProducto;
    }

    public void setSkuProducto(String skuProducto) {
        this.skuProducto = skuProducto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public TipoPrecio getTipoPrecio() {
        return tipoPrecio;
    }

    public void setTipoPrecio(TipoPrecio tipoPrecio) {
        this.tipoPrecio = tipoPrecio;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public BigDecimal getDescuento() {
        return descuento;
    }

    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public CarritoItemDTO getCarritoItem() {
        return carritoItem;
    }

    public void setCarritoItem(CarritoItemDTO carritoItem) {
        this.carritoItem = carritoItem;
    }

    public ProductoDTO getProducto() {
        return producto;
    }

    public void setProducto(ProductoDTO producto) {
        this.producto = producto;
    }

    public PedidoDTO getPedido() {
        return pedido;
    }

    public void setPedido(PedidoDTO pedido) {
        this.pedido = pedido;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PedidoDetalleDTO)) {
            return false;
        }

        PedidoDetalleDTO pedidoDetalleDTO = (PedidoDetalleDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, pedidoDetalleDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PedidoDetalleDTO{" +
            "id=" + getId() +
            ", skuProducto='" + getSkuProducto() + "'" +
            ", nombreProducto='" + getNombreProducto() + "'" +
            ", cantidad=" + getCantidad() +
            ", tipoPrecio='" + getTipoPrecio() + "'" +
            ", precioUnitario=" + getPrecioUnitario() +
            ", descuento=" + getDescuento() +
            ", subtotal=" + getSubtotal() +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", carritoItem=" + getCarritoItem() +
            ", producto=" + getProducto() +
            ", pedido=" + getPedido() +
            "}";
    }
}
