package pe.commerce.app.service;

import java.util.List;
import java.util.Optional;
import pe.commerce.app.service.dto.PersonaDTO;

/**
 * Service Interface for managing {@link pe.commerce.app.domain.Persona}.
 */
public interface PersonaService {
    /**
     * Save a persona.
     *
     * @param personaDTO the entity to save.
     * @return the persisted entity.
     */
    PersonaDTO save(PersonaDTO personaDTO);

    /**
     * Updates a persona.
     *
     * @param personaDTO the entity to update.
     * @return the persisted entity.
     */
    PersonaDTO update(PersonaDTO personaDTO);

    /**
     * Partially updates a persona.
     *
     * @param personaDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PersonaDTO> partialUpdate(PersonaDTO personaDTO);

    /**
     * Get all the personas.
     *
     * @return the list of entities.
     */
    List<PersonaDTO> findAll();

    /**
     * Get all the PersonaDTO where Cliente is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<PersonaDTO> findAllWhereClienteIsNull();
    /**
     * Get all the PersonaDTO where Empleado is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<PersonaDTO> findAllWhereEmpleadoIsNull();

    /**
     * Get the "id" persona.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PersonaDTO> findOne(Long id);

    /**
     * Delete the "id" persona.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
