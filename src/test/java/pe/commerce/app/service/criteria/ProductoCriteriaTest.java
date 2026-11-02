package pe.commerce.app.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ProductoCriteriaTest {

    @Test
    void newProductoCriteriaHasAllFiltersNullTest() {
        var productoCriteria = new ProductoCriteria();
        assertThat(productoCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void productoCriteriaFluentMethodsCreatesFiltersTest() {
        var productoCriteria = new ProductoCriteria();

        setAllFilters(productoCriteria);

        assertThat(productoCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void productoCriteriaCopyCreatesNullFilterTest() {
        var productoCriteria = new ProductoCriteria();
        var copy = productoCriteria.copy();

        assertThat(productoCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(productoCriteria)
        );
    }

    @Test
    void productoCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var productoCriteria = new ProductoCriteria();
        setAllFilters(productoCriteria);

        var copy = productoCriteria.copy();

        assertThat(productoCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(productoCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var productoCriteria = new ProductoCriteria();

        assertThat(productoCriteria).hasToString("ProductoCriteria{}");
    }

    private static void setAllFilters(ProductoCriteria productoCriteria) {
        productoCriteria.id();
        productoCriteria.sku();
        productoCriteria.nombre();
        productoCriteria.slug();
        productoCriteria.modelo();
        productoCriteria.descripcion();
        productoCriteria.especificaciones();
        productoCriteria.precioLista();
        productoCriteria.precioOferta();
        productoCriteria.pesoKg();
        productoCriteria.garantiaMeses();
        productoCriteria.activo();
        productoCriteria.createdBy();
        productoCriteria.createdDate();
        productoCriteria.lastModifiedBy();
        productoCriteria.lastModifiedDate();
        productoCriteria.imagenesId();
        productoCriteria.inventariosId();
        productoCriteria.resenasId();
        productoCriteria.categoriaId();
        productoCriteria.marcaId();
        productoCriteria.distinct();
    }

    private static Condition<ProductoCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getSku()) &&
                condition.apply(criteria.getNombre()) &&
                condition.apply(criteria.getSlug()) &&
                condition.apply(criteria.getModelo()) &&
                condition.apply(criteria.getDescripcion()) &&
                condition.apply(criteria.getEspecificaciones()) &&
                condition.apply(criteria.getPrecioLista()) &&
                condition.apply(criteria.getPrecioOferta()) &&
                condition.apply(criteria.getPesoKg()) &&
                condition.apply(criteria.getGarantiaMeses()) &&
                condition.apply(criteria.getActivo()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate()) &&
                condition.apply(criteria.getImagenesId()) &&
                condition.apply(criteria.getInventariosId()) &&
                condition.apply(criteria.getResenasId()) &&
                condition.apply(criteria.getCategoriaId()) &&
                condition.apply(criteria.getMarcaId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ProductoCriteria> copyFiltersAre(ProductoCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getSku(), copy.getSku()) &&
                condition.apply(criteria.getNombre(), copy.getNombre()) &&
                condition.apply(criteria.getSlug(), copy.getSlug()) &&
                condition.apply(criteria.getModelo(), copy.getModelo()) &&
                condition.apply(criteria.getDescripcion(), copy.getDescripcion()) &&
                condition.apply(criteria.getEspecificaciones(), copy.getEspecificaciones()) &&
                condition.apply(criteria.getPrecioLista(), copy.getPrecioLista()) &&
                condition.apply(criteria.getPrecioOferta(), copy.getPrecioOferta()) &&
                condition.apply(criteria.getPesoKg(), copy.getPesoKg()) &&
                condition.apply(criteria.getGarantiaMeses(), copy.getGarantiaMeses()) &&
                condition.apply(criteria.getActivo(), copy.getActivo()) &&
                condition.apply(criteria.getCreatedBy(), copy.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy(), copy.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate(), copy.getLastModifiedDate()) &&
                condition.apply(criteria.getImagenesId(), copy.getImagenesId()) &&
                condition.apply(criteria.getInventariosId(), copy.getInventariosId()) &&
                condition.apply(criteria.getResenasId(), copy.getResenasId()) &&
                condition.apply(criteria.getCategoriaId(), copy.getCategoriaId()) &&
                condition.apply(criteria.getMarcaId(), copy.getMarcaId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
