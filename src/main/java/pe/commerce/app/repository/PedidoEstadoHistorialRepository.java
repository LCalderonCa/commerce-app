package pe.commerce.app.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.commerce.app.domain.PedidoEstadoHistorial;

/**
 * Spring Data JPA repository for the PedidoEstadoHistorial entity.
 */
@Repository
public interface PedidoEstadoHistorialRepository extends JpaRepository<PedidoEstadoHistorial, Long> {
    default Optional<PedidoEstadoHistorial> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<PedidoEstadoHistorial> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<PedidoEstadoHistorial> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select pedidoEstadoHistorial from PedidoEstadoHistorial pedidoEstadoHistorial left join fetch pedidoEstadoHistorial.pedido",
        countQuery = "select count(pedidoEstadoHistorial) from PedidoEstadoHistorial pedidoEstadoHistorial"
    )
    Page<PedidoEstadoHistorial> findAllWithToOneRelationships(Pageable pageable);

    @Query("select pedidoEstadoHistorial from PedidoEstadoHistorial pedidoEstadoHistorial left join fetch pedidoEstadoHistorial.pedido")
    List<PedidoEstadoHistorial> findAllWithToOneRelationships();

    @Query(
        "select pedidoEstadoHistorial from PedidoEstadoHistorial pedidoEstadoHistorial left join fetch pedidoEstadoHistorial.pedido where pedidoEstadoHistorial.id =:id"
    )
    Optional<PedidoEstadoHistorial> findOneWithToOneRelationships(@Param("id") Long id);
}
