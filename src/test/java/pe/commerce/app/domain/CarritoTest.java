package pe.commerce.app.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static pe.commerce.app.domain.CarritoItemTestSamples.*;
import static pe.commerce.app.domain.CarritoTestSamples.*;
import static pe.commerce.app.domain.ClienteTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import pe.commerce.app.web.rest.TestUtil;

class CarritoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Carrito.class);
        Carrito carrito1 = getCarritoSample1();
        Carrito carrito2 = new Carrito();
        assertThat(carrito1).isNotEqualTo(carrito2);

        carrito2.setId(carrito1.getId());
        assertThat(carrito1).isEqualTo(carrito2);

        carrito2 = getCarritoSample2();
        assertThat(carrito1).isNotEqualTo(carrito2);
    }

    @Test
    void clienteTest() {
        Carrito carrito = getCarritoRandomSampleGenerator();
        Cliente clienteBack = getClienteRandomSampleGenerator();

        carrito.setCliente(clienteBack);
        assertThat(carrito.getCliente()).isEqualTo(clienteBack);

        carrito.cliente(null);
        assertThat(carrito.getCliente()).isNull();
    }

    @Test
    void itemsTest() {
        Carrito carrito = getCarritoRandomSampleGenerator();
        CarritoItem carritoItemBack = getCarritoItemRandomSampleGenerator();

        carrito.addItems(carritoItemBack);
        assertThat(carrito.getItemses()).containsOnly(carritoItemBack);
        assertThat(carritoItemBack.getCarrito()).isEqualTo(carrito);

        carrito.removeItems(carritoItemBack);
        assertThat(carrito.getItemses()).doesNotContain(carritoItemBack);
        assertThat(carritoItemBack.getCarrito()).isNull();

        carrito.itemses(new HashSet<>(Set.of(carritoItemBack)));
        assertThat(carrito.getItemses()).containsOnly(carritoItemBack);
        assertThat(carritoItemBack.getCarrito()).isEqualTo(carrito);

        carrito.setItemses(new HashSet<>());
        assertThat(carrito.getItemses()).doesNotContain(carritoItemBack);
        assertThat(carritoItemBack.getCarrito()).isNull();
    }
}
