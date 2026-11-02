package pe.commerce.app.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ClienteCriteriaTest {

    @Test
    void newClienteCriteriaHasAllFiltersNullTest() {
        var clienteCriteria = new ClienteCriteria();
        assertThat(clienteCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void clienteCriteriaFluentMethodsCreatesFiltersTest() {
        var clienteCriteria = new ClienteCriteria();

        setAllFilters(clienteCriteria);

        assertThat(clienteCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void clienteCriteriaCopyCreatesNullFilterTest() {
        var clienteCriteria = new ClienteCriteria();
        var copy = clienteCriteria.copy();

        assertThat(clienteCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(clienteCriteria)
        );
    }

    @Test
    void clienteCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var clienteCriteria = new ClienteCriteria();
        setAllFilters(clienteCriteria);

        var copy = clienteCriteria.copy();

        assertThat(clienteCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(clienteCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var clienteCriteria = new ClienteCriteria();

        assertThat(clienteCriteria).hasToString("ClienteCriteria{}");
    }

    private static void setAllFilters(ClienteCriteria clienteCriteria) {
        clienteCriteria.id();
        clienteCriteria.codigo();
        clienteCriteria.fechaRegistro();
        clienteCriteria.aceptaPublicidad();
        clienteCriteria.activo();
        clienteCriteria.createdBy();
        clienteCriteria.createdDate();
        clienteCriteria.lastModifiedBy();
        clienteCriteria.lastModifiedDate();
        clienteCriteria.userId();
        clienteCriteria.personaId();
        clienteCriteria.direccionesId();
        clienteCriteria.datosFacturacionId();
        clienteCriteria.pedidosId();
        clienteCriteria.resenasId();
        clienteCriteria.carritoId();
        clienteCriteria.distinct();
    }

    private static Condition<ClienteCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getCodigo()) &&
                condition.apply(criteria.getFechaRegistro()) &&
                condition.apply(criteria.getAceptaPublicidad()) &&
                condition.apply(criteria.getActivo()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate()) &&
                condition.apply(criteria.getUserId()) &&
                condition.apply(criteria.getPersonaId()) &&
                condition.apply(criteria.getDireccionesId()) &&
                condition.apply(criteria.getDatosFacturacionId()) &&
                condition.apply(criteria.getPedidosId()) &&
                condition.apply(criteria.getResenasId()) &&
                condition.apply(criteria.getCarritoId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ClienteCriteria> copyFiltersAre(ClienteCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getCodigo(), copy.getCodigo()) &&
                condition.apply(criteria.getFechaRegistro(), copy.getFechaRegistro()) &&
                condition.apply(criteria.getAceptaPublicidad(), copy.getAceptaPublicidad()) &&
                condition.apply(criteria.getActivo(), copy.getActivo()) &&
                condition.apply(criteria.getCreatedBy(), copy.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy(), copy.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate(), copy.getLastModifiedDate()) &&
                condition.apply(criteria.getUserId(), copy.getUserId()) &&
                condition.apply(criteria.getPersonaId(), copy.getPersonaId()) &&
                condition.apply(criteria.getDireccionesId(), copy.getDireccionesId()) &&
                condition.apply(criteria.getDatosFacturacionId(), copy.getDatosFacturacionId()) &&
                condition.apply(criteria.getPedidosId(), copy.getPedidosId()) &&
                condition.apply(criteria.getResenasId(), copy.getResenasId()) &&
                condition.apply(criteria.getCarritoId(), copy.getCarritoId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
