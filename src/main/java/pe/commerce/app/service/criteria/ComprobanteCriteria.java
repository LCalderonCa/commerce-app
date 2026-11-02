package pe.commerce.app.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import pe.commerce.app.domain.enumeration.EstadoSunat;
import pe.commerce.app.domain.enumeration.Moneda;
import pe.commerce.app.domain.enumeration.TipoComprobante;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link pe.commerce.app.domain.Comprobante} entity. This class is used
 * in {@link pe.commerce.app.web.rest.ComprobanteResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /comprobantes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ComprobanteCriteria implements Serializable, Criteria {

    /**
     * Class for filtering TipoComprobante
     */
    public static class TipoComprobanteFilter extends Filter<TipoComprobante> {

        public TipoComprobanteFilter() {}

        public TipoComprobanteFilter(TipoComprobanteFilter filter) {
            super(filter);
        }

        @Override
        public TipoComprobanteFilter copy() {
            return new TipoComprobanteFilter(this);
        }
    }

    /**
     * Class for filtering Moneda
     */
    public static class MonedaFilter extends Filter<Moneda> {

        public MonedaFilter() {}

        public MonedaFilter(MonedaFilter filter) {
            super(filter);
        }

        @Override
        public MonedaFilter copy() {
            return new MonedaFilter(this);
        }
    }

    /**
     * Class for filtering EstadoSunat
     */
    public static class EstadoSunatFilter extends Filter<EstadoSunat> {

        public EstadoSunatFilter() {}

        public EstadoSunatFilter(EstadoSunatFilter filter) {
            super(filter);
        }

        @Override
        public EstadoSunatFilter copy() {
            return new EstadoSunatFilter(this);
        }
    }

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private TipoComprobanteFilter tipo;

    private StringFilter serie;

    private IntegerFilter numero;

    private InstantFilter fechaEmision;

    private StringFilter receptorTipoDocumento;

    private StringFilter receptorNroDocumento;

    private StringFilter receptorNombre;

    private StringFilter receptorDireccion;

    private MonedaFilter moneda;

    private BigDecimalFilter opGravada;

    private BigDecimalFilter igv;

    private BigDecimalFilter total;

    private EstadoSunatFilter estadoSunat;

    private StringFilter xmlUrl;

    private StringFilter pdfUrl;

    private StringFilter createdBy;

    private InstantFilter createdDate;

    private StringFilter lastModifiedBy;

    private InstantFilter lastModifiedDate;

    private LongFilter comprobanteReferenciaId;

    private LongFilter pedidoId;

    private Boolean distinct;

    public ComprobanteCriteria() {}

    public ComprobanteCriteria(ComprobanteCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.tipo = other.optionalTipo().map(TipoComprobanteFilter::copy).orElse(null);
        this.serie = other.optionalSerie().map(StringFilter::copy).orElse(null);
        this.numero = other.optionalNumero().map(IntegerFilter::copy).orElse(null);
        this.fechaEmision = other.optionalFechaEmision().map(InstantFilter::copy).orElse(null);
        this.receptorTipoDocumento = other.optionalReceptorTipoDocumento().map(StringFilter::copy).orElse(null);
        this.receptorNroDocumento = other.optionalReceptorNroDocumento().map(StringFilter::copy).orElse(null);
        this.receptorNombre = other.optionalReceptorNombre().map(StringFilter::copy).orElse(null);
        this.receptorDireccion = other.optionalReceptorDireccion().map(StringFilter::copy).orElse(null);
        this.moneda = other.optionalMoneda().map(MonedaFilter::copy).orElse(null);
        this.opGravada = other.optionalOpGravada().map(BigDecimalFilter::copy).orElse(null);
        this.igv = other.optionalIgv().map(BigDecimalFilter::copy).orElse(null);
        this.total = other.optionalTotal().map(BigDecimalFilter::copy).orElse(null);
        this.estadoSunat = other.optionalEstadoSunat().map(EstadoSunatFilter::copy).orElse(null);
        this.xmlUrl = other.optionalXmlUrl().map(StringFilter::copy).orElse(null);
        this.pdfUrl = other.optionalPdfUrl().map(StringFilter::copy).orElse(null);
        this.createdBy = other.optionalCreatedBy().map(StringFilter::copy).orElse(null);
        this.createdDate = other.optionalCreatedDate().map(InstantFilter::copy).orElse(null);
        this.lastModifiedBy = other.optionalLastModifiedBy().map(StringFilter::copy).orElse(null);
        this.lastModifiedDate = other.optionalLastModifiedDate().map(InstantFilter::copy).orElse(null);
        this.comprobanteReferenciaId = other.optionalComprobanteReferenciaId().map(LongFilter::copy).orElse(null);
        this.pedidoId = other.optionalPedidoId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ComprobanteCriteria copy() {
        return new ComprobanteCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public TipoComprobanteFilter getTipo() {
        return tipo;
    }

    public Optional<TipoComprobanteFilter> optionalTipo() {
        return Optional.ofNullable(tipo);
    }

    public TipoComprobanteFilter tipo() {
        if (tipo == null) {
            setTipo(new TipoComprobanteFilter());
        }
        return tipo;
    }

    public void setTipo(TipoComprobanteFilter tipo) {
        this.tipo = tipo;
    }

    public StringFilter getSerie() {
        return serie;
    }

    public Optional<StringFilter> optionalSerie() {
        return Optional.ofNullable(serie);
    }

    public StringFilter serie() {
        if (serie == null) {
            setSerie(new StringFilter());
        }
        return serie;
    }

    public void setSerie(StringFilter serie) {
        this.serie = serie;
    }

    public IntegerFilter getNumero() {
        return numero;
    }

    public Optional<IntegerFilter> optionalNumero() {
        return Optional.ofNullable(numero);
    }

    public IntegerFilter numero() {
        if (numero == null) {
            setNumero(new IntegerFilter());
        }
        return numero;
    }

    public void setNumero(IntegerFilter numero) {
        this.numero = numero;
    }

    public InstantFilter getFechaEmision() {
        return fechaEmision;
    }

    public Optional<InstantFilter> optionalFechaEmision() {
        return Optional.ofNullable(fechaEmision);
    }

    public InstantFilter fechaEmision() {
        if (fechaEmision == null) {
            setFechaEmision(new InstantFilter());
        }
        return fechaEmision;
    }

    public void setFechaEmision(InstantFilter fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public StringFilter getReceptorTipoDocumento() {
        return receptorTipoDocumento;
    }

    public Optional<StringFilter> optionalReceptorTipoDocumento() {
        return Optional.ofNullable(receptorTipoDocumento);
    }

    public StringFilter receptorTipoDocumento() {
        if (receptorTipoDocumento == null) {
            setReceptorTipoDocumento(new StringFilter());
        }
        return receptorTipoDocumento;
    }

    public void setReceptorTipoDocumento(StringFilter receptorTipoDocumento) {
        this.receptorTipoDocumento = receptorTipoDocumento;
    }

    public StringFilter getReceptorNroDocumento() {
        return receptorNroDocumento;
    }

    public Optional<StringFilter> optionalReceptorNroDocumento() {
        return Optional.ofNullable(receptorNroDocumento);
    }

    public StringFilter receptorNroDocumento() {
        if (receptorNroDocumento == null) {
            setReceptorNroDocumento(new StringFilter());
        }
        return receptorNroDocumento;
    }

    public void setReceptorNroDocumento(StringFilter receptorNroDocumento) {
        this.receptorNroDocumento = receptorNroDocumento;
    }

    public StringFilter getReceptorNombre() {
        return receptorNombre;
    }

    public Optional<StringFilter> optionalReceptorNombre() {
        return Optional.ofNullable(receptorNombre);
    }

    public StringFilter receptorNombre() {
        if (receptorNombre == null) {
            setReceptorNombre(new StringFilter());
        }
        return receptorNombre;
    }

    public void setReceptorNombre(StringFilter receptorNombre) {
        this.receptorNombre = receptorNombre;
    }

    public StringFilter getReceptorDireccion() {
        return receptorDireccion;
    }

    public Optional<StringFilter> optionalReceptorDireccion() {
        return Optional.ofNullable(receptorDireccion);
    }

    public StringFilter receptorDireccion() {
        if (receptorDireccion == null) {
            setReceptorDireccion(new StringFilter());
        }
        return receptorDireccion;
    }

    public void setReceptorDireccion(StringFilter receptorDireccion) {
        this.receptorDireccion = receptorDireccion;
    }

    public MonedaFilter getMoneda() {
        return moneda;
    }

    public Optional<MonedaFilter> optionalMoneda() {
        return Optional.ofNullable(moneda);
    }

    public MonedaFilter moneda() {
        if (moneda == null) {
            setMoneda(new MonedaFilter());
        }
        return moneda;
    }

    public void setMoneda(MonedaFilter moneda) {
        this.moneda = moneda;
    }

    public BigDecimalFilter getOpGravada() {
        return opGravada;
    }

    public Optional<BigDecimalFilter> optionalOpGravada() {
        return Optional.ofNullable(opGravada);
    }

    public BigDecimalFilter opGravada() {
        if (opGravada == null) {
            setOpGravada(new BigDecimalFilter());
        }
        return opGravada;
    }

    public void setOpGravada(BigDecimalFilter opGravada) {
        this.opGravada = opGravada;
    }

    public BigDecimalFilter getIgv() {
        return igv;
    }

    public Optional<BigDecimalFilter> optionalIgv() {
        return Optional.ofNullable(igv);
    }

    public BigDecimalFilter igv() {
        if (igv == null) {
            setIgv(new BigDecimalFilter());
        }
        return igv;
    }

    public void setIgv(BigDecimalFilter igv) {
        this.igv = igv;
    }

    public BigDecimalFilter getTotal() {
        return total;
    }

    public Optional<BigDecimalFilter> optionalTotal() {
        return Optional.ofNullable(total);
    }

    public BigDecimalFilter total() {
        if (total == null) {
            setTotal(new BigDecimalFilter());
        }
        return total;
    }

    public void setTotal(BigDecimalFilter total) {
        this.total = total;
    }

    public EstadoSunatFilter getEstadoSunat() {
        return estadoSunat;
    }

    public Optional<EstadoSunatFilter> optionalEstadoSunat() {
        return Optional.ofNullable(estadoSunat);
    }

    public EstadoSunatFilter estadoSunat() {
        if (estadoSunat == null) {
            setEstadoSunat(new EstadoSunatFilter());
        }
        return estadoSunat;
    }

    public void setEstadoSunat(EstadoSunatFilter estadoSunat) {
        this.estadoSunat = estadoSunat;
    }

    public StringFilter getXmlUrl() {
        return xmlUrl;
    }

    public Optional<StringFilter> optionalXmlUrl() {
        return Optional.ofNullable(xmlUrl);
    }

    public StringFilter xmlUrl() {
        if (xmlUrl == null) {
            setXmlUrl(new StringFilter());
        }
        return xmlUrl;
    }

    public void setXmlUrl(StringFilter xmlUrl) {
        this.xmlUrl = xmlUrl;
    }

    public StringFilter getPdfUrl() {
        return pdfUrl;
    }

    public Optional<StringFilter> optionalPdfUrl() {
        return Optional.ofNullable(pdfUrl);
    }

    public StringFilter pdfUrl() {
        if (pdfUrl == null) {
            setPdfUrl(new StringFilter());
        }
        return pdfUrl;
    }

    public void setPdfUrl(StringFilter pdfUrl) {
        this.pdfUrl = pdfUrl;
    }

    public StringFilter getCreatedBy() {
        return createdBy;
    }

    public Optional<StringFilter> optionalCreatedBy() {
        return Optional.ofNullable(createdBy);
    }

    public StringFilter createdBy() {
        if (createdBy == null) {
            setCreatedBy(new StringFilter());
        }
        return createdBy;
    }

    public void setCreatedBy(StringFilter createdBy) {
        this.createdBy = createdBy;
    }

    public InstantFilter getCreatedDate() {
        return createdDate;
    }

    public Optional<InstantFilter> optionalCreatedDate() {
        return Optional.ofNullable(createdDate);
    }

    public InstantFilter createdDate() {
        if (createdDate == null) {
            setCreatedDate(new InstantFilter());
        }
        return createdDate;
    }

    public void setCreatedDate(InstantFilter createdDate) {
        this.createdDate = createdDate;
    }

    public StringFilter getLastModifiedBy() {
        return lastModifiedBy;
    }

    public Optional<StringFilter> optionalLastModifiedBy() {
        return Optional.ofNullable(lastModifiedBy);
    }

    public StringFilter lastModifiedBy() {
        if (lastModifiedBy == null) {
            setLastModifiedBy(new StringFilter());
        }
        return lastModifiedBy;
    }

    public void setLastModifiedBy(StringFilter lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public InstantFilter getLastModifiedDate() {
        return lastModifiedDate;
    }

    public Optional<InstantFilter> optionalLastModifiedDate() {
        return Optional.ofNullable(lastModifiedDate);
    }

    public InstantFilter lastModifiedDate() {
        if (lastModifiedDate == null) {
            setLastModifiedDate(new InstantFilter());
        }
        return lastModifiedDate;
    }

    public void setLastModifiedDate(InstantFilter lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public LongFilter getComprobanteReferenciaId() {
        return comprobanteReferenciaId;
    }

    public Optional<LongFilter> optionalComprobanteReferenciaId() {
        return Optional.ofNullable(comprobanteReferenciaId);
    }

    public LongFilter comprobanteReferenciaId() {
        if (comprobanteReferenciaId == null) {
            setComprobanteReferenciaId(new LongFilter());
        }
        return comprobanteReferenciaId;
    }

    public void setComprobanteReferenciaId(LongFilter comprobanteReferenciaId) {
        this.comprobanteReferenciaId = comprobanteReferenciaId;
    }

    public LongFilter getPedidoId() {
        return pedidoId;
    }

    public Optional<LongFilter> optionalPedidoId() {
        return Optional.ofNullable(pedidoId);
    }

    public LongFilter pedidoId() {
        if (pedidoId == null) {
            setPedidoId(new LongFilter());
        }
        return pedidoId;
    }

    public void setPedidoId(LongFilter pedidoId) {
        this.pedidoId = pedidoId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ComprobanteCriteria that = (ComprobanteCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(tipo, that.tipo) &&
            Objects.equals(serie, that.serie) &&
            Objects.equals(numero, that.numero) &&
            Objects.equals(fechaEmision, that.fechaEmision) &&
            Objects.equals(receptorTipoDocumento, that.receptorTipoDocumento) &&
            Objects.equals(receptorNroDocumento, that.receptorNroDocumento) &&
            Objects.equals(receptorNombre, that.receptorNombre) &&
            Objects.equals(receptorDireccion, that.receptorDireccion) &&
            Objects.equals(moneda, that.moneda) &&
            Objects.equals(opGravada, that.opGravada) &&
            Objects.equals(igv, that.igv) &&
            Objects.equals(total, that.total) &&
            Objects.equals(estadoSunat, that.estadoSunat) &&
            Objects.equals(xmlUrl, that.xmlUrl) &&
            Objects.equals(pdfUrl, that.pdfUrl) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(lastModifiedBy, that.lastModifiedBy) &&
            Objects.equals(lastModifiedDate, that.lastModifiedDate) &&
            Objects.equals(comprobanteReferenciaId, that.comprobanteReferenciaId) &&
            Objects.equals(pedidoId, that.pedidoId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            tipo,
            serie,
            numero,
            fechaEmision,
            receptorTipoDocumento,
            receptorNroDocumento,
            receptorNombre,
            receptorDireccion,
            moneda,
            opGravada,
            igv,
            total,
            estadoSunat,
            xmlUrl,
            pdfUrl,
            createdBy,
            createdDate,
            lastModifiedBy,
            lastModifiedDate,
            comprobanteReferenciaId,
            pedidoId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ComprobanteCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalTipo().map(f -> "tipo=" + f + ", ").orElse("") +
            optionalSerie().map(f -> "serie=" + f + ", ").orElse("") +
            optionalNumero().map(f -> "numero=" + f + ", ").orElse("") +
            optionalFechaEmision().map(f -> "fechaEmision=" + f + ", ").orElse("") +
            optionalReceptorTipoDocumento().map(f -> "receptorTipoDocumento=" + f + ", ").orElse("") +
            optionalReceptorNroDocumento().map(f -> "receptorNroDocumento=" + f + ", ").orElse("") +
            optionalReceptorNombre().map(f -> "receptorNombre=" + f + ", ").orElse("") +
            optionalReceptorDireccion().map(f -> "receptorDireccion=" + f + ", ").orElse("") +
            optionalMoneda().map(f -> "moneda=" + f + ", ").orElse("") +
            optionalOpGravada().map(f -> "opGravada=" + f + ", ").orElse("") +
            optionalIgv().map(f -> "igv=" + f + ", ").orElse("") +
            optionalTotal().map(f -> "total=" + f + ", ").orElse("") +
            optionalEstadoSunat().map(f -> "estadoSunat=" + f + ", ").orElse("") +
            optionalXmlUrl().map(f -> "xmlUrl=" + f + ", ").orElse("") +
            optionalPdfUrl().map(f -> "pdfUrl=" + f + ", ").orElse("") +
            optionalCreatedBy().map(f -> "createdBy=" + f + ", ").orElse("") +
            optionalCreatedDate().map(f -> "createdDate=" + f + ", ").orElse("") +
            optionalLastModifiedBy().map(f -> "lastModifiedBy=" + f + ", ").orElse("") +
            optionalLastModifiedDate().map(f -> "lastModifiedDate=" + f + ", ").orElse("") +
            optionalComprobanteReferenciaId().map(f -> "comprobanteReferenciaId=" + f + ", ").orElse("") +
            optionalPedidoId().map(f -> "pedidoId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
