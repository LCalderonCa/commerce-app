package pe.commerce.app.service.mapper;

import static pe.commerce.app.domain.EnvioAsserts.*;
import static pe.commerce.app.domain.EnvioTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EnvioMapperTest {

    private EnvioMapper envioMapper;

    @BeforeEach
    void setUp() {
        envioMapper = new EnvioMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getEnvioSample1();
        var actual = envioMapper.toEntity(envioMapper.toDto(expected));
        assertEnvioAllPropertiesEquals(expected, actual);
    }
}
