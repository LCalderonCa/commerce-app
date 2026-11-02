package pe.commerce.app.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import pe.commerce.app.domain.Cupon;

/**
 * Spring Data JPA repository for the Cupon entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CuponRepository extends JpaRepository<Cupon, Long> {}
