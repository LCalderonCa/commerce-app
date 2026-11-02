package pe.commerce.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import pe.commerce.app.web.rest.TestUtil;

class DocumentoIdentidadDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DocumentoIdentidadDTO.class);
        DocumentoIdentidadDTO documentoIdentidadDTO1 = new DocumentoIdentidadDTO();
        documentoIdentidadDTO1.setId(1L);
        DocumentoIdentidadDTO documentoIdentidadDTO2 = new DocumentoIdentidadDTO();
        assertThat(documentoIdentidadDTO1).isNotEqualTo(documentoIdentidadDTO2);
        documentoIdentidadDTO2.setId(documentoIdentidadDTO1.getId());
        assertThat(documentoIdentidadDTO1).isEqualTo(documentoIdentidadDTO2);
        documentoIdentidadDTO2.setId(2L);
        assertThat(documentoIdentidadDTO1).isNotEqualTo(documentoIdentidadDTO2);
        documentoIdentidadDTO1.setId(null);
        assertThat(documentoIdentidadDTO1).isNotEqualTo(documentoIdentidadDTO2);
    }
}
