package pe.commerce.app.service.mapper;

import org.mapstruct.*;
import pe.commerce.app.domain.DocumentoIdentidad;
import pe.commerce.app.domain.Persona;
import pe.commerce.app.service.dto.DocumentoIdentidadDTO;
import pe.commerce.app.service.dto.PersonaDTO;

/**
 * Mapper for the entity {@link DocumentoIdentidad} and its DTO {@link DocumentoIdentidadDTO}.
 */
@Mapper(componentModel = "spring")
public interface DocumentoIdentidadMapper extends EntityMapper<DocumentoIdentidadDTO, DocumentoIdentidad> {
    @Mapping(target = "persona", source = "persona", qualifiedByName = "personaNombres")
    DocumentoIdentidadDTO toDto(DocumentoIdentidad s);

    @Named("personaNombres")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombres", source = "nombres")
    PersonaDTO toDtoPersonaNombres(Persona persona);
}
