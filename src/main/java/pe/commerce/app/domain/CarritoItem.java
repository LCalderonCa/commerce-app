package pe.commerce.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import pe.commerce.app.domain.enumeration.EstadoItemCarrito;

/**
 * Ítem del carrito. El precio se lee siempre del Producto (vigente).
 * seleccionado = entra al checkout; los no seleccionados quedan ACTIVO.
 * Al confirmar el pedido, los ítems copiados pasan a CONVERTIDO.
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "carrito_item")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CarritoItem implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Min(value = 1)
    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @NotNull
    @Column(name = "seleccionado", nullable = false)
    private Boolean seleccionado;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoItemCarrito estado;

    @Size(max = 50)
    @CreatedBy
    @Column(name = "created_by", length = 50, updatable = false)
    private String createdBy;

    @CreatedDate
    @Column(name = "created_date", updatable = false)
    private Instant createdDate;

    @Size(max = 50)
    @LastModifiedBy
    @Column(name = "last_modified_by", length = 50)
    private String lastModifiedBy;

    @LastModifiedDate
    @Column(name = "last_modified_date")
    private Instant lastModifiedDate;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "imageneses", "inventarioses", "resenases", "categoria", "marca" }, allowSetters = true)
    private Producto producto;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "cliente", "itemses" }, allowSetters = true)
    private Carrito carrito;

    @JsonIgnoreProperties(value = { "carritoItem", "producto", "pedido" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "carritoItem")
    private PedidoDetalle pedidoDetalle;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CarritoItem id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCantidad() {
        return this.cantidad;
    }

    public CarritoItem cantidad(Integer cantidad) {
        this.setCantidad(cantidad);
        return this;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Boolean getSeleccionado() {
        return this.seleccionado;
    }

    public CarritoItem seleccionado(Boolean seleccionado) {
        this.setSeleccionado(seleccionado);
        return this;
    }

    public void setSeleccionado(Boolean seleccionado) {
        this.seleccionado = seleccionado;
    }

    public EstadoItemCarrito getEstado() {
        return this.estado;
    }

    public CarritoItem estado(EstadoItemCarrito estado) {
        this.setEstado(estado);
        return this;
    }

    public void setEstado(EstadoItemCarrito estado) {
        this.estado = estado;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public CarritoItem createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public CarritoItem createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public CarritoItem lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public CarritoItem lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Producto getProducto() {
        return this.producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public CarritoItem producto(Producto producto) {
        this.setProducto(producto);
        return this;
    }

    public Carrito getCarrito() {
        return this.carrito;
    }

    public void setCarrito(Carrito carrito) {
        this.carrito = carrito;
    }

    public CarritoItem carrito(Carrito carrito) {
        this.setCarrito(carrito);
        return this;
    }

    public PedidoDetalle getPedidoDetalle() {
        return this.pedidoDetalle;
    }

    public void setPedidoDetalle(PedidoDetalle pedidoDetalle) {
        if (this.pedidoDetalle != null) {
            this.pedidoDetalle.setCarritoItem(null);
        }
        if (pedidoDetalle != null) {
            pedidoDetalle.setCarritoItem(this);
        }
        this.pedidoDetalle = pedidoDetalle;
    }

    public CarritoItem pedidoDetalle(PedidoDetalle pedidoDetalle) {
        this.setPedidoDetalle(pedidoDetalle);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CarritoItem)) {
            return false;
        }
        return getId() != null && getId().equals(((CarritoItem) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CarritoItem{" +
            "id=" + getId() +
            ", cantidad=" + getCantidad() +
            ", seleccionado='" + getSeleccionado() + "'" +
            ", estado='" + getEstado() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
