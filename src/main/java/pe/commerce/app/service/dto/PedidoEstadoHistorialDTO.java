package pe.commerce.app.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import pe.commerce.app.domain.enumeration.EstadoPedido;

/**
 * A DTO for the {@link pe.commerce.app.domain.PedidoEstadoHistorial} entity.
 */
@Schema(description = "Trazabilidad de estados del pedido.\nUn registro por cambio; la cronología sale de ordenar por createdDate.")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PedidoEstadoHistorialDTO implements Serializable {

    private Long id;

    @NotNull
    private EstadoPedido estado;

    @Size(max = 500)
    private String comentario;

    @Size(max = 50)
    private String createdBy;

    private Instant createdDate;

    @Size(max = 50)
    private String lastModifiedBy;

    private Instant lastModifiedDate;

    @NotNull
    private PedidoDTO pedido;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public void setEstado(EstadoPedido estado) {
        this.estado = estado;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
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
        if (!(o instanceof PedidoEstadoHistorialDTO)) {
            return false;
        }

        PedidoEstadoHistorialDTO pedidoEstadoHistorialDTO = (PedidoEstadoHistorialDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, pedidoEstadoHistorialDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PedidoEstadoHistorialDTO{" +
            "id=" + getId() +
            ", estado='" + getEstado() + "'" +
            ", comentario='" + getComentario() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", pedido=" + getPedido() +
            "}";
    }
}
