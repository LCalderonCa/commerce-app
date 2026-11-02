package pe.commerce.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import pe.commerce.app.web.rest.TestUtil;

class ProductoImagenDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductoImagenDTO.class);
        ProductoImagenDTO productoImagenDTO1 = new ProductoImagenDTO();
        productoImagenDTO1.setId(1L);
        ProductoImagenDTO productoImagenDTO2 = new ProductoImagenDTO();
        assertThat(productoImagenDTO1).isNotEqualTo(productoImagenDTO2);
        productoImagenDTO2.setId(productoImagenDTO1.getId());
        assertThat(productoImagenDTO1).isEqualTo(productoImagenDTO2);
        productoImagenDTO2.setId(2L);
        assertThat(productoImagenDTO1).isNotEqualTo(productoImagenDTO2);
        productoImagenDTO1.setId(null);
        assertThat(productoImagenDTO1).isNotEqualTo(productoImagenDTO2);
    }
}
