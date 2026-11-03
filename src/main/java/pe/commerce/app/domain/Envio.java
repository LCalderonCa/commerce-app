package pe.commerce.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import pe.commerce.app.domain.enumeration.EstadoEnvio;

/**
 * Entrega por delivery; guarda copia de la dirección usada
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "envio")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Envio implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Size(max = 80)
    @Column(name = "courier", length = 80)
    private String courier;

    @Size(max = 100)
    @Column(name = "tracking", length = 100)
    private String tracking;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoEnvio estado;

    @NotNull
    @Size(max = 200)
    @Column(name = "direccion_entrega", length = 200, nullable = false)
    private String direccionEntrega;

    @NotNull
    @Size(max = 6)
    @Column(name = "ubigeo_entrega", length = 6, nullable = false)
    private String ubigeoEntrega;

    @Size(max = 200)
    @Column(name = "referencia_entrega", length = 200)
    private String referenciaEntrega;

    @NotNull
    @Size(max = 100)
    @Column(name = "nombre_receptor", length = 100, nullable = false)
    private String nombreReceptor;

    @NotNull
    @Size(max = 15)
    @Column(name = "telefono_receptor", length = 15, nullable = false)
    private String telefonoReceptor;

    @Column(name = "fecha_estimada")
    private LocalDate fechaEstimada;

    @Column(name = "fecha_entrega")
    private Instant fechaEntrega;

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
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private Pedido pedido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "ubigeo", "cliente" }, allowSetters = true)
    private Direccion direccion;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Envio id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCourier() {
        return this.courier;
    }

    public Envio courier(String courier) {
        this.setCourier(courier);
        return this;
    }

    public void setCourier(String courier) {
        this.courier = courier;
    }

    public String getTracking() {
        return this.tracking;
    }

    public Envio tracking(String tracking) {
        this.setTracking(tracking);
        return this;
    }

    public void setTracking(String tracking) {
        this.tracking = tracking;
    }

    public EstadoEnvio getEstado() {
        return this.estado;
    }

    public Envio estado(EstadoEnvio estado) {
        this.setEstado(estado);
        return this;
    }

    public void setEstado(EstadoEnvio estado) {
        this.estado = estado;
    }

    public String getDireccionEntrega() {
        return this.direccionEntrega;
    }

    public Envio direccionEntrega(String direccionEntrega) {
        this.setDireccionEntrega(direccionEntrega);
        return this;
    }

    public void setDireccionEntrega(String direccionEntrega) {
        this.direccionEntrega = direccionEntrega;
    }

    public String getUbigeoEntrega() {
        return this.ubigeoEntrega;
    }

    public Envio ubigeoEntrega(String ubigeoEntrega) {
        this.setUbigeoEntrega(ubigeoEntrega);
        return this;
    }

    public void setUbigeoEntrega(String ubigeoEntrega) {
        this.ubigeoEntrega = ubigeoEntrega;
    }

    public String getReferenciaEntrega() {
        return this.referenciaEntrega;
    }

    public Envio referenciaEntrega(String referenciaEntrega) {
        this.setReferenciaEntrega(referenciaEntrega);
        return this;
    }

    public void setReferenciaEntrega(String referenciaEntrega) {
        this.referenciaEntrega = referenciaEntrega;
    }

    public String getNombreReceptor() {
        return this.nombreReceptor;
    }

    public Envio nombreReceptor(String nombreReceptor) {
        this.setNombreReceptor(nombreReceptor);
        return this;
    }

    public void setNombreReceptor(String nombreReceptor) {
        this.nombreReceptor = nombreReceptor;
    }

    public String getTelefonoReceptor() {
        return this.telefonoReceptor;
    }

    public Envio telefonoReceptor(String telefonoReceptor) {
        this.setTelefonoReceptor(telefonoReceptor);
        return this;
    }

    public void setTelefonoReceptor(String telefonoReceptor) {
        this.telefonoReceptor = telefonoReceptor;
    }

    public LocalDate getFechaEstimada() {
        return this.fechaEstimada;
    }

    public Envio fechaEstimada(LocalDate fechaEstimada) {
        this.setFechaEstimada(fechaEstimada);
        return this;
    }

    public void setFechaEstimada(LocalDate fechaEstimada) {
        this.fechaEstimada = fechaEstimada;
    }

    public Instant getFechaEntrega() {
        return this.fechaEntrega;
    }

    public Envio fechaEntrega(Instant fechaEntrega) {
        this.setFechaEntrega(fechaEntrega);
        return this;
    }

    public void setFechaEntrega(Instant fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public Envio createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public Envio createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public Envio lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public Envio lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Pedido getPedido() {
        return this.pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public Envio pedido(Pedido pedido) {
        this.setPedido(pedido);
        return this;
    }

    public Direccion getDireccion() {
        return this.direccion;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }

    public Envio direccion(Direccion direccion) {
        this.setDireccion(direccion);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Envio)) {
            return false;
        }
        return getId() != null && getId().equals(((Envio) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Envio{" +
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
            "}";
    }
}
