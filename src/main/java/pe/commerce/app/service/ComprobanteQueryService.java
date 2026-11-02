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
import pe.commerce.app.domain.Comprobante;
import pe.commerce.app.repository.ComprobanteRepository;
import pe.commerce.app.service.criteria.ComprobanteCriteria;
import pe.commerce.app.service.dto.ComprobanteDTO;
import pe.commerce.app.service.mapper.ComprobanteMapper;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Comprobante} entities in the database.
 * The main input is a {@link ComprobanteCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ComprobanteDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ComprobanteQueryService extends QueryService<Comprobante> {

    private static final Logger LOG = LoggerFactory.getLogger(ComprobanteQueryService.class);

    private final ComprobanteRepository comprobanteRepository;

    private final ComprobanteMapper comprobanteMapper;

    public ComprobanteQueryService(ComprobanteRepository comprobanteRepository, ComprobanteMapper comprobanteMapper) {
        this.comprobanteRepository = comprobanteRepository;
        this.comprobanteMapper = comprobanteMapper;
    }

    /**
     * Return a {@link Page} of {@link ComprobanteDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ComprobanteDTO> findByCriteria(ComprobanteCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Comprobante> specification = createSpecification(criteria);
        return comprobanteRepository.findAll(specification, page).map(comprobanteMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ComprobanteCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Comprobante> specification = createSpecification(criteria);
        return comprobanteRepository.count(specification);
    }

    /**
     * Function to convert {@link ComprobanteCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Comprobante> createSpecification(ComprobanteCriteria criteria) {
        Specification<Comprobante> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(Comprobante_.comprobanteReferencia, JoinType.LEFT);
                root.fetch(Comprobante_.pedido, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), Comprobante_.id),
                    buildSpecification(criteria.getTipo(), Comprobante_.tipo),
                    buildStringSpecification(criteria.getSerie(), Comprobante_.serie),
                    buildRangeSpecification(criteria.getNumero(), Comprobante_.numero),
                    buildRangeSpecification(criteria.getFechaEmision(), Comprobante_.fechaEmision),
                    buildStringSpecification(criteria.getReceptorTipoDocumento(), Comprobante_.receptorTipoDocumento),
                    buildStringSpecification(criteria.getReceptorNroDocumento(), Comprobante_.receptorNroDocumento),
                    buildStringSpecification(criteria.getReceptorNombre(), Comprobante_.receptorNombre),
                    buildStringSpecification(criteria.getReceptorDireccion(), Comprobante_.receptorDireccion),
                    buildSpecification(criteria.getMoneda(), Comprobante_.moneda),
                    buildRangeSpecification(criteria.getOpGravada(), Comprobante_.opGravada),
                    buildRangeSpecification(criteria.getIgv(), Comprobante_.igv),
                    buildRangeSpecification(criteria.getTotal(), Comprobante_.total),
                    buildSpecification(criteria.getEstadoSunat(), Comprobante_.estadoSunat),
                    buildStringSpecification(criteria.getXmlUrl(), Comprobante_.xmlUrl),
                    buildStringSpecification(criteria.getPdfUrl(), Comprobante_.pdfUrl),
                    buildStringSpecification(criteria.getCreatedBy(), Comprobante_.createdBy),
                    buildRangeSpecification(criteria.getCreatedDate(), Comprobante_.createdDate),
                    buildStringSpecification(criteria.getLastModifiedBy(), Comprobante_.lastModifiedBy),
                    buildRangeSpecification(criteria.getLastModifiedDate(), Comprobante_.lastModifiedDate),
                    buildSpecification(criteria.getComprobanteReferenciaId(), root ->
                        root.join(Comprobante_.comprobanteReferencia, JoinType.LEFT).get(Comprobante_.id)
                    ),
                    buildSpecification(criteria.getPedidoId(), root -> root.join(Comprobante_.pedido, JoinType.LEFT).get(Pedido_.id))
                )
            );
        }
        return specification;
    }
}
