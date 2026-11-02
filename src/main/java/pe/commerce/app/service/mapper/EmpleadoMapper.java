package pe.commerce.app.service.mapper;

import org.mapstruct.*;
import pe.commerce.app.domain.Empleado;
import pe.commerce.app.domain.Persona;
import pe.commerce.app.domain.Tienda;
import pe.commerce.app.domain.User;
import pe.commerce.app.service.dto.EmpleadoDTO;
import pe.commerce.app.service.dto.PersonaDTO;
import pe.commerce.app.service.dto.TiendaDTO;
import pe.commerce.app.service.dto.UserDTO;

/**
 * Mapper for the entity {@link Empleado} and its DTO {@link EmpleadoDTO}.
 */
@Mapper(componentModel = "spring")
public interface EmpleadoMapper extends EntityMapper<EmpleadoDTO, Empleado> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userLogin")
    @Mapping(target = "persona", source = "persona", qualifiedByName = "personaNombres")
    @Mapping(target = "tienda", source = "tienda", qualifiedByName = "tiendaNombre")
    EmpleadoDTO toDto(Empleado s);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);

    @Named("personaNombres")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombres", source = "nombres")
    PersonaDTO toDtoPersonaNombres(Persona persona);

    @Named("tiendaNombre")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombre", source = "nombre")
    TiendaDTO toDtoTiendaNombre(Tienda tienda);
}
