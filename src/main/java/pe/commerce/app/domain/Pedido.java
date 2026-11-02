package pe.commerce.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import pe.commerce.app.domain.enumeration.EstadoPedido;
import pe.commerce.app.domain.enumeration.Moneda;
import pe.commerce.app.domain.enumeration.TipoComprobantePedido;
import pe.commerce.app.domain.enumeration.TipoEntrega;

/**
 * descuento = descuento del cupón (nivel pedido).
 * fechaExpiracion = fechaPedido + 30 min; pasado ese tiempo sin pago
 * el job lo pasa a EXPIRADO y libera stockReservado.
 */
@Entity
@Table(name = "pedido")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Pedido implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 20)
    @Column(name = "nro_pedido", length = 20, nullable = false, unique = true)
    private String nroPedido;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_entrega", nullable = false)
    private TipoEntrega tipoEntrega;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_comprobante", nullable = false)
    private TipoComprobantePedido tipoComprobante;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoPedido estado;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "moneda", nullable = false)
    private Moneda moneda;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "subtotal", precision = 21, scale = 2, nullable = false)
    private BigDecimal subtotal;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "descuento", precision = 21, scale = 2, nullable = false)
    private BigDecimal descuento;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "costo_envio", precision = 21, scale = 2, nullable = false)
    private BigDecimal costoEnvio;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "igv", precision = 21, scale = 2, nullable = false)
    private BigDecimal igv;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "total", precision = 21, scale = 2, nullable = false)
    private BigDecimal total;

    @NotNull
    @Column(name = "fecha_pedido", nullable = false)
    private Instant fechaPedido;

    @NotNull
    @Column(name = "fecha_expiracion", nullable = false)
    private Instant fechaExpiracion;

    @Size(max = 500)
    @Column(name = "observaciones", length = 500)
    private String observaciones;

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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "pedido")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "carritoItem", "producto", "pedido" }, allowSetters = true)
    private Set<PedidoDetalle> detalleses = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "pedido")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "pedido" }, allowSetters = true)
    private Set<PedidoEstadoHistorial> historialEstadoses = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "pedido")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "pedido" }, allowSetters = true)
    private Set<Pago> pagoses = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "pedido")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "comprobanteReferencia", "pedido" }, allowSetters = true)
    private Set<Comprobante> comprobanteses = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private Cupon cupon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "inventarioses", "empleadoses", "ubigeo" }, allowSetters = true)
    private Tienda tiendaRecojo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "user", "persona", "tienda" }, allowSetters = true)
    private Empleado atendidoPor;

    @JsonIgnoreProperties(value = { "pedido", "direccion" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "pedido")
    private Envio envio;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = { "user", "persona", "direccioneses", "datosFacturacions", "pedidoses", "resenases", "carrito" },
        allowSetters = true
    )
    private Cliente cliente;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Pedido id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNroPedido() {
        return this.nroPedido;
    }

    public Pedido nroPedido(String nroPedido) {
        this.setNroPedido(nroPedido);
        return this;
    }

    public void setNroPedido(String nroPedido) {
        this.nroPedido = nroPedido;
    }

    public TipoEntrega getTipoEntrega() {
        return this.tipoEntrega;
    }

    public Pedido tipoEntrega(TipoEntrega tipoEntrega) {
        this.setTipoEntrega(tipoEntrega);
        return this;
    }

    public void setTipoEntrega(TipoEntrega tipoEntrega) {
        this.tipoEntrega = tipoEntrega;
    }

    public TipoComprobantePedido getTipoComprobante() {
        return this.tipoComprobante;
    }

    public Pedido tipoComprobante(TipoComprobantePedido tipoComprobante) {
        this.setTipoComprobante(tipoComprobante);
        return this;
    }

    public void setTipoComprobante(TipoComprobantePedido tipoComprobante) {
        this.tipoComprobante = tipoComprobante;
    }

    public EstadoPedido getEstado() {
        return this.estado;
    }

    public Pedido estado(EstadoPedido estado) {
        this.setEstado(estado);
        return this;
    }

    public void setEstado(EstadoPedido estado) {
        this.estado = estado;
    }

    public Moneda getMoneda() {
        return this.moneda;
    }

    public Pedido moneda(Moneda moneda) {
        this.setMoneda(moneda);
        return this;
    }

    public void setMoneda(Moneda moneda) {
        this.moneda = moneda;
    }

    public BigDecimal getSubtotal() {
        return this.subtotal;
    }

    public Pedido subtotal(BigDecimal subtotal) {
        this.setSubtotal(subtotal);
        return this;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getDescuento() {
        return this.descuento;
    }

    public Pedido descuento(BigDecimal descuento) {
        this.setDescuento(descuento);
        return this;
    }

    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }

    public BigDecimal getCostoEnvio() {
        return this.costoEnvio;
    }

    public Pedido costoEnvio(BigDecimal costoEnvio) {
        this.setCostoEnvio(costoEnvio);
        return this;
    }

    public void setCostoEnvio(BigDecimal costoEnvio) {
        this.costoEnvio = costoEnvio;
    }

    public BigDecimal getIgv() {
        return this.igv;
    }

    public Pedido igv(BigDecimal igv) {
        this.setIgv(igv);
        return this;
    }

    public void setIgv(BigDecimal igv) {
        this.igv = igv;
    }

    public BigDecimal getTotal() {
        return this.total;
    }

    public Pedido total(BigDecimal total) {
        this.setTotal(total);
        return this;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Instant getFechaPedido() {
        return this.fechaPedido;
    }

    public Pedido fechaPedido(Instant fechaPedido) {
        this.setFechaPedido(fechaPedido);
        return this;
    }

    public void setFechaPedido(Instant fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public Instant getFechaExpiracion() {
        return this.fechaExpiracion;
    }

    public Pedido fechaExpiracion(Instant fechaExpiracion) {
        this.setFechaExpiracion(fechaExpiracion);
        return this;
    }

    public void setFechaExpiracion(Instant fechaExpiracion) {
        this.fechaExpiracion = fechaExpiracion;
    }

    public String getObservaciones() {
        return this.observaciones;
    }

    public Pedido observaciones(String observaciones) {
        this.setObservaciones(observaciones);
        return this;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public Pedido createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public Pedido createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public Pedido lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public Pedido lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Set<PedidoDetalle> getDetalleses() {
        return this.detalleses;
    }

    public void setDetalleses(Set<PedidoDetalle> pedidoDetalles) {
        if (this.detalleses != null) {
            this.detalleses.forEach(i -> i.setPedido(null));
        }
        if (pedidoDetalles != null) {
            pedidoDetalles.forEach(i -> i.setPedido(this));
        }
        this.detalleses = pedidoDetalles;
    }

    public Pedido detalleses(Set<PedidoDetalle> pedidoDetalles) {
        this.setDetalleses(pedidoDetalles);
        return this;
    }

    public Pedido addDetalles(PedidoDetalle pedidoDetalle) {
        this.detalleses.add(pedidoDetalle);
        pedidoDetalle.setPedido(this);
        return this;
    }

    public Pedido removeDetalles(PedidoDetalle pedidoDetalle) {
        this.detalleses.remove(pedidoDetalle);
        pedidoDetalle.setPedido(null);
        return this;
    }

    public Set<PedidoEstadoHistorial> getHistorialEstadoses() {
        return this.historialEstadoses;
    }

    public void setHistorialEstadoses(Set<PedidoEstadoHistorial> pedidoEstadoHistorials) {
        if (this.historialEstadoses != null) {
            this.historialEstadoses.forEach(i -> i.setPedido(null));
        }
        if (pedidoEstadoHistorials != null) {
            pedidoEstadoHistorials.forEach(i -> i.setPedido(this));
        }
        this.historialEstadoses = pedidoEstadoHistorials;
    }

    public Pedido historialEstadoses(Set<PedidoEstadoHistorial> pedidoEstadoHistorials) {
        this.setHistorialEstadoses(pedidoEstadoHistorials);
        return this;
    }

    public Pedido addHistorialEstados(PedidoEstadoHistorial pedidoEstadoHistorial) {
        this.historialEstadoses.add(pedidoEstadoHistorial);
        pedidoEstadoHistorial.setPedido(this);
        return this;
    }

    public Pedido removeHistorialEstados(PedidoEstadoHistorial pedidoEstadoHistorial) {
        this.historialEstadoses.remove(pedidoEstadoHistorial);
        pedidoEstadoHistorial.setPedido(null);
        return this;
    }

    public Set<Pago> getPagoses() {
        return this.pagoses;
    }

    public void setPagoses(Set<Pago> pagos) {
        if (this.pagoses != null) {
            this.pagoses.forEach(i -> i.setPedido(null));
        }
        if (pagos != null) {
            pagos.forEach(i -> i.setPedido(this));
        }
        this.pagoses = pagos;
    }

    public Pedido pagoses(Set<Pago> pagos) {
        this.setPagoses(pagos);
        return this;
    }

    public Pedido addPagos(Pago pago) {
        this.pagoses.add(pago);
        pago.setPedido(this);
        return this;
    }

    public Pedido removePagos(Pago pago) {
        this.pagoses.remove(pago);
        pago.setPedido(null);
        return this;
    }

    public Set<Comprobante> getComprobanteses() {
        return this.comprobanteses;
    }

    public void setComprobanteses(Set<Comprobante> comprobantes) {
        if (this.comprobanteses != null) {
            this.comprobanteses.forEach(i -> i.setPedido(null));
        }
        if (comprobantes != null) {
            comprobantes.forEach(i -> i.setPedido(this));
        }
        this.comprobanteses = comprobantes;
    }

    public Pedido comprobanteses(Set<Comprobante> comprobantes) {
        this.setComprobanteses(comprobantes);
        return this;
    }

    public Pedido addComprobantes(Comprobante comprobante) {
        this.comprobanteses.add(comprobante);
        comprobante.setPedido(this);
        return this;
    }

    public Pedido removeComprobantes(Comprobante comprobante) {
        this.comprobanteses.remove(comprobante);
        comprobante.setPedido(null);
        return this;
    }

    public Cupon getCupon() {
        return this.cupon;
    }

    public void setCupon(Cupon cupon) {
        this.cupon = cupon;
    }

    public Pedido cupon(Cupon cupon) {
        this.setCupon(cupon);
        return this;
    }

    public Tienda getTiendaRecojo() {
        return this.tiendaRecojo;
    }

    public void setTiendaRecojo(Tienda tienda) {
        this.tiendaRecojo = tienda;
    }

    public Pedido tiendaRecojo(Tienda tienda) {
        this.setTiendaRecojo(tienda);
        return this;
    }

    public Empleado getAtendidoPor() {
        return this.atendidoPor;
    }

    public void setAtendidoPor(Empleado empleado) {
        this.atendidoPor = empleado;
    }

    public Pedido atendidoPor(Empleado empleado) {
        this.setAtendidoPor(empleado);
        return this;
    }

    public Envio getEnvio() {
        return this.envio;
    }

    public void setEnvio(Envio envio) {
        if (this.envio != null) {
            this.envio.setPedido(null);
        }
        if (envio != null) {
            envio.setPedido(this);
        }
        this.envio = envio;
    }

    public Pedido envio(Envio envio) {
        this.setEnvio(envio);
        return this;
    }

    public Cliente getCliente() {
        return this.cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Pedido cliente(Cliente cliente) {
        this.setCliente(cliente);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Pedido)) {
            return false;
        }
        return getId() != null && getId().equals(((Pedido) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Pedido{" +
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
            "}";
    }
}
