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
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import pe.commerce.app.domain.enumeration.EstadoSunat;
import pe.commerce.app.domain.enumeration.Moneda;
import pe.commerce.app.domain.enumeration.TipoComprobante;

/**
 * Comprobante electrónico SUNAT.
 * Los datos del receptor se copian al emitir (no cambian si luego
 * el cliente actualiza su documento o su RUC), por eso no hay FK
 * a DocumentoIdentidad ni a DatoFacturacion.
 * comprobanteReferencia: solo para NOTA_CREDITO (boleta/factura que modifica).
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "comprobante")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Comprobante implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private TipoComprobante tipo;

    @NotNull
    @Size(min = 4, max = 4)
    @Column(name = "serie", length = 4, nullable = false)
    private String serie;

    @NotNull
    @Min(value = 1)
    @Column(name = "numero", nullable = false)
    private Integer numero;

    @NotNull
    @Column(name = "fecha_emision", nullable = false)
    private Instant fechaEmision;

    @NotNull
    @Size(max = 10)
    @Column(name = "receptor_tipo_documento", length = 10, nullable = false)
    private String receptorTipoDocumento;

    @NotNull
    @Size(max = 15)
    @Column(name = "receptor_nro_documento", length = 15, nullable = false)
    private String receptorNroDocumento;

    @NotNull
    @Size(max = 200)
    @Column(name = "receptor_nombre", length = 200, nullable = false)
    private String receptorNombre;

    @Size(max = 250)
    @Column(name = "receptor_direccion", length = 250)
    private String receptorDireccion;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "moneda", nullable = false)
    private Moneda moneda;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "op_gravada", precision = 21, scale = 2, nullable = false)
    private BigDecimal opGravada;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "igv", precision = 21, scale = 2, nullable = false)
    private BigDecimal igv;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "total", precision = 21, scale = 2, nullable = false)
    private BigDecimal total;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_sunat", nullable = false)
    private EstadoSunat estadoSunat;

    @Size(max = 500)
    @Column(name = "xml_url", length = 500)
    private String xmlUrl;

    @Size(max = 500)
    @Column(name = "pdf_url", length = 500)
    private String pdfUrl;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "comprobanteReferencia", "pedido" }, allowSetters = true)
    private Comprobante comprobanteReferencia;

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

    public Comprobante id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoComprobante getTipo() {
        return this.tipo;
    }

    public Comprobante tipo(TipoComprobante tipo) {
        this.setTipo(tipo);
        return this;
    }

    public void setTipo(TipoComprobante tipo) {
        this.tipo = tipo;
    }

    public String getSerie() {
        return this.serie;
    }

    public Comprobante serie(String serie) {
        this.setSerie(serie);
        return this;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public Integer getNumero() {
        return this.numero;
    }

    public Comprobante numero(Integer numero) {
        this.setNumero(numero);
        return this;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public Instant getFechaEmision() {
        return this.fechaEmision;
    }

    public Comprobante fechaEmision(Instant fechaEmision) {
        this.setFechaEmision(fechaEmision);
        return this;
    }

    public void setFechaEmision(Instant fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public String getReceptorTipoDocumento() {
        return this.receptorTipoDocumento;
    }

    public Comprobante receptorTipoDocumento(String receptorTipoDocumento) {
        this.setReceptorTipoDocumento(receptorTipoDocumento);
        return this;
    }

    public void setReceptorTipoDocumento(String receptorTipoDocumento) {
        this.receptorTipoDocumento = receptorTipoDocumento;
    }

    public String getReceptorNroDocumento() {
        return this.receptorNroDocumento;
    }

    public Comprobante receptorNroDocumento(String receptorNroDocumento) {
        this.setReceptorNroDocumento(receptorNroDocumento);
        return this;
    }

    public void setReceptorNroDocumento(String receptorNroDocumento) {
        this.receptorNroDocumento = receptorNroDocumento;
    }

    public String getReceptorNombre() {
        return this.receptorNombre;
    }

    public Comprobante receptorNombre(String receptorNombre) {
        this.setReceptorNombre(receptorNombre);
        return this;
    }

    public void setReceptorNombre(String receptorNombre) {
        this.receptorNombre = receptorNombre;
    }

    public String getReceptorDireccion() {
        return this.receptorDireccion;
    }

    public Comprobante receptorDireccion(String receptorDireccion) {
        this.setReceptorDireccion(receptorDireccion);
        return this;
    }

    public void setReceptorDireccion(String receptorDireccion) {
        this.receptorDireccion = receptorDireccion;
    }

    public Moneda getMoneda() {
        return this.moneda;
    }

    public Comprobante moneda(Moneda moneda) {
        this.setMoneda(moneda);
        return this;
    }

    public void setMoneda(Moneda moneda) {
        this.moneda = moneda;
    }

    public BigDecimal getOpGravada() {
        return this.opGravada;
    }

    public Comprobante opGravada(BigDecimal opGravada) {
        this.setOpGravada(opGravada);
        return this;
    }

    public void setOpGravada(BigDecimal opGravada) {
        this.opGravada = opGravada;
    }

    public BigDecimal getIgv() {
        return this.igv;
    }

    public Comprobante igv(BigDecimal igv) {
        this.setIgv(igv);
        return this;
    }

    public void setIgv(BigDecimal igv) {
        this.igv = igv;
    }

    public BigDecimal getTotal() {
        return this.total;
    }

    public Comprobante total(BigDecimal total) {
        this.setTotal(total);
        return this;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public EstadoSunat getEstadoSunat() {
        return this.estadoSunat;
    }

    public Comprobante estadoSunat(EstadoSunat estadoSunat) {
        this.setEstadoSunat(estadoSunat);
        return this;
    }

    public void setEstadoSunat(EstadoSunat estadoSunat) {
        this.estadoSunat = estadoSunat;
    }

    public String getXmlUrl() {
        return this.xmlUrl;
    }

    public Comprobante xmlUrl(String xmlUrl) {
        this.setXmlUrl(xmlUrl);
        return this;
    }

    public void setXmlUrl(String xmlUrl) {
        this.xmlUrl = xmlUrl;
    }

    public String getPdfUrl() {
        return this.pdfUrl;
    }

    public Comprobante pdfUrl(String pdfUrl) {
        this.setPdfUrl(pdfUrl);
        return this;
    }

    public void setPdfUrl(String pdfUrl) {
        this.pdfUrl = pdfUrl;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public Comprobante createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public Comprobante createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public Comprobante lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public Comprobante lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Comprobante getComprobanteReferencia() {
        return this.comprobanteReferencia;
    }

    public void setComprobanteReferencia(Comprobante comprobante) {
        this.comprobanteReferencia = comprobante;
    }

    public Comprobante comprobanteReferencia(Comprobante comprobante) {
        this.setComprobanteReferencia(comprobante);
        return this;
    }

    public Pedido getPedido() {
        return this.pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public Comprobante pedido(Pedido pedido) {
        this.setPedido(pedido);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Comprobante)) {
            return false;
        }
        return getId() != null && getId().equals(((Comprobante) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Comprobante{" +
            "id=" + getId() +
            ", tipo='" + getTipo() + "'" +
            ", serie='" + getSerie() + "'" +
            ", numero=" + getNumero() +
            ", fechaEmision='" + getFechaEmision() + "'" +
            ", receptorTipoDocumento='" + getReceptorTipoDocumento() + "'" +
            ", receptorNroDocumento='" + getReceptorNroDocumento() + "'" +
            ", receptorNombre='" + getReceptorNombre() + "'" +
            ", receptorDireccion='" + getReceptorDireccion() + "'" +
            ", moneda='" + getMoneda() + "'" +
            ", opGravada=" + getOpGravada() +
            ", igv=" + getIgv() +
            ", total=" + getTotal() +
            ", estadoSunat='" + getEstadoSunat() + "'" +
            ", xmlUrl='" + getXmlUrl() + "'" +
            ", pdfUrl='" + getPdfUrl() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
