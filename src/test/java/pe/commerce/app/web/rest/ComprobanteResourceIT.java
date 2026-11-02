package pe.commerce.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pe.commerce.app.domain.ComprobanteAsserts.*;
import static pe.commerce.app.web.rest.TestUtil.createUpdateProxyForBean;
import static pe.commerce.app.web.rest.TestUtil.sameNumber;

import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
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
import pe.commerce.app.domain.Comprobante;
import pe.commerce.app.domain.Comprobante;
import pe.commerce.app.domain.Pedido;
import pe.commerce.app.domain.enumeration.EstadoSunat;
import pe.commerce.app.domain.enumeration.Moneda;
import pe.commerce.app.domain.enumeration.TipoComprobante;
import pe.commerce.app.repository.ComprobanteRepository;
import pe.commerce.app.service.ComprobanteService;
import pe.commerce.app.service.dto.ComprobanteDTO;
import pe.commerce.app.service.mapper.ComprobanteMapper;
import tools.jackson.databind.ObjectMapper;

/**
 * Integration tests for the {@link ComprobanteResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ComprobanteResourceIT {

    private static final TipoComprobante DEFAULT_TIPO = TipoComprobante.BOLETA;
    private static final TipoComprobante UPDATED_TIPO = TipoComprobante.FACTURA;

    private static final String DEFAULT_SERIE = "AAAA";
    private static final String UPDATED_SERIE = "BBBB";

    private static final Integer DEFAULT_NUMERO = 1;
    private static final Integer UPDATED_NUMERO = 2;
    private static final Integer SMALLER_NUMERO = 1 - 1;

    private static final Instant DEFAULT_FECHA_EMISION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_EMISION = Instant.ofEpochMilli(1702246707543L);

    private static final String DEFAULT_RECEPTOR_TIPO_DOCUMENTO = "AAAAAAAAAA";
    private static final String UPDATED_RECEPTOR_TIPO_DOCUMENTO = "BBBBBBBBBB";

    private static final String DEFAULT_RECEPTOR_NRO_DOCUMENTO = "AAAAAAAAAA";
    private static final String UPDATED_RECEPTOR_NRO_DOCUMENTO = "BBBBBBBBBB";

    private static final String DEFAULT_RECEPTOR_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_RECEPTOR_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_RECEPTOR_DIRECCION = "AAAAAAAAAA";
    private static final String UPDATED_RECEPTOR_DIRECCION = "BBBBBBBBBB";

    private static final Moneda DEFAULT_MONEDA = Moneda.PEN;
    private static final Moneda UPDATED_MONEDA = Moneda.USD;

    private static final BigDecimal DEFAULT_OP_GRAVADA = new BigDecimal(0);
    private static final BigDecimal UPDATED_OP_GRAVADA = new BigDecimal(1);
    private static final BigDecimal SMALLER_OP_GRAVADA = new BigDecimal(0 - 1);

    private static final BigDecimal DEFAULT_IGV = new BigDecimal(0);
    private static final BigDecimal UPDATED_IGV = new BigDecimal(1);
    private static final BigDecimal SMALLER_IGV = new BigDecimal(0 - 1);

    private static final BigDecimal DEFAULT_TOTAL = new BigDecimal(0);
    private static final BigDecimal UPDATED_TOTAL = new BigDecimal(1);
    private static final BigDecimal SMALLER_TOTAL = new BigDecimal(0 - 1);

    private static final EstadoSunat DEFAULT_ESTADO_SUNAT = EstadoSunat.PENDIENTE;
    private static final EstadoSunat UPDATED_ESTADO_SUNAT = EstadoSunat.ACEPTADO;

    private static final String DEFAULT_XML_URL = "AAAAAAAAAA";
    private static final String UPDATED_XML_URL = "BBBBBBBBBB";

    private static final String DEFAULT_PDF_URL = "AAAAAAAAAA";
    private static final String UPDATED_PDF_URL = "BBBBBBBBBB";

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.ofEpochMilli(1702246707543L);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.ofEpochMilli(1702246707543L);

    private static final String ENTITY_API_URL = "/api/comprobantes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ComprobanteRepository comprobanteRepository;

    @Mock
    private ComprobanteRepository comprobanteRepositoryMock;

    @Autowired
    private ComprobanteMapper comprobanteMapper;

    @Mock
    private ComprobanteService comprobanteServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restComprobanteMockMvc;

    private Comprobante comprobante;

    private Comprobante insertedComprobante;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Comprobante createEntity(EntityManager em) {
        Comprobante comprobante = new Comprobante()
            .tipo(DEFAULT_TIPO)
            .serie(DEFAULT_SERIE)
            .numero(DEFAULT_NUMERO)
            .fechaEmision(DEFAULT_FECHA_EMISION)
            .receptorTipoDocumento(DEFAULT_RECEPTOR_TIPO_DOCUMENTO)
            .receptorNroDocumento(DEFAULT_RECEPTOR_NRO_DOCUMENTO)
            .receptorNombre(DEFAULT_RECEPTOR_NOMBRE)
            .receptorDireccion(DEFAULT_RECEPTOR_DIRECCION)
            .moneda(DEFAULT_MONEDA)
            .opGravada(DEFAULT_OP_GRAVADA)
            .igv(DEFAULT_IGV)
            .total(DEFAULT_TOTAL)
            .estadoSunat(DEFAULT_ESTADO_SUNAT)
            .xmlUrl(DEFAULT_XML_URL)
            .pdfUrl(DEFAULT_PDF_URL)
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
        comprobante.setPedido(pedido);
        return comprobante;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Comprobante createUpdatedEntity(EntityManager em) {
        Comprobante updatedComprobante = new Comprobante()
            .tipo(UPDATED_TIPO)
            .serie(UPDATED_SERIE)
            .numero(UPDATED_NUMERO)
            .fechaEmision(UPDATED_FECHA_EMISION)
            .receptorTipoDocumento(UPDATED_RECEPTOR_TIPO_DOCUMENTO)
            .receptorNroDocumento(UPDATED_RECEPTOR_NRO_DOCUMENTO)
            .receptorNombre(UPDATED_RECEPTOR_NOMBRE)
            .receptorDireccion(UPDATED_RECEPTOR_DIRECCION)
            .moneda(UPDATED_MONEDA)
            .opGravada(UPDATED_OP_GRAVADA)
            .igv(UPDATED_IGV)
            .total(UPDATED_TOTAL)
            .estadoSunat(UPDATED_ESTADO_SUNAT)
            .xmlUrl(UPDATED_XML_URL)
            .pdfUrl(UPDATED_PDF_URL)
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
        updatedComprobante.setPedido(pedido);
        return updatedComprobante;
    }

    @BeforeEach
    void initTest() {
        comprobante = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedComprobante != null) {
            comprobanteRepository.delete(insertedComprobante);
            insertedComprobante = null;
        }
    }

    @Test
    @Transactional
    void createComprobante() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Comprobante
        ComprobanteDTO comprobanteDTO = comprobanteMapper.toDto(comprobante);
        var returnedComprobanteDTO = om.readValue(
            restComprobanteMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(comprobanteDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ComprobanteDTO.class
        );

        // Validate the Comprobante in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedComprobante = comprobanteMapper.toEntity(returnedComprobanteDTO);
        assertComprobanteUpdatableFieldsEquals(returnedComprobante, getPersistedComprobante(returnedComprobante));

        insertedComprobante = returnedComprobante;
    }

    @Test
    @Transactional
    void createComprobanteWithExistingId() throws Exception {
        // Create the Comprobante with an existing ID
        comprobante.setId(1L);
        ComprobanteDTO comprobanteDTO = comprobanteMapper.toDto(comprobante);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restComprobanteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(comprobanteDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Comprobante in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTipoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        comprobante.setTipo(null);

        // Create the Comprobante, which fails.
        ComprobanteDTO comprobanteDTO = comprobanteMapper.toDto(comprobante);

        restComprobanteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(comprobanteDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSerieIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        comprobante.setSerie(null);

        // Create the Comprobante, which fails.
        ComprobanteDTO comprobanteDTO = comprobanteMapper.toDto(comprobante);

        restComprobanteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(comprobanteDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNumeroIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        comprobante.setNumero(null);

        // Create the Comprobante, which fails.
        ComprobanteDTO comprobanteDTO = comprobanteMapper.toDto(comprobante);

        restComprobanteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(comprobanteDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFechaEmisionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        comprobante.setFechaEmision(null);

        // Create the Comprobante, which fails.
        ComprobanteDTO comprobanteDTO = comprobanteMapper.toDto(comprobante);

        restComprobanteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(comprobanteDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkReceptorTipoDocumentoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        comprobante.setReceptorTipoDocumento(null);

        // Create the Comprobante, which fails.
        ComprobanteDTO comprobanteDTO = comprobanteMapper.toDto(comprobante);

        restComprobanteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(comprobanteDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkReceptorNroDocumentoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        comprobante.setReceptorNroDocumento(null);

        // Create the Comprobante, which fails.
        ComprobanteDTO comprobanteDTO = comprobanteMapper.toDto(comprobante);

        restComprobanteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(comprobanteDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkReceptorNombreIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        comprobante.setReceptorNombre(null);

        // Create the Comprobante, which fails.
        ComprobanteDTO comprobanteDTO = comprobanteMapper.toDto(comprobante);

        restComprobanteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(comprobanteDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMonedaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        comprobante.setMoneda(null);

        // Create the Comprobante, which fails.
        ComprobanteDTO comprobanteDTO = comprobanteMapper.toDto(comprobante);

        restComprobanteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(comprobanteDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkOpGravadaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        comprobante.setOpGravada(null);

        // Create the Comprobante, which fails.
        ComprobanteDTO comprobanteDTO = comprobanteMapper.toDto(comprobante);

        restComprobanteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(comprobanteDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIgvIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        comprobante.setIgv(null);

        // Create the Comprobante, which fails.
        ComprobanteDTO comprobanteDTO = comprobanteMapper.toDto(comprobante);

        restComprobanteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(comprobanteDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTotalIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        comprobante.setTotal(null);

        // Create the Comprobante, which fails.
        ComprobanteDTO comprobanteDTO = comprobanteMapper.toDto(comprobante);

        restComprobanteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(comprobanteDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEstadoSunatIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        comprobante.setEstadoSunat(null);

        // Create the Comprobante, which fails.
        ComprobanteDTO comprobanteDTO = comprobanteMapper.toDto(comprobante);

        restComprobanteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(comprobanteDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllComprobantes() throws Exception {
        // Initialize the database
        insertedComprobante = comprobanteRepository.saveAndFlush(comprobante);

        // Get all the comprobanteList
        restComprobanteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(comprobante.getId().intValue())))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO.toString())))
            .andExpect(jsonPath("$.[*].serie").value(hasItem(DEFAULT_SERIE)))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO)))
            .andExpect(jsonPath("$.[*].fechaEmision").value(hasItem(DEFAULT_FECHA_EMISION.toString())))
            .andExpect(jsonPath("$.[*].receptorTipoDocumento").value(hasItem(DEFAULT_RECEPTOR_TIPO_DOCUMENTO)))
            .andExpect(jsonPath("$.[*].receptorNroDocumento").value(hasItem(DEFAULT_RECEPTOR_NRO_DOCUMENTO)))
            .andExpect(jsonPath("$.[*].receptorNombre").value(hasItem(DEFAULT_RECEPTOR_NOMBRE)))
            .andExpect(jsonPath("$.[*].receptorDireccion").value(hasItem(DEFAULT_RECEPTOR_DIRECCION)))
            .andExpect(jsonPath("$.[*].moneda").value(hasItem(DEFAULT_MONEDA.toString())))
            .andExpect(jsonPath("$.[*].opGravada").value(hasItem(sameNumber(DEFAULT_OP_GRAVADA))))
            .andExpect(jsonPath("$.[*].igv").value(hasItem(sameNumber(DEFAULT_IGV))))
            .andExpect(jsonPath("$.[*].total").value(hasItem(sameNumber(DEFAULT_TOTAL))))
            .andExpect(jsonPath("$.[*].estadoSunat").value(hasItem(DEFAULT_ESTADO_SUNAT.toString())))
            .andExpect(jsonPath("$.[*].xmlUrl").value(hasItem(DEFAULT_XML_URL)))
            .andExpect(jsonPath("$.[*].pdfUrl").value(hasItem(DEFAULT_PDF_URL)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllComprobantesWithEagerRelationshipsIsEnabled() throws Exception {
        when(comprobanteServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restComprobanteMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(comprobanteServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllComprobantesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(comprobanteServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restComprobanteMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(comprobanteRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getComprobante() throws Exception {
        // Initialize the database
        insertedComprobante = comprobanteRepository.saveAndFlush(comprobante);

        // Get the comprobante
        restComprobanteMockMvc
            .perform(get(ENTITY_API_URL_ID, comprobante.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(comprobante.getId().intValue()))
            .andExpect(jsonPath("$.tipo").value(DEFAULT_TIPO.toString()))
            .andExpect(jsonPath("$.serie").value(DEFAULT_SERIE))
            .andExpect(jsonPath("$.numero").value(DEFAULT_NUMERO))
            .andExpect(jsonPath("$.fechaEmision").value(DEFAULT_FECHA_EMISION.toString()))
            .andExpect(jsonPath("$.receptorTipoDocumento").value(DEFAULT_RECEPTOR_TIPO_DOCUMENTO))
            .andExpect(jsonPath("$.receptorNroDocumento").value(DEFAULT_RECEPTOR_NRO_DOCUMENTO))
            .andExpect(jsonPath("$.receptorNombre").value(DEFAULT_RECEPTOR_NOMBRE))
            .andExpect(jsonPath("$.receptorDireccion").value(DEFAULT_RECEPTOR_DIRECCION))
            .andExpect(jsonPath("$.moneda").value(DEFAULT_MONEDA.toString()))
            .andExpect(jsonPath("$.opGravada").value(sameNumber(DEFAULT_OP_GRAVADA)))
            .andExpect(jsonPath("$.igv").value(sameNumber(DEFAULT_IGV)))
            .andExpect(jsonPath("$.total").value(sameNumber(DEFAULT_TOTAL)))
            .andExpect(jsonPath("$.estadoSunat").value(DEFAULT_ESTADO_SUNAT.toString()))
            .andExpect(jsonPath("$.xmlUrl").value(DEFAULT_XML_URL))
            .andExpect(jsonPath("$.pdfUrl").value(DEFAULT_PDF_URL))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getComprobantesByIdFiltering() throws Exception {
        // Initialize the database
        insertedComprobante = comprobanteRepository.saveAndFlush(comprobante);

        Long id = comprobante.getId();

        defaultComprobanteFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultComprobanteFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultComprobanteFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllComprobantesByTipoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedComprobante = comprobanteRepository.saveAndFlush(comprobante);

        // Get all the comprobanteList where tipo equals to
        defaultComprobanteFiltering("tipo.equals=" + DEFAULT_TIPO, "tipo.equals=" + UPDATED_TIPO);
    }

    @Test
    @Transactional
    void getAllComprobantesByTipoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedComprobante = comprobanteRepository.saveAndFlush(comprobante);

        // Get all the comprobanteList where tipo in
        defaultComprobanteFiltering("tipo.in=" + DEFAULT_TIPO + "," + UPDATED_TIPO, "tipo.in=" + UPDATED_TIPO);
    }

    @Test
    @Transactional
    void getAllComprobantesByTipoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedComprobante = comprobanteRepository.saveAndFlush(comprobante);

        // Get all the comprobanteList where tipo is not null
        defaultComprobanteFiltering("tipo.specified=true", "tipo.specified=false");
    }

    @Test
    @Transactional
    void getAllComprobantesBySerieIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedComprobante = comprobanteRepository.saveAndFlush(comprobante);

        // Get all the comprobanteList where serie equals to
        defaultComprobanteFiltering("serie.equals=" + DEFAULT_SERIE, "serie.equals=" + UPDATED_SERIE);
    }

    @Test
    @Transactional
    void getAllComprobantesBySerieIsInShouldWork() throws Exception {
        // Initialize the database
        insertedComprobante = comprobanteRepository.saveAndFlush(comprobante);

        // Get all the comprobanteList where serie in
        defaultComprobanteFiltering("serie.in=" + DEFAULT_SERIE + "," + UPDATED_SERIE, "serie.in=" + UPDATED_SERIE);
    }

    @Test
    @Transactional
    void getAllComprobantesBySerieIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedComprobante = comprobanteRepository.saveAndFlush(comprobante);

        // Get all the comprobanteList where serie is not null
        defaultComprobanteFiltering("serie.specified=true", "serie.specified=false");
    }

    @Test
    @Transactional
    void getAllComprobantesBySerieContainsSomething() throws Exception {
        // Initialize the database
        insertedComprobante = comprobanteRepository.saveAndFlush(comprobante);

        // Get all the comprobanteList where serie contains
        defaultComprobanteFiltering("serie.contains=" + DEFAULT_SERIE, "serie.contains=" + UPDATED_SERIE);
    }

    @Test
    @Transactional
    void getAllComprobantesBySerieNotContainsSomething() throws Exception {
        // Initialize the database
        insertedComprobante = comprobanteRepository.saveAndFlush(comprobante);

        // Get all the comprobanteList where serie does not contain
        defaultComprobanteFiltering("serie.doesNotContain=" + UPDATED_SERIE, "serie.doesNotContain=" + DEFAULT_SERIE);
    }

    @Test
    @Transactional
    void getAllComprobantesByNumeroIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedComprobante = comprobanteRepository.saveAndFlush(comprobante);

        // Get all the comprobanteList where numero equals to
        defaultComprobanteFiltering("numero.equals=" + DEFAULT_NUMERO, "numero.equals=" + UPDATED_NUMERO);
    }

    @Test
    @Transactional
    void getAllComprobantesByNumeroIsInShouldWork() throws Exception {
        // Initialize the database
        insertedComprobante = comprobanteRepository.saveAndFlush(comprobante);

        // Get all the comprobanteList where numero in
        defaultComprobanteFiltering("numero.in=" + DEFAULT_NUMERO + "," + UPDATED_NUMERO, "numero.in=" + UPDATED_NUMERO);
    }

    @Test
    @Transactional
    void getAllComprobantesByNumeroIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedComprobante = comprobanteRepository.saveAndFlush(comprobante);

        // Get all the comprobanteList where numero is not null
        defaultComprobanteFiltering("numero.specified=true", "numero.specified=false");
    }

    @Test
    @Transactional
    void getAllComprobantesByNumeroIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedComprobante = comprobanteRepository.saveAndFlush(comprobante);

        // Get all the comprobanteList where numero is greater than or equal to
        defaultComprobanteFiltering("numero.greaterThanOrEqual=" + DEFAULT_NUMERO, "numero.greaterThanOrEqual=" + UPDATED_NUMERO);
    }

    @Test
    @Transactional
    void getAllComprobantesByNumeroIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedComprobante = comprobanteRepository.saveAndFlush(comprobante);

        // Get all the comprobanteList where numero is less than or equal to
        defaultComprobanteFiltering("numero.lessThanOrEqual=" + DEFAULT_NUMERO, "numero.lessThanOrEqual=" + SMALLER_NUMERO);
    }

    @Test
    @Transactional
    void getAllComprobantesByNumeroIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedComprobante = comprobanteRepository.saveAndFlush(comprobante);

        // Get all the comprobanteList where numero is less than
        defaultComprobanteFiltering("numero.lessThan=" + UPDATED_NUMERO, "numero.lessThan=" + DEFAULT_NUMERO);
    }

    @Test
    @Transactional
    void getAllComprobantesByNumeroIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedComprobante = comprobanteRepository.saveAndFlush(comprobante);

        // Get all the comprobanteList where numero is greater than
        defaultComprobanteFiltering("numero.greaterThan=" + SMALLER_NUMERO, "numero.greaterThan=" + DEFAULT_NUMERO);
    }

    @Test
    @Transactional
    void getAllComprobantesByFechaEmisionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedComprobante = comprobanteRepository.saveAndFlush(comprobante);

        // Get all the comprobanteList where fechaEmision equals to
        defaultComprobanteFiltering("fechaEmision.equals=" + DEFAULT_FECHA_EMISION, "fechaEmision.equals=" + UPDATED_FECHA_EMISION);
    }

    @Test
    @Transactional
    void getAllComprobantesByFechaEmisionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedComprobante = comprobanteRepository.saveAndFlush(comprobante);

        // Get all the comprobanteList where fechaEmision in
        defaultComprobanteFiltering(
            "fechaEmision.in=" + DEFAULT_FECHA_EMISION + "," + UPDATED_FECHA_EMISION,
            "fechaEmision.in=" + UPDATED_FECHA_EMISION
        );
    }

    @Test
    @Transactional
    void getAllComprobantesByFechaEmisionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedComprobante = comprobanteRepository.saveAndFlush(comprobante);

        // Get all the comprobanteList where fechaEmision is not null
        defaultComprobanteFiltering("fechaEmision.specified=true", "fechaEmision.specified=false");
    }

    @Test
    @Transactional
    void getAllComprobantesByReceptorTipoDocumentoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedComprobante = comprobanteRepository.saveAndFlush(comprobante);

        // Get all the comprobanteList where receptorTipoDocumento equals to
        defaultComprobanteFiltering(
            "receptorTipoDocumento.equals=" + DEFAULT_RECEPTOR_TIPO_DOCUMENTO,
            "receptorTipoDocumento.equals=" + UPDATED_RECEPTOR_TIPO_DOCUMENTO
        );
    }

    @Test
    @Transactional
    void getAllComprobantesByReceptorTipoDocumentoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedComprobante = comprobanteRepository.saveAndFlush(comprobante);

        // Get all the comprobanteList where receptorTipoDocumento in
        defaultComprobanteFiltering(
            "receptorTipoDocumento.in=" + DEFAULT_RECEPTOR_TIPO_DOCUMENTO + "," + UPDATED_RECEPTOR_TIPO_DOCUMENTO,
            "receptorTipoDocumento.in=" + UPDATED_RECEPTOR_TIPO_DOCUMENTO
        );
    }

    @Test
    @Transactional
    void getAllComprobantesByReceptorTipoDocumentoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedComprobante = comprobanteRepository.saveAndFlush(comprobante);

        // Get all the comprobanteList where receptorTipoDocumento is not null
        defaultComprobanteFiltering("receptorTipoDocumento.specified=true", "receptorTipoDocumento.specified=false");
    }

    @Test
    @Transactional
    void getAllComprobantesByReceptorTipoDocumentoContainsSomething() throws Exception {
        // Initialize the database
        insertedComprobante = comprobanteRepository.saveAndFlush(comprobante);

        // Get all the comprobanteList where receptorTipoDocumento contains
        defaultComprobanteFiltering(
            "receptorTipoDocumento.contains=" + DEFAULT_RECEPTOR_TIPO_DOCUMENTO,
            "receptorTipoDocumento.contains=" + UPDATED_RECEPTOR_TIPO_DOCUMENTO
        );
    }

    @Test
    @Transactional
    void getAllComprobantesByReceptorTipoDocumentoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedComprobante = comprobanteRepository.saveAndFlush(comprobante);

        // Get all the comprobanteList where receptorTipoDocumento does not contain
        defaultComprobanteFiltering(
            "receptorTipoDocumento.doesNotContain=" + UPDATED_RECEPTOR_TIPO_DOCUMENTO,
            "receptorTipoDocumento.doesNotContain=" + DEFAULT_RECEPTOR_TIPO_DOCUMENTO
        );
    }

    @Test
    @Transactional
    void getAllComprobantesByReceptorNroDocumentoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedComprobante = comprobanteRepository.saveAndFlush(comprobante);

        // Get all the comprobanteList where receptorNroDocumento equals to
        defaultComprobanteFiltering(
            "receptorNroDocumento.equals=" + DEFAULT_RECEPTOR_NRO_DOCUMENTO,
            "receptorNroDocumento.equals=" + UPDATED_RECEPTOR_NRO_DOCUMENTO
        );
    }

    @Test
    @Transactional
    void getAllComprobantesByReceptorNroDocumentoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedComprobante = comprobanteRepository.saveAndFlush(comprobante);

        // Get all the comprobanteList where receptorNroDocumento in
        defaultComprobanteFiltering(
            "receptorNroDocumento.in=" + DEFAULT_RECEPTOR_NRO_DOCUMENTO + "," + UPDATED_RECEPTOR_NRO_DOCUMENTO,
            "receptorNroDocumento.in=" + UPDATED_RECEPTOR_NRO_DOCUMENTO
        );
    }

    @Test
    @Transactional
    void getAllComprobantesByReceptorNroDocumentoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedComprobante = comprobanteRepository.saveAndFlush(comprobante);

        // Get all the comprobanteList where receptorNroDocumento is not null
        defaultComprobanteFiltering("receptorNroDocumento.specified=true", "receptorNroDocumento.specified=false");
    }

    @Test
    @Transactional
    void getAllComprobantesByReceptorNroDocumentoContainsSomething() throws Exception {
        // Initialize the database
        insertedComprobante = comprobanteRepository.saveAndFlush(comprobante);

        // Get all the comprobanteList where receptorNroDocumento contains
        defaultComprobanteFiltering(
            "receptorNroDocumento.contains=" + DEFAULT_RECEPTOR_NRO_DOCUMENTO,
            "receptorNroDocumento.contains=" + UPDATED_RECEPTOR_NRO_DOCUMENTO
        );
    }

    @Test
    @Transactional
    void getAllComprobantesByReceptorNroDocumentoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedComprobante = comprobanteRepository.saveAndFlush(comprobante);

        // Get all the comprobanteList where receptorNroDocumento does not contain
        defaultComprobanteFiltering(
            "receptorNroDocumento.doesNotContain=" + UPDATED_RECEPTOR_NRO_DOCUMENTO,
            "receptorNroDocumento.doesNotContain=" + DEFAULT_RECEPTOR_NRO_DOCUMENTO
        );
    }

    @Test
    @Transactional
    void getAllComprobantesByReceptorNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedComprobante = comprobanteRepository.saveAndFlush(comprobante);

        // Get all the comprobanteList where receptorNombre equals to
        defaultComprobanteFiltering("receptorNombre.equals=" + DEFAULT_RECEPTOR_NOMBRE, "receptorNombre.equals=" + UPDATED_RECEPTOR_NOMBRE);
    }

    @Test
    @Transactional
    void getAllComprobantesByReceptorNombreIsInShouldWork() throws Exception {
        // Initialize the database
        insertedComprobante = comprobanteRepository.saveAndFlush(comprobante);

        // Get all the comprobanteList where receptorNombre in
        defaultComprobanteFiltering(
            "receptorNombre.in=" + DEFAULT_RECEPTOR_NOMBRE + "," + UPDATED_RECEPTOR_NOMBRE,
            "receptorNombre.in=" + UPDATED_RECEPTOR_NOMBRE
        );
    }

    @Test
    @Transactional
    void getAllComprobantesByReceptorNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedComprobante = comprobanteRepository.saveAndFlush(comprobante);

        // Get all the comprobanteList where receptorNombre is not null
        defaultComprobanteFiltering("receptorNombre.specified=true", "receptorNombre.specified=false");
    }

    @Test
    @Transactional
    void getAllComprobantesByReceptorNombreContainsSomething() throws Exception {
        // Initialize the database
        insertedComprobante = comprobanteRepository.saveAndFlush(comprobante);

        // Get all the comprobanteList where receptorNombre contains
        defaultComprobanteFiltering(
            "receptorNombre.contains=" + DEFAULT_RECEPTOR_NOMBRE,
            "receptorNombre.contains=" + UPDATED_RECEPTOR_NOMBRE
        );
    }

    @Test
    @Transactional
    void getAllComprobantesByReceptorNombreNotContainsSomething() throws Exception {
        // Initialize the database
        insertedComprobante = comprobanteRepository.saveAndFlush(comprobante);

        // Get all the comprobanteList where receptorNombre does not contain
        defaultComprobanteFiltering(
            "receptorNombre.doesNotContain=" + UPDATED_RECEPTOR_NOMBRE,
            "receptorNombre.doesNotContain=" + DEFAULT_RECEPTOR_NOMBRE
        );
    }

    @Test
    @Transactional
    void getAllComprobantesByReceptorDireccionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedComprobante = comprobanteRepository.saveAndFlush(comprobante);

        // Get all the comprobanteList where receptorDireccion equals to
        defaultComprobanteFiltering(
            "receptorDireccion.equals=" + DEFAULT_RECEPTOR_DIRECCION,
            "receptorDireccion.equals=" + UPDATED_RECEPTOR_DIRECCION
        );
    }

    @Test
    @Transactional
    void getAllComprobantesByReceptorDireccionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedComprobante = comprobanteRepository.saveAndFlush(comprobante);

        // Get all the comprobanteList where receptorDireccion in
        defaultComprobanteFiltering(
            "receptorDireccion.in=" + DEFAULT_RECEPTOR_DIRECCION + "," + UPDATED_RECEPTOR_DIRECCION,
            "receptorDireccion.in=" + UPDATED_RECEPTOR_DIRECCION
        );
    }

    @Test
    @Transactional
    void getAllComprobantesByReceptorDireccionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedComprobante = comprobanteRepository.saveAndFlush(comprobante);

        // Get all the comprobanteList where receptorDireccion is not null
        defaultComprobanteFiltering("receptorDireccion.specified=true", "receptorDireccion.specified=false");
    }

    @Test
    @Transactional
    void getAllComprobantesByReceptorDireccionContainsSomething() throws Exception {
        // Initialize the database
        insertedComprobante = comprobanteRepository.saveAndFlush(comprobante);

        // Get all the comprobanteList where receptorDireccion contains
        defaultComprobanteFiltering(
            "receptorDireccion.contains=" + DEFAULT_RECEPTOR_DIRECCION,
            "receptorDireccion.contains=" + UPDATED_RECEPTOR_DIRECCION
        );
    }

    @Test
    @Transactional
    void getAllComprobantesByReceptorDireccionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedComprobante = comprobanteRepository.saveAndFlush(comprobante);

        // Get all the comprobanteList where receptorDireccion does not contain
        defaultComprobanteFiltering(
            "receptorDireccion.doesNotContain=" + UPDATED_RECEPTOR_DIRECCION,
            "receptorDireccion.doesNotContain=" + DEFAULT_RECEPTOR_DIRECCION
        );
    }

    @Test
    @Transactional
    void getAllComprobantesByMonedaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedComprobante = comprobanteRepository.saveAndFlush(comprobante);

        // Get all the comprobanteList where moneda equals to
        defaultComprobanteFiltering("moneda.equals=" + DEFAULT_MONEDA, "moneda.equals=" + UPDATED_MONEDA);
    }

    @Test
    @Transactional
    void getAllComprobantesByMonedaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedComprobante = comprobanteRepository.saveAndFlush(comprobante);

        // Get all the comprobanteList where moneda in
        defaultComprobanteFiltering("moneda.in=" + DEFAULT_MONEDA + "," + UPDATED_MONEDA, "moneda.in=" + UPDATED_MONEDA);
    }

    @Test
    @Transactional
    void getAllComprobantesByMonedaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedComprobante = comprobanteRepository.saveAndFlush(comprobante);

        // Get all the comprobanteList where moneda is not null
        defaultComprobanteFiltering("moneda.specified=true", "moneda.specified=false");
    }

    @Test
    @Transactional
    void getAllComprobantesByOpGravadaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedComprobante = comprobanteRepository.saveAndFlush(comprobante);

        // Get all the comprobanteList where opGravada equals to
        defaultComprobanteFiltering("opGravada.equals=" + DEFAULT_OP_GRAVADA, "opGravada.equals=" + UPDATED_OP_GRAVADA);
    }

    @Test
    @Transactional
    void getAllComprobantesByOpGravadaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedComprobante = comprobanteRepository.saveAndFlush(comprobante);

        // Get all the comprobanteList where opGravada in
        defaultComprobanteFiltering("opGravada.in=" + DEFAULT_OP_GRAVADA + "," + UPDATED_OP_GRAVADA, "opGravada.in=" + UPDATED_OP_GRAVADA);
    }

    @Test
    @Transactional
    void getAllComprobantesByOpGravadaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedComprobante = comprobanteRepository.saveAndFlush(comprobante);

        // Get all the comprobanteList where opGravada is not null
        defaultComprobanteFiltering("opGravada.specified=true", "opGravada.specified=false");
    }

    @Test
    @Transactional
    void getAllComprobantesByOpGravadaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedComprobante = comprobanteRepository.saveAndFlush(comprobante);

        // Get all the comprobanteList where opGravada is greater than or equal to
        defaultComprobanteFiltering(
            "opGravada.greaterThanOrEqual=" + DEFAULT_OP_GRAVADA,
            "opGravada.greaterThanOrEqual=" + UPDATED_OP_GRAVADA
        );
    }

    @Test
    @Transactional
    void getAllComprobantesByOpGravadaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedComprobante = comprobanteRepository.saveAndFlush(comprobante);

        // Get all the comprobanteList where opGravada is less than or equal to
        defaultComprobanteFiltering("opGravada.lessThanOrEqual=" + DEFAULT_OP_GRAVADA, "opGravada.lessThanOrEqual=" + SMALLER_OP_GRAVADA);
    }

    @Test
    @Transactional
    void getAllComprobantesByOpGravadaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedComprobante = comprobanteRepository.saveAndFlush(comprobante);

        // Get all the comprobanteList where opGravada is less than
        defaultComprobanteFiltering("opGravada.lessThan=" + UPDATED_OP_GRAVADA, "opGravada.lessThan=" + DEFAULT_OP_GRAVADA);
    }

    @Test
    @Transactional
    void getAllComprobantesByOpGravadaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedComprobante = comprobanteRepository.saveAndFlush(comprobante);

        // Get all the comprobanteList where opGravada is greater than
        defaultComprobanteFiltering("opGravada.greaterThan=" + SMALLER_OP_GRAVADA, "opGravada.greaterThan=" + DEFAULT_OP_GRAVADA);
    }

    @Test
    @Transactional
    void getAllComprobantesByIgvIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedComprobante = comprobanteRepository.saveAndFlush(comprobante);

        // Get all the comprobanteList where igv equals to
        defaultComprobanteFiltering("igv.equals=" + DEFAULT_IGV, "igv.equals=" + UPDATED_IGV);
    }

    @Test
    @Transactional
    void getAllComprobantesByIgvIsInShouldWork() throws Exception {
        // Initialize the database
        insertedComprobante = comprobanteRepository.saveAndFlush(comprobante);

        // Get all the comprobanteList where igv in
        defaultComprobanteFiltering("igv.in=" + DEFAULT_IGV + "," + UPDATED_IGV, "igv.in=" + UPDATED_IGV);
    }

    @Test
    @Transactional
    void getAllComprobantesByIgvIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedComprobante = comprobanteRepository.saveAndFlush(comprobante);

        // Get all the comprobanteList where igv is not null
        defaultComprobanteFiltering("igv.specified=true", "igv.specified=false");
    }

    @Test
    @Transactional
    void getAllComprobantesByIgvIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedComprobante = comprobanteRepository.saveAndFlush(comprobante);

        // Get all the comprobanteList where igv is greater than or equal to
        defaultComprobanteFiltering("igv.greaterThanOrEqual=" + DEFAULT_IGV, "igv.greaterThanOrEqual=" + UPDATED_IGV);
    }

    @Test
    @Transactional
    void getAllComprobantesByIgvIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedComprobante = comprobanteRepository.saveAndFlush(comprobante);

        // Get all the comprobanteList where igv is less than or equal to
        defaultComprobanteFiltering("igv.lessThanOrEqual=" + DEFAULT_IGV, "igv.lessThanOrEqual=" + SMALLER_IGV);
    }

    @Test
    @Transactional
    void getAllComprobantesByIgvIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedComprobante = comprobanteRepository.saveAndFlush(comprobante);

        // Get all the comprobanteList where igv is less than
        defaultComprobanteFiltering("igv.lessThan=" + UPDATED_IGV, "igv.lessThan=" + DEFAULT_IGV);
    }

    @Test
    @Transactional
    void getAllComprobantesByIgvIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedComprobante = comprobanteRepository.saveAndFlush(comprobante);

        // Get all the comprobanteList where igv is greater than
        defaultComprobanteFiltering("igv.greaterThan=" + SMALLER_IGV, "igv.greaterThan=" + DEFAULT_IGV);
    }

    @Test
    @Transactional
    void getAllComprobantesByTotalIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedComprobante = comprobanteRepository.saveAndFlush(comprobante);

        // Get all the comprobanteList where total equals to
        defaultComprobanteFiltering("total.equals=" + DEFAULT_TOTAL, "total.equals=" + UPDATED_TOTAL);
    }

    @Test
    @Transactional
    void getAllComprobantesByTotalIsInShouldWork() throws Exception {
        // Initialize the database
        insertedComprobante = comprobanteRepository.saveAndFlush(comprobante);

        // Get all the comprobanteList where total in
        defaultComprobanteFiltering("total.in=" + DEFAULT_TOTAL + "," + UPDATED_TOTAL, "total.in=" + UPDATED_TOTAL);
    }

    @Test
    @Transactional
    void getAllComprobantesByTotalIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedComprobante = comprobanteRepository.saveAndFlush(comprobante);

        // Get all the comprobanteList where total is not null
        defaultComprobanteFiltering("total.specified=true", "total.specified=false");
    }

    @Test
    @Transactional
    void getAllComprobantesByTotalIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedComprobante = comprobanteRepository.saveAndFlush(comprobante);

        // Get all the comprobanteList where total is greater than or equal to
        defaultComprobanteFiltering("total.greaterThanOrEqual=" + DEFAULT_TOTAL, "total.greaterThanOrEqual=" + UPDATED_TOTAL);
    }

    @Test
    @Transactional
    void getAllComprobantesByTotalIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedComprobante = comprobanteRepository.saveAndFlush(comprobante);

        // Get all the comprobanteList where total is less than or equal to
        defaultComprobanteFiltering("total.lessThanOrEqual=" + DEFAULT_TOTAL, "total.lessThanOrEqual=" + SMALLER_TOTAL);
    }

    @Test
    @Transactional
    void getAllComprobantesByTotalIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedComprobante = comprobanteRepository.saveAndFlush(comprobante);

        // Get all the comprobanteList where total is less than
        defaultComprobanteFiltering("total.lessThan=" + UPDATED_TOTAL, "total.lessThan=" + DEFAULT_TOTAL);
    }

    @Test
    @Transactional
    void getAllComprobantesByTotalIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedComprobante = comprobanteRepository.saveAndFlush(comprobante);

        // Get all the comprobanteList where total is greater than
        defaultComprobanteFiltering("total.greaterThan=" + SMALLER_TOTAL, "total.greaterThan=" + DEFAULT_TOTAL);
    }

    @Test
    @Transactional
    void getAllComprobantesByEstadoSunatIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedComprobante = comprobanteRepository.saveAndFlush(comprobante);

        // Get all the comprobanteList where estadoSunat equals to
        defaultComprobanteFiltering("estadoSunat.equals=" + DEFAULT_ESTADO_SUNAT, "estadoSunat.equals=" + UPDATED_ESTADO_SUNAT);
    }

    @Test
    @Transactional
    void getAllComprobantesByEstadoSunatIsInShouldWork() throws Exception {
        // Initialize the database
        insertedComprobante = comprobanteRepository.saveAndFlush(comprobante);

        // Get all the comprobanteList where estadoSunat in
        defaultComprobanteFiltering(
            "estadoSunat.in=" + DEFAULT_ESTADO_SUNAT + "," + UPDATED_ESTADO_SUNAT,
            "estadoSunat.in=" + UPDATED_ESTADO_SUNAT
        );
    }

    @Test
    @Transactional
    void getAllComprobantesByEstadoSunatIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedComprobante = comprobanteRepository.saveAndFlush(comprobante);

        // Get all the comprobanteList where estadoSunat is not null
        defaultComprobanteFiltering("estadoSunat.specified=true", "estadoSunat.specified=false");
    }

    @Test
    @Transactional
    void getAllComprobantesByXmlUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedComprobante = comprobanteRepository.saveAndFlush(comprobante);

        // Get all the comprobanteList where xmlUrl equals to
        defaultComprobanteFiltering("xmlUrl.equals=" + DEFAULT_XML_URL, "xmlUrl.equals=" + UPDATED_XML_URL);
    }

    @Test
    @Transactional
    void getAllComprobantesByXmlUrlIsInShouldWork() throws Exception {
        // Initialize the database
        insertedComprobante = comprobanteRepository.saveAndFlush(comprobante);

        // Get all the comprobanteList where xmlUrl in
        defaultComprobanteFiltering("xmlUrl.in=" + DEFAULT_XML_URL + "," + UPDATED_XML_URL, "xmlUrl.in=" + UPDATED_XML_URL);
    }

    @Test
    @Transactional
    void getAllComprobantesByXmlUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedComprobante = comprobanteRepository.saveAndFlush(comprobante);

        // Get all the comprobanteList where xmlUrl is not null
        defaultComprobanteFiltering("xmlUrl.specified=true", "xmlUrl.specified=false");
    }

    @Test
    @Transactional
    void getAllComprobantesByXmlUrlContainsSomething() throws Exception {
        // Initialize the database
        insertedComprobante = comprobanteRepository.saveAndFlush(comprobante);

        // Get all the comprobanteList where xmlUrl contains
        defaultComprobanteFiltering("xmlUrl.contains=" + DEFAULT_XML_URL, "xmlUrl.contains=" + UPDATED_XML_URL);
    }

    @Test
    @Transactional
    void getAllComprobantesByXmlUrlNotContainsSomething() throws Exception {
        // Initialize the database
        insertedComprobante = comprobanteRepository.saveAndFlush(comprobante);

        // Get all the comprobanteList where xmlUrl does not contain
        defaultComprobanteFiltering("xmlUrl.doesNotContain=" + UPDATED_XML_URL, "xmlUrl.doesNotContain=" + DEFAULT_XML_URL);
    }

    @Test
    @Transactional
    void getAllComprobantesByPdfUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedComprobante = comprobanteRepository.saveAndFlush(comprobante);

        // Get all the comprobanteList where pdfUrl equals to
        defaultComprobanteFiltering("pdfUrl.equals=" + DEFAULT_PDF_URL, "pdfUrl.equals=" + UPDATED_PDF_URL);
    }

    @Test
    @Transactional
    void getAllComprobantesByPdfUrlIsInShouldWork() throws Exception {
        // Initialize the database
        insertedComprobante = comprobanteRepository.saveAndFlush(comprobante);

        // Get all the comprobanteList where pdfUrl in
        defaultComprobanteFiltering("pdfUrl.in=" + DEFAULT_PDF_URL + "," + UPDATED_PDF_URL, "pdfUrl.in=" + UPDATED_PDF_URL);
    }

    @Test
    @Transactional
    void getAllComprobantesByPdfUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedComprobante = comprobanteRepository.saveAndFlush(comprobante);

        // Get all the comprobanteList where pdfUrl is not null
        defaultComprobanteFiltering("pdfUrl.specified=true", "pdfUrl.specified=false");
    }

    @Test
    @Transactional
    void getAllComprobantesByPdfUrlContainsSomething() throws Exception {
        // Initialize the database
        insertedComprobante = comprobanteRepository.saveAndFlush(comprobante);

        // Get all the comprobanteList where pdfUrl contains
        defaultComprobanteFiltering("pdfUrl.contains=" + DEFAULT_PDF_URL, "pdfUrl.contains=" + UPDATED_PDF_URL);
    }

    @Test
    @Transactional
    void getAllComprobantesByPdfUrlNotContainsSomething() throws Exception {
        // Initialize the database
        insertedComprobante = comprobanteRepository.saveAndFlush(comprobante);

        // Get all the comprobanteList where pdfUrl does not contain
        defaultComprobanteFiltering("pdfUrl.doesNotContain=" + UPDATED_PDF_URL, "pdfUrl.doesNotContain=" + DEFAULT_PDF_URL);
    }

    @Test
    @Transactional
    void getAllComprobantesByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedComprobante = comprobanteRepository.saveAndFlush(comprobante);

        // Get all the comprobanteList where createdBy equals to
        defaultComprobanteFiltering("createdBy.equals=" + DEFAULT_CREATED_BY, "createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllComprobantesByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedComprobante = comprobanteRepository.saveAndFlush(comprobante);

        // Get all the comprobanteList where createdBy in
        defaultComprobanteFiltering("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY, "createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllComprobantesByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedComprobante = comprobanteRepository.saveAndFlush(comprobante);

        // Get all the comprobanteList where createdBy is not null
        defaultComprobanteFiltering("createdBy.specified=true", "createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllComprobantesByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        insertedComprobante = comprobanteRepository.saveAndFlush(comprobante);

        // Get all the comprobanteList where createdBy contains
        defaultComprobanteFiltering("createdBy.contains=" + DEFAULT_CREATED_BY, "createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllComprobantesByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedComprobante = comprobanteRepository.saveAndFlush(comprobante);

        // Get all the comprobanteList where createdBy does not contain
        defaultComprobanteFiltering("createdBy.doesNotContain=" + UPDATED_CREATED_BY, "createdBy.doesNotContain=" + DEFAULT_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllComprobantesByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedComprobante = comprobanteRepository.saveAndFlush(comprobante);

        // Get all the comprobanteList where createdDate equals to
        defaultComprobanteFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllComprobantesByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedComprobante = comprobanteRepository.saveAndFlush(comprobante);

        // Get all the comprobanteList where createdDate in
        defaultComprobanteFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllComprobantesByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedComprobante = comprobanteRepository.saveAndFlush(comprobante);

        // Get all the comprobanteList where createdDate is not null
        defaultComprobanteFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllComprobantesByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedComprobante = comprobanteRepository.saveAndFlush(comprobante);

        // Get all the comprobanteList where lastModifiedBy equals to
        defaultComprobanteFiltering(
            "lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY,
            "lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllComprobantesByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedComprobante = comprobanteRepository.saveAndFlush(comprobante);

        // Get all the comprobanteList where lastModifiedBy in
        defaultComprobanteFiltering(
            "lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY,
            "lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllComprobantesByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedComprobante = comprobanteRepository.saveAndFlush(comprobante);

        // Get all the comprobanteList where lastModifiedBy is not null
        defaultComprobanteFiltering("lastModifiedBy.specified=true", "lastModifiedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllComprobantesByLastModifiedByContainsSomething() throws Exception {
        // Initialize the database
        insertedComprobante = comprobanteRepository.saveAndFlush(comprobante);

        // Get all the comprobanteList where lastModifiedBy contains
        defaultComprobanteFiltering(
            "lastModifiedBy.contains=" + DEFAULT_LAST_MODIFIED_BY,
            "lastModifiedBy.contains=" + UPDATED_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllComprobantesByLastModifiedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedComprobante = comprobanteRepository.saveAndFlush(comprobante);

        // Get all the comprobanteList where lastModifiedBy does not contain
        defaultComprobanteFiltering(
            "lastModifiedBy.doesNotContain=" + UPDATED_LAST_MODIFIED_BY,
            "lastModifiedBy.doesNotContain=" + DEFAULT_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllComprobantesByLastModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedComprobante = comprobanteRepository.saveAndFlush(comprobante);

        // Get all the comprobanteList where lastModifiedDate equals to
        defaultComprobanteFiltering(
            "lastModifiedDate.equals=" + DEFAULT_LAST_MODIFIED_DATE,
            "lastModifiedDate.equals=" + UPDATED_LAST_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllComprobantesByLastModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedComprobante = comprobanteRepository.saveAndFlush(comprobante);

        // Get all the comprobanteList where lastModifiedDate in
        defaultComprobanteFiltering(
            "lastModifiedDate.in=" + DEFAULT_LAST_MODIFIED_DATE + "," + UPDATED_LAST_MODIFIED_DATE,
            "lastModifiedDate.in=" + UPDATED_LAST_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllComprobantesByLastModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedComprobante = comprobanteRepository.saveAndFlush(comprobante);

        // Get all the comprobanteList where lastModifiedDate is not null
        defaultComprobanteFiltering("lastModifiedDate.specified=true", "lastModifiedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllComprobantesByComprobanteReferenciaIsEqualToSomething() throws Exception {
        Comprobante comprobanteReferencia;
        if (TestUtil.findAll(em, Comprobante.class).isEmpty()) {
            comprobanteRepository.saveAndFlush(comprobante);
            comprobanteReferencia = ComprobanteResourceIT.createEntity(em);
        } else {
            comprobanteReferencia = TestUtil.findAll(em, Comprobante.class).getFirst();
        }
        em.persist(comprobanteReferencia);
        em.flush();
        comprobante.setComprobanteReferencia(comprobanteReferencia);
        comprobanteRepository.saveAndFlush(comprobante);
        Long comprobanteReferenciaId = comprobanteReferencia.getId();
        // Get all the comprobanteList where comprobanteReferencia equals to comprobanteReferenciaId
        defaultComprobanteShouldBeFound("comprobanteReferenciaId.equals=" + comprobanteReferenciaId);

        // Get all the comprobanteList where comprobanteReferencia equals to (comprobanteReferenciaId + 1)
        defaultComprobanteShouldNotBeFound("comprobanteReferenciaId.equals=" + (comprobanteReferenciaId + 1));
    }

    @Test
    @Transactional
    void getAllComprobantesByPedidoIsEqualToSomething() throws Exception {
        Pedido pedido;
        if (TestUtil.findAll(em, Pedido.class).isEmpty()) {
            comprobanteRepository.saveAndFlush(comprobante);
            pedido = PedidoResourceIT.createEntity(em);
        } else {
            pedido = TestUtil.findAll(em, Pedido.class).getFirst();
        }
        em.persist(pedido);
        em.flush();
        comprobante.setPedido(pedido);
        comprobanteRepository.saveAndFlush(comprobante);
        Long pedidoId = pedido.getId();
        // Get all the comprobanteList where pedido equals to pedidoId
        defaultComprobanteShouldBeFound("pedidoId.equals=" + pedidoId);

        // Get all the comprobanteList where pedido equals to (pedidoId + 1)
        defaultComprobanteShouldNotBeFound("pedidoId.equals=" + (pedidoId + 1));
    }

    private void defaultComprobanteFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultComprobanteShouldBeFound(shouldBeFound);
        defaultComprobanteShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultComprobanteShouldBeFound(String filter) throws Exception {
        restComprobanteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(comprobante.getId().intValue())))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO.toString())))
            .andExpect(jsonPath("$.[*].serie").value(hasItem(DEFAULT_SERIE)))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO)))
            .andExpect(jsonPath("$.[*].fechaEmision").value(hasItem(DEFAULT_FECHA_EMISION.toString())))
            .andExpect(jsonPath("$.[*].receptorTipoDocumento").value(hasItem(DEFAULT_RECEPTOR_TIPO_DOCUMENTO)))
            .andExpect(jsonPath("$.[*].receptorNroDocumento").value(hasItem(DEFAULT_RECEPTOR_NRO_DOCUMENTO)))
            .andExpect(jsonPath("$.[*].receptorNombre").value(hasItem(DEFAULT_RECEPTOR_NOMBRE)))
            .andExpect(jsonPath("$.[*].receptorDireccion").value(hasItem(DEFAULT_RECEPTOR_DIRECCION)))
            .andExpect(jsonPath("$.[*].moneda").value(hasItem(DEFAULT_MONEDA.toString())))
            .andExpect(jsonPath("$.[*].opGravada").value(hasItem(sameNumber(DEFAULT_OP_GRAVADA))))
            .andExpect(jsonPath("$.[*].igv").value(hasItem(sameNumber(DEFAULT_IGV))))
            .andExpect(jsonPath("$.[*].total").value(hasItem(sameNumber(DEFAULT_TOTAL))))
            .andExpect(jsonPath("$.[*].estadoSunat").value(hasItem(DEFAULT_ESTADO_SUNAT.toString())))
            .andExpect(jsonPath("$.[*].xmlUrl").value(hasItem(DEFAULT_XML_URL)))
            .andExpect(jsonPath("$.[*].pdfUrl").value(hasItem(DEFAULT_PDF_URL)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));

        // Check, that the count call also returns 1
        restComprobanteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultComprobanteShouldNotBeFound(String filter) throws Exception {
        restComprobanteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restComprobanteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingComprobante() throws Exception {
        // Get the comprobante
        restComprobanteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingComprobante() throws Exception {
        // Initialize the database
        insertedComprobante = comprobanteRepository.saveAndFlush(comprobante);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the comprobante
        Comprobante updatedComprobante = comprobanteRepository.findById(comprobante.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedComprobante are not directly saved in db
        em.detach(updatedComprobante);
        updatedComprobante
            .tipo(UPDATED_TIPO)
            .serie(UPDATED_SERIE)
            .numero(UPDATED_NUMERO)
            .fechaEmision(UPDATED_FECHA_EMISION)
            .receptorTipoDocumento(UPDATED_RECEPTOR_TIPO_DOCUMENTO)
            .receptorNroDocumento(UPDATED_RECEPTOR_NRO_DOCUMENTO)
            .receptorNombre(UPDATED_RECEPTOR_NOMBRE)
            .receptorDireccion(UPDATED_RECEPTOR_DIRECCION)
            .moneda(UPDATED_MONEDA)
            .opGravada(UPDATED_OP_GRAVADA)
            .igv(UPDATED_IGV)
            .total(UPDATED_TOTAL)
            .estadoSunat(UPDATED_ESTADO_SUNAT)
            .xmlUrl(UPDATED_XML_URL)
            .pdfUrl(UPDATED_PDF_URL)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        ComprobanteDTO comprobanteDTO = comprobanteMapper.toDto(updatedComprobante);

        restComprobanteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, comprobanteDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(comprobanteDTO))
            )
            .andExpect(status().isOk());

        // Validate the Comprobante in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedComprobanteToMatchAllProperties(updatedComprobante);
    }

    @Test
    @Transactional
    void putNonExistingComprobante() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        comprobante.setId(longCount.incrementAndGet());

        // Create the Comprobante
        ComprobanteDTO comprobanteDTO = comprobanteMapper.toDto(comprobante);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restComprobanteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, comprobanteDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(comprobanteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Comprobante in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchComprobante() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        comprobante.setId(longCount.incrementAndGet());

        // Create the Comprobante
        ComprobanteDTO comprobanteDTO = comprobanteMapper.toDto(comprobante);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restComprobanteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(comprobanteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Comprobante in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamComprobante() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        comprobante.setId(longCount.incrementAndGet());

        // Create the Comprobante
        ComprobanteDTO comprobanteDTO = comprobanteMapper.toDto(comprobante);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restComprobanteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(comprobanteDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Comprobante in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateComprobanteWithPatch() throws Exception {
        // Initialize the database
        insertedComprobante = comprobanteRepository.saveAndFlush(comprobante);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the comprobante using partial update
        Comprobante partialUpdatedComprobante = new Comprobante();
        partialUpdatedComprobante.setId(comprobante.getId());

        partialUpdatedComprobante
            .tipo(UPDATED_TIPO)
            .serie(UPDATED_SERIE)
            .fechaEmision(UPDATED_FECHA_EMISION)
            .receptorTipoDocumento(UPDATED_RECEPTOR_TIPO_DOCUMENTO)
            .moneda(UPDATED_MONEDA)
            .total(UPDATED_TOTAL)
            .estadoSunat(UPDATED_ESTADO_SUNAT)
            .pdfUrl(UPDATED_PDF_URL)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);

        restComprobanteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedComprobante.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedComprobante))
            )
            .andExpect(status().isOk());

        // Validate the Comprobante in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertComprobanteUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedComprobante, comprobante),
            getPersistedComprobante(comprobante)
        );
    }

    @Test
    @Transactional
    void fullUpdateComprobanteWithPatch() throws Exception {
        // Initialize the database
        insertedComprobante = comprobanteRepository.saveAndFlush(comprobante);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the comprobante using partial update
        Comprobante partialUpdatedComprobante = new Comprobante();
        partialUpdatedComprobante.setId(comprobante.getId());

        partialUpdatedComprobante
            .tipo(UPDATED_TIPO)
            .serie(UPDATED_SERIE)
            .numero(UPDATED_NUMERO)
            .fechaEmision(UPDATED_FECHA_EMISION)
            .receptorTipoDocumento(UPDATED_RECEPTOR_TIPO_DOCUMENTO)
            .receptorNroDocumento(UPDATED_RECEPTOR_NRO_DOCUMENTO)
            .receptorNombre(UPDATED_RECEPTOR_NOMBRE)
            .receptorDireccion(UPDATED_RECEPTOR_DIRECCION)
            .moneda(UPDATED_MONEDA)
            .opGravada(UPDATED_OP_GRAVADA)
            .igv(UPDATED_IGV)
            .total(UPDATED_TOTAL)
            .estadoSunat(UPDATED_ESTADO_SUNAT)
            .xmlUrl(UPDATED_XML_URL)
            .pdfUrl(UPDATED_PDF_URL)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restComprobanteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedComprobante.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedComprobante))
            )
            .andExpect(status().isOk());

        // Validate the Comprobante in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertComprobanteUpdatableFieldsEquals(partialUpdatedComprobante, getPersistedComprobante(partialUpdatedComprobante));
    }

    @Test
    @Transactional
    void patchNonExistingComprobante() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        comprobante.setId(longCount.incrementAndGet());

        // Create the Comprobante
        ComprobanteDTO comprobanteDTO = comprobanteMapper.toDto(comprobante);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restComprobanteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, comprobanteDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(comprobanteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Comprobante in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchComprobante() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        comprobante.setId(longCount.incrementAndGet());

        // Create the Comprobante
        ComprobanteDTO comprobanteDTO = comprobanteMapper.toDto(comprobante);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restComprobanteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(comprobanteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Comprobante in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamComprobante() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        comprobante.setId(longCount.incrementAndGet());

        // Create the Comprobante
        ComprobanteDTO comprobanteDTO = comprobanteMapper.toDto(comprobante);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restComprobanteMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(comprobanteDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Comprobante in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteComprobante() throws Exception {
        // Initialize the database
        insertedComprobante = comprobanteRepository.saveAndFlush(comprobante);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the comprobante
        restComprobanteMockMvc
            .perform(delete(ENTITY_API_URL_ID, comprobante.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return comprobanteRepository.count();
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

    protected Comprobante getPersistedComprobante(Comprobante comprobante) {
        return comprobanteRepository.findById(comprobante.getId()).orElseThrow();
    }

    protected void assertPersistedComprobanteToMatchAllProperties(Comprobante expectedComprobante) {
        assertComprobanteAllPropertiesEquals(expectedComprobante, getPersistedComprobante(expectedComprobante));
    }

    protected void assertPersistedComprobanteToMatchUpdatableProperties(Comprobante expectedComprobante) {
        assertComprobanteAllUpdatablePropertiesEquals(expectedComprobante, getPersistedComprobante(expectedComprobante));
    }
}
