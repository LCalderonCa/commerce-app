package pe.commerce.app.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static pe.commerce.app.domain.DocumentoIdentidadTestSamples.*;
import static pe.commerce.app.domain.PersonaTestSamples.*;

import org.junit.jupiter.api.Test;
import pe.commerce.app.web.rest.TestUtil;

class DocumentoIdentidadTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DocumentoIdentidad.class);
        DocumentoIdentidad documentoIdentidad1 = getDocumentoIdentidadSample1();
        DocumentoIdentidad documentoIdentidad2 = new DocumentoIdentidad();
        assertThat(documentoIdentidad1).isNotEqualTo(documentoIdentidad2);

        documentoIdentidad2.setId(documentoIdentidad1.getId());
        assertThat(documentoIdentidad1).isEqualTo(documentoIdentidad2);

        documentoIdentidad2 = getDocumentoIdentidadSample2();
        assertThat(documentoIdentidad1).isNotEqualTo(documentoIdentidad2);
    }

    @Test
    void personaTest() {
        DocumentoIdentidad documentoIdentidad = getDocumentoIdentidadRandomSampleGenerator();
        Persona personaBack = getPersonaRandomSampleGenerator();

        documentoIdentidad.setPersona(personaBack);
        assertThat(documentoIdentidad.getPersona()).isEqualTo(personaBack);

        documentoIdentidad.persona(null);
        assertThat(documentoIdentidad.getPersona()).isNull();
    }
}
