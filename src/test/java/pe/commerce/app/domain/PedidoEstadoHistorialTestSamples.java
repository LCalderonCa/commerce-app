package pe.commerce.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PedidoEstadoHistorialTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static PedidoEstadoHistorial getPedidoEstadoHistorialSample1() {
        return new PedidoEstadoHistorial().id(1L).comentario("comentario1").createdBy("createdBy1").lastModifiedBy("lastModifiedBy1");
    }

    public static PedidoEstadoHistorial getPedidoEstadoHistorialSample2() {
        return new PedidoEstadoHistorial().id(2L).comentario("comentario2").createdBy("createdBy2").lastModifiedBy("lastModifiedBy2");
    }

    public static PedidoEstadoHistorial getPedidoEstadoHistorialRandomSampleGenerator() {
        return new PedidoEstadoHistorial()
            .id(longCount.incrementAndGet())
            .comentario(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
