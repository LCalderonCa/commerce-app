package pe.commerce.app.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link pe.commerce.app.domain.Producto} entity. This class is used
 * in {@link pe.commerce.app.web.rest.ProductoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /productos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductoCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter sku;

    private StringFilter nombre;

    private StringFilter slug;

    private StringFilter modelo;

    private StringFilter descripcion;

    private StringFilter especificaciones;

    private BigDecimalFilter precioLista;

    private BigDecimalFilter precioOferta;

    private BigDecimalFilter pesoKg;

    private IntegerFilter garantiaMeses;

    private BooleanFilter activo;

    private StringFilter createdBy;

    private InstantFilter createdDate;

    private StringFilter lastModifiedBy;

    private InstantFilter lastModifiedDate;

    private LongFilter imagenesId;

    private LongFilter inventariosId;

    private LongFilter resenasId;

    private LongFilter categoriaId;

    private LongFilter marcaId;

    private Boolean distinct;

    public ProductoCriteria() {}

    public ProductoCriteria(ProductoCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.sku = other.optionalSku().map(StringFilter::copy).orElse(null);
        this.nombre = other.optionalNombre().map(StringFilter::copy).orElse(null);
        this.slug = other.optionalSlug().map(StringFilter::copy).orElse(null);
        this.modelo = other.optionalModelo().map(StringFilter::copy).orElse(null);
        this.descripcion = other.optionalDescripcion().map(StringFilter::copy).orElse(null);
        this.especificaciones = other.optionalEspecificaciones().map(StringFilter::copy).orElse(null);
        this.precioLista = other.optionalPrecioLista().map(BigDecimalFilter::copy).orElse(null);
        this.precioOferta = other.optionalPrecioOferta().map(BigDecimalFilter::copy).orElse(null);
        this.pesoKg = other.optionalPesoKg().map(BigDecimalFilter::copy).orElse(null);
        this.garantiaMeses = other.optionalGarantiaMeses().map(IntegerFilter::copy).orElse(null);
        this.activo = other.optionalActivo().map(BooleanFilter::copy).orElse(null);
        this.createdBy = other.optionalCreatedBy().map(StringFilter::copy).orElse(null);
        this.createdDate = other.optionalCreatedDate().map(InstantFilter::copy).orElse(null);
        this.lastModifiedBy = other.optionalLastModifiedBy().map(StringFilter::copy).orElse(null);
        this.lastModifiedDate = other.optionalLastModifiedDate().map(InstantFilter::copy).orElse(null);
        this.imagenesId = other.optionalImagenesId().map(LongFilter::copy).orElse(null);
        this.inventariosId = other.optionalInventariosId().map(LongFilter::copy).orElse(null);
        this.resenasId = other.optionalResenasId().map(LongFilter::copy).orElse(null);
        this.categoriaId = other.optionalCategoriaId().map(LongFilter::copy).orElse(null);
        this.marcaId = other.optionalMarcaId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ProductoCriteria copy() {
        return new ProductoCriteria(this);
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

    public StringFilter getSku() {
        return sku;
    }

    public Optional<StringFilter> optionalSku() {
        return Optional.ofNullable(sku);
    }

    public StringFilter sku() {
        if (sku == null) {
            setSku(new StringFilter());
        }
        return sku;
    }

    public void setSku(StringFilter sku) {
        this.sku = sku;
    }

    public StringFilter getNombre() {
        return nombre;
    }

    public Optional<StringFilter> optionalNombre() {
        return Optional.ofNullable(nombre);
    }

    public StringFilter nombre() {
        if (nombre == null) {
            setNombre(new StringFilter());
        }
        return nombre;
    }

    public void setNombre(StringFilter nombre) {
        this.nombre = nombre;
    }

    public StringFilter getSlug() {
        return slug;
    }

    public Optional<StringFilter> optionalSlug() {
        return Optional.ofNullable(slug);
    }

    public StringFilter slug() {
        if (slug == null) {
            setSlug(new StringFilter());
        }
        return slug;
    }

    public void setSlug(StringFilter slug) {
        this.slug = slug;
    }

    public StringFilter getModelo() {
        return modelo;
    }

    public Optional<StringFilter> optionalModelo() {
        return Optional.ofNullable(modelo);
    }

    public StringFilter modelo() {
        if (modelo == null) {
            setModelo(new StringFilter());
        }
        return modelo;
    }

    public void setModelo(StringFilter modelo) {
        this.modelo = modelo;
    }

    public StringFilter getDescripcion() {
        return descripcion;
    }

    public Optional<StringFilter> optionalDescripcion() {
        return Optional.ofNullable(descripcion);
    }

    public StringFilter descripcion() {
        if (descripcion == null) {
            setDescripcion(new StringFilter());
        }
        return descripcion;
    }

    public void setDescripcion(StringFilter descripcion) {
        this.descripcion = descripcion;
    }

    public StringFilter getEspecificaciones() {
        return especificaciones;
    }

    public Optional<StringFilter> optionalEspecificaciones() {
        return Optional.ofNullable(especificaciones);
    }

    public StringFilter especificaciones() {
        if (especificaciones == null) {
            setEspecificaciones(new StringFilter());
        }
        return especificaciones;
    }

    public void setEspecificaciones(StringFilter especificaciones) {
        this.especificaciones = especificaciones;
    }

    public BigDecimalFilter getPrecioLista() {
        return precioLista;
    }

    public Optional<BigDecimalFilter> optionalPrecioLista() {
        return Optional.ofNullable(precioLista);
    }

    public BigDecimalFilter precioLista() {
        if (precioLista == null) {
            setPrecioLista(new BigDecimalFilter());
        }
        return precioLista;
    }

    public void setPrecioLista(BigDecimalFilter precioLista) {
        this.precioLista = precioLista;
    }

    public BigDecimalFilter getPrecioOferta() {
        return precioOferta;
    }

    public Optional<BigDecimalFilter> optionalPrecioOferta() {
        return Optional.ofNullable(precioOferta);
    }

    public BigDecimalFilter precioOferta() {
        if (precioOferta == null) {
            setPrecioOferta(new BigDecimalFilter());
        }
        return precioOferta;
    }

    public void setPrecioOferta(BigDecimalFilter precioOferta) {
        this.precioOferta = precioOferta;
    }

    public BigDecimalFilter getPesoKg() {
        return pesoKg;
    }

    public Optional<BigDecimalFilter> optionalPesoKg() {
        return Optional.ofNullable(pesoKg);
    }

    public BigDecimalFilter pesoKg() {
        if (pesoKg == null) {
            setPesoKg(new BigDecimalFilter());
        }
        return pesoKg;
    }

    public void setPesoKg(BigDecimalFilter pesoKg) {
        this.pesoKg = pesoKg;
    }

    public IntegerFilter getGarantiaMeses() {
        return garantiaMeses;
    }

    public Optional<IntegerFilter> optionalGarantiaMeses() {
        return Optional.ofNullable(garantiaMeses);
    }

    public IntegerFilter garantiaMeses() {
        if (garantiaMeses == null) {
            setGarantiaMeses(new IntegerFilter());
        }
        return garantiaMeses;
    }

    public void setGarantiaMeses(IntegerFilter garantiaMeses) {
        this.garantiaMeses = garantiaMeses;
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

    public LongFilter getImagenesId() {
        return imagenesId;
    }

    public Optional<LongFilter> optionalImagenesId() {
        return Optional.ofNullable(imagenesId);
    }

    public LongFilter imagenesId() {
        if (imagenesId == null) {
            setImagenesId(new LongFilter());
        }
        return imagenesId;
    }

    public void setImagenesId(LongFilter imagenesId) {
        this.imagenesId = imagenesId;
    }

    public LongFilter getInventariosId() {
        return inventariosId;
    }

    public Optional<LongFilter> optionalInventariosId() {
        return Optional.ofNullable(inventariosId);
    }

    public LongFilter inventariosId() {
        if (inventariosId == null) {
            setInventariosId(new LongFilter());
        }
        return inventariosId;
    }

    public void setInventariosId(LongFilter inventariosId) {
        this.inventariosId = inventariosId;
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

    public LongFilter getCategoriaId() {
        return categoriaId;
    }

    public Optional<LongFilter> optionalCategoriaId() {
        return Optional.ofNullable(categoriaId);
    }

    public LongFilter categoriaId() {
        if (categoriaId == null) {
            setCategoriaId(new LongFilter());
        }
        return categoriaId;
    }

    public void setCategoriaId(LongFilter categoriaId) {
        this.categoriaId = categoriaId;
    }

    public LongFilter getMarcaId() {
        return marcaId;
    }

    public Optional<LongFilter> optionalMarcaId() {
        return Optional.ofNullable(marcaId);
    }

    public LongFilter marcaId() {
        if (marcaId == null) {
            setMarcaId(new LongFilter());
        }
        return marcaId;
    }

    public void setMarcaId(LongFilter marcaId) {
        this.marcaId = marcaId;
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
        final ProductoCriteria that = (ProductoCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(sku, that.sku) &&
            Objects.equals(nombre, that.nombre) &&
            Objects.equals(slug, that.slug) &&
            Objects.equals(modelo, that.modelo) &&
            Objects.equals(descripcion, that.descripcion) &&
            Objects.equals(especificaciones, that.especificaciones) &&
            Objects.equals(precioLista, that.precioLista) &&
            Objects.equals(precioOferta, that.precioOferta) &&
            Objects.equals(pesoKg, that.pesoKg) &&
            Objects.equals(garantiaMeses, that.garantiaMeses) &&
            Objects.equals(activo, that.activo) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(lastModifiedBy, that.lastModifiedBy) &&
            Objects.equals(lastModifiedDate, that.lastModifiedDate) &&
            Objects.equals(imagenesId, that.imagenesId) &&
            Objects.equals(inventariosId, that.inventariosId) &&
            Objects.equals(resenasId, that.resenasId) &&
            Objects.equals(categoriaId, that.categoriaId) &&
            Objects.equals(marcaId, that.marcaId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            sku,
            nombre,
            slug,
            modelo,
            descripcion,
            especificaciones,
            precioLista,
            precioOferta,
            pesoKg,
            garantiaMeses,
            activo,
            createdBy,
            createdDate,
            lastModifiedBy,
            lastModifiedDate,
            imagenesId,
            inventariosId,
            resenasId,
            categoriaId,
            marcaId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductoCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalSku().map(f -> "sku=" + f + ", ").orElse("") +
            optionalNombre().map(f -> "nombre=" + f + ", ").orElse("") +
            optionalSlug().map(f -> "slug=" + f + ", ").orElse("") +
            optionalModelo().map(f -> "modelo=" + f + ", ").orElse("") +
            optionalDescripcion().map(f -> "descripcion=" + f + ", ").orElse("") +
            optionalEspecificaciones().map(f -> "especificaciones=" + f + ", ").orElse("") +
            optionalPrecioLista().map(f -> "precioLista=" + f + ", ").orElse("") +
            optionalPrecioOferta().map(f -> "precioOferta=" + f + ", ").orElse("") +
            optionalPesoKg().map(f -> "pesoKg=" + f + ", ").orElse("") +
            optionalGarantiaMeses().map(f -> "garantiaMeses=" + f + ", ").orElse("") +
            optionalActivo().map(f -> "activo=" + f + ", ").orElse("") +
            optionalCreatedBy().map(f -> "createdBy=" + f + ", ").orElse("") +
            optionalCreatedDate().map(f -> "createdDate=" + f + ", ").orElse("") +
            optionalLastModifiedBy().map(f -> "lastModifiedBy=" + f + ", ").orElse("") +
            optionalLastModifiedDate().map(f -> "lastModifiedDate=" + f + ", ").orElse("") +
            optionalImagenesId().map(f -> "imagenesId=" + f + ", ").orElse("") +
            optionalInventariosId().map(f -> "inventariosId=" + f + ", ").orElse("") +
            optionalResenasId().map(f -> "resenasId=" + f + ", ").orElse("") +
            optionalCategoriaId().map(f -> "categoriaId=" + f + ", ").orElse("") +
            optionalMarcaId().map(f -> "marcaId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
