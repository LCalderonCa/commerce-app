package pe.commerce.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pe.commerce.app.domain.PedidoDetalleAsserts.*;
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
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import pe.commerce.app.IntegrationTest;
import pe.commerce.app.domain.Pedido;
import pe.commerce.app.domain.PedidoDetalle;
import pe.commerce.app.domain.Producto;
import pe.commerce.app.domain.enumeration.TipoPrecio;
import pe.commerce.app.repository.PedidoDetalleRepository;
import pe.commerce.app.service.PedidoDetalleService;
import pe.commerce.app.service.dto.PedidoDetalleDTO;
import pe.commerce.app.service.mapper.PedidoDetalleMapper;
import tools.jackson.databind.ObjectMapper;

/**
 * Integration tests for the {@link PedidoDetalleResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PedidoDetalleResourceIT {

    private static final String DEFAULT_SKU_PRODUCTO = "AAAAAAAAAA";
    private static final String UPDATED_SKU_PRODUCTO = "BBBBBBBBBB";

    private static final String DEFAULT_NOMBRE_PRODUCTO = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE_PRODUCTO = "BBBBBBBBBB";

    private static final Integer DEFAULT_CANTIDAD = 1;
    private static final Integer UPDATED_CANTIDAD = 2;

    private static final TipoPrecio DEFAULT_TIPO_PRECIO = TipoPrecio.LISTA;
    private static final TipoPrecio UPDATED_TIPO_PRECIO = TipoPrecio.OFERTA;

    private static final BigDecimal DEFAULT_PRECIO_UNITARIO = new BigDecimal(0);
    private static final BigDecimal UPDATED_PRECIO_UNITARIO = new BigDecimal(1);

    private static final BigDecimal DEFAULT_DESCUENTO = new BigDecimal(0);
    private static final BigDecimal UPDATED_DESCUENTO = new BigDecimal(1);

    private static final BigDecimal DEFAULT_SUBTOTAL = new BigDecimal(0);
    private static final BigDecimal UPDATED_SUBTOTAL = new BigDecimal(1);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.ofEpochMilli(1702246707543L);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.ofEpochMilli(1702246707543L);

    private static final String ENTITY_API_URL = "/api/pedido-detalles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PedidoDetalleRepository pedidoDetalleRepository;

    @Mock
    private PedidoDetalleRepository pedidoDetalleRepositoryMock;

    @Autowired
    private PedidoDetalleMapper pedidoDetalleMapper;

    @Mock
    private PedidoDetalleService pedidoDetalleServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPedidoDetalleMockMvc;

    private PedidoDetalle pedidoDetalle;

    private PedidoDetalle insertedPedidoDetalle;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PedidoDetalle createEntity(EntityManager em) {
        PedidoDetalle pedidoDetalle = new PedidoDetalle()
            .skuProducto(DEFAULT_SKU_PRODUCTO)
            .nombreProducto(DEFAULT_NOMBRE_PRODUCTO)
            .cantidad(DEFAULT_CANTIDAD)
            .tipoPrecio(DEFAULT_TIPO_PRECIO)
            .precioUnitario(DEFAULT_PRECIO_UNITARIO)
            .descuento(DEFAULT_DESCUENTO)
            .subtotal(DEFAULT_SUBTOTAL)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        // Add required entity
        Producto producto;
        if (TestUtil.findAll(em, Producto.class).isEmpty()) {
            producto = ProductoResourceIT.createEntity(em);
            em.persist(producto);
            em.flush();
        } else {
            producto = TestUtil.findAll(em, Producto.class).getFirst();
        }
        pedidoDetalle.setProducto(producto);
        // Add required entity
        Pedido pedido;
        if (TestUtil.findAll(em, Pedido.class).isEmpty()) {
            pedido = PedidoResourceIT.createEntity(em);
            em.persist(pedido);
            em.flush();
        } else {
            pedido = TestUtil.findAll(em, Pedido.class).getFirst();
        }
        pedidoDetalle.setPedido(pedido);
        return pedidoDetalle;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PedidoDetalle createUpdatedEntity(EntityManager em) {
        PedidoDetalle updatedPedidoDetalle = new PedidoDetalle()
            .skuProducto(UPDATED_SKU_PRODUCTO)
            .nombreProducto(UPDATED_NOMBRE_PRODUCTO)
            .cantidad(UPDATED_CANTIDAD)
            .tipoPrecio(UPDATED_TIPO_PRECIO)
            .precioUnitario(UPDATED_PRECIO_UNITARIO)
            .descuento(UPDATED_DESCUENTO)
            .subtotal(UPDATED_SUBTOTAL)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        // Add required entity
        Producto producto;
        if (TestUtil.findAll(em, Producto.class).isEmpty()) {
            producto = ProductoResourceIT.createUpdatedEntity(em);
            em.persist(producto);
            em.flush();
        } else {
            producto = TestUtil.findAll(em, Producto.class).getFirst();
        }
        updatedPedidoDetalle.setProducto(producto);
        // Add required entity
        Pedido pedido;
        if (TestUtil.findAll(em, Pedido.class).isEmpty()) {
            pedido = PedidoResourceIT.createUpdatedEntity(em);
            em.persist(pedido);
            em.flush();
        } else {
            pedido = TestUtil.findAll(em, Pedido.class).getFirst();
        }
        updatedPedidoDetalle.setPedido(pedido);
        return updatedPedidoDetalle;
    }

    @BeforeEach
    void initTest() {
        pedidoDetalle = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedPedidoDetalle != null) {
            pedidoDetalleRepository.delete(insertedPedidoDetalle);
            insertedPedidoDetalle = null;
        }
    }

    @Test
    @Transactional
    void createPedidoDetalle() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PedidoDetalle
        PedidoDetalleDTO pedidoDetalleDTO = pedidoDetalleMapper.toDto(pedidoDetalle);
        var returnedPedidoDetalleDTO = om.readValue(
            restPedidoDetalleMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pedidoDetalleDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PedidoDetalleDTO.class
        );

        // Validate the PedidoDetalle in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPedidoDetalle = pedidoDetalleMapper.toEntity(returnedPedidoDetalleDTO);
        assertPedidoDetalleUpdatableFieldsEquals(returnedPedidoDetalle, getPersistedPedidoDetalle(returnedPedidoDetalle));

        insertedPedidoDetalle = returnedPedidoDetalle;
    }

    @Test
    @Transactional
    void createPedidoDetalleWithExistingId() throws Exception {
        // Create the PedidoDetalle with an existing ID
        pedidoDetalle.setId(1L);
        PedidoDetalleDTO pedidoDetalleDTO = pedidoDetalleMapper.toDto(pedidoDetalle);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPedidoDetalleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pedidoDetalleDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PedidoDetalle in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSkuProductoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        pedidoDetalle.setSkuProducto(null);

        // Create the PedidoDetalle, which fails.
        PedidoDetalleDTO pedidoDetalleDTO = pedidoDetalleMapper.toDto(pedidoDetalle);

        restPedidoDetalleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pedidoDetalleDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNombreProductoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        pedidoDetalle.setNombreProducto(null);

        // Create the PedidoDetalle, which fails.
        PedidoDetalleDTO pedidoDetalleDTO = pedidoDetalleMapper.toDto(pedidoDetalle);

        restPedidoDetalleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pedidoDetalleDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCantidadIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        pedidoDetalle.setCantidad(null);

        // Create the PedidoDetalle, which fails.
        PedidoDetalleDTO pedidoDetalleDTO = pedidoDetalleMapper.toDto(pedidoDetalle);

        restPedidoDetalleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pedidoDetalleDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTipoPrecioIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        pedidoDetalle.setTipoPrecio(null);

        // Create the PedidoDetalle, which fails.
        PedidoDetalleDTO pedidoDetalleDTO = pedidoDetalleMapper.toDto(pedidoDetalle);

        restPedidoDetalleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pedidoDetalleDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPrecioUnitarioIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        pedidoDetalle.setPrecioUnitario(null);

        // Create the PedidoDetalle, which fails.
        PedidoDetalleDTO pedidoDetalleDTO = pedidoDetalleMapper.toDto(pedidoDetalle);

        restPedidoDetalleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pedidoDetalleDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescuentoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        pedidoDetalle.setDescuento(null);

        // Create the PedidoDetalle, which fails.
        PedidoDetalleDTO pedidoDetalleDTO = pedidoDetalleMapper.toDto(pedidoDetalle);

        restPedidoDetalleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pedidoDetalleDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSubtotalIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        pedidoDetalle.setSubtotal(null);

        // Create the PedidoDetalle, which fails.
        PedidoDetalleDTO pedidoDetalleDTO = pedidoDetalleMapper.toDto(pedidoDetalle);

        restPedidoDetalleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pedidoDetalleDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPedidoDetalles() throws Exception {
        // Initialize the database
        insertedPedidoDetalle = pedidoDetalleRepository.saveAndFlush(pedidoDetalle);

        // Get all the pedidoDetalleList
        restPedidoDetalleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pedidoDetalle.getId().intValue())))
            .andExpect(jsonPath("$.[*].skuProducto").value(hasItem(DEFAULT_SKU_PRODUCTO)))
            .andExpect(jsonPath("$.[*].nombreProducto").value(hasItem(DEFAULT_NOMBRE_PRODUCTO)))
            .andExpect(jsonPath("$.[*].cantidad").value(hasItem(DEFAULT_CANTIDAD)))
            .andExpect(jsonPath("$.[*].tipoPrecio").value(hasItem(DEFAULT_TIPO_PRECIO.toString())))
            .andExpect(jsonPath("$.[*].precioUnitario").value(hasItem(sameNumber(DEFAULT_PRECIO_UNITARIO))))
            .andExpect(jsonPath("$.[*].descuento").value(hasItem(sameNumber(DEFAULT_DESCUENTO))))
            .andExpect(jsonPath("$.[*].subtotal").value(hasItem(sameNumber(DEFAULT_SUBTOTAL))))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPedidoDetallesWithEagerRelationshipsIsEnabled() throws Exception {
        when(pedidoDetalleServiceMock.findAllWithEagerRelationships()).thenReturn(new ArrayList<>());

        restPedidoDetalleMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(pedidoDetalleServiceMock, times(1)).findAllWithEagerRelationships();
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPedidoDetallesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(pedidoDetalleServiceMock.findAllWithEagerRelationships()).thenReturn(new ArrayList<>());

        restPedidoDetalleMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(pedidoDetalleRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getPedidoDetalle() throws Exception {
        // Initialize the database
        insertedPedidoDetalle = pedidoDetalleRepository.saveAndFlush(pedidoDetalle);

        // Get the pedidoDetalle
        restPedidoDetalleMockMvc
            .perform(get(ENTITY_API_URL_ID, pedidoDetalle.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pedidoDetalle.getId().intValue()))
            .andExpect(jsonPath("$.skuProducto").value(DEFAULT_SKU_PRODUCTO))
            .andExpect(jsonPath("$.nombreProducto").value(DEFAULT_NOMBRE_PRODUCTO))
            .andExpect(jsonPath("$.cantidad").value(DEFAULT_CANTIDAD))
            .andExpect(jsonPath("$.tipoPrecio").value(DEFAULT_TIPO_PRECIO.toString()))
            .andExpect(jsonPath("$.precioUnitario").value(sameNumber(DEFAULT_PRECIO_UNITARIO)))
            .andExpect(jsonPath("$.descuento").value(sameNumber(DEFAULT_DESCUENTO)))
            .andExpect(jsonPath("$.subtotal").value(sameNumber(DEFAULT_SUBTOTAL)))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingPedidoDetalle() throws Exception {
        // Get the pedidoDetalle
        restPedidoDetalleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPedidoDetalle() throws Exception {
        // Initialize the database
        insertedPedidoDetalle = pedidoDetalleRepository.saveAndFlush(pedidoDetalle);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pedidoDetalle
        PedidoDetalle updatedPedidoDetalle = pedidoDetalleRepository.findById(pedidoDetalle.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPedidoDetalle are not directly saved in db
        em.detach(updatedPedidoDetalle);
        updatedPedidoDetalle
            .skuProducto(UPDATED_SKU_PRODUCTO)
            .nombreProducto(UPDATED_NOMBRE_PRODUCTO)
            .cantidad(UPDATED_CANTIDAD)
            .tipoPrecio(UPDATED_TIPO_PRECIO)
            .precioUnitario(UPDATED_PRECIO_UNITARIO)
            .descuento(UPDATED_DESCUENTO)
            .subtotal(UPDATED_SUBTOTAL)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        PedidoDetalleDTO pedidoDetalleDTO = pedidoDetalleMapper.toDto(updatedPedidoDetalle);

        restPedidoDetalleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pedidoDetalleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(pedidoDetalleDTO))
            )
            .andExpect(status().isOk());

        // Validate the PedidoDetalle in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPedidoDetalleToMatchAllProperties(updatedPedidoDetalle);
    }

    @Test
    @Transactional
    void putNonExistingPedidoDetalle() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pedidoDetalle.setId(longCount.incrementAndGet());

        // Create the PedidoDetalle
        PedidoDetalleDTO pedidoDetalleDTO = pedidoDetalleMapper.toDto(pedidoDetalle);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPedidoDetalleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pedidoDetalleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(pedidoDetalleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PedidoDetalle in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPedidoDetalle() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pedidoDetalle.setId(longCount.incrementAndGet());

        // Create the PedidoDetalle
        PedidoDetalleDTO pedidoDetalleDTO = pedidoDetalleMapper.toDto(pedidoDetalle);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPedidoDetalleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(pedidoDetalleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PedidoDetalle in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPedidoDetalle() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pedidoDetalle.setId(longCount.incrementAndGet());

        // Create the PedidoDetalle
        PedidoDetalleDTO pedidoDetalleDTO = pedidoDetalleMapper.toDto(pedidoDetalle);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPedidoDetalleMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pedidoDetalleDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PedidoDetalle in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePedidoDetalleWithPatch() throws Exception {
        // Initialize the database
        insertedPedidoDetalle = pedidoDetalleRepository.saveAndFlush(pedidoDetalle);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pedidoDetalle using partial update
        PedidoDetalle partialUpdatedPedidoDetalle = new PedidoDetalle();
        partialUpdatedPedidoDetalle.setId(pedidoDetalle.getId());

        partialUpdatedPedidoDetalle
            .skuProducto(UPDATED_SKU_PRODUCTO)
            .nombreProducto(UPDATED_NOMBRE_PRODUCTO)
            .tipoPrecio(UPDATED_TIPO_PRECIO)
            .precioUnitario(UPDATED_PRECIO_UNITARIO)
            .descuento(UPDATED_DESCUENTO)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restPedidoDetalleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPedidoDetalle.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPedidoDetalle))
            )
            .andExpect(status().isOk());

        // Validate the PedidoDetalle in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPedidoDetalleUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPedidoDetalle, pedidoDetalle),
            getPersistedPedidoDetalle(pedidoDetalle)
        );
    }

    @Test
    @Transactional
    void fullUpdatePedidoDetalleWithPatch() throws Exception {
        // Initialize the database
        insertedPedidoDetalle = pedidoDetalleRepository.saveAndFlush(pedidoDetalle);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pedidoDetalle using partial update
        PedidoDetalle partialUpdatedPedidoDetalle = new PedidoDetalle();
        partialUpdatedPedidoDetalle.setId(pedidoDetalle.getId());

        partialUpdatedPedidoDetalle
            .skuProducto(UPDATED_SKU_PRODUCTO)
            .nombreProducto(UPDATED_NOMBRE_PRODUCTO)
            .cantidad(UPDATED_CANTIDAD)
            .tipoPrecio(UPDATED_TIPO_PRECIO)
            .precioUnitario(UPDATED_PRECIO_UNITARIO)
            .descuento(UPDATED_DESCUENTO)
            .subtotal(UPDATED_SUBTOTAL)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restPedidoDetalleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPedidoDetalle.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPedidoDetalle))
            )
            .andExpect(status().isOk());

        // Validate the PedidoDetalle in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPedidoDetalleUpdatableFieldsEquals(partialUpdatedPedidoDetalle, getPersistedPedidoDetalle(partialUpdatedPedidoDetalle));
    }

    @Test
    @Transactional
    void patchNonExistingPedidoDetalle() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pedidoDetalle.setId(longCount.incrementAndGet());

        // Create the PedidoDetalle
        PedidoDetalleDTO pedidoDetalleDTO = pedidoDetalleMapper.toDto(pedidoDetalle);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPedidoDetalleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pedidoDetalleDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(pedidoDetalleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PedidoDetalle in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPedidoDetalle() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pedidoDetalle.setId(longCount.incrementAndGet());

        // Create the PedidoDetalle
        PedidoDetalleDTO pedidoDetalleDTO = pedidoDetalleMapper.toDto(pedidoDetalle);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPedidoDetalleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(pedidoDetalleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PedidoDetalle in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPedidoDetalle() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pedidoDetalle.setId(longCount.incrementAndGet());

        // Create the PedidoDetalle
        PedidoDetalleDTO pedidoDetalleDTO = pedidoDetalleMapper.toDto(pedidoDetalle);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPedidoDetalleMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(pedidoDetalleDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PedidoDetalle in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePedidoDetalle() throws Exception {
        // Initialize the database
        insertedPedidoDetalle = pedidoDetalleRepository.saveAndFlush(pedidoDetalle);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the pedidoDetalle
        restPedidoDetalleMockMvc
            .perform(delete(ENTITY_API_URL_ID, pedidoDetalle.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return pedidoDetalleRepository.count();
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

    protected PedidoDetalle getPersistedPedidoDetalle(PedidoDetalle pedidoDetalle) {
        return pedidoDetalleRepository.findById(pedidoDetalle.getId()).orElseThrow();
    }

    protected void assertPersistedPedidoDetalleToMatchAllProperties(PedidoDetalle expectedPedidoDetalle) {
        assertPedidoDetalleAllPropertiesEquals(expectedPedidoDetalle, getPersistedPedidoDetalle(expectedPedidoDetalle));
    }

    protected void assertPersistedPedidoDetalleToMatchUpdatableProperties(PedidoDetalle expectedPedidoDetalle) {
        assertPedidoDetalleAllUpdatablePropertiesEquals(expectedPedidoDetalle, getPersistedPedidoDetalle(expectedPedidoDetalle));
    }
}
