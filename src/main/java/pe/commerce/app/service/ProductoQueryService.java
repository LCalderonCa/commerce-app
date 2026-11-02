package pe.commerce.app.service;

import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.commerce.app.domain.*; // for static metamodels
import pe.commerce.app.domain.Producto;
import pe.commerce.app.repository.ProductoRepository;
import pe.commerce.app.service.criteria.ProductoCriteria;
import pe.commerce.app.service.dto.ProductoDTO;
import pe.commerce.app.service.mapper.ProductoMapper;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Producto} entities in the database.
 * The main input is a {@link ProductoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ProductoDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProductoQueryService extends QueryService<Producto> {

    private static final Logger LOG = LoggerFactory.getLogger(ProductoQueryService.class);

    private final ProductoRepository productoRepository;

    private final ProductoMapper productoMapper;

    public ProductoQueryService(ProductoRepository productoRepository, ProductoMapper productoMapper) {
        this.productoRepository = productoRepository;
        this.productoMapper = productoMapper;
    }

    /**
     * Return a {@link Page} of {@link ProductoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProductoDTO> findByCriteria(ProductoCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Producto> specification = createSpecification(criteria);
        return productoRepository.findAll(specification, page).map(productoMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProductoCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Producto> specification = createSpecification(criteria);
        return productoRepository.count(specification);
    }

    /**
     * Function to convert {@link ProductoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Producto> createSpecification(ProductoCriteria criteria) {
        Specification<Producto> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(Producto_.categoria, JoinType.LEFT);
                root.fetch(Producto_.marca, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), Producto_.id),
                    buildStringSpecification(criteria.getSku(), Producto_.sku),
                    buildStringSpecification(criteria.getNombre(), Producto_.nombre),
                    buildStringSpecification(criteria.getSlug(), Producto_.slug),
                    buildStringSpecification(criteria.getModelo(), Producto_.modelo),
                    buildStringSpecification(criteria.getDescripcion(), Producto_.descripcion),
                    buildStringSpecification(criteria.getEspecificaciones(), Producto_.especificaciones),
                    buildRangeSpecification(criteria.getPrecioLista(), Producto_.precioLista),
                    buildRangeSpecification(criteria.getPrecioOferta(), Producto_.precioOferta),
                    buildRangeSpecification(criteria.getPesoKg(), Producto_.pesoKg),
                    buildRangeSpecification(criteria.getGarantiaMeses(), Producto_.garantiaMeses),
                    buildSpecification(criteria.getActivo(), Producto_.activo),
                    buildStringSpecification(criteria.getCreatedBy(), Producto_.createdBy),
                    buildRangeSpecification(criteria.getCreatedDate(), Producto_.createdDate),
                    buildStringSpecification(criteria.getLastModifiedBy(), Producto_.lastModifiedBy),
                    buildRangeSpecification(criteria.getLastModifiedDate(), Producto_.lastModifiedDate),
                    buildSpecification(criteria.getImagenesId(), root ->
                        root.join(Producto_.imageneses, JoinType.LEFT).get(ProductoImagen_.id)
                    ),
                    buildSpecification(criteria.getInventariosId(), root ->
                        root.join(Producto_.inventarioses, JoinType.LEFT).get(Inventario_.id)
                    ),
                    buildSpecification(criteria.getResenasId(), root -> root.join(Producto_.resenases, JoinType.LEFT).get(Resena_.id)),
                    buildSpecification(criteria.getCategoriaId(), root -> root.join(Producto_.categoria, JoinType.LEFT).get(Categoria_.id)),
                    buildSpecification(criteria.getMarcaId(), root -> root.join(Producto_.marca, JoinType.LEFT).get(Marca_.id))
                )
            );
        }
        return specification;
    }
}
