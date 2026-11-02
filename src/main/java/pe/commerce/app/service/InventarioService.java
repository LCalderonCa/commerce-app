package pe.commerce.app.service;

import java.util.List;
import java.util.Optional;
import pe.commerce.app.service.dto.InventarioDTO;

/**
 * Service Interface for managing {@link pe.commerce.app.domain.Inventario}.
 */
public interface InventarioService {
    /**
     * Save a inventario.
     *
     * @param inventarioDTO the entity to save.
     * @return the persisted entity.
     */
    InventarioDTO save(InventarioDTO inventarioDTO);

    /**
     * Updates a inventario.
     *
     * @param inventarioDTO the entity to update.
     * @return the persisted entity.
     */
    InventarioDTO update(InventarioDTO inventarioDTO);

    /**
     * Partially updates a inventario.
     *
     * @param inventarioDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<InventarioDTO> partialUpdate(InventarioDTO inventarioDTO);

    /**
     * Get all the inventarios.
     *
     * @return the list of entities.
     */
    List<InventarioDTO> findAll();

    /**
     * Get all the inventarios with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    List<InventarioDTO> findAllWithEagerRelationships();

    /**
     * Get the "id" inventario.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<InventarioDTO> findOne(Long id);

    /**
     * Delete the "id" inventario.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
