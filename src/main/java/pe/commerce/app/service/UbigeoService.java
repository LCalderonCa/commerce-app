package pe.commerce.app.service;

import java.util.List;
import java.util.Optional;
import pe.commerce.app.service.dto.UbigeoDTO;

/**
 * Service Interface for managing {@link pe.commerce.app.domain.Ubigeo}.
 */
public interface UbigeoService {
    /**
     * Save a ubigeo.
     *
     * @param ubigeoDTO the entity to save.
     * @return the persisted entity.
     */
    UbigeoDTO save(UbigeoDTO ubigeoDTO);

    /**
     * Updates a ubigeo.
     *
     * @param ubigeoDTO the entity to update.
     * @return the persisted entity.
     */
    UbigeoDTO update(UbigeoDTO ubigeoDTO);

    /**
     * Partially updates a ubigeo.
     *
     * @param ubigeoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<UbigeoDTO> partialUpdate(UbigeoDTO ubigeoDTO);

    /**
     * Get all the ubigeos.
     *
     * @return the list of entities.
     */
    List<UbigeoDTO> findAll();

    /**
     * Get the "id" ubigeo.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<UbigeoDTO> findOne(Long id);

    /**
     * Delete the "id" ubigeo.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
