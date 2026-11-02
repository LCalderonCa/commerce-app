package pe.commerce.app.service.mapper;

import org.mapstruct.*;
import pe.commerce.app.domain.Cliente;
import pe.commerce.app.domain.Persona;
import pe.commerce.app.domain.User;
import pe.commerce.app.service.dto.ClienteDTO;
import pe.commerce.app.service.dto.PersonaDTO;
import pe.commerce.app.service.dto.UserDTO;

/**
 * Mapper for the entity {@link Cliente} and its DTO {@link ClienteDTO}.
 */
@Mapper(componentModel = "spring")
public interface ClienteMapper extends EntityMapper<ClienteDTO, Cliente> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userLogin")
    @Mapping(target = "persona", source = "persona", qualifiedByName = "personaNombres")
    ClienteDTO toDto(Cliente s);

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
}
