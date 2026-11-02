package pe.commerce.app.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link pe.commerce.app.domain.Inventario} entity.
 */
@Schema(
    description = "Stock por producto y tienda. stock y stockMinimo los alimenta el\nservicio de logística y stock vía API. Este servicio solo maneja\nstockReservado (pedidos PENDIENTE_PAGO)."
)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InventarioDTO implements Serializable {

    private Long id;

    @NotNull
    @Min(value = 0)
    private Integer stock;

    @NotNull
    @Min(value = 0)
    private Integer stockReservado;

    @Min(value = 0)
    private Integer stockMinimo;

    @Size(max = 50)
    private String createdBy;

    private Instant createdDate;

    @Size(max = 50)
    private String lastModifiedBy;

    private Instant lastModifiedDate;

    @NotNull
    private ProductoDTO producto;

    @NotNull
    private TiendaDTO tienda;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getStockReservado() {
        return stockReservado;
    }

    public void setStockReservado(Integer stockReservado) {
        this.stockReservado = stockReservado;
    }

    public Integer getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(Integer stockMinimo) {
        this.stockMinimo = stockMinimo;
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

    public TiendaDTO getTienda() {
        return tienda;
    }

    public void setTienda(TiendaDTO tienda) {
        this.tienda = tienda;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InventarioDTO)) {
            return false;
        }

        InventarioDTO inventarioDTO = (InventarioDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, inventarioDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InventarioDTO{" +
            "id=" + getId() +
            ", stock=" + getStock() +
            ", stockReservado=" + getStockReservado() +
            ", stockMinimo=" + getStockMinimo() +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", producto=" + getProducto() +
            ", tienda=" + getTienda() +
            "}";
    }
}
