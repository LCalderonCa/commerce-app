package pe.commerce.app.service.mapper;

import org.mapstruct.*;
import pe.commerce.app.domain.Ubigeo;
import pe.commerce.app.service.dto.UbigeoDTO;

/**
 * Mapper for the entity {@link Ubigeo} and its DTO {@link UbigeoDTO}.
 */
@Mapper(componentModel = "spring")
public interface UbigeoMapper extends EntityMapper<UbigeoDTO, Ubigeo> {}
