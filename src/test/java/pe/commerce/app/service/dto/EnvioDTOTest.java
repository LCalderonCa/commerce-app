package pe.commerce.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import pe.commerce.app.web.rest.TestUtil;

class EnvioDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EnvioDTO.class);
        EnvioDTO envioDTO1 = new EnvioDTO();
        envioDTO1.setId(1L);
        EnvioDTO envioDTO2 = new EnvioDTO();
        assertThat(envioDTO1).isNotEqualTo(envioDTO2);
        envioDTO2.setId(envioDTO1.getId());
        assertThat(envioDTO1).isEqualTo(envioDTO2);
        envioDTO2.setId(2L);
        assertThat(envioDTO1).isNotEqualTo(envioDTO2);
        envioDTO1.setId(null);
        assertThat(envioDTO1).isNotEqualTo(envioDTO2);
    }
}
