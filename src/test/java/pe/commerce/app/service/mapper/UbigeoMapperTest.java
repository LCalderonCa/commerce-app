package pe.commerce.app.service.mapper;

import static pe.commerce.app.domain.UbigeoAsserts.*;
import static pe.commerce.app.domain.UbigeoTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UbigeoMapperTest {

    private UbigeoMapper ubigeoMapper;

    @BeforeEach
    void setUp() {
        ubigeoMapper = new UbigeoMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getUbigeoSample1();
        var actual = ubigeoMapper.toEntity(ubigeoMapper.toDto(expected));
        assertUbigeoAllPropertiesEquals(expected, actual);
    }
}
