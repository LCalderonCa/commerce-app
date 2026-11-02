package pe.commerce.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PagoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static Pago getPagoSample1() {
        return new Pago()
            .id(1L)
            .subMetodo("subMetodo1")
            .pasarela("pasarela1")
            .nroTransaccion("nroTransaccion1")
            .createdBy("createdBy1")
            .lastModifiedBy("lastModifiedBy1");
    }

    public static Pago getPagoSample2() {
        return new Pago()
            .id(2L)
            .subMetodo("subMetodo2")
            .pasarela("pasarela2")
            .nroTransaccion("nroTransaccion2")
            .createdBy("createdBy2")
            .lastModifiedBy("lastModifiedBy2");
    }

    public static Pago getPagoRandomSampleGenerator() {
        return new Pago()
            .id(longCount.incrementAndGet())
            .subMetodo(UUID.randomUUID().toString())
            .pasarela(UUID.randomUUID().toString())
            .nroTransaccion(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
