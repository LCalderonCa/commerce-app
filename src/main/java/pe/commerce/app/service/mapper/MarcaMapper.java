package pe.commerce.app.service.mapper;

import org.mapstruct.*;
import pe.commerce.app.domain.Marca;
import pe.commerce.app.service.dto.MarcaDTO;

/**
 * Mapper for the entity {@link Marca} and its DTO {@link MarcaDTO}.
 */
@Mapper(componentModel = "spring")
public interface MarcaMapper extends EntityMapper<MarcaDTO, Marca> {}
