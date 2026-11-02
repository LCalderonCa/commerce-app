package pe.commerce.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pe.commerce.app.domain.DireccionAsserts.*;
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
import pe.commerce.app.domain.Cliente;
import pe.commerce.app.domain.Direccion;
import pe.commerce.app.domain.Ubigeo;
import pe.commerce.app.repository.DireccionRepository;
import pe.commerce.app.service.DireccionService;
import pe.commerce.app.service.dto.DireccionDTO;
import pe.commerce.app.service.mapper.DireccionMapper;
import tools.jackson.databind.ObjectMapper;

/**
 * Integration tests for the {@link DireccionResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class DireccionResourceIT {

    private static final String DEFAULT_ALIAS = "AAAAAAAAAA";
    private static final String UPDATED_ALIAS = "BBBBBBBBBB";

    private static final String DEFAULT_DIRECCION = "AAAAAAAAAA";
    private static final String UPDATED_DIRECCION = "BBBBBBBBBB";

    private static final String DEFAULT_REFERENCIA = "AAAAAAAAAA";
    private static final String UPDATED_REFERENCIA = "BBBBBBBBBB";

    private static final String DEFAULT_NOMBRE_CONTACTO = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE_CONTACTO = "BBBBBBBBBB";

    private static final String DEFAULT_TELEFONO_CONTACTO = "AAAAAAAAAA";
    private static final String UPDATED_TELEFONO_CONTACTO = "BBBBBBBBBB";

    private static final Boolean DEFAULT_PREDETERMINADA = false;
    private static final Boolean UPDATED_PREDETERMINADA = true;

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

    private static final String ENTITY_API_URL = "/api/direccions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DireccionRepository direccionRepository;

    @Mock
    private DireccionRepository direccionRepositoryMock;

    @Autowired
    private DireccionMapper direccionMapper;

    @Mock
    private DireccionService direccionServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDireccionMockMvc;

    private Direccion direccion;

    private Direccion insertedDireccion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Direccion createEntity(EntityManager em) {
        Direccion direccion = new Direccion()
            .alias(DEFAULT_ALIAS)
            .direccion(DEFAULT_DIRECCION)
            .referencia(DEFAULT_REFERENCIA)
            .nombreContacto(DEFAULT_NOMBRE_CONTACTO)
            .telefonoContacto(DEFAULT_TELEFONO_CONTACTO)
            .predeterminada(DEFAULT_PREDETERMINADA)
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
        direccion.setUbigeo(ubigeo);
        // Add required entity
        Cliente cliente;
        if (TestUtil.findAll(em, Cliente.class).isEmpty()) {
            cliente = ClienteResourceIT.createEntity(em);
            em.persist(cliente);
            em.flush();
        } else {
            cliente = TestUtil.findAll(em, Cliente.class).getFirst();
        }
        direccion.setCliente(cliente);
        return direccion;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Direccion createUpdatedEntity(EntityManager em) {
        Direccion updatedDireccion = new Direccion()
            .alias(UPDATED_ALIAS)
            .direccion(UPDATED_DIRECCION)
            .referencia(UPDATED_REFERENCIA)
            .nombreContacto(UPDATED_NOMBRE_CONTACTO)
            .telefonoContacto(UPDATED_TELEFONO_CONTACTO)
            .predeterminada(UPDATED_PREDETERMINADA)
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
        updatedDireccion.setUbigeo(ubigeo);
        // Add required entity
        Cliente cliente;
        if (TestUtil.findAll(em, Cliente.class).isEmpty()) {
            cliente = ClienteResourceIT.createUpdatedEntity(em);
            em.persist(cliente);
            em.flush();
        } else {
            cliente = TestUtil.findAll(em, Cliente.class).getFirst();
        }
        updatedDireccion.setCliente(cliente);
        return updatedDireccion;
    }

    @BeforeEach
    void initTest() {
        direccion = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedDireccion != null) {
            direccionRepository.delete(insertedDireccion);
            insertedDireccion = null;
        }
    }

    @Test
    @Transactional
    void createDireccion() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Direccion
        DireccionDTO direccionDTO = direccionMapper.toDto(direccion);
        var returnedDireccionDTO = om.readValue(
            restDireccionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(direccionDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DireccionDTO.class
        );

        // Validate the Direccion in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedDireccion = direccionMapper.toEntity(returnedDireccionDTO);
        assertDireccionUpdatableFieldsEquals(returnedDireccion, getPersistedDireccion(returnedDireccion));

        insertedDireccion = returnedDireccion;
    }

    @Test
    @Transactional
    void createDireccionWithExistingId() throws Exception {
        // Create the Direccion with an existing ID
        direccion.setId(1L);
        DireccionDTO direccionDTO = direccionMapper.toDto(direccion);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDireccionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(direccionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Direccion in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAliasIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        direccion.setAlias(null);

        // Create the Direccion, which fails.
        DireccionDTO direccionDTO = direccionMapper.toDto(direccion);

        restDireccionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(direccionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDireccionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        direccion.setDireccion(null);

        // Create the Direccion, which fails.
        DireccionDTO direccionDTO = direccionMapper.toDto(direccion);

        restDireccionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(direccionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPredeterminadaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        direccion.setPredeterminada(null);

        // Create the Direccion, which fails.
        DireccionDTO direccionDTO = direccionMapper.toDto(direccion);

        restDireccionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(direccionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActivoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        direccion.setActivo(null);

        // Create the Direccion, which fails.
        DireccionDTO direccionDTO = direccionMapper.toDto(direccion);

        restDireccionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(direccionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDireccions() throws Exception {
        // Initialize the database
        insertedDireccion = direccionRepository.saveAndFlush(direccion);

        // Get all the direccionList
        restDireccionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(direccion.getId().intValue())))
            .andExpect(jsonPath("$.[*].alias").value(hasItem(DEFAULT_ALIAS)))
            .andExpect(jsonPath("$.[*].direccion").value(hasItem(DEFAULT_DIRECCION)))
            .andExpect(jsonPath("$.[*].referencia").value(hasItem(DEFAULT_REFERENCIA)))
            .andExpect(jsonPath("$.[*].nombreContacto").value(hasItem(DEFAULT_NOMBRE_CONTACTO)))
            .andExpect(jsonPath("$.[*].telefonoContacto").value(hasItem(DEFAULT_TELEFONO_CONTACTO)))
            .andExpect(jsonPath("$.[*].predeterminada").value(hasItem(DEFAULT_PREDETERMINADA)))
            .andExpect(jsonPath("$.[*].activo").value(hasItem(DEFAULT_ACTIVO)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDireccionsWithEagerRelationshipsIsEnabled() throws Exception {
        when(direccionServiceMock.findAllWithEagerRelationships()).thenReturn(new ArrayList<>());

        restDireccionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(direccionServiceMock, times(1)).findAllWithEagerRelationships();
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDireccionsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(direccionServiceMock.findAllWithEagerRelationships()).thenReturn(new ArrayList<>());

        restDireccionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(direccionRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getDireccion() throws Exception {
        // Initialize the database
        insertedDireccion = direccionRepository.saveAndFlush(direccion);

        // Get the direccion
        restDireccionMockMvc
            .perform(get(ENTITY_API_URL_ID, direccion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(direccion.getId().intValue()))
            .andExpect(jsonPath("$.alias").value(DEFAULT_ALIAS))
            .andExpect(jsonPath("$.direccion").value(DEFAULT_DIRECCION))
            .andExpect(jsonPath("$.referencia").value(DEFAULT_REFERENCIA))
            .andExpect(jsonPath("$.nombreContacto").value(DEFAULT_NOMBRE_CONTACTO))
            .andExpect(jsonPath("$.telefonoContacto").value(DEFAULT_TELEFONO_CONTACTO))
            .andExpect(jsonPath("$.predeterminada").value(DEFAULT_PREDETERMINADA))
            .andExpect(jsonPath("$.activo").value(DEFAULT_ACTIVO))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingDireccion() throws Exception {
        // Get the direccion
        restDireccionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDireccion() throws Exception {
        // Initialize the database
        insertedDireccion = direccionRepository.saveAndFlush(direccion);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the direccion
        Direccion updatedDireccion = direccionRepository.findById(direccion.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDireccion are not directly saved in db
        em.detach(updatedDireccion);
        updatedDireccion
            .alias(UPDATED_ALIAS)
            .direccion(UPDATED_DIRECCION)
            .referencia(UPDATED_REFERENCIA)
            .nombreContacto(UPDATED_NOMBRE_CONTACTO)
            .telefonoContacto(UPDATED_TELEFONO_CONTACTO)
            .predeterminada(UPDATED_PREDETERMINADA)
            .activo(UPDATED_ACTIVO)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        DireccionDTO direccionDTO = direccionMapper.toDto(updatedDireccion);

        restDireccionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, direccionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(direccionDTO))
            )
            .andExpect(status().isOk());

        // Validate the Direccion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDireccionToMatchAllProperties(updatedDireccion);
    }

    @Test
    @Transactional
    void putNonExistingDireccion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        direccion.setId(longCount.incrementAndGet());

        // Create the Direccion
        DireccionDTO direccionDTO = direccionMapper.toDto(direccion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDireccionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, direccionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(direccionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Direccion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDireccion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        direccion.setId(longCount.incrementAndGet());

        // Create the Direccion
        DireccionDTO direccionDTO = direccionMapper.toDto(direccion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDireccionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(direccionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Direccion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDireccion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        direccion.setId(longCount.incrementAndGet());

        // Create the Direccion
        DireccionDTO direccionDTO = direccionMapper.toDto(direccion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDireccionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(direccionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Direccion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDireccionWithPatch() throws Exception {
        // Initialize the database
        insertedDireccion = direccionRepository.saveAndFlush(direccion);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the direccion using partial update
        Direccion partialUpdatedDireccion = new Direccion();
        partialUpdatedDireccion.setId(direccion.getId());

        partialUpdatedDireccion
            .alias(UPDATED_ALIAS)
            .direccion(UPDATED_DIRECCION)
            .telefonoContacto(UPDATED_TELEFONO_CONTACTO)
            .activo(UPDATED_ACTIVO)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restDireccionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDireccion.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDireccion))
            )
            .andExpect(status().isOk());

        // Validate the Direccion in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDireccionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDireccion, direccion),
            getPersistedDireccion(direccion)
        );
    }

    @Test
    @Transactional
    void fullUpdateDireccionWithPatch() throws Exception {
        // Initialize the database
        insertedDireccion = direccionRepository.saveAndFlush(direccion);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the direccion using partial update
        Direccion partialUpdatedDireccion = new Direccion();
        partialUpdatedDireccion.setId(direccion.getId());

        partialUpdatedDireccion
            .alias(UPDATED_ALIAS)
            .direccion(UPDATED_DIRECCION)
            .referencia(UPDATED_REFERENCIA)
            .nombreContacto(UPDATED_NOMBRE_CONTACTO)
            .telefonoContacto(UPDATED_TELEFONO_CONTACTO)
            .predeterminada(UPDATED_PREDETERMINADA)
            .activo(UPDATED_ACTIVO)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restDireccionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDireccion.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDireccion))
            )
            .andExpect(status().isOk());

        // Validate the Direccion in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDireccionUpdatableFieldsEquals(partialUpdatedDireccion, getPersistedDireccion(partialUpdatedDireccion));
    }

    @Test
    @Transactional
    void patchNonExistingDireccion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        direccion.setId(longCount.incrementAndGet());

        // Create the Direccion
        DireccionDTO direccionDTO = direccionMapper.toDto(direccion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDireccionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, direccionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(direccionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Direccion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDireccion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        direccion.setId(longCount.incrementAndGet());

        // Create the Direccion
        DireccionDTO direccionDTO = direccionMapper.toDto(direccion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDireccionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(direccionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Direccion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDireccion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        direccion.setId(longCount.incrementAndGet());

        // Create the Direccion
        DireccionDTO direccionDTO = direccionMapper.toDto(direccion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDireccionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(direccionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Direccion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDireccion() throws Exception {
        // Initialize the database
        insertedDireccion = direccionRepository.saveAndFlush(direccion);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the direccion
        restDireccionMockMvc
            .perform(delete(ENTITY_API_URL_ID, direccion.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return direccionRepository.count();
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

    protected Direccion getPersistedDireccion(Direccion direccion) {
        return direccionRepository.findById(direccion.getId()).orElseThrow();
    }

    protected void assertPersistedDireccionToMatchAllProperties(Direccion expectedDireccion) {
        assertDireccionAllPropertiesEquals(expectedDireccion, getPersistedDireccion(expectedDireccion));
    }

    protected void assertPersistedDireccionToMatchUpdatableProperties(Direccion expectedDireccion) {
        assertDireccionAllUpdatablePropertiesEquals(expectedDireccion, getPersistedDireccion(expectedDireccion));
    }
}
