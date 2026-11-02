package pe.commerce.app.service.mapper;

import static pe.commerce.app.domain.TiendaAsserts.*;
import static pe.commerce.app.domain.TiendaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TiendaMapperTest {

    private TiendaMapper tiendaMapper;

    @BeforeEach
    void setUp() {
        tiendaMapper = new TiendaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getTiendaSample1();
        var actual = tiendaMapper.toEntity(tiendaMapper.toDto(expected));
        assertTiendaAllPropertiesEquals(expected, actual);
    }
}
