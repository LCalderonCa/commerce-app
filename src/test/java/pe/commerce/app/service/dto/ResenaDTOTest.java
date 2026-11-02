package pe.commerce.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import pe.commerce.app.web.rest.TestUtil;

class ResenaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ResenaDTO.class);
        ResenaDTO resenaDTO1 = new ResenaDTO();
        resenaDTO1.setId(1L);
        ResenaDTO resenaDTO2 = new ResenaDTO();
        assertThat(resenaDTO1).isNotEqualTo(resenaDTO2);
        resenaDTO2.setId(resenaDTO1.getId());
        assertThat(resenaDTO1).isEqualTo(resenaDTO2);
        resenaDTO2.setId(2L);
        assertThat(resenaDTO1).isNotEqualTo(resenaDTO2);
        resenaDTO1.setId(null);
        assertThat(resenaDTO1).isNotEqualTo(resenaDTO2);
    }
}
