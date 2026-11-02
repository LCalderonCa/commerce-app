package pe.commerce.app.service;

import java.util.List;
import java.util.Optional;
import pe.commerce.app.service.dto.MarcaDTO;

/**
 * Service Interface for managing {@link pe.commerce.app.domain.Marca}.
 */
public interface MarcaService {
    /**
     * Save a marca.
     *
     * @param marcaDTO the entity to save.
     * @return the persisted entity.
     */
    MarcaDTO save(MarcaDTO marcaDTO);

    /**
     * Updates a marca.
     *
     * @param marcaDTO the entity to update.
     * @return the persisted entity.
     */
    MarcaDTO update(MarcaDTO marcaDTO);

    /**
     * Partially updates a marca.
     *
     * @param marcaDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MarcaDTO> partialUpdate(MarcaDTO marcaDTO);

    /**
     * Get all the marcas.
     *
     * @return the list of entities.
     */
    List<MarcaDTO> findAll();

    /**
     * Get the "id" marca.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MarcaDTO> findOne(Long id);

    /**
     * Delete the "id" marca.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
