package pe.commerce.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pe.commerce.app.domain.UbigeoAsserts.*;
import static pe.commerce.app.web.rest.TestUtil.createUpdateProxyForBean;

import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import pe.commerce.app.IntegrationTest;
import pe.commerce.app.domain.Ubigeo;
import pe.commerce.app.repository.UbigeoRepository;
import pe.commerce.app.service.dto.UbigeoDTO;
import pe.commerce.app.service.mapper.UbigeoMapper;
import tools.jackson.databind.ObjectMapper;

/**
 * Integration tests for the {@link UbigeoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UbigeoResourceIT {

    private static final String DEFAULT_CODIGO = "AAAAAA";
    private static final String UPDATED_CODIGO = "BBBBBB";

    private static final String DEFAULT_DEPARTAMENTO = "AAAAAAAAAA";
    private static final String UPDATED_DEPARTAMENTO = "BBBBBBBBBB";

    private static final String DEFAULT_PROVINCIA = "AAAAAAAAAA";
    private static final String UPDATED_PROVINCIA = "BBBBBBBBBB";

    private static final String DEFAULT_DISTRITO = "AAAAAAAAAA";
    private static final String UPDATED_DISTRITO = "BBBBBBBBBB";

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.ofEpochMilli(1702246707543L);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.ofEpochMilli(1702246707543L);

    private static final String ENTITY_API_URL = "/api/ubigeos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UbigeoRepository ubigeoRepository;

    @Autowired
    private UbigeoMapper ubigeoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUbigeoMockMvc;

    private Ubigeo ubigeo;

    private Ubigeo insertedUbigeo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ubigeo createEntity() {
        return new Ubigeo()
            .codigo(DEFAULT_CODIGO)
            .departamento(DEFAULT_DEPARTAMENTO)
            .provincia(DEFAULT_PROVINCIA)
            .distrito(DEFAULT_DISTRITO)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ubigeo createUpdatedEntity() {
        return new Ubigeo()
            .codigo(UPDATED_CODIGO)
            .departamento(UPDATED_DEPARTAMENTO)
            .provincia(UPDATED_PROVINCIA)
            .distrito(UPDATED_DISTRITO)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
    }

    @BeforeEach
    void initTest() {
        ubigeo = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedUbigeo != null) {
            ubigeoRepository.delete(insertedUbigeo);
            insertedUbigeo = null;
        }
    }

    @Test
    @Transactional
    void createUbigeo() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Ubigeo
        UbigeoDTO ubigeoDTO = ubigeoMapper.toDto(ubigeo);
        var returnedUbigeoDTO = om.readValue(
            restUbigeoMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ubigeoDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            UbigeoDTO.class
        );

        // Validate the Ubigeo in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedUbigeo = ubigeoMapper.toEntity(returnedUbigeoDTO);
        assertUbigeoUpdatableFieldsEquals(returnedUbigeo, getPersistedUbigeo(returnedUbigeo));

        insertedUbigeo = returnedUbigeo;
    }

    @Test
    @Transactional
    void createUbigeoWithExistingId() throws Exception {
        // Create the Ubigeo with an existing ID
        ubigeo.setId(1L);
        UbigeoDTO ubigeoDTO = ubigeoMapper.toDto(ubigeo);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUbigeoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ubigeoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Ubigeo in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodigoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        ubigeo.setCodigo(null);

        // Create the Ubigeo, which fails.
        UbigeoDTO ubigeoDTO = ubigeoMapper.toDto(ubigeo);

        restUbigeoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ubigeoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDepartamentoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        ubigeo.setDepartamento(null);

        // Create the Ubigeo, which fails.
        UbigeoDTO ubigeoDTO = ubigeoMapper.toDto(ubigeo);

        restUbigeoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ubigeoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkProvinciaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        ubigeo.setProvincia(null);

        // Create the Ubigeo, which fails.
        UbigeoDTO ubigeoDTO = ubigeoMapper.toDto(ubigeo);

        restUbigeoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ubigeoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDistritoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        ubigeo.setDistrito(null);

        // Create the Ubigeo, which fails.
        UbigeoDTO ubigeoDTO = ubigeoMapper.toDto(ubigeo);

        restUbigeoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ubigeoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUbigeos() throws Exception {
        // Initialize the database
        insertedUbigeo = ubigeoRepository.saveAndFlush(ubigeo);

        // Get all the ubigeoList
        restUbigeoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ubigeo.getId().intValue())))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO)))
            .andExpect(jsonPath("$.[*].departamento").value(hasItem(DEFAULT_DEPARTAMENTO)))
            .andExpect(jsonPath("$.[*].provincia").value(hasItem(DEFAULT_PROVINCIA)))
            .andExpect(jsonPath("$.[*].distrito").value(hasItem(DEFAULT_DISTRITO)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getUbigeo() throws Exception {
        // Initialize the database
        insertedUbigeo = ubigeoRepository.saveAndFlush(ubigeo);

        // Get the ubigeo
        restUbigeoMockMvc
            .perform(get(ENTITY_API_URL_ID, ubigeo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ubigeo.getId().intValue()))
            .andExpect(jsonPath("$.codigo").value(DEFAULT_CODIGO))
            .andExpect(jsonPath("$.departamento").value(DEFAULT_DEPARTAMENTO))
            .andExpect(jsonPath("$.provincia").value(DEFAULT_PROVINCIA))
            .andExpect(jsonPath("$.distrito").value(DEFAULT_DISTRITO))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingUbigeo() throws Exception {
        // Get the ubigeo
        restUbigeoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUbigeo() throws Exception {
        // Initialize the database
        insertedUbigeo = ubigeoRepository.saveAndFlush(ubigeo);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the ubigeo
        Ubigeo updatedUbigeo = ubigeoRepository.findById(ubigeo.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedUbigeo are not directly saved in db
        em.detach(updatedUbigeo);
        updatedUbigeo
            .codigo(UPDATED_CODIGO)
            .departamento(UPDATED_DEPARTAMENTO)
            .provincia(UPDATED_PROVINCIA)
            .distrito(UPDATED_DISTRITO)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        UbigeoDTO ubigeoDTO = ubigeoMapper.toDto(updatedUbigeo);

        restUbigeoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ubigeoDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ubigeoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Ubigeo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedUbigeoToMatchAllProperties(updatedUbigeo);
    }

    @Test
    @Transactional
    void putNonExistingUbigeo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ubigeo.setId(longCount.incrementAndGet());

        // Create the Ubigeo
        UbigeoDTO ubigeoDTO = ubigeoMapper.toDto(ubigeo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUbigeoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ubigeoDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ubigeoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ubigeo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUbigeo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ubigeo.setId(longCount.incrementAndGet());

        // Create the Ubigeo
        UbigeoDTO ubigeoDTO = ubigeoMapper.toDto(ubigeo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUbigeoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(ubigeoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ubigeo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUbigeo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ubigeo.setId(longCount.incrementAndGet());

        // Create the Ubigeo
        UbigeoDTO ubigeoDTO = ubigeoMapper.toDto(ubigeo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUbigeoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ubigeoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ubigeo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUbigeoWithPatch() throws Exception {
        // Initialize the database
        insertedUbigeo = ubigeoRepository.saveAndFlush(ubigeo);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the ubigeo using partial update
        Ubigeo partialUpdatedUbigeo = new Ubigeo();
        partialUpdatedUbigeo.setId(ubigeo.getId());

        partialUpdatedUbigeo
            .departamento(UPDATED_DEPARTAMENTO)
            .provincia(UPDATED_PROVINCIA)
            .distrito(UPDATED_DISTRITO)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);

        restUbigeoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUbigeo.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUbigeo))
            )
            .andExpect(status().isOk());

        // Validate the Ubigeo in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUbigeoUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedUbigeo, ubigeo), getPersistedUbigeo(ubigeo));
    }

    @Test
    @Transactional
    void fullUpdateUbigeoWithPatch() throws Exception {
        // Initialize the database
        insertedUbigeo = ubigeoRepository.saveAndFlush(ubigeo);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the ubigeo using partial update
        Ubigeo partialUpdatedUbigeo = new Ubigeo();
        partialUpdatedUbigeo.setId(ubigeo.getId());

        partialUpdatedUbigeo
            .codigo(UPDATED_CODIGO)
            .departamento(UPDATED_DEPARTAMENTO)
            .provincia(UPDATED_PROVINCIA)
            .distrito(UPDATED_DISTRITO)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restUbigeoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUbigeo.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUbigeo))
            )
            .andExpect(status().isOk());

        // Validate the Ubigeo in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUbigeoUpdatableFieldsEquals(partialUpdatedUbigeo, getPersistedUbigeo(partialUpdatedUbigeo));
    }

    @Test
    @Transactional
    void patchNonExistingUbigeo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ubigeo.setId(longCount.incrementAndGet());

        // Create the Ubigeo
        UbigeoDTO ubigeoDTO = ubigeoMapper.toDto(ubigeo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUbigeoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ubigeoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(ubigeoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ubigeo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUbigeo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ubigeo.setId(longCount.incrementAndGet());

        // Create the Ubigeo
        UbigeoDTO ubigeoDTO = ubigeoMapper.toDto(ubigeo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUbigeoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(ubigeoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ubigeo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUbigeo() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ubigeo.setId(longCount.incrementAndGet());

        // Create the Ubigeo
        UbigeoDTO ubigeoDTO = ubigeoMapper.toDto(ubigeo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUbigeoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(ubigeoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ubigeo in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUbigeo() throws Exception {
        // Initialize the database
        insertedUbigeo = ubigeoRepository.saveAndFlush(ubigeo);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the ubigeo
        restUbigeoMockMvc
            .perform(delete(ENTITY_API_URL_ID, ubigeo.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return ubigeoRepository.count();
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

    protected Ubigeo getPersistedUbigeo(Ubigeo ubigeo) {
        return ubigeoRepository.findById(ubigeo.getId()).orElseThrow();
    }

    protected void assertPersistedUbigeoToMatchAllProperties(Ubigeo expectedUbigeo) {
        assertUbigeoAllPropertiesEquals(expectedUbigeo, getPersistedUbigeo(expectedUbigeo));
    }

    protected void assertPersistedUbigeoToMatchUpdatableProperties(Ubigeo expectedUbigeo) {
        assertUbigeoAllUpdatablePropertiesEquals(expectedUbigeo, getPersistedUbigeo(expectedUbigeo));
    }
}
