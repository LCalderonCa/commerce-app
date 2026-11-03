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
 * Categoría jerárquica (padre opcional)
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "categoria")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Categoria implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 80)
    @Column(name = "nombre", length = 80, nullable = false)
    private String nombre;

    @NotNull
    @Size(max = 100)
    @Column(name = "slug", length = 100, nullable = false, unique = true)
    private String slug;

    @Column(name = "orden")
    private Integer orden;

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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "padre")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "subcategoriases", "productoses", "padre" }, allowSetters = true)
    private Set<Categoria> subcategoriases = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "categoria")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "imageneses", "inventarioses", "resenases", "categoria", "marca" }, allowSetters = true)
    private Set<Producto> productoses = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "subcategoriases", "productoses", "padre" }, allowSetters = true)
    private Categoria padre;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Categoria id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Categoria nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getSlug() {
        return this.slug;
    }

    public Categoria slug(String slug) {
        this.setSlug(slug);
        return this;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public Integer getOrden() {
        return this.orden;
    }

    public Categoria orden(Integer orden) {
        this.setOrden(orden);
        return this;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public Boolean getActivo() {
        return this.activo;
    }

    public Categoria activo(Boolean activo) {
        this.setActivo(activo);
        return this;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public Categoria createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public Categoria createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public Categoria lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public Categoria lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Set<Categoria> getSubcategoriases() {
        return this.subcategoriases;
    }

    public void setSubcategoriases(Set<Categoria> categorias) {
        if (this.subcategoriases != null) {
            this.subcategoriases.forEach(i -> i.setPadre(null));
        }
        if (categorias != null) {
            categorias.forEach(i -> i.setPadre(this));
        }
        this.subcategoriases = categorias;
    }

    public Categoria subcategoriases(Set<Categoria> categorias) {
        this.setSubcategoriases(categorias);
        return this;
    }

    public Categoria addSubcategorias(Categoria categoria) {
        this.subcategoriases.add(categoria);
        categoria.setPadre(this);
        return this;
    }

    public Categoria removeSubcategorias(Categoria categoria) {
        this.subcategoriases.remove(categoria);
        categoria.setPadre(null);
        return this;
    }

    public Set<Producto> getProductoses() {
        return this.productoses;
    }

    public void setProductoses(Set<Producto> productos) {
        if (this.productoses != null) {
            this.productoses.forEach(i -> i.setCategoria(null));
        }
        if (productos != null) {
            productos.forEach(i -> i.setCategoria(this));
        }
        this.productoses = productos;
    }

    public Categoria productoses(Set<Producto> productos) {
        this.setProductoses(productos);
        return this;
    }

    public Categoria addProductos(Producto producto) {
        this.productoses.add(producto);
        producto.setCategoria(this);
        return this;
    }

    public Categoria removeProductos(Producto producto) {
        this.productoses.remove(producto);
        producto.setCategoria(null);
        return this;
    }

    public Categoria getPadre() {
        return this.padre;
    }

    public void setPadre(Categoria categoria) {
        this.padre = categoria;
    }

    public Categoria padre(Categoria categoria) {
        this.setPadre(categoria);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Categoria)) {
            return false;
        }
        return getId() != null && getId().equals(((Categoria) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Categoria{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", slug='" + getSlug() + "'" +
            ", orden=" + getOrden() +
            ", activo='" + getActivo() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
