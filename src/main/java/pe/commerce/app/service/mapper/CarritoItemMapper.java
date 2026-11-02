package pe.commerce.app.service.mapper;

import org.mapstruct.*;
import pe.commerce.app.domain.Carrito;
import pe.commerce.app.domain.CarritoItem;
import pe.commerce.app.domain.Producto;
import pe.commerce.app.service.dto.CarritoDTO;
import pe.commerce.app.service.dto.CarritoItemDTO;
import pe.commerce.app.service.dto.ProductoDTO;

/**
 * Mapper for the entity {@link CarritoItem} and its DTO {@link CarritoItemDTO}.
 */
@Mapper(componentModel = "spring")
public interface CarritoItemMapper extends EntityMapper<CarritoItemDTO, CarritoItem> {
    @Mapping(target = "producto", source = "producto", qualifiedByName = "productoSku")
    @Mapping(target = "carrito", source = "carrito", qualifiedByName = "carritoId")
    CarritoItemDTO toDto(CarritoItem s);

    @Named("productoSku")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "sku", source = "sku")
    ProductoDTO toDtoProductoSku(Producto producto);

    @Named("carritoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CarritoDTO toDtoCarritoId(Carrito carrito);
}
