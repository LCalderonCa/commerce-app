package pe.commerce.app.service.mapper;

import static pe.commerce.app.domain.DocumentoIdentidadAsserts.*;
import static pe.commerce.app.domain.DocumentoIdentidadTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DocumentoIdentidadMapperTest {

    private DocumentoIdentidadMapper documentoIdentidadMapper;

    @BeforeEach
    void setUp() {
        documentoIdentidadMapper = new DocumentoIdentidadMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getDocumentoIdentidadSample1();
        var actual = documentoIdentidadMapper.toEntity(documentoIdentidadMapper.toDto(expected));
        assertDocumentoIdentidadAllPropertiesEquals(expected, actual);
    }
}
