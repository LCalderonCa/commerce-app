package pe.commerce.app.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static pe.commerce.app.domain.CategoriaTestSamples.*;
import static pe.commerce.app.domain.CategoriaTestSamples.*;
import static pe.commerce.app.domain.ProductoTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import pe.commerce.app.web.rest.TestUtil;

class CategoriaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Categoria.class);
        Categoria categoria1 = getCategoriaSample1();
        Categoria categoria2 = new Categoria();
        assertThat(categoria1).isNotEqualTo(categoria2);

        categoria2.setId(categoria1.getId());
        assertThat(categoria1).isEqualTo(categoria2);

        categoria2 = getCategoriaSample2();
        assertThat(categoria1).isNotEqualTo(categoria2);
    }

    @Test
    void subcategoriasTest() {
        Categoria categoria = getCategoriaRandomSampleGenerator();
        Categoria categoriaBack = getCategoriaRandomSampleGenerator();

        categoria.addSubcategorias(categoriaBack);
        assertThat(categoria.getSubcategoriases()).containsOnly(categoriaBack);
        assertThat(categoriaBack.getPadre()).isEqualTo(categoria);

        categoria.removeSubcategorias(categoriaBack);
        assertThat(categoria.getSubcategoriases()).doesNotContain(categoriaBack);
        assertThat(categoriaBack.getPadre()).isNull();

        categoria.subcategoriases(new HashSet<>(Set.of(categoriaBack)));
        assertThat(categoria.getSubcategoriases()).containsOnly(categoriaBack);
        assertThat(categoriaBack.getPadre()).isEqualTo(categoria);

        categoria.setSubcategoriases(new HashSet<>());
        assertThat(categoria.getSubcategoriases()).doesNotContain(categoriaBack);
        assertThat(categoriaBack.getPadre()).isNull();
    }

    @Test
    void productosTest() {
        Categoria categoria = getCategoriaRandomSampleGenerator();
        Producto productoBack = getProductoRandomSampleGenerator();

        categoria.addProductos(productoBack);
        assertThat(categoria.getProductoses()).containsOnly(productoBack);
        assertThat(productoBack.getCategoria()).isEqualTo(categoria);

        categoria.removeProductos(productoBack);
        assertThat(categoria.getProductoses()).doesNotContain(productoBack);
        assertThat(productoBack.getCategoria()).isNull();

        categoria.productoses(new HashSet<>(Set.of(productoBack)));
        assertThat(categoria.getProductoses()).containsOnly(productoBack);
        assertThat(productoBack.getCategoria()).isEqualTo(categoria);

        categoria.setProductoses(new HashSet<>());
        assertThat(categoria.getProductoses()).doesNotContain(productoBack);
        assertThat(productoBack.getCategoria()).isNull();
    }

    @Test
    void padreTest() {
        Categoria categoria = getCategoriaRandomSampleGenerator();
        Categoria categoriaBack = getCategoriaRandomSampleGenerator();

        categoria.setPadre(categoriaBack);
        assertThat(categoria.getPadre()).isEqualTo(categoriaBack);

        categoria.padre(null);
        assertThat(categoria.getPadre()).isNull();
    }
}
