package pe.commerce.app.service;

import java.util.List;
import java.util.Optional;
import pe.commerce.app.service.dto.PedidoDetalleDTO;

/**
 * Service Interface for managing {@link pe.commerce.app.domain.PedidoDetalle}.
 */
public interface PedidoDetalleService {
    /**
     * Save a pedidoDetalle.
     *
     * @param pedidoDetalleDTO the entity to save.
     * @return the persisted entity.
     */
    PedidoDetalleDTO save(PedidoDetalleDTO pedidoDetalleDTO);

    /**
     * Updates a pedidoDetalle.
     *
     * @param pedidoDetalleDTO the entity to update.
     * @return the persisted entity.
     */
    PedidoDetalleDTO update(PedidoDetalleDTO pedidoDetalleDTO);

    /**
     * Partially updates a pedidoDetalle.
     *
     * @param pedidoDetalleDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PedidoDetalleDTO> partialUpdate(PedidoDetalleDTO pedidoDetalleDTO);

    /**
     * Get all the pedidoDetalles.
     *
     * @return the list of entities.
     */
    List<PedidoDetalleDTO> findAll();

    /**
     * Get all the pedidoDetalles with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    List<PedidoDetalleDTO> findAllWithEagerRelationships();

    /**
     * Get the "id" pedidoDetalle.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PedidoDetalleDTO> findOne(Long id);

    /**
     * Delete the "id" pedidoDetalle.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
