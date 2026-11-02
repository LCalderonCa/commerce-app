package pe.commerce.app.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ComprobanteCriteriaTest {

    @Test
    void newComprobanteCriteriaHasAllFiltersNullTest() {
        var comprobanteCriteria = new ComprobanteCriteria();
        assertThat(comprobanteCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void comprobanteCriteriaFluentMethodsCreatesFiltersTest() {
        var comprobanteCriteria = new ComprobanteCriteria();

        setAllFilters(comprobanteCriteria);

        assertThat(comprobanteCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void comprobanteCriteriaCopyCreatesNullFilterTest() {
        var comprobanteCriteria = new ComprobanteCriteria();
        var copy = comprobanteCriteria.copy();

        assertThat(comprobanteCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(comprobanteCriteria)
        );
    }

    @Test
    void comprobanteCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var comprobanteCriteria = new ComprobanteCriteria();
        setAllFilters(comprobanteCriteria);

        var copy = comprobanteCriteria.copy();

        assertThat(comprobanteCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(comprobanteCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var comprobanteCriteria = new ComprobanteCriteria();

        assertThat(comprobanteCriteria).hasToString("ComprobanteCriteria{}");
    }

    private static void setAllFilters(ComprobanteCriteria comprobanteCriteria) {
        comprobanteCriteria.id();
        comprobanteCriteria.tipo();
        comprobanteCriteria.serie();
        comprobanteCriteria.numero();
        comprobanteCriteria.fechaEmision();
        comprobanteCriteria.receptorTipoDocumento();
        comprobanteCriteria.receptorNroDocumento();
        comprobanteCriteria.receptorNombre();
        comprobanteCriteria.receptorDireccion();
        comprobanteCriteria.moneda();
        comprobanteCriteria.opGravada();
        comprobanteCriteria.igv();
        comprobanteCriteria.total();
        comprobanteCriteria.estadoSunat();
        comprobanteCriteria.xmlUrl();
        comprobanteCriteria.pdfUrl();
        comprobanteCriteria.createdBy();
        comprobanteCriteria.createdDate();
        comprobanteCriteria.lastModifiedBy();
        comprobanteCriteria.lastModifiedDate();
        comprobanteCriteria.comprobanteReferenciaId();
        comprobanteCriteria.pedidoId();
        comprobanteCriteria.distinct();
    }

    private static Condition<ComprobanteCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getTipo()) &&
                condition.apply(criteria.getSerie()) &&
                condition.apply(criteria.getNumero()) &&
                condition.apply(criteria.getFechaEmision()) &&
                condition.apply(criteria.getReceptorTipoDocumento()) &&
                condition.apply(criteria.getReceptorNroDocumento()) &&
                condition.apply(criteria.getReceptorNombre()) &&
                condition.apply(criteria.getReceptorDireccion()) &&
                condition.apply(criteria.getMoneda()) &&
                condition.apply(criteria.getOpGravada()) &&
                condition.apply(criteria.getIgv()) &&
                condition.apply(criteria.getTotal()) &&
                condition.apply(criteria.getEstadoSunat()) &&
                condition.apply(criteria.getXmlUrl()) &&
                condition.apply(criteria.getPdfUrl()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate()) &&
                condition.apply(criteria.getComprobanteReferenciaId()) &&
                condition.apply(criteria.getPedidoId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ComprobanteCriteria> copyFiltersAre(ComprobanteCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getTipo(), copy.getTipo()) &&
                condition.apply(criteria.getSerie(), copy.getSerie()) &&
                condition.apply(criteria.getNumero(), copy.getNumero()) &&
                condition.apply(criteria.getFechaEmision(), copy.getFechaEmision()) &&
                condition.apply(criteria.getReceptorTipoDocumento(), copy.getReceptorTipoDocumento()) &&
                condition.apply(criteria.getReceptorNroDocumento(), copy.getReceptorNroDocumento()) &&
                condition.apply(criteria.getReceptorNombre(), copy.getReceptorNombre()) &&
                condition.apply(criteria.getReceptorDireccion(), copy.getReceptorDireccion()) &&
                condition.apply(criteria.getMoneda(), copy.getMoneda()) &&
                condition.apply(criteria.getOpGravada(), copy.getOpGravada()) &&
                condition.apply(criteria.getIgv(), copy.getIgv()) &&
                condition.apply(criteria.getTotal(), copy.getTotal()) &&
                condition.apply(criteria.getEstadoSunat(), copy.getEstadoSunat()) &&
                condition.apply(criteria.getXmlUrl(), copy.getXmlUrl()) &&
                condition.apply(criteria.getPdfUrl(), copy.getPdfUrl()) &&
                condition.apply(criteria.getCreatedBy(), copy.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy(), copy.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate(), copy.getLastModifiedDate()) &&
                condition.apply(criteria.getComprobanteReferenciaId(), copy.getComprobanteReferenciaId()) &&
                condition.apply(criteria.getPedidoId(), copy.getPedidoId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
