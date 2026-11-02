package pe.commerce.app.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pe.commerce.app.service.dto.ResenaDTO;

/**
 * Service Interface for managing {@link pe.commerce.app.domain.Resena}.
 */
public interface ResenaService {
    /**
     * Save a resena.
     *
     * @param resenaDTO the entity to save.
     * @return the persisted entity.
     */
    ResenaDTO save(ResenaDTO resenaDTO);

    /**
     * Updates a resena.
     *
     * @param resenaDTO the entity to update.
     * @return the persisted entity.
     */
    ResenaDTO update(ResenaDTO resenaDTO);

    /**
     * Partially updates a resena.
     *
     * @param resenaDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ResenaDTO> partialUpdate(ResenaDTO resenaDTO);

    /**
     * Get all the resenas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ResenaDTO> findAll(Pageable pageable);

    /**
     * Get all the resenas with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ResenaDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" resena.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ResenaDTO> findOne(Long id);

    /**
     * Delete the "id" resena.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
