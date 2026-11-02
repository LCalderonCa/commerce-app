package pe.commerce.app.service.mapper;

import static pe.commerce.app.domain.ResenaAsserts.*;
import static pe.commerce.app.domain.ResenaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ResenaMapperTest {

    private ResenaMapper resenaMapper;

    @BeforeEach
    void setUp() {
        resenaMapper = new ResenaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getResenaSample1();
        var actual = resenaMapper.toEntity(resenaMapper.toDto(expected));
        assertResenaAllPropertiesEquals(expected, actual);
    }
}
