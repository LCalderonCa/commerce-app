package pe.commerce.app.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.commerce.app.repository.CuponRepository;
import pe.commerce.app.service.CuponService;
import pe.commerce.app.service.dto.CuponDTO;
import pe.commerce.app.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link pe.commerce.app.domain.Cupon}.
 */
@RestController
@RequestMapping("/api/cupons")
public class CuponResource {

    private static final Logger LOG = LoggerFactory.getLogger(CuponResource.class);

    private static final String ENTITY_NAME = "cupon";

    @Value("${jhipster.clientApp.name:commerceApp}")
    private String applicationName;

    private final CuponService cuponService;

    private final CuponRepository cuponRepository;

    public CuponResource(CuponService cuponService, CuponRepository cuponRepository) {
        this.cuponService = cuponService;
        this.cuponRepository = cuponRepository;
    }

    /**
     * {@code POST  /cupons} : Create a new cupon.
     *
     * @param cuponDTO the cuponDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cuponDTO, or with status {@code 400 (Bad Request)} if the cupon already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CuponDTO> createCupon(@Valid @RequestBody CuponDTO cuponDTO) throws URISyntaxException {
        LOG.debug("REST request to save Cupon : {}", cuponDTO);
        if (cuponDTO.getId() != null) {
            throw new BadRequestAlertException("A new cupon cannot already have an ID", ENTITY_NAME, "idexists");
        }
        cuponDTO = cuponService.save(cuponDTO);
        return ResponseEntity.created(new URI("/api/cupons/" + cuponDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, cuponDTO.getId().toString()))
            .body(cuponDTO);
    }

    /**
     * {@code PUT  /cupons/:id} : Updates an existing cupon.
     *
     * @param id the id of the cuponDTO to save.
     * @param cuponDTO the cuponDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cuponDTO,
     * or with status {@code 400 (Bad Request)} if the cuponDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cuponDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CuponDTO> updateCupon(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CuponDTO cuponDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Cupon : {}, {}", id, cuponDTO);
        if (cuponDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cuponDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cuponRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        cuponDTO = cuponService.update(cuponDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cuponDTO.getId().toString()))
            .body(cuponDTO);
    }

    /**
     * {@code PATCH  /cupons/:id} : Partial updates given fields of an existing cupon, field will ignore if it is null
     *
     * @param id the id of the cuponDTO to save.
     * @param cuponDTO the cuponDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cuponDTO,
     * or with status {@code 400 (Bad Request)} if the cuponDTO is not valid,
     * or with status {@code 404 (Not Found)} if the cuponDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the cuponDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CuponDTO> partialUpdateCupon(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CuponDTO cuponDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update Cupon : {}, {}", id, cuponDTO);
        if (cuponDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cuponDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cuponRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CuponDTO> result = cuponService.partialUpdate(cuponDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cuponDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /cupons} : get all the Cupons.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Cupons in body.
     */
    @GetMapping("")
    public List<CuponDTO> getAllCupons() {
        LOG.debug("REST request to get all Cupons");
        return cuponService.findAll();
    }

    /**
     * {@code GET  /cupons/:id} : get the "id" cupon.
     *
     * @param id the id of the cuponDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cuponDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CuponDTO> getCupon(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Cupon : {}", id);
        Optional<CuponDTO> cuponDTO = cuponService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cuponDTO);
    }

    /**
     * {@code DELETE  /cupons/:id} : delete the "id" cupon.
     *
     * @param id the id of the cuponDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCupon(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Cupon : {}", id);
        cuponService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
