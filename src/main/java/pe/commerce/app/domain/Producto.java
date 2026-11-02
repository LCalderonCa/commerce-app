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

/**
 * slug: identificador legible para la URL del producto
 * (ej. /producto/audifonos-sony-wh1000xm5).
 * Precio aplicable: si precioOferta no es nulo manda la oferta,
 * si no, precioLista.
 */
@Entity
@Table(name = "producto")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Producto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 30)
    @Column(name = "sku", length = 30, nullable = false, unique = true)
    private String sku;

    @NotNull
    @Size(max = 200)
    @Column(name = "nombre", length = 200, nullable = false)
    private String nombre;

    @NotNull
    @Size(max = 220)
    @Column(name = "slug", length = 220, nullable = false, unique = true)
    private String slug;

    @Size(max = 80)
    @Column(name = "modelo", length = 80)
    private String modelo;

    @Size(max = 4000)
    @Column(name = "descripcion", length = 4000)
    private String descripcion;

    @Size(max = 4000)
    @Column(name = "especificaciones", length = 4000)
    private String especificaciones;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "precio_lista", precision = 21, scale = 2, nullable = false)
    private BigDecimal precioLista;

    @DecimalMin(value = "0")
    @Column(name = "precio_oferta", precision = 21, scale = 2)
    private BigDecimal precioOferta;

    @DecimalMin(value = "0")
    @Column(name = "peso_kg", precision = 21, scale = 2)
    private BigDecimal pesoKg;

    @Min(value = 0)
    @Column(name = "garantia_meses")
    private Integer garantiaMeses;

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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "producto")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "producto" }, allowSetters = true)
    private Set<ProductoImagen> imageneses = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "producto")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "producto", "tienda" }, allowSetters = true)
    private Set<Inventario> inventarioses = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "producto")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "cliente", "producto" }, allowSetters = true)
    private Set<Resena> resenases = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "subcategoriases", "productoses", "padre" }, allowSetters = true)
    private Categoria categoria;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "productoses" }, allowSetters = true)
    private Marca marca;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Producto id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSku() {
        return this.sku;
    }

    public Producto sku(String sku) {
        this.setSku(sku);
        return this;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Producto nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getSlug() {
        return this.slug;
    }

    public Producto slug(String slug) {
        this.setSlug(slug);
        return this;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getModelo() {
        return this.modelo;
    }

    public Producto modelo(String modelo) {
        this.setModelo(modelo);
        return this;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public Producto descripcion(String descripcion) {
        this.setDescripcion(descripcion);
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEspecificaciones() {
        return this.especificaciones;
    }

    public Producto especificaciones(String especificaciones) {
        this.setEspecificaciones(especificaciones);
        return this;
    }

    public void setEspecificaciones(String especificaciones) {
        this.especificaciones = especificaciones;
    }

    public BigDecimal getPrecioLista() {
        return this.precioLista;
    }

    public Producto precioLista(BigDecimal precioLista) {
        this.setPrecioLista(precioLista);
        return this;
    }

    public void setPrecioLista(BigDecimal precioLista) {
        this.precioLista = precioLista;
    }

    public BigDecimal getPrecioOferta() {
        return this.precioOferta;
    }

    public Producto precioOferta(BigDecimal precioOferta) {
        this.setPrecioOferta(precioOferta);
        return this;
    }

    public void setPrecioOferta(BigDecimal precioOferta) {
        this.precioOferta = precioOferta;
    }

    public BigDecimal getPesoKg() {
        return this.pesoKg;
    }

    public Producto pesoKg(BigDecimal pesoKg) {
        this.setPesoKg(pesoKg);
        return this;
    }

    public void setPesoKg(BigDecimal pesoKg) {
        this.pesoKg = pesoKg;
    }

    public Integer getGarantiaMeses() {
        return this.garantiaMeses;
    }

    public Producto garantiaMeses(Integer garantiaMeses) {
        this.setGarantiaMeses(garantiaMeses);
        return this;
    }

    public void setGarantiaMeses(Integer garantiaMeses) {
        this.garantiaMeses = garantiaMeses;
    }

    public Boolean getActivo() {
        return this.activo;
    }

    public Producto activo(Boolean activo) {
        this.setActivo(activo);
        return this;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public Producto createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public Producto createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public Producto lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public Producto lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Set<ProductoImagen> getImageneses() {
        return this.imageneses;
    }

    public void setImageneses(Set<ProductoImagen> productoImagens) {
        if (this.imageneses != null) {
            this.imageneses.forEach(i -> i.setProducto(null));
        }
        if (productoImagens != null) {
            productoImagens.forEach(i -> i.setProducto(this));
        }
        this.imageneses = productoImagens;
    }

    public Producto imageneses(Set<ProductoImagen> productoImagens) {
        this.setImageneses(productoImagens);
        return this;
    }

    public Producto addImagenes(ProductoImagen productoImagen) {
        this.imageneses.add(productoImagen);
        productoImagen.setProducto(this);
        return this;
    }

    public Producto removeImagenes(ProductoImagen productoImagen) {
        this.imageneses.remove(productoImagen);
        productoImagen.setProducto(null);
        return this;
    }

    public Set<Inventario> getInventarioses() {
        return this.inventarioses;
    }

    public void setInventarioses(Set<Inventario> inventarios) {
        if (this.inventarioses != null) {
            this.inventarioses.forEach(i -> i.setProducto(null));
        }
        if (inventarios != null) {
            inventarios.forEach(i -> i.setProducto(this));
        }
        this.inventarioses = inventarios;
    }

    public Producto inventarioses(Set<Inventario> inventarios) {
        this.setInventarioses(inventarios);
        return this;
    }

    public Producto addInventarios(Inventario inventario) {
        this.inventarioses.add(inventario);
        inventario.setProducto(this);
        return this;
    }

    public Producto removeInventarios(Inventario inventario) {
        this.inventarioses.remove(inventario);
        inventario.setProducto(null);
        return this;
    }

    public Set<Resena> getResenases() {
        return this.resenases;
    }

    public void setResenases(Set<Resena> resenas) {
        if (this.resenases != null) {
            this.resenases.forEach(i -> i.setProducto(null));
        }
        if (resenas != null) {
            resenas.forEach(i -> i.setProducto(this));
        }
        this.resenases = resenas;
    }

    public Producto resenases(Set<Resena> resenas) {
        this.setResenases(resenas);
        return this;
    }

    public Producto addResenas(Resena resena) {
        this.resenases.add(resena);
        resena.setProducto(this);
        return this;
    }

    public Producto removeResenas(Resena resena) {
        this.resenases.remove(resena);
        resena.setProducto(null);
        return this;
    }

    public Categoria getCategoria() {
        return this.categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Producto categoria(Categoria categoria) {
        this.setCategoria(categoria);
        return this;
    }

    public Marca getMarca() {
        return this.marca;
    }

    public void setMarca(Marca marca) {
        this.marca = marca;
    }

    public Producto marca(Marca marca) {
        this.setMarca(marca);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Producto)) {
            return false;
        }
        return getId() != null && getId().equals(((Producto) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Producto{" +
            "id=" + getId() +
            ", sku='" + getSku() + "'" +
            ", nombre='" + getNombre() + "'" +
            ", slug='" + getSlug() + "'" +
            ", modelo='" + getModelo() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", especificaciones='" + getEspecificaciones() + "'" +
            ", precioLista=" + getPrecioLista() +
            ", precioOferta=" + getPrecioOferta() +
            ", pesoKg=" + getPesoKg() +
            ", garantiaMeses=" + getGarantiaMeses() +
            ", activo='" + getActivo() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
