package pe.commerce.app.service.mapper;

import org.mapstruct.*;
import pe.commerce.app.domain.Tienda;
import pe.commerce.app.domain.Ubigeo;
import pe.commerce.app.service.dto.TiendaDTO;
import pe.commerce.app.service.dto.UbigeoDTO;

/**
 * Mapper for the entity {@link Tienda} and its DTO {@link TiendaDTO}.
 */
@Mapper(componentModel = "spring")
public interface TiendaMapper extends EntityMapper<TiendaDTO, Tienda> {
    @Mapping(target = "ubigeo", source = "ubigeo", qualifiedByName = "ubigeoCodigo")
    TiendaDTO toDto(Tienda s);

    @Named("ubigeoCodigo")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "codigo", source = "codigo")
    UbigeoDTO toDtoUbigeoCodigo(Ubigeo ubigeo);
}
