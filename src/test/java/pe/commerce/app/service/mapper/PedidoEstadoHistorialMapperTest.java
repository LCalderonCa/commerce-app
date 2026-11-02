package pe.commerce.app.service.mapper;

import static pe.commerce.app.domain.PedidoEstadoHistorialAsserts.*;
import static pe.commerce.app.domain.PedidoEstadoHistorialTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PedidoEstadoHistorialMapperTest {

    private PedidoEstadoHistorialMapper pedidoEstadoHistorialMapper;

    @BeforeEach
    void setUp() {
        pedidoEstadoHistorialMapper = new PedidoEstadoHistorialMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPedidoEstadoHistorialSample1();
        var actual = pedidoEstadoHistorialMapper.toEntity(pedidoEstadoHistorialMapper.toDto(expected));
        assertPedidoEstadoHistorialAllPropertiesEquals(expected, actual);
    }
}
