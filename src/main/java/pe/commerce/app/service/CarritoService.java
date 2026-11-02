package pe.commerce.app.service;

import java.util.List;
import java.util.Optional;
import pe.commerce.app.service.dto.CarritoDTO;

/**
 * Service Interface for managing {@link pe.commerce.app.domain.Carrito}.
 */
public interface CarritoService {
    /**
     * Save a carrito.
     *
     * @param carritoDTO the entity to save.
     * @return the persisted entity.
     */
    CarritoDTO save(CarritoDTO carritoDTO);

    /**
     * Updates a carrito.
     *
     * @param carritoDTO the entity to update.
     * @return the persisted entity.
     */
    CarritoDTO update(CarritoDTO carritoDTO);

    /**
     * Partially updates a carrito.
     *
     * @param carritoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CarritoDTO> partialUpdate(CarritoDTO carritoDTO);

    /**
     * Get all the carritos.
     *
     * @return the list of entities.
     */
    List<CarritoDTO> findAll();

    /**
     * Get all the carritos with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    List<CarritoDTO> findAllWithEagerRelationships();

    /**
     * Get the "id" carrito.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CarritoDTO> findOne(Long id);

    /**
     * Delete the "id" carrito.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
