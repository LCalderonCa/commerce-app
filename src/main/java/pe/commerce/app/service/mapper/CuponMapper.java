package pe.commerce.app.service.mapper;

import org.mapstruct.*;
import pe.commerce.app.domain.Cupon;
import pe.commerce.app.service.dto.CuponDTO;

/**
 * Mapper for the entity {@link Cupon} and its DTO {@link CuponDTO}.
 */
@Mapper(componentModel = "spring")
public interface CuponMapper extends EntityMapper<CuponDTO, Cupon> {}
