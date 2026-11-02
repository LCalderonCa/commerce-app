package pe.commerce.app.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pe.commerce.app.service.dto.ComprobanteDTO;

/**
 * Service Interface for managing {@link pe.commerce.app.domain.Comprobante}.
 */
public interface ComprobanteService {
    /**
     * Save a comprobante.
     *
     * @param comprobanteDTO the entity to save.
     * @return the persisted entity.
     */
    ComprobanteDTO save(ComprobanteDTO comprobanteDTO);

    /**
     * Updates a comprobante.
     *
     * @param comprobanteDTO the entity to update.
     * @return the persisted entity.
     */
    ComprobanteDTO update(ComprobanteDTO comprobanteDTO);

    /**
     * Partially updates a comprobante.
     *
     * @param comprobanteDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ComprobanteDTO> partialUpdate(ComprobanteDTO comprobanteDTO);

    /**
     * Get all the comprobantes with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ComprobanteDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" comprobante.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ComprobanteDTO> findOne(Long id);

    /**
     * Delete the "id" comprobante.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
