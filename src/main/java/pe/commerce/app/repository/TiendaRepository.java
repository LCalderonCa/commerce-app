package pe.commerce.app.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.commerce.app.domain.Tienda;

/**
 * Spring Data JPA repository for the Tienda entity.
 */
@Repository
public interface TiendaRepository extends JpaRepository<Tienda, Long> {
    default Optional<Tienda> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Tienda> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Tienda> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(value = "select tienda from Tienda tienda left join fetch tienda.ubigeo", countQuery = "select count(tienda) from Tienda tienda")
    Page<Tienda> findAllWithToOneRelationships(Pageable pageable);

    @Query("select tienda from Tienda tienda left join fetch tienda.ubigeo")
    List<Tienda> findAllWithToOneRelationships();

    @Query("select tienda from Tienda tienda left join fetch tienda.ubigeo where tienda.id =:id")
    Optional<Tienda> findOneWithToOneRelationships(@Param("id") Long id);
}
