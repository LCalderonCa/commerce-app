package pe.commerce.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pe.commerce.app.domain.DocumentoIdentidadAsserts.*;
import static pe.commerce.app.web.rest.TestUtil.createUpdateProxyForBean;

import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.LocalDate;
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
import pe.commerce.app.domain.DocumentoIdentidad;
import pe.commerce.app.domain.Persona;
import pe.commerce.app.domain.enumeration.TipoDocumento;
import pe.commerce.app.repository.DocumentoIdentidadRepository;
import pe.commerce.app.service.DocumentoIdentidadService;
import pe.commerce.app.service.dto.DocumentoIdentidadDTO;
import pe.commerce.app.service.mapper.DocumentoIdentidadMapper;
import tools.jackson.databind.ObjectMapper;

/**
 * Integration tests for the {@link DocumentoIdentidadResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class DocumentoIdentidadResourceIT {

    private static final TipoDocumento DEFAULT_TIPO_DOCUMENTO = TipoDocumento.DNI;
    private static final TipoDocumento UPDATED_TIPO_DOCUMENTO = TipoDocumento.CE;

    private static final String DEFAULT_NUMERO = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_FECHA_VENCIMIENTO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_VENCIMIENTO = LocalDate.parse("2023-12-10");

    private static final Boolean DEFAULT_PRINCIPAL = false;
    private static final Boolean UPDATED_PRINCIPAL = true;

    private static final Boolean DEFAULT_VIGENTE = false;
    private static final Boolean UPDATED_VIGENTE = true;

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.ofEpochMilli(1702246707543L);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.ofEpochMilli(1702246707543L);

    private static final String ENTITY_API_URL = "/api/documento-identidads";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DocumentoIdentidadRepository documentoIdentidadRepository;

    @Mock
    private DocumentoIdentidadRepository documentoIdentidadRepositoryMock;

    @Autowired
    private DocumentoIdentidadMapper documentoIdentidadMapper;

    @Mock
    private DocumentoIdentidadService documentoIdentidadServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDocumentoIdentidadMockMvc;

    private DocumentoIdentidad documentoIdentidad;

    private DocumentoIdentidad insertedDocumentoIdentidad;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocumentoIdentidad createEntity(EntityManager em) {
        DocumentoIdentidad documentoIdentidad = new DocumentoIdentidad()
            .tipoDocumento(DEFAULT_TIPO_DOCUMENTO)
            .numero(DEFAULT_NUMERO)
            .fechaVencimiento(DEFAULT_FECHA_VENCIMIENTO)
            .principal(DEFAULT_PRINCIPAL)
            .vigente(DEFAULT_VIGENTE)
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
        documentoIdentidad.setPersona(persona);
        return documentoIdentidad;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocumentoIdentidad createUpdatedEntity(EntityManager em) {
        DocumentoIdentidad updatedDocumentoIdentidad = new DocumentoIdentidad()
            .tipoDocumento(UPDATED_TIPO_DOCUMENTO)
            .numero(UPDATED_NUMERO)
            .fechaVencimiento(UPDATED_FECHA_VENCIMIENTO)
            .principal(UPDATED_PRINCIPAL)
            .vigente(UPDATED_VIGENTE)
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
        updatedDocumentoIdentidad.setPersona(persona);
        return updatedDocumentoIdentidad;
    }

    @BeforeEach
    void initTest() {
        documentoIdentidad = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedDocumentoIdentidad != null) {
            documentoIdentidadRepository.delete(insertedDocumentoIdentidad);
            insertedDocumentoIdentidad = null;
        }
    }

    @Test
    @Transactional
    void createDocumentoIdentidad() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the DocumentoIdentidad
        DocumentoIdentidadDTO documentoIdentidadDTO = documentoIdentidadMapper.toDto(documentoIdentidad);
        var returnedDocumentoIdentidadDTO = om.readValue(
            restDocumentoIdentidadMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentoIdentidadDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DocumentoIdentidadDTO.class
        );

        // Validate the DocumentoIdentidad in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedDocumentoIdentidad = documentoIdentidadMapper.toEntity(returnedDocumentoIdentidadDTO);
        assertDocumentoIdentidadUpdatableFieldsEquals(
            returnedDocumentoIdentidad,
            getPersistedDocumentoIdentidad(returnedDocumentoIdentidad)
        );

        insertedDocumentoIdentidad = returnedDocumentoIdentidad;
    }

    @Test
    @Transactional
    void createDocumentoIdentidadWithExistingId() throws Exception {
        // Create the DocumentoIdentidad with an existing ID
        documentoIdentidad.setId(1L);
        DocumentoIdentidadDTO documentoIdentidadDTO = documentoIdentidadMapper.toDto(documentoIdentidad);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDocumentoIdentidadMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentoIdentidadDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DocumentoIdentidad in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTipoDocumentoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        documentoIdentidad.setTipoDocumento(null);

        // Create the DocumentoIdentidad, which fails.
        DocumentoIdentidadDTO documentoIdentidadDTO = documentoIdentidadMapper.toDto(documentoIdentidad);

        restDocumentoIdentidadMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentoIdentidadDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNumeroIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        documentoIdentidad.setNumero(null);

        // Create the DocumentoIdentidad, which fails.
        DocumentoIdentidadDTO documentoIdentidadDTO = documentoIdentidadMapper.toDto(documentoIdentidad);

        restDocumentoIdentidadMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentoIdentidadDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPrincipalIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        documentoIdentidad.setPrincipal(null);

        // Create the DocumentoIdentidad, which fails.
        DocumentoIdentidadDTO documentoIdentidadDTO = documentoIdentidadMapper.toDto(documentoIdentidad);

        restDocumentoIdentidadMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentoIdentidadDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkVigenteIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        documentoIdentidad.setVigente(null);

        // Create the DocumentoIdentidad, which fails.
        DocumentoIdentidadDTO documentoIdentidadDTO = documentoIdentidadMapper.toDto(documentoIdentidad);

        restDocumentoIdentidadMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentoIdentidadDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDocumentoIdentidads() throws Exception {
        // Initialize the database
        insertedDocumentoIdentidad = documentoIdentidadRepository.saveAndFlush(documentoIdentidad);

        // Get all the documentoIdentidadList
        restDocumentoIdentidadMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documentoIdentidad.getId().intValue())))
            .andExpect(jsonPath("$.[*].tipoDocumento").value(hasItem(DEFAULT_TIPO_DOCUMENTO.toString())))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO)))
            .andExpect(jsonPath("$.[*].fechaVencimiento").value(hasItem(DEFAULT_FECHA_VENCIMIENTO.toString())))
            .andExpect(jsonPath("$.[*].principal").value(hasItem(DEFAULT_PRINCIPAL)))
            .andExpect(jsonPath("$.[*].vigente").value(hasItem(DEFAULT_VIGENTE)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDocumentoIdentidadsWithEagerRelationshipsIsEnabled() throws Exception {
        when(documentoIdentidadServiceMock.findAllWithEagerRelationships()).thenReturn(new ArrayList<>());

        restDocumentoIdentidadMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(documentoIdentidadServiceMock, times(1)).findAllWithEagerRelationships();
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDocumentoIdentidadsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(documentoIdentidadServiceMock.findAllWithEagerRelationships()).thenReturn(new ArrayList<>());

        restDocumentoIdentidadMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(documentoIdentidadRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getDocumentoIdentidad() throws Exception {
        // Initialize the database
        insertedDocumentoIdentidad = documentoIdentidadRepository.saveAndFlush(documentoIdentidad);

        // Get the documentoIdentidad
        restDocumentoIdentidadMockMvc
            .perform(get(ENTITY_API_URL_ID, documentoIdentidad.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(documentoIdentidad.getId().intValue()))
            .andExpect(jsonPath("$.tipoDocumento").value(DEFAULT_TIPO_DOCUMENTO.toString()))
            .andExpect(jsonPath("$.numero").value(DEFAULT_NUMERO))
            .andExpect(jsonPath("$.fechaVencimiento").value(DEFAULT_FECHA_VENCIMIENTO.toString()))
            .andExpect(jsonPath("$.principal").value(DEFAULT_PRINCIPAL))
            .andExpect(jsonPath("$.vigente").value(DEFAULT_VIGENTE))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingDocumentoIdentidad() throws Exception {
        // Get the documentoIdentidad
        restDocumentoIdentidadMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDocumentoIdentidad() throws Exception {
        // Initialize the database
        insertedDocumentoIdentidad = documentoIdentidadRepository.saveAndFlush(documentoIdentidad);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the documentoIdentidad
        DocumentoIdentidad updatedDocumentoIdentidad = documentoIdentidadRepository.findById(documentoIdentidad.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDocumentoIdentidad are not directly saved in db
        em.detach(updatedDocumentoIdentidad);
        updatedDocumentoIdentidad
            .tipoDocumento(UPDATED_TIPO_DOCUMENTO)
            .numero(UPDATED_NUMERO)
            .fechaVencimiento(UPDATED_FECHA_VENCIMIENTO)
            .principal(UPDATED_PRINCIPAL)
            .vigente(UPDATED_VIGENTE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        DocumentoIdentidadDTO documentoIdentidadDTO = documentoIdentidadMapper.toDto(updatedDocumentoIdentidad);

        restDocumentoIdentidadMockMvc
            .perform(
                put(ENTITY_API_URL_ID, documentoIdentidadDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(documentoIdentidadDTO))
            )
            .andExpect(status().isOk());

        // Validate the DocumentoIdentidad in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDocumentoIdentidadToMatchAllProperties(updatedDocumentoIdentidad);
    }

    @Test
    @Transactional
    void putNonExistingDocumentoIdentidad() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentoIdentidad.setId(longCount.incrementAndGet());

        // Create the DocumentoIdentidad
        DocumentoIdentidadDTO documentoIdentidadDTO = documentoIdentidadMapper.toDto(documentoIdentidad);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentoIdentidadMockMvc
            .perform(
                put(ENTITY_API_URL_ID, documentoIdentidadDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(documentoIdentidadDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentoIdentidad in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDocumentoIdentidad() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentoIdentidad.setId(longCount.incrementAndGet());

        // Create the DocumentoIdentidad
        DocumentoIdentidadDTO documentoIdentidadDTO = documentoIdentidadMapper.toDto(documentoIdentidad);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentoIdentidadMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(documentoIdentidadDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentoIdentidad in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDocumentoIdentidad() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentoIdentidad.setId(longCount.incrementAndGet());

        // Create the DocumentoIdentidad
        DocumentoIdentidadDTO documentoIdentidadDTO = documentoIdentidadMapper.toDto(documentoIdentidad);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentoIdentidadMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentoIdentidadDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocumentoIdentidad in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDocumentoIdentidadWithPatch() throws Exception {
        // Initialize the database
        insertedDocumentoIdentidad = documentoIdentidadRepository.saveAndFlush(documentoIdentidad);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the documentoIdentidad using partial update
        DocumentoIdentidad partialUpdatedDocumentoIdentidad = new DocumentoIdentidad();
        partialUpdatedDocumentoIdentidad.setId(documentoIdentidad.getId());

        partialUpdatedDocumentoIdentidad
            .tipoDocumento(UPDATED_TIPO_DOCUMENTO)
            .numero(UPDATED_NUMERO)
            .fechaVencimiento(UPDATED_FECHA_VENCIMIENTO)
            .vigente(UPDATED_VIGENTE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restDocumentoIdentidadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocumentoIdentidad.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDocumentoIdentidad))
            )
            .andExpect(status().isOk());

        // Validate the DocumentoIdentidad in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDocumentoIdentidadUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDocumentoIdentidad, documentoIdentidad),
            getPersistedDocumentoIdentidad(documentoIdentidad)
        );
    }

    @Test
    @Transactional
    void fullUpdateDocumentoIdentidadWithPatch() throws Exception {
        // Initialize the database
        insertedDocumentoIdentidad = documentoIdentidadRepository.saveAndFlush(documentoIdentidad);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the documentoIdentidad using partial update
        DocumentoIdentidad partialUpdatedDocumentoIdentidad = new DocumentoIdentidad();
        partialUpdatedDocumentoIdentidad.setId(documentoIdentidad.getId());

        partialUpdatedDocumentoIdentidad
            .tipoDocumento(UPDATED_TIPO_DOCUMENTO)
            .numero(UPDATED_NUMERO)
            .fechaVencimiento(UPDATED_FECHA_VENCIMIENTO)
            .principal(UPDATED_PRINCIPAL)
            .vigente(UPDATED_VIGENTE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restDocumentoIdentidadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocumentoIdentidad.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDocumentoIdentidad))
            )
            .andExpect(status().isOk());

        // Validate the DocumentoIdentidad in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDocumentoIdentidadUpdatableFieldsEquals(
            partialUpdatedDocumentoIdentidad,
            getPersistedDocumentoIdentidad(partialUpdatedDocumentoIdentidad)
        );
    }

    @Test
    @Transactional
    void patchNonExistingDocumentoIdentidad() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentoIdentidad.setId(longCount.incrementAndGet());

        // Create the DocumentoIdentidad
        DocumentoIdentidadDTO documentoIdentidadDTO = documentoIdentidadMapper.toDto(documentoIdentidad);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentoIdentidadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, documentoIdentidadDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(documentoIdentidadDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentoIdentidad in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDocumentoIdentidad() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentoIdentidad.setId(longCount.incrementAndGet());

        // Create the DocumentoIdentidad
        DocumentoIdentidadDTO documentoIdentidadDTO = documentoIdentidadMapper.toDto(documentoIdentidad);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentoIdentidadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(documentoIdentidadDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentoIdentidad in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDocumentoIdentidad() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentoIdentidad.setId(longCount.incrementAndGet());

        // Create the DocumentoIdentidad
        DocumentoIdentidadDTO documentoIdentidadDTO = documentoIdentidadMapper.toDto(documentoIdentidad);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentoIdentidadMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(documentoIdentidadDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocumentoIdentidad in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDocumentoIdentidad() throws Exception {
        // Initialize the database
        insertedDocumentoIdentidad = documentoIdentidadRepository.saveAndFlush(documentoIdentidad);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the documentoIdentidad
        restDocumentoIdentidadMockMvc
            .perform(delete(ENTITY_API_URL_ID, documentoIdentidad.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return documentoIdentidadRepository.count();
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

    protected DocumentoIdentidad getPersistedDocumentoIdentidad(DocumentoIdentidad documentoIdentidad) {
        return documentoIdentidadRepository.findById(documentoIdentidad.getId()).orElseThrow();
    }

    protected void assertPersistedDocumentoIdentidadToMatchAllProperties(DocumentoIdentidad expectedDocumentoIdentidad) {
        assertDocumentoIdentidadAllPropertiesEquals(expectedDocumentoIdentidad, getPersistedDocumentoIdentidad(expectedDocumentoIdentidad));
    }

    protected void assertPersistedDocumentoIdentidadToMatchUpdatableProperties(DocumentoIdentidad expectedDocumentoIdentidad) {
        assertDocumentoIdentidadAllUpdatablePropertiesEquals(
            expectedDocumentoIdentidad,
            getPersistedDocumentoIdentidad(expectedDocumentoIdentidad)
        );
    }
}
