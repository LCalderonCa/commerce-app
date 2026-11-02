package pe.commerce.app.service;

import java.util.List;
import java.util.Optional;
import pe.commerce.app.service.dto.ProductoImagenDTO;

/**
 * Service Interface for managing {@link pe.commerce.app.domain.ProductoImagen}.
 */
public interface ProductoImagenService {
    /**
     * Save a productoImagen.
     *
     * @param productoImagenDTO the entity to save.
     * @return the persisted entity.
     */
    ProductoImagenDTO save(ProductoImagenDTO productoImagenDTO);

    /**
     * Updates a productoImagen.
     *
     * @param productoImagenDTO the entity to update.
     * @return the persisted entity.
     */
    ProductoImagenDTO update(ProductoImagenDTO productoImagenDTO);

    /**
     * Partially updates a productoImagen.
     *
     * @param productoImagenDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ProductoImagenDTO> partialUpdate(ProductoImagenDTO productoImagenDTO);

    /**
     * Get all the productoImagens.
     *
     * @return the list of entities.
     */
    List<ProductoImagenDTO> findAll();

    /**
     * Get all the productoImagens with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    List<ProductoImagenDTO> findAllWithEagerRelationships();

    /**
     * Get the "id" productoImagen.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ProductoImagenDTO> findOne(Long id);

    /**
     * Delete the "id" productoImagen.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
