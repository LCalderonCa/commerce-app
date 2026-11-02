package pe.commerce.app.service.mapper;

import org.mapstruct.*;
import pe.commerce.app.domain.MedioContacto;
import pe.commerce.app.domain.Persona;
import pe.commerce.app.service.dto.MedioContactoDTO;
import pe.commerce.app.service.dto.PersonaDTO;

/**
 * Mapper for the entity {@link MedioContacto} and its DTO {@link MedioContactoDTO}.
 */
@Mapper(componentModel = "spring")
public interface MedioContactoMapper extends EntityMapper<MedioContactoDTO, MedioContacto> {
    @Mapping(target = "persona", source = "persona", qualifiedByName = "personaNombres")
    MedioContactoDTO toDto(MedioContacto s);

    @Named("personaNombres")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombres", source = "nombres")
    PersonaDTO toDtoPersonaNombres(Persona persona);
}
