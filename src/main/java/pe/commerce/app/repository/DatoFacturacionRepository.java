package pe.commerce.app.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.commerce.app.domain.DatoFacturacion;

/**
 * Spring Data JPA repository for the DatoFacturacion entity.
 */
@Repository
public interface DatoFacturacionRepository extends JpaRepository<DatoFacturacion, Long> {
    default Optional<DatoFacturacion> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<DatoFacturacion> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<DatoFacturacion> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select datoFacturacion from DatoFacturacion datoFacturacion left join fetch datoFacturacion.cliente",
        countQuery = "select count(datoFacturacion) from DatoFacturacion datoFacturacion"
    )
    Page<DatoFacturacion> findAllWithToOneRelationships(Pageable pageable);

    @Query("select datoFacturacion from DatoFacturacion datoFacturacion left join fetch datoFacturacion.cliente")
    List<DatoFacturacion> findAllWithToOneRelationships();

    @Query(
        "select datoFacturacion from DatoFacturacion datoFacturacion left join fetch datoFacturacion.cliente where datoFacturacion.id =:id"
    )
    Optional<DatoFacturacion> findOneWithToOneRelationships(@Param("id") Long id);
}
