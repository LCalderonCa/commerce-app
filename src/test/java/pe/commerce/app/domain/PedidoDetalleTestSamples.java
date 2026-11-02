package pe.commerce.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class PedidoDetalleTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + 2 * Short.MAX_VALUE);

    public static PedidoDetalle getPedidoDetalleSample1() {
        return new PedidoDetalle()
            .id(1L)
            .skuProducto("skuProducto1")
            .nombreProducto("nombreProducto1")
            .cantidad(1)
            .createdBy("createdBy1")
            .lastModifiedBy("lastModifiedBy1");
    }

    public static PedidoDetalle getPedidoDetalleSample2() {
        return new PedidoDetalle()
            .id(2L)
            .skuProducto("skuProducto2")
            .nombreProducto("nombreProducto2")
            .cantidad(2)
            .createdBy("createdBy2")
            .lastModifiedBy("lastModifiedBy2");
    }

    public static PedidoDetalle getPedidoDetalleRandomSampleGenerator() {
        return new PedidoDetalle()
            .id(longCount.incrementAndGet())
            .skuProducto(UUID.randomUUID().toString())
            .nombreProducto(UUID.randomUUID().toString())
            .cantidad(intCount.incrementAndGet())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
