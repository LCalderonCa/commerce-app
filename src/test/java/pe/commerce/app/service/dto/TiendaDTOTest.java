package pe.commerce.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import pe.commerce.app.web.rest.TestUtil;

class TiendaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TiendaDTO.class);
        TiendaDTO tiendaDTO1 = new TiendaDTO();
        tiendaDTO1.setId(1L);
        TiendaDTO tiendaDTO2 = new TiendaDTO();
        assertThat(tiendaDTO1).isNotEqualTo(tiendaDTO2);
        tiendaDTO2.setId(tiendaDTO1.getId());
        assertThat(tiendaDTO1).isEqualTo(tiendaDTO2);
        tiendaDTO2.setId(2L);
        assertThat(tiendaDTO1).isNotEqualTo(tiendaDTO2);
        tiendaDTO1.setId(null);
        assertThat(tiendaDTO1).isNotEqualTo(tiendaDTO2);
    }
}
