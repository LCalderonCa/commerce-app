package pe.commerce.app.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static pe.commerce.app.domain.CarritoItemTestSamples.*;
import static pe.commerce.app.domain.PedidoDetalleTestSamples.*;
import static pe.commerce.app.domain.PedidoTestSamples.*;
import static pe.commerce.app.domain.ProductoTestSamples.*;

import org.junit.jupiter.api.Test;
import pe.commerce.app.web.rest.TestUtil;

class PedidoDetalleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PedidoDetalle.class);
        PedidoDetalle pedidoDetalle1 = getPedidoDetalleSample1();
        PedidoDetalle pedidoDetalle2 = new PedidoDetalle();
        assertThat(pedidoDetalle1).isNotEqualTo(pedidoDetalle2);

        pedidoDetalle2.setId(pedidoDetalle1.getId());
        assertThat(pedidoDetalle1).isEqualTo(pedidoDetalle2);

        pedidoDetalle2 = getPedidoDetalleSample2();
        assertThat(pedidoDetalle1).isNotEqualTo(pedidoDetalle2);
    }

    @Test
    void carritoItemTest() {
        PedidoDetalle pedidoDetalle = getPedidoDetalleRandomSampleGenerator();
        CarritoItem carritoItemBack = getCarritoItemRandomSampleGenerator();

        pedidoDetalle.setCarritoItem(carritoItemBack);
        assertThat(pedidoDetalle.getCarritoItem()).isEqualTo(carritoItemBack);

        pedidoDetalle.carritoItem(null);
        assertThat(pedidoDetalle.getCarritoItem()).isNull();
    }

    @Test
    void productoTest() {
        PedidoDetalle pedidoDetalle = getPedidoDetalleRandomSampleGenerator();
        Producto productoBack = getProductoRandomSampleGenerator();

        pedidoDetalle.setProducto(productoBack);
        assertThat(pedidoDetalle.getProducto()).isEqualTo(productoBack);

        pedidoDetalle.producto(null);
        assertThat(pedidoDetalle.getProducto()).isNull();
    }

    @Test
    void pedidoTest() {
        PedidoDetalle pedidoDetalle = getPedidoDetalleRandomSampleGenerator();
        Pedido pedidoBack = getPedidoRandomSampleGenerator();

        pedidoDetalle.setPedido(pedidoBack);
        assertThat(pedidoDetalle.getPedido()).isEqualTo(pedidoBack);

        pedidoDetalle.pedido(null);
        assertThat(pedidoDetalle.getPedido()).isNull();
    }
}
