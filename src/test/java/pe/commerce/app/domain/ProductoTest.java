package pe.commerce.app.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static pe.commerce.app.domain.CategoriaTestSamples.*;
import static pe.commerce.app.domain.InventarioTestSamples.*;
import static pe.commerce.app.domain.MarcaTestSamples.*;
import static pe.commerce.app.domain.ProductoImagenTestSamples.*;
import static pe.commerce.app.domain.ProductoTestSamples.*;
import static pe.commerce.app.domain.ResenaTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import pe.commerce.app.web.rest.TestUtil;

class ProductoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Producto.class);
        Producto producto1 = getProductoSample1();
        Producto producto2 = new Producto();
        assertThat(producto1).isNotEqualTo(producto2);

        producto2.setId(producto1.getId());
        assertThat(producto1).isEqualTo(producto2);

        producto2 = getProductoSample2();
        assertThat(producto1).isNotEqualTo(producto2);
    }

    @Test
    void imagenesTest() {
        Producto producto = getProductoRandomSampleGenerator();
        ProductoImagen productoImagenBack = getProductoImagenRandomSampleGenerator();

        producto.addImagenes(productoImagenBack);
        assertThat(producto.getImageneses()).containsOnly(productoImagenBack);
        assertThat(productoImagenBack.getProducto()).isEqualTo(producto);

        producto.removeImagenes(productoImagenBack);
        assertThat(producto.getImageneses()).doesNotContain(productoImagenBack);
        assertThat(productoImagenBack.getProducto()).isNull();

        producto.imageneses(new HashSet<>(Set.of(productoImagenBack)));
        assertThat(producto.getImageneses()).containsOnly(productoImagenBack);
        assertThat(productoImagenBack.getProducto()).isEqualTo(producto);

        producto.setImageneses(new HashSet<>());
        assertThat(producto.getImageneses()).doesNotContain(productoImagenBack);
        assertThat(productoImagenBack.getProducto()).isNull();
    }

    @Test
    void inventariosTest() {
        Producto producto = getProductoRandomSampleGenerator();
        Inventario inventarioBack = getInventarioRandomSampleGenerator();

        producto.addInventarios(inventarioBack);
        assertThat(producto.getInventarioses()).containsOnly(inventarioBack);
        assertThat(inventarioBack.getProducto()).isEqualTo(producto);

        producto.removeInventarios(inventarioBack);
        assertThat(producto.getInventarioses()).doesNotContain(inventarioBack);
        assertThat(inventarioBack.getProducto()).isNull();

        producto.inventarioses(new HashSet<>(Set.of(inventarioBack)));
        assertThat(producto.getInventarioses()).containsOnly(inventarioBack);
        assertThat(inventarioBack.getProducto()).isEqualTo(producto);

        producto.setInventarioses(new HashSet<>());
        assertThat(producto.getInventarioses()).doesNotContain(inventarioBack);
        assertThat(inventarioBack.getProducto()).isNull();
    }

    @Test
    void resenasTest() {
        Producto producto = getProductoRandomSampleGenerator();
        Resena resenaBack = getResenaRandomSampleGenerator();

        producto.addResenas(resenaBack);
        assertThat(producto.getResenases()).containsOnly(resenaBack);
        assertThat(resenaBack.getProducto()).isEqualTo(producto);

        producto.removeResenas(resenaBack);
        assertThat(producto.getResenases()).doesNotContain(resenaBack);
        assertThat(resenaBack.getProducto()).isNull();

        producto.resenases(new HashSet<>(Set.of(resenaBack)));
        assertThat(producto.getResenases()).containsOnly(resenaBack);
        assertThat(resenaBack.getProducto()).isEqualTo(producto);

        producto.setResenases(new HashSet<>());
        assertThat(producto.getResenases()).doesNotContain(resenaBack);
        assertThat(resenaBack.getProducto()).isNull();
    }

    @Test
    void categoriaTest() {
        Producto producto = getProductoRandomSampleGenerator();
        Categoria categoriaBack = getCategoriaRandomSampleGenerator();

        producto.setCategoria(categoriaBack);
        assertThat(producto.getCategoria()).isEqualTo(categoriaBack);

        producto.categoria(null);
        assertThat(producto.getCategoria()).isNull();
    }

    @Test
    void marcaTest() {
        Producto producto = getProductoRandomSampleGenerator();
        Marca marcaBack = getMarcaRandomSampleGenerator();

        producto.setMarca(marcaBack);
        assertThat(producto.getMarca()).isEqualTo(marcaBack);

        producto.marca(null);
        assertThat(producto.getMarca()).isNull();
    }
}
