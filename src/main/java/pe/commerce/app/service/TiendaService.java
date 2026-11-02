package pe.commerce.app.service;

import java.util.List;
import java.util.Optional;
import pe.commerce.app.service.dto.TiendaDTO;

/**
 * Service Interface for managing {@link pe.commerce.app.domain.Tienda}.
 */
public interface TiendaService {
    /**
     * Save a tienda.
     *
     * @param tiendaDTO the entity to save.
     * @return the persisted entity.
     */
    TiendaDTO save(TiendaDTO tiendaDTO);

    /**
     * Updates a tienda.
     *
     * @param tiendaDTO the entity to update.
     * @return the persisted entity.
     */
    TiendaDTO update(TiendaDTO tiendaDTO);

    /**
     * Partially updates a tienda.
     *
     * @param tiendaDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TiendaDTO> partialUpdate(TiendaDTO tiendaDTO);

    /**
     * Get all the tiendas.
     *
     * @return the list of entities.
     */
    List<TiendaDTO> findAll();

    /**
     * Get all the tiendas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    List<TiendaDTO> findAllWithEagerRelationships();

    /**
     * Get the "id" tienda.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TiendaDTO> findOne(Long id);

    /**
     * Delete the "id" tienda.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
