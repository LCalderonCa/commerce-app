package pe.commerce.app.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.commerce.app.domain.Pago;

/**
 * Spring Data JPA repository for the Pago entity.
 */
@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {
    default Optional<Pago> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Pago> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Pago> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(value = "select pago from Pago pago left join fetch pago.pedido", countQuery = "select count(pago) from Pago pago")
    Page<Pago> findAllWithToOneRelationships(Pageable pageable);

    @Query("select pago from Pago pago left join fetch pago.pedido")
    List<Pago> findAllWithToOneRelationships();

    @Query("select pago from Pago pago left join fetch pago.pedido where pago.id =:id")
    Optional<Pago> findOneWithToOneRelationships(@Param("id") Long id);
}
