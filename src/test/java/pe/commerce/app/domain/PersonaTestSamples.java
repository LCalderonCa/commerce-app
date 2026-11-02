package pe.commerce.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PersonaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static Persona getPersonaSample1() {
        return new Persona().id(1L).nombres("nombres1").apellidos("apellidos1").createdBy("createdBy1").lastModifiedBy("lastModifiedBy1");
    }

    public static Persona getPersonaSample2() {
        return new Persona().id(2L).nombres("nombres2").apellidos("apellidos2").createdBy("createdBy2").lastModifiedBy("lastModifiedBy2");
    }

    public static Persona getPersonaRandomSampleGenerator() {
        return new Persona()
            .id(longCount.incrementAndGet())
            .nombres(UUID.randomUUID().toString())
            .apellidos(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
