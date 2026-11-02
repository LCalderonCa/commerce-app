package pe.commerce.app.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.commerce.app.domain.Pedido;

/**
 * Spring Data JPA repository for the Pedido entity.
 */
@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long>, JpaSpecificationExecutor<Pedido> {
    default Optional<Pedido> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Pedido> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Pedido> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select pedido from Pedido pedido left join fetch pedido.cupon left join fetch pedido.tiendaRecojo left join fetch pedido.atendidoPor left join fetch pedido.cliente",
        countQuery = "select count(pedido) from Pedido pedido"
    )
    Page<Pedido> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select pedido from Pedido pedido left join fetch pedido.cupon left join fetch pedido.tiendaRecojo left join fetch pedido.atendidoPor left join fetch pedido.cliente"
    )
    List<Pedido> findAllWithToOneRelationships();

    @Query(
        "select pedido from Pedido pedido left join fetch pedido.cupon left join fetch pedido.tiendaRecojo left join fetch pedido.atendidoPor left join fetch pedido.cliente where pedido.id =:id"
    )
    Optional<Pedido> findOneWithToOneRelationships(@Param("id") Long id);
}
