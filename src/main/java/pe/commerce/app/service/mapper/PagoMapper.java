package pe.commerce.app.service.mapper;

import org.mapstruct.*;
import pe.commerce.app.domain.Pago;
import pe.commerce.app.domain.Pedido;
import pe.commerce.app.service.dto.PagoDTO;
import pe.commerce.app.service.dto.PedidoDTO;

/**
 * Mapper for the entity {@link Pago} and its DTO {@link PagoDTO}.
 */
@Mapper(componentModel = "spring")
public interface PagoMapper extends EntityMapper<PagoDTO, Pago> {
    @Mapping(target = "pedido", source = "pedido", qualifiedByName = "pedidoNroPedido")
    PagoDTO toDto(Pago s);

    @Named("pedidoNroPedido")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nroPedido", source = "nroPedido")
    PedidoDTO toDtoPedidoNroPedido(Pedido pedido);
}
