package pe.commerce.app.service.mapper;

import static pe.commerce.app.domain.CarritoAsserts.*;
import static pe.commerce.app.domain.CarritoTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CarritoMapperTest {

    private CarritoMapper carritoMapper;

    @BeforeEach
    void setUp() {
        carritoMapper = new CarritoMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCarritoSample1();
        var actual = carritoMapper.toEntity(carritoMapper.toDto(expected));
        assertCarritoAllPropertiesEquals(expected, actual);
    }
}
