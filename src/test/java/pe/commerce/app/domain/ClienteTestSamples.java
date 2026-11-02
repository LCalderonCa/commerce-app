package pe.commerce.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ClienteTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static Cliente getClienteSample1() {
        return new Cliente().id(1L).codigo("codigo1").createdBy("createdBy1").lastModifiedBy("lastModifiedBy1");
    }

    public static Cliente getClienteSample2() {
        return new Cliente().id(2L).codigo("codigo2").createdBy("createdBy2").lastModifiedBy("lastModifiedBy2");
    }

    public static Cliente getClienteRandomSampleGenerator() {
        return new Cliente()
            .id(longCount.incrementAndGet())
            .codigo(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
