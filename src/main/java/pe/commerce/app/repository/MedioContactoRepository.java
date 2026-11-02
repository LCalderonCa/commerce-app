package pe.commerce.app.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.commerce.app.domain.MedioContacto;

/**
 * Spring Data JPA repository for the MedioContacto entity.
 */
@Repository
public interface MedioContactoRepository extends JpaRepository<MedioContacto, Long> {
    default Optional<MedioContacto> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<MedioContacto> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<MedioContacto> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select medioContacto from MedioContacto medioContacto left join fetch medioContacto.persona",
        countQuery = "select count(medioContacto) from MedioContacto medioContacto"
    )
    Page<MedioContacto> findAllWithToOneRelationships(Pageable pageable);

    @Query("select medioContacto from MedioContacto medioContacto left join fetch medioContacto.persona")
    List<MedioContacto> findAllWithToOneRelationships();

    @Query("select medioContacto from MedioContacto medioContacto left join fetch medioContacto.persona where medioContacto.id =:id")
    Optional<MedioContacto> findOneWithToOneRelationships(@Param("id") Long id);
}
