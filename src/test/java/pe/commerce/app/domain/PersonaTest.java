package pe.commerce.app.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static pe.commerce.app.domain.ClienteTestSamples.*;
import static pe.commerce.app.domain.DocumentoIdentidadTestSamples.*;
import static pe.commerce.app.domain.EmpleadoTestSamples.*;
import static pe.commerce.app.domain.MedioContactoTestSamples.*;
import static pe.commerce.app.domain.PersonaTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import pe.commerce.app.web.rest.TestUtil;

class PersonaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Persona.class);
        Persona persona1 = getPersonaSample1();
        Persona persona2 = new Persona();
        assertThat(persona1).isNotEqualTo(persona2);

        persona2.setId(persona1.getId());
        assertThat(persona1).isEqualTo(persona2);

        persona2 = getPersonaSample2();
        assertThat(persona1).isNotEqualTo(persona2);
    }

    @Test
    void documentosTest() {
        Persona persona = getPersonaRandomSampleGenerator();
        DocumentoIdentidad documentoIdentidadBack = getDocumentoIdentidadRandomSampleGenerator();

        persona.addDocumentos(documentoIdentidadBack);
        assertThat(persona.getDocumentoses()).containsOnly(documentoIdentidadBack);
        assertThat(documentoIdentidadBack.getPersona()).isEqualTo(persona);

        persona.removeDocumentos(documentoIdentidadBack);
        assertThat(persona.getDocumentoses()).doesNotContain(documentoIdentidadBack);
        assertThat(documentoIdentidadBack.getPersona()).isNull();

        persona.documentoses(new HashSet<>(Set.of(documentoIdentidadBack)));
        assertThat(persona.getDocumentoses()).containsOnly(documentoIdentidadBack);
        assertThat(documentoIdentidadBack.getPersona()).isEqualTo(persona);

        persona.setDocumentoses(new HashSet<>());
        assertThat(persona.getDocumentoses()).doesNotContain(documentoIdentidadBack);
        assertThat(documentoIdentidadBack.getPersona()).isNull();
    }

    @Test
    void mediosContactoTest() {
        Persona persona = getPersonaRandomSampleGenerator();
        MedioContacto medioContactoBack = getMedioContactoRandomSampleGenerator();

        persona.addMediosContacto(medioContactoBack);
        assertThat(persona.getMediosContactos()).containsOnly(medioContactoBack);
        assertThat(medioContactoBack.getPersona()).isEqualTo(persona);

        persona.removeMediosContacto(medioContactoBack);
        assertThat(persona.getMediosContactos()).doesNotContain(medioContactoBack);
        assertThat(medioContactoBack.getPersona()).isNull();

        persona.mediosContactos(new HashSet<>(Set.of(medioContactoBack)));
        assertThat(persona.getMediosContactos()).containsOnly(medioContactoBack);
        assertThat(medioContactoBack.getPersona()).isEqualTo(persona);

        persona.setMediosContactos(new HashSet<>());
        assertThat(persona.getMediosContactos()).doesNotContain(medioContactoBack);
        assertThat(medioContactoBack.getPersona()).isNull();
    }

    @Test
    void clienteTest() {
        Persona persona = getPersonaRandomSampleGenerator();
        Cliente clienteBack = getClienteRandomSampleGenerator();

        persona.setCliente(clienteBack);
        assertThat(persona.getCliente()).isEqualTo(clienteBack);
        assertThat(clienteBack.getPersona()).isEqualTo(persona);

        persona.cliente(null);
        assertThat(persona.getCliente()).isNull();
        assertThat(clienteBack.getPersona()).isNull();
    }

    @Test
    void empleadoTest() {
        Persona persona = getPersonaRandomSampleGenerator();
        Empleado empleadoBack = getEmpleadoRandomSampleGenerator();

        persona.setEmpleado(empleadoBack);
        assertThat(persona.getEmpleado()).isEqualTo(empleadoBack);
        assertThat(empleadoBack.getPersona()).isEqualTo(persona);

        persona.empleado(null);
        assertThat(persona.getEmpleado()).isNull();
        assertThat(empleadoBack.getPersona()).isNull();
    }
}
