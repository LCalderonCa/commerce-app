package pe.commerce.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ResenaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + 2 * Short.MAX_VALUE);

    public static Resena getResenaSample1() {
        return new Resena().id(1L).calificacion(1).comentario("comentario1").createdBy("createdBy1").lastModifiedBy("lastModifiedBy1");
    }

    public static Resena getResenaSample2() {
        return new Resena().id(2L).calificacion(2).comentario("comentario2").createdBy("createdBy2").lastModifiedBy("lastModifiedBy2");
    }

    public static Resena getResenaRandomSampleGenerator() {
        return new Resena()
            .id(longCount.incrementAndGet())
            .calificacion(intCount.incrementAndGet())
            .comentario(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
