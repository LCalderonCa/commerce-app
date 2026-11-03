package pe.commerce.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
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
 * Datos personales comunes a clientes y empleados
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "persona")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Persona implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "nombres", length = 100, nullable = false)
    private String nombres;

    @NotNull
    @Size(max = 100)
    @Column(name = "apellidos", length = 100, nullable = false)
    private String apellidos;

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "persona")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "persona" }, allowSetters = true)
    private Set<DocumentoIdentidad> documentoses = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "persona")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "persona" }, allowSetters = true)
    private Set<MedioContacto> mediosContactos = new HashSet<>();

    @JsonIgnoreProperties(
        value = { "user", "persona", "direccioneses", "datosFacturacions", "pedidoses", "resenases", "carrito" },
        allowSetters = true
    )
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "persona")
    private Cliente cliente;

    @JsonIgnoreProperties(value = { "user", "persona", "tienda" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "persona")
    private Empleado empleado;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Persona id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombres() {
        return this.nombres;
    }

    public Persona nombres(String nombres) {
        this.setNombres(nombres);
        return this;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return this.apellidos;
    }

    public Persona apellidos(String apellidos) {
        this.setApellidos(apellidos);
        return this;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public LocalDate getFechaNacimiento() {
        return this.fechaNacimiento;
    }

    public Persona fechaNacimiento(LocalDate fechaNacimiento) {
        this.setFechaNacimiento(fechaNacimiento);
        return this;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public Persona createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public Persona createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public Persona lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public Persona lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Set<DocumentoIdentidad> getDocumentoses() {
        return this.documentoses;
    }

    public void setDocumentoses(Set<DocumentoIdentidad> documentoIdentidads) {
        if (this.documentoses != null) {
            this.documentoses.forEach(i -> i.setPersona(null));
        }
        if (documentoIdentidads != null) {
            documentoIdentidads.forEach(i -> i.setPersona(this));
        }
        this.documentoses = documentoIdentidads;
    }

    public Persona documentoses(Set<DocumentoIdentidad> documentoIdentidads) {
        this.setDocumentoses(documentoIdentidads);
        return this;
    }

    public Persona addDocumentos(DocumentoIdentidad documentoIdentidad) {
        this.documentoses.add(documentoIdentidad);
        documentoIdentidad.setPersona(this);
        return this;
    }

    public Persona removeDocumentos(DocumentoIdentidad documentoIdentidad) {
        this.documentoses.remove(documentoIdentidad);
        documentoIdentidad.setPersona(null);
        return this;
    }

    public Set<MedioContacto> getMediosContactos() {
        return this.mediosContactos;
    }

    public void setMediosContactos(Set<MedioContacto> medioContactos) {
        if (this.mediosContactos != null) {
            this.mediosContactos.forEach(i -> i.setPersona(null));
        }
        if (medioContactos != null) {
            medioContactos.forEach(i -> i.setPersona(this));
        }
        this.mediosContactos = medioContactos;
    }

    public Persona mediosContactos(Set<MedioContacto> medioContactos) {
        this.setMediosContactos(medioContactos);
        return this;
    }

    public Persona addMediosContacto(MedioContacto medioContacto) {
        this.mediosContactos.add(medioContacto);
        medioContacto.setPersona(this);
        return this;
    }

    public Persona removeMediosContacto(MedioContacto medioContacto) {
        this.mediosContactos.remove(medioContacto);
        medioContacto.setPersona(null);
        return this;
    }

    public Cliente getCliente() {
        return this.cliente;
    }

    public void setCliente(Cliente cliente) {
        if (this.cliente != null) {
            this.cliente.setPersona(null);
        }
        if (cliente != null) {
            cliente.setPersona(this);
        }
        this.cliente = cliente;
    }

    public Persona cliente(Cliente cliente) {
        this.setCliente(cliente);
        return this;
    }

    public Empleado getEmpleado() {
        return this.empleado;
    }

    public void setEmpleado(Empleado empleado) {
        if (this.empleado != null) {
            this.empleado.setPersona(null);
        }
        if (empleado != null) {
            empleado.setPersona(this);
        }
        this.empleado = empleado;
    }

    public Persona empleado(Empleado empleado) {
        this.setEmpleado(empleado);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Persona)) {
            return false;
        }
        return getId() != null && getId().equals(((Persona) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Persona{" +
            "id=" + getId() +
            ", nombres='" + getNombres() + "'" +
            ", apellidos='" + getApellidos() + "'" +
            ", fechaNacimiento='" + getFechaNacimiento() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
