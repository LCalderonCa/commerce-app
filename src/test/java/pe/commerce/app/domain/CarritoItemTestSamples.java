package pe.commerce.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class CarritoItemTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + 2 * Short.MAX_VALUE);

    public static CarritoItem getCarritoItemSample1() {
        return new CarritoItem().id(1L).cantidad(1).createdBy("createdBy1").lastModifiedBy("lastModifiedBy1");
    }

    public static CarritoItem getCarritoItemSample2() {
        return new CarritoItem().id(2L).cantidad(2).createdBy("createdBy2").lastModifiedBy("lastModifiedBy2");
    }

    public static CarritoItem getCarritoItemRandomSampleGenerator() {
        return new CarritoItem()
            .id(longCount.incrementAndGet())
            .cantidad(intCount.incrementAndGet())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
