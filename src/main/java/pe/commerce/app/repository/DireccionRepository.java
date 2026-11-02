package pe.commerce.app.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.commerce.app.domain.Direccion;

/**
 * Spring Data JPA repository for the Direccion entity.
 */
@Repository
public interface DireccionRepository extends JpaRepository<Direccion, Long> {
    default Optional<Direccion> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Direccion> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Direccion> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select direccion from Direccion direccion left join fetch direccion.ubigeo left join fetch direccion.cliente",
        countQuery = "select count(direccion) from Direccion direccion"
    )
    Page<Direccion> findAllWithToOneRelationships(Pageable pageable);

    @Query("select direccion from Direccion direccion left join fetch direccion.ubigeo left join fetch direccion.cliente")
    List<Direccion> findAllWithToOneRelationships();

    @Query(
        "select direccion from Direccion direccion left join fetch direccion.ubigeo left join fetch direccion.cliente where direccion.id =:id"
    )
    Optional<Direccion> findOneWithToOneRelationships(@Param("id") Long id);
}
