package pe.commerce.app.service.mapper;

import org.mapstruct.*;
import pe.commerce.app.domain.Persona;
import pe.commerce.app.service.dto.PersonaDTO;

/**
 * Mapper for the entity {@link Persona} and its DTO {@link PersonaDTO}.
 */
@Mapper(componentModel = "spring")
public interface PersonaMapper extends EntityMapper<PersonaDTO, Persona> {}
