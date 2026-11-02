package pe.commerce.app.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.commerce.app.domain.Categoria;

/**
 * Spring Data JPA repository for the Categoria entity.
 */
@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    default Optional<Categoria> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Categoria> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Categoria> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select categoria from Categoria categoria left join fetch categoria.padre",
        countQuery = "select count(categoria) from Categoria categoria"
    )
    Page<Categoria> findAllWithToOneRelationships(Pageable pageable);

    @Query("select categoria from Categoria categoria left join fetch categoria.padre")
    List<Categoria> findAllWithToOneRelationships();

    @Query("select categoria from Categoria categoria left join fetch categoria.padre where categoria.id =:id")
    Optional<Categoria> findOneWithToOneRelationships(@Param("id") Long id);
}
