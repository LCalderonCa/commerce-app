package pe.commerce.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import pe.commerce.app.web.rest.TestUtil;

class CarritoItemDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CarritoItemDTO.class);
        CarritoItemDTO carritoItemDTO1 = new CarritoItemDTO();
        carritoItemDTO1.setId(1L);
        CarritoItemDTO carritoItemDTO2 = new CarritoItemDTO();
        assertThat(carritoItemDTO1).isNotEqualTo(carritoItemDTO2);
        carritoItemDTO2.setId(carritoItemDTO1.getId());
        assertThat(carritoItemDTO1).isEqualTo(carritoItemDTO2);
        carritoItemDTO2.setId(2L);
        assertThat(carritoItemDTO1).isNotEqualTo(carritoItemDTO2);
        carritoItemDTO1.setId(null);
        assertThat(carritoItemDTO1).isNotEqualTo(carritoItemDTO2);
    }
}
