package pe.commerce.app.service;

import java.util.List;
import java.util.Optional;
import pe.commerce.app.service.dto.PedidoEstadoHistorialDTO;

/**
 * Service Interface for managing {@link pe.commerce.app.domain.PedidoEstadoHistorial}.
 */
public interface PedidoEstadoHistorialService {
    /**
     * Save a pedidoEstadoHistorial.
     *
     * @param pedidoEstadoHistorialDTO the entity to save.
     * @return the persisted entity.
     */
    PedidoEstadoHistorialDTO save(PedidoEstadoHistorialDTO pedidoEstadoHistorialDTO);

    /**
     * Updates a pedidoEstadoHistorial.
     *
     * @param pedidoEstadoHistorialDTO the entity to update.
     * @return the persisted entity.
     */
    PedidoEstadoHistorialDTO update(PedidoEstadoHistorialDTO pedidoEstadoHistorialDTO);

    /**
     * Partially updates a pedidoEstadoHistorial.
     *
     * @param pedidoEstadoHistorialDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PedidoEstadoHistorialDTO> partialUpdate(PedidoEstadoHistorialDTO pedidoEstadoHistorialDTO);

    /**
     * Get all the pedidoEstadoHistorials.
     *
     * @return the list of entities.
     */
    List<PedidoEstadoHistorialDTO> findAll();

    /**
     * Get all the pedidoEstadoHistorials with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    List<PedidoEstadoHistorialDTO> findAllWithEagerRelationships();

    /**
     * Get the "id" pedidoEstadoHistorial.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PedidoEstadoHistorialDTO> findOne(Long id);

    /**
     * Delete the "id" pedidoEstadoHistorial.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
