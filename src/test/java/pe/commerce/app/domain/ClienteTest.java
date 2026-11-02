package pe.commerce.app.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static pe.commerce.app.domain.CarritoTestSamples.*;
import static pe.commerce.app.domain.ClienteTestSamples.*;
import static pe.commerce.app.domain.DatoFacturacionTestSamples.*;
import static pe.commerce.app.domain.DireccionTestSamples.*;
import static pe.commerce.app.domain.PedidoTestSamples.*;
import static pe.commerce.app.domain.PersonaTestSamples.*;
import static pe.commerce.app.domain.ResenaTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import pe.commerce.app.web.rest.TestUtil;

class ClienteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cliente.class);
        Cliente cliente1 = getClienteSample1();
        Cliente cliente2 = new Cliente();
        assertThat(cliente1).isNotEqualTo(cliente2);

        cliente2.setId(cliente1.getId());
        assertThat(cliente1).isEqualTo(cliente2);

        cliente2 = getClienteSample2();
        assertThat(cliente1).isNotEqualTo(cliente2);
    }

    @Test
    void personaTest() {
        Cliente cliente = getClienteRandomSampleGenerator();
        Persona personaBack = getPersonaRandomSampleGenerator();

        cliente.setPersona(personaBack);
        assertThat(cliente.getPersona()).isEqualTo(personaBack);

        cliente.persona(null);
        assertThat(cliente.getPersona()).isNull();
    }

    @Test
    void direccionesTest() {
        Cliente cliente = getClienteRandomSampleGenerator();
        Direccion direccionBack = getDireccionRandomSampleGenerator();

        cliente.addDirecciones(direccionBack);
        assertThat(cliente.getDireccioneses()).containsOnly(direccionBack);
        assertThat(direccionBack.getCliente()).isEqualTo(cliente);

        cliente.removeDirecciones(direccionBack);
        assertThat(cliente.getDireccioneses()).doesNotContain(direccionBack);
        assertThat(direccionBack.getCliente()).isNull();

        cliente.direccioneses(new HashSet<>(Set.of(direccionBack)));
        assertThat(cliente.getDireccioneses()).containsOnly(direccionBack);
        assertThat(direccionBack.getCliente()).isEqualTo(cliente);

        cliente.setDireccioneses(new HashSet<>());
        assertThat(cliente.getDireccioneses()).doesNotContain(direccionBack);
        assertThat(direccionBack.getCliente()).isNull();
    }

    @Test
    void datosFacturacionTest() {
        Cliente cliente = getClienteRandomSampleGenerator();
        DatoFacturacion datoFacturacionBack = getDatoFacturacionRandomSampleGenerator();

        cliente.addDatosFacturacion(datoFacturacionBack);
        assertThat(cliente.getDatosFacturacions()).containsOnly(datoFacturacionBack);
        assertThat(datoFacturacionBack.getCliente()).isEqualTo(cliente);

        cliente.removeDatosFacturacion(datoFacturacionBack);
        assertThat(cliente.getDatosFacturacions()).doesNotContain(datoFacturacionBack);
        assertThat(datoFacturacionBack.getCliente()).isNull();

        cliente.datosFacturacions(new HashSet<>(Set.of(datoFacturacionBack)));
        assertThat(cliente.getDatosFacturacions()).containsOnly(datoFacturacionBack);
        assertThat(datoFacturacionBack.getCliente()).isEqualTo(cliente);

        cliente.setDatosFacturacions(new HashSet<>());
        assertThat(cliente.getDatosFacturacions()).doesNotContain(datoFacturacionBack);
        assertThat(datoFacturacionBack.getCliente()).isNull();
    }

    @Test
    void pedidosTest() {
        Cliente cliente = getClienteRandomSampleGenerator();
        Pedido pedidoBack = getPedidoRandomSampleGenerator();

        cliente.addPedidos(pedidoBack);
        assertThat(cliente.getPedidoses()).containsOnly(pedidoBack);
        assertThat(pedidoBack.getCliente()).isEqualTo(cliente);

        cliente.removePedidos(pedidoBack);
        assertThat(cliente.getPedidoses()).doesNotContain(pedidoBack);
        assertThat(pedidoBack.getCliente()).isNull();

        cliente.pedidoses(new HashSet<>(Set.of(pedidoBack)));
        assertThat(cliente.getPedidoses()).containsOnly(pedidoBack);
        assertThat(pedidoBack.getCliente()).isEqualTo(cliente);

        cliente.setPedidoses(new HashSet<>());
        assertThat(cliente.getPedidoses()).doesNotContain(pedidoBack);
        assertThat(pedidoBack.getCliente()).isNull();
    }

    @Test
    void resenasTest() {
        Cliente cliente = getClienteRandomSampleGenerator();
        Resena resenaBack = getResenaRandomSampleGenerator();

        cliente.addResenas(resenaBack);
        assertThat(cliente.getResenases()).containsOnly(resenaBack);
        assertThat(resenaBack.getCliente()).isEqualTo(cliente);

        cliente.removeResenas(resenaBack);
        assertThat(cliente.getResenases()).doesNotContain(resenaBack);
        assertThat(resenaBack.getCliente()).isNull();

        cliente.resenases(new HashSet<>(Set.of(resenaBack)));
        assertThat(cliente.getResenases()).containsOnly(resenaBack);
        assertThat(resenaBack.getCliente()).isEqualTo(cliente);

        cliente.setResenases(new HashSet<>());
        assertThat(cliente.getResenases()).doesNotContain(resenaBack);
        assertThat(resenaBack.getCliente()).isNull();
    }

    @Test
    void carritoTest() {
        Cliente cliente = getClienteRandomSampleGenerator();
        Carrito carritoBack = getCarritoRandomSampleGenerator();

        cliente.setCarrito(carritoBack);
        assertThat(cliente.getCarrito()).isEqualTo(carritoBack);
        assertThat(carritoBack.getCliente()).isEqualTo(cliente);

        cliente.carrito(null);
        assertThat(cliente.getCarrito()).isNull();
        assertThat(carritoBack.getCliente()).isNull();
    }
}
