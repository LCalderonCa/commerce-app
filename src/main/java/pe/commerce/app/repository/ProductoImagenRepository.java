package pe.commerce.app.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.commerce.app.domain.ProductoImagen;

/**
 * Spring Data JPA repository for the ProductoImagen entity.
 */
@Repository
public interface ProductoImagenRepository extends JpaRepository<ProductoImagen, Long> {
    default Optional<ProductoImagen> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ProductoImagen> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ProductoImagen> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select productoImagen from ProductoImagen productoImagen left join fetch productoImagen.producto",
        countQuery = "select count(productoImagen) from ProductoImagen productoImagen"
    )
    Page<ProductoImagen> findAllWithToOneRelationships(Pageable pageable);

    @Query("select productoImagen from ProductoImagen productoImagen left join fetch productoImagen.producto")
    List<ProductoImagen> findAllWithToOneRelationships();

    @Query("select productoImagen from ProductoImagen productoImagen left join fetch productoImagen.producto where productoImagen.id =:id")
    Optional<ProductoImagen> findOneWithToOneRelationships(@Param("id") Long id);
}
