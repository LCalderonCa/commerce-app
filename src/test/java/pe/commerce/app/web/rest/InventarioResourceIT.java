package pe.commerce.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pe.commerce.app.domain.InventarioAsserts.*;
import static pe.commerce.app.web.rest.TestUtil.createUpdateProxyForBean;

import jakarta.persistence.EntityManager;
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
import pe.commerce.app.domain.Inventario;
import pe.commerce.app.domain.Producto;
import pe.commerce.app.domain.Tienda;
import pe.commerce.app.repository.InventarioRepository;
import pe.commerce.app.service.InventarioService;
import pe.commerce.app.service.dto.InventarioDTO;
import pe.commerce.app.service.mapper.InventarioMapper;
import tools.jackson.databind.ObjectMapper;

/**
 * Integration tests for the {@link InventarioResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class InventarioResourceIT {

    private static final Integer DEFAULT_STOCK = 0;
    private static final Integer UPDATED_STOCK = 1;

    private static final Integer DEFAULT_STOCK_RESERVADO = 0;
    private static final Integer UPDATED_STOCK_RESERVADO = 1;

    private static final Integer DEFAULT_STOCK_MINIMO = 0;
    private static final Integer UPDATED_STOCK_MINIMO = 1;

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.ofEpochMilli(1702246707543L);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.ofEpochMilli(1702246707543L);

    private static final String ENTITY_API_URL = "/api/inventarios";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private InventarioRepository inventarioRepository;

    @Mock
    private InventarioRepository inventarioRepositoryMock;

    @Autowired
    private InventarioMapper inventarioMapper;

    @Mock
    private InventarioService inventarioServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInventarioMockMvc;

    private Inventario inventario;

    private Inventario insertedInventario;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Inventario createEntity(EntityManager em) {
        Inventario inventario = new Inventario()
            .stock(DEFAULT_STOCK)
            .stockReservado(DEFAULT_STOCK_RESERVADO)
            .stockMinimo(DEFAULT_STOCK_MINIMO)
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
        inventario.setProducto(producto);
        // Add required entity
        Tienda tienda;
        if (TestUtil.findAll(em, Tienda.class).isEmpty()) {
            tienda = TiendaResourceIT.createEntity(em);
            em.persist(tienda);
            em.flush();
        } else {
            tienda = TestUtil.findAll(em, Tienda.class).getFirst();
        }
        inventario.setTienda(tienda);
        return inventario;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Inventario createUpdatedEntity(EntityManager em) {
        Inventario updatedInventario = new Inventario()
            .stock(UPDATED_STOCK)
            .stockReservado(UPDATED_STOCK_RESERVADO)
            .stockMinimo(UPDATED_STOCK_MINIMO)
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
        updatedInventario.setProducto(producto);
        // Add required entity
        Tienda tienda;
        if (TestUtil.findAll(em, Tienda.class).isEmpty()) {
            tienda = TiendaResourceIT.createUpdatedEntity(em);
            em.persist(tienda);
            em.flush();
        } else {
            tienda = TestUtil.findAll(em, Tienda.class).getFirst();
        }
        updatedInventario.setTienda(tienda);
        return updatedInventario;
    }

    @BeforeEach
    void initTest() {
        inventario = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedInventario != null) {
            inventarioRepository.delete(insertedInventario);
            insertedInventario = null;
        }
    }

    @Test
    @Transactional
    void createInventario() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Inventario
        InventarioDTO inventarioDTO = inventarioMapper.toDto(inventario);
        var returnedInventarioDTO = om.readValue(
            restInventarioMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(inventarioDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            InventarioDTO.class
        );

        // Validate the Inventario in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedInventario = inventarioMapper.toEntity(returnedInventarioDTO);
        assertInventarioUpdatableFieldsEquals(returnedInventario, getPersistedInventario(returnedInventario));

        insertedInventario = returnedInventario;
    }

    @Test
    @Transactional
    void createInventarioWithExistingId() throws Exception {
        // Create the Inventario with an existing ID
        inventario.setId(1L);
        InventarioDTO inventarioDTO = inventarioMapper.toDto(inventario);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInventarioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(inventarioDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Inventario in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkStockIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        inventario.setStock(null);

        // Create the Inventario, which fails.
        InventarioDTO inventarioDTO = inventarioMapper.toDto(inventario);

        restInventarioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(inventarioDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStockReservadoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        inventario.setStockReservado(null);

        // Create the Inventario, which fails.
        InventarioDTO inventarioDTO = inventarioMapper.toDto(inventario);

        restInventarioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(inventarioDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllInventarios() throws Exception {
        // Initialize the database
        insertedInventario = inventarioRepository.saveAndFlush(inventario);

        // Get all the inventarioList
        restInventarioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(inventario.getId().intValue())))
            .andExpect(jsonPath("$.[*].stock").value(hasItem(DEFAULT_STOCK)))
            .andExpect(jsonPath("$.[*].stockReservado").value(hasItem(DEFAULT_STOCK_RESERVADO)))
            .andExpect(jsonPath("$.[*].stockMinimo").value(hasItem(DEFAULT_STOCK_MINIMO)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllInventariosWithEagerRelationshipsIsEnabled() throws Exception {
        when(inventarioServiceMock.findAllWithEagerRelationships()).thenReturn(new ArrayList<>());

        restInventarioMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(inventarioServiceMock, times(1)).findAllWithEagerRelationships();
    }

    @SuppressWarnings({ "unchecked" })
    void getAllInventariosWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(inventarioServiceMock.findAllWithEagerRelationships()).thenReturn(new ArrayList<>());

        restInventarioMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(inventarioRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getInventario() throws Exception {
        // Initialize the database
        insertedInventario = inventarioRepository.saveAndFlush(inventario);

        // Get the inventario
        restInventarioMockMvc
            .perform(get(ENTITY_API_URL_ID, inventario.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(inventario.getId().intValue()))
            .andExpect(jsonPath("$.stock").value(DEFAULT_STOCK))
            .andExpect(jsonPath("$.stockReservado").value(DEFAULT_STOCK_RESERVADO))
            .andExpect(jsonPath("$.stockMinimo").value(DEFAULT_STOCK_MINIMO))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingInventario() throws Exception {
        // Get the inventario
        restInventarioMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingInventario() throws Exception {
        // Initialize the database
        insertedInventario = inventarioRepository.saveAndFlush(inventario);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the inventario
        Inventario updatedInventario = inventarioRepository.findById(inventario.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedInventario are not directly saved in db
        em.detach(updatedInventario);
        updatedInventario
            .stock(UPDATED_STOCK)
            .stockReservado(UPDATED_STOCK_RESERVADO)
            .stockMinimo(UPDATED_STOCK_MINIMO)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        InventarioDTO inventarioDTO = inventarioMapper.toDto(updatedInventario);

        restInventarioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, inventarioDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(inventarioDTO))
            )
            .andExpect(status().isOk());

        // Validate the Inventario in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedInventarioToMatchAllProperties(updatedInventario);
    }

    @Test
    @Transactional
    void putNonExistingInventario() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inventario.setId(longCount.incrementAndGet());

        // Create the Inventario
        InventarioDTO inventarioDTO = inventarioMapper.toDto(inventario);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInventarioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, inventarioDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(inventarioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Inventario in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInventario() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inventario.setId(longCount.incrementAndGet());

        // Create the Inventario
        InventarioDTO inventarioDTO = inventarioMapper.toDto(inventario);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInventarioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(inventarioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Inventario in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInventario() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inventario.setId(longCount.incrementAndGet());

        // Create the Inventario
        InventarioDTO inventarioDTO = inventarioMapper.toDto(inventario);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInventarioMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(inventarioDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Inventario in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInventarioWithPatch() throws Exception {
        // Initialize the database
        insertedInventario = inventarioRepository.saveAndFlush(inventario);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the inventario using partial update
        Inventario partialUpdatedInventario = new Inventario();
        partialUpdatedInventario.setId(inventario.getId());

        partialUpdatedInventario
            .stock(UPDATED_STOCK)
            .stockReservado(UPDATED_STOCK_RESERVADO)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restInventarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInventario.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInventario))
            )
            .andExpect(status().isOk());

        // Validate the Inventario in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInventarioUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedInventario, inventario),
            getPersistedInventario(inventario)
        );
    }

    @Test
    @Transactional
    void fullUpdateInventarioWithPatch() throws Exception {
        // Initialize the database
        insertedInventario = inventarioRepository.saveAndFlush(inventario);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the inventario using partial update
        Inventario partialUpdatedInventario = new Inventario();
        partialUpdatedInventario.setId(inventario.getId());

        partialUpdatedInventario
            .stock(UPDATED_STOCK)
            .stockReservado(UPDATED_STOCK_RESERVADO)
            .stockMinimo(UPDATED_STOCK_MINIMO)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restInventarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInventario.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInventario))
            )
            .andExpect(status().isOk());

        // Validate the Inventario in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInventarioUpdatableFieldsEquals(partialUpdatedInventario, getPersistedInventario(partialUpdatedInventario));
    }

    @Test
    @Transactional
    void patchNonExistingInventario() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inventario.setId(longCount.incrementAndGet());

        // Create the Inventario
        InventarioDTO inventarioDTO = inventarioMapper.toDto(inventario);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInventarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, inventarioDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(inventarioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Inventario in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInventario() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inventario.setId(longCount.incrementAndGet());

        // Create the Inventario
        InventarioDTO inventarioDTO = inventarioMapper.toDto(inventario);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInventarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(inventarioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Inventario in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInventario() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inventario.setId(longCount.incrementAndGet());

        // Create the Inventario
        InventarioDTO inventarioDTO = inventarioMapper.toDto(inventario);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInventarioMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(inventarioDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Inventario in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInventario() throws Exception {
        // Initialize the database
        insertedInventario = inventarioRepository.saveAndFlush(inventario);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the inventario
        restInventarioMockMvc
            .perform(delete(ENTITY_API_URL_ID, inventario.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return inventarioRepository.count();
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

    protected Inventario getPersistedInventario(Inventario inventario) {
        return inventarioRepository.findById(inventario.getId()).orElseThrow();
    }

    protected void assertPersistedInventarioToMatchAllProperties(Inventario expectedInventario) {
        assertInventarioAllPropertiesEquals(expectedInventario, getPersistedInventario(expectedInventario));
    }

    protected void assertPersistedInventarioToMatchUpdatableProperties(Inventario expectedInventario) {
        assertInventarioAllUpdatablePropertiesEquals(expectedInventario, getPersistedInventario(expectedInventario));
    }
}
