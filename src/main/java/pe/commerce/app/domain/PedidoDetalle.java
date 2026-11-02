package pe.commerce.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import pe.commerce.app.domain.enumeration.TipoPrecio;

/**
 * Línea del pedido: copia inmutable con precio congelado.
 * precioUnitario = precioLista del producto al momento de la compra.
 * descuento      = precioLista - precioOferta (0 si tipoPrecio = LISTA).
 * subtotal       = (precioUnitario - descuento) * cantidad.
 * El cupón NO se prorratea aquí; va en Pedido.descuento.
 */
@Entity
@Table(name = "pedido_detalle")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PedidoDetalle implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 30)
    @Column(name = "sku_producto", length = 30, nullable = false)
    private String skuProducto;

    @NotNull
    @Size(max = 200)
    @Column(name = "nombre_producto", length = 200, nullable = false)
    private String nombreProducto;

    @NotNull
    @Min(value = 1)
    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_precio", nullable = false)
    private TipoPrecio tipoPrecio;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "precio_unitario", precision = 21, scale = 2, nullable = false)
    private BigDecimal precioUnitario;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "descuento", precision = 21, scale = 2, nullable = false)
    private BigDecimal descuento;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "subtotal", precision = 21, scale = 2, nullable = false)
    private BigDecimal subtotal;

    @Size(max = 50)
    @Column(name = "created_by", length = 50)
    private String createdBy;

    @Column(name = "created_date")
    private Instant createdDate;

    @Size(max = 50)
    @Column(name = "last_modified_by", length = 50)
    private String lastModifiedBy;

    @Column(name = "last_modified_date")
    private Instant lastModifiedDate;

    @JsonIgnoreProperties(value = { "producto", "carrito", "pedidoDetalle" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private CarritoItem carritoItem;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "imageneses", "inventarioses", "resenases", "categoria", "marca" }, allowSetters = true)
    private Producto producto;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = {
            "detalleses",
            "historialEstadoses",
            "pagoses",
            "comprobanteses",
            "cupon",
            "tiendaRecojo",
            "atendidoPor",
            "envio",
            "cliente",
        },
        allowSetters = true
    )
    private Pedido pedido;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PedidoDetalle id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSkuProducto() {
        return this.skuProducto;
    }

    public PedidoDetalle skuProducto(String skuProducto) {
        this.setSkuProducto(skuProducto);
        return this;
    }

    public void setSkuProducto(String skuProducto) {
        this.skuProducto = skuProducto;
    }

    public String getNombreProducto() {
        return this.nombreProducto;
    }

    public PedidoDetalle nombreProducto(String nombreProducto) {
        this.setNombreProducto(nombreProducto);
        return this;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public Integer getCantidad() {
        return this.cantidad;
    }

    public PedidoDetalle cantidad(Integer cantidad) {
        this.setCantidad(cantidad);
        return this;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public TipoPrecio getTipoPrecio() {
        return this.tipoPrecio;
    }

    public PedidoDetalle tipoPrecio(TipoPrecio tipoPrecio) {
        this.setTipoPrecio(tipoPrecio);
        return this;
    }

    public void setTipoPrecio(TipoPrecio tipoPrecio) {
        this.tipoPrecio = tipoPrecio;
    }

    public BigDecimal getPrecioUnitario() {
        return this.precioUnitario;
    }

    public PedidoDetalle precioUnitario(BigDecimal precioUnitario) {
        this.setPrecioUnitario(precioUnitario);
        return this;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public BigDecimal getDescuento() {
        return this.descuento;
    }

    public PedidoDetalle descuento(BigDecimal descuento) {
        this.setDescuento(descuento);
        return this;
    }

    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }

    public BigDecimal getSubtotal() {
        return this.subtotal;
    }

    public PedidoDetalle subtotal(BigDecimal subtotal) {
        this.setSubtotal(subtotal);
        return this;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public PedidoDetalle createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public PedidoDetalle createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public PedidoDetalle lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public PedidoDetalle lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public CarritoItem getCarritoItem() {
        return this.carritoItem;
    }

    public void setCarritoItem(CarritoItem carritoItem) {
        this.carritoItem = carritoItem;
    }

    public PedidoDetalle carritoItem(CarritoItem carritoItem) {
        this.setCarritoItem(carritoItem);
        return this;
    }

    public Producto getProducto() {
        return this.producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public PedidoDetalle producto(Producto producto) {
        this.setProducto(producto);
        return this;
    }

    public Pedido getPedido() {
        return this.pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public PedidoDetalle pedido(Pedido pedido) {
        this.setPedido(pedido);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PedidoDetalle)) {
            return false;
        }
        return getId() != null && getId().equals(((PedidoDetalle) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PedidoDetalle{" +
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
            "}";
    }
}
