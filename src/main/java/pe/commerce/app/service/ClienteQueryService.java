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
import pe.commerce.app.domain.Cliente;
import pe.commerce.app.repository.ClienteRepository;
import pe.commerce.app.service.criteria.ClienteCriteria;
import pe.commerce.app.service.dto.ClienteDTO;
import pe.commerce.app.service.mapper.ClienteMapper;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Cliente} entities in the database.
 * The main input is a {@link ClienteCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ClienteDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ClienteQueryService extends QueryService<Cliente> {

    private static final Logger LOG = LoggerFactory.getLogger(ClienteQueryService.class);

    private final ClienteRepository clienteRepository;

    private final ClienteMapper clienteMapper;

    public ClienteQueryService(ClienteRepository clienteRepository, ClienteMapper clienteMapper) {
        this.clienteRepository = clienteRepository;
        this.clienteMapper = clienteMapper;
    }

    /**
     * Return a {@link Page} of {@link ClienteDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ClienteDTO> findByCriteria(ClienteCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Cliente> specification = createSpecification(criteria);
        return clienteRepository.findAll(specification, page).map(clienteMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ClienteCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Cliente> specification = createSpecification(criteria);
        return clienteRepository.count(specification);
    }

    /**
     * Function to convert {@link ClienteCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Cliente> createSpecification(ClienteCriteria criteria) {
        Specification<Cliente> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(Cliente_.user, JoinType.LEFT);
                root.fetch(Cliente_.persona, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), Cliente_.id),
                    buildStringSpecification(criteria.getCodigo(), Cliente_.codigo),
                    buildRangeSpecification(criteria.getFechaRegistro(), Cliente_.fechaRegistro),
                    buildSpecification(criteria.getAceptaPublicidad(), Cliente_.aceptaPublicidad),
                    buildSpecification(criteria.getActivo(), Cliente_.activo),
                    buildStringSpecification(criteria.getCreatedBy(), Cliente_.createdBy),
                    buildRangeSpecification(criteria.getCreatedDate(), Cliente_.createdDate),
                    buildStringSpecification(criteria.getLastModifiedBy(), Cliente_.lastModifiedBy),
                    buildRangeSpecification(criteria.getLastModifiedDate(), Cliente_.lastModifiedDate),
                    buildSpecification(criteria.getUserId(), root -> root.join(Cliente_.user, JoinType.LEFT).get(User_.id)),
                    buildSpecification(criteria.getPersonaId(), root -> root.join(Cliente_.persona, JoinType.LEFT).get(Persona_.id)),
                    buildSpecification(criteria.getDireccionesId(), root ->
                        root.join(Cliente_.direccioneses, JoinType.LEFT).get(Direccion_.id)
                    ),
                    buildSpecification(criteria.getDatosFacturacionId(), root ->
                        root.join(Cliente_.datosFacturacions, JoinType.LEFT).get(DatoFacturacion_.id)
                    ),
                    buildSpecification(criteria.getPedidosId(), root -> root.join(Cliente_.pedidoses, JoinType.LEFT).get(Pedido_.id)),
                    buildSpecification(criteria.getResenasId(), root -> root.join(Cliente_.resenases, JoinType.LEFT).get(Resena_.id)),
                    buildSpecification(criteria.getCarritoId(), root -> root.join(Cliente_.carrito, JoinType.LEFT).get(Carrito_.id))
                )
            );
        }
        return specification;
    }
}
