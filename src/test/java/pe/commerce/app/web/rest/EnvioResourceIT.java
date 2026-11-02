package pe.commerce.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pe.commerce.app.domain.EnvioAsserts.*;
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
import pe.commerce.app.domain.Envio;
import pe.commerce.app.domain.Pedido;
import pe.commerce.app.domain.enumeration.EstadoEnvio;
import pe.commerce.app.repository.EnvioRepository;
import pe.commerce.app.service.EnvioService;
import pe.commerce.app.service.dto.EnvioDTO;
import pe.commerce.app.service.mapper.EnvioMapper;
import tools.jackson.databind.ObjectMapper;

/**
 * Integration tests for the {@link EnvioResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class EnvioResourceIT {

    private static final String DEFAULT_COURIER = "AAAAAAAAAA";
    private static final String UPDATED_COURIER = "BBBBBBBBBB";

    private static final String DEFAULT_TRACKING = "AAAAAAAAAA";
    private static final String UPDATED_TRACKING = "BBBBBBBBBB";

    private static final EstadoEnvio DEFAULT_ESTADO = EstadoEnvio.PENDIENTE;
    private static final EstadoEnvio UPDATED_ESTADO = EstadoEnvio.EN_RUTA;

    private static final String DEFAULT_DIRECCION_ENTREGA = "AAAAAAAAAA";
    private static final String UPDATED_DIRECCION_ENTREGA = "BBBBBBBBBB";

    private static final String DEFAULT_UBIGEO_ENTREGA = "AAAAAA";
    private static final String UPDATED_UBIGEO_ENTREGA = "BBBBBB";

    private static final String DEFAULT_REFERENCIA_ENTREGA = "AAAAAAAAAA";
    private static final String UPDATED_REFERENCIA_ENTREGA = "BBBBBBBBBB";

    private static final String DEFAULT_NOMBRE_RECEPTOR = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE_RECEPTOR = "BBBBBBBBBB";

    private static final String DEFAULT_TELEFONO_RECEPTOR = "AAAAAAAAAA";
    private static final String UPDATED_TELEFONO_RECEPTOR = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_FECHA_ESTIMADA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_ESTIMADA = LocalDate.parse("2023-12-10");

    private static final Instant DEFAULT_FECHA_ENTREGA = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_ENTREGA = Instant.ofEpochMilli(1702246707543L);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.ofEpochMilli(1702246707543L);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.ofEpochMilli(1702246707543L);

    private static final String ENTITY_API_URL = "/api/envios";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EnvioRepository envioRepository;

    @Mock
    private EnvioRepository envioRepositoryMock;

    @Autowired
    private EnvioMapper envioMapper;

    @Mock
    private EnvioService envioServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEnvioMockMvc;

    private Envio envio;

    private Envio insertedEnvio;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Envio createEntity(EntityManager em) {
        Envio envio = new Envio()
            .courier(DEFAULT_COURIER)
            .tracking(DEFAULT_TRACKING)
            .estado(DEFAULT_ESTADO)
            .direccionEntrega(DEFAULT_DIRECCION_ENTREGA)
            .ubigeoEntrega(DEFAULT_UBIGEO_ENTREGA)
            .referenciaEntrega(DEFAULT_REFERENCIA_ENTREGA)
            .nombreReceptor(DEFAULT_NOMBRE_RECEPTOR)
            .telefonoReceptor(DEFAULT_TELEFONO_RECEPTOR)
            .fechaEstimada(DEFAULT_FECHA_ESTIMADA)
            .fechaEntrega(DEFAULT_FECHA_ENTREGA)
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
        envio.setPedido(pedido);
        return envio;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Envio createUpdatedEntity(EntityManager em) {
        Envio updatedEnvio = new Envio()
            .courier(UPDATED_COURIER)
            .tracking(UPDATED_TRACKING)
            .estado(UPDATED_ESTADO)
            .direccionEntrega(UPDATED_DIRECCION_ENTREGA)
            .ubigeoEntrega(UPDATED_UBIGEO_ENTREGA)
            .referenciaEntrega(UPDATED_REFERENCIA_ENTREGA)
            .nombreReceptor(UPDATED_NOMBRE_RECEPTOR)
            .telefonoReceptor(UPDATED_TELEFONO_RECEPTOR)
            .fechaEstimada(UPDATED_FECHA_ESTIMADA)
            .fechaEntrega(UPDATED_FECHA_ENTREGA)
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
        updatedEnvio.setPedido(pedido);
        return updatedEnvio;
    }

    @BeforeEach
    void initTest() {
        envio = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedEnvio != null) {
            envioRepository.delete(insertedEnvio);
            insertedEnvio = null;
        }
    }

    @Test
    @Transactional
    void createEnvio() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Envio
        EnvioDTO envioDTO = envioMapper.toDto(envio);
        var returnedEnvioDTO = om.readValue(
            restEnvioMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(envioDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            EnvioDTO.class
        );

        // Validate the Envio in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedEnvio = envioMapper.toEntity(returnedEnvioDTO);
        assertEnvioUpdatableFieldsEquals(returnedEnvio, getPersistedEnvio(returnedEnvio));

        insertedEnvio = returnedEnvio;
    }

    @Test
    @Transactional
    void createEnvioWithExistingId() throws Exception {
        // Create the Envio with an existing ID
        envio.setId(1L);
        EnvioDTO envioDTO = envioMapper.toDto(envio);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEnvioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(envioDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Envio in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkEstadoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        envio.setEstado(null);

        // Create the Envio, which fails.
        EnvioDTO envioDTO = envioMapper.toDto(envio);

        restEnvioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(envioDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDireccionEntregaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        envio.setDireccionEntrega(null);

        // Create the Envio, which fails.
        EnvioDTO envioDTO = envioMapper.toDto(envio);

        restEnvioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(envioDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUbigeoEntregaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        envio.setUbigeoEntrega(null);

        // Create the Envio, which fails.
        EnvioDTO envioDTO = envioMapper.toDto(envio);

        restEnvioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(envioDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNombreReceptorIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        envio.setNombreReceptor(null);

        // Create the Envio, which fails.
        EnvioDTO envioDTO = envioMapper.toDto(envio);

        restEnvioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(envioDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTelefonoReceptorIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        envio.setTelefonoReceptor(null);

        // Create the Envio, which fails.
        EnvioDTO envioDTO = envioMapper.toDto(envio);

        restEnvioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(envioDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEnvios() throws Exception {
        // Initialize the database
        insertedEnvio = envioRepository.saveAndFlush(envio);

        // Get all the envioList
        restEnvioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(envio.getId().intValue())))
            .andExpect(jsonPath("$.[*].courier").value(hasItem(DEFAULT_COURIER)))
            .andExpect(jsonPath("$.[*].tracking").value(hasItem(DEFAULT_TRACKING)))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO.toString())))
            .andExpect(jsonPath("$.[*].direccionEntrega").value(hasItem(DEFAULT_DIRECCION_ENTREGA)))
            .andExpect(jsonPath("$.[*].ubigeoEntrega").value(hasItem(DEFAULT_UBIGEO_ENTREGA)))
            .andExpect(jsonPath("$.[*].referenciaEntrega").value(hasItem(DEFAULT_REFERENCIA_ENTREGA)))
            .andExpect(jsonPath("$.[*].nombreReceptor").value(hasItem(DEFAULT_NOMBRE_RECEPTOR)))
            .andExpect(jsonPath("$.[*].telefonoReceptor").value(hasItem(DEFAULT_TELEFONO_RECEPTOR)))
            .andExpect(jsonPath("$.[*].fechaEstimada").value(hasItem(DEFAULT_FECHA_ESTIMADA.toString())))
            .andExpect(jsonPath("$.[*].fechaEntrega").value(hasItem(DEFAULT_FECHA_ENTREGA.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEnviosWithEagerRelationshipsIsEnabled() throws Exception {
        when(envioServiceMock.findAllWithEagerRelationships()).thenReturn(new ArrayList<>());

        restEnvioMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(envioServiceMock, times(1)).findAllWithEagerRelationships();
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEnviosWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(envioServiceMock.findAllWithEagerRelationships()).thenReturn(new ArrayList<>());

        restEnvioMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(envioRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getEnvio() throws Exception {
        // Initialize the database
        insertedEnvio = envioRepository.saveAndFlush(envio);

        // Get the envio
        restEnvioMockMvc
            .perform(get(ENTITY_API_URL_ID, envio.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(envio.getId().intValue()))
            .andExpect(jsonPath("$.courier").value(DEFAULT_COURIER))
            .andExpect(jsonPath("$.tracking").value(DEFAULT_TRACKING))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO.toString()))
            .andExpect(jsonPath("$.direccionEntrega").value(DEFAULT_DIRECCION_ENTREGA))
            .andExpect(jsonPath("$.ubigeoEntrega").value(DEFAULT_UBIGEO_ENTREGA))
            .andExpect(jsonPath("$.referenciaEntrega").value(DEFAULT_REFERENCIA_ENTREGA))
            .andExpect(jsonPath("$.nombreReceptor").value(DEFAULT_NOMBRE_RECEPTOR))
            .andExpect(jsonPath("$.telefonoReceptor").value(DEFAULT_TELEFONO_RECEPTOR))
            .andExpect(jsonPath("$.fechaEstimada").value(DEFAULT_FECHA_ESTIMADA.toString()))
            .andExpect(jsonPath("$.fechaEntrega").value(DEFAULT_FECHA_ENTREGA.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingEnvio() throws Exception {
        // Get the envio
        restEnvioMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEnvio() throws Exception {
        // Initialize the database
        insertedEnvio = envioRepository.saveAndFlush(envio);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the envio
        Envio updatedEnvio = envioRepository.findById(envio.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEnvio are not directly saved in db
        em.detach(updatedEnvio);
        updatedEnvio
            .courier(UPDATED_COURIER)
            .tracking(UPDATED_TRACKING)
            .estado(UPDATED_ESTADO)
            .direccionEntrega(UPDATED_DIRECCION_ENTREGA)
            .ubigeoEntrega(UPDATED_UBIGEO_ENTREGA)
            .referenciaEntrega(UPDATED_REFERENCIA_ENTREGA)
            .nombreReceptor(UPDATED_NOMBRE_RECEPTOR)
            .telefonoReceptor(UPDATED_TELEFONO_RECEPTOR)
            .fechaEstimada(UPDATED_FECHA_ESTIMADA)
            .fechaEntrega(UPDATED_FECHA_ENTREGA)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        EnvioDTO envioDTO = envioMapper.toDto(updatedEnvio);

        restEnvioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, envioDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(envioDTO))
            )
            .andExpect(status().isOk());

        // Validate the Envio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEnvioToMatchAllProperties(updatedEnvio);
    }

    @Test
    @Transactional
    void putNonExistingEnvio() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        envio.setId(longCount.incrementAndGet());

        // Create the Envio
        EnvioDTO envioDTO = envioMapper.toDto(envio);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEnvioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, envioDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(envioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Envio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEnvio() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        envio.setId(longCount.incrementAndGet());

        // Create the Envio
        EnvioDTO envioDTO = envioMapper.toDto(envio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnvioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(envioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Envio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEnvio() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        envio.setId(longCount.incrementAndGet());

        // Create the Envio
        EnvioDTO envioDTO = envioMapper.toDto(envio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnvioMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(envioDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Envio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEnvioWithPatch() throws Exception {
        // Initialize the database
        insertedEnvio = envioRepository.saveAndFlush(envio);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the envio using partial update
        Envio partialUpdatedEnvio = new Envio();
        partialUpdatedEnvio.setId(envio.getId());

        partialUpdatedEnvio
            .courier(UPDATED_COURIER)
            .estado(UPDATED_ESTADO)
            .ubigeoEntrega(UPDATED_UBIGEO_ENTREGA)
            .fechaEntrega(UPDATED_FECHA_ENTREGA)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restEnvioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEnvio.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEnvio))
            )
            .andExpect(status().isOk());

        // Validate the Envio in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEnvioUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedEnvio, envio), getPersistedEnvio(envio));
    }

    @Test
    @Transactional
    void fullUpdateEnvioWithPatch() throws Exception {
        // Initialize the database
        insertedEnvio = envioRepository.saveAndFlush(envio);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the envio using partial update
        Envio partialUpdatedEnvio = new Envio();
        partialUpdatedEnvio.setId(envio.getId());

        partialUpdatedEnvio
            .courier(UPDATED_COURIER)
            .tracking(UPDATED_TRACKING)
            .estado(UPDATED_ESTADO)
            .direccionEntrega(UPDATED_DIRECCION_ENTREGA)
            .ubigeoEntrega(UPDATED_UBIGEO_ENTREGA)
            .referenciaEntrega(UPDATED_REFERENCIA_ENTREGA)
            .nombreReceptor(UPDATED_NOMBRE_RECEPTOR)
            .telefonoReceptor(UPDATED_TELEFONO_RECEPTOR)
            .fechaEstimada(UPDATED_FECHA_ESTIMADA)
            .fechaEntrega(UPDATED_FECHA_ENTREGA)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restEnvioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEnvio.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEnvio))
            )
            .andExpect(status().isOk());

        // Validate the Envio in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEnvioUpdatableFieldsEquals(partialUpdatedEnvio, getPersistedEnvio(partialUpdatedEnvio));
    }

    @Test
    @Transactional
    void patchNonExistingEnvio() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        envio.setId(longCount.incrementAndGet());

        // Create the Envio
        EnvioDTO envioDTO = envioMapper.toDto(envio);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEnvioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, envioDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(envioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Envio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEnvio() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        envio.setId(longCount.incrementAndGet());

        // Create the Envio
        EnvioDTO envioDTO = envioMapper.toDto(envio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnvioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(envioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Envio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEnvio() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        envio.setId(longCount.incrementAndGet());

        // Create the Envio
        EnvioDTO envioDTO = envioMapper.toDto(envio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnvioMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(envioDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Envio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEnvio() throws Exception {
        // Initialize the database
        insertedEnvio = envioRepository.saveAndFlush(envio);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the envio
        restEnvioMockMvc
            .perform(delete(ENTITY_API_URL_ID, envio.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return envioRepository.count();
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

    protected Envio getPersistedEnvio(Envio envio) {
        return envioRepository.findById(envio.getId()).orElseThrow();
    }

    protected void assertPersistedEnvioToMatchAllProperties(Envio expectedEnvio) {
        assertEnvioAllPropertiesEquals(expectedEnvio, getPersistedEnvio(expectedEnvio));
    }

    protected void assertPersistedEnvioToMatchUpdatableProperties(Envio expectedEnvio) {
        assertEnvioAllUpdatablePropertiesEquals(expectedEnvio, getPersistedEnvio(expectedEnvio));
    }
}
