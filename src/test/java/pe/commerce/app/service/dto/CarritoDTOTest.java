package pe.commerce.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import pe.commerce.app.web.rest.TestUtil;

class CarritoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CarritoDTO.class);
        CarritoDTO carritoDTO1 = new CarritoDTO();
        carritoDTO1.setId(1L);
        CarritoDTO carritoDTO2 = new CarritoDTO();
        assertThat(carritoDTO1).isNotEqualTo(carritoDTO2);
        carritoDTO2.setId(carritoDTO1.getId());
        assertThat(carritoDTO1).isEqualTo(carritoDTO2);
        carritoDTO2.setId(2L);
        assertThat(carritoDTO1).isNotEqualTo(carritoDTO2);
        carritoDTO1.setId(null);
        assertThat(carritoDTO1).isNotEqualTo(carritoDTO2);
    }
}
