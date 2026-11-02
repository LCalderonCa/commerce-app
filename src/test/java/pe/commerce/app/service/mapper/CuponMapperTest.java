package pe.commerce.app.service.mapper;

import static pe.commerce.app.domain.CuponAsserts.*;
import static pe.commerce.app.domain.CuponTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CuponMapperTest {

    private CuponMapper cuponMapper;

    @BeforeEach
    void setUp() {
        cuponMapper = new CuponMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCuponSample1();
        var actual = cuponMapper.toEntity(cuponMapper.toDto(expected));
        assertCuponAllPropertiesEquals(expected, actual);
    }
}
