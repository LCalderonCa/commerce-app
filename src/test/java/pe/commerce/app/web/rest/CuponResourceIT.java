package pe.commerce.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pe.commerce.app.domain.CuponAsserts.*;
import static pe.commerce.app.web.rest.TestUtil.createUpdateProxyForBean;
import static pe.commerce.app.web.rest.TestUtil.sameNumber;

import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
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
import pe.commerce.app.domain.Cupon;
import pe.commerce.app.domain.enumeration.TipoDescuento;
import pe.commerce.app.repository.CuponRepository;
import pe.commerce.app.service.dto.CuponDTO;
import pe.commerce.app.service.mapper.CuponMapper;
import tools.jackson.databind.ObjectMapper;

/**
 * Integration tests for the {@link CuponResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CuponResourceIT {

    private static final String DEFAULT_CODIGO = "AAAAAAAAAA";
    private static final String UPDATED_CODIGO = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final TipoDescuento DEFAULT_TIPO_DESCUENTO = TipoDescuento.PORCENTAJE;
    private static final TipoDescuento UPDATED_TIPO_DESCUENTO = TipoDescuento.MONTO_FIJO;

    private static final BigDecimal DEFAULT_VALOR = new BigDecimal(0);
    private static final BigDecimal UPDATED_VALOR = new BigDecimal(1);

    private static final BigDecimal DEFAULT_MONTO_MINIMO = new BigDecimal(0);
    private static final BigDecimal UPDATED_MONTO_MINIMO = new BigDecimal(1);

    private static final LocalDate DEFAULT_FECHA_INICIO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_INICIO = LocalDate.parse("2023-12-10");

    private static final LocalDate DEFAULT_FECHA_FIN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_FIN = LocalDate.parse("2023-12-10");

    private static final Integer DEFAULT_USOS_MAXIMOS = 1;
    private static final Integer UPDATED_USOS_MAXIMOS = 2;

    private static final Integer DEFAULT_USOS_POR_CLIENTE = 1;
    private static final Integer UPDATED_USOS_POR_CLIENTE = 2;

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

    private static final String ENTITY_API_URL = "/api/cupons";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CuponRepository cuponRepository;

    @Autowired
    private CuponMapper cuponMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCuponMockMvc;

    private Cupon cupon;

    private Cupon insertedCupon;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cupon createEntity() {
        return new Cupon()
            .codigo(DEFAULT_CODIGO)
            .descripcion(DEFAULT_DESCRIPCION)
            .tipoDescuento(DEFAULT_TIPO_DESCUENTO)
            .valor(DEFAULT_VALOR)
            .montoMinimo(DEFAULT_MONTO_MINIMO)
            .fechaInicio(DEFAULT_FECHA_INICIO)
            .fechaFin(DEFAULT_FECHA_FIN)
            .usosMaximos(DEFAULT_USOS_MAXIMOS)
            .usosPorCliente(DEFAULT_USOS_POR_CLIENTE)
            .activo(DEFAULT_ACTIVO)
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
    public static Cupon createUpdatedEntity() {
        return new Cupon()
            .codigo(UPDATED_CODIGO)
            .descripcion(UPDATED_DESCRIPCION)
            .tipoDescuento(UPDATED_TIPO_DESCUENTO)
            .valor(UPDATED_VALOR)
            .montoMinimo(UPDATED_MONTO_MINIMO)
            .fechaInicio(UPDATED_FECHA_INICIO)
            .fechaFin(UPDATED_FECHA_FIN)
            .usosMaximos(UPDATED_USOS_MAXIMOS)
            .usosPorCliente(UPDATED_USOS_POR_CLIENTE)
            .activo(UPDATED_ACTIVO)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
    }

    @BeforeEach
    void initTest() {
        cupon = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedCupon != null) {
            cuponRepository.delete(insertedCupon);
            insertedCupon = null;
        }
    }

    @Test
    @Transactional
    void createCupon() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Cupon
        CuponDTO cuponDTO = cuponMapper.toDto(cupon);
        var returnedCuponDTO = om.readValue(
            restCuponMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cuponDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CuponDTO.class
        );

        // Validate the Cupon in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCupon = cuponMapper.toEntity(returnedCuponDTO);
        assertCuponUpdatableFieldsEquals(returnedCupon, getPersistedCupon(returnedCupon));

        insertedCupon = returnedCupon;
    }

    @Test
    @Transactional
    void createCuponWithExistingId() throws Exception {
        // Create the Cupon with an existing ID
        cupon.setId(1L);
        CuponDTO cuponDTO = cuponMapper.toDto(cupon);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCuponMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cuponDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Cupon in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodigoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cupon.setCodigo(null);

        // Create the Cupon, which fails.
        CuponDTO cuponDTO = cuponMapper.toDto(cupon);

        restCuponMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cuponDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTipoDescuentoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cupon.setTipoDescuento(null);

        // Create the Cupon, which fails.
        CuponDTO cuponDTO = cuponMapper.toDto(cupon);

        restCuponMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cuponDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkValorIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cupon.setValor(null);

        // Create the Cupon, which fails.
        CuponDTO cuponDTO = cuponMapper.toDto(cupon);

        restCuponMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cuponDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFechaInicioIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cupon.setFechaInicio(null);

        // Create the Cupon, which fails.
        CuponDTO cuponDTO = cuponMapper.toDto(cupon);

        restCuponMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cuponDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFechaFinIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cupon.setFechaFin(null);

        // Create the Cupon, which fails.
        CuponDTO cuponDTO = cuponMapper.toDto(cupon);

        restCuponMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cuponDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActivoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cupon.setActivo(null);

        // Create the Cupon, which fails.
        CuponDTO cuponDTO = cuponMapper.toDto(cupon);

        restCuponMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cuponDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCupons() throws Exception {
        // Initialize the database
        insertedCupon = cuponRepository.saveAndFlush(cupon);

        // Get all the cuponList
        restCuponMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cupon.getId().intValue())))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].tipoDescuento").value(hasItem(DEFAULT_TIPO_DESCUENTO.toString())))
            .andExpect(jsonPath("$.[*].valor").value(hasItem(sameNumber(DEFAULT_VALOR))))
            .andExpect(jsonPath("$.[*].montoMinimo").value(hasItem(sameNumber(DEFAULT_MONTO_MINIMO))))
            .andExpect(jsonPath("$.[*].fechaInicio").value(hasItem(DEFAULT_FECHA_INICIO.toString())))
            .andExpect(jsonPath("$.[*].fechaFin").value(hasItem(DEFAULT_FECHA_FIN.toString())))
            .andExpect(jsonPath("$.[*].usosMaximos").value(hasItem(DEFAULT_USOS_MAXIMOS)))
            .andExpect(jsonPath("$.[*].usosPorCliente").value(hasItem(DEFAULT_USOS_POR_CLIENTE)))
            .andExpect(jsonPath("$.[*].activo").value(hasItem(DEFAULT_ACTIVO)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getCupon() throws Exception {
        // Initialize the database
        insertedCupon = cuponRepository.saveAndFlush(cupon);

        // Get the cupon
        restCuponMockMvc
            .perform(get(ENTITY_API_URL_ID, cupon.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cupon.getId().intValue()))
            .andExpect(jsonPath("$.codigo").value(DEFAULT_CODIGO))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION))
            .andExpect(jsonPath("$.tipoDescuento").value(DEFAULT_TIPO_DESCUENTO.toString()))
            .andExpect(jsonPath("$.valor").value(sameNumber(DEFAULT_VALOR)))
            .andExpect(jsonPath("$.montoMinimo").value(sameNumber(DEFAULT_MONTO_MINIMO)))
            .andExpect(jsonPath("$.fechaInicio").value(DEFAULT_FECHA_INICIO.toString()))
            .andExpect(jsonPath("$.fechaFin").value(DEFAULT_FECHA_FIN.toString()))
            .andExpect(jsonPath("$.usosMaximos").value(DEFAULT_USOS_MAXIMOS))
            .andExpect(jsonPath("$.usosPorCliente").value(DEFAULT_USOS_POR_CLIENTE))
            .andExpect(jsonPath("$.activo").value(DEFAULT_ACTIVO))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingCupon() throws Exception {
        // Get the cupon
        restCuponMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCupon() throws Exception {
        // Initialize the database
        insertedCupon = cuponRepository.saveAndFlush(cupon);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cupon
        Cupon updatedCupon = cuponRepository.findById(cupon.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCupon are not directly saved in db
        em.detach(updatedCupon);
        updatedCupon
            .codigo(UPDATED_CODIGO)
            .descripcion(UPDATED_DESCRIPCION)
            .tipoDescuento(UPDATED_TIPO_DESCUENTO)
            .valor(UPDATED_VALOR)
            .montoMinimo(UPDATED_MONTO_MINIMO)
            .fechaInicio(UPDATED_FECHA_INICIO)
            .fechaFin(UPDATED_FECHA_FIN)
            .usosMaximos(UPDATED_USOS_MAXIMOS)
            .usosPorCliente(UPDATED_USOS_POR_CLIENTE)
            .activo(UPDATED_ACTIVO)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        CuponDTO cuponDTO = cuponMapper.toDto(updatedCupon);

        restCuponMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cuponDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cuponDTO))
            )
            .andExpect(status().isOk());

        // Validate the Cupon in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCuponToMatchAllProperties(updatedCupon);
    }

    @Test
    @Transactional
    void putNonExistingCupon() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cupon.setId(longCount.incrementAndGet());

        // Create the Cupon
        CuponDTO cuponDTO = cuponMapper.toDto(cupon);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCuponMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cuponDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cuponDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cupon in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCupon() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cupon.setId(longCount.incrementAndGet());

        // Create the Cupon
        CuponDTO cuponDTO = cuponMapper.toDto(cupon);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCuponMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cuponDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cupon in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCupon() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cupon.setId(longCount.incrementAndGet());

        // Create the Cupon
        CuponDTO cuponDTO = cuponMapper.toDto(cupon);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCuponMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cuponDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cupon in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCuponWithPatch() throws Exception {
        // Initialize the database
        insertedCupon = cuponRepository.saveAndFlush(cupon);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cupon using partial update
        Cupon partialUpdatedCupon = new Cupon();
        partialUpdatedCupon.setId(cupon.getId());

        partialUpdatedCupon
            .codigo(UPDATED_CODIGO)
            .montoMinimo(UPDATED_MONTO_MINIMO)
            .fechaFin(UPDATED_FECHA_FIN)
            .usosMaximos(UPDATED_USOS_MAXIMOS)
            .usosPorCliente(UPDATED_USOS_POR_CLIENTE)
            .activo(UPDATED_ACTIVO)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restCuponMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCupon.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCupon))
            )
            .andExpect(status().isOk());

        // Validate the Cupon in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCuponUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedCupon, cupon), getPersistedCupon(cupon));
    }

    @Test
    @Transactional
    void fullUpdateCuponWithPatch() throws Exception {
        // Initialize the database
        insertedCupon = cuponRepository.saveAndFlush(cupon);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cupon using partial update
        Cupon partialUpdatedCupon = new Cupon();
        partialUpdatedCupon.setId(cupon.getId());

        partialUpdatedCupon
            .codigo(UPDATED_CODIGO)
            .descripcion(UPDATED_DESCRIPCION)
            .tipoDescuento(UPDATED_TIPO_DESCUENTO)
            .valor(UPDATED_VALOR)
            .montoMinimo(UPDATED_MONTO_MINIMO)
            .fechaInicio(UPDATED_FECHA_INICIO)
            .fechaFin(UPDATED_FECHA_FIN)
            .usosMaximos(UPDATED_USOS_MAXIMOS)
            .usosPorCliente(UPDATED_USOS_POR_CLIENTE)
            .activo(UPDATED_ACTIVO)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restCuponMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCupon.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCupon))
            )
            .andExpect(status().isOk());

        // Validate the Cupon in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCuponUpdatableFieldsEquals(partialUpdatedCupon, getPersistedCupon(partialUpdatedCupon));
    }

    @Test
    @Transactional
    void patchNonExistingCupon() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cupon.setId(longCount.incrementAndGet());

        // Create the Cupon
        CuponDTO cuponDTO = cuponMapper.toDto(cupon);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCuponMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cuponDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cuponDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cupon in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCupon() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cupon.setId(longCount.incrementAndGet());

        // Create the Cupon
        CuponDTO cuponDTO = cuponMapper.toDto(cupon);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCuponMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cuponDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cupon in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCupon() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cupon.setId(longCount.incrementAndGet());

        // Create the Cupon
        CuponDTO cuponDTO = cuponMapper.toDto(cupon);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCuponMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(cuponDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cupon in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCupon() throws Exception {
        // Initialize the database
        insertedCupon = cuponRepository.saveAndFlush(cupon);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the cupon
        restCuponMockMvc
            .perform(delete(ENTITY_API_URL_ID, cupon.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return cuponRepository.count();
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

    protected Cupon getPersistedCupon(Cupon cupon) {
        return cuponRepository.findById(cupon.getId()).orElseThrow();
    }

    protected void assertPersistedCuponToMatchAllProperties(Cupon expectedCupon) {
        assertCuponAllPropertiesEquals(expectedCupon, getPersistedCupon(expectedCupon));
    }

    protected void assertPersistedCuponToMatchUpdatableProperties(Cupon expectedCupon) {
        assertCuponAllUpdatablePropertiesEquals(expectedCupon, getPersistedCupon(expectedCupon));
    }
}
