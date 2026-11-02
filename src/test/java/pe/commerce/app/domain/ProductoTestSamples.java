package pe.commerce.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ProductoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + 2 * Short.MAX_VALUE);

    public static Producto getProductoSample1() {
        return new Producto()
            .id(1L)
            .sku("sku1")
            .nombre("nombre1")
            .slug("slug1")
            .modelo("modelo1")
            .descripcion("descripcion1")
            .especificaciones("especificaciones1")
            .garantiaMeses(1)
            .createdBy("createdBy1")
            .lastModifiedBy("lastModifiedBy1");
    }

    public static Producto getProductoSample2() {
        return new Producto()
            .id(2L)
            .sku("sku2")
            .nombre("nombre2")
            .slug("slug2")
            .modelo("modelo2")
            .descripcion("descripcion2")
            .especificaciones("especificaciones2")
            .garantiaMeses(2)
            .createdBy("createdBy2")
            .lastModifiedBy("lastModifiedBy2");
    }

    public static Producto getProductoRandomSampleGenerator() {
        return new Producto()
            .id(longCount.incrementAndGet())
            .sku(UUID.randomUUID().toString())
            .nombre(UUID.randomUUID().toString())
            .slug(UUID.randomUUID().toString())
            .modelo(UUID.randomUUID().toString())
            .descripcion(UUID.randomUUID().toString())
            .especificaciones(UUID.randomUUID().toString())
            .garantiaMeses(intCount.incrementAndGet())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
