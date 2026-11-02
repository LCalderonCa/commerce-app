package pe.commerce.app.service.mapper;

import org.mapstruct.*;
import pe.commerce.app.domain.CarritoItem;
import pe.commerce.app.domain.Pedido;
import pe.commerce.app.domain.PedidoDetalle;
import pe.commerce.app.domain.Producto;
import pe.commerce.app.service.dto.CarritoItemDTO;
import pe.commerce.app.service.dto.PedidoDTO;
import pe.commerce.app.service.dto.PedidoDetalleDTO;
import pe.commerce.app.service.dto.ProductoDTO;

/**
 * Mapper for the entity {@link PedidoDetalle} and its DTO {@link PedidoDetalleDTO}.
 */
@Mapper(componentModel = "spring")
public interface PedidoDetalleMapper extends EntityMapper<PedidoDetalleDTO, PedidoDetalle> {
    @Mapping(target = "carritoItem", source = "carritoItem", qualifiedByName = "carritoItemId")
    @Mapping(target = "producto", source = "producto", qualifiedByName = "productoSku")
    @Mapping(target = "pedido", source = "pedido", qualifiedByName = "pedidoNroPedido")
    PedidoDetalleDTO toDto(PedidoDetalle s);

    @Named("carritoItemId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CarritoItemDTO toDtoCarritoItemId(CarritoItem carritoItem);

    @Named("productoSku")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "sku", source = "sku")
    ProductoDTO toDtoProductoSku(Producto producto);

    @Named("pedidoNroPedido")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nroPedido", source = "nroPedido")
    PedidoDTO toDtoPedidoNroPedido(Pedido pedido);
}
