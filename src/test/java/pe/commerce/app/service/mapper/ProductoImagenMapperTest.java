package pe.commerce.app.service.mapper;

import static pe.commerce.app.domain.ProductoImagenAsserts.*;
import static pe.commerce.app.domain.ProductoImagenTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProductoImagenMapperTest {

    private ProductoImagenMapper productoImagenMapper;

    @BeforeEach
    void setUp() {
        productoImagenMapper = new ProductoImagenMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getProductoImagenSample1();
        var actual = productoImagenMapper.toEntity(productoImagenMapper.toDto(expected));
        assertProductoImagenAllPropertiesEquals(expected, actual);
    }
}
