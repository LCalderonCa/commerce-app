package pe.commerce.app.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static pe.commerce.app.domain.EmpleadoTestSamples.*;
import static pe.commerce.app.domain.InventarioTestSamples.*;
import static pe.commerce.app.domain.TiendaTestSamples.*;
import static pe.commerce.app.domain.UbigeoTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import pe.commerce.app.web.rest.TestUtil;

class TiendaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Tienda.class);
        Tienda tienda1 = getTiendaSample1();
        Tienda tienda2 = new Tienda();
        assertThat(tienda1).isNotEqualTo(tienda2);

        tienda2.setId(tienda1.getId());
        assertThat(tienda1).isEqualTo(tienda2);

        tienda2 = getTiendaSample2();
        assertThat(tienda1).isNotEqualTo(tienda2);
    }

    @Test
    void inventariosTest() {
        Tienda tienda = getTiendaRandomSampleGenerator();
        Inventario inventarioBack = getInventarioRandomSampleGenerator();

        tienda.addInventarios(inventarioBack);
        assertThat(tienda.getInventarioses()).containsOnly(inventarioBack);
        assertThat(inventarioBack.getTienda()).isEqualTo(tienda);

        tienda.removeInventarios(inventarioBack);
        assertThat(tienda.getInventarioses()).doesNotContain(inventarioBack);
        assertThat(inventarioBack.getTienda()).isNull();

        tienda.inventarioses(new HashSet<>(Set.of(inventarioBack)));
        assertThat(tienda.getInventarioses()).containsOnly(inventarioBack);
        assertThat(inventarioBack.getTienda()).isEqualTo(tienda);

        tienda.setInventarioses(new HashSet<>());
        assertThat(tienda.getInventarioses()).doesNotContain(inventarioBack);
        assertThat(inventarioBack.getTienda()).isNull();
    }

    @Test
    void empleadosTest() {
        Tienda tienda = getTiendaRandomSampleGenerator();
        Empleado empleadoBack = getEmpleadoRandomSampleGenerator();

        tienda.addEmpleados(empleadoBack);
        assertThat(tienda.getEmpleadoses()).containsOnly(empleadoBack);
        assertThat(empleadoBack.getTienda()).isEqualTo(tienda);

        tienda.removeEmpleados(empleadoBack);
        assertThat(tienda.getEmpleadoses()).doesNotContain(empleadoBack);
        assertThat(empleadoBack.getTienda()).isNull();

        tienda.empleadoses(new HashSet<>(Set.of(empleadoBack)));
        assertThat(tienda.getEmpleadoses()).containsOnly(empleadoBack);
        assertThat(empleadoBack.getTienda()).isEqualTo(tienda);

        tienda.setEmpleadoses(new HashSet<>());
        assertThat(tienda.getEmpleadoses()).doesNotContain(empleadoBack);
        assertThat(empleadoBack.getTienda()).isNull();
    }

    @Test
    void ubigeoTest() {
        Tienda tienda = getTiendaRandomSampleGenerator();
        Ubigeo ubigeoBack = getUbigeoRandomSampleGenerator();

        tienda.setUbigeo(ubigeoBack);
        assertThat(tienda.getUbigeo()).isEqualTo(ubigeoBack);

        tienda.ubigeo(null);
        assertThat(tienda.getUbigeo()).isNull();
    }
}
