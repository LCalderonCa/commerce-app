package pe.commerce.app.service.mapper;

import org.mapstruct.*;
import pe.commerce.app.domain.Cliente;
import pe.commerce.app.domain.Cupon;
import pe.commerce.app.domain.Empleado;
import pe.commerce.app.domain.Pedido;
import pe.commerce.app.domain.Tienda;
import pe.commerce.app.service.dto.ClienteDTO;
import pe.commerce.app.service.dto.CuponDTO;
import pe.commerce.app.service.dto.EmpleadoDTO;
import pe.commerce.app.service.dto.PedidoDTO;
import pe.commerce.app.service.dto.TiendaDTO;

/**
 * Mapper for the entity {@link Pedido} and its DTO {@link PedidoDTO}.
 */
@Mapper(componentModel = "spring")
public interface PedidoMapper extends EntityMapper<PedidoDTO, Pedido> {
    @Mapping(target = "cupon", source = "cupon", qualifiedByName = "cuponCodigo")
    @Mapping(target = "tiendaRecojo", source = "tiendaRecojo", qualifiedByName = "tiendaNombre")
    @Mapping(target = "atendidoPor", source = "atendidoPor", qualifiedByName = "empleadoCodigo")
    @Mapping(target = "cliente", source = "cliente", qualifiedByName = "clienteCodigo")
    PedidoDTO toDto(Pedido s);

    @Named("cuponCodigo")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "codigo", source = "codigo")
    CuponDTO toDtoCuponCodigo(Cupon cupon);

    @Named("tiendaNombre")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombre", source = "nombre")
    TiendaDTO toDtoTiendaNombre(Tienda tienda);

    @Named("empleadoCodigo")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "codigo", source = "codigo")
    EmpleadoDTO toDtoEmpleadoCodigo(Empleado empleado);

    @Named("clienteCodigo")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "codigo", source = "codigo")
    ClienteDTO toDtoClienteCodigo(Cliente cliente);
}
