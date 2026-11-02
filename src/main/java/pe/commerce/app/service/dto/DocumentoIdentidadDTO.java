package pe.commerce.app.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;
import pe.commerce.app.domain.enumeration.TipoDocumento;

/**
 * A DTO for the {@link pe.commerce.app.domain.DocumentoIdentidad} entity.
 */
@Schema(
    description = "Documentos de identidad de la persona (historial).\nEj.: CE no vigente -> DNI vigente y principal.\nUnicidad (tipoDocumento, numero): agregar constraint en Liquibase."
)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DocumentoIdentidadDTO implements Serializable {

    private Long id;

    @NotNull
    private TipoDocumento tipoDocumento;

    @NotNull
    @Size(max = 15)
    private String numero;

    private LocalDate fechaVencimiento;

    @NotNull
    private Boolean principal;

    @NotNull
    private Boolean vigente;

    @Size(max = 50)
    private String createdBy;

    private Instant createdDate;

    @Size(max = 50)
    private String lastModifiedBy;

    private Instant lastModifiedDate;

    @NotNull
    private PersonaDTO persona;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoDocumento getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(TipoDocumento tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public Boolean getPrincipal() {
        return principal;
    }

    public void setPrincipal(Boolean principal) {
        this.principal = principal;
    }

    public Boolean getVigente() {
        return vigente;
    }

    public void setVigente(Boolean vigente) {
        this.vigente = vigente;
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

    public PersonaDTO getPersona() {
        return persona;
    }

    public void setPersona(PersonaDTO persona) {
        this.persona = persona;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DocumentoIdentidadDTO)) {
            return false;
        }

        DocumentoIdentidadDTO documentoIdentidadDTO = (DocumentoIdentidadDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, documentoIdentidadDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocumentoIdentidadDTO{" +
            "id=" + getId() +
            ", tipoDocumento='" + getTipoDocumento() + "'" +
            ", numero='" + getNumero() + "'" +
            ", fechaVencimiento='" + getFechaVencimiento() + "'" +
            ", principal='" + getPrincipal() + "'" +
            ", vigente='" + getVigente() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", persona=" + getPersona() +
            "}";
    }
}
