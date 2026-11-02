package pe.commerce.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pe.commerce.app.domain.PedidoAsserts.*;
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
import pe.commerce.app.domain.Cliente;
import pe.commerce.app.domain.Cupon;
import pe.commerce.app.domain.Empleado;
import pe.commerce.app.domain.Pedido;
import pe.commerce.app.domain.Tienda;
import pe.commerce.app.domain.enumeration.EstadoPedido;
import pe.commerce.app.domain.enumeration.Moneda;
import pe.commerce.app.domain.enumeration.TipoComprobantePedido;
import pe.commerce.app.domain.enumeration.TipoEntrega;
import pe.commerce.app.repository.PedidoRepository;
import pe.commerce.app.service.PedidoService;
import pe.commerce.app.service.dto.PedidoDTO;
import pe.commerce.app.service.mapper.PedidoMapper;
import tools.jackson.databind.ObjectMapper;

/**
 * Integration tests for the {@link PedidoResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PedidoResourceIT {

    private static final String DEFAULT_NRO_PEDIDO = "AAAAAAAAAA";
    private static final String UPDATED_NRO_PEDIDO = "BBBBBBBBBB";

    private static final TipoEntrega DEFAULT_TIPO_ENTREGA = TipoEntrega.DELIVERY;
    private static final TipoEntrega UPDATED_TIPO_ENTREGA = TipoEntrega.RECOJO_TIENDA;

    private static final TipoComprobantePedido DEFAULT_TIPO_COMPROBANTE = TipoComprobantePedido.BOLETA;
    private static final TipoComprobantePedido UPDATED_TIPO_COMPROBANTE = TipoComprobantePedido.FACTURA;

    private static final EstadoPedido DEFAULT_ESTADO = EstadoPedido.PENDIENTE_PAGO;
    private static final EstadoPedido UPDATED_ESTADO = EstadoPedido.PAGADO;

    private static final Moneda DEFAULT_MONEDA = Moneda.PEN;
    private static final Moneda UPDATED_MONEDA = Moneda.USD;

    private static final BigDecimal DEFAULT_SUBTOTAL = new BigDecimal(0);
    private static final BigDecimal UPDATED_SUBTOTAL = new BigDecimal(1);
    private static final BigDecimal SMALLER_SUBTOTAL = new BigDecimal(0 - 1);

    private static final BigDecimal DEFAULT_DESCUENTO = new BigDecimal(0);
    private static final BigDecimal UPDATED_DESCUENTO = new BigDecimal(1);
    private static final BigDecimal SMALLER_DESCUENTO = new BigDecimal(0 - 1);

    private static final BigDecimal DEFAULT_COSTO_ENVIO = new BigDecimal(0);
    private static final BigDecimal UPDATED_COSTO_ENVIO = new BigDecimal(1);
    private static final BigDecimal SMALLER_COSTO_ENVIO = new BigDecimal(0 - 1);

    private static final BigDecimal DEFAULT_IGV = new BigDecimal(0);
    private static final BigDecimal UPDATED_IGV = new BigDecimal(1);
    private static final BigDecimal SMALLER_IGV = new BigDecimal(0 - 1);

    private static final BigDecimal DEFAULT_TOTAL = new BigDecimal(0);
    private static final BigDecimal UPDATED_TOTAL = new BigDecimal(1);
    private static final BigDecimal SMALLER_TOTAL = new BigDecimal(0 - 1);

    private static final Instant DEFAULT_FECHA_PEDIDO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_PEDIDO = Instant.ofEpochMilli(1702246707543L);

    private static final Instant DEFAULT_FECHA_EXPIRACION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_EXPIRACION = Instant.ofEpochMilli(1702246707543L);

    private static final String DEFAULT_OBSERVACIONES = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVACIONES = "BBBBBBBBBB";

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.ofEpochMilli(1702246707543L);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.ofEpochMilli(1702246707543L);

    private static final String ENTITY_API_URL = "/api/pedidos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Mock
    private PedidoRepository pedidoRepositoryMock;

    @Autowired
    private PedidoMapper pedidoMapper;

    @Mock
    private PedidoService pedidoServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPedidoMockMvc;

    private Pedido pedido;

    private Pedido insertedPedido;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pedido createEntity(EntityManager em) {
        Pedido pedido = new Pedido()
            .nroPedido(DEFAULT_NRO_PEDIDO)
            .tipoEntrega(DEFAULT_TIPO_ENTREGA)
            .tipoComprobante(DEFAULT_TIPO_COMPROBANTE)
            .estado(DEFAULT_ESTADO)
            .moneda(DEFAULT_MONEDA)
            .subtotal(DEFAULT_SUBTOTAL)
            .descuento(DEFAULT_DESCUENTO)
            .costoEnvio(DEFAULT_COSTO_ENVIO)
            .igv(DEFAULT_IGV)
            .total(DEFAULT_TOTAL)
            .fechaPedido(DEFAULT_FECHA_PEDIDO)
            .fechaExpiracion(DEFAULT_FECHA_EXPIRACION)
            .observaciones(DEFAULT_OBSERVACIONES)
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
        pedido.setCliente(cliente);
        return pedido;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pedido createUpdatedEntity(EntityManager em) {
        Pedido updatedPedido = new Pedido()
            .nroPedido(UPDATED_NRO_PEDIDO)
            .tipoEntrega(UPDATED_TIPO_ENTREGA)
            .tipoComprobante(UPDATED_TIPO_COMPROBANTE)
            .estado(UPDATED_ESTADO)
            .moneda(UPDATED_MONEDA)
            .subtotal(UPDATED_SUBTOTAL)
            .descuento(UPDATED_DESCUENTO)
            .costoEnvio(UPDATED_COSTO_ENVIO)
            .igv(UPDATED_IGV)
            .total(UPDATED_TOTAL)
            .fechaPedido(UPDATED_FECHA_PEDIDO)
            .fechaExpiracion(UPDATED_FECHA_EXPIRACION)
            .observaciones(UPDATED_OBSERVACIONES)
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
        updatedPedido.setCliente(cliente);
        return updatedPedido;
    }

    @BeforeEach
    void initTest() {
        pedido = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedPedido != null) {
            pedidoRepository.delete(insertedPedido);
            insertedPedido = null;
        }
    }

    @Test
    @Transactional
    void createPedido() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Pedido
        PedidoDTO pedidoDTO = pedidoMapper.toDto(pedido);
        var returnedPedidoDTO = om.readValue(
            restPedidoMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pedidoDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PedidoDTO.class
        );

        // Validate the Pedido in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPedido = pedidoMapper.toEntity(returnedPedidoDTO);
        assertPedidoUpdatableFieldsEquals(returnedPedido, getPersistedPedido(returnedPedido));

        insertedPedido = returnedPedido;
    }

    @Test
    @Transactional
    void createPedidoWithExistingId() throws Exception {
        // Create the Pedido with an existing ID
        pedido.setId(1L);
        PedidoDTO pedidoDTO = pedidoMapper.toDto(pedido);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPedidoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pedidoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Pedido in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNroPedidoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        pedido.setNroPedido(null);

        // Create the Pedido, which fails.
        PedidoDTO pedidoDTO = pedidoMapper.toDto(pedido);

        restPedidoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pedidoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTipoEntregaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        pedido.setTipoEntrega(null);

        // Create the Pedido, which fails.
        PedidoDTO pedidoDTO = pedidoMapper.toDto(pedido);

        restPedidoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pedidoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTipoComprobanteIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        pedido.setTipoComprobante(null);

        // Create the Pedido, which fails.
        PedidoDTO pedidoDTO = pedidoMapper.toDto(pedido);

        restPedidoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pedidoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEstadoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        pedido.setEstado(null);

        // Create the Pedido, which fails.
        PedidoDTO pedidoDTO = pedidoMapper.toDto(pedido);

        restPedidoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pedidoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMonedaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        pedido.setMoneda(null);

        // Create the Pedido, which fails.
        PedidoDTO pedidoDTO = pedidoMapper.toDto(pedido);

        restPedidoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pedidoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSubtotalIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        pedido.setSubtotal(null);

        // Create the Pedido, which fails.
        PedidoDTO pedidoDTO = pedidoMapper.toDto(pedido);

        restPedidoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pedidoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescuentoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        pedido.setDescuento(null);

        // Create the Pedido, which fails.
        PedidoDTO pedidoDTO = pedidoMapper.toDto(pedido);

        restPedidoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pedidoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCostoEnvioIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        pedido.setCostoEnvio(null);

        // Create the Pedido, which fails.
        PedidoDTO pedidoDTO = pedidoMapper.toDto(pedido);

        restPedidoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pedidoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIgvIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        pedido.setIgv(null);

        // Create the Pedido, which fails.
        PedidoDTO pedidoDTO = pedidoMapper.toDto(pedido);

        restPedidoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pedidoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTotalIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        pedido.setTotal(null);

        // Create the Pedido, which fails.
        PedidoDTO pedidoDTO = pedidoMapper.toDto(pedido);

        restPedidoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pedidoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFechaPedidoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        pedido.setFechaPedido(null);

        // Create the Pedido, which fails.
        PedidoDTO pedidoDTO = pedidoMapper.toDto(pedido);

        restPedidoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pedidoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFechaExpiracionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        pedido.setFechaExpiracion(null);

        // Create the Pedido, which fails.
        PedidoDTO pedidoDTO = pedidoMapper.toDto(pedido);

        restPedidoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pedidoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPedidos() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList
        restPedidoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pedido.getId().intValue())))
            .andExpect(jsonPath("$.[*].nroPedido").value(hasItem(DEFAULT_NRO_PEDIDO)))
            .andExpect(jsonPath("$.[*].tipoEntrega").value(hasItem(DEFAULT_TIPO_ENTREGA.toString())))
            .andExpect(jsonPath("$.[*].tipoComprobante").value(hasItem(DEFAULT_TIPO_COMPROBANTE.toString())))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO.toString())))
            .andExpect(jsonPath("$.[*].moneda").value(hasItem(DEFAULT_MONEDA.toString())))
            .andExpect(jsonPath("$.[*].subtotal").value(hasItem(sameNumber(DEFAULT_SUBTOTAL))))
            .andExpect(jsonPath("$.[*].descuento").value(hasItem(sameNumber(DEFAULT_DESCUENTO))))
            .andExpect(jsonPath("$.[*].costoEnvio").value(hasItem(sameNumber(DEFAULT_COSTO_ENVIO))))
            .andExpect(jsonPath("$.[*].igv").value(hasItem(sameNumber(DEFAULT_IGV))))
            .andExpect(jsonPath("$.[*].total").value(hasItem(sameNumber(DEFAULT_TOTAL))))
            .andExpect(jsonPath("$.[*].fechaPedido").value(hasItem(DEFAULT_FECHA_PEDIDO.toString())))
            .andExpect(jsonPath("$.[*].fechaExpiracion").value(hasItem(DEFAULT_FECHA_EXPIRACION.toString())))
            .andExpect(jsonPath("$.[*].observaciones").value(hasItem(DEFAULT_OBSERVACIONES)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPedidosWithEagerRelationshipsIsEnabled() throws Exception {
        when(pedidoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPedidoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(pedidoServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPedidosWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(pedidoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPedidoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(pedidoRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getPedido() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        // Get the pedido
        restPedidoMockMvc
            .perform(get(ENTITY_API_URL_ID, pedido.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pedido.getId().intValue()))
            .andExpect(jsonPath("$.nroPedido").value(DEFAULT_NRO_PEDIDO))
            .andExpect(jsonPath("$.tipoEntrega").value(DEFAULT_TIPO_ENTREGA.toString()))
            .andExpect(jsonPath("$.tipoComprobante").value(DEFAULT_TIPO_COMPROBANTE.toString()))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO.toString()))
            .andExpect(jsonPath("$.moneda").value(DEFAULT_MONEDA.toString()))
            .andExpect(jsonPath("$.subtotal").value(sameNumber(DEFAULT_SUBTOTAL)))
            .andExpect(jsonPath("$.descuento").value(sameNumber(DEFAULT_DESCUENTO)))
            .andExpect(jsonPath("$.costoEnvio").value(sameNumber(DEFAULT_COSTO_ENVIO)))
            .andExpect(jsonPath("$.igv").value(sameNumber(DEFAULT_IGV)))
            .andExpect(jsonPath("$.total").value(sameNumber(DEFAULT_TOTAL)))
            .andExpect(jsonPath("$.fechaPedido").value(DEFAULT_FECHA_PEDIDO.toString()))
            .andExpect(jsonPath("$.fechaExpiracion").value(DEFAULT_FECHA_EXPIRACION.toString()))
            .andExpect(jsonPath("$.observaciones").value(DEFAULT_OBSERVACIONES))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getPedidosByIdFiltering() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        Long id = pedido.getId();

        defaultPedidoFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultPedidoFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultPedidoFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPedidosByNroPedidoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where nroPedido equals to
        defaultPedidoFiltering("nroPedido.equals=" + DEFAULT_NRO_PEDIDO, "nroPedido.equals=" + UPDATED_NRO_PEDIDO);
    }

    @Test
    @Transactional
    void getAllPedidosByNroPedidoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where nroPedido in
        defaultPedidoFiltering("nroPedido.in=" + DEFAULT_NRO_PEDIDO + "," + UPDATED_NRO_PEDIDO, "nroPedido.in=" + UPDATED_NRO_PEDIDO);
    }

    @Test
    @Transactional
    void getAllPedidosByNroPedidoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where nroPedido is not null
        defaultPedidoFiltering("nroPedido.specified=true", "nroPedido.specified=false");
    }

    @Test
    @Transactional
    void getAllPedidosByNroPedidoContainsSomething() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where nroPedido contains
        defaultPedidoFiltering("nroPedido.contains=" + DEFAULT_NRO_PEDIDO, "nroPedido.contains=" + UPDATED_NRO_PEDIDO);
    }

    @Test
    @Transactional
    void getAllPedidosByNroPedidoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where nroPedido does not contain
        defaultPedidoFiltering("nroPedido.doesNotContain=" + UPDATED_NRO_PEDIDO, "nroPedido.doesNotContain=" + DEFAULT_NRO_PEDIDO);
    }

    @Test
    @Transactional
    void getAllPedidosByTipoEntregaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where tipoEntrega equals to
        defaultPedidoFiltering("tipoEntrega.equals=" + DEFAULT_TIPO_ENTREGA, "tipoEntrega.equals=" + UPDATED_TIPO_ENTREGA);
    }

    @Test
    @Transactional
    void getAllPedidosByTipoEntregaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where tipoEntrega in
        defaultPedidoFiltering(
            "tipoEntrega.in=" + DEFAULT_TIPO_ENTREGA + "," + UPDATED_TIPO_ENTREGA,
            "tipoEntrega.in=" + UPDATED_TIPO_ENTREGA
        );
    }

    @Test
    @Transactional
    void getAllPedidosByTipoEntregaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where tipoEntrega is not null
        defaultPedidoFiltering("tipoEntrega.specified=true", "tipoEntrega.specified=false");
    }

    @Test
    @Transactional
    void getAllPedidosByTipoComprobanteIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where tipoComprobante equals to
        defaultPedidoFiltering("tipoComprobante.equals=" + DEFAULT_TIPO_COMPROBANTE, "tipoComprobante.equals=" + UPDATED_TIPO_COMPROBANTE);
    }

    @Test
    @Transactional
    void getAllPedidosByTipoComprobanteIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where tipoComprobante in
        defaultPedidoFiltering(
            "tipoComprobante.in=" + DEFAULT_TIPO_COMPROBANTE + "," + UPDATED_TIPO_COMPROBANTE,
            "tipoComprobante.in=" + UPDATED_TIPO_COMPROBANTE
        );
    }

    @Test
    @Transactional
    void getAllPedidosByTipoComprobanteIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where tipoComprobante is not null
        defaultPedidoFiltering("tipoComprobante.specified=true", "tipoComprobante.specified=false");
    }

    @Test
    @Transactional
    void getAllPedidosByEstadoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where estado equals to
        defaultPedidoFiltering("estado.equals=" + DEFAULT_ESTADO, "estado.equals=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllPedidosByEstadoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where estado in
        defaultPedidoFiltering("estado.in=" + DEFAULT_ESTADO + "," + UPDATED_ESTADO, "estado.in=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllPedidosByEstadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where estado is not null
        defaultPedidoFiltering("estado.specified=true", "estado.specified=false");
    }

    @Test
    @Transactional
    void getAllPedidosByMonedaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where moneda equals to
        defaultPedidoFiltering("moneda.equals=" + DEFAULT_MONEDA, "moneda.equals=" + UPDATED_MONEDA);
    }

    @Test
    @Transactional
    void getAllPedidosByMonedaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where moneda in
        defaultPedidoFiltering("moneda.in=" + DEFAULT_MONEDA + "," + UPDATED_MONEDA, "moneda.in=" + UPDATED_MONEDA);
    }

    @Test
    @Transactional
    void getAllPedidosByMonedaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where moneda is not null
        defaultPedidoFiltering("moneda.specified=true", "moneda.specified=false");
    }

    @Test
    @Transactional
    void getAllPedidosBySubtotalIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where subtotal equals to
        defaultPedidoFiltering("subtotal.equals=" + DEFAULT_SUBTOTAL, "subtotal.equals=" + UPDATED_SUBTOTAL);
    }

    @Test
    @Transactional
    void getAllPedidosBySubtotalIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where subtotal in
        defaultPedidoFiltering("subtotal.in=" + DEFAULT_SUBTOTAL + "," + UPDATED_SUBTOTAL, "subtotal.in=" + UPDATED_SUBTOTAL);
    }

    @Test
    @Transactional
    void getAllPedidosBySubtotalIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where subtotal is not null
        defaultPedidoFiltering("subtotal.specified=true", "subtotal.specified=false");
    }

    @Test
    @Transactional
    void getAllPedidosBySubtotalIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where subtotal is greater than or equal to
        defaultPedidoFiltering("subtotal.greaterThanOrEqual=" + DEFAULT_SUBTOTAL, "subtotal.greaterThanOrEqual=" + UPDATED_SUBTOTAL);
    }

    @Test
    @Transactional
    void getAllPedidosBySubtotalIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where subtotal is less than or equal to
        defaultPedidoFiltering("subtotal.lessThanOrEqual=" + DEFAULT_SUBTOTAL, "subtotal.lessThanOrEqual=" + SMALLER_SUBTOTAL);
    }

    @Test
    @Transactional
    void getAllPedidosBySubtotalIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where subtotal is less than
        defaultPedidoFiltering("subtotal.lessThan=" + UPDATED_SUBTOTAL, "subtotal.lessThan=" + DEFAULT_SUBTOTAL);
    }

    @Test
    @Transactional
    void getAllPedidosBySubtotalIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where subtotal is greater than
        defaultPedidoFiltering("subtotal.greaterThan=" + SMALLER_SUBTOTAL, "subtotal.greaterThan=" + DEFAULT_SUBTOTAL);
    }

    @Test
    @Transactional
    void getAllPedidosByDescuentoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where descuento equals to
        defaultPedidoFiltering("descuento.equals=" + DEFAULT_DESCUENTO, "descuento.equals=" + UPDATED_DESCUENTO);
    }

    @Test
    @Transactional
    void getAllPedidosByDescuentoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where descuento in
        defaultPedidoFiltering("descuento.in=" + DEFAULT_DESCUENTO + "," + UPDATED_DESCUENTO, "descuento.in=" + UPDATED_DESCUENTO);
    }

    @Test
    @Transactional
    void getAllPedidosByDescuentoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where descuento is not null
        defaultPedidoFiltering("descuento.specified=true", "descuento.specified=false");
    }

    @Test
    @Transactional
    void getAllPedidosByDescuentoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where descuento is greater than or equal to
        defaultPedidoFiltering("descuento.greaterThanOrEqual=" + DEFAULT_DESCUENTO, "descuento.greaterThanOrEqual=" + UPDATED_DESCUENTO);
    }

    @Test
    @Transactional
    void getAllPedidosByDescuentoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where descuento is less than or equal to
        defaultPedidoFiltering("descuento.lessThanOrEqual=" + DEFAULT_DESCUENTO, "descuento.lessThanOrEqual=" + SMALLER_DESCUENTO);
    }

    @Test
    @Transactional
    void getAllPedidosByDescuentoIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where descuento is less than
        defaultPedidoFiltering("descuento.lessThan=" + UPDATED_DESCUENTO, "descuento.lessThan=" + DEFAULT_DESCUENTO);
    }

    @Test
    @Transactional
    void getAllPedidosByDescuentoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where descuento is greater than
        defaultPedidoFiltering("descuento.greaterThan=" + SMALLER_DESCUENTO, "descuento.greaterThan=" + DEFAULT_DESCUENTO);
    }

    @Test
    @Transactional
    void getAllPedidosByCostoEnvioIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where costoEnvio equals to
        defaultPedidoFiltering("costoEnvio.equals=" + DEFAULT_COSTO_ENVIO, "costoEnvio.equals=" + UPDATED_COSTO_ENVIO);
    }

    @Test
    @Transactional
    void getAllPedidosByCostoEnvioIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where costoEnvio in
        defaultPedidoFiltering("costoEnvio.in=" + DEFAULT_COSTO_ENVIO + "," + UPDATED_COSTO_ENVIO, "costoEnvio.in=" + UPDATED_COSTO_ENVIO);
    }

    @Test
    @Transactional
    void getAllPedidosByCostoEnvioIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where costoEnvio is not null
        defaultPedidoFiltering("costoEnvio.specified=true", "costoEnvio.specified=false");
    }

    @Test
    @Transactional
    void getAllPedidosByCostoEnvioIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where costoEnvio is greater than or equal to
        defaultPedidoFiltering(
            "costoEnvio.greaterThanOrEqual=" + DEFAULT_COSTO_ENVIO,
            "costoEnvio.greaterThanOrEqual=" + UPDATED_COSTO_ENVIO
        );
    }

    @Test
    @Transactional
    void getAllPedidosByCostoEnvioIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where costoEnvio is less than or equal to
        defaultPedidoFiltering("costoEnvio.lessThanOrEqual=" + DEFAULT_COSTO_ENVIO, "costoEnvio.lessThanOrEqual=" + SMALLER_COSTO_ENVIO);
    }

    @Test
    @Transactional
    void getAllPedidosByCostoEnvioIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where costoEnvio is less than
        defaultPedidoFiltering("costoEnvio.lessThan=" + UPDATED_COSTO_ENVIO, "costoEnvio.lessThan=" + DEFAULT_COSTO_ENVIO);
    }

    @Test
    @Transactional
    void getAllPedidosByCostoEnvioIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where costoEnvio is greater than
        defaultPedidoFiltering("costoEnvio.greaterThan=" + SMALLER_COSTO_ENVIO, "costoEnvio.greaterThan=" + DEFAULT_COSTO_ENVIO);
    }

    @Test
    @Transactional
    void getAllPedidosByIgvIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where igv equals to
        defaultPedidoFiltering("igv.equals=" + DEFAULT_IGV, "igv.equals=" + UPDATED_IGV);
    }

    @Test
    @Transactional
    void getAllPedidosByIgvIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where igv in
        defaultPedidoFiltering("igv.in=" + DEFAULT_IGV + "," + UPDATED_IGV, "igv.in=" + UPDATED_IGV);
    }

    @Test
    @Transactional
    void getAllPedidosByIgvIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where igv is not null
        defaultPedidoFiltering("igv.specified=true", "igv.specified=false");
    }

    @Test
    @Transactional
    void getAllPedidosByIgvIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where igv is greater than or equal to
        defaultPedidoFiltering("igv.greaterThanOrEqual=" + DEFAULT_IGV, "igv.greaterThanOrEqual=" + UPDATED_IGV);
    }

    @Test
    @Transactional
    void getAllPedidosByIgvIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where igv is less than or equal to
        defaultPedidoFiltering("igv.lessThanOrEqual=" + DEFAULT_IGV, "igv.lessThanOrEqual=" + SMALLER_IGV);
    }

    @Test
    @Transactional
    void getAllPedidosByIgvIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where igv is less than
        defaultPedidoFiltering("igv.lessThan=" + UPDATED_IGV, "igv.lessThan=" + DEFAULT_IGV);
    }

    @Test
    @Transactional
    void getAllPedidosByIgvIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where igv is greater than
        defaultPedidoFiltering("igv.greaterThan=" + SMALLER_IGV, "igv.greaterThan=" + DEFAULT_IGV);
    }

    @Test
    @Transactional
    void getAllPedidosByTotalIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where total equals to
        defaultPedidoFiltering("total.equals=" + DEFAULT_TOTAL, "total.equals=" + UPDATED_TOTAL);
    }

    @Test
    @Transactional
    void getAllPedidosByTotalIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where total in
        defaultPedidoFiltering("total.in=" + DEFAULT_TOTAL + "," + UPDATED_TOTAL, "total.in=" + UPDATED_TOTAL);
    }

    @Test
    @Transactional
    void getAllPedidosByTotalIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where total is not null
        defaultPedidoFiltering("total.specified=true", "total.specified=false");
    }

    @Test
    @Transactional
    void getAllPedidosByTotalIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where total is greater than or equal to
        defaultPedidoFiltering("total.greaterThanOrEqual=" + DEFAULT_TOTAL, "total.greaterThanOrEqual=" + UPDATED_TOTAL);
    }

    @Test
    @Transactional
    void getAllPedidosByTotalIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where total is less than or equal to
        defaultPedidoFiltering("total.lessThanOrEqual=" + DEFAULT_TOTAL, "total.lessThanOrEqual=" + SMALLER_TOTAL);
    }

    @Test
    @Transactional
    void getAllPedidosByTotalIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where total is less than
        defaultPedidoFiltering("total.lessThan=" + UPDATED_TOTAL, "total.lessThan=" + DEFAULT_TOTAL);
    }

    @Test
    @Transactional
    void getAllPedidosByTotalIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where total is greater than
        defaultPedidoFiltering("total.greaterThan=" + SMALLER_TOTAL, "total.greaterThan=" + DEFAULT_TOTAL);
    }

    @Test
    @Transactional
    void getAllPedidosByFechaPedidoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where fechaPedido equals to
        defaultPedidoFiltering("fechaPedido.equals=" + DEFAULT_FECHA_PEDIDO, "fechaPedido.equals=" + UPDATED_FECHA_PEDIDO);
    }

    @Test
    @Transactional
    void getAllPedidosByFechaPedidoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where fechaPedido in
        defaultPedidoFiltering(
            "fechaPedido.in=" + DEFAULT_FECHA_PEDIDO + "," + UPDATED_FECHA_PEDIDO,
            "fechaPedido.in=" + UPDATED_FECHA_PEDIDO
        );
    }

    @Test
    @Transactional
    void getAllPedidosByFechaPedidoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where fechaPedido is not null
        defaultPedidoFiltering("fechaPedido.specified=true", "fechaPedido.specified=false");
    }

    @Test
    @Transactional
    void getAllPedidosByFechaExpiracionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where fechaExpiracion equals to
        defaultPedidoFiltering("fechaExpiracion.equals=" + DEFAULT_FECHA_EXPIRACION, "fechaExpiracion.equals=" + UPDATED_FECHA_EXPIRACION);
    }

    @Test
    @Transactional
    void getAllPedidosByFechaExpiracionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where fechaExpiracion in
        defaultPedidoFiltering(
            "fechaExpiracion.in=" + DEFAULT_FECHA_EXPIRACION + "," + UPDATED_FECHA_EXPIRACION,
            "fechaExpiracion.in=" + UPDATED_FECHA_EXPIRACION
        );
    }

    @Test
    @Transactional
    void getAllPedidosByFechaExpiracionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where fechaExpiracion is not null
        defaultPedidoFiltering("fechaExpiracion.specified=true", "fechaExpiracion.specified=false");
    }

    @Test
    @Transactional
    void getAllPedidosByObservacionesIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where observaciones equals to
        defaultPedidoFiltering("observaciones.equals=" + DEFAULT_OBSERVACIONES, "observaciones.equals=" + UPDATED_OBSERVACIONES);
    }

    @Test
    @Transactional
    void getAllPedidosByObservacionesIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where observaciones in
        defaultPedidoFiltering(
            "observaciones.in=" + DEFAULT_OBSERVACIONES + "," + UPDATED_OBSERVACIONES,
            "observaciones.in=" + UPDATED_OBSERVACIONES
        );
    }

    @Test
    @Transactional
    void getAllPedidosByObservacionesIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where observaciones is not null
        defaultPedidoFiltering("observaciones.specified=true", "observaciones.specified=false");
    }

    @Test
    @Transactional
    void getAllPedidosByObservacionesContainsSomething() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where observaciones contains
        defaultPedidoFiltering("observaciones.contains=" + DEFAULT_OBSERVACIONES, "observaciones.contains=" + UPDATED_OBSERVACIONES);
    }

    @Test
    @Transactional
    void getAllPedidosByObservacionesNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where observaciones does not contain
        defaultPedidoFiltering(
            "observaciones.doesNotContain=" + UPDATED_OBSERVACIONES,
            "observaciones.doesNotContain=" + DEFAULT_OBSERVACIONES
        );
    }

    @Test
    @Transactional
    void getAllPedidosByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where createdBy equals to
        defaultPedidoFiltering("createdBy.equals=" + DEFAULT_CREATED_BY, "createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllPedidosByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where createdBy in
        defaultPedidoFiltering("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY, "createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllPedidosByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where createdBy is not null
        defaultPedidoFiltering("createdBy.specified=true", "createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllPedidosByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where createdBy contains
        defaultPedidoFiltering("createdBy.contains=" + DEFAULT_CREATED_BY, "createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllPedidosByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where createdBy does not contain
        defaultPedidoFiltering("createdBy.doesNotContain=" + UPDATED_CREATED_BY, "createdBy.doesNotContain=" + DEFAULT_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllPedidosByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where createdDate equals to
        defaultPedidoFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllPedidosByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where createdDate in
        defaultPedidoFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllPedidosByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where createdDate is not null
        defaultPedidoFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllPedidosByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where lastModifiedBy equals to
        defaultPedidoFiltering("lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY, "lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllPedidosByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where lastModifiedBy in
        defaultPedidoFiltering(
            "lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY,
            "lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllPedidosByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where lastModifiedBy is not null
        defaultPedidoFiltering("lastModifiedBy.specified=true", "lastModifiedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllPedidosByLastModifiedByContainsSomething() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where lastModifiedBy contains
        defaultPedidoFiltering(
            "lastModifiedBy.contains=" + DEFAULT_LAST_MODIFIED_BY,
            "lastModifiedBy.contains=" + UPDATED_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllPedidosByLastModifiedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where lastModifiedBy does not contain
        defaultPedidoFiltering(
            "lastModifiedBy.doesNotContain=" + UPDATED_LAST_MODIFIED_BY,
            "lastModifiedBy.doesNotContain=" + DEFAULT_LAST_MODIFIED_BY
        );
    }

    @Test
    @Transactional
    void getAllPedidosByLastModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where lastModifiedDate equals to
        defaultPedidoFiltering(
            "lastModifiedDate.equals=" + DEFAULT_LAST_MODIFIED_DATE,
            "lastModifiedDate.equals=" + UPDATED_LAST_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllPedidosByLastModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where lastModifiedDate in
        defaultPedidoFiltering(
            "lastModifiedDate.in=" + DEFAULT_LAST_MODIFIED_DATE + "," + UPDATED_LAST_MODIFIED_DATE,
            "lastModifiedDate.in=" + UPDATED_LAST_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllPedidosByLastModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where lastModifiedDate is not null
        defaultPedidoFiltering("lastModifiedDate.specified=true", "lastModifiedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllPedidosByCuponIsEqualToSomething() throws Exception {
        Cupon cupon;
        if (TestUtil.findAll(em, Cupon.class).isEmpty()) {
            pedidoRepository.saveAndFlush(pedido);
            cupon = CuponResourceIT.createEntity();
        } else {
            cupon = TestUtil.findAll(em, Cupon.class).getFirst();
        }
        em.persist(cupon);
        em.flush();
        pedido.setCupon(cupon);
        pedidoRepository.saveAndFlush(pedido);
        Long cuponId = cupon.getId();
        // Get all the pedidoList where cupon equals to cuponId
        defaultPedidoShouldBeFound("cuponId.equals=" + cuponId);

        // Get all the pedidoList where cupon equals to (cuponId + 1)
        defaultPedidoShouldNotBeFound("cuponId.equals=" + (cuponId + 1));
    }

    @Test
    @Transactional
    void getAllPedidosByTiendaRecojoIsEqualToSomething() throws Exception {
        Tienda tiendaRecojo;
        if (TestUtil.findAll(em, Tienda.class).isEmpty()) {
            pedidoRepository.saveAndFlush(pedido);
            tiendaRecojo = TiendaResourceIT.createEntity(em);
        } else {
            tiendaRecojo = TestUtil.findAll(em, Tienda.class).getFirst();
        }
        em.persist(tiendaRecojo);
        em.flush();
        pedido.setTiendaRecojo(tiendaRecojo);
        pedidoRepository.saveAndFlush(pedido);
        Long tiendaRecojoId = tiendaRecojo.getId();
        // Get all the pedidoList where tiendaRecojo equals to tiendaRecojoId
        defaultPedidoShouldBeFound("tiendaRecojoId.equals=" + tiendaRecojoId);

        // Get all the pedidoList where tiendaRecojo equals to (tiendaRecojoId + 1)
        defaultPedidoShouldNotBeFound("tiendaRecojoId.equals=" + (tiendaRecojoId + 1));
    }

    @Test
    @Transactional
    void getAllPedidosByAtendidoPorIsEqualToSomething() throws Exception {
        Empleado atendidoPor;
        if (TestUtil.findAll(em, Empleado.class).isEmpty()) {
            pedidoRepository.saveAndFlush(pedido);
            atendidoPor = EmpleadoResourceIT.createEntity(em);
        } else {
            atendidoPor = TestUtil.findAll(em, Empleado.class).getFirst();
        }
        em.persist(atendidoPor);
        em.flush();
        pedido.setAtendidoPor(atendidoPor);
        pedidoRepository.saveAndFlush(pedido);
        Long atendidoPorId = atendidoPor.getId();
        // Get all the pedidoList where atendidoPor equals to atendidoPorId
        defaultPedidoShouldBeFound("atendidoPorId.equals=" + atendidoPorId);

        // Get all the pedidoList where atendidoPor equals to (atendidoPorId + 1)
        defaultPedidoShouldNotBeFound("atendidoPorId.equals=" + (atendidoPorId + 1));
    }

    @Test
    @Transactional
    void getAllPedidosByClienteIsEqualToSomething() throws Exception {
        Cliente cliente;
        if (TestUtil.findAll(em, Cliente.class).isEmpty()) {
            pedidoRepository.saveAndFlush(pedido);
            cliente = ClienteResourceIT.createEntity(em);
        } else {
            cliente = TestUtil.findAll(em, Cliente.class).getFirst();
        }
        em.persist(cliente);
        em.flush();
        pedido.setCliente(cliente);
        pedidoRepository.saveAndFlush(pedido);
        Long clienteId = cliente.getId();
        // Get all the pedidoList where cliente equals to clienteId
        defaultPedidoShouldBeFound("clienteId.equals=" + clienteId);

        // Get all the pedidoList where cliente equals to (clienteId + 1)
        defaultPedidoShouldNotBeFound("clienteId.equals=" + (clienteId + 1));
    }

    private void defaultPedidoFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultPedidoShouldBeFound(shouldBeFound);
        defaultPedidoShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPedidoShouldBeFound(String filter) throws Exception {
        restPedidoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pedido.getId().intValue())))
            .andExpect(jsonPath("$.[*].nroPedido").value(hasItem(DEFAULT_NRO_PEDIDO)))
            .andExpect(jsonPath("$.[*].tipoEntrega").value(hasItem(DEFAULT_TIPO_ENTREGA.toString())))
            .andExpect(jsonPath("$.[*].tipoComprobante").value(hasItem(DEFAULT_TIPO_COMPROBANTE.toString())))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO.toString())))
            .andExpect(jsonPath("$.[*].moneda").value(hasItem(DEFAULT_MONEDA.toString())))
            .andExpect(jsonPath("$.[*].subtotal").value(hasItem(sameNumber(DEFAULT_SUBTOTAL))))
            .andExpect(jsonPath("$.[*].descuento").value(hasItem(sameNumber(DEFAULT_DESCUENTO))))
            .andExpect(jsonPath("$.[*].costoEnvio").value(hasItem(sameNumber(DEFAULT_COSTO_ENVIO))))
            .andExpect(jsonPath("$.[*].igv").value(hasItem(sameNumber(DEFAULT_IGV))))
            .andExpect(jsonPath("$.[*].total").value(hasItem(sameNumber(DEFAULT_TOTAL))))
            .andExpect(jsonPath("$.[*].fechaPedido").value(hasItem(DEFAULT_FECHA_PEDIDO.toString())))
            .andExpect(jsonPath("$.[*].fechaExpiracion").value(hasItem(DEFAULT_FECHA_EXPIRACION.toString())))
            .andExpect(jsonPath("$.[*].observaciones").value(hasItem(DEFAULT_OBSERVACIONES)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));

        // Check, that the count call also returns 1
        restPedidoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPedidoShouldNotBeFound(String filter) throws Exception {
        restPedidoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPedidoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPedido() throws Exception {
        // Get the pedido
        restPedidoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPedido() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pedido
        Pedido updatedPedido = pedidoRepository.findById(pedido.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPedido are not directly saved in db
        em.detach(updatedPedido);
        updatedPedido
            .nroPedido(UPDATED_NRO_PEDIDO)
            .tipoEntrega(UPDATED_TIPO_ENTREGA)
            .tipoComprobante(UPDATED_TIPO_COMPROBANTE)
            .estado(UPDATED_ESTADO)
            .moneda(UPDATED_MONEDA)
            .subtotal(UPDATED_SUBTOTAL)
            .descuento(UPDATED_DESCUENTO)
            .costoEnvio(UPDATED_COSTO_ENVIO)
            .igv(UPDATED_IGV)
            .total(UPDATED_TOTAL)
            .fechaPedido(UPDATED_FECHA_PEDIDO)
            .fechaExpiracion(UPDATED_FECHA_EXPIRACION)
            .observaciones(UPDATED_OBSERVACIONES)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        PedidoDTO pedidoDTO = pedidoMapper.toDto(updatedPedido);

        restPedidoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pedidoDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pedidoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Pedido in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPedidoToMatchAllProperties(updatedPedido);
    }

    @Test
    @Transactional
    void putNonExistingPedido() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pedido.setId(longCount.incrementAndGet());

        // Create the Pedido
        PedidoDTO pedidoDTO = pedidoMapper.toDto(pedido);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPedidoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pedidoDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pedidoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pedido in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPedido() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pedido.setId(longCount.incrementAndGet());

        // Create the Pedido
        PedidoDTO pedidoDTO = pedidoMapper.toDto(pedido);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPedidoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(pedidoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pedido in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPedido() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pedido.setId(longCount.incrementAndGet());

        // Create the Pedido
        PedidoDTO pedidoDTO = pedidoMapper.toDto(pedido);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPedidoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pedidoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pedido in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePedidoWithPatch() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pedido using partial update
        Pedido partialUpdatedPedido = new Pedido();
        partialUpdatedPedido.setId(pedido.getId());

        partialUpdatedPedido
            .tipoEntrega(UPDATED_TIPO_ENTREGA)
            .estado(UPDATED_ESTADO)
            .moneda(UPDATED_MONEDA)
            .descuento(UPDATED_DESCUENTO)
            .total(UPDATED_TOTAL)
            .fechaPedido(UPDATED_FECHA_PEDIDO)
            .observaciones(UPDATED_OBSERVACIONES)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restPedidoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPedido.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPedido))
            )
            .andExpect(status().isOk());

        // Validate the Pedido in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPedidoUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedPedido, pedido), getPersistedPedido(pedido));
    }

    @Test
    @Transactional
    void fullUpdatePedidoWithPatch() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pedido using partial update
        Pedido partialUpdatedPedido = new Pedido();
        partialUpdatedPedido.setId(pedido.getId());

        partialUpdatedPedido
            .nroPedido(UPDATED_NRO_PEDIDO)
            .tipoEntrega(UPDATED_TIPO_ENTREGA)
            .tipoComprobante(UPDATED_TIPO_COMPROBANTE)
            .estado(UPDATED_ESTADO)
            .moneda(UPDATED_MONEDA)
            .subtotal(UPDATED_SUBTOTAL)
            .descuento(UPDATED_DESCUENTO)
            .costoEnvio(UPDATED_COSTO_ENVIO)
            .igv(UPDATED_IGV)
            .total(UPDATED_TOTAL)
            .fechaPedido(UPDATED_FECHA_PEDIDO)
            .fechaExpiracion(UPDATED_FECHA_EXPIRACION)
            .observaciones(UPDATED_OBSERVACIONES)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restPedidoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPedido.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPedido))
            )
            .andExpect(status().isOk());

        // Validate the Pedido in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPedidoUpdatableFieldsEquals(partialUpdatedPedido, getPersistedPedido(partialUpdatedPedido));
    }

    @Test
    @Transactional
    void patchNonExistingPedido() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pedido.setId(longCount.incrementAndGet());

        // Create the Pedido
        PedidoDTO pedidoDTO = pedidoMapper.toDto(pedido);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPedidoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pedidoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(pedidoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pedido in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPedido() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pedido.setId(longCount.incrementAndGet());

        // Create the Pedido
        PedidoDTO pedidoDTO = pedidoMapper.toDto(pedido);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPedidoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(pedidoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pedido in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPedido() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pedido.setId(longCount.incrementAndGet());

        // Create the Pedido
        PedidoDTO pedidoDTO = pedidoMapper.toDto(pedido);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPedidoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(pedidoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pedido in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePedido() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the pedido
        restPedidoMockMvc
            .perform(delete(ENTITY_API_URL_ID, pedido.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return pedidoRepository.count();
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

    protected Pedido getPersistedPedido(Pedido pedido) {
        return pedidoRepository.findById(pedido.getId()).orElseThrow();
    }

    protected void assertPersistedPedidoToMatchAllProperties(Pedido expectedPedido) {
        assertPedidoAllPropertiesEquals(expectedPedido, getPersistedPedido(expectedPedido));
    }

    protected void assertPersistedPedidoToMatchUpdatableProperties(Pedido expectedPedido) {
        assertPedidoAllUpdatablePropertiesEquals(expectedPedido, getPersistedPedido(expectedPedido));
    }
}
