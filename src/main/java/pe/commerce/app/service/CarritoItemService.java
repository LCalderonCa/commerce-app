package pe.commerce.app.service;

import java.util.List;
import java.util.Optional;
import pe.commerce.app.service.dto.CarritoItemDTO;

/**
 * Service Interface for managing {@link pe.commerce.app.domain.CarritoItem}.
 */
public interface CarritoItemService {
    /**
     * Save a carritoItem.
     *
     * @param carritoItemDTO the entity to save.
     * @return the persisted entity.
     */
    CarritoItemDTO save(CarritoItemDTO carritoItemDTO);

    /**
     * Updates a carritoItem.
     *
     * @param carritoItemDTO the entity to update.
     * @return the persisted entity.
     */
    CarritoItemDTO update(CarritoItemDTO carritoItemDTO);

    /**
     * Partially updates a carritoItem.
     *
     * @param carritoItemDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CarritoItemDTO> partialUpdate(CarritoItemDTO carritoItemDTO);

    /**
     * Get all the carritoItems.
     *
     * @return the list of entities.
     */
    List<CarritoItemDTO> findAll();

    /**
     * Get all the CarritoItemDTO where PedidoDetalle is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<CarritoItemDTO> findAllWherePedidoDetalleIsNull();

    /**
     * Get all the carritoItems with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    List<CarritoItemDTO> findAllWithEagerRelationships();

    /**
     * Get the "id" carritoItem.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CarritoItemDTO> findOne(Long id);

    /**
     * Delete the "id" carritoItem.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
