package pe.commerce.app.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static pe.commerce.app.domain.CuponTestSamples.*;

import org.junit.jupiter.api.Test;
import pe.commerce.app.web.rest.TestUtil;

class CuponTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cupon.class);
        Cupon cupon1 = getCuponSample1();
        Cupon cupon2 = new Cupon();
        assertThat(cupon1).isNotEqualTo(cupon2);

        cupon2.setId(cupon1.getId());
        assertThat(cupon1).isEqualTo(cupon2);

        cupon2 = getCuponSample2();
        assertThat(cupon1).isNotEqualTo(cupon2);
    }
}
