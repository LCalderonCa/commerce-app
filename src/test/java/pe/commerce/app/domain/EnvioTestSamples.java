package pe.commerce.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class EnvioTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static Envio getEnvioSample1() {
        return new Envio()
            .id(1L)
            .courier("courier1")
            .tracking("tracking1")
            .direccionEntrega("direccionEntrega1")
            .ubigeoEntrega("ubigeoEntrega1")
            .referenciaEntrega("referenciaEntrega1")
            .nombreReceptor("nombreReceptor1")
            .telefonoReceptor("telefonoReceptor1")
            .createdBy("createdBy1")
            .lastModifiedBy("lastModifiedBy1");
    }

    public static Envio getEnvioSample2() {
        return new Envio()
            .id(2L)
            .courier("courier2")
            .tracking("tracking2")
            .direccionEntrega("direccionEntrega2")
            .ubigeoEntrega("ubigeoEntrega2")
            .referenciaEntrega("referenciaEntrega2")
            .nombreReceptor("nombreReceptor2")
            .telefonoReceptor("telefonoReceptor2")
            .createdBy("createdBy2")
            .lastModifiedBy("lastModifiedBy2");
    }

    public static Envio getEnvioRandomSampleGenerator() {
        return new Envio()
            .id(longCount.incrementAndGet())
            .courier(UUID.randomUUID().toString())
            .tracking(UUID.randomUUID().toString())
            .direccionEntrega(UUID.randomUUID().toString())
            .ubigeoEntrega(UUID.randomUUID().toString())
            .referenciaEntrega(UUID.randomUUID().toString())
            .nombreReceptor(UUID.randomUUID().toString())
            .telefonoReceptor(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
