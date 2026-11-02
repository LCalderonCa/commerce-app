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

/**
 * A Tienda.
 */
@Entity
@Table(name = "tienda")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Tienda implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "nombre", length = 100, nullable = false)
    private String nombre;

    @NotNull
    @Size(max = 200)
    @Column(name = "direccion", length = 200, nullable = false)
    private String direccion;

    @Size(max = 15)
    @Column(name = "telefono", length = 15)
    private String telefono;

    @NotNull
    @Column(name = "permite_recojo", nullable = false)
    private Boolean permiteRecojo;

    @NotNull
    @Column(name = "activo", nullable = false)
    private Boolean activo;

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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tienda")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "producto", "tienda" }, allowSetters = true)
    private Set<Inventario> inventarioses = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tienda")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "user", "persona", "tienda" }, allowSetters = true)
    private Set<Empleado> empleadoses = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    private Ubigeo ubigeo;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Tienda id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Tienda nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return this.direccion;
    }

    public Tienda direccion(String direccion) {
        this.setDireccion(direccion);
        return this;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return this.telefono;
    }

    public Tienda telefono(String telefono) {
        this.setTelefono(telefono);
        return this;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Boolean getPermiteRecojo() {
        return this.permiteRecojo;
    }

    public Tienda permiteRecojo(Boolean permiteRecojo) {
        this.setPermiteRecojo(permiteRecojo);
        return this;
    }

    public void setPermiteRecojo(Boolean permiteRecojo) {
        this.permiteRecojo = permiteRecojo;
    }

    public Boolean getActivo() {
        return this.activo;
    }

    public Tienda activo(Boolean activo) {
        this.setActivo(activo);
        return this;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public Tienda createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public Tienda createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public Tienda lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public Tienda lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Set<Inventario> getInventarioses() {
        return this.inventarioses;
    }

    public void setInventarioses(Set<Inventario> inventarios) {
        if (this.inventarioses != null) {
            this.inventarioses.forEach(i -> i.setTienda(null));
        }
        if (inventarios != null) {
            inventarios.forEach(i -> i.setTienda(this));
        }
        this.inventarioses = inventarios;
    }

    public Tienda inventarioses(Set<Inventario> inventarios) {
        this.setInventarioses(inventarios);
        return this;
    }

    public Tienda addInventarios(Inventario inventario) {
        this.inventarioses.add(inventario);
        inventario.setTienda(this);
        return this;
    }

    public Tienda removeInventarios(Inventario inventario) {
        this.inventarioses.remove(inventario);
        inventario.setTienda(null);
        return this;
    }

    public Set<Empleado> getEmpleadoses() {
        return this.empleadoses;
    }

    public void setEmpleadoses(Set<Empleado> empleados) {
        if (this.empleadoses != null) {
            this.empleadoses.forEach(i -> i.setTienda(null));
        }
        if (empleados != null) {
            empleados.forEach(i -> i.setTienda(this));
        }
        this.empleadoses = empleados;
    }

    public Tienda empleadoses(Set<Empleado> empleados) {
        this.setEmpleadoses(empleados);
        return this;
    }

    public Tienda addEmpleados(Empleado empleado) {
        this.empleadoses.add(empleado);
        empleado.setTienda(this);
        return this;
    }

    public Tienda removeEmpleados(Empleado empleado) {
        this.empleadoses.remove(empleado);
        empleado.setTienda(null);
        return this;
    }

    public Ubigeo getUbigeo() {
        return this.ubigeo;
    }

    public void setUbigeo(Ubigeo ubigeo) {
        this.ubigeo = ubigeo;
    }

    public Tienda ubigeo(Ubigeo ubigeo) {
        this.setUbigeo(ubigeo);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Tienda)) {
            return false;
        }
        return getId() != null && getId().equals(((Tienda) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Tienda{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", direccion='" + getDireccion() + "'" +
            ", telefono='" + getTelefono() + "'" +
            ", permiteRecojo='" + getPermiteRecojo() + "'" +
            ", activo='" + getActivo() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
