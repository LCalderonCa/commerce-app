package pe.commerce.app.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import pe.commerce.app.domain.enumeration.EstadoItemCarrito;

/**
 * A DTO for the {@link pe.commerce.app.domain.CarritoItem} entity.
 */
@Schema(
    description = "Ítem del carrito. El precio se lee siempre del Producto (vigente).\nseleccionado = entra al checkout; los no seleccionados quedan ACTIVO.\nAl confirmar el pedido, los ítems copiados pasan a CONVERTIDO."
)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CarritoItemDTO implements Serializable {

    private Long id;

    @NotNull
    @Min(value = 1)
    private Integer cantidad;

    @NotNull
    private Boolean seleccionado;

    @NotNull
    private EstadoItemCarrito estado;

    @Size(max = 50)
    private String createdBy;

    private Instant createdDate;

    @Size(max = 50)
    private String lastModifiedBy;

    private Instant lastModifiedDate;

    @NotNull
    private ProductoDTO producto;

    @NotNull
    private CarritoDTO carrito;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Boolean getSeleccionado() {
        return seleccionado;
    }

    public void setSeleccionado(Boolean seleccionado) {
        this.seleccionado = seleccionado;
    }

    public EstadoItemCarrito getEstado() {
        return estado;
    }

    public void setEstado(EstadoItemCarrito estado) {
        this.estado = estado;
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

    public ProductoDTO getProducto() {
        return producto;
    }

    public void setProducto(ProductoDTO producto) {
        this.producto = producto;
    }

    public CarritoDTO getCarrito() {
        return carrito;
    }

    public void setCarrito(CarritoDTO carrito) {
        this.carrito = carrito;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CarritoItemDTO)) {
            return false;
        }

        CarritoItemDTO carritoItemDTO = (CarritoItemDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, carritoItemDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CarritoItemDTO{" +
            "id=" + getId() +
            ", cantidad=" + getCantidad() +
            ", seleccionado='" + getSeleccionado() + "'" +
            ", estado='" + getEstado() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", producto=" + getProducto() +
            ", carrito=" + getCarrito() +
            "}";
    }
}
