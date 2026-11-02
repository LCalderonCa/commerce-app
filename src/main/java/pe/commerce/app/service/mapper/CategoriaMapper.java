package pe.commerce.app.service.mapper;

import org.mapstruct.*;
import pe.commerce.app.domain.Categoria;
import pe.commerce.app.service.dto.CategoriaDTO;

/**
 * Mapper for the entity {@link Categoria} and its DTO {@link CategoriaDTO}.
 */
@Mapper(componentModel = "spring")
public interface CategoriaMapper extends EntityMapper<CategoriaDTO, Categoria> {
    @Mapping(target = "padre", source = "padre", qualifiedByName = "categoriaNombre")
    CategoriaDTO toDto(Categoria s);

    @Named("categoriaNombre")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombre", source = "nombre")
    CategoriaDTO toDtoCategoriaNombre(Categoria categoria);
}
