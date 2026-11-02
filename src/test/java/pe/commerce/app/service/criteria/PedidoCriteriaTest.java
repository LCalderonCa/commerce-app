package pe.commerce.app.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class PedidoCriteriaTest {

    @Test
    void newPedidoCriteriaHasAllFiltersNullTest() {
        var pedidoCriteria = new PedidoCriteria();
        assertThat(pedidoCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void pedidoCriteriaFluentMethodsCreatesFiltersTest() {
        var pedidoCriteria = new PedidoCriteria();

        setAllFilters(pedidoCriteria);

        assertThat(pedidoCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void pedidoCriteriaCopyCreatesNullFilterTest() {
        var pedidoCriteria = new PedidoCriteria();
        var copy = pedidoCriteria.copy();

        assertThat(pedidoCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(pedidoCriteria)
        );
    }

    @Test
    void pedidoCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var pedidoCriteria = new PedidoCriteria();
        setAllFilters(pedidoCriteria);

        var copy = pedidoCriteria.copy();

        assertThat(pedidoCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(pedidoCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var pedidoCriteria = new PedidoCriteria();

        assertThat(pedidoCriteria).hasToString("PedidoCriteria{}");
    }

    private static void setAllFilters(PedidoCriteria pedidoCriteria) {
        pedidoCriteria.id();
        pedidoCriteria.nroPedido();
        pedidoCriteria.tipoEntrega();
        pedidoCriteria.tipoComprobante();
        pedidoCriteria.estado();
        pedidoCriteria.moneda();
        pedidoCriteria.subtotal();
        pedidoCriteria.descuento();
        pedidoCriteria.costoEnvio();
        pedidoCriteria.igv();
        pedidoCriteria.total();
        pedidoCriteria.fechaPedido();
        pedidoCriteria.fechaExpiracion();
        pedidoCriteria.observaciones();
        pedidoCriteria.createdBy();
        pedidoCriteria.createdDate();
        pedidoCriteria.lastModifiedBy();
        pedidoCriteria.lastModifiedDate();
        pedidoCriteria.detallesId();
        pedidoCriteria.historialEstadosId();
        pedidoCriteria.pagosId();
        pedidoCriteria.comprobantesId();
        pedidoCriteria.cuponId();
        pedidoCriteria.tiendaRecojoId();
        pedidoCriteria.atendidoPorId();
        pedidoCriteria.envioId();
        pedidoCriteria.clienteId();
        pedidoCriteria.distinct();
    }

    private static Condition<PedidoCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getNroPedido()) &&
                condition.apply(criteria.getTipoEntrega()) &&
                condition.apply(criteria.getTipoComprobante()) &&
                condition.apply(criteria.getEstado()) &&
                condition.apply(criteria.getMoneda()) &&
                condition.apply(criteria.getSubtotal()) &&
                condition.apply(criteria.getDescuento()) &&
                condition.apply(criteria.getCostoEnvio()) &&
                condition.apply(criteria.getIgv()) &&
                condition.apply(criteria.getTotal()) &&
                condition.apply(criteria.getFechaPedido()) &&
                condition.apply(criteria.getFechaExpiracion()) &&
                condition.apply(criteria.getObservaciones()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate()) &&
                condition.apply(criteria.getDetallesId()) &&
                condition.apply(criteria.getHistorialEstadosId()) &&
                condition.apply(criteria.getPagosId()) &&
                condition.apply(criteria.getComprobantesId()) &&
                condition.apply(criteria.getCuponId()) &&
                condition.apply(criteria.getTiendaRecojoId()) &&
                condition.apply(criteria.getAtendidoPorId()) &&
                condition.apply(criteria.getEnvioId()) &&
                condition.apply(criteria.getClienteId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<PedidoCriteria> copyFiltersAre(PedidoCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getNroPedido(), copy.getNroPedido()) &&
                condition.apply(criteria.getTipoEntrega(), copy.getTipoEntrega()) &&
                condition.apply(criteria.getTipoComprobante(), copy.getTipoComprobante()) &&
                condition.apply(criteria.getEstado(), copy.getEstado()) &&
                condition.apply(criteria.getMoneda(), copy.getMoneda()) &&
                condition.apply(criteria.getSubtotal(), copy.getSubtotal()) &&
                condition.apply(criteria.getDescuento(), copy.getDescuento()) &&
                condition.apply(criteria.getCostoEnvio(), copy.getCostoEnvio()) &&
                condition.apply(criteria.getIgv(), copy.getIgv()) &&
                condition.apply(criteria.getTotal(), copy.getTotal()) &&
                condition.apply(criteria.getFechaPedido(), copy.getFechaPedido()) &&
                condition.apply(criteria.getFechaExpiracion(), copy.getFechaExpiracion()) &&
                condition.apply(criteria.getObservaciones(), copy.getObservaciones()) &&
                condition.apply(criteria.getCreatedBy(), copy.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy(), copy.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate(), copy.getLastModifiedDate()) &&
                condition.apply(criteria.getDetallesId(), copy.getDetallesId()) &&
                condition.apply(criteria.getHistorialEstadosId(), copy.getHistorialEstadosId()) &&
                condition.apply(criteria.getPagosId(), copy.getPagosId()) &&
                condition.apply(criteria.getComprobantesId(), copy.getComprobantesId()) &&
                condition.apply(criteria.getCuponId(), copy.getCuponId()) &&
                condition.apply(criteria.getTiendaRecojoId(), copy.getTiendaRecojoId()) &&
                condition.apply(criteria.getAtendidoPorId(), copy.getAtendidoPorId()) &&
                condition.apply(criteria.getEnvioId(), copy.getEnvioId()) &&
                condition.apply(criteria.getClienteId(), copy.getClienteId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
