package pe.commerce.app.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.commerce.app.domain.PedidoDetalle;

/**
 * Spring Data JPA repository for the PedidoDetalle entity.
 */
@Repository
public interface PedidoDetalleRepository extends JpaRepository<PedidoDetalle, Long> {
    default Optional<PedidoDetalle> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<PedidoDetalle> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<PedidoDetalle> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select pedidoDetalle from PedidoDetalle pedidoDetalle left join fetch pedidoDetalle.producto left join fetch pedidoDetalle.pedido",
        countQuery = "select count(pedidoDetalle) from PedidoDetalle pedidoDetalle"
    )
    Page<PedidoDetalle> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select pedidoDetalle from PedidoDetalle pedidoDetalle left join fetch pedidoDetalle.producto left join fetch pedidoDetalle.pedido"
    )
    List<PedidoDetalle> findAllWithToOneRelationships();

    @Query(
        "select pedidoDetalle from PedidoDetalle pedidoDetalle left join fetch pedidoDetalle.producto left join fetch pedidoDetalle.pedido where pedidoDetalle.id =:id"
    )
    Optional<PedidoDetalle> findOneWithToOneRelationships(@Param("id") Long id);
}
