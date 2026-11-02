package pe.commerce.app.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static pe.commerce.app.domain.InventarioTestSamples.*;
import static pe.commerce.app.domain.ProductoTestSamples.*;
import static pe.commerce.app.domain.TiendaTestSamples.*;

import org.junit.jupiter.api.Test;
import pe.commerce.app.web.rest.TestUtil;

class InventarioTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Inventario.class);
        Inventario inventario1 = getInventarioSample1();
        Inventario inventario2 = new Inventario();
        assertThat(inventario1).isNotEqualTo(inventario2);

        inventario2.setId(inventario1.getId());
        assertThat(inventario1).isEqualTo(inventario2);

        inventario2 = getInventarioSample2();
        assertThat(inventario1).isNotEqualTo(inventario2);
    }

    @Test
    void productoTest() {
        Inventario inventario = getInventarioRandomSampleGenerator();
        Producto productoBack = getProductoRandomSampleGenerator();

        inventario.setProducto(productoBack);
        assertThat(inventario.getProducto()).isEqualTo(productoBack);

        inventario.producto(null);
        assertThat(inventario.getProducto()).isNull();
    }

    @Test
    void tiendaTest() {
        Inventario inventario = getInventarioRandomSampleGenerator();
        Tienda tiendaBack = getTiendaRandomSampleGenerator();

        inventario.setTienda(tiendaBack);
        assertThat(inventario.getTienda()).isEqualTo(tiendaBack);

        inventario.tienda(null);
        assertThat(inventario.getTienda()).isNull();
    }
}
