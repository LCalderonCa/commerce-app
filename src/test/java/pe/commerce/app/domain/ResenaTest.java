package pe.commerce.app.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static pe.commerce.app.domain.ClienteTestSamples.*;
import static pe.commerce.app.domain.ProductoTestSamples.*;
import static pe.commerce.app.domain.ResenaTestSamples.*;

import org.junit.jupiter.api.Test;
import pe.commerce.app.web.rest.TestUtil;

class ResenaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Resena.class);
        Resena resena1 = getResenaSample1();
        Resena resena2 = new Resena();
        assertThat(resena1).isNotEqualTo(resena2);

        resena2.setId(resena1.getId());
        assertThat(resena1).isEqualTo(resena2);

        resena2 = getResenaSample2();
        assertThat(resena1).isNotEqualTo(resena2);
    }

    @Test
    void clienteTest() {
        Resena resena = getResenaRandomSampleGenerator();
        Cliente clienteBack = getClienteRandomSampleGenerator();

        resena.setCliente(clienteBack);
        assertThat(resena.getCliente()).isEqualTo(clienteBack);

        resena.cliente(null);
        assertThat(resena.getCliente()).isNull();
    }

    @Test
    void productoTest() {
        Resena resena = getResenaRandomSampleGenerator();
        Producto productoBack = getProductoRandomSampleGenerator();

        resena.setProducto(productoBack);
        assertThat(resena.getProducto()).isEqualTo(productoBack);

        resena.producto(null);
        assertThat(resena.getProducto()).isNull();
    }
}
