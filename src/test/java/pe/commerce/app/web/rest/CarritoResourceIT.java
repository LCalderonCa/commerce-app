package pe.commerce.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pe.commerce.app.domain.CarritoAsserts.*;
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
import pe.commerce.app.domain.Cliente;
import pe.commerce.app.repository.CarritoRepository;
import pe.commerce.app.service.CarritoService;
import pe.commerce.app.service.dto.CarritoDTO;
import pe.commerce.app.service.mapper.CarritoMapper;
import tools.jackson.databind.ObjectMapper;

/**
 * Integration tests for the {@link CarritoResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CarritoResourceIT {

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.ofEpochMilli(1702246707543L);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.ofEpochMilli(1702246707543L);

    private static final String ENTITY_API_URL = "/api/carritos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CarritoRepository carritoRepository;

    @Mock
    private CarritoRepository carritoRepositoryMock;

    @Autowired
    private CarritoMapper carritoMapper;

    @Mock
    private CarritoService carritoServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCarritoMockMvc;

    private Carrito carrito;

    private Carrito insertedCarrito;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Carrito createEntity(EntityManager em) {
        Carrito carrito = new Carrito()
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        // Add required entity
        Cliente cliente;
        if (TestUtil.findAll(em, Cliente.class).isEmpty()) {
            cliente = ClienteResourceIT.createEntity(em);
            em.persist(cliente);
            em.flush();
        } else {
            cliente = TestUtil.findAll(em, Cliente.class).getFirst();
        }
        carrito.setCliente(cliente);
        return carrito;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Carrito createUpdatedEntity(EntityManager em) {
        Carrito updatedCarrito = new Carrito()
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        // Add required entity
        Cliente cliente;
        if (TestUtil.findAll(em, Cliente.class).isEmpty()) {
            cliente = ClienteResourceIT.createUpdatedEntity(em);
            em.persist(cliente);
            em.flush();
        } else {
            cliente = TestUtil.findAll(em, Cliente.class).getFirst();
        }
        updatedCarrito.setCliente(cliente);
        return updatedCarrito;
    }

    @BeforeEach
    void initTest() {
        carrito = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedCarrito != null) {
            carritoRepository.delete(insertedCarrito);
            insertedCarrito = null;
        }
    }

    @Test
    @Transactional
    void createCarrito() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Carrito
        CarritoDTO carritoDTO = carritoMapper.toDto(carrito);
        var returnedCarritoDTO = om.readValue(
            restCarritoMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(carritoDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CarritoDTO.class
        );

        // Validate the Carrito in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCarrito = carritoMapper.toEntity(returnedCarritoDTO);
        assertCarritoUpdatableFieldsEquals(returnedCarrito, getPersistedCarrito(returnedCarrito));

        insertedCarrito = returnedCarrito;
    }

    @Test
    @Transactional
    void createCarritoWithExistingId() throws Exception {
        // Create the Carrito with an existing ID
        carrito.setId(1L);
        CarritoDTO carritoDTO = carritoMapper.toDto(carrito);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCarritoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(carritoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Carrito in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCarritos() throws Exception {
        // Initialize the database
        insertedCarrito = carritoRepository.saveAndFlush(carrito);

        // Get all the carritoList
        restCarritoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(carrito.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCarritosWithEagerRelationshipsIsEnabled() throws Exception {
        when(carritoServiceMock.findAllWithEagerRelationships()).thenReturn(new ArrayList<>());

        restCarritoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(carritoServiceMock, times(1)).findAllWithEagerRelationships();
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCarritosWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(carritoServiceMock.findAllWithEagerRelationships()).thenReturn(new ArrayList<>());

        restCarritoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(carritoRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getCarrito() throws Exception {
        // Initialize the database
        insertedCarrito = carritoRepository.saveAndFlush(carrito);

        // Get the carrito
        restCarritoMockMvc
            .perform(get(ENTITY_API_URL_ID, carrito.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(carrito.getId().intValue()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingCarrito() throws Exception {
        // Get the carrito
        restCarritoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCarrito() throws Exception {
        // Initialize the database
        insertedCarrito = carritoRepository.saveAndFlush(carrito);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the carrito
        Carrito updatedCarrito = carritoRepository.findById(carrito.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCarrito are not directly saved in db
        em.detach(updatedCarrito);
        updatedCarrito
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        CarritoDTO carritoDTO = carritoMapper.toDto(updatedCarrito);

        restCarritoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, carritoDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(carritoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Carrito in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCarritoToMatchAllProperties(updatedCarrito);
    }

    @Test
    @Transactional
    void putNonExistingCarrito() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        carrito.setId(longCount.incrementAndGet());

        // Create the Carrito
        CarritoDTO carritoDTO = carritoMapper.toDto(carrito);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCarritoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, carritoDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(carritoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Carrito in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCarrito() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        carrito.setId(longCount.incrementAndGet());

        // Create the Carrito
        CarritoDTO carritoDTO = carritoMapper.toDto(carrito);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCarritoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(carritoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Carrito in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCarrito() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        carrito.setId(longCount.incrementAndGet());

        // Create the Carrito
        CarritoDTO carritoDTO = carritoMapper.toDto(carrito);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCarritoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(carritoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Carrito in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCarritoWithPatch() throws Exception {
        // Initialize the database
        insertedCarrito = carritoRepository.saveAndFlush(carrito);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the carrito using partial update
        Carrito partialUpdatedCarrito = new Carrito();
        partialUpdatedCarrito.setId(carrito.getId());

        partialUpdatedCarrito.createdBy(UPDATED_CREATED_BY).createdDate(UPDATED_CREATED_DATE).lastModifiedBy(UPDATED_LAST_MODIFIED_BY);

        restCarritoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCarrito.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCarrito))
            )
            .andExpect(status().isOk());

        // Validate the Carrito in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCarritoUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedCarrito, carrito), getPersistedCarrito(carrito));
    }

    @Test
    @Transactional
    void fullUpdateCarritoWithPatch() throws Exception {
        // Initialize the database
        insertedCarrito = carritoRepository.saveAndFlush(carrito);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the carrito using partial update
        Carrito partialUpdatedCarrito = new Carrito();
        partialUpdatedCarrito.setId(carrito.getId());

        partialUpdatedCarrito
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restCarritoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCarrito.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCarrito))
            )
            .andExpect(status().isOk());

        // Validate the Carrito in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCarritoUpdatableFieldsEquals(partialUpdatedCarrito, getPersistedCarrito(partialUpdatedCarrito));
    }

    @Test
    @Transactional
    void patchNonExistingCarrito() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        carrito.setId(longCount.incrementAndGet());

        // Create the Carrito
        CarritoDTO carritoDTO = carritoMapper.toDto(carrito);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCarritoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, carritoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(carritoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Carrito in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCarrito() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        carrito.setId(longCount.incrementAndGet());

        // Create the Carrito
        CarritoDTO carritoDTO = carritoMapper.toDto(carrito);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCarritoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(carritoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Carrito in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCarrito() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        carrito.setId(longCount.incrementAndGet());

        // Create the Carrito
        CarritoDTO carritoDTO = carritoMapper.toDto(carrito);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCarritoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(carritoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Carrito in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCarrito() throws Exception {
        // Initialize the database
        insertedCarrito = carritoRepository.saveAndFlush(carrito);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the carrito
        restCarritoMockMvc
            .perform(delete(ENTITY_API_URL_ID, carrito.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return carritoRepository.count();
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

    protected Carrito getPersistedCarrito(Carrito carrito) {
        return carritoRepository.findById(carrito.getId()).orElseThrow();
    }

    protected void assertPersistedCarritoToMatchAllProperties(Carrito expectedCarrito) {
        assertCarritoAllPropertiesEquals(expectedCarrito, getPersistedCarrito(expectedCarrito));
    }

    protected void assertPersistedCarritoToMatchUpdatableProperties(Carrito expectedCarrito) {
        assertCarritoAllUpdatablePropertiesEquals(expectedCarrito, getPersistedCarrito(expectedCarrito));
    }
}
