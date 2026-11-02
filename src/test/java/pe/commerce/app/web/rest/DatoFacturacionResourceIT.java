package pe.commerce.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pe.commerce.app.domain.DatoFacturacionAsserts.*;
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
import pe.commerce.app.domain.DatoFacturacion;
import pe.commerce.app.repository.DatoFacturacionRepository;
import pe.commerce.app.service.DatoFacturacionService;
import pe.commerce.app.service.dto.DatoFacturacionDTO;
import pe.commerce.app.service.mapper.DatoFacturacionMapper;
import tools.jackson.databind.ObjectMapper;

/**
 * Integration tests for the {@link DatoFacturacionResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class DatoFacturacionResourceIT {

    private static final String DEFAULT_RUC = "10053563203";
    private static final String UPDATED_RUC = "10400689421";

    private static final String DEFAULT_RAZON_SOCIAL = "AAAAAAAAAA";
    private static final String UPDATED_RAZON_SOCIAL = "BBBBBBBBBB";

    private static final String DEFAULT_DIRECCION_FISCAL = "AAAAAAAAAA";
    private static final String UPDATED_DIRECCION_FISCAL = "BBBBBBBBBB";

    private static final Boolean DEFAULT_PREDETERMINADO = false;
    private static final Boolean UPDATED_PREDETERMINADO = true;

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

    private static final String ENTITY_API_URL = "/api/dato-facturacions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DatoFacturacionRepository datoFacturacionRepository;

    @Mock
    private DatoFacturacionRepository datoFacturacionRepositoryMock;

    @Autowired
    private DatoFacturacionMapper datoFacturacionMapper;

    @Mock
    private DatoFacturacionService datoFacturacionServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDatoFacturacionMockMvc;

    private DatoFacturacion datoFacturacion;

    private DatoFacturacion insertedDatoFacturacion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DatoFacturacion createEntity(EntityManager em) {
        DatoFacturacion datoFacturacion = new DatoFacturacion()
            .ruc(DEFAULT_RUC)
            .razonSocial(DEFAULT_RAZON_SOCIAL)
            .direccionFiscal(DEFAULT_DIRECCION_FISCAL)
            .predeterminado(DEFAULT_PREDETERMINADO)
            .activo(DEFAULT_ACTIVO)
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
        datoFacturacion.setCliente(cliente);
        return datoFacturacion;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DatoFacturacion createUpdatedEntity(EntityManager em) {
        DatoFacturacion updatedDatoFacturacion = new DatoFacturacion()
            .ruc(UPDATED_RUC)
            .razonSocial(UPDATED_RAZON_SOCIAL)
            .direccionFiscal(UPDATED_DIRECCION_FISCAL)
            .predeterminado(UPDATED_PREDETERMINADO)
            .activo(UPDATED_ACTIVO)
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
        updatedDatoFacturacion.setCliente(cliente);
        return updatedDatoFacturacion;
    }

    @BeforeEach
    void initTest() {
        datoFacturacion = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedDatoFacturacion != null) {
            datoFacturacionRepository.delete(insertedDatoFacturacion);
            insertedDatoFacturacion = null;
        }
    }

    @Test
    @Transactional
    void createDatoFacturacion() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the DatoFacturacion
        DatoFacturacionDTO datoFacturacionDTO = datoFacturacionMapper.toDto(datoFacturacion);
        var returnedDatoFacturacionDTO = om.readValue(
            restDatoFacturacionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(datoFacturacionDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DatoFacturacionDTO.class
        );

        // Validate the DatoFacturacion in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedDatoFacturacion = datoFacturacionMapper.toEntity(returnedDatoFacturacionDTO);
        assertDatoFacturacionUpdatableFieldsEquals(returnedDatoFacturacion, getPersistedDatoFacturacion(returnedDatoFacturacion));

        insertedDatoFacturacion = returnedDatoFacturacion;
    }

    @Test
    @Transactional
    void createDatoFacturacionWithExistingId() throws Exception {
        // Create the DatoFacturacion with an existing ID
        datoFacturacion.setId(1L);
        DatoFacturacionDTO datoFacturacionDTO = datoFacturacionMapper.toDto(datoFacturacion);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDatoFacturacionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(datoFacturacionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DatoFacturacion in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkRucIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        datoFacturacion.setRuc(null);

        // Create the DatoFacturacion, which fails.
        DatoFacturacionDTO datoFacturacionDTO = datoFacturacionMapper.toDto(datoFacturacion);

        restDatoFacturacionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(datoFacturacionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRazonSocialIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        datoFacturacion.setRazonSocial(null);

        // Create the DatoFacturacion, which fails.
        DatoFacturacionDTO datoFacturacionDTO = datoFacturacionMapper.toDto(datoFacturacion);

        restDatoFacturacionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(datoFacturacionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDireccionFiscalIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        datoFacturacion.setDireccionFiscal(null);

        // Create the DatoFacturacion, which fails.
        DatoFacturacionDTO datoFacturacionDTO = datoFacturacionMapper.toDto(datoFacturacion);

        restDatoFacturacionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(datoFacturacionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPredeterminadoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        datoFacturacion.setPredeterminado(null);

        // Create the DatoFacturacion, which fails.
        DatoFacturacionDTO datoFacturacionDTO = datoFacturacionMapper.toDto(datoFacturacion);

        restDatoFacturacionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(datoFacturacionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActivoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        datoFacturacion.setActivo(null);

        // Create the DatoFacturacion, which fails.
        DatoFacturacionDTO datoFacturacionDTO = datoFacturacionMapper.toDto(datoFacturacion);

        restDatoFacturacionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(datoFacturacionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDatoFacturacions() throws Exception {
        // Initialize the database
        insertedDatoFacturacion = datoFacturacionRepository.saveAndFlush(datoFacturacion);

        // Get all the datoFacturacionList
        restDatoFacturacionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(datoFacturacion.getId().intValue())))
            .andExpect(jsonPath("$.[*].ruc").value(hasItem(DEFAULT_RUC)))
            .andExpect(jsonPath("$.[*].razonSocial").value(hasItem(DEFAULT_RAZON_SOCIAL)))
            .andExpect(jsonPath("$.[*].direccionFiscal").value(hasItem(DEFAULT_DIRECCION_FISCAL)))
            .andExpect(jsonPath("$.[*].predeterminado").value(hasItem(DEFAULT_PREDETERMINADO)))
            .andExpect(jsonPath("$.[*].activo").value(hasItem(DEFAULT_ACTIVO)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDatoFacturacionsWithEagerRelationshipsIsEnabled() throws Exception {
        when(datoFacturacionServiceMock.findAllWithEagerRelationships()).thenReturn(new ArrayList<>());

        restDatoFacturacionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(datoFacturacionServiceMock, times(1)).findAllWithEagerRelationships();
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDatoFacturacionsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(datoFacturacionServiceMock.findAllWithEagerRelationships()).thenReturn(new ArrayList<>());

        restDatoFacturacionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(datoFacturacionRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getDatoFacturacion() throws Exception {
        // Initialize the database
        insertedDatoFacturacion = datoFacturacionRepository.saveAndFlush(datoFacturacion);

        // Get the datoFacturacion
        restDatoFacturacionMockMvc
            .perform(get(ENTITY_API_URL_ID, datoFacturacion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(datoFacturacion.getId().intValue()))
            .andExpect(jsonPath("$.ruc").value(DEFAULT_RUC))
            .andExpect(jsonPath("$.razonSocial").value(DEFAULT_RAZON_SOCIAL))
            .andExpect(jsonPath("$.direccionFiscal").value(DEFAULT_DIRECCION_FISCAL))
            .andExpect(jsonPath("$.predeterminado").value(DEFAULT_PREDETERMINADO))
            .andExpect(jsonPath("$.activo").value(DEFAULT_ACTIVO))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingDatoFacturacion() throws Exception {
        // Get the datoFacturacion
        restDatoFacturacionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDatoFacturacion() throws Exception {
        // Initialize the database
        insertedDatoFacturacion = datoFacturacionRepository.saveAndFlush(datoFacturacion);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the datoFacturacion
        DatoFacturacion updatedDatoFacturacion = datoFacturacionRepository.findById(datoFacturacion.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDatoFacturacion are not directly saved in db
        em.detach(updatedDatoFacturacion);
        updatedDatoFacturacion
            .ruc(UPDATED_RUC)
            .razonSocial(UPDATED_RAZON_SOCIAL)
            .direccionFiscal(UPDATED_DIRECCION_FISCAL)
            .predeterminado(UPDATED_PREDETERMINADO)
            .activo(UPDATED_ACTIVO)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        DatoFacturacionDTO datoFacturacionDTO = datoFacturacionMapper.toDto(updatedDatoFacturacion);

        restDatoFacturacionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, datoFacturacionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(datoFacturacionDTO))
            )
            .andExpect(status().isOk());

        // Validate the DatoFacturacion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDatoFacturacionToMatchAllProperties(updatedDatoFacturacion);
    }

    @Test
    @Transactional
    void putNonExistingDatoFacturacion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        datoFacturacion.setId(longCount.incrementAndGet());

        // Create the DatoFacturacion
        DatoFacturacionDTO datoFacturacionDTO = datoFacturacionMapper.toDto(datoFacturacion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDatoFacturacionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, datoFacturacionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(datoFacturacionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DatoFacturacion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDatoFacturacion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        datoFacturacion.setId(longCount.incrementAndGet());

        // Create the DatoFacturacion
        DatoFacturacionDTO datoFacturacionDTO = datoFacturacionMapper.toDto(datoFacturacion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDatoFacturacionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(datoFacturacionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DatoFacturacion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDatoFacturacion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        datoFacturacion.setId(longCount.incrementAndGet());

        // Create the DatoFacturacion
        DatoFacturacionDTO datoFacturacionDTO = datoFacturacionMapper.toDto(datoFacturacion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDatoFacturacionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(datoFacturacionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DatoFacturacion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDatoFacturacionWithPatch() throws Exception {
        // Initialize the database
        insertedDatoFacturacion = datoFacturacionRepository.saveAndFlush(datoFacturacion);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the datoFacturacion using partial update
        DatoFacturacion partialUpdatedDatoFacturacion = new DatoFacturacion();
        partialUpdatedDatoFacturacion.setId(datoFacturacion.getId());

        partialUpdatedDatoFacturacion
            .direccionFiscal(UPDATED_DIRECCION_FISCAL)
            .activo(UPDATED_ACTIVO)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restDatoFacturacionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDatoFacturacion.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDatoFacturacion))
            )
            .andExpect(status().isOk());

        // Validate the DatoFacturacion in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDatoFacturacionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDatoFacturacion, datoFacturacion),
            getPersistedDatoFacturacion(datoFacturacion)
        );
    }

    @Test
    @Transactional
    void fullUpdateDatoFacturacionWithPatch() throws Exception {
        // Initialize the database
        insertedDatoFacturacion = datoFacturacionRepository.saveAndFlush(datoFacturacion);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the datoFacturacion using partial update
        DatoFacturacion partialUpdatedDatoFacturacion = new DatoFacturacion();
        partialUpdatedDatoFacturacion.setId(datoFacturacion.getId());

        partialUpdatedDatoFacturacion
            .ruc(UPDATED_RUC)
            .razonSocial(UPDATED_RAZON_SOCIAL)
            .direccionFiscal(UPDATED_DIRECCION_FISCAL)
            .predeterminado(UPDATED_PREDETERMINADO)
            .activo(UPDATED_ACTIVO)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restDatoFacturacionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDatoFacturacion.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDatoFacturacion))
            )
            .andExpect(status().isOk());

        // Validate the DatoFacturacion in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDatoFacturacionUpdatableFieldsEquals(
            partialUpdatedDatoFacturacion,
            getPersistedDatoFacturacion(partialUpdatedDatoFacturacion)
        );
    }

    @Test
    @Transactional
    void patchNonExistingDatoFacturacion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        datoFacturacion.setId(longCount.incrementAndGet());

        // Create the DatoFacturacion
        DatoFacturacionDTO datoFacturacionDTO = datoFacturacionMapper.toDto(datoFacturacion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDatoFacturacionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, datoFacturacionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(datoFacturacionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DatoFacturacion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDatoFacturacion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        datoFacturacion.setId(longCount.incrementAndGet());

        // Create the DatoFacturacion
        DatoFacturacionDTO datoFacturacionDTO = datoFacturacionMapper.toDto(datoFacturacion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDatoFacturacionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(datoFacturacionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DatoFacturacion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDatoFacturacion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        datoFacturacion.setId(longCount.incrementAndGet());

        // Create the DatoFacturacion
        DatoFacturacionDTO datoFacturacionDTO = datoFacturacionMapper.toDto(datoFacturacion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDatoFacturacionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(datoFacturacionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DatoFacturacion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDatoFacturacion() throws Exception {
        // Initialize the database
        insertedDatoFacturacion = datoFacturacionRepository.saveAndFlush(datoFacturacion);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the datoFacturacion
        restDatoFacturacionMockMvc
            .perform(delete(ENTITY_API_URL_ID, datoFacturacion.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return datoFacturacionRepository.count();
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

    protected DatoFacturacion getPersistedDatoFacturacion(DatoFacturacion datoFacturacion) {
        return datoFacturacionRepository.findById(datoFacturacion.getId()).orElseThrow();
    }

    protected void assertPersistedDatoFacturacionToMatchAllProperties(DatoFacturacion expectedDatoFacturacion) {
        assertDatoFacturacionAllPropertiesEquals(expectedDatoFacturacion, getPersistedDatoFacturacion(expectedDatoFacturacion));
    }

    protected void assertPersistedDatoFacturacionToMatchUpdatableProperties(DatoFacturacion expectedDatoFacturacion) {
        assertDatoFacturacionAllUpdatablePropertiesEquals(expectedDatoFacturacion, getPersistedDatoFacturacion(expectedDatoFacturacion));
    }
}
