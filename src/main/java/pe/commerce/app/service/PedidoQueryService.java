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
import pe.commerce.app.domain.Pedido;
import pe.commerce.app.repository.PedidoRepository;
import pe.commerce.app.service.criteria.PedidoCriteria;
import pe.commerce.app.service.dto.PedidoDTO;
import pe.commerce.app.service.mapper.PedidoMapper;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Pedido} entities in the database.
 * The main input is a {@link PedidoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link PedidoDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PedidoQueryService extends QueryService<Pedido> {

    private static final Logger LOG = LoggerFactory.getLogger(PedidoQueryService.class);

    private final PedidoRepository pedidoRepository;

    private final PedidoMapper pedidoMapper;

    public PedidoQueryService(PedidoRepository pedidoRepository, PedidoMapper pedidoMapper) {
        this.pedidoRepository = pedidoRepository;
        this.pedidoMapper = pedidoMapper;
    }

    /**
     * Return a {@link Page} of {@link PedidoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PedidoDTO> findByCriteria(PedidoCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Pedido> specification = createSpecification(criteria);
        return pedidoRepository.findAll(specification, page).map(pedidoMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PedidoCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Pedido> specification = createSpecification(criteria);
        return pedidoRepository.count(specification);
    }

    /**
     * Function to convert {@link PedidoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Pedido> createSpecification(PedidoCriteria criteria) {
        Specification<Pedido> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(Pedido_.cupon, JoinType.LEFT);
                root.fetch(Pedido_.tiendaRecojo, JoinType.LEFT);
                root.fetch(Pedido_.atendidoPor, JoinType.LEFT);
                root.fetch(Pedido_.cliente, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), Pedido_.id),
                    buildStringSpecification(criteria.getNroPedido(), Pedido_.nroPedido),
                    buildSpecification(criteria.getTipoEntrega(), Pedido_.tipoEntrega),
                    buildSpecification(criteria.getTipoComprobante(), Pedido_.tipoComprobante),
                    buildSpecification(criteria.getEstado(), Pedido_.estado),
                    buildSpecification(criteria.getMoneda(), Pedido_.moneda),
                    buildRangeSpecification(criteria.getSubtotal(), Pedido_.subtotal),
                    buildRangeSpecification(criteria.getDescuento(), Pedido_.descuento),
                    buildRangeSpecification(criteria.getCostoEnvio(), Pedido_.costoEnvio),
                    buildRangeSpecification(criteria.getIgv(), Pedido_.igv),
                    buildRangeSpecification(criteria.getTotal(), Pedido_.total),
                    buildRangeSpecification(criteria.getFechaPedido(), Pedido_.fechaPedido),
                    buildRangeSpecification(criteria.getFechaExpiracion(), Pedido_.fechaExpiracion),
                    buildStringSpecification(criteria.getObservaciones(), Pedido_.observaciones),
                    buildStringSpecification(criteria.getCreatedBy(), Pedido_.createdBy),
                    buildRangeSpecification(criteria.getCreatedDate(), Pedido_.createdDate),
                    buildStringSpecification(criteria.getLastModifiedBy(), Pedido_.lastModifiedBy),
                    buildRangeSpecification(criteria.getLastModifiedDate(), Pedido_.lastModifiedDate),
                    buildSpecification(criteria.getDetallesId(), root ->
                        root.join(Pedido_.detalleses, JoinType.LEFT).get(PedidoDetalle_.id)
                    ),
                    buildSpecification(criteria.getHistorialEstadosId(), root ->
                        root.join(Pedido_.historialEstadoses, JoinType.LEFT).get(PedidoEstadoHistorial_.id)
                    ),
                    buildSpecification(criteria.getPagosId(), root -> root.join(Pedido_.pagoses, JoinType.LEFT).get(Pago_.id)),
                    buildSpecification(criteria.getComprobantesId(), root ->
                        root.join(Pedido_.comprobanteses, JoinType.LEFT).get(Comprobante_.id)
                    ),
                    buildSpecification(criteria.getCuponId(), root -> root.join(Pedido_.cupon, JoinType.LEFT).get(Cupon_.id)),
                    buildSpecification(criteria.getTiendaRecojoId(), root ->
                        root.join(Pedido_.tiendaRecojo, JoinType.LEFT).get(Tienda_.id)
                    ),
                    buildSpecification(criteria.getAtendidoPorId(), root ->
                        root.join(Pedido_.atendidoPor, JoinType.LEFT).get(Empleado_.id)
                    ),
                    buildSpecification(criteria.getEnvioId(), root -> root.join(Pedido_.envio, JoinType.LEFT).get(Envio_.id)),
                    buildSpecification(criteria.getClienteId(), root -> root.join(Pedido_.cliente, JoinType.LEFT).get(Cliente_.id))
                )
            );
        }
        return specification;
    }
}
