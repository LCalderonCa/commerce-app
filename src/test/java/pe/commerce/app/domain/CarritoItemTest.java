package pe.commerce.app.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static pe.commerce.app.domain.CarritoItemTestSamples.*;
import static pe.commerce.app.domain.CarritoTestSamples.*;
import static pe.commerce.app.domain.PedidoDetalleTestSamples.*;
import static pe.commerce.app.domain.ProductoTestSamples.*;

import org.junit.jupiter.api.Test;
import pe.commerce.app.web.rest.TestUtil;

class CarritoItemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CarritoItem.class);
        CarritoItem carritoItem1 = getCarritoItemSample1();
        CarritoItem carritoItem2 = new CarritoItem();
        assertThat(carritoItem1).isNotEqualTo(carritoItem2);

        carritoItem2.setId(carritoItem1.getId());
        assertThat(carritoItem1).isEqualTo(carritoItem2);

        carritoItem2 = getCarritoItemSample2();
        assertThat(carritoItem1).isNotEqualTo(carritoItem2);
    }

    @Test
    void productoTest() {
        CarritoItem carritoItem = getCarritoItemRandomSampleGenerator();
        Producto productoBack = getProductoRandomSampleGenerator();

        carritoItem.setProducto(productoBack);
        assertThat(carritoItem.getProducto()).isEqualTo(productoBack);

        carritoItem.producto(null);
        assertThat(carritoItem.getProducto()).isNull();
    }

    @Test
    void carritoTest() {
        CarritoItem carritoItem = getCarritoItemRandomSampleGenerator();
        Carrito carritoBack = getCarritoRandomSampleGenerator();

        carritoItem.setCarrito(carritoBack);
        assertThat(carritoItem.getCarrito()).isEqualTo(carritoBack);

        carritoItem.carrito(null);
        assertThat(carritoItem.getCarrito()).isNull();
    }

    @Test
    void pedidoDetalleTest() {
        CarritoItem carritoItem = getCarritoItemRandomSampleGenerator();
        PedidoDetalle pedidoDetalleBack = getPedidoDetalleRandomSampleGenerator();

        carritoItem.setPedidoDetalle(pedidoDetalleBack);
        assertThat(carritoItem.getPedidoDetalle()).isEqualTo(pedidoDetalleBack);
        assertThat(pedidoDetalleBack.getCarritoItem()).isEqualTo(carritoItem);

        carritoItem.pedidoDetalle(null);
        assertThat(carritoItem.getPedidoDetalle()).isNull();
        assertThat(pedidoDetalleBack.getCarritoItem()).isNull();
    }
}
