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
import pe.commerce.app.repository.DireccionRepository;
import pe.commerce.app.service.DireccionService;
import pe.commerce.app.service.dto.DireccionDTO;
import pe.commerce.app.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link pe.commerce.app.domain.Direccion}.
 */
@RestController
@RequestMapping("/api/direccions")
public class DireccionResource {

    private static final Logger LOG = LoggerFactory.getLogger(DireccionResource.class);

    private static final String ENTITY_NAME = "direccion";

    @Value("${jhipster.clientApp.name:commerceApp}")
    private String applicationName;

    private final DireccionService direccionService;

    private final DireccionRepository direccionRepository;

    public DireccionResource(DireccionService direccionService, DireccionRepository direccionRepository) {
        this.direccionService = direccionService;
        this.direccionRepository = direccionRepository;
    }

    /**
     * {@code POST  /direccions} : Create a new direccion.
     *
     * @param direccionDTO the direccionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new direccionDTO, or with status {@code 400 (Bad Request)} if the direccion already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<DireccionDTO> createDireccion(@Valid @RequestBody DireccionDTO direccionDTO) throws URISyntaxException {
        LOG.debug("REST request to save Direccion : {}", direccionDTO);
        if (direccionDTO.getId() != null) {
            throw new BadRequestAlertException("A new direccion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        direccionDTO = direccionService.save(direccionDTO);
        return ResponseEntity.created(new URI("/api/direccions/" + direccionDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, direccionDTO.getId().toString()))
            .body(direccionDTO);
    }

    /**
     * {@code PUT  /direccions/:id} : Updates an existing direccion.
     *
     * @param id the id of the direccionDTO to save.
     * @param direccionDTO the direccionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated direccionDTO,
     * or with status {@code 400 (Bad Request)} if the direccionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the direccionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DireccionDTO> updateDireccion(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DireccionDTO direccionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Direccion : {}, {}", id, direccionDTO);
        if (direccionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, direccionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!direccionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        direccionDTO = direccionService.update(direccionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, direccionDTO.getId().toString()))
            .body(direccionDTO);
    }

    /**
     * {@code PATCH  /direccions/:id} : Partial updates given fields of an existing direccion, field will ignore if it is null
     *
     * @param id the id of the direccionDTO to save.
     * @param direccionDTO the direccionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated direccionDTO,
     * or with status {@code 400 (Bad Request)} if the direccionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the direccionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the direccionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DireccionDTO> partialUpdateDireccion(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DireccionDTO direccionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update Direccion : {}, {}", id, direccionDTO);
        if (direccionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, direccionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!direccionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DireccionDTO> result = direccionService.partialUpdate(direccionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, direccionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /direccions} : get all the Direccions.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Direccions in body.
     */
    @GetMapping("")
    public List<DireccionDTO> getAllDireccions(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get all Direccions");
        if (eagerload) {
            return direccionService.findAllWithEagerRelationships();
        } else {
            return direccionService.findAll();
        }
    }

    /**
     * {@code GET  /direccions/:id} : get the "id" direccion.
     *
     * @param id the id of the direccionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the direccionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DireccionDTO> getDireccion(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Direccion : {}", id);
        Optional<DireccionDTO> direccionDTO = direccionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(direccionDTO);
    }

    /**
     * {@code DELETE  /direccions/:id} : delete the "id" direccion.
     *
     * @param id the id of the direccionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDireccion(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Direccion : {}", id);
        direccionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
