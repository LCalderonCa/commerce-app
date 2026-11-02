package pe.commerce.app.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.commerce.app.domain.Comprobante;

/**
 * Spring Data JPA repository for the Comprobante entity.
 */
@Repository
public interface ComprobanteRepository extends JpaRepository<Comprobante, Long>, JpaSpecificationExecutor<Comprobante> {
    default Optional<Comprobante> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Comprobante> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Comprobante> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select comprobante from Comprobante comprobante left join fetch comprobante.comprobanteReferencia left join fetch comprobante.pedido",
        countQuery = "select count(comprobante) from Comprobante comprobante"
    )
    Page<Comprobante> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select comprobante from Comprobante comprobante left join fetch comprobante.comprobanteReferencia left join fetch comprobante.pedido"
    )
    List<Comprobante> findAllWithToOneRelationships();

    @Query(
        "select comprobante from Comprobante comprobante left join fetch comprobante.comprobanteReferencia left join fetch comprobante.pedido where comprobante.id =:id"
    )
    Optional<Comprobante> findOneWithToOneRelationships(@Param("id") Long id);
}
