package pe.commerce.app.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import pe.commerce.app.domain.Ubigeo;

/**
 * Spring Data JPA repository for the Ubigeo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UbigeoRepository extends JpaRepository<Ubigeo, Long> {}
