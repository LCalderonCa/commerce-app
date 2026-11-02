package pe.commerce.app.service.mapper;

import static pe.commerce.app.domain.PedidoDetalleAsserts.*;
import static pe.commerce.app.domain.PedidoDetalleTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PedidoDetalleMapperTest {

    private PedidoDetalleMapper pedidoDetalleMapper;

    @BeforeEach
    void setUp() {
        pedidoDetalleMapper = new PedidoDetalleMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPedidoDetalleSample1();
        var actual = pedidoDetalleMapper.toEntity(pedidoDetalleMapper.toDto(expected));
        assertPedidoDetalleAllPropertiesEquals(expected, actual);
    }
}
