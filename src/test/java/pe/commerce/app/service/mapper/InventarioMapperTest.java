package pe.commerce.app.service.mapper;

import static pe.commerce.app.domain.InventarioAsserts.*;
import static pe.commerce.app.domain.InventarioTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InventarioMapperTest {

    private InventarioMapper inventarioMapper;

    @BeforeEach
    void setUp() {
        inventarioMapper = new InventarioMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getInventarioSample1();
        var actual = inventarioMapper.toEntity(inventarioMapper.toDto(expected));
        assertInventarioAllPropertiesEquals(expected, actual);
    }
}
