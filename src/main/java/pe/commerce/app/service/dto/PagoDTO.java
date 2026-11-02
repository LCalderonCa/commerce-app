package pe.commerce.app.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import pe.commerce.app.domain.enumeration.EstadoPago;
import pe.commerce.app.domain.enumeration.MetodoPago;
import pe.commerce.app.domain.enumeration.Moneda;

/**
 * A DTO for the {@link pe.commerce.app.domain.Pago} entity.
 */
@Schema(
    description = "Resultado de un intento de pago (puede haber varios por pedido).\nsubMetodo: YAPE, PLIN (billetera) / VISA, MASTERCARD (tarjeta).\npasarela: código/nombre que devuelve la pasarela (Niubiz, Izipay, Mercado Pago...)."
)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PagoDTO implements Serializable {

    private Long id;

    @NotNull
    private MetodoPago metodo;

    @Size(max = 30)
    private String subMetodo;

    @Size(max = 50)
    private String pasarela;

    @Size(max = 100)
    private String nroTransaccion;

    @NotNull
    private Moneda moneda;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal monto;

    @NotNull
    private EstadoPago estado;

    @NotNull
    private Instant fechaPago;

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

    public MetodoPago getMetodo() {
        return metodo;
    }

    public void setMetodo(MetodoPago metodo) {
        this.metodo = metodo;
    }

    public String getSubMetodo() {
        return subMetodo;
    }

    public void setSubMetodo(String subMetodo) {
        this.subMetodo = subMetodo;
    }

    public String getPasarela() {
        return pasarela;
    }

    public void setPasarela(String pasarela) {
        this.pasarela = pasarela;
    }

    public String getNroTransaccion() {
        return nroTransaccion;
    }

    public void setNroTransaccion(String nroTransaccion) {
        this.nroTransaccion = nroTransaccion;
    }

    public Moneda getMoneda() {
        return moneda;
    }

    public void setMoneda(Moneda moneda) {
        this.moneda = moneda;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public EstadoPago getEstado() {
        return estado;
    }

    public void setEstado(EstadoPago estado) {
        this.estado = estado;
    }

    public Instant getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(Instant fechaPago) {
        this.fechaPago = fechaPago;
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
        if (!(o instanceof PagoDTO)) {
            return false;
        }

        PagoDTO pagoDTO = (PagoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, pagoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PagoDTO{" +
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
            ", pedido=" + getPedido() +
            "}";
    }
}
