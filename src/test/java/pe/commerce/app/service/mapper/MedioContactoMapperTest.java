package pe.commerce.app.service.mapper;

import static pe.commerce.app.domain.MedioContactoAsserts.*;
import static pe.commerce.app.domain.MedioContactoTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MedioContactoMapperTest {

    private MedioContactoMapper medioContactoMapper;

    @BeforeEach
    void setUp() {
        medioContactoMapper = new MedioContactoMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getMedioContactoSample1();
        var actual = medioContactoMapper.toEntity(medioContactoMapper.toDto(expected));
        assertMedioContactoAllPropertiesEquals(expected, actual);
    }
}
