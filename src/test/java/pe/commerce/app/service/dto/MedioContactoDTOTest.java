package pe.commerce.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import pe.commerce.app.web.rest.TestUtil;

class MedioContactoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MedioContactoDTO.class);
        MedioContactoDTO medioContactoDTO1 = new MedioContactoDTO();
        medioContactoDTO1.setId(1L);
        MedioContactoDTO medioContactoDTO2 = new MedioContactoDTO();
        assertThat(medioContactoDTO1).isNotEqualTo(medioContactoDTO2);
        medioContactoDTO2.setId(medioContactoDTO1.getId());
        assertThat(medioContactoDTO1).isEqualTo(medioContactoDTO2);
        medioContactoDTO2.setId(2L);
        assertThat(medioContactoDTO1).isNotEqualTo(medioContactoDTO2);
        medioContactoDTO1.setId(null);
        assertThat(medioContactoDTO1).isNotEqualTo(medioContactoDTO2);
    }
}
