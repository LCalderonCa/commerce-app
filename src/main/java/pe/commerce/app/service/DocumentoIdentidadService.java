package pe.commerce.app.service;

import java.util.List;
import java.util.Optional;
import pe.commerce.app.service.dto.DocumentoIdentidadDTO;

/**
 * Service Interface for managing {@link pe.commerce.app.domain.DocumentoIdentidad}.
 */
public interface DocumentoIdentidadService {
    /**
     * Save a documentoIdentidad.
     *
     * @param documentoIdentidadDTO the entity to save.
     * @return the persisted entity.
     */
    DocumentoIdentidadDTO save(DocumentoIdentidadDTO documentoIdentidadDTO);

    /**
     * Updates a documentoIdentidad.
     *
     * @param documentoIdentidadDTO the entity to update.
     * @return the persisted entity.
     */
    DocumentoIdentidadDTO update(DocumentoIdentidadDTO documentoIdentidadDTO);

    /**
     * Partially updates a documentoIdentidad.
     *
     * @param documentoIdentidadDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DocumentoIdentidadDTO> partialUpdate(DocumentoIdentidadDTO documentoIdentidadDTO);

    /**
     * Get all the documentoIdentidads.
     *
     * @return the list of entities.
     */
    List<DocumentoIdentidadDTO> findAll();

    /**
     * Get all the documentoIdentidads with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    List<DocumentoIdentidadDTO> findAllWithEagerRelationships();

    /**
     * Get the "id" documentoIdentidad.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DocumentoIdentidadDTO> findOne(Long id);

    /**
     * Delete the "id" documentoIdentidad.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
