package pe.commerce.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pe.commerce.app.domain.TiendaAsserts.*;
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
import pe.commerce.app.domain.Tienda;
import pe.commerce.app.domain.Ubigeo;
import pe.commerce.app.repository.TiendaRepository;
import pe.commerce.app.service.TiendaService;
import pe.commerce.app.service.dto.TiendaDTO;
import pe.commerce.app.service.mapper.TiendaMapper;
import tools.jackson.databind.ObjectMapper;

/**
 * Integration tests for the {@link TiendaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class TiendaResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_DIRECCION = "AAAAAAAAAA";
    private static final String UPDATED_DIRECCION = "BBBBBBBBBB";

    private static final String DEFAULT_TELEFONO = "AAAAAAAAAA";
    private static final String UPDATED_TELEFONO = "BBBBBBBBBB";

    private static final Boolean DEFAULT_PERMITE_RECOJO = false;
    private static final Boolean UPDATED_PERMITE_RECOJO = true;

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

    private static final String ENTITY_API_URL = "/api/tiendas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TiendaRepository tiendaRepository;

    @Mock
    private TiendaRepository tiendaRepositoryMock;

    @Autowired
    private TiendaMapper tiendaMapper;

    @Mock
    private TiendaService tiendaServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTiendaMockMvc;

    private Tienda tienda;

    private Tienda insertedTienda;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tienda createEntity(EntityManager em) {
        Tienda tienda = new Tienda()
            .nombre(DEFAULT_NOMBRE)
            .direccion(DEFAULT_DIRECCION)
            .telefono(DEFAULT_TELEFONO)
            .permiteRecojo(DEFAULT_PERMITE_RECOJO)
            .activo(DEFAULT_ACTIVO)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        // Add required entity
        Ubigeo ubigeo;
        if (TestUtil.findAll(em, Ubigeo.class).isEmpty()) {
            ubigeo = UbigeoResourceIT.createEntity();
            em.persist(ubigeo);
            em.flush();
        } else {
            ubigeo = TestUtil.findAll(em, Ubigeo.class).getFirst();
        }
        tienda.setUbigeo(ubigeo);
        return tienda;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tienda createUpdatedEntity(EntityManager em) {
        Tienda updatedTienda = new Tienda()
            .nombre(UPDATED_NOMBRE)
            .direccion(UPDATED_DIRECCION)
            .telefono(UPDATED_TELEFONO)
            .permiteRecojo(UPDATED_PERMITE_RECOJO)
            .activo(UPDATED_ACTIVO)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        // Add required entity
        Ubigeo ubigeo;
        if (TestUtil.findAll(em, Ubigeo.class).isEmpty()) {
            ubigeo = UbigeoResourceIT.createUpdatedEntity();
            em.persist(ubigeo);
            em.flush();
        } else {
            ubigeo = TestUtil.findAll(em, Ubigeo.class).getFirst();
        }
        updatedTienda.setUbigeo(ubigeo);
        return updatedTienda;
    }

    @BeforeEach
    void initTest() {
        tienda = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedTienda != null) {
            tiendaRepository.delete(insertedTienda);
            insertedTienda = null;
        }
    }

    @Test
    @Transactional
    void createTienda() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Tienda
        TiendaDTO tiendaDTO = tiendaMapper.toDto(tienda);
        var returnedTiendaDTO = om.readValue(
            restTiendaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tiendaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TiendaDTO.class
        );

        // Validate the Tienda in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedTienda = tiendaMapper.toEntity(returnedTiendaDTO);
        assertTiendaUpdatableFieldsEquals(returnedTienda, getPersistedTienda(returnedTienda));

        insertedTienda = returnedTienda;
    }

    @Test
    @Transactional
    void createTiendaWithExistingId() throws Exception {
        // Create the Tienda with an existing ID
        tienda.setId(1L);
        TiendaDTO tiendaDTO = tiendaMapper.toDto(tienda);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTiendaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tiendaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Tienda in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        tienda.setNombre(null);

        // Create the Tienda, which fails.
        TiendaDTO tiendaDTO = tiendaMapper.toDto(tienda);

        restTiendaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tiendaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDireccionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        tienda.setDireccion(null);

        // Create the Tienda, which fails.
        TiendaDTO tiendaDTO = tiendaMapper.toDto(tienda);

        restTiendaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tiendaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPermiteRecojoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        tienda.setPermiteRecojo(null);

        // Create the Tienda, which fails.
        TiendaDTO tiendaDTO = tiendaMapper.toDto(tienda);

        restTiendaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tiendaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActivoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        tienda.setActivo(null);

        // Create the Tienda, which fails.
        TiendaDTO tiendaDTO = tiendaMapper.toDto(tienda);

        restTiendaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tiendaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTiendas() throws Exception {
        // Initialize the database
        insertedTienda = tiendaRepository.saveAndFlush(tienda);

        // Get all the tiendaList
        restTiendaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tienda.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].direccion").value(hasItem(DEFAULT_DIRECCION)))
            .andExpect(jsonPath("$.[*].telefono").value(hasItem(DEFAULT_TELEFONO)))
            .andExpect(jsonPath("$.[*].permiteRecojo").value(hasItem(DEFAULT_PERMITE_RECOJO)))
            .andExpect(jsonPath("$.[*].activo").value(hasItem(DEFAULT_ACTIVO)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTiendasWithEagerRelationshipsIsEnabled() throws Exception {
        when(tiendaServiceMock.findAllWithEagerRelationships()).thenReturn(new ArrayList<>());

        restTiendaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(tiendaServiceMock, times(1)).findAllWithEagerRelationships();
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTiendasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(tiendaServiceMock.findAllWithEagerRelationships()).thenReturn(new ArrayList<>());

        restTiendaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(tiendaRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getTienda() throws Exception {
        // Initialize the database
        insertedTienda = tiendaRepository.saveAndFlush(tienda);

        // Get the tienda
        restTiendaMockMvc
            .perform(get(ENTITY_API_URL_ID, tienda.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tienda.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.direccion").value(DEFAULT_DIRECCION))
            .andExpect(jsonPath("$.telefono").value(DEFAULT_TELEFONO))
            .andExpect(jsonPath("$.permiteRecojo").value(DEFAULT_PERMITE_RECOJO))
            .andExpect(jsonPath("$.activo").value(DEFAULT_ACTIVO))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingTienda() throws Exception {
        // Get the tienda
        restTiendaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTienda() throws Exception {
        // Initialize the database
        insertedTienda = tiendaRepository.saveAndFlush(tienda);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tienda
        Tienda updatedTienda = tiendaRepository.findById(tienda.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTienda are not directly saved in db
        em.detach(updatedTienda);
        updatedTienda
            .nombre(UPDATED_NOMBRE)
            .direccion(UPDATED_DIRECCION)
            .telefono(UPDATED_TELEFONO)
            .permiteRecojo(UPDATED_PERMITE_RECOJO)
            .activo(UPDATED_ACTIVO)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        TiendaDTO tiendaDTO = tiendaMapper.toDto(updatedTienda);

        restTiendaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tiendaDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tiendaDTO))
            )
            .andExpect(status().isOk());

        // Validate the Tienda in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTiendaToMatchAllProperties(updatedTienda);
    }

    @Test
    @Transactional
    void putNonExistingTienda() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tienda.setId(longCount.incrementAndGet());

        // Create the Tienda
        TiendaDTO tiendaDTO = tiendaMapper.toDto(tienda);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTiendaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tiendaDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tiendaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tienda in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTienda() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tienda.setId(longCount.incrementAndGet());

        // Create the Tienda
        TiendaDTO tiendaDTO = tiendaMapper.toDto(tienda);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTiendaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(tiendaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tienda in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTienda() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tienda.setId(longCount.incrementAndGet());

        // Create the Tienda
        TiendaDTO tiendaDTO = tiendaMapper.toDto(tienda);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTiendaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tiendaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tienda in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTiendaWithPatch() throws Exception {
        // Initialize the database
        insertedTienda = tiendaRepository.saveAndFlush(tienda);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tienda using partial update
        Tienda partialUpdatedTienda = new Tienda();
        partialUpdatedTienda.setId(tienda.getId());

        partialUpdatedTienda
            .nombre(UPDATED_NOMBRE)
            .telefono(UPDATED_TELEFONO)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restTiendaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTienda.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTienda))
            )
            .andExpect(status().isOk());

        // Validate the Tienda in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTiendaUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedTienda, tienda), getPersistedTienda(tienda));
    }

    @Test
    @Transactional
    void fullUpdateTiendaWithPatch() throws Exception {
        // Initialize the database
        insertedTienda = tiendaRepository.saveAndFlush(tienda);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tienda using partial update
        Tienda partialUpdatedTienda = new Tienda();
        partialUpdatedTienda.setId(tienda.getId());

        partialUpdatedTienda
            .nombre(UPDATED_NOMBRE)
            .direccion(UPDATED_DIRECCION)
            .telefono(UPDATED_TELEFONO)
            .permiteRecojo(UPDATED_PERMITE_RECOJO)
            .activo(UPDATED_ACTIVO)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restTiendaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTienda.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTienda))
            )
            .andExpect(status().isOk());

        // Validate the Tienda in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTiendaUpdatableFieldsEquals(partialUpdatedTienda, getPersistedTienda(partialUpdatedTienda));
    }

    @Test
    @Transactional
    void patchNonExistingTienda() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tienda.setId(longCount.incrementAndGet());

        // Create the Tienda
        TiendaDTO tiendaDTO = tiendaMapper.toDto(tienda);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTiendaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tiendaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(tiendaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tienda in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTienda() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tienda.setId(longCount.incrementAndGet());

        // Create the Tienda
        TiendaDTO tiendaDTO = tiendaMapper.toDto(tienda);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTiendaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(tiendaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tienda in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTienda() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tienda.setId(longCount.incrementAndGet());

        // Create the Tienda
        TiendaDTO tiendaDTO = tiendaMapper.toDto(tienda);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTiendaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(tiendaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tienda in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTienda() throws Exception {
        // Initialize the database
        insertedTienda = tiendaRepository.saveAndFlush(tienda);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the tienda
        restTiendaMockMvc
            .perform(delete(ENTITY_API_URL_ID, tienda.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return tiendaRepository.count();
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

    protected Tienda getPersistedTienda(Tienda tienda) {
        return tiendaRepository.findById(tienda.getId()).orElseThrow();
    }

    protected void assertPersistedTiendaToMatchAllProperties(Tienda expectedTienda) {
        assertTiendaAllPropertiesEquals(expectedTienda, getPersistedTienda(expectedTienda));
    }

    protected void assertPersistedTiendaToMatchUpdatableProperties(Tienda expectedTienda) {
        assertTiendaAllUpdatablePropertiesEquals(expectedTienda, getPersistedTienda(expectedTienda));
    }
}
