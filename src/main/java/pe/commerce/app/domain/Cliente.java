package pe.commerce.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * Perfil comercial del comprador
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "cliente")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Cliente implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 20)
    @Column(name = "codigo", length = 20, nullable = false, unique = true)
    private String codigo;

    @NotNull
    @Column(name = "fecha_registro", nullable = false)
    private Instant fechaRegistro;

    @NotNull
    @Column(name = "acepta_publicidad", nullable = false)
    private Boolean aceptaPublicidad;

    @NotNull
    @Column(name = "activo", nullable = false)
    private Boolean activo;

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

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private User user;

    @JsonIgnoreProperties(value = { "documentoses", "mediosContactos", "cliente", "empleado" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private Persona persona;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "cliente")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "ubigeo", "cliente" }, allowSetters = true)
    private Set<Direccion> direccioneses = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "cliente")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "cliente" }, allowSetters = true)
    private Set<DatoFacturacion> datosFacturacions = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "cliente")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
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
    private Set<Pedido> pedidoses = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "cliente")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "cliente", "producto" }, allowSetters = true)
    private Set<Resena> resenases = new HashSet<>();

    @JsonIgnoreProperties(value = { "cliente", "itemses" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "cliente")
    private Carrito carrito;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Cliente id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return this.codigo;
    }

    public Cliente codigo(String codigo) {
        this.setCodigo(codigo);
        return this;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Instant getFechaRegistro() {
        return this.fechaRegistro;
    }

    public Cliente fechaRegistro(Instant fechaRegistro) {
        this.setFechaRegistro(fechaRegistro);
        return this;
    }

    public void setFechaRegistro(Instant fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Boolean getAceptaPublicidad() {
        return this.aceptaPublicidad;
    }

    public Cliente aceptaPublicidad(Boolean aceptaPublicidad) {
        this.setAceptaPublicidad(aceptaPublicidad);
        return this;
    }

    public void setAceptaPublicidad(Boolean aceptaPublicidad) {
        this.aceptaPublicidad = aceptaPublicidad;
    }

    public Boolean getActivo() {
        return this.activo;
    }

    public Cliente activo(Boolean activo) {
        this.setActivo(activo);
        return this;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public Cliente createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public Cliente createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public Cliente lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public Cliente lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Cliente user(User user) {
        this.setUser(user);
        return this;
    }

    public Persona getPersona() {
        return this.persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public Cliente persona(Persona persona) {
        this.setPersona(persona);
        return this;
    }

    public Set<Direccion> getDireccioneses() {
        return this.direccioneses;
    }

    public void setDireccioneses(Set<Direccion> direccions) {
        if (this.direccioneses != null) {
            this.direccioneses.forEach(i -> i.setCliente(null));
        }
        if (direccions != null) {
            direccions.forEach(i -> i.setCliente(this));
        }
        this.direccioneses = direccions;
    }

    public Cliente direccioneses(Set<Direccion> direccions) {
        this.setDireccioneses(direccions);
        return this;
    }

    public Cliente addDirecciones(Direccion direccion) {
        this.direccioneses.add(direccion);
        direccion.setCliente(this);
        return this;
    }

    public Cliente removeDirecciones(Direccion direccion) {
        this.direccioneses.remove(direccion);
        direccion.setCliente(null);
        return this;
    }

    public Set<DatoFacturacion> getDatosFacturacions() {
        return this.datosFacturacions;
    }

    public void setDatosFacturacions(Set<DatoFacturacion> datoFacturacions) {
        if (this.datosFacturacions != null) {
            this.datosFacturacions.forEach(i -> i.setCliente(null));
        }
        if (datoFacturacions != null) {
            datoFacturacions.forEach(i -> i.setCliente(this));
        }
        this.datosFacturacions = datoFacturacions;
    }

    public Cliente datosFacturacions(Set<DatoFacturacion> datoFacturacions) {
        this.setDatosFacturacions(datoFacturacions);
        return this;
    }

    public Cliente addDatosFacturacion(DatoFacturacion datoFacturacion) {
        this.datosFacturacions.add(datoFacturacion);
        datoFacturacion.setCliente(this);
        return this;
    }

    public Cliente removeDatosFacturacion(DatoFacturacion datoFacturacion) {
        this.datosFacturacions.remove(datoFacturacion);
        datoFacturacion.setCliente(null);
        return this;
    }

    public Set<Pedido> getPedidoses() {
        return this.pedidoses;
    }

    public void setPedidoses(Set<Pedido> pedidos) {
        if (this.pedidoses != null) {
            this.pedidoses.forEach(i -> i.setCliente(null));
        }
        if (pedidos != null) {
            pedidos.forEach(i -> i.setCliente(this));
        }
        this.pedidoses = pedidos;
    }

    public Cliente pedidoses(Set<Pedido> pedidos) {
        this.setPedidoses(pedidos);
        return this;
    }

    public Cliente addPedidos(Pedido pedido) {
        this.pedidoses.add(pedido);
        pedido.setCliente(this);
        return this;
    }

    public Cliente removePedidos(Pedido pedido) {
        this.pedidoses.remove(pedido);
        pedido.setCliente(null);
        return this;
    }

    public Set<Resena> getResenases() {
        return this.resenases;
    }

    public void setResenases(Set<Resena> resenas) {
        if (this.resenases != null) {
            this.resenases.forEach(i -> i.setCliente(null));
        }
        if (resenas != null) {
            resenas.forEach(i -> i.setCliente(this));
        }
        this.resenases = resenas;
    }

    public Cliente resenases(Set<Resena> resenas) {
        this.setResenases(resenas);
        return this;
    }

    public Cliente addResenas(Resena resena) {
        this.resenases.add(resena);
        resena.setCliente(this);
        return this;
    }

    public Cliente removeResenas(Resena resena) {
        this.resenases.remove(resena);
        resena.setCliente(null);
        return this;
    }

    public Carrito getCarrito() {
        return this.carrito;
    }

    public void setCarrito(Carrito carrito) {
        if (this.carrito != null) {
            this.carrito.setCliente(null);
        }
        if (carrito != null) {
            carrito.setCliente(this);
        }
        this.carrito = carrito;
    }

    public Cliente carrito(Carrito carrito) {
        this.setCarrito(carrito);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cliente)) {
            return false;
        }
        return getId() != null && getId().equals(((Cliente) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Cliente{" +
            "id=" + getId() +
            ", codigo='" + getCodigo() + "'" +
            ", fechaRegistro='" + getFechaRegistro() + "'" +
            ", aceptaPublicidad='" + getAceptaPublicidad() + "'" +
            ", activo='" + getActivo() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
