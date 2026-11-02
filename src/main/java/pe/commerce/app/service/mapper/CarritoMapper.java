package pe.commerce.app.service.mapper;

import org.mapstruct.*;
import pe.commerce.app.domain.Carrito;
import pe.commerce.app.domain.Cliente;
import pe.commerce.app.service.dto.CarritoDTO;
import pe.commerce.app.service.dto.ClienteDTO;

/**
 * Mapper for the entity {@link Carrito} and its DTO {@link CarritoDTO}.
 */
@Mapper(componentModel = "spring")
public interface CarritoMapper extends EntityMapper<CarritoDTO, Carrito> {
    @Mapping(target = "cliente", source = "cliente", qualifiedByName = "clienteCodigo")
    CarritoDTO toDto(Carrito s);

    @Named("clienteCodigo")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "codigo", source = "codigo")
    ClienteDTO toDtoClienteCodigo(Cliente cliente);
}
