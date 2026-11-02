package pe.commerce.app.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.commerce.app.domain.Resena;

/**
 * Spring Data JPA repository for the Resena entity.
 */
@Repository
public interface ResenaRepository extends JpaRepository<Resena, Long> {
    default Optional<Resena> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Resena> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Resena> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select resena from Resena resena left join fetch resena.cliente left join fetch resena.producto",
        countQuery = "select count(resena) from Resena resena"
    )
    Page<Resena> findAllWithToOneRelationships(Pageable pageable);

    @Query("select resena from Resena resena left join fetch resena.cliente left join fetch resena.producto")
    List<Resena> findAllWithToOneRelationships();

    @Query("select resena from Resena resena left join fetch resena.cliente left join fetch resena.producto where resena.id =:id")
    Optional<Resena> findOneWithToOneRelationships(@Param("id") Long id);
}
