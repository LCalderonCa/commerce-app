package pe.commerce.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class CategoriaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + 2 * Short.MAX_VALUE);

    public static Categoria getCategoriaSample1() {
        return new Categoria().id(1L).nombre("nombre1").slug("slug1").orden(1).createdBy("createdBy1").lastModifiedBy("lastModifiedBy1");
    }

    public static Categoria getCategoriaSample2() {
        return new Categoria().id(2L).nombre("nombre2").slug("slug2").orden(2).createdBy("createdBy2").lastModifiedBy("lastModifiedBy2");
    }

    public static Categoria getCategoriaRandomSampleGenerator() {
        return new Categoria()
            .id(longCount.incrementAndGet())
            .nombre(UUID.randomUUID().toString())
            .slug(UUID.randomUUID().toString())
            .orden(intCount.incrementAndGet())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
