package pe.commerce.app.service;

import java.util.List;
import java.util.Optional;
import pe.commerce.app.service.dto.CategoriaDTO;

/**
 * Service Interface for managing {@link pe.commerce.app.domain.Categoria}.
 */
public interface CategoriaService {
    /**
     * Save a categoria.
     *
     * @param categoriaDTO the entity to save.
     * @return the persisted entity.
     */
    CategoriaDTO save(CategoriaDTO categoriaDTO);

    /**
     * Updates a categoria.
     *
     * @param categoriaDTO the entity to update.
     * @return the persisted entity.
     */
    CategoriaDTO update(CategoriaDTO categoriaDTO);

    /**
     * Partially updates a categoria.
     *
     * @param categoriaDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CategoriaDTO> partialUpdate(CategoriaDTO categoriaDTO);

    /**
     * Get all the categorias.
     *
     * @return the list of entities.
     */
    List<CategoriaDTO> findAll();

    /**
     * Get all the categorias with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    List<CategoriaDTO> findAllWithEagerRelationships();

    /**
     * Get the "id" categoria.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CategoriaDTO> findOne(Long id);

    /**
     * Delete the "id" categoria.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
