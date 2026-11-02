package pe.commerce.app.service;

import java.util.List;
import java.util.Optional;
import pe.commerce.app.service.dto.EnvioDTO;

/**
 * Service Interface for managing {@link pe.commerce.app.domain.Envio}.
 */
public interface EnvioService {
    /**
     * Save a envio.
     *
     * @param envioDTO the entity to save.
     * @return the persisted entity.
     */
    EnvioDTO save(EnvioDTO envioDTO);

    /**
     * Updates a envio.
     *
     * @param envioDTO the entity to update.
     * @return the persisted entity.
     */
    EnvioDTO update(EnvioDTO envioDTO);

    /**
     * Partially updates a envio.
     *
     * @param envioDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<EnvioDTO> partialUpdate(EnvioDTO envioDTO);

    /**
     * Get all the envios.
     *
     * @return the list of entities.
     */
    List<EnvioDTO> findAll();

    /**
     * Get all the envios with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    List<EnvioDTO> findAllWithEagerRelationships();

    /**
     * Get the "id" envio.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EnvioDTO> findOne(Long id);

    /**
     * Delete the "id" envio.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
