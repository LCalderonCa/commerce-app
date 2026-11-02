package pe.commerce.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import pe.commerce.app.web.rest.TestUtil;

class CuponDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CuponDTO.class);
        CuponDTO cuponDTO1 = new CuponDTO();
        cuponDTO1.setId(1L);
        CuponDTO cuponDTO2 = new CuponDTO();
        assertThat(cuponDTO1).isNotEqualTo(cuponDTO2);
        cuponDTO2.setId(cuponDTO1.getId());
        assertThat(cuponDTO1).isEqualTo(cuponDTO2);
        cuponDTO2.setId(2L);
        assertThat(cuponDTO1).isNotEqualTo(cuponDTO2);
        cuponDTO1.setId(null);
        assertThat(cuponDTO1).isNotEqualTo(cuponDTO2);
    }
}
