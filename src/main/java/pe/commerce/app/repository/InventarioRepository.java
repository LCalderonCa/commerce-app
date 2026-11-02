package pe.commerce.app.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.commerce.app.domain.Inventario;

/**
 * Spring Data JPA repository for the Inventario entity.
 */
@Repository
public interface InventarioRepository extends JpaRepository<Inventario, Long> {
    default Optional<Inventario> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Inventario> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Inventario> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select inventario from Inventario inventario left join fetch inventario.producto left join fetch inventario.tienda",
        countQuery = "select count(inventario) from Inventario inventario"
    )
    Page<Inventario> findAllWithToOneRelationships(Pageable pageable);

    @Query("select inventario from Inventario inventario left join fetch inventario.producto left join fetch inventario.tienda")
    List<Inventario> findAllWithToOneRelationships();

    @Query(
        "select inventario from Inventario inventario left join fetch inventario.producto left join fetch inventario.tienda where inventario.id =:id"
    )
    Optional<Inventario> findOneWithToOneRelationships(@Param("id") Long id);
}
