package pe.commerce.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import pe.commerce.app.web.rest.TestUtil;

class UbigeoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UbigeoDTO.class);
        UbigeoDTO ubigeoDTO1 = new UbigeoDTO();
        ubigeoDTO1.setId(1L);
        UbigeoDTO ubigeoDTO2 = new UbigeoDTO();
        assertThat(ubigeoDTO1).isNotEqualTo(ubigeoDTO2);
        ubigeoDTO2.setId(ubigeoDTO1.getId());
        assertThat(ubigeoDTO1).isEqualTo(ubigeoDTO2);
        ubigeoDTO2.setId(2L);
        assertThat(ubigeoDTO1).isNotEqualTo(ubigeoDTO2);
        ubigeoDTO1.setId(null);
        assertThat(ubigeoDTO1).isNotEqualTo(ubigeoDTO2);
    }
}
