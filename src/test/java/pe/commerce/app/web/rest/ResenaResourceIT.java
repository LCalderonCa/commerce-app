package pe.commerce.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pe.commerce.app.domain.ResenaAsserts.*;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import pe.commerce.app.IntegrationTest;
import pe.commerce.app.domain.Cliente;
import pe.commerce.app.domain.Producto;
import pe.commerce.app.domain.Resena;
import pe.commerce.app.repository.ResenaRepository;
import pe.commerce.app.service.ResenaService;
import pe.commerce.app.service.dto.ResenaDTO;
import pe.commerce.app.service.mapper.ResenaMapper;
import tools.jackson.databind.ObjectMapper;

/**
 * Integration tests for the {@link ResenaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ResenaResourceIT {

    private static final Integer DEFAULT_CALIFICACION = 1;
    private static final Integer UPDATED_CALIFICACION = 2;

    private static final String DEFAULT_COMENTARIO = "AAAAAAAAAA";
    private static final String UPDATED_COMENTARIO = "BBBBBBBBBB";

    private static final Boolean DEFAULT_COMPRA_VERIFICADA = false;
    private static final Boolean UPDATED_COMPRA_VERIFICADA = true;

    private static final Boolean DEFAULT_APROBADA = false;
    private static final Boolean UPDATED_APROBADA = true;

    private static final Instant DEFAULT_FECHA = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA = Instant.ofEpochMilli(1702246707543L);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.ofEpochMilli(1702246707543L);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.ofEpochMilli(1702246707543L);

    private static final String ENTITY_API_URL = "/api/resenas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ResenaRepository resenaRepository;

    @Mock
    private ResenaRepository resenaRepositoryMock;

    @Autowired
    private ResenaMapper resenaMapper;

    @Mock
    private ResenaService resenaServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restResenaMockMvc;

    private Resena resena;

    private Resena insertedResena;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Resena createEntity(EntityManager em) {
        Resena resena = new Resena()
            .calificacion(DEFAULT_CALIFICACION)
            .comentario(DEFAULT_COMENTARIO)
            .compraVerificada(DEFAULT_COMPRA_VERIFICADA)
            .aprobada(DEFAULT_APROBADA)
            .fecha(DEFAULT_FECHA)
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
        resena.setCliente(cliente);
        // Add required entity
        Producto producto;
        if (TestUtil.findAll(em, Producto.class).isEmpty()) {
            producto = ProductoResourceIT.createEntity(em);
            em.persist(producto);
            em.flush();
        } else {
            producto = TestUtil.findAll(em, Producto.class).getFirst();
        }
        resena.setProducto(producto);
        return resena;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Resena createUpdatedEntity(EntityManager em) {
        Resena updatedResena = new Resena()
            .calificacion(UPDATED_CALIFICACION)
            .comentario(UPDATED_COMENTARIO)
            .compraVerificada(UPDATED_COMPRA_VERIFICADA)
            .aprobada(UPDATED_APROBADA)
            .fecha(UPDATED_FECHA)
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
        updatedResena.setCliente(cliente);
        // Add required entity
        Producto producto;
        if (TestUtil.findAll(em, Producto.class).isEmpty()) {
            producto = ProductoResourceIT.createUpdatedEntity(em);
            em.persist(producto);
            em.flush();
        } else {
            producto = TestUtil.findAll(em, Producto.class).getFirst();
        }
        updatedResena.setProducto(producto);
        return updatedResena;
    }

    @BeforeEach
    void initTest() {
        resena = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedResena != null) {
            resenaRepository.delete(insertedResena);
            insertedResena = null;
        }
    }

    @Test
    @Transactional
    void createResena() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Resena
        ResenaDTO resenaDTO = resenaMapper.toDto(resena);
        var returnedResenaDTO = om.readValue(
            restResenaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(resenaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ResenaDTO.class
        );

        // Validate the Resena in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedResena = resenaMapper.toEntity(returnedResenaDTO);
        assertResenaUpdatableFieldsEquals(returnedResena, getPersistedResena(returnedResena));

        insertedResena = returnedResena;
    }

    @Test
    @Transactional
    void createResenaWithExistingId() throws Exception {
        // Create the Resena with an existing ID
        resena.setId(1L);
        ResenaDTO resenaDTO = resenaMapper.toDto(resena);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restResenaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(resenaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Resena in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCalificacionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        resena.setCalificacion(null);

        // Create the Resena, which fails.
        ResenaDTO resenaDTO = resenaMapper.toDto(resena);

        restResenaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(resenaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCompraVerificadaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        resena.setCompraVerificada(null);

        // Create the Resena, which fails.
        ResenaDTO resenaDTO = resenaMapper.toDto(resena);

        restResenaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(resenaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAprobadaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        resena.setAprobada(null);

        // Create the Resena, which fails.
        ResenaDTO resenaDTO = resenaMapper.toDto(resena);

        restResenaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(resenaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFechaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        resena.setFecha(null);

        // Create the Resena, which fails.
        ResenaDTO resenaDTO = resenaMapper.toDto(resena);

        restResenaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(resenaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllResenas() throws Exception {
        // Initialize the database
        insertedResena = resenaRepository.saveAndFlush(resena);

        // Get all the resenaList
        restResenaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(resena.getId().intValue())))
            .andExpect(jsonPath("$.[*].calificacion").value(hasItem(DEFAULT_CALIFICACION)))
            .andExpect(jsonPath("$.[*].comentario").value(hasItem(DEFAULT_COMENTARIO)))
            .andExpect(jsonPath("$.[*].compraVerificada").value(hasItem(DEFAULT_COMPRA_VERIFICADA)))
            .andExpect(jsonPath("$.[*].aprobada").value(hasItem(DEFAULT_APROBADA)))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllResenasWithEagerRelationshipsIsEnabled() throws Exception {
        when(resenaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restResenaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(resenaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllResenasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(resenaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restResenaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(resenaRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getResena() throws Exception {
        // Initialize the database
        insertedResena = resenaRepository.saveAndFlush(resena);

        // Get the resena
        restResenaMockMvc
            .perform(get(ENTITY_API_URL_ID, resena.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(resena.getId().intValue()))
            .andExpect(jsonPath("$.calificacion").value(DEFAULT_CALIFICACION))
            .andExpect(jsonPath("$.comentario").value(DEFAULT_COMENTARIO))
            .andExpect(jsonPath("$.compraVerificada").value(DEFAULT_COMPRA_VERIFICADA))
            .andExpect(jsonPath("$.aprobada").value(DEFAULT_APROBADA))
            .andExpect(jsonPath("$.fecha").value(DEFAULT_FECHA.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingResena() throws Exception {
        // Get the resena
        restResenaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingResena() throws Exception {
        // Initialize the database
        insertedResena = resenaRepository.saveAndFlush(resena);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the resena
        Resena updatedResena = resenaRepository.findById(resena.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedResena are not directly saved in db
        em.detach(updatedResena);
        updatedResena
            .calificacion(UPDATED_CALIFICACION)
            .comentario(UPDATED_COMENTARIO)
            .compraVerificada(UPDATED_COMPRA_VERIFICADA)
            .aprobada(UPDATED_APROBADA)
            .fecha(UPDATED_FECHA)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        ResenaDTO resenaDTO = resenaMapper.toDto(updatedResena);

        restResenaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, resenaDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(resenaDTO))
            )
            .andExpect(status().isOk());

        // Validate the Resena in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedResenaToMatchAllProperties(updatedResena);
    }

    @Test
    @Transactional
    void putNonExistingResena() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        resena.setId(longCount.incrementAndGet());

        // Create the Resena
        ResenaDTO resenaDTO = resenaMapper.toDto(resena);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restResenaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, resenaDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(resenaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Resena in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchResena() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        resena.setId(longCount.incrementAndGet());

        // Create the Resena
        ResenaDTO resenaDTO = resenaMapper.toDto(resena);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResenaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(resenaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Resena in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamResena() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        resena.setId(longCount.incrementAndGet());

        // Create the Resena
        ResenaDTO resenaDTO = resenaMapper.toDto(resena);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResenaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(resenaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Resena in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateResenaWithPatch() throws Exception {
        // Initialize the database
        insertedResena = resenaRepository.saveAndFlush(resena);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the resena using partial update
        Resena partialUpdatedResena = new Resena();
        partialUpdatedResena.setId(resena.getId());

        partialUpdatedResena.calificacion(UPDATED_CALIFICACION).fecha(UPDATED_FECHA).lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restResenaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedResena.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedResena))
            )
            .andExpect(status().isOk());

        // Validate the Resena in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertResenaUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedResena, resena), getPersistedResena(resena));
    }

    @Test
    @Transactional
    void fullUpdateResenaWithPatch() throws Exception {
        // Initialize the database
        insertedResena = resenaRepository.saveAndFlush(resena);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the resena using partial update
        Resena partialUpdatedResena = new Resena();
        partialUpdatedResena.setId(resena.getId());

        partialUpdatedResena
            .calificacion(UPDATED_CALIFICACION)
            .comentario(UPDATED_COMENTARIO)
            .compraVerificada(UPDATED_COMPRA_VERIFICADA)
            .aprobada(UPDATED_APROBADA)
            .fecha(UPDATED_FECHA)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restResenaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedResena.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedResena))
            )
            .andExpect(status().isOk());

        // Validate the Resena in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertResenaUpdatableFieldsEquals(partialUpdatedResena, getPersistedResena(partialUpdatedResena));
    }

    @Test
    @Transactional
    void patchNonExistingResena() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        resena.setId(longCount.incrementAndGet());

        // Create the Resena
        ResenaDTO resenaDTO = resenaMapper.toDto(resena);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restResenaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, resenaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(resenaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Resena in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchResena() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        resena.setId(longCount.incrementAndGet());

        // Create the Resena
        ResenaDTO resenaDTO = resenaMapper.toDto(resena);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResenaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(resenaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Resena in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamResena() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        resena.setId(longCount.incrementAndGet());

        // Create the Resena
        ResenaDTO resenaDTO = resenaMapper.toDto(resena);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResenaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(resenaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Resena in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteResena() throws Exception {
        // Initialize the database
        insertedResena = resenaRepository.saveAndFlush(resena);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the resena
        restResenaMockMvc
            .perform(delete(ENTITY_API_URL_ID, resena.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return resenaRepository.count();
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

    protected Resena getPersistedResena(Resena resena) {
        return resenaRepository.findById(resena.getId()).orElseThrow();
    }

    protected void assertPersistedResenaToMatchAllProperties(Resena expectedResena) {
        assertResenaAllPropertiesEquals(expectedResena, getPersistedResena(expectedResena));
    }

    protected void assertPersistedResenaToMatchUpdatableProperties(Resena expectedResena) {
        assertResenaAllUpdatablePropertiesEquals(expectedResena, getPersistedResena(expectedResena));
    }
}
