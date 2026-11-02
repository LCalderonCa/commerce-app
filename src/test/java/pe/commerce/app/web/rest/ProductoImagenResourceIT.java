package pe.commerce.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pe.commerce.app.domain.ProductoImagenAsserts.*;
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
import pe.commerce.app.domain.Producto;
import pe.commerce.app.domain.ProductoImagen;
import pe.commerce.app.repository.ProductoImagenRepository;
import pe.commerce.app.service.ProductoImagenService;
import pe.commerce.app.service.dto.ProductoImagenDTO;
import pe.commerce.app.service.mapper.ProductoImagenMapper;
import tools.jackson.databind.ObjectMapper;

/**
 * Integration tests for the {@link ProductoImagenResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ProductoImagenResourceIT {

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final Integer DEFAULT_ORDEN = 1;
    private static final Integer UPDATED_ORDEN = 2;

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.ofEpochMilli(1702246707543L);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.ofEpochMilli(1702246707543L);

    private static final String ENTITY_API_URL = "/api/producto-imagens";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProductoImagenRepository productoImagenRepository;

    @Mock
    private ProductoImagenRepository productoImagenRepositoryMock;

    @Autowired
    private ProductoImagenMapper productoImagenMapper;

    @Mock
    private ProductoImagenService productoImagenServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductoImagenMockMvc;

    private ProductoImagen productoImagen;

    private ProductoImagen insertedProductoImagen;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductoImagen createEntity(EntityManager em) {
        ProductoImagen productoImagen = new ProductoImagen()
            .url(DEFAULT_URL)
            .orden(DEFAULT_ORDEN)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        // Add required entity
        Producto producto;
        if (TestUtil.findAll(em, Producto.class).isEmpty()) {
            producto = ProductoResourceIT.createEntity(em);
            em.persist(producto);
            em.flush();
        } else {
            producto = TestUtil.findAll(em, Producto.class).getFirst();
        }
        productoImagen.setProducto(producto);
        return productoImagen;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductoImagen createUpdatedEntity(EntityManager em) {
        ProductoImagen updatedProductoImagen = new ProductoImagen()
            .url(UPDATED_URL)
            .orden(UPDATED_ORDEN)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        // Add required entity
        Producto producto;
        if (TestUtil.findAll(em, Producto.class).isEmpty()) {
            producto = ProductoResourceIT.createUpdatedEntity(em);
            em.persist(producto);
            em.flush();
        } else {
            producto = TestUtil.findAll(em, Producto.class).getFirst();
        }
        updatedProductoImagen.setProducto(producto);
        return updatedProductoImagen;
    }

    @BeforeEach
    void initTest() {
        productoImagen = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedProductoImagen != null) {
            productoImagenRepository.delete(insertedProductoImagen);
            insertedProductoImagen = null;
        }
    }

    @Test
    @Transactional
    void createProductoImagen() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ProductoImagen
        ProductoImagenDTO productoImagenDTO = productoImagenMapper.toDto(productoImagen);
        var returnedProductoImagenDTO = om.readValue(
            restProductoImagenMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productoImagenDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProductoImagenDTO.class
        );

        // Validate the ProductoImagen in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedProductoImagen = productoImagenMapper.toEntity(returnedProductoImagenDTO);
        assertProductoImagenUpdatableFieldsEquals(returnedProductoImagen, getPersistedProductoImagen(returnedProductoImagen));

        insertedProductoImagen = returnedProductoImagen;
    }

    @Test
    @Transactional
    void createProductoImagenWithExistingId() throws Exception {
        // Create the ProductoImagen with an existing ID
        productoImagen.setId(1L);
        ProductoImagenDTO productoImagenDTO = productoImagenMapper.toDto(productoImagen);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductoImagenMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productoImagenDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ProductoImagen in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUrlIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        productoImagen.setUrl(null);

        // Create the ProductoImagen, which fails.
        ProductoImagenDTO productoImagenDTO = productoImagenMapper.toDto(productoImagen);

        restProductoImagenMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productoImagenDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkOrdenIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        productoImagen.setOrden(null);

        // Create the ProductoImagen, which fails.
        ProductoImagenDTO productoImagenDTO = productoImagenMapper.toDto(productoImagen);

        restProductoImagenMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productoImagenDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProductoImagens() throws Exception {
        // Initialize the database
        insertedProductoImagen = productoImagenRepository.saveAndFlush(productoImagen);

        // Get all the productoImagenList
        restProductoImagenMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productoImagen.getId().intValue())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)))
            .andExpect(jsonPath("$.[*].orden").value(hasItem(DEFAULT_ORDEN)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProductoImagensWithEagerRelationshipsIsEnabled() throws Exception {
        when(productoImagenServiceMock.findAllWithEagerRelationships()).thenReturn(new ArrayList<>());

        restProductoImagenMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(productoImagenServiceMock, times(1)).findAllWithEagerRelationships();
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProductoImagensWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(productoImagenServiceMock.findAllWithEagerRelationships()).thenReturn(new ArrayList<>());

        restProductoImagenMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(productoImagenRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getProductoImagen() throws Exception {
        // Initialize the database
        insertedProductoImagen = productoImagenRepository.saveAndFlush(productoImagen);

        // Get the productoImagen
        restProductoImagenMockMvc
            .perform(get(ENTITY_API_URL_ID, productoImagen.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productoImagen.getId().intValue()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL))
            .andExpect(jsonPath("$.orden").value(DEFAULT_ORDEN))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingProductoImagen() throws Exception {
        // Get the productoImagen
        restProductoImagenMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProductoImagen() throws Exception {
        // Initialize the database
        insertedProductoImagen = productoImagenRepository.saveAndFlush(productoImagen);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productoImagen
        ProductoImagen updatedProductoImagen = productoImagenRepository.findById(productoImagen.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProductoImagen are not directly saved in db
        em.detach(updatedProductoImagen);
        updatedProductoImagen
            .url(UPDATED_URL)
            .orden(UPDATED_ORDEN)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        ProductoImagenDTO productoImagenDTO = productoImagenMapper.toDto(updatedProductoImagen);

        restProductoImagenMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productoImagenDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productoImagenDTO))
            )
            .andExpect(status().isOk());

        // Validate the ProductoImagen in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProductoImagenToMatchAllProperties(updatedProductoImagen);
    }

    @Test
    @Transactional
    void putNonExistingProductoImagen() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productoImagen.setId(longCount.incrementAndGet());

        // Create the ProductoImagen
        ProductoImagenDTO productoImagenDTO = productoImagenMapper.toDto(productoImagen);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductoImagenMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productoImagenDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productoImagenDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductoImagen in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductoImagen() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productoImagen.setId(longCount.incrementAndGet());

        // Create the ProductoImagen
        ProductoImagenDTO productoImagenDTO = productoImagenMapper.toDto(productoImagen);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductoImagenMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productoImagenDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductoImagen in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductoImagen() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productoImagen.setId(longCount.incrementAndGet());

        // Create the ProductoImagen
        ProductoImagenDTO productoImagenDTO = productoImagenMapper.toDto(productoImagen);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductoImagenMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productoImagenDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductoImagen in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductoImagenWithPatch() throws Exception {
        // Initialize the database
        insertedProductoImagen = productoImagenRepository.saveAndFlush(productoImagen);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productoImagen using partial update
        ProductoImagen partialUpdatedProductoImagen = new ProductoImagen();
        partialUpdatedProductoImagen.setId(productoImagen.getId());

        partialUpdatedProductoImagen
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);

        restProductoImagenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductoImagen.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductoImagen))
            )
            .andExpect(status().isOk());

        // Validate the ProductoImagen in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductoImagenUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedProductoImagen, productoImagen),
            getPersistedProductoImagen(productoImagen)
        );
    }

    @Test
    @Transactional
    void fullUpdateProductoImagenWithPatch() throws Exception {
        // Initialize the database
        insertedProductoImagen = productoImagenRepository.saveAndFlush(productoImagen);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productoImagen using partial update
        ProductoImagen partialUpdatedProductoImagen = new ProductoImagen();
        partialUpdatedProductoImagen.setId(productoImagen.getId());

        partialUpdatedProductoImagen
            .url(UPDATED_URL)
            .orden(UPDATED_ORDEN)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restProductoImagenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductoImagen.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductoImagen))
            )
            .andExpect(status().isOk());

        // Validate the ProductoImagen in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductoImagenUpdatableFieldsEquals(partialUpdatedProductoImagen, getPersistedProductoImagen(partialUpdatedProductoImagen));
    }

    @Test
    @Transactional
    void patchNonExistingProductoImagen() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productoImagen.setId(longCount.incrementAndGet());

        // Create the ProductoImagen
        ProductoImagenDTO productoImagenDTO = productoImagenMapper.toDto(productoImagen);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductoImagenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productoImagenDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productoImagenDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductoImagen in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductoImagen() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productoImagen.setId(longCount.incrementAndGet());

        // Create the ProductoImagen
        ProductoImagenDTO productoImagenDTO = productoImagenMapper.toDto(productoImagen);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductoImagenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productoImagenDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductoImagen in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductoImagen() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productoImagen.setId(longCount.incrementAndGet());

        // Create the ProductoImagen
        ProductoImagenDTO productoImagenDTO = productoImagenMapper.toDto(productoImagen);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductoImagenMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(productoImagenDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductoImagen in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProductoImagen() throws Exception {
        // Initialize the database
        insertedProductoImagen = productoImagenRepository.saveAndFlush(productoImagen);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the productoImagen
        restProductoImagenMockMvc
            .perform(delete(ENTITY_API_URL_ID, productoImagen.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return productoImagenRepository.count();
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

    protected ProductoImagen getPersistedProductoImagen(ProductoImagen productoImagen) {
        return productoImagenRepository.findById(productoImagen.getId()).orElseThrow();
    }

    protected void assertPersistedProductoImagenToMatchAllProperties(ProductoImagen expectedProductoImagen) {
        assertProductoImagenAllPropertiesEquals(expectedProductoImagen, getPersistedProductoImagen(expectedProductoImagen));
    }

    protected void assertPersistedProductoImagenToMatchUpdatableProperties(ProductoImagen expectedProductoImagen) {
        assertProductoImagenAllUpdatablePropertiesEquals(expectedProductoImagen, getPersistedProductoImagen(expectedProductoImagen));
    }
}
