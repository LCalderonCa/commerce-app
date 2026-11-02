package pe.commerce.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import pe.commerce.app.web.rest.TestUtil;

class DatoFacturacionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DatoFacturacionDTO.class);
        DatoFacturacionDTO datoFacturacionDTO1 = new DatoFacturacionDTO();
        datoFacturacionDTO1.setId(1L);
        DatoFacturacionDTO datoFacturacionDTO2 = new DatoFacturacionDTO();
        assertThat(datoFacturacionDTO1).isNotEqualTo(datoFacturacionDTO2);
        datoFacturacionDTO2.setId(datoFacturacionDTO1.getId());
        assertThat(datoFacturacionDTO1).isEqualTo(datoFacturacionDTO2);
        datoFacturacionDTO2.setId(2L);
        assertThat(datoFacturacionDTO1).isNotEqualTo(datoFacturacionDTO2);
        datoFacturacionDTO1.setId(null);
        assertThat(datoFacturacionDTO1).isNotEqualTo(datoFacturacionDTO2);
    }
}
