package pe.commerce.app.service.mapper;

import org.mapstruct.*;
import pe.commerce.app.domain.Comprobante;
import pe.commerce.app.domain.Pedido;
import pe.commerce.app.service.dto.ComprobanteDTO;
import pe.commerce.app.service.dto.PedidoDTO;

/**
 * Mapper for the entity {@link Comprobante} and its DTO {@link ComprobanteDTO}.
 */
@Mapper(componentModel = "spring")
public interface ComprobanteMapper extends EntityMapper<ComprobanteDTO, Comprobante> {
    @Mapping(target = "comprobanteReferencia", source = "comprobanteReferencia", qualifiedByName = "comprobanteNumero")
    @Mapping(target = "pedido", source = "pedido", qualifiedByName = "pedidoNroPedido")
    ComprobanteDTO toDto(Comprobante s);

    @Named("comprobanteNumero")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "numero", source = "numero")
    ComprobanteDTO toDtoComprobanteNumero(Comprobante comprobante);

    @Named("pedidoNroPedido")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nroPedido", source = "nroPedido")
    PedidoDTO toDtoPedidoNroPedido(Pedido pedido);
}
