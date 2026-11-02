package pe.commerce.app.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import pe.commerce.app.domain.enumeration.TipoMedioContacto;

/**
 * A DTO for the {@link pe.commerce.app.domain.MedioContacto} entity.
 */
@Schema(
    description = "Teléfonos y correos de la persona.\nEl formato de \"valor\" se valida en el service según el tipo.\nUn principal por tipo (uno para teléfonos, uno para correos).\nEl email de login está en User; aquí van los de contacto."
)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MedioContactoDTO implements Serializable {

    private Long id;

    @NotNull
    private TipoMedioContacto tipo;

    @NotNull
    @Size(max = 100)
    private String valor;

    @Size(max = 4)
    private String codigoPais;

    @Size(max = 6)
    private String anexo;

    @NotNull
    private Boolean principal;

    @NotNull
    private Boolean verificado;

    @NotNull
    private Boolean activo;

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

    public TipoMedioContacto getTipo() {
        return tipo;
    }

    public void setTipo(TipoMedioContacto tipo) {
        this.tipo = tipo;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getCodigoPais() {
        return codigoPais;
    }

    public void setCodigoPais(String codigoPais) {
        this.codigoPais = codigoPais;
    }

    public String getAnexo() {
        return anexo;
    }

    public void setAnexo(String anexo) {
        this.anexo = anexo;
    }

    public Boolean getPrincipal() {
        return principal;
    }

    public void setPrincipal(Boolean principal) {
        this.principal = principal;
    }

    public Boolean getVerificado() {
        return verificado;
    }

    public void setVerificado(Boolean verificado) {
        this.verificado = verificado;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
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
        if (!(o instanceof MedioContactoDTO)) {
            return false;
        }

        MedioContactoDTO medioContactoDTO = (MedioContactoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, medioContactoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MedioContactoDTO{" +
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
            ", persona=" + getPersona() +
            "}";
    }
}
