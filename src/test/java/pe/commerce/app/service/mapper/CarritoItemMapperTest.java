package pe.commerce.app.service.mapper;

import static pe.commerce.app.domain.CarritoItemAsserts.*;
import static pe.commerce.app.domain.CarritoItemTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CarritoItemMapperTest {

    private CarritoItemMapper carritoItemMapper;

    @BeforeEach
    void setUp() {
        carritoItemMapper = new CarritoItemMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCarritoItemSample1();
        var actual = carritoItemMapper.toEntity(carritoItemMapper.toDto(expected));
        assertCarritoItemAllPropertiesEquals(expected, actual);
    }
}
