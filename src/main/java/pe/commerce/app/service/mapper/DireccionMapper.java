package pe.commerce.app.service.mapper;

import org.mapstruct.*;
import pe.commerce.app.domain.Cliente;
import pe.commerce.app.domain.Direccion;
import pe.commerce.app.domain.Ubigeo;
import pe.commerce.app.service.dto.ClienteDTO;
import pe.commerce.app.service.dto.DireccionDTO;
import pe.commerce.app.service.dto.UbigeoDTO;

/**
 * Mapper for the entity {@link Direccion} and its DTO {@link DireccionDTO}.
 */
@Mapper(componentModel = "spring")
public interface DireccionMapper extends EntityMapper<DireccionDTO, Direccion> {
    @Mapping(target = "ubigeo", source = "ubigeo", qualifiedByName = "ubigeoCodigo")
    @Mapping(target = "cliente", source = "cliente", qualifiedByName = "clienteCodigo")
    DireccionDTO toDto(Direccion s);

    @Named("ubigeoCodigo")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "codigo", source = "codigo")
    UbigeoDTO toDtoUbigeoCodigo(Ubigeo ubigeo);

    @Named("clienteCodigo")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "codigo", source = "codigo")
    ClienteDTO toDtoClienteCodigo(Cliente cliente);
}
