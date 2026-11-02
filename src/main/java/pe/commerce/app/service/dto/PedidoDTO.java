package pe.commerce.app.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import pe.commerce.app.domain.enumeration.EstadoPedido;
import pe.commerce.app.domain.enumeration.Moneda;
import pe.commerce.app.domain.enumeration.TipoComprobantePedido;
import pe.commerce.app.domain.enumeration.TipoEntrega;

/**
 * A DTO for the {@link pe.commerce.app.domain.Pedido} entity.
 */
@Schema(
    description = "descuento = descuento del cupón (nivel pedido).\nfechaExpiracion = fechaPedido + 30 min; pasado ese tiempo sin pago\nel job lo pasa a EXPIRADO y libera stockReservado."
)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PedidoDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 20)
    private String nroPedido;

    @NotNull
    private TipoEntrega tipoEntrega;

    @NotNull
    private TipoComprobantePedido tipoComprobante;

    @NotNull
    private EstadoPedido estado;

    @NotNull
    private Moneda moneda;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal subtotal;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal descuento;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal costoEnvio;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal igv;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal total;

    @NotNull
    private Instant fechaPedido;

    @NotNull
    private Instant fechaExpiracion;

    @Size(max = 500)
    private String observaciones;

    @Size(max = 50)
    private String createdBy;

    private Instant createdDate;

    @Size(max = 50)
    private String lastModifiedBy;

    private Instant lastModifiedDate;

    private CuponDTO cupon;

    private TiendaDTO tiendaRecojo;

    private EmpleadoDTO atendidoPor;

    @NotNull
    private ClienteDTO cliente;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNroPedido() {
        return nroPedido;
    }

    public void setNroPedido(String nroPedido) {
        this.nroPedido = nroPedido;
    }

    public TipoEntrega getTipoEntrega() {
        return tipoEntrega;
    }

    public void setTipoEntrega(TipoEntrega tipoEntrega) {
        this.tipoEntrega = tipoEntrega;
    }

    public TipoComprobantePedido getTipoComprobante() {
        return tipoComprobante;
    }

    public void setTipoComprobante(TipoComprobantePedido tipoComprobante) {
        this.tipoComprobante = tipoComprobante;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public void setEstado(EstadoPedido estado) {
        this.estado = estado;
    }

    public Moneda getMoneda() {
        return moneda;
    }

    public void setMoneda(Moneda moneda) {
        this.moneda = moneda;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getDescuento() {
        return descuento;
    }

    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }

    public BigDecimal getCostoEnvio() {
        return costoEnvio;
    }

    public void setCostoEnvio(BigDecimal costoEnvio) {
        this.costoEnvio = costoEnvio;
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

    public Instant getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(Instant fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public Instant getFechaExpiracion() {
        return fechaExpiracion;
    }

    public void setFechaExpiracion(Instant fechaExpiracion) {
        this.fechaExpiracion = fechaExpiracion;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
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

    public CuponDTO getCupon() {
        return cupon;
    }

    public void setCupon(CuponDTO cupon) {
        this.cupon = cupon;
    }

    public TiendaDTO getTiendaRecojo() {
        return tiendaRecojo;
    }

    public void setTiendaRecojo(TiendaDTO tiendaRecojo) {
        this.tiendaRecojo = tiendaRecojo;
    }

    public EmpleadoDTO getAtendidoPor() {
        return atendidoPor;
    }

    public void setAtendidoPor(EmpleadoDTO atendidoPor) {
        this.atendidoPor = atendidoPor;
    }

    public ClienteDTO getCliente() {
        return cliente;
    }

    public void setCliente(ClienteDTO cliente) {
        this.cliente = cliente;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PedidoDTO)) {
            return false;
        }

        PedidoDTO pedidoDTO = (PedidoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, pedidoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PedidoDTO{" +
            "id=" + getId() +
            ", nroPedido='" + getNroPedido() + "'" +
            ", tipoEntrega='" + getTipoEntrega() + "'" +
            ", tipoComprobante='" + getTipoComprobante() + "'" +
            ", estado='" + getEstado() + "'" +
            ", moneda='" + getMoneda() + "'" +
            ", subtotal=" + getSubtotal() +
            ", descuento=" + getDescuento() +
            ", costoEnvio=" + getCostoEnvio() +
            ", igv=" + getIgv() +
            ", total=" + getTotal() +
            ", fechaPedido='" + getFechaPedido() + "'" +
            ", fechaExpiracion='" + getFechaExpiracion() + "'" +
            ", observaciones='" + getObservaciones() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", cupon=" + getCupon() +
            ", tiendaRecojo=" + getTiendaRecojo() +
            ", atendidoPor=" + getAtendidoPor() +
            ", cliente=" + getCliente() +
            "}";
    }
}
