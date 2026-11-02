package pe.commerce.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import pe.commerce.app.web.rest.TestUtil;

class PedidoEstadoHistorialDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PedidoEstadoHistorialDTO.class);
        PedidoEstadoHistorialDTO pedidoEstadoHistorialDTO1 = new PedidoEstadoHistorialDTO();
        pedidoEstadoHistorialDTO1.setId(1L);
        PedidoEstadoHistorialDTO pedidoEstadoHistorialDTO2 = new PedidoEstadoHistorialDTO();
        assertThat(pedidoEstadoHistorialDTO1).isNotEqualTo(pedidoEstadoHistorialDTO2);
        pedidoEstadoHistorialDTO2.setId(pedidoEstadoHistorialDTO1.getId());
        assertThat(pedidoEstadoHistorialDTO1).isEqualTo(pedidoEstadoHistorialDTO2);
        pedidoEstadoHistorialDTO2.setId(2L);
        assertThat(pedidoEstadoHistorialDTO1).isNotEqualTo(pedidoEstadoHistorialDTO2);
        pedidoEstadoHistorialDTO1.setId(null);
        assertThat(pedidoEstadoHistorialDTO1).isNotEqualTo(pedidoEstadoHistorialDTO2);
    }
}
