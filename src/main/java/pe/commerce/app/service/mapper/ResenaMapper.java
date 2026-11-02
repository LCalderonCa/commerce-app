package pe.commerce.app.service.mapper;

import org.mapstruct.*;
import pe.commerce.app.domain.Cliente;
import pe.commerce.app.domain.Producto;
import pe.commerce.app.domain.Resena;
import pe.commerce.app.service.dto.ClienteDTO;
import pe.commerce.app.service.dto.ProductoDTO;
import pe.commerce.app.service.dto.ResenaDTO;

/**
 * Mapper for the entity {@link Resena} and its DTO {@link ResenaDTO}.
 */
@Mapper(componentModel = "spring")
public interface ResenaMapper extends EntityMapper<ResenaDTO, Resena> {
    @Mapping(target = "cliente", source = "cliente", qualifiedByName = "clienteCodigo")
    @Mapping(target = "producto", source = "producto", qualifiedByName = "productoSku")
    ResenaDTO toDto(Resena s);

    @Named("clienteCodigo")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "codigo", source = "codigo")
    ClienteDTO toDtoClienteCodigo(Cliente cliente);

    @Named("productoSku")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "sku", source = "sku")
    ProductoDTO toDtoProductoSku(Producto producto);
}
