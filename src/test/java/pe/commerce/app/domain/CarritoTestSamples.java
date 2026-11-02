package pe.commerce.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CarritoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static Carrito getCarritoSample1() {
        return new Carrito().id(1L).createdBy("createdBy1").lastModifiedBy("lastModifiedBy1");
    }

    public static Carrito getCarritoSample2() {
        return new Carrito().id(2L).createdBy("createdBy2").lastModifiedBy("lastModifiedBy2");
    }

    public static Carrito getCarritoRandomSampleGenerator() {
        return new Carrito()
            .id(longCount.incrementAndGet())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
