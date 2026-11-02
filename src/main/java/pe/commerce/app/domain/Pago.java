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
import pe.commerce.app.domain.enumeration.EstadoPago;
import pe.commerce.app.domain.enumeration.MetodoPago;
import pe.commerce.app.domain.enumeration.Moneda;

/**
 * Resultado de un intento de pago (puede haber varios por pedido).
 * subMetodo: YAPE, PLIN (billetera) / VISA, MASTERCARD (tarjeta).
 * pasarela: código/nombre que devuelve la pasarela (Niubiz, Izipay, Mercado Pago...).
 */
@Entity
@Table(name = "pago")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Pago implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "metodo", nullable = false)
    private MetodoPago metodo;

    @Size(max = 30)
    @Column(name = "sub_metodo", length = 30)
    private String subMetodo;

    @Size(max = 50)
    @Column(name = "pasarela", length = 50)
    private String pasarela;

    @Size(max = 100)
    @Column(name = "nro_transaccion", length = 100)
    private String nroTransaccion;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "moneda", nullable = false)
    private Moneda moneda;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "monto", precision = 21, scale = 2, nullable = false)
    private BigDecimal monto;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoPago estado;

    @NotNull
    @Column(name = "fecha_pago", nullable = false)
    private Instant fechaPago;

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

    public Pago id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MetodoPago getMetodo() {
        return this.metodo;
    }

    public Pago metodo(MetodoPago metodo) {
        this.setMetodo(metodo);
        return this;
    }

    public void setMetodo(MetodoPago metodo) {
        this.metodo = metodo;
    }

    public String getSubMetodo() {
        return this.subMetodo;
    }

    public Pago subMetodo(String subMetodo) {
        this.setSubMetodo(subMetodo);
        return this;
    }

    public void setSubMetodo(String subMetodo) {
        this.subMetodo = subMetodo;
    }

    public String getPasarela() {
        return this.pasarela;
    }

    public Pago pasarela(String pasarela) {
        this.setPasarela(pasarela);
        return this;
    }

    public void setPasarela(String pasarela) {
        this.pasarela = pasarela;
    }

    public String getNroTransaccion() {
        return this.nroTransaccion;
    }

    public Pago nroTransaccion(String nroTransaccion) {
        this.setNroTransaccion(nroTransaccion);
        return this;
    }

    public void setNroTransaccion(String nroTransaccion) {
        this.nroTransaccion = nroTransaccion;
    }

    public Moneda getMoneda() {
        return this.moneda;
    }

    public Pago moneda(Moneda moneda) {
        this.setMoneda(moneda);
        return this;
    }

    public void setMoneda(Moneda moneda) {
        this.moneda = moneda;
    }

    public BigDecimal getMonto() {
        return this.monto;
    }

    public Pago monto(BigDecimal monto) {
        this.setMonto(monto);
        return this;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public EstadoPago getEstado() {
        return this.estado;
    }

    public Pago estado(EstadoPago estado) {
        this.setEstado(estado);
        return this;
    }

    public void setEstado(EstadoPago estado) {
        this.estado = estado;
    }

    public Instant getFechaPago() {
        return this.fechaPago;
    }

    public Pago fechaPago(Instant fechaPago) {
        this.setFechaPago(fechaPago);
        return this;
    }

    public void setFechaPago(Instant fechaPago) {
        this.fechaPago = fechaPago;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public Pago createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public Pago createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public Pago lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public Pago lastModifiedDate(Instant lastModifiedDate) {
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

    public Pago pedido(Pedido pedido) {
        this.setPedido(pedido);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Pago)) {
            return false;
        }
        return getId() != null && getId().equals(((Pago) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Pago{" +
            "id=" + getId() +
            ", metodo='" + getMetodo() + "'" +
            ", subMetodo='" + getSubMetodo() + "'" +
            ", pasarela='" + getPasarela() + "'" +
            ", nroTransaccion='" + getNroTransaccion() + "'" +
            ", moneda='" + getMoneda() + "'" +
            ", monto=" + getMonto() +
            ", estado='" + getEstado() + "'" +
            ", fechaPago='" + getFechaPago() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
