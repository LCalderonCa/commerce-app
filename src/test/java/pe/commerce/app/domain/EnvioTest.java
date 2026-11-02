package pe.commerce.app.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static pe.commerce.app.domain.DireccionTestSamples.*;
import static pe.commerce.app.domain.EnvioTestSamples.*;
import static pe.commerce.app.domain.PedidoTestSamples.*;

import org.junit.jupiter.api.Test;
import pe.commerce.app.web.rest.TestUtil;

class EnvioTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Envio.class);
        Envio envio1 = getEnvioSample1();
        Envio envio2 = new Envio();
        assertThat(envio1).isNotEqualTo(envio2);

        envio2.setId(envio1.getId());
        assertThat(envio1).isEqualTo(envio2);

        envio2 = getEnvioSample2();
        assertThat(envio1).isNotEqualTo(envio2);
    }

    @Test
    void pedidoTest() {
        Envio envio = getEnvioRandomSampleGenerator();
        Pedido pedidoBack = getPedidoRandomSampleGenerator();

        envio.setPedido(pedidoBack);
        assertThat(envio.getPedido()).isEqualTo(pedidoBack);

        envio.pedido(null);
        assertThat(envio.getPedido()).isNull();
    }

    @Test
    void direccionTest() {
        Envio envio = getEnvioRandomSampleGenerator();
        Direccion direccionBack = getDireccionRandomSampleGenerator();

        envio.setDireccion(direccionBack);
        assertThat(envio.getDireccion()).isEqualTo(direccionBack);

        envio.direccion(null);
        assertThat(envio.getDireccion()).isNull();
    }
}
