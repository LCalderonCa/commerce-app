package pe.commerce.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class UbigeoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static Ubigeo getUbigeoSample1() {
        return new Ubigeo()
            .id(1L)
            .codigo("codigo1")
            .departamento("departamento1")
            .provincia("provincia1")
            .distrito("distrito1")
            .createdBy("createdBy1")
            .lastModifiedBy("lastModifiedBy1");
    }

    public static Ubigeo getUbigeoSample2() {
        return new Ubigeo()
            .id(2L)
            .codigo("codigo2")
            .departamento("departamento2")
            .provincia("provincia2")
            .distrito("distrito2")
            .createdBy("createdBy2")
            .lastModifiedBy("lastModifiedBy2");
    }

    public static Ubigeo getUbigeoRandomSampleGenerator() {
        return new Ubigeo()
            .id(longCount.incrementAndGet())
            .codigo(UUID.randomUUID().toString())
            .departamento(UUID.randomUUID().toString())
            .provincia(UUID.randomUUID().toString())
            .distrito(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
