package pe.commerce.app.service;

import java.util.List;
import java.util.Optional;
import pe.commerce.app.service.dto.DireccionDTO;

/**
 * Service Interface for managing {@link pe.commerce.app.domain.Direccion}.
 */
public interface DireccionService {
    /**
     * Save a direccion.
     *
     * @param direccionDTO the entity to save.
     * @return the persisted entity.
     */
    DireccionDTO save(DireccionDTO direccionDTO);

    /**
     * Updates a direccion.
     *
     * @param direccionDTO the entity to update.
     * @return the persisted entity.
     */
    DireccionDTO update(DireccionDTO direccionDTO);

    /**
     * Partially updates a direccion.
     *
     * @param direccionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DireccionDTO> partialUpdate(DireccionDTO direccionDTO);

    /**
     * Get all the direccions.
     *
     * @return the list of entities.
     */
    List<DireccionDTO> findAll();

    /**
     * Get all the direccions with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    List<DireccionDTO> findAllWithEagerRelationships();

    /**
     * Get the "id" direccion.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DireccionDTO> findOne(Long id);

    /**
     * Delete the "id" direccion.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
