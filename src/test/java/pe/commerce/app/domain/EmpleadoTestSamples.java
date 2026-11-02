package pe.commerce.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class EmpleadoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static Empleado getEmpleadoSample1() {
        return new Empleado().id(1L).codigo("codigo1").createdBy("createdBy1").lastModifiedBy("lastModifiedBy1");
    }

    public static Empleado getEmpleadoSample2() {
        return new Empleado().id(2L).codigo("codigo2").createdBy("createdBy2").lastModifiedBy("lastModifiedBy2");
    }

    public static Empleado getEmpleadoRandomSampleGenerator() {
        return new Empleado()
            .id(longCount.incrementAndGet())
            .codigo(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
