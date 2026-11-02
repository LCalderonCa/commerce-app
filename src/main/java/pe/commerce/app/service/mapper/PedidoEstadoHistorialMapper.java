package pe.commerce.app.service.mapper;

import org.mapstruct.*;
import pe.commerce.app.domain.Pedido;
import pe.commerce.app.domain.PedidoEstadoHistorial;
import pe.commerce.app.service.dto.PedidoDTO;
import pe.commerce.app.service.dto.PedidoEstadoHistorialDTO;

/**
 * Mapper for the entity {@link PedidoEstadoHistorial} and its DTO {@link PedidoEstadoHistorialDTO}.
 */
@Mapper(componentModel = "spring")
public interface PedidoEstadoHistorialMapper extends EntityMapper<PedidoEstadoHistorialDTO, PedidoEstadoHistorial> {
    @Mapping(target = "pedido", source = "pedido", qualifiedByName = "pedidoNroPedido")
    PedidoEstadoHistorialDTO toDto(PedidoEstadoHistorial s);

    @Named("pedidoNroPedido")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nroPedido", source = "nroPedido")
    PedidoDTO toDtoPedidoNroPedido(Pedido pedido);
}
