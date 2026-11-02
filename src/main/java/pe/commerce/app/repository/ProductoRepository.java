package pe.commerce.app.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.commerce.app.domain.Producto;

/**
 * Spring Data JPA repository for the Producto entity.
 */
@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long>, JpaSpecificationExecutor<Producto> {
    default Optional<Producto> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Producto> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Producto> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select producto from Producto producto left join fetch producto.categoria left join fetch producto.marca",
        countQuery = "select count(producto) from Producto producto"
    )
    Page<Producto> findAllWithToOneRelationships(Pageable pageable);

    @Query("select producto from Producto producto left join fetch producto.categoria left join fetch producto.marca")
    List<Producto> findAllWithToOneRelationships();

    @Query(
        "select producto from Producto producto left join fetch producto.categoria left join fetch producto.marca where producto.id =:id"
    )
    Optional<Producto> findOneWithToOneRelationships(@Param("id") Long id);
}
