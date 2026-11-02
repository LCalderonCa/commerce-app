package pe.commerce.app.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static pe.commerce.app.domain.ComprobanteTestSamples.*;
import static pe.commerce.app.domain.ComprobanteTestSamples.*;
import static pe.commerce.app.domain.PedidoTestSamples.*;

import org.junit.jupiter.api.Test;
import pe.commerce.app.web.rest.TestUtil;

class ComprobanteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Comprobante.class);
        Comprobante comprobante1 = getComprobanteSample1();
        Comprobante comprobante2 = new Comprobante();
        assertThat(comprobante1).isNotEqualTo(comprobante2);

        comprobante2.setId(comprobante1.getId());
        assertThat(comprobante1).isEqualTo(comprobante2);

        comprobante2 = getComprobanteSample2();
        assertThat(comprobante1).isNotEqualTo(comprobante2);
    }

    @Test
    void comprobanteReferenciaTest() {
        Comprobante comprobante = getComprobanteRandomSampleGenerator();
        Comprobante comprobanteBack = getComprobanteRandomSampleGenerator();

        comprobante.setComprobanteReferencia(comprobanteBack);
        assertThat(comprobante.getComprobanteReferencia()).isEqualTo(comprobanteBack);

        comprobante.comprobanteReferencia(null);
        assertThat(comprobante.getComprobanteReferencia()).isNull();
    }

    @Test
    void pedidoTest() {
        Comprobante comprobante = getComprobanteRandomSampleGenerator();
        Pedido pedidoBack = getPedidoRandomSampleGenerator();

        comprobante.setPedido(pedidoBack);
        assertThat(comprobante.getPedido()).isEqualTo(pedidoBack);

        comprobante.pedido(null);
        assertThat(comprobante.getPedido()).isNull();
    }
}
