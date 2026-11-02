package pe.commerce.app.service;

import java.util.List;
import java.util.Optional;
import pe.commerce.app.service.dto.DatoFacturacionDTO;

/**
 * Service Interface for managing {@link pe.commerce.app.domain.DatoFacturacion}.
 */
public interface DatoFacturacionService {
    /**
     * Save a datoFacturacion.
     *
     * @param datoFacturacionDTO the entity to save.
     * @return the persisted entity.
     */
    DatoFacturacionDTO save(DatoFacturacionDTO datoFacturacionDTO);

    /**
     * Updates a datoFacturacion.
     *
     * @param datoFacturacionDTO the entity to update.
     * @return the persisted entity.
     */
    DatoFacturacionDTO update(DatoFacturacionDTO datoFacturacionDTO);

    /**
     * Partially updates a datoFacturacion.
     *
     * @param datoFacturacionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DatoFacturacionDTO> partialUpdate(DatoFacturacionDTO datoFacturacionDTO);

    /**
     * Get all the datoFacturacions.
     *
     * @return the list of entities.
     */
    List<DatoFacturacionDTO> findAll();

    /**
     * Get all the datoFacturacions with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    List<DatoFacturacionDTO> findAllWithEagerRelationships();

    /**
     * Get the "id" datoFacturacion.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DatoFacturacionDTO> findOne(Long id);

    /**
     * Delete the "id" datoFacturacion.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
