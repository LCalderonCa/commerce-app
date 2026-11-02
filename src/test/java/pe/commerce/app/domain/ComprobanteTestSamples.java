package pe.commerce.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ComprobanteTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + 2 * Short.MAX_VALUE);

    public static Comprobante getComprobanteSample1() {
        return new Comprobante()
            .id(1L)
            .serie("serie1")
            .numero(1)
            .receptorTipoDocumento("receptorTipoDocumento1")
            .receptorNroDocumento("receptorNroDocumento1")
            .receptorNombre("receptorNombre1")
            .receptorDireccion("receptorDireccion1")
            .xmlUrl("xmlUrl1")
            .pdfUrl("pdfUrl1")
            .createdBy("createdBy1")
            .lastModifiedBy("lastModifiedBy1");
    }

    public static Comprobante getComprobanteSample2() {
        return new Comprobante()
            .id(2L)
            .serie("serie2")
            .numero(2)
            .receptorTipoDocumento("receptorTipoDocumento2")
            .receptorNroDocumento("receptorNroDocumento2")
            .receptorNombre("receptorNombre2")
            .receptorDireccion("receptorDireccion2")
            .xmlUrl("xmlUrl2")
            .pdfUrl("pdfUrl2")
            .createdBy("createdBy2")
            .lastModifiedBy("lastModifiedBy2");
    }

    public static Comprobante getComprobanteRandomSampleGenerator() {
        return new Comprobante()
            .id(longCount.incrementAndGet())
            .serie(UUID.randomUUID().toString())
            .numero(intCount.incrementAndGet())
            .receptorTipoDocumento(UUID.randomUUID().toString())
            .receptorNroDocumento(UUID.randomUUID().toString())
            .receptorNombre(UUID.randomUUID().toString())
            .receptorDireccion(UUID.randomUUID().toString())
            .xmlUrl(UUID.randomUUID().toString())
            .pdfUrl(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
