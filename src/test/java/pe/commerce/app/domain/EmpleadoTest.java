package pe.commerce.app.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static pe.commerce.app.domain.EmpleadoTestSamples.*;
import static pe.commerce.app.domain.PersonaTestSamples.*;
import static pe.commerce.app.domain.TiendaTestSamples.*;

import org.junit.jupiter.api.Test;
import pe.commerce.app.web.rest.TestUtil;

class EmpleadoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Empleado.class);
        Empleado empleado1 = getEmpleadoSample1();
        Empleado empleado2 = new Empleado();
        assertThat(empleado1).isNotEqualTo(empleado2);

        empleado2.setId(empleado1.getId());
        assertThat(empleado1).isEqualTo(empleado2);

        empleado2 = getEmpleadoSample2();
        assertThat(empleado1).isNotEqualTo(empleado2);
    }

    @Test
    void personaTest() {
        Empleado empleado = getEmpleadoRandomSampleGenerator();
        Persona personaBack = getPersonaRandomSampleGenerator();

        empleado.setPersona(personaBack);
        assertThat(empleado.getPersona()).isEqualTo(personaBack);

        empleado.persona(null);
        assertThat(empleado.getPersona()).isNull();
    }

    @Test
    void tiendaTest() {
        Empleado empleado = getEmpleadoRandomSampleGenerator();
        Tienda tiendaBack = getTiendaRandomSampleGenerator();

        empleado.setTienda(tiendaBack);
        assertThat(empleado.getTienda()).isEqualTo(tiendaBack);

        empleado.tienda(null);
        assertThat(empleado.getTienda()).isNull();
    }
}
