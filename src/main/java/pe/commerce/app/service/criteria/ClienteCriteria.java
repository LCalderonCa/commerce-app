package pe.commerce.app.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link pe.commerce.app.domain.Cliente} entity. This class is used
 * in {@link pe.commerce.app.web.rest.ClienteResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /clientes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ClienteCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter codigo;

    private InstantFilter fechaRegistro;

    private BooleanFilter aceptaPublicidad;

    private BooleanFilter activo;

    private StringFilter createdBy;

    private InstantFilter createdDate;

    private StringFilter lastModifiedBy;

    private InstantFilter lastModifiedDate;

    private LongFilter userId;

    private LongFilter personaId;

    private LongFilter direccionesId;

    private LongFilter datosFacturacionId;

    private LongFilter pedidosId;

    private LongFilter resenasId;

    private LongFilter carritoId;

    private Boolean distinct;

    public ClienteCriteria() {}

    public ClienteCriteria(ClienteCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.codigo = other.optionalCodigo().map(StringFilter::copy).orElse(null);
        this.fechaRegistro = other.optionalFechaRegistro().map(InstantFilter::copy).orElse(null);
        this.aceptaPublicidad = other.optionalAceptaPublicidad().map(BooleanFilter::copy).orElse(null);
        this.activo = other.optionalActivo().map(BooleanFilter::copy).orElse(null);
        this.createdBy = other.optionalCreatedBy().map(StringFilter::copy).orElse(null);
        this.createdDate = other.optionalCreatedDate().map(InstantFilter::copy).orElse(null);
        this.lastModifiedBy = other.optionalLastModifiedBy().map(StringFilter::copy).orElse(null);
        this.lastModifiedDate = other.optionalLastModifiedDate().map(InstantFilter::copy).orElse(null);
        this.userId = other.optionalUserId().map(LongFilter::copy).orElse(null);
        this.personaId = other.optionalPersonaId().map(LongFilter::copy).orElse(null);
        this.direccionesId = other.optionalDireccionesId().map(LongFilter::copy).orElse(null);
        this.datosFacturacionId = other.optionalDatosFacturacionId().map(LongFilter::copy).orElse(null);
        this.pedidosId = other.optionalPedidosId().map(LongFilter::copy).orElse(null);
        this.resenasId = other.optionalResenasId().map(LongFilter::copy).orElse(null);
        this.carritoId = other.optionalCarritoId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ClienteCriteria copy() {
        return new ClienteCriteria(this);
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

    public StringFilter getCodigo() {
        return codigo;
    }

    public Optional<StringFilter> optionalCodigo() {
        return Optional.ofNullable(codigo);
    }

    public StringFilter codigo() {
        if (codigo == null) {
            setCodigo(new StringFilter());
        }
        return codigo;
    }

    public void setCodigo(StringFilter codigo) {
        this.codigo = codigo;
    }

    public InstantFilter getFechaRegistro() {
        return fechaRegistro;
    }

    public Optional<InstantFilter> optionalFechaRegistro() {
        return Optional.ofNullable(fechaRegistro);
    }

    public InstantFilter fechaRegistro() {
        if (fechaRegistro == null) {
            setFechaRegistro(new InstantFilter());
        }
        return fechaRegistro;
    }

    public void setFechaRegistro(InstantFilter fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public BooleanFilter getAceptaPublicidad() {
        return aceptaPublicidad;
    }

    public Optional<BooleanFilter> optionalAceptaPublicidad() {
        return Optional.ofNullable(aceptaPublicidad);
    }

    public BooleanFilter aceptaPublicidad() {
        if (aceptaPublicidad == null) {
            setAceptaPublicidad(new BooleanFilter());
        }
        return aceptaPublicidad;
    }

    public void setAceptaPublicidad(BooleanFilter aceptaPublicidad) {
        this.aceptaPublicidad = aceptaPublicidad;
    }

    public BooleanFilter getActivo() {
        return activo;
    }

    public Optional<BooleanFilter> optionalActivo() {
        return Optional.ofNullable(activo);
    }

    public BooleanFilter activo() {
        if (activo == null) {
            setActivo(new BooleanFilter());
        }
        return activo;
    }

    public void setActivo(BooleanFilter activo) {
        this.activo = activo;
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

    public LongFilter getUserId() {
        return userId;
    }

    public Optional<LongFilter> optionalUserId() {
        return Optional.ofNullable(userId);
    }

    public LongFilter userId() {
        if (userId == null) {
            setUserId(new LongFilter());
        }
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public LongFilter getPersonaId() {
        return personaId;
    }

    public Optional<LongFilter> optionalPersonaId() {
        return Optional.ofNullable(personaId);
    }

    public LongFilter personaId() {
        if (personaId == null) {
            setPersonaId(new LongFilter());
        }
        return personaId;
    }

    public void setPersonaId(LongFilter personaId) {
        this.personaId = personaId;
    }

    public LongFilter getDireccionesId() {
        return direccionesId;
    }

    public Optional<LongFilter> optionalDireccionesId() {
        return Optional.ofNullable(direccionesId);
    }

    public LongFilter direccionesId() {
        if (direccionesId == null) {
            setDireccionesId(new LongFilter());
        }
        return direccionesId;
    }

    public void setDireccionesId(LongFilter direccionesId) {
        this.direccionesId = direccionesId;
    }

    public LongFilter getDatosFacturacionId() {
        return datosFacturacionId;
    }

    public Optional<LongFilter> optionalDatosFacturacionId() {
        return Optional.ofNullable(datosFacturacionId);
    }

    public LongFilter datosFacturacionId() {
        if (datosFacturacionId == null) {
            setDatosFacturacionId(new LongFilter());
        }
        return datosFacturacionId;
    }

    public void setDatosFacturacionId(LongFilter datosFacturacionId) {
        this.datosFacturacionId = datosFacturacionId;
    }

    public LongFilter getPedidosId() {
        return pedidosId;
    }

    public Optional<LongFilter> optionalPedidosId() {
        return Optional.ofNullable(pedidosId);
    }

    public LongFilter pedidosId() {
        if (pedidosId == null) {
            setPedidosId(new LongFilter());
        }
        return pedidosId;
    }

    public void setPedidosId(LongFilter pedidosId) {
        this.pedidosId = pedidosId;
    }

    public LongFilter getResenasId() {
        return resenasId;
    }

    public Optional<LongFilter> optionalResenasId() {
        return Optional.ofNullable(resenasId);
    }

    public LongFilter resenasId() {
        if (resenasId == null) {
            setResenasId(new LongFilter());
        }
        return resenasId;
    }

    public void setResenasId(LongFilter resenasId) {
        this.resenasId = resenasId;
    }

    public LongFilter getCarritoId() {
        return carritoId;
    }

    public Optional<LongFilter> optionalCarritoId() {
        return Optional.ofNullable(carritoId);
    }

    public LongFilter carritoId() {
        if (carritoId == null) {
            setCarritoId(new LongFilter());
        }
        return carritoId;
    }

    public void setCarritoId(LongFilter carritoId) {
        this.carritoId = carritoId;
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
        final ClienteCriteria that = (ClienteCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(codigo, that.codigo) &&
            Objects.equals(fechaRegistro, that.fechaRegistro) &&
            Objects.equals(aceptaPublicidad, that.aceptaPublicidad) &&
            Objects.equals(activo, that.activo) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(lastModifiedBy, that.lastModifiedBy) &&
            Objects.equals(lastModifiedDate, that.lastModifiedDate) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(personaId, that.personaId) &&
            Objects.equals(direccionesId, that.direccionesId) &&
            Objects.equals(datosFacturacionId, that.datosFacturacionId) &&
            Objects.equals(pedidosId, that.pedidosId) &&
            Objects.equals(resenasId, that.resenasId) &&
            Objects.equals(carritoId, that.carritoId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            codigo,
            fechaRegistro,
            aceptaPublicidad,
            activo,
            createdBy,
            createdDate,
            lastModifiedBy,
            lastModifiedDate,
            userId,
            personaId,
            direccionesId,
            datosFacturacionId,
            pedidosId,
            resenasId,
            carritoId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClienteCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalCodigo().map(f -> "codigo=" + f + ", ").orElse("") +
            optionalFechaRegistro().map(f -> "fechaRegistro=" + f + ", ").orElse("") +
            optionalAceptaPublicidad().map(f -> "aceptaPublicidad=" + f + ", ").orElse("") +
            optionalActivo().map(f -> "activo=" + f + ", ").orElse("") +
            optionalCreatedBy().map(f -> "createdBy=" + f + ", ").orElse("") +
            optionalCreatedDate().map(f -> "createdDate=" + f + ", ").orElse("") +
            optionalLastModifiedBy().map(f -> "lastModifiedBy=" + f + ", ").orElse("") +
            optionalLastModifiedDate().map(f -> "lastModifiedDate=" + f + ", ").orElse("") +
            optionalUserId().map(f -> "userId=" + f + ", ").orElse("") +
            optionalPersonaId().map(f -> "personaId=" + f + ", ").orElse("") +
            optionalDireccionesId().map(f -> "direccionesId=" + f + ", ").orElse("") +
            optionalDatosFacturacionId().map(f -> "datosFacturacionId=" + f + ", ").orElse("") +
            optionalPedidosId().map(f -> "pedidosId=" + f + ", ").orElse("") +
            optionalResenasId().map(f -> "resenasId=" + f + ", ").orElse("") +
            optionalCarritoId().map(f -> "carritoId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
