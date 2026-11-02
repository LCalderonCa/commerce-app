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
import pe.commerce.app.repository.DatoFacturacionRepository;
import pe.commerce.app.service.DatoFacturacionService;
import pe.commerce.app.service.dto.DatoFacturacionDTO;
import pe.commerce.app.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link pe.commerce.app.domain.DatoFacturacion}.
 */
@RestController
@RequestMapping("/api/dato-facturacions")
public class DatoFacturacionResource {

    private static final Logger LOG = LoggerFactory.getLogger(DatoFacturacionResource.class);

    private static final String ENTITY_NAME = "datoFacturacion";

    @Value("${jhipster.clientApp.name:commerceApp}")
    private String applicationName;

    private final DatoFacturacionService datoFacturacionService;

    private final DatoFacturacionRepository datoFacturacionRepository;

    public DatoFacturacionResource(DatoFacturacionService datoFacturacionService, DatoFacturacionRepository datoFacturacionRepository) {
        this.datoFacturacionService = datoFacturacionService;
        this.datoFacturacionRepository = datoFacturacionRepository;
    }

    /**
     * {@code POST  /dato-facturacions} : Create a new datoFacturacion.
     *
     * @param datoFacturacionDTO the datoFacturacionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new datoFacturacionDTO, or with status {@code 400 (Bad Request)} if the datoFacturacion already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<DatoFacturacionDTO> createDatoFacturacion(@Valid @RequestBody DatoFacturacionDTO datoFacturacionDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save DatoFacturacion : {}", datoFacturacionDTO);
        if (datoFacturacionDTO.getId() != null) {
            throw new BadRequestAlertException("A new datoFacturacion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        datoFacturacionDTO = datoFacturacionService.save(datoFacturacionDTO);
        return ResponseEntity.created(new URI("/api/dato-facturacions/" + datoFacturacionDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, datoFacturacionDTO.getId().toString()))
            .body(datoFacturacionDTO);
    }

    /**
     * {@code PUT  /dato-facturacions/:id} : Updates an existing datoFacturacion.
     *
     * @param id the id of the datoFacturacionDTO to save.
     * @param datoFacturacionDTO the datoFacturacionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated datoFacturacionDTO,
     * or with status {@code 400 (Bad Request)} if the datoFacturacionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the datoFacturacionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DatoFacturacionDTO> updateDatoFacturacion(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DatoFacturacionDTO datoFacturacionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update DatoFacturacion : {}, {}", id, datoFacturacionDTO);
        if (datoFacturacionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, datoFacturacionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!datoFacturacionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        datoFacturacionDTO = datoFacturacionService.update(datoFacturacionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, datoFacturacionDTO.getId().toString()))
            .body(datoFacturacionDTO);
    }

    /**
     * {@code PATCH  /dato-facturacions/:id} : Partial updates given fields of an existing datoFacturacion, field will ignore if it is null
     *
     * @param id the id of the datoFacturacionDTO to save.
     * @param datoFacturacionDTO the datoFacturacionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated datoFacturacionDTO,
     * or with status {@code 400 (Bad Request)} if the datoFacturacionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the datoFacturacionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the datoFacturacionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DatoFacturacionDTO> partialUpdateDatoFacturacion(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DatoFacturacionDTO datoFacturacionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update DatoFacturacion : {}, {}", id, datoFacturacionDTO);
        if (datoFacturacionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, datoFacturacionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!datoFacturacionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DatoFacturacionDTO> result = datoFacturacionService.partialUpdate(datoFacturacionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, datoFacturacionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /dato-facturacions} : get all the Dato Facturacions.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Dato Facturacions in body.
     */
    @GetMapping("")
    public List<DatoFacturacionDTO> getAllDatoFacturacions(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get all DatoFacturacions");
        if (eagerload) {
            return datoFacturacionService.findAllWithEagerRelationships();
        } else {
            return datoFacturacionService.findAll();
        }
    }

    /**
     * {@code GET  /dato-facturacions/:id} : get the "id" datoFacturacion.
     *
     * @param id the id of the datoFacturacionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the datoFacturacionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DatoFacturacionDTO> getDatoFacturacion(@PathVariable("id") Long id) {
        LOG.debug("REST request to get DatoFacturacion : {}", id);
        Optional<DatoFacturacionDTO> datoFacturacionDTO = datoFacturacionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(datoFacturacionDTO);
    }

    /**
     * {@code DELETE  /dato-facturacions/:id} : delete the "id" datoFacturacion.
     *
     * @param id the id of the datoFacturacionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDatoFacturacion(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete DatoFacturacion : {}", id);
        datoFacturacionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
