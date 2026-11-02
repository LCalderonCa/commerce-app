package pe.commerce.app.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import pe.commerce.app.domain.enumeration.EstadoPedido;
import pe.commerce.app.domain.enumeration.Moneda;
import pe.commerce.app.domain.enumeration.TipoComprobantePedido;
import pe.commerce.app.domain.enumeration.TipoEntrega;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link pe.commerce.app.domain.Pedido} entity. This class is used
 * in {@link pe.commerce.app.web.rest.PedidoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /pedidos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PedidoCriteria implements Serializable, Criteria {

    /**
     * Class for filtering TipoEntrega
     */
    public static class TipoEntregaFilter extends Filter<TipoEntrega> {

        public TipoEntregaFilter() {}

        public TipoEntregaFilter(TipoEntregaFilter filter) {
            super(filter);
        }

        @Override
        public TipoEntregaFilter copy() {
            return new TipoEntregaFilter(this);
        }
    }

    /**
     * Class for filtering TipoComprobantePedido
     */
    public static class TipoComprobantePedidoFilter extends Filter<TipoComprobantePedido> {

        public TipoComprobantePedidoFilter() {}

        public TipoComprobantePedidoFilter(TipoComprobantePedidoFilter filter) {
            super(filter);
        }

        @Override
        public TipoComprobantePedidoFilter copy() {
            return new TipoComprobantePedidoFilter(this);
        }
    }

    /**
     * Class for filtering EstadoPedido
     */
    public static class EstadoPedidoFilter extends Filter<EstadoPedido> {

        public EstadoPedidoFilter() {}

        public EstadoPedidoFilter(EstadoPedidoFilter filter) {
            super(filter);
        }

        @Override
        public EstadoPedidoFilter copy() {
            return new EstadoPedidoFilter(this);
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

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nroPedido;

    private TipoEntregaFilter tipoEntrega;

    private TipoComprobantePedidoFilter tipoComprobante;

    private EstadoPedidoFilter estado;

    private MonedaFilter moneda;

    private BigDecimalFilter subtotal;

    private BigDecimalFilter descuento;

    private BigDecimalFilter costoEnvio;

    private BigDecimalFilter igv;

    private BigDecimalFilter total;

    private InstantFilter fechaPedido;

    private InstantFilter fechaExpiracion;

    private StringFilter observaciones;

    private StringFilter createdBy;

    private InstantFilter createdDate;

    private StringFilter lastModifiedBy;

    private InstantFilter lastModifiedDate;

    private LongFilter detallesId;

    private LongFilter historialEstadosId;

    private LongFilter pagosId;

    private LongFilter comprobantesId;

    private LongFilter cuponId;

    private LongFilter tiendaRecojoId;

    private LongFilter atendidoPorId;

    private LongFilter envioId;

    private LongFilter clienteId;

    private Boolean distinct;

    public PedidoCriteria() {}

    public PedidoCriteria(PedidoCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.nroPedido = other.optionalNroPedido().map(StringFilter::copy).orElse(null);
        this.tipoEntrega = other.optionalTipoEntrega().map(TipoEntregaFilter::copy).orElse(null);
        this.tipoComprobante = other.optionalTipoComprobante().map(TipoComprobantePedidoFilter::copy).orElse(null);
        this.estado = other.optionalEstado().map(EstadoPedidoFilter::copy).orElse(null);
        this.moneda = other.optionalMoneda().map(MonedaFilter::copy).orElse(null);
        this.subtotal = other.optionalSubtotal().map(BigDecimalFilter::copy).orElse(null);
        this.descuento = other.optionalDescuento().map(BigDecimalFilter::copy).orElse(null);
        this.costoEnvio = other.optionalCostoEnvio().map(BigDecimalFilter::copy).orElse(null);
        this.igv = other.optionalIgv().map(BigDecimalFilter::copy).orElse(null);
        this.total = other.optionalTotal().map(BigDecimalFilter::copy).orElse(null);
        this.fechaPedido = other.optionalFechaPedido().map(InstantFilter::copy).orElse(null);
        this.fechaExpiracion = other.optionalFechaExpiracion().map(InstantFilter::copy).orElse(null);
        this.observaciones = other.optionalObservaciones().map(StringFilter::copy).orElse(null);
        this.createdBy = other.optionalCreatedBy().map(StringFilter::copy).orElse(null);
        this.createdDate = other.optionalCreatedDate().map(InstantFilter::copy).orElse(null);
        this.lastModifiedBy = other.optionalLastModifiedBy().map(StringFilter::copy).orElse(null);
        this.lastModifiedDate = other.optionalLastModifiedDate().map(InstantFilter::copy).orElse(null);
        this.detallesId = other.optionalDetallesId().map(LongFilter::copy).orElse(null);
        this.historialEstadosId = other.optionalHistorialEstadosId().map(LongFilter::copy).orElse(null);
        this.pagosId = other.optionalPagosId().map(LongFilter::copy).orElse(null);
        this.comprobantesId = other.optionalComprobantesId().map(LongFilter::copy).orElse(null);
        this.cuponId = other.optionalCuponId().map(LongFilter::copy).orElse(null);
        this.tiendaRecojoId = other.optionalTiendaRecojoId().map(LongFilter::copy).orElse(null);
        this.atendidoPorId = other.optionalAtendidoPorId().map(LongFilter::copy).orElse(null);
        this.envioId = other.optionalEnvioId().map(LongFilter::copy).orElse(null);
        this.clienteId = other.optionalClienteId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public PedidoCriteria copy() {
        return new PedidoCriteria(this);
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

    public StringFilter getNroPedido() {
        return nroPedido;
    }

    public Optional<StringFilter> optionalNroPedido() {
        return Optional.ofNullable(nroPedido);
    }

    public StringFilter nroPedido() {
        if (nroPedido == null) {
            setNroPedido(new StringFilter());
        }
        return nroPedido;
    }

    public void setNroPedido(StringFilter nroPedido) {
        this.nroPedido = nroPedido;
    }

    public TipoEntregaFilter getTipoEntrega() {
        return tipoEntrega;
    }

    public Optional<TipoEntregaFilter> optionalTipoEntrega() {
        return Optional.ofNullable(tipoEntrega);
    }

    public TipoEntregaFilter tipoEntrega() {
        if (tipoEntrega == null) {
            setTipoEntrega(new TipoEntregaFilter());
        }
        return tipoEntrega;
    }

    public void setTipoEntrega(TipoEntregaFilter tipoEntrega) {
        this.tipoEntrega = tipoEntrega;
    }

    public TipoComprobantePedidoFilter getTipoComprobante() {
        return tipoComprobante;
    }

    public Optional<TipoComprobantePedidoFilter> optionalTipoComprobante() {
        return Optional.ofNullable(tipoComprobante);
    }

    public TipoComprobantePedidoFilter tipoComprobante() {
        if (tipoComprobante == null) {
            setTipoComprobante(new TipoComprobantePedidoFilter());
        }
        return tipoComprobante;
    }

    public void setTipoComprobante(TipoComprobantePedidoFilter tipoComprobante) {
        this.tipoComprobante = tipoComprobante;
    }

    public EstadoPedidoFilter getEstado() {
        return estado;
    }

    public Optional<EstadoPedidoFilter> optionalEstado() {
        return Optional.ofNullable(estado);
    }

    public EstadoPedidoFilter estado() {
        if (estado == null) {
            setEstado(new EstadoPedidoFilter());
        }
        return estado;
    }

    public void setEstado(EstadoPedidoFilter estado) {
        this.estado = estado;
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

    public BigDecimalFilter getSubtotal() {
        return subtotal;
    }

    public Optional<BigDecimalFilter> optionalSubtotal() {
        return Optional.ofNullable(subtotal);
    }

    public BigDecimalFilter subtotal() {
        if (subtotal == null) {
            setSubtotal(new BigDecimalFilter());
        }
        return subtotal;
    }

    public void setSubtotal(BigDecimalFilter subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimalFilter getDescuento() {
        return descuento;
    }

    public Optional<BigDecimalFilter> optionalDescuento() {
        return Optional.ofNullable(descuento);
    }

    public BigDecimalFilter descuento() {
        if (descuento == null) {
            setDescuento(new BigDecimalFilter());
        }
        return descuento;
    }

    public void setDescuento(BigDecimalFilter descuento) {
        this.descuento = descuento;
    }

    public BigDecimalFilter getCostoEnvio() {
        return costoEnvio;
    }

    public Optional<BigDecimalFilter> optionalCostoEnvio() {
        return Optional.ofNullable(costoEnvio);
    }

    public BigDecimalFilter costoEnvio() {
        if (costoEnvio == null) {
            setCostoEnvio(new BigDecimalFilter());
        }
        return costoEnvio;
    }

    public void setCostoEnvio(BigDecimalFilter costoEnvio) {
        this.costoEnvio = costoEnvio;
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

    public InstantFilter getFechaPedido() {
        return fechaPedido;
    }

    public Optional<InstantFilter> optionalFechaPedido() {
        return Optional.ofNullable(fechaPedido);
    }

    public InstantFilter fechaPedido() {
        if (fechaPedido == null) {
            setFechaPedido(new InstantFilter());
        }
        return fechaPedido;
    }

    public void setFechaPedido(InstantFilter fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public InstantFilter getFechaExpiracion() {
        return fechaExpiracion;
    }

    public Optional<InstantFilter> optionalFechaExpiracion() {
        return Optional.ofNullable(fechaExpiracion);
    }

    public InstantFilter fechaExpiracion() {
        if (fechaExpiracion == null) {
            setFechaExpiracion(new InstantFilter());
        }
        return fechaExpiracion;
    }

    public void setFechaExpiracion(InstantFilter fechaExpiracion) {
        this.fechaExpiracion = fechaExpiracion;
    }

    public StringFilter getObservaciones() {
        return observaciones;
    }

    public Optional<StringFilter> optionalObservaciones() {
        return Optional.ofNullable(observaciones);
    }

    public StringFilter observaciones() {
        if (observaciones == null) {
            setObservaciones(new StringFilter());
        }
        return observaciones;
    }

    public void setObservaciones(StringFilter observaciones) {
        this.observaciones = observaciones;
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

    public LongFilter getDetallesId() {
        return detallesId;
    }

    public Optional<LongFilter> optionalDetallesId() {
        return Optional.ofNullable(detallesId);
    }

    public LongFilter detallesId() {
        if (detallesId == null) {
            setDetallesId(new LongFilter());
        }
        return detallesId;
    }

    public void setDetallesId(LongFilter detallesId) {
        this.detallesId = detallesId;
    }

    public LongFilter getHistorialEstadosId() {
        return historialEstadosId;
    }

    public Optional<LongFilter> optionalHistorialEstadosId() {
        return Optional.ofNullable(historialEstadosId);
    }

    public LongFilter historialEstadosId() {
        if (historialEstadosId == null) {
            setHistorialEstadosId(new LongFilter());
        }
        return historialEstadosId;
    }

    public void setHistorialEstadosId(LongFilter historialEstadosId) {
        this.historialEstadosId = historialEstadosId;
    }

    public LongFilter getPagosId() {
        return pagosId;
    }

    public Optional<LongFilter> optionalPagosId() {
        return Optional.ofNullable(pagosId);
    }

    public LongFilter pagosId() {
        if (pagosId == null) {
            setPagosId(new LongFilter());
        }
        return pagosId;
    }

    public void setPagosId(LongFilter pagosId) {
        this.pagosId = pagosId;
    }

    public LongFilter getComprobantesId() {
        return comprobantesId;
    }

    public Optional<LongFilter> optionalComprobantesId() {
        return Optional.ofNullable(comprobantesId);
    }

    public LongFilter comprobantesId() {
        if (comprobantesId == null) {
            setComprobantesId(new LongFilter());
        }
        return comprobantesId;
    }

    public void setComprobantesId(LongFilter comprobantesId) {
        this.comprobantesId = comprobantesId;
    }

    public LongFilter getCuponId() {
        return cuponId;
    }

    public Optional<LongFilter> optionalCuponId() {
        return Optional.ofNullable(cuponId);
    }

    public LongFilter cuponId() {
        if (cuponId == null) {
            setCuponId(new LongFilter());
        }
        return cuponId;
    }

    public void setCuponId(LongFilter cuponId) {
        this.cuponId = cuponId;
    }

    public LongFilter getTiendaRecojoId() {
        return tiendaRecojoId;
    }

    public Optional<LongFilter> optionalTiendaRecojoId() {
        return Optional.ofNullable(tiendaRecojoId);
    }

    public LongFilter tiendaRecojoId() {
        if (tiendaRecojoId == null) {
            setTiendaRecojoId(new LongFilter());
        }
        return tiendaRecojoId;
    }

    public void setTiendaRecojoId(LongFilter tiendaRecojoId) {
        this.tiendaRecojoId = tiendaRecojoId;
    }

    public LongFilter getAtendidoPorId() {
        return atendidoPorId;
    }

    public Optional<LongFilter> optionalAtendidoPorId() {
        return Optional.ofNullable(atendidoPorId);
    }

    public LongFilter atendidoPorId() {
        if (atendidoPorId == null) {
            setAtendidoPorId(new LongFilter());
        }
        return atendidoPorId;
    }

    public void setAtendidoPorId(LongFilter atendidoPorId) {
        this.atendidoPorId = atendidoPorId;
    }

    public LongFilter getEnvioId() {
        return envioId;
    }

    public Optional<LongFilter> optionalEnvioId() {
        return Optional.ofNullable(envioId);
    }

    public LongFilter envioId() {
        if (envioId == null) {
            setEnvioId(new LongFilter());
        }
        return envioId;
    }

    public void setEnvioId(LongFilter envioId) {
        this.envioId = envioId;
    }

    public LongFilter getClienteId() {
        return clienteId;
    }

    public Optional<LongFilter> optionalClienteId() {
        return Optional.ofNullable(clienteId);
    }

    public LongFilter clienteId() {
        if (clienteId == null) {
            setClienteId(new LongFilter());
        }
        return clienteId;
    }

    public void setClienteId(LongFilter clienteId) {
        this.clienteId = clienteId;
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
        final PedidoCriteria that = (PedidoCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nroPedido, that.nroPedido) &&
            Objects.equals(tipoEntrega, that.tipoEntrega) &&
            Objects.equals(tipoComprobante, that.tipoComprobante) &&
            Objects.equals(estado, that.estado) &&
            Objects.equals(moneda, that.moneda) &&
            Objects.equals(subtotal, that.subtotal) &&
            Objects.equals(descuento, that.descuento) &&
            Objects.equals(costoEnvio, that.costoEnvio) &&
            Objects.equals(igv, that.igv) &&
            Objects.equals(total, that.total) &&
            Objects.equals(fechaPedido, that.fechaPedido) &&
            Objects.equals(fechaExpiracion, that.fechaExpiracion) &&
            Objects.equals(observaciones, that.observaciones) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(lastModifiedBy, that.lastModifiedBy) &&
            Objects.equals(lastModifiedDate, that.lastModifiedDate) &&
            Objects.equals(detallesId, that.detallesId) &&
            Objects.equals(historialEstadosId, that.historialEstadosId) &&
            Objects.equals(pagosId, that.pagosId) &&
            Objects.equals(comprobantesId, that.comprobantesId) &&
            Objects.equals(cuponId, that.cuponId) &&
            Objects.equals(tiendaRecojoId, that.tiendaRecojoId) &&
            Objects.equals(atendidoPorId, that.atendidoPorId) &&
            Objects.equals(envioId, that.envioId) &&
            Objects.equals(clienteId, that.clienteId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            nroPedido,
            tipoEntrega,
            tipoComprobante,
            estado,
            moneda,
            subtotal,
            descuento,
            costoEnvio,
            igv,
            total,
            fechaPedido,
            fechaExpiracion,
            observaciones,
            createdBy,
            createdDate,
            lastModifiedBy,
            lastModifiedDate,
            detallesId,
            historialEstadosId,
            pagosId,
            comprobantesId,
            cuponId,
            tiendaRecojoId,
            atendidoPorId,
            envioId,
            clienteId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PedidoCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalNroPedido().map(f -> "nroPedido=" + f + ", ").orElse("") +
            optionalTipoEntrega().map(f -> "tipoEntrega=" + f + ", ").orElse("") +
            optionalTipoComprobante().map(f -> "tipoComprobante=" + f + ", ").orElse("") +
            optionalEstado().map(f -> "estado=" + f + ", ").orElse("") +
            optionalMoneda().map(f -> "moneda=" + f + ", ").orElse("") +
            optionalSubtotal().map(f -> "subtotal=" + f + ", ").orElse("") +
            optionalDescuento().map(f -> "descuento=" + f + ", ").orElse("") +
            optionalCostoEnvio().map(f -> "costoEnvio=" + f + ", ").orElse("") +
            optionalIgv().map(f -> "igv=" + f + ", ").orElse("") +
            optionalTotal().map(f -> "total=" + f + ", ").orElse("") +
            optionalFechaPedido().map(f -> "fechaPedido=" + f + ", ").orElse("") +
            optionalFechaExpiracion().map(f -> "fechaExpiracion=" + f + ", ").orElse("") +
            optionalObservaciones().map(f -> "observaciones=" + f + ", ").orElse("") +
            optionalCreatedBy().map(f -> "createdBy=" + f + ", ").orElse("") +
            optionalCreatedDate().map(f -> "createdDate=" + f + ", ").orElse("") +
            optionalLastModifiedBy().map(f -> "lastModifiedBy=" + f + ", ").orElse("") +
            optionalLastModifiedDate().map(f -> "lastModifiedDate=" + f + ", ").orElse("") +
            optionalDetallesId().map(f -> "detallesId=" + f + ", ").orElse("") +
            optionalHistorialEstadosId().map(f -> "historialEstadosId=" + f + ", ").orElse("") +
            optionalPagosId().map(f -> "pagosId=" + f + ", ").orElse("") +
            optionalComprobantesId().map(f -> "comprobantesId=" + f + ", ").orElse("") +
            optionalCuponId().map(f -> "cuponId=" + f + ", ").orElse("") +
            optionalTiendaRecojoId().map(f -> "tiendaRecojoId=" + f + ", ").orElse("") +
            optionalAtendidoPorId().map(f -> "atendidoPorId=" + f + ", ").orElse("") +
            optionalEnvioId().map(f -> "envioId=" + f + ", ").orElse("") +
            optionalClienteId().map(f -> "clienteId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
