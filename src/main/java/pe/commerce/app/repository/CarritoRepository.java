package pe.commerce.app.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.commerce.app.domain.Carrito;

/**
 * Spring Data JPA repository for the Carrito entity.
 */
@Repository
public interface CarritoRepository extends JpaRepository<Carrito, Long> {
    default Optional<Carrito> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Carrito> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Carrito> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select carrito from Carrito carrito left join fetch carrito.cliente",
        countQuery = "select count(carrito) from Carrito carrito"
    )
    Page<Carrito> findAllWithToOneRelationships(Pageable pageable);

    @Query("select carrito from Carrito carrito left join fetch carrito.cliente")
    List<Carrito> findAllWithToOneRelationships();

    @Query("select carrito from Carrito carrito left join fetch carrito.cliente where carrito.id =:id")
    Optional<Carrito> findOneWithToOneRelationships(@Param("id") Long id);
}
