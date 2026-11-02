package pe.commerce.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PedidoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static Pedido getPedidoSample1() {
        return new Pedido()
            .id(1L)
            .nroPedido("nroPedido1")
            .observaciones("observaciones1")
            .createdBy("createdBy1")
            .lastModifiedBy("lastModifiedBy1");
    }

    public static Pedido getPedidoSample2() {
        return new Pedido()
            .id(2L)
            .nroPedido("nroPedido2")
            .observaciones("observaciones2")
            .createdBy("createdBy2")
            .lastModifiedBy("lastModifiedBy2");
    }

    public static Pedido getPedidoRandomSampleGenerator() {
        return new Pedido()
            .id(longCount.incrementAndGet())
            .nroPedido(UUID.randomUUID().toString())
            .observaciones(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
