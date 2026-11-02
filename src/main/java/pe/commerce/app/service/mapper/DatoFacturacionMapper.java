package pe.commerce.app.service.mapper;

import org.mapstruct.*;
import pe.commerce.app.domain.Cliente;
import pe.commerce.app.domain.DatoFacturacion;
import pe.commerce.app.service.dto.ClienteDTO;
import pe.commerce.app.service.dto.DatoFacturacionDTO;

/**
 * Mapper for the entity {@link DatoFacturacion} and its DTO {@link DatoFacturacionDTO}.
 */
@Mapper(componentModel = "spring")
public interface DatoFacturacionMapper extends EntityMapper<DatoFacturacionDTO, DatoFacturacion> {
    @Mapping(target = "cliente", source = "cliente", qualifiedByName = "clienteCodigo")
    DatoFacturacionDTO toDto(DatoFacturacion s);

    @Named("clienteCodigo")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "codigo", source = "codigo")
    ClienteDTO toDtoClienteCodigo(Cliente cliente);
}
