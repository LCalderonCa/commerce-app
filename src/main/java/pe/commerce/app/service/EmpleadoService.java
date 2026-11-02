package pe.commerce.app.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pe.commerce.app.service.dto.EmpleadoDTO;

/**
 * Service Interface for managing {@link pe.commerce.app.domain.Empleado}.
 */
public interface EmpleadoService {
    /**
     * Save a empleado.
     *
     * @param empleadoDTO the entity to save.
     * @return the persisted entity.
     */
    EmpleadoDTO save(EmpleadoDTO empleadoDTO);

    /**
     * Updates a empleado.
     *
     * @param empleadoDTO the entity to update.
     * @return the persisted entity.
     */
    EmpleadoDTO update(EmpleadoDTO empleadoDTO);

    /**
     * Partially updates a empleado.
     *
     * @param empleadoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<EmpleadoDTO> partialUpdate(EmpleadoDTO empleadoDTO);

    /**
     * Get all the empleados.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<EmpleadoDTO> findAll(Pageable pageable);

    /**
     * Get all the empleados with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<EmpleadoDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" empleado.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EmpleadoDTO> findOne(Long id);

    /**
     * Delete the "id" empleado.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
