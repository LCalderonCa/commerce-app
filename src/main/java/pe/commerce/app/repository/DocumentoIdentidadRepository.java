package pe.commerce.app.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.commerce.app.domain.DocumentoIdentidad;

/**
 * Spring Data JPA repository for the DocumentoIdentidad entity.
 */
@Repository
public interface DocumentoIdentidadRepository extends JpaRepository<DocumentoIdentidad, Long> {
    default Optional<DocumentoIdentidad> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<DocumentoIdentidad> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<DocumentoIdentidad> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select documentoIdentidad from DocumentoIdentidad documentoIdentidad left join fetch documentoIdentidad.persona",
        countQuery = "select count(documentoIdentidad) from DocumentoIdentidad documentoIdentidad"
    )
    Page<DocumentoIdentidad> findAllWithToOneRelationships(Pageable pageable);

    @Query("select documentoIdentidad from DocumentoIdentidad documentoIdentidad left join fetch documentoIdentidad.persona")
    List<DocumentoIdentidad> findAllWithToOneRelationships();

    @Query(
        "select documentoIdentidad from DocumentoIdentidad documentoIdentidad left join fetch documentoIdentidad.persona where documentoIdentidad.id =:id"
    )
    Optional<DocumentoIdentidad> findOneWithToOneRelationships(@Param("id") Long id);
}
