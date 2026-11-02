package pe.commerce.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MedioContactoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static MedioContacto getMedioContactoSample1() {
        return new MedioContacto()
            .id(1L)
            .valor("valor1")
            .codigoPais("codigoPais1")
            .anexo("anexo1")
            .createdBy("createdBy1")
            .lastModifiedBy("lastModifiedBy1");
    }

    public static MedioContacto getMedioContactoSample2() {
        return new MedioContacto()
            .id(2L)
            .valor("valor2")
            .codigoPais("codigoPais2")
            .anexo("anexo2")
            .createdBy("createdBy2")
            .lastModifiedBy("lastModifiedBy2");
    }

    public static MedioContacto getMedioContactoRandomSampleGenerator() {
        return new MedioContacto()
            .id(longCount.incrementAndGet())
            .valor(UUID.randomUUID().toString())
            .codigoPais(UUID.randomUUID().toString())
            .anexo(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
