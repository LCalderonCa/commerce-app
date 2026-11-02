package pe.commerce.app.service.mapper;

import org.mapstruct.*;
import pe.commerce.app.domain.Direccion;
import pe.commerce.app.domain.Envio;
import pe.commerce.app.domain.Pedido;
import pe.commerce.app.service.dto.DireccionDTO;
import pe.commerce.app.service.dto.EnvioDTO;
import pe.commerce.app.service.dto.PedidoDTO;

/**
 * Mapper for the entity {@link Envio} and its DTO {@link EnvioDTO}.
 */
@Mapper(componentModel = "spring")
public interface EnvioMapper extends EntityMapper<EnvioDTO, Envio> {
    @Mapping(target = "pedido", source = "pedido", qualifiedByName = "pedidoNroPedido")
    @Mapping(target = "direccion", source = "direccion", qualifiedByName = "direccionAlias")
    EnvioDTO toDto(Envio s);

    @Named("pedidoNroPedido")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nroPedido", source = "nroPedido")
    PedidoDTO toDtoPedidoNroPedido(Pedido pedido);

    @Named("direccionAlias")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "alias", source = "alias")
    DireccionDTO toDtoDireccionAlias(Direccion direccion);
}
