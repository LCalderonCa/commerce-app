package pe.commerce.app.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static pe.commerce.app.domain.UbigeoTestSamples.*;

import org.junit.jupiter.api.Test;
import pe.commerce.app.web.rest.TestUtil;

class UbigeoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Ubigeo.class);
        Ubigeo ubigeo1 = getUbigeoSample1();
        Ubigeo ubigeo2 = new Ubigeo();
        assertThat(ubigeo1).isNotEqualTo(ubigeo2);

        ubigeo2.setId(ubigeo1.getId());
        assertThat(ubigeo1).isEqualTo(ubigeo2);

        ubigeo2 = getUbigeoSample2();
        assertThat(ubigeo1).isNotEqualTo(ubigeo2);
    }
}
