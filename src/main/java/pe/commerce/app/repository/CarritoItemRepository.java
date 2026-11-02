package pe.commerce.app.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.commerce.app.domain.CarritoItem;

/**
 * Spring Data JPA repository for the CarritoItem entity.
 */
@Repository
public interface CarritoItemRepository extends JpaRepository<CarritoItem, Long> {
    default Optional<CarritoItem> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<CarritoItem> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<CarritoItem> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select carritoItem from CarritoItem carritoItem left join fetch carritoItem.producto",
        countQuery = "select count(carritoItem) from CarritoItem carritoItem"
    )
    Page<CarritoItem> findAllWithToOneRelationships(Pageable pageable);

    @Query("select carritoItem from CarritoItem carritoItem left join fetch carritoItem.producto")
    List<CarritoItem> findAllWithToOneRelationships();

    @Query("select carritoItem from CarritoItem carritoItem left join fetch carritoItem.producto where carritoItem.id =:id")
    Optional<CarritoItem> findOneWithToOneRelationships(@Param("id") Long id);
}
