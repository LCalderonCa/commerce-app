package pe.commerce.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class DocumentoIdentidadTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static DocumentoIdentidad getDocumentoIdentidadSample1() {
        return new DocumentoIdentidad().id(1L).numero("numero1").createdBy("createdBy1").lastModifiedBy("lastModifiedBy1");
    }

    public static DocumentoIdentidad getDocumentoIdentidadSample2() {
        return new DocumentoIdentidad().id(2L).numero("numero2").createdBy("createdBy2").lastModifiedBy("lastModifiedBy2");
    }

    public static DocumentoIdentidad getDocumentoIdentidadRandomSampleGenerator() {
        return new DocumentoIdentidad()
            .id(longCount.incrementAndGet())
            .numero(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
