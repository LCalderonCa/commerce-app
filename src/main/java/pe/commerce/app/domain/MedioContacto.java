package pe.commerce.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import pe.commerce.app.domain.enumeration.TipoMedioContacto;

/**
 * Teléfonos y correos de la persona.
 * El formato de \"valor\" se valida en el service según el tipo.
 * Un principal por tipo (uno para teléfonos, uno para correos).
 * El email de login está en User; aquí van los de contacto.
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "medio_contacto")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MedioContacto implements Serializable {

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
    private TipoMedioContacto tipo;

    @NotNull
    @Size(max = 100)
    @Column(name = "valor", length = 100, nullable = false)
    private String valor;

    @Size(max = 4)
    @Column(name = "codigo_pais", length = 4)
    private String codigoPais;

    @Size(max = 6)
    @Column(name = "anexo", length = 6)
    private String anexo;

    @NotNull
    @Column(name = "principal", nullable = false)
    private Boolean principal;

    @NotNull
    @Column(name = "verificado", nullable = false)
    private Boolean verificado;

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

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "documentoses", "mediosContactos", "cliente", "empleado" }, allowSetters = true)
    private Persona persona;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MedioContacto id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoMedioContacto getTipo() {
        return this.tipo;
    }

    public MedioContacto tipo(TipoMedioContacto tipo) {
        this.setTipo(tipo);
        return this;
    }

    public void setTipo(TipoMedioContacto tipo) {
        this.tipo = tipo;
    }

    public String getValor() {
        return this.valor;
    }

    public MedioContacto valor(String valor) {
        this.setValor(valor);
        return this;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getCodigoPais() {
        return this.codigoPais;
    }

    public MedioContacto codigoPais(String codigoPais) {
        this.setCodigoPais(codigoPais);
        return this;
    }

    public void setCodigoPais(String codigoPais) {
        this.codigoPais = codigoPais;
    }

    public String getAnexo() {
        return this.anexo;
    }

    public MedioContacto anexo(String anexo) {
        this.setAnexo(anexo);
        return this;
    }

    public void setAnexo(String anexo) {
        this.anexo = anexo;
    }

    public Boolean getPrincipal() {
        return this.principal;
    }

    public MedioContacto principal(Boolean principal) {
        this.setPrincipal(principal);
        return this;
    }

    public void setPrincipal(Boolean principal) {
        this.principal = principal;
    }

    public Boolean getVerificado() {
        return this.verificado;
    }

    public MedioContacto verificado(Boolean verificado) {
        this.setVerificado(verificado);
        return this;
    }

    public void setVerificado(Boolean verificado) {
        this.verificado = verificado;
    }

    public Boolean getActivo() {
        return this.activo;
    }

    public MedioContacto activo(Boolean activo) {
        this.setActivo(activo);
        return this;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public MedioContacto createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public MedioContacto createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public MedioContacto lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public MedioContacto lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Persona getPersona() {
        return this.persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public MedioContacto persona(Persona persona) {
        this.setPersona(persona);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MedioContacto)) {
            return false;
        }
        return getId() != null && getId().equals(((MedioContacto) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MedioContacto{" +
            "id=" + getId() +
            ", tipo='" + getTipo() + "'" +
            ", valor='" + getValor() + "'" +
            ", codigoPais='" + getCodigoPais() + "'" +
            ", anexo='" + getAnexo() + "'" +
            ", principal='" + getPrincipal() + "'" +
            ", verificado='" + getVerificado() + "'" +
            ", activo='" + getActivo() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
