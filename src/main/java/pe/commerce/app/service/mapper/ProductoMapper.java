package pe.commerce.app.service.mapper;

import org.mapstruct.*;
import pe.commerce.app.domain.Categoria;
import pe.commerce.app.domain.Marca;
import pe.commerce.app.domain.Producto;
import pe.commerce.app.service.dto.CategoriaDTO;
import pe.commerce.app.service.dto.MarcaDTO;
import pe.commerce.app.service.dto.ProductoDTO;

/**
 * Mapper for the entity {@link Producto} and its DTO {@link ProductoDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProductoMapper extends EntityMapper<ProductoDTO, Producto> {
    @Mapping(target = "categoria", source = "categoria", qualifiedByName = "categoriaNombre")
    @Mapping(target = "marca", source = "marca", qualifiedByName = "marcaNombre")
    ProductoDTO toDto(Producto s);

    @Named("categoriaNombre")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombre", source = "nombre")
    CategoriaDTO toDtoCategoriaNombre(Categoria categoria);

    @Named("marcaNombre")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombre", source = "nombre")
    MarcaDTO toDtoMarcaNombre(Marca marca);
}
