package pe.commerce.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class CuponTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + 2 * Short.MAX_VALUE);

    public static Cupon getCuponSample1() {
        return new Cupon()
            .id(1L)
            .codigo("codigo1")
            .descripcion("descripcion1")
            .usosMaximos(1)
            .usosPorCliente(1)
            .createdBy("createdBy1")
            .lastModifiedBy("lastModifiedBy1");
    }

    public static Cupon getCuponSample2() {
        return new Cupon()
            .id(2L)
            .codigo("codigo2")
            .descripcion("descripcion2")
            .usosMaximos(2)
            .usosPorCliente(2)
            .createdBy("createdBy2")
            .lastModifiedBy("lastModifiedBy2");
    }

    public static Cupon getCuponRandomSampleGenerator() {
        return new Cupon()
            .id(longCount.incrementAndGet())
            .codigo(UUID.randomUUID().toString())
            .descripcion(UUID.randomUUID().toString())
            .usosMaximos(intCount.incrementAndGet())
            .usosPorCliente(intCount.incrementAndGet())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
