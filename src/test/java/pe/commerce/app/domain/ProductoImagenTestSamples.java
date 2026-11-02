package pe.commerce.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ProductoImagenTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + 2 * Short.MAX_VALUE);

    public static ProductoImagen getProductoImagenSample1() {
        return new ProductoImagen().id(1L).url("url1").orden(1).createdBy("createdBy1").lastModifiedBy("lastModifiedBy1");
    }

    public static ProductoImagen getProductoImagenSample2() {
        return new ProductoImagen().id(2L).url("url2").orden(2).createdBy("createdBy2").lastModifiedBy("lastModifiedBy2");
    }

    public static ProductoImagen getProductoImagenRandomSampleGenerator() {
        return new ProductoImagen()
            .id(longCount.incrementAndGet())
            .url(UUID.randomUUID().toString())
            .orden(intCount.incrementAndGet())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
