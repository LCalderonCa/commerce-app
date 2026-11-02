package pe.commerce.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pe.commerce.app.domain.PedidoEstadoHistorialAsserts.*;
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
import pe.commerce.app.domain.Pedido;
import pe.commerce.app.domain.PedidoEstadoHistorial;
import pe.commerce.app.domain.enumeration.EstadoPedido;
import pe.commerce.app.repository.PedidoEstadoHistorialRepository;
import pe.commerce.app.service.PedidoEstadoHistorialService;
import pe.commerce.app.service.dto.PedidoEstadoHistorialDTO;
import pe.commerce.app.service.mapper.PedidoEstadoHistorialMapper;
import tools.jackson.databind.ObjectMapper;

/**
 * Integration tests for the {@link PedidoEstadoHistorialResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PedidoEstadoHistorialResourceIT {

    private static final EstadoPedido DEFAULT_ESTADO = EstadoPedido.PENDIENTE_PAGO;
    private static final EstadoPedido UPDATED_ESTADO = EstadoPedido.PAGADO;

    private static final String DEFAULT_COMENTARIO = "AAAAAAAAAA";
    private static final String UPDATED_COMENTARIO = "BBBBBBBBBB";

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.ofEpochMilli(1702246707543L);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.ofEpochMilli(1702246707543L);

    private static final String ENTITY_API_URL = "/api/pedido-estado-historials";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PedidoEstadoHistorialRepository pedidoEstadoHistorialRepository;

    @Mock
    private PedidoEstadoHistorialRepository pedidoEstadoHistorialRepositoryMock;

    @Autowired
    private PedidoEstadoHistorialMapper pedidoEstadoHistorialMapper;

    @Mock
    private PedidoEstadoHistorialService pedidoEstadoHistorialServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPedidoEstadoHistorialMockMvc;

    private PedidoEstadoHistorial pedidoEstadoHistorial;

    private PedidoEstadoHistorial insertedPedidoEstadoHistorial;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PedidoEstadoHistorial createEntity(EntityManager em) {
        PedidoEstadoHistorial pedidoEstadoHistorial = new PedidoEstadoHistorial()
            .estado(DEFAULT_ESTADO)
            .comentario(DEFAULT_COMENTARIO)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        // Add required entity
        Pedido pedido;
        if (TestUtil.findAll(em, Pedido.class).isEmpty()) {
            pedido = PedidoResourceIT.createEntity(em);
            em.persist(pedido);
            em.flush();
        } else {
            pedido = TestUtil.findAll(em, Pedido.class).getFirst();
        }
        pedidoEstadoHistorial.setPedido(pedido);
        return pedidoEstadoHistorial;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PedidoEstadoHistorial createUpdatedEntity(EntityManager em) {
        PedidoEstadoHistorial updatedPedidoEstadoHistorial = new PedidoEstadoHistorial()
            .estado(UPDATED_ESTADO)
            .comentario(UPDATED_COMENTARIO)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        // Add required entity
        Pedido pedido;
        if (TestUtil.findAll(em, Pedido.class).isEmpty()) {
            pedido = PedidoResourceIT.createUpdatedEntity(em);
            em.persist(pedido);
            em.flush();
        } else {
            pedido = TestUtil.findAll(em, Pedido.class).getFirst();
        }
        updatedPedidoEstadoHistorial.setPedido(pedido);
        return updatedPedidoEstadoHistorial;
    }

    @BeforeEach
    void initTest() {
        pedidoEstadoHistorial = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedPedidoEstadoHistorial != null) {
            pedidoEstadoHistorialRepository.delete(insertedPedidoEstadoHistorial);
            insertedPedidoEstadoHistorial = null;
        }
    }

    @Test
    @Transactional
    void createPedidoEstadoHistorial() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PedidoEstadoHistorial
        PedidoEstadoHistorialDTO pedidoEstadoHistorialDTO = pedidoEstadoHistorialMapper.toDto(pedidoEstadoHistorial);
        var returnedPedidoEstadoHistorialDTO = om.readValue(
            restPedidoEstadoHistorialMockMvc
                .perform(
                    post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pedidoEstadoHistorialDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PedidoEstadoHistorialDTO.class
        );

        // Validate the PedidoEstadoHistorial in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPedidoEstadoHistorial = pedidoEstadoHistorialMapper.toEntity(returnedPedidoEstadoHistorialDTO);
        assertPedidoEstadoHistorialUpdatableFieldsEquals(
            returnedPedidoEstadoHistorial,
            getPersistedPedidoEstadoHistorial(returnedPedidoEstadoHistorial)
        );

        insertedPedidoEstadoHistorial = returnedPedidoEstadoHistorial;
    }

    @Test
    @Transactional
    void createPedidoEstadoHistorialWithExistingId() throws Exception {
        // Create the PedidoEstadoHistorial with an existing ID
        pedidoEstadoHistorial.setId(1L);
        PedidoEstadoHistorialDTO pedidoEstadoHistorialDTO = pedidoEstadoHistorialMapper.toDto(pedidoEstadoHistorial);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPedidoEstadoHistorialMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pedidoEstadoHistorialDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PedidoEstadoHistorial in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkEstadoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        pedidoEstadoHistorial.setEstado(null);

        // Create the PedidoEstadoHistorial, which fails.
        PedidoEstadoHistorialDTO pedidoEstadoHistorialDTO = pedidoEstadoHistorialMapper.toDto(pedidoEstadoHistorial);

        restPedidoEstadoHistorialMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pedidoEstadoHistorialDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPedidoEstadoHistorials() throws Exception {
        // Initialize the database
        insertedPedidoEstadoHistorial = pedidoEstadoHistorialRepository.saveAndFlush(pedidoEstadoHistorial);

        // Get all the pedidoEstadoHistorialList
        restPedidoEstadoHistorialMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pedidoEstadoHistorial.getId().intValue())))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO.toString())))
            .andExpect(jsonPath("$.[*].comentario").value(hasItem(DEFAULT_COMENTARIO)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPedidoEstadoHistorialsWithEagerRelationshipsIsEnabled() throws Exception {
        when(pedidoEstadoHistorialServiceMock.findAllWithEagerRelationships()).thenReturn(new ArrayList<>());

        restPedidoEstadoHistorialMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(pedidoEstadoHistorialServiceMock, times(1)).findAllWithEagerRelationships();
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPedidoEstadoHistorialsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(pedidoEstadoHistorialServiceMock.findAllWithEagerRelationships()).thenReturn(new ArrayList<>());

        restPedidoEstadoHistorialMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(pedidoEstadoHistorialRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getPedidoEstadoHistorial() throws Exception {
        // Initialize the database
        insertedPedidoEstadoHistorial = pedidoEstadoHistorialRepository.saveAndFlush(pedidoEstadoHistorial);

        // Get the pedidoEstadoHistorial
        restPedidoEstadoHistorialMockMvc
            .perform(get(ENTITY_API_URL_ID, pedidoEstadoHistorial.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pedidoEstadoHistorial.getId().intValue()))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO.toString()))
            .andExpect(jsonPath("$.comentario").value(DEFAULT_COMENTARIO))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingPedidoEstadoHistorial() throws Exception {
        // Get the pedidoEstadoHistorial
        restPedidoEstadoHistorialMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPedidoEstadoHistorial() throws Exception {
        // Initialize the database
        insertedPedidoEstadoHistorial = pedidoEstadoHistorialRepository.saveAndFlush(pedidoEstadoHistorial);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pedidoEstadoHistorial
        PedidoEstadoHistorial updatedPedidoEstadoHistorial = pedidoEstadoHistorialRepository
            .findById(pedidoEstadoHistorial.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedPedidoEstadoHistorial are not directly saved in db
        em.detach(updatedPedidoEstadoHistorial);
        updatedPedidoEstadoHistorial
            .estado(UPDATED_ESTADO)
            .comentario(UPDATED_COMENTARIO)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        PedidoEstadoHistorialDTO pedidoEstadoHistorialDTO = pedidoEstadoHistorialMapper.toDto(updatedPedidoEstadoHistorial);

        restPedidoEstadoHistorialMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pedidoEstadoHistorialDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(pedidoEstadoHistorialDTO))
            )
            .andExpect(status().isOk());

        // Validate the PedidoEstadoHistorial in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPedidoEstadoHistorialToMatchAllProperties(updatedPedidoEstadoHistorial);
    }

    @Test
    @Transactional
    void putNonExistingPedidoEstadoHistorial() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pedidoEstadoHistorial.setId(longCount.incrementAndGet());

        // Create the PedidoEstadoHistorial
        PedidoEstadoHistorialDTO pedidoEstadoHistorialDTO = pedidoEstadoHistorialMapper.toDto(pedidoEstadoHistorial);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPedidoEstadoHistorialMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pedidoEstadoHistorialDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(pedidoEstadoHistorialDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PedidoEstadoHistorial in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPedidoEstadoHistorial() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pedidoEstadoHistorial.setId(longCount.incrementAndGet());

        // Create the PedidoEstadoHistorial
        PedidoEstadoHistorialDTO pedidoEstadoHistorialDTO = pedidoEstadoHistorialMapper.toDto(pedidoEstadoHistorial);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPedidoEstadoHistorialMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(pedidoEstadoHistorialDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PedidoEstadoHistorial in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPedidoEstadoHistorial() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pedidoEstadoHistorial.setId(longCount.incrementAndGet());

        // Create the PedidoEstadoHistorial
        PedidoEstadoHistorialDTO pedidoEstadoHistorialDTO = pedidoEstadoHistorialMapper.toDto(pedidoEstadoHistorial);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPedidoEstadoHistorialMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pedidoEstadoHistorialDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PedidoEstadoHistorial in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePedidoEstadoHistorialWithPatch() throws Exception {
        // Initialize the database
        insertedPedidoEstadoHistorial = pedidoEstadoHistorialRepository.saveAndFlush(pedidoEstadoHistorial);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pedidoEstadoHistorial using partial update
        PedidoEstadoHistorial partialUpdatedPedidoEstadoHistorial = new PedidoEstadoHistorial();
        partialUpdatedPedidoEstadoHistorial.setId(pedidoEstadoHistorial.getId());

        partialUpdatedPedidoEstadoHistorial
            .estado(UPDATED_ESTADO)
            .comentario(UPDATED_COMENTARIO)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);

        restPedidoEstadoHistorialMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPedidoEstadoHistorial.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPedidoEstadoHistorial))
            )
            .andExpect(status().isOk());

        // Validate the PedidoEstadoHistorial in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPedidoEstadoHistorialUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPedidoEstadoHistorial, pedidoEstadoHistorial),
            getPersistedPedidoEstadoHistorial(pedidoEstadoHistorial)
        );
    }

    @Test
    @Transactional
    void fullUpdatePedidoEstadoHistorialWithPatch() throws Exception {
        // Initialize the database
        insertedPedidoEstadoHistorial = pedidoEstadoHistorialRepository.saveAndFlush(pedidoEstadoHistorial);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pedidoEstadoHistorial using partial update
        PedidoEstadoHistorial partialUpdatedPedidoEstadoHistorial = new PedidoEstadoHistorial();
        partialUpdatedPedidoEstadoHistorial.setId(pedidoEstadoHistorial.getId());

        partialUpdatedPedidoEstadoHistorial
            .estado(UPDATED_ESTADO)
            .comentario(UPDATED_COMENTARIO)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restPedidoEstadoHistorialMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPedidoEstadoHistorial.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPedidoEstadoHistorial))
            )
            .andExpect(status().isOk());

        // Validate the PedidoEstadoHistorial in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPedidoEstadoHistorialUpdatableFieldsEquals(
            partialUpdatedPedidoEstadoHistorial,
            getPersistedPedidoEstadoHistorial(partialUpdatedPedidoEstadoHistorial)
        );
    }

    @Test
    @Transactional
    void patchNonExistingPedidoEstadoHistorial() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pedidoEstadoHistorial.setId(longCount.incrementAndGet());

        // Create the PedidoEstadoHistorial
        PedidoEstadoHistorialDTO pedidoEstadoHistorialDTO = pedidoEstadoHistorialMapper.toDto(pedidoEstadoHistorial);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPedidoEstadoHistorialMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pedidoEstadoHistorialDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(pedidoEstadoHistorialDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PedidoEstadoHistorial in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPedidoEstadoHistorial() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pedidoEstadoHistorial.setId(longCount.incrementAndGet());

        // Create the PedidoEstadoHistorial
        PedidoEstadoHistorialDTO pedidoEstadoHistorialDTO = pedidoEstadoHistorialMapper.toDto(pedidoEstadoHistorial);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPedidoEstadoHistorialMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(pedidoEstadoHistorialDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PedidoEstadoHistorial in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPedidoEstadoHistorial() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pedidoEstadoHistorial.setId(longCount.incrementAndGet());

        // Create the PedidoEstadoHistorial
        PedidoEstadoHistorialDTO pedidoEstadoHistorialDTO = pedidoEstadoHistorialMapper.toDto(pedidoEstadoHistorial);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPedidoEstadoHistorialMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(pedidoEstadoHistorialDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PedidoEstadoHistorial in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePedidoEstadoHistorial() throws Exception {
        // Initialize the database
        insertedPedidoEstadoHistorial = pedidoEstadoHistorialRepository.saveAndFlush(pedidoEstadoHistorial);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the pedidoEstadoHistorial
        restPedidoEstadoHistorialMockMvc
            .perform(delete(ENTITY_API_URL_ID, pedidoEstadoHistorial.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return pedidoEstadoHistorialRepository.count();
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

    protected PedidoEstadoHistorial getPersistedPedidoEstadoHistorial(PedidoEstadoHistorial pedidoEstadoHistorial) {
        return pedidoEstadoHistorialRepository.findById(pedidoEstadoHistorial.getId()).orElseThrow();
    }

    protected void assertPersistedPedidoEstadoHistorialToMatchAllProperties(PedidoEstadoHistorial expectedPedidoEstadoHistorial) {
        assertPedidoEstadoHistorialAllPropertiesEquals(
            expectedPedidoEstadoHistorial,
            getPersistedPedidoEstadoHistorial(expectedPedidoEstadoHistorial)
        );
    }

    protected void assertPersistedPedidoEstadoHistorialToMatchUpdatableProperties(PedidoEstadoHistorial expectedPedidoEstadoHistorial) {
        assertPedidoEstadoHistorialAllUpdatablePropertiesEquals(
            expectedPedidoEstadoHistorial,
            getPersistedPedidoEstadoHistorial(expectedPedidoEstadoHistorial)
        );
    }
}
