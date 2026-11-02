package pe.commerce.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class InventarioTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + 2 * Short.MAX_VALUE);

    public static Inventario getInventarioSample1() {
        return new Inventario().id(1L).stock(1).stockReservado(1).stockMinimo(1).createdBy("createdBy1").lastModifiedBy("lastModifiedBy1");
    }

    public static Inventario getInventarioSample2() {
        return new Inventario().id(2L).stock(2).stockReservado(2).stockMinimo(2).createdBy("createdBy2").lastModifiedBy("lastModifiedBy2");
    }

    public static Inventario getInventarioRandomSampleGenerator() {
        return new Inventario()
            .id(longCount.incrementAndGet())
            .stock(intCount.incrementAndGet())
            .stockReservado(intCount.incrementAndGet())
            .stockMinimo(intCount.incrementAndGet())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
