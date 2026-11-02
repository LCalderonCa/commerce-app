package pe.commerce.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pe.commerce.app.domain.CarritoItemAsserts.*;
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
import pe.commerce.app.domain.Carrito;
import pe.commerce.app.domain.CarritoItem;
import pe.commerce.app.domain.Producto;
import pe.commerce.app.domain.enumeration.EstadoItemCarrito;
import pe.commerce.app.repository.CarritoItemRepository;
import pe.commerce.app.service.CarritoItemService;
import pe.commerce.app.service.dto.CarritoItemDTO;
import pe.commerce.app.service.mapper.CarritoItemMapper;
import tools.jackson.databind.ObjectMapper;

/**
 * Integration tests for the {@link CarritoItemResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CarritoItemResourceIT {

    private static final Integer DEFAULT_CANTIDAD = 1;
    private static final Integer UPDATED_CANTIDAD = 2;

    private static final Boolean DEFAULT_SELECCIONADO = false;
    private static final Boolean UPDATED_SELECCIONADO = true;

    private static final EstadoItemCarrito DEFAULT_ESTADO = EstadoItemCarrito.ACTIVO;
    private static final EstadoItemCarrito UPDATED_ESTADO = EstadoItemCarrito.GUARDADO;

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.ofEpochMilli(1702246707543L);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.ofEpochMilli(1702246707543L);

    private static final String ENTITY_API_URL = "/api/carrito-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CarritoItemRepository carritoItemRepository;

    @Mock
    private CarritoItemRepository carritoItemRepositoryMock;

    @Autowired
    private CarritoItemMapper carritoItemMapper;

    @Mock
    private CarritoItemService carritoItemServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCarritoItemMockMvc;

    private CarritoItem carritoItem;

    private CarritoItem insertedCarritoItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CarritoItem createEntity(EntityManager em) {
        CarritoItem carritoItem = new CarritoItem()
            .cantidad(DEFAULT_CANTIDAD)
            .seleccionado(DEFAULT_SELECCIONADO)
            .estado(DEFAULT_ESTADO)
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
        carritoItem.setProducto(producto);
        // Add required entity
        Carrito carrito;
        if (TestUtil.findAll(em, Carrito.class).isEmpty()) {
            carrito = CarritoResourceIT.createEntity(em);
            em.persist(carrito);
            em.flush();
        } else {
            carrito = TestUtil.findAll(em, Carrito.class).getFirst();
        }
        carritoItem.setCarrito(carrito);
        return carritoItem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CarritoItem createUpdatedEntity(EntityManager em) {
        CarritoItem updatedCarritoItem = new CarritoItem()
            .cantidad(UPDATED_CANTIDAD)
            .seleccionado(UPDATED_SELECCIONADO)
            .estado(UPDATED_ESTADO)
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
        updatedCarritoItem.setProducto(producto);
        // Add required entity
        Carrito carrito;
        if (TestUtil.findAll(em, Carrito.class).isEmpty()) {
            carrito = CarritoResourceIT.createUpdatedEntity(em);
            em.persist(carrito);
            em.flush();
        } else {
            carrito = TestUtil.findAll(em, Carrito.class).getFirst();
        }
        updatedCarritoItem.setCarrito(carrito);
        return updatedCarritoItem;
    }

    @BeforeEach
    void initTest() {
        carritoItem = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedCarritoItem != null) {
            carritoItemRepository.delete(insertedCarritoItem);
            insertedCarritoItem = null;
        }
    }

    @Test
    @Transactional
    void createCarritoItem() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CarritoItem
        CarritoItemDTO carritoItemDTO = carritoItemMapper.toDto(carritoItem);
        var returnedCarritoItemDTO = om.readValue(
            restCarritoItemMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(carritoItemDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CarritoItemDTO.class
        );

        // Validate the CarritoItem in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCarritoItem = carritoItemMapper.toEntity(returnedCarritoItemDTO);
        assertCarritoItemUpdatableFieldsEquals(returnedCarritoItem, getPersistedCarritoItem(returnedCarritoItem));

        insertedCarritoItem = returnedCarritoItem;
    }

    @Test
    @Transactional
    void createCarritoItemWithExistingId() throws Exception {
        // Create the CarritoItem with an existing ID
        carritoItem.setId(1L);
        CarritoItemDTO carritoItemDTO = carritoItemMapper.toDto(carritoItem);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCarritoItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(carritoItemDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CarritoItem in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCantidadIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        carritoItem.setCantidad(null);

        // Create the CarritoItem, which fails.
        CarritoItemDTO carritoItemDTO = carritoItemMapper.toDto(carritoItem);

        restCarritoItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(carritoItemDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSeleccionadoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        carritoItem.setSeleccionado(null);

        // Create the CarritoItem, which fails.
        CarritoItemDTO carritoItemDTO = carritoItemMapper.toDto(carritoItem);

        restCarritoItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(carritoItemDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEstadoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        carritoItem.setEstado(null);

        // Create the CarritoItem, which fails.
        CarritoItemDTO carritoItemDTO = carritoItemMapper.toDto(carritoItem);

        restCarritoItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(carritoItemDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCarritoItems() throws Exception {
        // Initialize the database
        insertedCarritoItem = carritoItemRepository.saveAndFlush(carritoItem);

        // Get all the carritoItemList
        restCarritoItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(carritoItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].cantidad").value(hasItem(DEFAULT_CANTIDAD)))
            .andExpect(jsonPath("$.[*].seleccionado").value(hasItem(DEFAULT_SELECCIONADO)))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCarritoItemsWithEagerRelationshipsIsEnabled() throws Exception {
        when(carritoItemServiceMock.findAllWithEagerRelationships()).thenReturn(new ArrayList<>());

        restCarritoItemMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(carritoItemServiceMock, times(1)).findAllWithEagerRelationships();
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCarritoItemsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(carritoItemServiceMock.findAllWithEagerRelationships()).thenReturn(new ArrayList<>());

        restCarritoItemMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(carritoItemRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getCarritoItem() throws Exception {
        // Initialize the database
        insertedCarritoItem = carritoItemRepository.saveAndFlush(carritoItem);

        // Get the carritoItem
        restCarritoItemMockMvc
            .perform(get(ENTITY_API_URL_ID, carritoItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(carritoItem.getId().intValue()))
            .andExpect(jsonPath("$.cantidad").value(DEFAULT_CANTIDAD))
            .andExpect(jsonPath("$.seleccionado").value(DEFAULT_SELECCIONADO))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingCarritoItem() throws Exception {
        // Get the carritoItem
        restCarritoItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCarritoItem() throws Exception {
        // Initialize the database
        insertedCarritoItem = carritoItemRepository.saveAndFlush(carritoItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the carritoItem
        CarritoItem updatedCarritoItem = carritoItemRepository.findById(carritoItem.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCarritoItem are not directly saved in db
        em.detach(updatedCarritoItem);
        updatedCarritoItem
            .cantidad(UPDATED_CANTIDAD)
            .seleccionado(UPDATED_SELECCIONADO)
            .estado(UPDATED_ESTADO)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        CarritoItemDTO carritoItemDTO = carritoItemMapper.toDto(updatedCarritoItem);

        restCarritoItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, carritoItemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(carritoItemDTO))
            )
            .andExpect(status().isOk());

        // Validate the CarritoItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCarritoItemToMatchAllProperties(updatedCarritoItem);
    }

    @Test
    @Transactional
    void putNonExistingCarritoItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        carritoItem.setId(longCount.incrementAndGet());

        // Create the CarritoItem
        CarritoItemDTO carritoItemDTO = carritoItemMapper.toDto(carritoItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCarritoItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, carritoItemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(carritoItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CarritoItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCarritoItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        carritoItem.setId(longCount.incrementAndGet());

        // Create the CarritoItem
        CarritoItemDTO carritoItemDTO = carritoItemMapper.toDto(carritoItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCarritoItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(carritoItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CarritoItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCarritoItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        carritoItem.setId(longCount.incrementAndGet());

        // Create the CarritoItem
        CarritoItemDTO carritoItemDTO = carritoItemMapper.toDto(carritoItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCarritoItemMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(carritoItemDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CarritoItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCarritoItemWithPatch() throws Exception {
        // Initialize the database
        insertedCarritoItem = carritoItemRepository.saveAndFlush(carritoItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the carritoItem using partial update
        CarritoItem partialUpdatedCarritoItem = new CarritoItem();
        partialUpdatedCarritoItem.setId(carritoItem.getId());

        partialUpdatedCarritoItem.cantidad(UPDATED_CANTIDAD).seleccionado(UPDATED_SELECCIONADO).createdBy(UPDATED_CREATED_BY);

        restCarritoItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCarritoItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCarritoItem))
            )
            .andExpect(status().isOk());

        // Validate the CarritoItem in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCarritoItemUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCarritoItem, carritoItem),
            getPersistedCarritoItem(carritoItem)
        );
    }

    @Test
    @Transactional
    void fullUpdateCarritoItemWithPatch() throws Exception {
        // Initialize the database
        insertedCarritoItem = carritoItemRepository.saveAndFlush(carritoItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the carritoItem using partial update
        CarritoItem partialUpdatedCarritoItem = new CarritoItem();
        partialUpdatedCarritoItem.setId(carritoItem.getId());

        partialUpdatedCarritoItem
            .cantidad(UPDATED_CANTIDAD)
            .seleccionado(UPDATED_SELECCIONADO)
            .estado(UPDATED_ESTADO)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restCarritoItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCarritoItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCarritoItem))
            )
            .andExpect(status().isOk());

        // Validate the CarritoItem in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCarritoItemUpdatableFieldsEquals(partialUpdatedCarritoItem, getPersistedCarritoItem(partialUpdatedCarritoItem));
    }

    @Test
    @Transactional
    void patchNonExistingCarritoItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        carritoItem.setId(longCount.incrementAndGet());

        // Create the CarritoItem
        CarritoItemDTO carritoItemDTO = carritoItemMapper.toDto(carritoItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCarritoItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, carritoItemDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(carritoItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CarritoItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCarritoItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        carritoItem.setId(longCount.incrementAndGet());

        // Create the CarritoItem
        CarritoItemDTO carritoItemDTO = carritoItemMapper.toDto(carritoItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCarritoItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(carritoItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CarritoItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCarritoItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        carritoItem.setId(longCount.incrementAndGet());

        // Create the CarritoItem
        CarritoItemDTO carritoItemDTO = carritoItemMapper.toDto(carritoItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCarritoItemMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(carritoItemDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CarritoItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCarritoItem() throws Exception {
        // Initialize the database
        insertedCarritoItem = carritoItemRepository.saveAndFlush(carritoItem);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the carritoItem
        restCarritoItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, carritoItem.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return carritoItemRepository.count();
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

    protected CarritoItem getPersistedCarritoItem(CarritoItem carritoItem) {
        return carritoItemRepository.findById(carritoItem.getId()).orElseThrow();
    }

    protected void assertPersistedCarritoItemToMatchAllProperties(CarritoItem expectedCarritoItem) {
        assertCarritoItemAllPropertiesEquals(expectedCarritoItem, getPersistedCarritoItem(expectedCarritoItem));
    }

    protected void assertPersistedCarritoItemToMatchUpdatableProperties(CarritoItem expectedCarritoItem) {
        assertCarritoItemAllUpdatablePropertiesEquals(expectedCarritoItem, getPersistedCarritoItem(expectedCarritoItem));
    }
}
