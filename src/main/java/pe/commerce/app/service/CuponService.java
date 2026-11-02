package pe.commerce.app.service;

import java.util.List;
import java.util.Optional;
import pe.commerce.app.service.dto.CuponDTO;

/**
 * Service Interface for managing {@link pe.commerce.app.domain.Cupon}.
 */
public interface CuponService {
    /**
     * Save a cupon.
     *
     * @param cuponDTO the entity to save.
     * @return the persisted entity.
     */
    CuponDTO save(CuponDTO cuponDTO);

    /**
     * Updates a cupon.
     *
     * @param cuponDTO the entity to update.
     * @return the persisted entity.
     */
    CuponDTO update(CuponDTO cuponDTO);

    /**
     * Partially updates a cupon.
     *
     * @param cuponDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CuponDTO> partialUpdate(CuponDTO cuponDTO);

    /**
     * Get all the cupons.
     *
     * @return the list of entities.
     */
    List<CuponDTO> findAll();

    /**
     * Get the "id" cupon.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CuponDTO> findOne(Long id);

    /**
     * Delete the "id" cupon.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
