package pe.commerce.app.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static pe.commerce.app.domain.PedidoEstadoHistorialTestSamples.*;
import static pe.commerce.app.domain.PedidoTestSamples.*;

import org.junit.jupiter.api.Test;
import pe.commerce.app.web.rest.TestUtil;

class PedidoEstadoHistorialTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PedidoEstadoHistorial.class);
        PedidoEstadoHistorial pedidoEstadoHistorial1 = getPedidoEstadoHistorialSample1();
        PedidoEstadoHistorial pedidoEstadoHistorial2 = new PedidoEstadoHistorial();
        assertThat(pedidoEstadoHistorial1).isNotEqualTo(pedidoEstadoHistorial2);

        pedidoEstadoHistorial2.setId(pedidoEstadoHistorial1.getId());
        assertThat(pedidoEstadoHistorial1).isEqualTo(pedidoEstadoHistorial2);

        pedidoEstadoHistorial2 = getPedidoEstadoHistorialSample2();
        assertThat(pedidoEstadoHistorial1).isNotEqualTo(pedidoEstadoHistorial2);
    }

    @Test
    void pedidoTest() {
        PedidoEstadoHistorial pedidoEstadoHistorial = getPedidoEstadoHistorialRandomSampleGenerator();
        Pedido pedidoBack = getPedidoRandomSampleGenerator();

        pedidoEstadoHistorial.setPedido(pedidoBack);
        assertThat(pedidoEstadoHistorial.getPedido()).isEqualTo(pedidoBack);

        pedidoEstadoHistorial.pedido(null);
        assertThat(pedidoEstadoHistorial.getPedido()).isNull();
    }
}
