package pe.commerce.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TiendaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static Tienda getTiendaSample1() {
        return new Tienda()
            .id(1L)
            .nombre("nombre1")
            .direccion("direccion1")
            .telefono("telefono1")
            .createdBy("createdBy1")
            .lastModifiedBy("lastModifiedBy1");
    }

    public static Tienda getTiendaSample2() {
        return new Tienda()
            .id(2L)
            .nombre("nombre2")
            .direccion("direccion2")
            .telefono("telefono2")
            .createdBy("createdBy2")
            .lastModifiedBy("lastModifiedBy2");
    }

    public static Tienda getTiendaRandomSampleGenerator() {
        return new Tienda()
            .id(longCount.incrementAndGet())
            .nombre(UUID.randomUUID().toString())
            .direccion(UUID.randomUUID().toString())
            .telefono(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
