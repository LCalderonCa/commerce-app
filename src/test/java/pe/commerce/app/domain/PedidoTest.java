package pe.commerce.app.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static pe.commerce.app.domain.ClienteTestSamples.*;
import static pe.commerce.app.domain.ComprobanteTestSamples.*;
import static pe.commerce.app.domain.CuponTestSamples.*;
import static pe.commerce.app.domain.EmpleadoTestSamples.*;
import static pe.commerce.app.domain.EnvioTestSamples.*;
import static pe.commerce.app.domain.PagoTestSamples.*;
import static pe.commerce.app.domain.PedidoDetalleTestSamples.*;
import static pe.commerce.app.domain.PedidoEstadoHistorialTestSamples.*;
import static pe.commerce.app.domain.PedidoTestSamples.*;
import static pe.commerce.app.domain.TiendaTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import pe.commerce.app.web.rest.TestUtil;

class PedidoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Pedido.class);
        Pedido pedido1 = getPedidoSample1();
        Pedido pedido2 = new Pedido();
        assertThat(pedido1).isNotEqualTo(pedido2);

        pedido2.setId(pedido1.getId());
        assertThat(pedido1).isEqualTo(pedido2);

        pedido2 = getPedidoSample2();
        assertThat(pedido1).isNotEqualTo(pedido2);
    }

    @Test
    void detallesTest() {
        Pedido pedido = getPedidoRandomSampleGenerator();
        PedidoDetalle pedidoDetalleBack = getPedidoDetalleRandomSampleGenerator();

        pedido.addDetalles(pedidoDetalleBack);
        assertThat(pedido.getDetalleses()).containsOnly(pedidoDetalleBack);
        assertThat(pedidoDetalleBack.getPedido()).isEqualTo(pedido);

        pedido.removeDetalles(pedidoDetalleBack);
        assertThat(pedido.getDetalleses()).doesNotContain(pedidoDetalleBack);
        assertThat(pedidoDetalleBack.getPedido()).isNull();

        pedido.detalleses(new HashSet<>(Set.of(pedidoDetalleBack)));
        assertThat(pedido.getDetalleses()).containsOnly(pedidoDetalleBack);
        assertThat(pedidoDetalleBack.getPedido()).isEqualTo(pedido);

        pedido.setDetalleses(new HashSet<>());
        assertThat(pedido.getDetalleses()).doesNotContain(pedidoDetalleBack);
        assertThat(pedidoDetalleBack.getPedido()).isNull();
    }

    @Test
    void historialEstadosTest() {
        Pedido pedido = getPedidoRandomSampleGenerator();
        PedidoEstadoHistorial pedidoEstadoHistorialBack = getPedidoEstadoHistorialRandomSampleGenerator();

        pedido.addHistorialEstados(pedidoEstadoHistorialBack);
        assertThat(pedido.getHistorialEstadoses()).containsOnly(pedidoEstadoHistorialBack);
        assertThat(pedidoEstadoHistorialBack.getPedido()).isEqualTo(pedido);

        pedido.removeHistorialEstados(pedidoEstadoHistorialBack);
        assertThat(pedido.getHistorialEstadoses()).doesNotContain(pedidoEstadoHistorialBack);
        assertThat(pedidoEstadoHistorialBack.getPedido()).isNull();

        pedido.historialEstadoses(new HashSet<>(Set.of(pedidoEstadoHistorialBack)));
        assertThat(pedido.getHistorialEstadoses()).containsOnly(pedidoEstadoHistorialBack);
        assertThat(pedidoEstadoHistorialBack.getPedido()).isEqualTo(pedido);

        pedido.setHistorialEstadoses(new HashSet<>());
        assertThat(pedido.getHistorialEstadoses()).doesNotContain(pedidoEstadoHistorialBack);
        assertThat(pedidoEstadoHistorialBack.getPedido()).isNull();
    }

    @Test
    void pagosTest() {
        Pedido pedido = getPedidoRandomSampleGenerator();
        Pago pagoBack = getPagoRandomSampleGenerator();

        pedido.addPagos(pagoBack);
        assertThat(pedido.getPagoses()).containsOnly(pagoBack);
        assertThat(pagoBack.getPedido()).isEqualTo(pedido);

        pedido.removePagos(pagoBack);
        assertThat(pedido.getPagoses()).doesNotContain(pagoBack);
        assertThat(pagoBack.getPedido()).isNull();

        pedido.pagoses(new HashSet<>(Set.of(pagoBack)));
        assertThat(pedido.getPagoses()).containsOnly(pagoBack);
        assertThat(pagoBack.getPedido()).isEqualTo(pedido);

        pedido.setPagoses(new HashSet<>());
        assertThat(pedido.getPagoses()).doesNotContain(pagoBack);
        assertThat(pagoBack.getPedido()).isNull();
    }

    @Test
    void comprobantesTest() {
        Pedido pedido = getPedidoRandomSampleGenerator();
        Comprobante comprobanteBack = getComprobanteRandomSampleGenerator();

        pedido.addComprobantes(comprobanteBack);
        assertThat(pedido.getComprobanteses()).containsOnly(comprobanteBack);
        assertThat(comprobanteBack.getPedido()).isEqualTo(pedido);

        pedido.removeComprobantes(comprobanteBack);
        assertThat(pedido.getComprobanteses()).doesNotContain(comprobanteBack);
        assertThat(comprobanteBack.getPedido()).isNull();

        pedido.comprobanteses(new HashSet<>(Set.of(comprobanteBack)));
        assertThat(pedido.getComprobanteses()).containsOnly(comprobanteBack);
        assertThat(comprobanteBack.getPedido()).isEqualTo(pedido);

        pedido.setComprobanteses(new HashSet<>());
        assertThat(pedido.getComprobanteses()).doesNotContain(comprobanteBack);
        assertThat(comprobanteBack.getPedido()).isNull();
    }

    @Test
    void cuponTest() {
        Pedido pedido = getPedidoRandomSampleGenerator();
        Cupon cuponBack = getCuponRandomSampleGenerator();

        pedido.setCupon(cuponBack);
        assertThat(pedido.getCupon()).isEqualTo(cuponBack);

        pedido.cupon(null);
        assertThat(pedido.getCupon()).isNull();
    }

    @Test
    void tiendaRecojoTest() {
        Pedido pedido = getPedidoRandomSampleGenerator();
        Tienda tiendaBack = getTiendaRandomSampleGenerator();

        pedido.setTiendaRecojo(tiendaBack);
        assertThat(pedido.getTiendaRecojo()).isEqualTo(tiendaBack);

        pedido.tiendaRecojo(null);
        assertThat(pedido.getTiendaRecojo()).isNull();
    }

    @Test
    void atendidoPorTest() {
        Pedido pedido = getPedidoRandomSampleGenerator();
        Empleado empleadoBack = getEmpleadoRandomSampleGenerator();

        pedido.setAtendidoPor(empleadoBack);
        assertThat(pedido.getAtendidoPor()).isEqualTo(empleadoBack);

        pedido.atendidoPor(null);
        assertThat(pedido.getAtendidoPor()).isNull();
    }

    @Test
    void envioTest() {
        Pedido pedido = getPedidoRandomSampleGenerator();
        Envio envioBack = getEnvioRandomSampleGenerator();

        pedido.setEnvio(envioBack);
        assertThat(pedido.getEnvio()).isEqualTo(envioBack);
        assertThat(envioBack.getPedido()).isEqualTo(pedido);

        pedido.envio(null);
        assertThat(pedido.getEnvio()).isNull();
        assertThat(envioBack.getPedido()).isNull();
    }

    @Test
    void clienteTest() {
        Pedido pedido = getPedidoRandomSampleGenerator();
        Cliente clienteBack = getClienteRandomSampleGenerator();

        pedido.setCliente(clienteBack);
        assertThat(pedido.getCliente()).isEqualTo(clienteBack);

        pedido.cliente(null);
        assertThat(pedido.getCliente()).isNull();
    }
}
