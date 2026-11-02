package pe.commerce.app.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.commerce.app.domain.Envio;

/**
 * Spring Data JPA repository for the Envio entity.
 */
@Repository
public interface EnvioRepository extends JpaRepository<Envio, Long> {
    default Optional<Envio> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Envio> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Envio> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select envio from Envio envio left join fetch envio.pedido left join fetch envio.direccion",
        countQuery = "select count(envio) from Envio envio"
    )
    Page<Envio> findAllWithToOneRelationships(Pageable pageable);

    @Query("select envio from Envio envio left join fetch envio.pedido left join fetch envio.direccion")
    List<Envio> findAllWithToOneRelationships();

    @Query("select envio from Envio envio left join fetch envio.pedido left join fetch envio.direccion where envio.id =:id")
    Optional<Envio> findOneWithToOneRelationships(@Param("id") Long id);
}
