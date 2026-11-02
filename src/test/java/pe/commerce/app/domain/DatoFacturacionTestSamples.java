package pe.commerce.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class DatoFacturacionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static DatoFacturacion getDatoFacturacionSample1() {
        return new DatoFacturacion()
            .id(1L)
            .ruc("ruc1")
            .razonSocial("razonSocial1")
            .direccionFiscal("direccionFiscal1")
            .createdBy("createdBy1")
            .lastModifiedBy("lastModifiedBy1");
    }

    public static DatoFacturacion getDatoFacturacionSample2() {
        return new DatoFacturacion()
            .id(2L)
            .ruc("ruc2")
            .razonSocial("razonSocial2")
            .direccionFiscal("direccionFiscal2")
            .createdBy("createdBy2")
            .lastModifiedBy("lastModifiedBy2");
    }

    public static DatoFacturacion getDatoFacturacionRandomSampleGenerator() {
        return new DatoFacturacion()
            .id(longCount.incrementAndGet())
            .ruc(UUID.randomUUID().toString())
            .razonSocial(UUID.randomUUID().toString())
            .direccionFiscal(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
