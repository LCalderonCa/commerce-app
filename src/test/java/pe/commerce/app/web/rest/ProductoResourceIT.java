package pe.commerce.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pe.commerce.app.domain.ProductoAsserts.*;
import static pe.commerce.app.web.rest.TestUtil.createUpdateProxyForBean;
import static pe.commerce.app.web.rest.TestUtil.sameNumber;

import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import pe.commerce.app.IntegrationTest;
import pe.commerce.app.domain.Categoria;
import pe.commerce.app.domain.Marca;
import pe.commerce.app.domain.Producto;
import pe.commerce.app.repository.ProductoRepository;
import pe.commerce.app.service.ProductoService;
import pe.commerce.app.service.dto.ProductoDTO;
import pe.commerce.app.service.mapper.ProductoMapper;
import tools.jackson.databind.ObjectMapper;

/**
 * Integration tests for the {@link ProductoResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ProductoResourceIT {

    private static final String DEFAULT_SKU = "AAAAAAAAAA";
    private static final String UPDATED_SKU = "BBBBBBBBBB";

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_SLUG = "AAAAAAAAAA";
    private static final String UPDATED_SLUG = "BBBBBBBBBB";

    private static final String DEFAULT_MODELO = "AAAAAAAAAA";
    private static final String UPDATED_MODELO = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final String DEFAULT_ESPECIFICACIONES = "AAAAAAAAAA";
    private static final String UPDATED_ESPECIFICACIONES = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_PRECIO_LISTA = new BigDecimal(0);
    private static final BigDecimal UPDATED_PRECIO_LISTA = new BigDecimal(1);
    private static final BigDecimal SMALLER_PRECIO_LISTA = new BigDecimal(0 - 1);

    private static final BigDecimal DEFAULT_PRECIO_OFERTA = new BigDecimal(0);
    private static final BigDecimal UPDATED_PRECIO_OFERTA = new BigDecimal(1);
    private static final BigDecimal SMALLER_PRECIO_OFERTA = new BigDecimal(0 - 1);

    private static final BigDecimal DEFAULT_PESO_KG = new BigDecimal(0);
    private static final BigDecimal UPDATED_PESO_KG = new BigDecimal(1);
    private static final BigDecimal SMALLER_PESO_KG = new BigDecimal(0 - 1);

    private static final Integer DEFAULT_GARANTIA_MESES = 0;
    private static final Integer UPDATED_GARANTIA_MESES = 1;
    private static final Integer SMALLER_GARANTIA_MESES = 0 - 1;

    private static final Boolean DEFAULT_ACTIVO = false;
    private static final Boolean UPDATED_ACTIVO = true;

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.ofEpochMilli(1702246707543L);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.ofEpochMilli(1702246707543L);

    private static final String ENTITY_API_URL = "/api/productos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProductoRepository productoRepository;

    @Mock
    private ProductoRepository productoRepositoryMock;

    @Autowired
    private ProductoMapper productoMapper;

    @Mock
    private ProductoService productoServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductoMockMvc;

    private Producto producto;

    private Producto insertedProducto;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Producto createEntity(EntityManager em) {
        Producto producto = new Producto()
            .sku(DEFAULT_SKU)
            .nombre(DEFAULT_NOMBRE)
            .slug(DEFAULT_SLUG)
            .modelo(DEFAULT_MODELO)
            .descripcion(DEFAULT_DESCRIPCION)
            .especificaciones(DEFAULT_ESPECIFICACIONES)
            .precioLista(DEFAULT_PRECIO_LISTA)
            .precioOferta(DEFAULT_PRECIO_OFERTA)
            .pesoKg(DEFAULT_PESO_KG)
            .garantiaMeses(DEFAULT_GARANTIA_MESES)
            .activo(DEFAULT_ACTIVO)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        // Add required entity
        Categoria categoria;
        if (TestUtil.findAll(em, Categoria.class).isEmpty()) {
            categoria = CategoriaResourceIT.createEntity();
            em.persist(categoria);
            em.flush();
        } else {
            categoria = TestUtil.findAll(em, Categoria.class).getFirst();
        }
        producto.setCategoria(categoria);
        // Add required entity
        Marca marca;
        if (TestUtil.findAll(em, Marca.class).isEmpty()) {
            marca = MarcaResourceIT.createEntity();
            em.persist(marca);
            em.flush();
        } else {
            marca = TestUtil.findAll(em, Marca.class).getFirst();
        }
        producto.setMarca(marca);
        return producto;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Producto createUpdatedEntity(EntityManager em) {
        Producto updatedProducto = new Producto()
            .sku(UPDATED_SKU)
            .nombre(UPDATED_NOMBRE)
            .slug(UPDATED_SLUG)
            .modelo(UPDATED_MODELO)
            .descripcion(UPDATED_DESCRIPCION)
            .especificaciones(UPDATED_ESPECIFICACIONES)
            .precioLista(UPDATED_PRECIO_LISTA)
            .precioOferta(UPDATED_PRECIO_OFERTA)
            .pesoKg(UPDATED_PESO_KG)
            .garantiaMeses(UPDATED_GARANTIA_MESES)
            .activo(UPDATED_ACTIVO)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        // Add required entity
        Categoria categoria;
        if (TestUtil.findAll(em, Categoria.class).isEmpty()) {
            categoria = CategoriaResourceIT.createUpdatedEntity();
            em.persist(categoria);
            em.flush();
        } else {
            categoria = TestUtil.findAll(em, Categoria.class).getFirst();
        }
        updatedProducto.setCategoria(categoria);
        // Add required entity
        Marca marca;
        if (TestUtil.findAll(em, Marca.class).isEmpty()) {
            marca = MarcaResourceIT.createUpdatedEntity();
            em.persist(marca);
            em.flush();
        } else {
            marca = TestUtil.findAll(em, Marca.class).getFirst();
        }
        updatedProducto.setMarca(marca);
        return updatedProducto;
    }

    @BeforeEach
    void initTest() {
        producto = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedProducto != null) {
            productoRepository.delete(insertedProducto);
            insertedProducto = null;
        }
    }

    @Test
    @Transactional
    void createProducto() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Producto
        ProductoDTO productoDTO = productoMapper.toDto(producto);
        var returnedProductoDTO = om.readValue(
            restProductoMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productoDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProductoDTO.class
        );

        // Validate the Producto in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedProducto = productoMapper.toEntity(returnedProductoDTO);
        assertProductoUpdatableFieldsEquals(returnedProducto, getPersistedProducto(returnedProducto));

        insertedProducto = returnedProducto;
    }

    @Test
    @Transactional
    void createProductoWithExistingId() throws Exception {
        // Create the Producto with an existing ID
        producto.setId(1L);
        ProductoDTO productoDTO = productoMapper.toDto(producto);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Producto in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSkuIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        producto.setSku(null);

        // Create the Producto, which fails.
        ProductoDTO productoDTO = productoMapper.toDto(producto);

        restProductoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        producto.setNombre(null);

        // Create the Producto, which fails.
        ProductoDTO productoDTO = productoMapper.toDto(producto);

        restProductoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSlugIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        producto.setSlug(null);

        // Create the Producto, which fails.
        ProductoDTO productoDTO = productoMapper.toDto(producto);

        restProductoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPrecioListaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        producto.setPrecioLista(null);

        // Create the Producto, which fails.
        ProductoDTO productoDTO = productoMapper.toDto(producto);

        restProductoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActivoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        producto.setActivo(null);

        // Create the Producto, which fails.
        ProductoDTO productoDTO = productoMapper.toDto(producto);

        restProductoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProductos() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList
        restProductoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(producto.getId().intValue())))
            .andExpect(jsonPath("$.[*].sku").value(hasItem(DEFAULT_SKU)))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].slug").value(hasItem(DEFAULT_SLUG)))
            .andExpect(jsonPath("$.[*].modelo").value(hasItem(DEFAULT_MODELO)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].especificaciones").value(hasItem(DEFAULT_ESPECIFICACIONES)))
            .andExpect(jsonPath("$.[*].precioLista").value(hasItem(sameNumber(DEFAULT_PRECIO_LISTA))))
            .andExpect(jsonPath("$.[*].precioOferta").value(hasItem(sameNumber(DEFAULT_PRECIO_OFERTA))))
            .andExpect(jsonPath("$.[*].pesoKg").value(hasItem(sameNumber(DEFAULT_PESO_KG))))
            .andExpect(jsonPath("$.[*].garantiaMeses").value(hasItem(DEFAULT_GARANTIA_MESES)))
            .andExpect(jsonPath("$.[*].activo").value(hasItem(DEFAULT_ACTIVO)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProductosWithEagerRelationshipsIsEnabled() throws Exception {
        when(productoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProductoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(productoServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProductosWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(productoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProductoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(productoRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getProducto() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get the producto
        restProductoMockMvc
            .perform(get(ENTITY_API_URL_ID, producto.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(producto.getId().intValue()))
            .andExpect(jsonPath("$.sku").value(DEFAULT_SKU))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.slug").value(DEFAULT_SLUG))
            .andExpect(jsonPath("$.modelo").value(DEFAULT_MODELO))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION))
            .andExpect(jsonPath("$.especificaciones").value(DEFAULT_ESPECIFICACIONES))
            .andExpect(jsonPath("$.precioLista").value(sameNumber(DEFAULT_PRECIO_LISTA)))
            .andExpect(jsonPath("$.precioOferta").value(sameNumber(DEFAULT_PRECIO_OFERTA)))
            .andExpect(jsonPath("$.pesoKg").value(sameNumber(DEFAULT_PESO_KG)))
            .andExpect(jsonPath("$.garantiaMeses").value(DEFAULT_GARANTIA_MESES))
            .andExpect(jsonPath("$.activo").value(DEFAULT_ACTIVO))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getProductosByIdFiltering() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        Long id = producto.getId();

        defaultProductoFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultProductoFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultProductoFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProductosBySkuIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where sku equals to
        defaultProductoFiltering("sku.equals=" + DEFAULT_SKU, "sku.equals=" + UPDATED_SKU);
    }

    @Test
    @Transactional
    void getAllProductosBySkuIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where sku in
        defaultProductoFiltering("sku.in=" + DEFAULT_SKU + "," + UPDATED_SKU, "sku.in=" + UPDATED_SKU);
    }

    @Test
    @Transactional
    void getAllProductosBySkuIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where sku is not null
        defaultProductoFiltering("sku.specified=true", "sku.specified=false");
    }

    @Test
    @Transactional
    void getAllProductosBySkuContainsSomething() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where sku contains
        defaultProductoFiltering("sku.contains=" + DEFAULT_SKU, "sku.contains=" + UPDATED_SKU);
    }

    @Test
    @Transactional
    void getAllProductosBySkuNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where sku does not contain
        defaultProductoFiltering("sku.doesNotContain=" + UPDATED_SKU, "sku.doesNotContain=" + DEFAULT_SKU);
    }

    @Test
    @Transactional
    void getAllProductosByNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where nombre equals to
        defaultProductoFiltering("nombre.equals=" + DEFAULT_NOMBRE, "nombre.equals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllProductosByNombreIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where nombre in
        defaultProductoFiltering("nombre.in=" + DEFAULT_NOMBRE + "," + UPDATED_NOMBRE, "nombre.in=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllProductosByNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where nombre is not null
        defaultProductoFiltering("nombre.specified=true", "nombre.specified=false");
    }

    @Test
    @Transactional
    void getAllProductosByNombreContainsSomething() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where nombre contains
        defaultProductoFiltering("nombre.contains=" + DEFAULT_NOMBRE, "nombre.contains=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllProductosByNombreNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where nombre does not contain
        defaultProductoFiltering("nombre.doesNotContain=" + UPDATED_NOMBRE, "nombre.doesNotContain=" + DEFAULT_NOMBRE);
    }

    @Test
    @Transactional
    void getAllProductosBySlugIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where slug equals to
        defaultProductoFiltering("slug.equals=" + DEFAULT_SLUG, "slug.equals=" + UPDATED_SLUG);
    }

    @Test
    @Transactional
    void getAllProductosBySlugIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where slug in
        defaultProductoFiltering("slug.in=" + DEFAULT_SLUG + "," + UPDATED_SLUG, "slug.in=" + UPDATED_SLUG);
    }

    @Test
    @Transactional
    void getAllProductosBySlugIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where slug is not null
        defaultProductoFiltering("slug.specified=true", "slug.specified=false");
    }

    @Test
    @Transactional
    void getAllProductosBySlugContainsSomething() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where slug contains
        defaultProductoFiltering("slug.contains=" + DEFAULT_SLUG, "slug.contains=" + UPDATED_SLUG);
    }

    @Test
    @Transactional
    void getAllProductosBySlugNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where slug does not contain
        defaultProductoFiltering("slug.doesNotContain=" + UPDATED_SLUG, "slug.doesNotContain=" + DEFAULT_SLUG);
    }

    @Test
    @Transactional
    void getAllProductosByModeloIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where modelo equals to
        defaultProductoFiltering("modelo.equals=" + DEFAULT_MODELO, "modelo.equals=" + UPDATED_MODELO);
    }

    @Test
    @Transactional
    void getAllProductosByModeloIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where modelo in
        defaultProductoFiltering("modelo.in=" + DEFAULT_MODELO + "," + UPDATED_MODELO, "modelo.in=" + UPDATED_MODELO);
    }

    @Test
    @Transactional
    void getAllProductosByModeloIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where modelo is not null
        defaultProductoFiltering("modelo.specified=true", "modelo.specified=false");
    }

    @Test
    @Transactional
    void getAllProductosByModeloContainsSomething() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where modelo contains
        defaultProductoFiltering("modelo.contains=" + DEFAULT_MODELO, "modelo.contains=" + UPDATED_MODELO);
    }

    @Test
    @Transactional
    void getAllProductosByModeloNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where modelo does not contain
        defaultProductoFiltering("modelo.doesNotContain=" + UPDATED_MODELO, "modelo.doesNotContain=" + DEFAULT_MODELO);
    }

    @Test
    @Transactional
    void getAllProductosByDescripcionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where descripcion equals to
        defaultProductoFiltering("descripcion.equals=" + DEFAULT_DESCRIPCION, "descripcion.equals=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllProductosByDescripcionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where descripcion in
        defaultProductoFiltering(
            "descripcion.in=" + DEFAULT_DESCRIPCION + "," + UPDATED_DESCRIPCION,
            "descripcion.in=" + UPDATED_DESCRIPCION
        );
    }

    @Test
    @Transactional
    void getAllProductosByDescripcionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where descripcion is not null
        defaultProductoFiltering("descripcion.specified=true", "descripcion.specified=false");
    }

    @Test
    @Transactional
    void getAllProductosByDescripcionContainsSomething() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where descripcion contains
        defaultProductoFiltering("descripcion.contains=" + DEFAULT_DESCRIPCION, "descripcion.contains=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllProductosByDescripcionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where descripcion does not contain
        defaultProductoFiltering("descripcion.doesNotContain=" + UPDATED_DESCRIPCION, "descripcion.doesNotContain=" + DEFAULT_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllProductosByEspecificacionesIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where especificaciones equals to
        defaultProductoFiltering(
            "especificaciones.equals=" + DEFAULT_ESPECIFICACIONES,
            "especificaciones.equals=" + UPDATED_ESPECIFICACIONES
        );
    }

    @Test
    @Transactional
    void getAllProductosByEspecificacionesIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where especificaciones in
        defaultProductoFiltering(
            "especificaciones.in=" + DEFAULT_ESPECIFICACIONES + "," + UPDATED_ESPECIFICACIONES,
            "especificaciones.in=" + UPDATED_ESPECIFICACIONES
        );
    }

    @Test
    @Transactional
    void getAllProductosByEspecificacionesIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where especificaciones is not null
        defaultProductoFiltering("especificaciones.specified=true", "especificaciones.specified=false");
    }

    @Test
    @Transactional
    void getAllProductosByEspecificacionesContainsSomething() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where especificaciones contains
        defaultProductoFiltering(
            "especificaciones.contains=" + DEFAULT_ESPECIFICACIONES,
            "especificaciones.contains=" + UPDATED_ESPECIFICACIONES
        );
    }

    @Test
    @Transactional
    void getAllProductosByEspecificacionesNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where especificaciones does not contain
        defaultProductoFiltering(
            "especificaciones.doesNotContain=" + UPDATED_ESPECIFICACIONES,
            "especificaciones.doesNotContain=" + DEFAULT_ESPECIFICACIONES
        );
    }

    @Test
    @Transactional
    void getAllProductosByPrecioListaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where precioLista equals to
        defaultProductoFiltering("precioLista.equals=" + DEFAULT_PRECIO_LISTA, "precioLista.equals=" + UPDATED_PRECIO_LISTA);
    }

    @Test
    @Transactional
    void getAllProductosByPrecioListaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where precioLista in
        defaultProductoFiltering(
            "precioLista.in=" + DEFAULT_PRECIO_LISTA + "," + UPDATED_PRECIO_LISTA,
            "precioLista.in=" + UPDATED_PRECIO_LISTA
        );
    }

    @Test
    @Transactional
    void getAllProductosByPrecioListaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where precioLista is not null
        defaultProductoFiltering("precioLista.specified=true", "precioLista.specified=false");
    }

    @Test
    @Transactional
    void getAllProductosByPrecioListaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where precioLista is greater than or equal to
        defaultProductoFiltering(
            "precioLista.greaterThanOrEqual=" + DEFAULT_PRECIO_LISTA,
            "precioLista.greaterThanOrEqual=" + UPDATED_PRECIO_LISTA
        );
    }

    @Test
    @Transactional
    void getAllProductosByPrecioListaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where precioLista is less than or equal to
        defaultProductoFiltering(
            "precioLista.lessThanOrEqual=" + DEFAULT_PRECIO_LISTA,
            "precioLista.lessThanOrEqual=" + SMALLER_PRECIO_LISTA
        );
    }

    @Test
    @Transactional
    void getAllProductosByPrecioListaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where precioLista is less than
        defaultProductoFiltering("precioLista.lessThan=" + UPDATED_PRECIO_LISTA, "precioLista.lessThan=" + DEFAULT_PRECIO_LISTA);
    }

    @Test
    @Transactional
    void getAllProductosByPrecioListaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where precioLista is greater than
        defaultProductoFiltering("precioLista.greaterThan=" + SMALLER_PRECIO_LISTA, "precioLista.greaterThan=" + DEFAULT_PRECIO_LISTA);
    }

    @Test
    @Transactional
    void getAllProductosByPrecioOfertaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where precioOferta equals to
        defaultProductoFiltering("precioOferta.equals=" + DEFAULT_PRECIO_OFERTA, "precioOferta.equals=" + UPDATED_PRECIO_OFERTA);
    }

    @Test
    @Transactional
    void getAllProductosByPrecioOfertaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where precioOferta in
        defaultProductoFiltering(
            "precioOferta.in=" + DEFAULT_PRECIO_OFERTA + "," + UPDATED_PRECIO_OFERTA,
            "precioOferta.in=" + UPDATED_PRECIO_OFERTA
        );
    }

    @Test
    @Transactional
    void getAllProductosByPrecioOfertaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where precioOferta is not null
        defaultProductoFiltering("precioOferta.specified=true", "precioOferta.specified=false");
    }

    @Test
    @Transactional
    void getAllProductosByPrecioOfertaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where precioOferta is greater than or equal to
        defaultProductoFiltering(
            "precioOferta.greaterThanOrEqual=" + DEFAULT_PRECIO_OFERTA,
            "precioOferta.greaterThanOrEqual=" + UPDATED_PRECIO_OFERTA
        );
    }

    @Test
    @Transactional
    void getAllProductosByPrecioOfertaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where precioOferta is less than or equal to
        defaultProductoFiltering(
            "precioOferta.lessThanOrEqual=" + DEFAULT_PRECIO_OFERTA,
            "precioOferta.lessThanOrEqual=" + SMALLER_PRECIO_OFERTA
        );
    }

    @Test
    @Transactional
    void getAllProductosByPrecioOfertaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where precioOferta is less than
        defaultProductoFiltering("precioOferta.lessThan=" + UPDATED_PRECIO_OFERTA, "precioOferta.lessThan=" + DEFAULT_PRECIO_OFERTA);
    }

    @Test
    @Transactional
    void getAllProductosByPrecioOfertaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where precioOferta is greater than
        defaultProductoFiltering("precioOferta.greaterThan=" + SMALLER_PRECIO_OFERTA, "precioOferta.greaterThan=" + DEFAULT_PRECIO_OFERTA);
    }

    @Test
    @Transactional
    void getAllProductosByPesoKgIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where pesoKg equals to
        defaultProductoFiltering("pesoKg.equals=" + DEFAULT_PESO_KG, "pesoKg.equals=" + UPDATED_PESO_KG);
    }

    @Test
    @Transactional
    void getAllProductosByPesoKgIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where pesoKg in
        defaultProductoFiltering("pesoKg.in=" + DEFAULT_PESO_KG + "," + UPDATED_PESO_KG, "pesoKg.in=" + UPDATED_PESO_KG);
    }

    @Test
    @Transactional
    void getAllProductosByPesoKgIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where pesoKg is not null
        defaultProductoFiltering("pesoKg.specified=true", "pesoKg.specified=false");
    }

    @Test
    @Transactional
    void getAllProductosByPesoKgIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where pesoKg is greater than or equal to
        defaultProductoFiltering("pesoKg.greaterThanOrEqual=" + DEFAULT_PESO_KG, "pesoKg.greaterThanOrEqual=" + UPDATED_PESO_KG);
    }

    @Test
    @Transactional
    void getAllProductosByPesoKgIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where pesoKg is less than or equal to
        defaultProductoFiltering("pesoKg.lessThanOrEqual=" + DEFAULT_PESO_KG, "pesoKg.lessThanOrEqual=" + SMALLER_PESO_KG);
    }

    @Test
    @Transactional
    void getAllProductosByPesoKgIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where pesoKg is less than
        defaultProductoFiltering("pesoKg.lessThan=" + UPDATED_PESO_KG, "pesoKg.lessThan=" + DEFAULT_PESO_KG);
    }

    @Test
    @Transactional
    void getAllProductosByPesoKgIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where pesoKg is greater than
        defaultProductoFiltering("pesoKg.greaterThan=" + SMALLER_PESO_KG, "pesoKg.greaterThan=" + DEFAULT_PESO_KG);
    }

    @Test
    @Transactional
    void getAllProductosByGarantiaMesesIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where garantiaMeses equals to
        defaultProductoFiltering("garantiaMeses.equals=" + DEFAULT_GARANTIA_MESES, "garantiaMeses.equals=" + UPDATED_GARANTIA_MESES);
    }

    @Test
    @Transactional
    void getAllProductosByGarantiaMesesIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where garantiaMeses in
        defaultProductoFiltering(
            "garantiaMeses.in=" + DEFAULT_GARANTIA_MESES + "," + UPDATED_GARANTIA_MESES,
            "garantiaMeses.in=" + UPDATED_GARANTIA_MESES
        );
    }

    @Test
    @Transactional
    void getAllProductosByGarantiaMesesIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where garantiaMeses is not null
        defaultProductoFiltering("garantiaMeses.specified=true", "garantiaMeses.specified=false");
    }

    @Test
    @Transactional
    void getAllProductosByGarantiaMesesIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where garantiaMeses is greater than or equal to
        defaultProductoFiltering(
            "garantiaMeses.greaterThanOrEqual=" + DEFAULT_GARANTIA_MESES,
            "garantiaMeses.greaterThanOrEqual=" + UPDATED_GARANTIA_MESES
        );
    }

    @Test
    @Transactional
    void getAllProductosByGarantiaMesesIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where garantiaMeses is less than or equal to
        defaultProductoFiltering(
            "garantiaMeses.lessThanOrEqual=" + DEFAULT_GARANTIA_MESES,
            "garantiaMeses.lessThanOrEqual=" + SMALLER_GARANTIA_MESES
        );
    }

    @Test
    @Transactional
    void getAllProductosByGarantiaMesesIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where garantiaMeses is less than
        defaultProductoFiltering("garantiaMeses.lessThan=" + UPDATED_GARANTIA_MESES, "garantiaMeses.lessThan=" + DEFAULT_GARANTIA_MESES);
    }

    @Test
    @Transactional
    void getAllProductosByGarantiaMesesIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where garantiaMeses is greater than
        defaultProductoFiltering(
            "garantiaMeses.greaterThan=" + SMALLER_GARANTIA_MESES,
            "garantiaMeses.greaterThan=" + DEFAULT_GARANTIA_MESES
        );
    }

    @Test
    @Transactional
    void getAllProductosByActivoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where activo equals to
        defaultProductoFiltering("activo.equals=" + DEFAULT_ACTIVO, "activo.equals=" + UPDATED_ACTIVO);
    }

    @Test
    @Transactional
    void getAllProductosByActivoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where activo in
        defaultProductoFiltering("activo.in=" + DEFAULT_ACTIVO + "," + UPDATED_ACTIVO, "activo.in=" + UPDATED_ACTIVO);
    }

    @Test
    @Transactional
    void getAllProductosByActivoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where activo is not null
        defaultProductoFiltering("activo.specified=true", "activo.specified=false");
    }

    @Test
    @Transactional
    void getAllProductosByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where createdBy equals to
        defaultProductoFiltering("createdBy.equals=" + DEFAULT_CREATED_BY, "createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllProductosByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where createdBy in
        defaultProductoFiltering("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY, "createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllProductosByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where createdBy is not null
        defaultProductoFiltering("createdBy.specified=true", "createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllProductosByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where createdBy contains
        defaultProductoFiltering("createdBy.contains=" + DEFAULT_CREATED_BY, "createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllProductosByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where createdBy does not contain
        defaultProductoFiltering("createdBy.doesNotContain=" + UPDATED_CREATED_BY, "createdBy.doesNotContain=" + DEFAULT_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllProductosByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where createdDate equals to
        defaultProductoFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllProductosByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where createdDate in
        defaultProductoFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllProductosByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where createdDate is not null
        defaultProductoFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllProductosByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where lastModifiedBy equals to
        defaultProductoFiltering("lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY, "lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllProductosByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where lastModifiedBy in
        defaultProductoFiltering(
            "lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY,
            "lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllProductosByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where lastModifiedBy is not null
        defaultProductoFiltering("lastModifiedBy.specified=true", "lastModifiedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllProductosByLastModifiedByContainsSomething() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where lastModifiedBy contains
        defaultProductoFiltering(
            "lastModifiedBy.contains=" + DEFAULT_LAST_MODIFIED_BY,
            "lastModifiedBy.contains=" + UPDATED_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllProductosByLastModifiedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where lastModifiedBy does not contain
        defaultProductoFiltering(
            "lastModifiedBy.doesNotContain=" + UPDATED_LAST_MODIFIED_BY,
            "lastModifiedBy.doesNotContain=" + DEFAULT_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllProductosByLastModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where lastModifiedDate equals to
        defaultProductoFiltering(
            "lastModifiedDate.equals=" + DEFAULT_LAST_MODIFIED_DATE,
            "lastModifiedDate.equals=" + UPDATED_LAST_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllProductosByLastModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where lastModifiedDate in
        defaultProductoFiltering(
            "lastModifiedDate.in=" + DEFAULT_LAST_MODIFIED_DATE + "," + UPDATED_LAST_MODIFIED_DATE,
            "lastModifiedDate.in=" + UPDATED_LAST_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllProductosByLastModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where lastModifiedDate is not null
        defaultProductoFiltering("lastModifiedDate.specified=true", "lastModifiedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllProductosByCategoriaIsEqualToSomething() throws Exception {
        Categoria categoria;
        if (TestUtil.findAll(em, Categoria.class).isEmpty()) {
            productoRepository.saveAndFlush(producto);
            categoria = CategoriaResourceIT.createEntity();
        } else {
            categoria = TestUtil.findAll(em, Categoria.class).getFirst();
        }
        em.persist(categoria);
        em.flush();
        producto.setCategoria(categoria);
        productoRepository.saveAndFlush(producto);
        Long categoriaId = categoria.getId();
        // Get all the productoList where categoria equals to categoriaId
        defaultProductoShouldBeFound("categoriaId.equals=" + categoriaId);

        // Get all the productoList where categoria equals to (categoriaId + 1)
        defaultProductoShouldNotBeFound("categoriaId.equals=" + (categoriaId + 1));
    }

    @Test
    @Transactional
    void getAllProductosByMarcaIsEqualToSomething() throws Exception {
        Marca marca;
        if (TestUtil.findAll(em, Marca.class).isEmpty()) {
            productoRepository.saveAndFlush(producto);
            marca = MarcaResourceIT.createEntity();
        } else {
            marca = TestUtil.findAll(em, Marca.class).getFirst();
        }
        em.persist(marca);
        em.flush();
        producto.setMarca(marca);
        productoRepository.saveAndFlush(producto);
        Long marcaId = marca.getId();
        // Get all the productoList where marca equals to marcaId
        defaultProductoShouldBeFound("marcaId.equals=" + marcaId);

        // Get all the productoList where marca equals to (marcaId + 1)
        defaultProductoShouldNotBeFound("marcaId.equals=" + (marcaId + 1));
    }

    private void defaultProductoFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultProductoShouldBeFound(shouldBeFound);
        defaultProductoShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProductoShouldBeFound(String filter) throws Exception {
        restProductoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(producto.getId().intValue())))
            .andExpect(jsonPath("$.[*].sku").value(hasItem(DEFAULT_SKU)))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].slug").value(hasItem(DEFAULT_SLUG)))
            .andExpect(jsonPath("$.[*].modelo").value(hasItem(DEFAULT_MODELO)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].especificaciones").value(hasItem(DEFAULT_ESPECIFICACIONES)))
            .andExpect(jsonPath("$.[*].precioLista").value(hasItem(sameNumber(DEFAULT_PRECIO_LISTA))))
            .andExpect(jsonPath("$.[*].precioOferta").value(hasItem(sameNumber(DEFAULT_PRECIO_OFERTA))))
            .andExpect(jsonPath("$.[*].pesoKg").value(hasItem(sameNumber(DEFAULT_PESO_KG))))
            .andExpect(jsonPath("$.[*].garantiaMeses").value(hasItem(DEFAULT_GARANTIA_MESES)))
            .andExpect(jsonPath("$.[*].activo").value(hasItem(DEFAULT_ACTIVO)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));

        // Check, that the count call also returns 1
        restProductoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProductoShouldNotBeFound(String filter) throws Exception {
        restProductoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProductoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProducto() throws Exception {
        // Get the producto
        restProductoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProducto() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the producto
        Producto updatedProducto = productoRepository.findById(producto.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProducto are not directly saved in db
        em.detach(updatedProducto);
        updatedProducto
            .sku(UPDATED_SKU)
            .nombre(UPDATED_NOMBRE)
            .slug(UPDATED_SLUG)
            .modelo(UPDATED_MODELO)
            .descripcion(UPDATED_DESCRIPCION)
            .especificaciones(UPDATED_ESPECIFICACIONES)
            .precioLista(UPDATED_PRECIO_LISTA)
            .precioOferta(UPDATED_PRECIO_OFERTA)
            .pesoKg(UPDATED_PESO_KG)
            .garantiaMeses(UPDATED_GARANTIA_MESES)
            .activo(UPDATED_ACTIVO)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        ProductoDTO productoDTO = productoMapper.toDto(updatedProducto);

        restProductoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Producto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProductoToMatchAllProperties(updatedProducto);
    }

    @Test
    @Transactional
    void putNonExistingProducto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        producto.setId(longCount.incrementAndGet());

        // Create the Producto
        ProductoDTO productoDTO = productoMapper.toDto(producto);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Producto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProducto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        producto.setId(longCount.incrementAndGet());

        // Create the Producto
        ProductoDTO productoDTO = productoMapper.toDto(producto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Producto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProducto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        producto.setId(longCount.incrementAndGet());

        // Create the Producto
        ProductoDTO productoDTO = productoMapper.toDto(producto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Producto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductoWithPatch() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the producto using partial update
        Producto partialUpdatedProducto = new Producto();
        partialUpdatedProducto.setId(producto.getId());

        partialUpdatedProducto
            .sku(UPDATED_SKU)
            .nombre(UPDATED_NOMBRE)
            .slug(UPDATED_SLUG)
            .modelo(UPDATED_MODELO)
            .precioLista(UPDATED_PRECIO_LISTA)
            .pesoKg(UPDATED_PESO_KG)
            .garantiaMeses(UPDATED_GARANTIA_MESES)
            .activo(UPDATED_ACTIVO)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restProductoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProducto.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProducto))
            )
            .andExpect(status().isOk());

        // Validate the Producto in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductoUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedProducto, producto), getPersistedProducto(producto));
    }

    @Test
    @Transactional
    void fullUpdateProductoWithPatch() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the producto using partial update
        Producto partialUpdatedProducto = new Producto();
        partialUpdatedProducto.setId(producto.getId());

        partialUpdatedProducto
            .sku(UPDATED_SKU)
            .nombre(UPDATED_NOMBRE)
            .slug(UPDATED_SLUG)
            .modelo(UPDATED_MODELO)
            .descripcion(UPDATED_DESCRIPCION)
            .especificaciones(UPDATED_ESPECIFICACIONES)
            .precioLista(UPDATED_PRECIO_LISTA)
            .precioOferta(UPDATED_PRECIO_OFERTA)
            .pesoKg(UPDATED_PESO_KG)
            .garantiaMeses(UPDATED_GARANTIA_MESES)
            .activo(UPDATED_ACTIVO)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restProductoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProducto.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProducto))
            )
            .andExpect(status().isOk());

        // Validate the Producto in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductoUpdatableFieldsEquals(partialUpdatedProducto, getPersistedProducto(partialUpdatedProducto));
    }

    @Test
    @Transactional
    void patchNonExistingProducto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        producto.setId(longCount.incrementAndGet());

        // Create the Producto
        ProductoDTO productoDTO = productoMapper.toDto(producto);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Producto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProducto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        producto.setId(longCount.incrementAndGet());

        // Create the Producto
        ProductoDTO productoDTO = productoMapper.toDto(producto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Producto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProducto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        producto.setId(longCount.incrementAndGet());

        // Create the Producto
        ProductoDTO productoDTO = productoMapper.toDto(producto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(productoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Producto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProducto() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the producto
        restProductoMockMvc
            .perform(delete(ENTITY_API_URL_ID, producto.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return productoRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Producto getPersistedProducto(Producto producto) {
        return productoRepository.findById(producto.getId()).orElseThrow();
    }

    protected void assertPersistedProductoToMatchAllProperties(Producto expectedProducto) {
        assertProductoAllPropertiesEquals(expectedProducto, getPersistedProducto(expectedProducto));
    }

    protected void assertPersistedProductoToMatchUpdatableProperties(Producto expectedProducto) {
        assertProductoAllUpdatablePropertiesEquals(expectedProducto, getPersistedProducto(expectedProducto));
    }
}
