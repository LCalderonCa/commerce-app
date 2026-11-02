package pe.commerce.app.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static pe.commerce.app.domain.ClienteTestSamples.*;
import static pe.commerce.app.domain.DireccionTestSamples.*;
import static pe.commerce.app.domain.UbigeoTestSamples.*;

import org.junit.jupiter.api.Test;
import pe.commerce.app.web.rest.TestUtil;

class DireccionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Direccion.class);
        Direccion direccion1 = getDireccionSample1();
        Direccion direccion2 = new Direccion();
        assertThat(direccion1).isNotEqualTo(direccion2);

        direccion2.setId(direccion1.getId());
        assertThat(direccion1).isEqualTo(direccion2);

        direccion2 = getDireccionSample2();
        assertThat(direccion1).isNotEqualTo(direccion2);
    }

    @Test
    void ubigeoTest() {
        Direccion direccion = getDireccionRandomSampleGenerator();
        Ubigeo ubigeoBack = getUbigeoRandomSampleGenerator();

        direccion.setUbigeo(ubigeoBack);
        assertThat(direccion.getUbigeo()).isEqualTo(ubigeoBack);

        direccion.ubigeo(null);
        assertThat(direccion.getUbigeo()).isNull();
    }

    @Test
    void clienteTest() {
        Direccion direccion = getDireccionRandomSampleGenerator();
        Cliente clienteBack = getClienteRandomSampleGenerator();

        direccion.setCliente(clienteBack);
        assertThat(direccion.getCliente()).isEqualTo(clienteBack);

        direccion.cliente(null);
        assertThat(direccion.getCliente()).isNull();
    }
}
