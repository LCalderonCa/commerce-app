package pe.commerce.app.service;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pe.commerce.app.service.dto.PedidoDTO;

/**
 * Service Interface for managing {@link pe.commerce.app.domain.Pedido}.
 */
public interface PedidoService {
    /**
     * Save a pedido.
     *
     * @param pedidoDTO the entity to save.
     * @return the persisted entity.
     */
    PedidoDTO save(PedidoDTO pedidoDTO);

    /**
     * Updates a pedido.
     *
     * @param pedidoDTO the entity to update.
     * @return the persisted entity.
     */
    PedidoDTO update(PedidoDTO pedidoDTO);

    /**
     * Partially updates a pedido.
     *
     * @param pedidoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PedidoDTO> partialUpdate(PedidoDTO pedidoDTO);

    /**
     * Get all the PedidoDTO where Envio is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<PedidoDTO> findAllWhereEnvioIsNull();

    /**
     * Get all the pedidos with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PedidoDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" pedido.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PedidoDTO> findOne(Long id);

    /**
     * Delete the "id" pedido.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
