package pe.commerce.app.service;

import java.util.List;
import java.util.Optional;
import pe.commerce.app.service.dto.MedioContactoDTO;

/**
 * Service Interface for managing {@link pe.commerce.app.domain.MedioContacto}.
 */
public interface MedioContactoService {
    /**
     * Save a medioContacto.
     *
     * @param medioContactoDTO the entity to save.
     * @return the persisted entity.
     */
    MedioContactoDTO save(MedioContactoDTO medioContactoDTO);

    /**
     * Updates a medioContacto.
     *
     * @param medioContactoDTO the entity to update.
     * @return the persisted entity.
     */
    MedioContactoDTO update(MedioContactoDTO medioContactoDTO);

    /**
     * Partially updates a medioContacto.
     *
     * @param medioContactoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MedioContactoDTO> partialUpdate(MedioContactoDTO medioContactoDTO);

    /**
     * Get all the medioContactos.
     *
     * @return the list of entities.
     */
    List<MedioContactoDTO> findAll();

    /**
     * Get all the medioContactos with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    List<MedioContactoDTO> findAllWithEagerRelationships();

    /**
     * Get the "id" medioContacto.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MedioContactoDTO> findOne(Long id);

    /**
     * Delete the "id" medioContacto.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
