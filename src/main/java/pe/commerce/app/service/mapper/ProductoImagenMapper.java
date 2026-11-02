package pe.commerce.app.service.mapper;

import org.mapstruct.*;
import pe.commerce.app.domain.Producto;
import pe.commerce.app.domain.ProductoImagen;
import pe.commerce.app.service.dto.ProductoDTO;
import pe.commerce.app.service.dto.ProductoImagenDTO;

/**
 * Mapper for the entity {@link ProductoImagen} and its DTO {@link ProductoImagenDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProductoImagenMapper extends EntityMapper<ProductoImagenDTO, ProductoImagen> {
    @Mapping(target = "producto", source = "producto", qualifiedByName = "productoSku")
    ProductoImagenDTO toDto(ProductoImagen s);

    @Named("productoSku")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "sku", source = "sku")
    ProductoDTO toDtoProductoSku(Producto producto);
}
