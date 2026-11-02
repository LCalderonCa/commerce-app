package pe.commerce.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class DireccionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static Direccion getDireccionSample1() {
        return new Direccion()
            .id(1L)
            .alias("alias1")
            .direccion("direccion1")
            .referencia("referencia1")
            .nombreContacto("nombreContacto1")
            .telefonoContacto("telefonoContacto1")
            .createdBy("createdBy1")
            .lastModifiedBy("lastModifiedBy1");
    }

    public static Direccion getDireccionSample2() {
        return new Direccion()
            .id(2L)
            .alias("alias2")
            .direccion("direccion2")
            .referencia("referencia2")
            .nombreContacto("nombreContacto2")
            .telefonoContacto("telefonoContacto2")
            .createdBy("createdBy2")
            .lastModifiedBy("lastModifiedBy2");
    }

    public static Direccion getDireccionRandomSampleGenerator() {
        return new Direccion()
            .id(longCount.incrementAndGet())
            .alias(UUID.randomUUID().toString())
            .direccion(UUID.randomUUID().toString())
            .referencia(UUID.randomUUID().toString())
            .nombreContacto(UUID.randomUUID().toString())
            .telefonoContacto(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
