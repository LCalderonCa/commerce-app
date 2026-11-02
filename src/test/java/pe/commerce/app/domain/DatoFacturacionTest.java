package pe.commerce.app.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static pe.commerce.app.domain.ClienteTestSamples.*;
import static pe.commerce.app.domain.DatoFacturacionTestSamples.*;

import org.junit.jupiter.api.Test;
import pe.commerce.app.web.rest.TestUtil;

class DatoFacturacionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DatoFacturacion.class);
        DatoFacturacion datoFacturacion1 = getDatoFacturacionSample1();
        DatoFacturacion datoFacturacion2 = new DatoFacturacion();
        assertThat(datoFacturacion1).isNotEqualTo(datoFacturacion2);

        datoFacturacion2.setId(datoFacturacion1.getId());
        assertThat(datoFacturacion1).isEqualTo(datoFacturacion2);

        datoFacturacion2 = getDatoFacturacionSample2();
        assertThat(datoFacturacion1).isNotEqualTo(datoFacturacion2);
    }

    @Test
    void clienteTest() {
        DatoFacturacion datoFacturacion = getDatoFacturacionRandomSampleGenerator();
        Cliente clienteBack = getClienteRandomSampleGenerator();

        datoFacturacion.setCliente(clienteBack);
        assertThat(datoFacturacion.getCliente()).isEqualTo(clienteBack);

        datoFacturacion.cliente(null);
        assertThat(datoFacturacion.getCliente()).isNull();
    }
}
