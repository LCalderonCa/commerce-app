package pe.commerce.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pe.commerce.app.domain.MedioContactoAsserts.*;
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
import pe.commerce.app.domain.MedioContacto;
import pe.commerce.app.domain.Persona;
import pe.commerce.app.domain.enumeration.TipoMedioContacto;
import pe.commerce.app.repository.MedioContactoRepository;
import pe.commerce.app.service.MedioContactoService;
import pe.commerce.app.service.dto.MedioContactoDTO;
import pe.commerce.app.service.mapper.MedioContactoMapper;
import tools.jackson.databind.ObjectMapper;

/**
 * Integration tests for the {@link MedioContactoResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class MedioContactoResourceIT {

    private static final TipoMedioContacto DEFAULT_TIPO = TipoMedioContacto.CELULAR;
    private static final TipoMedioContacto UPDATED_TIPO = TipoMedioContacto.FIJO;

    private static final String DEFAULT_VALOR = "AAAAAAAAAA";
    private static final String UPDATED_VALOR = "BBBBBBBBBB";

    private static final String DEFAULT_CODIGO_PAIS = "AAAA";
    private static final String UPDATED_CODIGO_PAIS = "BBBB";

    private static final String DEFAULT_ANEXO = "AAAAAA";
    private static final String UPDATED_ANEXO = "BBBBBB";

    private static final Boolean DEFAULT_PRINCIPAL = false;
    private static final Boolean UPDATED_PRINCIPAL = true;

    private static final Boolean DEFAULT_VERIFICADO = false;
    private static final Boolean UPDATED_VERIFICADO = true;

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

    private static final String ENTITY_API_URL = "/api/medio-contactos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MedioContactoRepository medioContactoRepository;

    @Mock
    private MedioContactoRepository medioContactoRepositoryMock;

    @Autowired
    private MedioContactoMapper medioContactoMapper;

    @Mock
    private MedioContactoService medioContactoServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMedioContactoMockMvc;

    private MedioContacto medioContacto;

    private MedioContacto insertedMedioContacto;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MedioContacto createEntity(EntityManager em) {
        MedioContacto medioContacto = new MedioContacto()
            .tipo(DEFAULT_TIPO)
            .valor(DEFAULT_VALOR)
            .codigoPais(DEFAULT_CODIGO_PAIS)
            .anexo(DEFAULT_ANEXO)
            .principal(DEFAULT_PRINCIPAL)
            .verificado(DEFAULT_VERIFICADO)
            .activo(DEFAULT_ACTIVO)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        // Add required entity
        Persona persona;
        if (TestUtil.findAll(em, Persona.class).isEmpty()) {
            persona = PersonaResourceIT.createEntity();
            em.persist(persona);
            em.flush();
        } else {
            persona = TestUtil.findAll(em, Persona.class).getFirst();
        }
        medioContacto.setPersona(persona);
        return medioContacto;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MedioContacto createUpdatedEntity(EntityManager em) {
        MedioContacto updatedMedioContacto = new MedioContacto()
            .tipo(UPDATED_TIPO)
            .valor(UPDATED_VALOR)
            .codigoPais(UPDATED_CODIGO_PAIS)
            .anexo(UPDATED_ANEXO)
            .principal(UPDATED_PRINCIPAL)
            .verificado(UPDATED_VERIFICADO)
            .activo(UPDATED_ACTIVO)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        // Add required entity
        Persona persona;
        if (TestUtil.findAll(em, Persona.class).isEmpty()) {
            persona = PersonaResourceIT.createUpdatedEntity();
            em.persist(persona);
            em.flush();
        } else {
            persona = TestUtil.findAll(em, Persona.class).getFirst();
        }
        updatedMedioContacto.setPersona(persona);
        return updatedMedioContacto;
    }

    @BeforeEach
    void initTest() {
        medioContacto = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedMedioContacto != null) {
            medioContactoRepository.delete(insertedMedioContacto);
            insertedMedioContacto = null;
        }
    }

    @Test
    @Transactional
    void createMedioContacto() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the MedioContacto
        MedioContactoDTO medioContactoDTO = medioContactoMapper.toDto(medioContacto);
        var returnedMedioContactoDTO = om.readValue(
            restMedioContactoMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medioContactoDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MedioContactoDTO.class
        );

        // Validate the MedioContacto in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMedioContacto = medioContactoMapper.toEntity(returnedMedioContactoDTO);
        assertMedioContactoUpdatableFieldsEquals(returnedMedioContacto, getPersistedMedioContacto(returnedMedioContacto));

        insertedMedioContacto = returnedMedioContacto;
    }

    @Test
    @Transactional
    void createMedioContactoWithExistingId() throws Exception {
        // Create the MedioContacto with an existing ID
        medioContacto.setId(1L);
        MedioContactoDTO medioContactoDTO = medioContactoMapper.toDto(medioContacto);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMedioContactoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medioContactoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MedioContacto in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTipoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        medioContacto.setTipo(null);

        // Create the MedioContacto, which fails.
        MedioContactoDTO medioContactoDTO = medioContactoMapper.toDto(medioContacto);

        restMedioContactoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medioContactoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkValorIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        medioContacto.setValor(null);

        // Create the MedioContacto, which fails.
        MedioContactoDTO medioContactoDTO = medioContactoMapper.toDto(medioContacto);

        restMedioContactoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medioContactoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPrincipalIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        medioContacto.setPrincipal(null);

        // Create the MedioContacto, which fails.
        MedioContactoDTO medioContactoDTO = medioContactoMapper.toDto(medioContacto);

        restMedioContactoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medioContactoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkVerificadoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        medioContacto.setVerificado(null);

        // Create the MedioContacto, which fails.
        MedioContactoDTO medioContactoDTO = medioContactoMapper.toDto(medioContacto);

        restMedioContactoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medioContactoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActivoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        medioContacto.setActivo(null);

        // Create the MedioContacto, which fails.
        MedioContactoDTO medioContactoDTO = medioContactoMapper.toDto(medioContacto);

        restMedioContactoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medioContactoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMedioContactos() throws Exception {
        // Initialize the database
        insertedMedioContacto = medioContactoRepository.saveAndFlush(medioContacto);

        // Get all the medioContactoList
        restMedioContactoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(medioContacto.getId().intValue())))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO.toString())))
            .andExpect(jsonPath("$.[*].valor").value(hasItem(DEFAULT_VALOR)))
            .andExpect(jsonPath("$.[*].codigoPais").value(hasItem(DEFAULT_CODIGO_PAIS)))
            .andExpect(jsonPath("$.[*].anexo").value(hasItem(DEFAULT_ANEXO)))
            .andExpect(jsonPath("$.[*].principal").value(hasItem(DEFAULT_PRINCIPAL)))
            .andExpect(jsonPath("$.[*].verificado").value(hasItem(DEFAULT_VERIFICADO)))
            .andExpect(jsonPath("$.[*].activo").value(hasItem(DEFAULT_ACTIVO)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMedioContactosWithEagerRelationshipsIsEnabled() throws Exception {
        when(medioContactoServiceMock.findAllWithEagerRelationships()).thenReturn(new ArrayList<>());

        restMedioContactoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(medioContactoServiceMock, times(1)).findAllWithEagerRelationships();
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMedioContactosWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(medioContactoServiceMock.findAllWithEagerRelationships()).thenReturn(new ArrayList<>());

        restMedioContactoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(medioContactoRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getMedioContacto() throws Exception {
        // Initialize the database
        insertedMedioContacto = medioContactoRepository.saveAndFlush(medioContacto);

        // Get the medioContacto
        restMedioContactoMockMvc
            .perform(get(ENTITY_API_URL_ID, medioContacto.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(medioContacto.getId().intValue()))
            .andExpect(jsonPath("$.tipo").value(DEFAULT_TIPO.toString()))
            .andExpect(jsonPath("$.valor").value(DEFAULT_VALOR))
            .andExpect(jsonPath("$.codigoPais").value(DEFAULT_CODIGO_PAIS))
            .andExpect(jsonPath("$.anexo").value(DEFAULT_ANEXO))
            .andExpect(jsonPath("$.principal").value(DEFAULT_PRINCIPAL))
            .andExpect(jsonPath("$.verificado").value(DEFAULT_VERIFICADO))
            .andExpect(jsonPath("$.activo").value(DEFAULT_ACTIVO))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingMedioContacto() throws Exception {
        // Get the medioContacto
        restMedioContactoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMedioContacto() throws Exception {
        // Initialize the database
        insertedMedioContacto = medioContactoRepository.saveAndFlush(medioContacto);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the medioContacto
        MedioContacto updatedMedioContacto = medioContactoRepository.findById(medioContacto.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMedioContacto are not directly saved in db
        em.detach(updatedMedioContacto);
        updatedMedioContacto
            .tipo(UPDATED_TIPO)
            .valor(UPDATED_VALOR)
            .codigoPais(UPDATED_CODIGO_PAIS)
            .anexo(UPDATED_ANEXO)
            .principal(UPDATED_PRINCIPAL)
            .verificado(UPDATED_VERIFICADO)
            .activo(UPDATED_ACTIVO)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        MedioContactoDTO medioContactoDTO = medioContactoMapper.toDto(updatedMedioContacto);

        restMedioContactoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, medioContactoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(medioContactoDTO))
            )
            .andExpect(status().isOk());

        // Validate the MedioContacto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMedioContactoToMatchAllProperties(updatedMedioContacto);
    }

    @Test
    @Transactional
    void putNonExistingMedioContacto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medioContacto.setId(longCount.incrementAndGet());

        // Create the MedioContacto
        MedioContactoDTO medioContactoDTO = medioContactoMapper.toDto(medioContacto);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMedioContactoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, medioContactoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(medioContactoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MedioContacto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMedioContacto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medioContacto.setId(longCount.incrementAndGet());

        // Create the MedioContacto
        MedioContactoDTO medioContactoDTO = medioContactoMapper.toDto(medioContacto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedioContactoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(medioContactoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MedioContacto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMedioContacto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medioContacto.setId(longCount.incrementAndGet());

        // Create the MedioContacto
        MedioContactoDTO medioContactoDTO = medioContactoMapper.toDto(medioContacto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedioContactoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(medioContactoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MedioContacto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMedioContactoWithPatch() throws Exception {
        // Initialize the database
        insertedMedioContacto = medioContactoRepository.saveAndFlush(medioContacto);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the medioContacto using partial update
        MedioContacto partialUpdatedMedioContacto = new MedioContacto();
        partialUpdatedMedioContacto.setId(medioContacto.getId());

        partialUpdatedMedioContacto
            .tipo(UPDATED_TIPO)
            .codigoPais(UPDATED_CODIGO_PAIS)
            .anexo(UPDATED_ANEXO)
            .verificado(UPDATED_VERIFICADO)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);

        restMedioContactoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMedioContacto.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMedioContacto))
            )
            .andExpect(status().isOk());

        // Validate the MedioContacto in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMedioContactoUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedMedioContacto, medioContacto),
            getPersistedMedioContacto(medioContacto)
        );
    }

    @Test
    @Transactional
    void fullUpdateMedioContactoWithPatch() throws Exception {
        // Initialize the database
        insertedMedioContacto = medioContactoRepository.saveAndFlush(medioContacto);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the medioContacto using partial update
        MedioContacto partialUpdatedMedioContacto = new MedioContacto();
        partialUpdatedMedioContacto.setId(medioContacto.getId());

        partialUpdatedMedioContacto
            .tipo(UPDATED_TIPO)
            .valor(UPDATED_VALOR)
            .codigoPais(UPDATED_CODIGO_PAIS)
            .anexo(UPDATED_ANEXO)
            .principal(UPDATED_PRINCIPAL)
            .verificado(UPDATED_VERIFICADO)
            .activo(UPDATED_ACTIVO)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restMedioContactoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMedioContacto.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMedioContacto))
            )
            .andExpect(status().isOk());

        // Validate the MedioContacto in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMedioContactoUpdatableFieldsEquals(partialUpdatedMedioContacto, getPersistedMedioContacto(partialUpdatedMedioContacto));
    }

    @Test
    @Transactional
    void patchNonExistingMedioContacto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medioContacto.setId(longCount.incrementAndGet());

        // Create the MedioContacto
        MedioContactoDTO medioContactoDTO = medioContactoMapper.toDto(medioContacto);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMedioContactoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, medioContactoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(medioContactoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MedioContacto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMedioContacto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medioContacto.setId(longCount.incrementAndGet());

        // Create the MedioContacto
        MedioContactoDTO medioContactoDTO = medioContactoMapper.toDto(medioContacto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedioContactoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(medioContactoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MedioContacto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMedioContacto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        medioContacto.setId(longCount.incrementAndGet());

        // Create the MedioContacto
        MedioContactoDTO medioContactoDTO = medioContactoMapper.toDto(medioContacto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedioContactoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(medioContactoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MedioContacto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMedioContacto() throws Exception {
        // Initialize the database
        insertedMedioContacto = medioContactoRepository.saveAndFlush(medioContacto);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the medioContacto
        restMedioContactoMockMvc
            .perform(delete(ENTITY_API_URL_ID, medioContacto.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return medioContactoRepository.count();
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

    protected MedioContacto getPersistedMedioContacto(MedioContacto medioContacto) {
        return medioContactoRepository.findById(medioContacto.getId()).orElseThrow();
    }

    protected void assertPersistedMedioContactoToMatchAllProperties(MedioContacto expectedMedioContacto) {
        assertMedioContactoAllPropertiesEquals(expectedMedioContacto, getPersistedMedioContacto(expectedMedioContacto));
    }

    protected void assertPersistedMedioContactoToMatchUpdatableProperties(MedioContacto expectedMedioContacto) {
        assertMedioContactoAllUpdatablePropertiesEquals(expectedMedioContacto, getPersistedMedioContacto(expectedMedioContacto));
    }
}
