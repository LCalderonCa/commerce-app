package pe.commerce.app.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import pe.commerce.app.domain.enumeration.EstadoSunat;
import pe.commerce.app.domain.enumeration.Moneda;
import pe.commerce.app.domain.enumeration.TipoComprobante;

/**
 * A DTO for the {@link pe.commerce.app.domain.Comprobante} entity.
 */
@Schema(
    description = "Comprobante electrónico SUNAT.\nLos datos del receptor se copian al emitir (no cambian si luego\nel cliente actualiza su documento o su RUC), por eso no hay FK\na DocumentoIdentidad ni a DatoFacturacion.\ncomprobanteReferencia: solo para NOTA_CREDITO (boleta/factura que modifica)."
)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ComprobanteDTO implements Serializable {

    private Long id;

    @NotNull
    private TipoComprobante tipo;

    @NotNull
    @Size(min = 4, max = 4)
    private String serie;

    @NotNull
    @Min(value = 1)
    private Integer numero;

    @NotNull
    private Instant fechaEmision;

    @NotNull
    @Size(max = 10)
    private String receptorTipoDocumento;

    @NotNull
    @Size(max = 15)
    private String receptorNroDocumento;

    @NotNull
    @Size(max = 200)
    private String receptorNombre;

    @Size(max = 250)
    private String receptorDireccion;

    @NotNull
    private Moneda moneda;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal opGravada;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal igv;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal total;

    @NotNull
    private EstadoSunat estadoSunat;

    @Size(max = 500)
    private String xmlUrl;

    @Size(max = 500)
    private String pdfUrl;

    @Size(max = 50)
    private String createdBy;

    private Instant createdDate;

    @Size(max = 50)
    private String lastModifiedBy;

    private Instant lastModifiedDate;

    private ComprobanteDTO comprobanteReferencia;

    @NotNull
    private PedidoDTO pedido;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoComprobante getTipo() {
        return tipo;
    }

    public void setTipo(TipoComprobante tipo) {
        this.tipo = tipo;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public Instant getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Instant fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public String getReceptorTipoDocumento() {
        return receptorTipoDocumento;
    }

    public void setReceptorTipoDocumento(String receptorTipoDocumento) {
        this.receptorTipoDocumento = receptorTipoDocumento;
    }

    public String getReceptorNroDocumento() {
        return receptorNroDocumento;
    }

    public void setReceptorNroDocumento(String receptorNroDocumento) {
        this.receptorNroDocumento = receptorNroDocumento;
    }

    public String getReceptorNombre() {
        return receptorNombre;
    }

    public void setReceptorNombre(String receptorNombre) {
        this.receptorNombre = receptorNombre;
    }

    public String getReceptorDireccion() {
        return receptorDireccion;
    }

    public void setReceptorDireccion(String receptorDireccion) {
        this.receptorDireccion = receptorDireccion;
    }

    public Moneda getMoneda() {
        return moneda;
    }

    public void setMoneda(Moneda moneda) {
        this.moneda = moneda;
    }

    public BigDecimal getOpGravada() {
        return opGravada;
    }

    public void setOpGravada(BigDecimal opGravada) {
        this.opGravada = opGravada;
    }

    public BigDecimal getIgv() {
        return igv;
    }

    public void setIgv(BigDecimal igv) {
        this.igv = igv;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public EstadoSunat getEstadoSunat() {
        return estadoSunat;
    }

    public void setEstadoSunat(EstadoSunat estadoSunat) {
        this.estadoSunat = estadoSunat;
    }

    public String getXmlUrl() {
        return xmlUrl;
    }

    public void setXmlUrl(String xmlUrl) {
        this.xmlUrl = xmlUrl;
    }

    public String getPdfUrl() {
        return pdfUrl;
    }

    public void setPdfUrl(String pdfUrl) {
        this.pdfUrl = pdfUrl;
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

    public ComprobanteDTO getComprobanteReferencia() {
        return comprobanteReferencia;
    }

    public void setComprobanteReferencia(ComprobanteDTO comprobanteReferencia) {
        this.comprobanteReferencia = comprobanteReferencia;
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
        if (!(o instanceof ComprobanteDTO)) {
            return false;
        }

        ComprobanteDTO comprobanteDTO = (ComprobanteDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, comprobanteDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ComprobanteDTO{" +
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
            ", comprobanteReferencia=" + getComprobanteReferencia() +
            ", pedido=" + getPedido() +
            "}";
    }
}
