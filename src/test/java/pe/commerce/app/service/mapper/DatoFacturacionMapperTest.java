package pe.commerce.app.service.mapper;

import static pe.commerce.app.domain.DatoFacturacionAsserts.*;
import static pe.commerce.app.domain.DatoFacturacionTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DatoFacturacionMapperTest {

    private DatoFacturacionMapper datoFacturacionMapper;

    @BeforeEach
    void setUp() {
        datoFacturacionMapper = new DatoFacturacionMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getDatoFacturacionSample1();
        var actual = datoFacturacionMapper.toEntity(datoFacturacionMapper.toDto(expected));
        assertDatoFacturacionAllPropertiesEquals(expected, actual);
    }
}
