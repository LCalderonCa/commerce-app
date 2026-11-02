package pe.commerce.app.service.mapper;

import org.mapstruct.*;
import pe.commerce.app.domain.Inventario;
import pe.commerce.app.domain.Producto;
import pe.commerce.app.domain.Tienda;
import pe.commerce.app.service.dto.InventarioDTO;
import pe.commerce.app.service.dto.ProductoDTO;
import pe.commerce.app.service.dto.TiendaDTO;

/**
 * Mapper for the entity {@link Inventario} and its DTO {@link InventarioDTO}.
 */
@Mapper(componentModel = "spring")
public interface InventarioMapper extends EntityMapper<InventarioDTO, Inventario> {
    @Mapping(target = "producto", source = "producto", qualifiedByName = "productoSku")
    @Mapping(target = "tienda", source = "tienda", qualifiedByName = "tiendaNombre")
    InventarioDTO toDto(Inventario s);

    @Named("productoSku")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "sku", source = "sku")
    ProductoDTO toDtoProductoSku(Producto producto);

    @Named("tiendaNombre")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombre", source = "nombre")
    TiendaDTO toDtoTiendaNombre(Tienda tienda);
}
