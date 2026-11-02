package pe.commerce.app.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static pe.commerce.app.domain.MedioContactoTestSamples.*;
import static pe.commerce.app.domain.PersonaTestSamples.*;

import org.junit.jupiter.api.Test;
import pe.commerce.app.web.rest.TestUtil;

class MedioContactoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MedioContacto.class);
        MedioContacto medioContacto1 = getMedioContactoSample1();
        MedioContacto medioContacto2 = new MedioContacto();
        assertThat(medioContacto1).isNotEqualTo(medioContacto2);

        medioContacto2.setId(medioContacto1.getId());
        assertThat(medioContacto1).isEqualTo(medioContacto2);

        medioContacto2 = getMedioContactoSample2();
        assertThat(medioContacto1).isNotEqualTo(medioContacto2);
    }

    @Test
    void personaTest() {
        MedioContacto medioContacto = getMedioContactoRandomSampleGenerator();
        Persona personaBack = getPersonaRandomSampleGenerator();

        medioContacto.setPersona(personaBack);
        assertThat(medioContacto.getPersona()).isEqualTo(personaBack);

        medioContacto.persona(null);
        assertThat(medioContacto.getPersona()).isNull();
    }
}
