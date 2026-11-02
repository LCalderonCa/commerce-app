package pe.commerce.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import pe.commerce.app.web.rest.TestUtil;

class PedidoDetalleDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PedidoDetalleDTO.class);
        PedidoDetalleDTO pedidoDetalleDTO1 = new PedidoDetalleDTO();
        pedidoDetalleDTO1.setId(1L);
        PedidoDetalleDTO pedidoDetalleDTO2 = new PedidoDetalleDTO();
        assertThat(pedidoDetalleDTO1).isNotEqualTo(pedidoDetalleDTO2);
        pedidoDetalleDTO2.setId(pedidoDetalleDTO1.getId());
        assertThat(pedidoDetalleDTO1).isEqualTo(pedidoDetalleDTO2);
        pedidoDetalleDTO2.setId(2L);
        assertThat(pedidoDetalleDTO1).isNotEqualTo(pedidoDetalleDTO2);
        pedidoDetalleDTO1.setId(null);
        assertThat(pedidoDetalleDTO1).isNotEqualTo(pedidoDetalleDTO2);
    }
}
