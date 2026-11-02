package pe.commerce.app.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;
import pe.commerce.app.domain.enumeration.EstadoEnvio;

/**
 * A DTO for the {@link pe.commerce.app.domain.Envio} entity.
 */
@Schema(description = "Entrega por delivery; guarda copia de la dirección usada")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EnvioDTO implements Serializable {

    private Long id;

    @Size(max = 80)
    private String courier;

    @Size(max = 100)
    private String tracking;

    @NotNull
    private EstadoEnvio estado;

    @NotNull
    @Size(max = 200)
    private String direccionEntrega;

    @NotNull
    @Size(max = 6)
    private String ubigeoEntrega;

    @Size(max = 200)
    private String referenciaEntrega;

    @NotNull
    @Size(max = 100)
    private String nombreReceptor;

    @NotNull
    @Size(max = 15)
    private String telefonoReceptor;

    private LocalDate fechaEstimada;

    private Instant fechaEntrega;

    @Size(max = 50)
    private String createdBy;

    private Instant createdDate;

    @Size(max = 50)
    private String lastModifiedBy;

    private Instant lastModifiedDate;

    @NotNull
    private PedidoDTO pedido;

    private DireccionDTO direccion;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCourier() {
        return courier;
    }

    public void setCourier(String courier) {
        this.courier = courier;
    }

    public String getTracking() {
        return tracking;
    }

    public void setTracking(String tracking) {
        this.tracking = tracking;
    }

    public EstadoEnvio getEstado() {
        return estado;
    }

    public void setEstado(EstadoEnvio estado) {
        this.estado = estado;
    }

    public String getDireccionEntrega() {
        return direccionEntrega;
    }

    public void setDireccionEntrega(String direccionEntrega) {
        this.direccionEntrega = direccionEntrega;
    }

    public String getUbigeoEntrega() {
        return ubigeoEntrega;
    }

    public void setUbigeoEntrega(String ubigeoEntrega) {
        this.ubigeoEntrega = ubigeoEntrega;
    }

    public String getReferenciaEntrega() {
        return referenciaEntrega;
    }

    public void setReferenciaEntrega(String referenciaEntrega) {
        this.referenciaEntrega = referenciaEntrega;
    }

    public String getNombreReceptor() {
        return nombreReceptor;
    }

    public void setNombreReceptor(String nombreReceptor) {
        this.nombreReceptor = nombreReceptor;
    }

    public String getTelefonoReceptor() {
        return telefonoReceptor;
    }

    public void setTelefonoReceptor(String telefonoReceptor) {
        this.telefonoReceptor = telefonoReceptor;
    }

    public LocalDate getFechaEstimada() {
        return fechaEstimada;
    }

    public void setFechaEstimada(LocalDate fechaEstimada) {
        this.fechaEstimada = fechaEstimada;
    }

    public Instant getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(Instant fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
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

    public DireccionDTO getDireccion() {
        return direccion;
    }

    public void setDireccion(DireccionDTO direccion) {
        this.direccion = direccion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EnvioDTO)) {
            return false;
        }

        EnvioDTO envioDTO = (EnvioDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, envioDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EnvioDTO{" +
            "id=" + getId() +
            ", courier='" + getCourier() + "'" +
            ", tracking='" + getTracking() + "'" +
            ", estado='" + getEstado() + "'" +
            ", direccionEntrega='" + getDireccionEntrega() + "'" +
            ", ubigeoEntrega='" + getUbigeoEntrega() + "'" +
            ", referenciaEntrega='" + getReferenciaEntrega() + "'" +
            ", nombreReceptor='" + getNombreReceptor() + "'" +
            ", telefonoReceptor='" + getTelefonoReceptor() + "'" +
            ", fechaEstimada='" + getFechaEstimada() + "'" +
            ", fechaEntrega='" + getFechaEntrega() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", pedido=" + getPedido() +
            ", direccion=" + getDireccion() +
            "}";
    }
}
