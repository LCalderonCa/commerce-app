package pe.commerce.app.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static pe.commerce.app.domain.ProductoImagenTestSamples.*;
import static pe.commerce.app.domain.ProductoTestSamples.*;

import org.junit.jupiter.api.Test;
import pe.commerce.app.web.rest.TestUtil;

class ProductoImagenTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductoImagen.class);
        ProductoImagen productoImagen1 = getProductoImagenSample1();
        ProductoImagen productoImagen2 = new ProductoImagen();
        assertThat(productoImagen1).isNotEqualTo(productoImagen2);

        productoImagen2.setId(productoImagen1.getId());
        assertThat(productoImagen1).isEqualTo(productoImagen2);

        productoImagen2 = getProductoImagenSample2();
        assertThat(productoImagen1).isNotEqualTo(productoImagen2);
    }

    @Test
    void productoTest() {
        ProductoImagen productoImagen = getProductoImagenRandomSampleGenerator();
        Producto productoBack = getProductoRandomSampleGenerator();

        productoImagen.setProducto(productoBack);
        assertThat(productoImagen.getProducto()).isEqualTo(productoBack);

        productoImagen.producto(null);
        assertThat(productoImagen.getProducto()).isNull();
    }
}
