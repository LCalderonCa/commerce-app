package pe.commerce.app.service.mapper;

import static pe.commerce.app.domain.PagoAsserts.*;
import static pe.commerce.app.domain.PagoTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PagoMapperTest {

    private PagoMapper pagoMapper;

    @BeforeEach
    void setUp() {
        pagoMapper = new PagoMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPagoSample1();
        var actual = pagoMapper.toEntity(pagoMapper.toDto(expected));
        assertPagoAllPropertiesEquals(expected, actual);
    }
}
