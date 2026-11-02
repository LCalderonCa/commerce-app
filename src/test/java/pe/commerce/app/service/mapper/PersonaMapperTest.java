package pe.commerce.app.service.mapper;

import static pe.commerce.app.domain.PersonaAsserts.*;
import static pe.commerce.app.domain.PersonaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PersonaMapperTest {

    private PersonaMapper personaMapper;

    @BeforeEach
    void setUp() {
        personaMapper = new PersonaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPersonaSample1();
        var actual = personaMapper.toEntity(personaMapper.toDto(expected));
        assertPersonaAllPropertiesEquals(expected, actual);
    }
}
