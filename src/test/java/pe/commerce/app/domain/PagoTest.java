package pe.commerce.app.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static pe.commerce.app.domain.PagoTestSamples.*;
import static pe.commerce.app.domain.PedidoTestSamples.*;

import org.junit.jupiter.api.Test;
import pe.commerce.app.web.rest.TestUtil;

class PagoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Pago.class);
        Pago pago1 = getPagoSample1();
        Pago pago2 = new Pago();
        assertThat(pago1).isNotEqualTo(pago2);

        pago2.setId(pago1.getId());
        assertThat(pago1).isEqualTo(pago2);

        pago2 = getPagoSample2();
        assertThat(pago1).isNotEqualTo(pago2);
    }

    @Test
    void pedidoTest() {
        Pago pago = getPagoRandomSampleGenerator();
        Pedido pedidoBack = getPedidoRandomSampleGenerator();

        pago.setPedido(pedidoBack);
        assertThat(pago.getPedido()).isEqualTo(pedidoBack);

        pago.pedido(null);
        assertThat(pago.getPedido()).isNull();
    }
}
