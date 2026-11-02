package pe.commerce.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MarcaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static Marca getMarcaSample1() {
        return new Marca().id(1L).nombre("nombre1").logoUrl("logoUrl1").createdBy("createdBy1").lastModifiedBy("lastModifiedBy1");
    }

    public static Marca getMarcaSample2() {
        return new Marca().id(2L).nombre("nombre2").logoUrl("logoUrl2").createdBy("createdBy2").lastModifiedBy("lastModifiedBy2");
    }

    public static Marca getMarcaRandomSampleGenerator() {
        return new Marca()
            .id(longCount.incrementAndGet())
            .nombre(UUID.randomUUID().toString())
            .logoUrl(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
