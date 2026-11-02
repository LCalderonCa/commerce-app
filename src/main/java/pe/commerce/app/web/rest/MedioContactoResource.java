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
import pe.commerce.app.repository.MedioContactoRepository;
import pe.commerce.app.service.MedioContactoService;
import pe.commerce.app.service.dto.MedioContactoDTO;
import pe.commerce.app.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link pe.commerce.app.domain.MedioContacto}.
 */
@RestController
@RequestMapping("/api/medio-contactos")
public class MedioContactoResource {

    private static final Logger LOG = LoggerFactory.getLogger(MedioContactoResource.class);

    private static final String ENTITY_NAME = "medioContacto";

    @Value("${jhipster.clientApp.name:commerceApp}")
    private String applicationName;

    private final MedioContactoService medioContactoService;

    private final MedioContactoRepository medioContactoRepository;

    public MedioContactoResource(MedioContactoService medioContactoService, MedioContactoRepository medioContactoRepository) {
        this.medioContactoService = medioContactoService;
        this.medioContactoRepository = medioContactoRepository;
    }

    /**
     * {@code POST  /medio-contactos} : Create a new medioContacto.
     *
     * @param medioContactoDTO the medioContactoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new medioContactoDTO, or with status {@code 400 (Bad Request)} if the medioContacto already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MedioContactoDTO> createMedioContacto(@Valid @RequestBody MedioContactoDTO medioContactoDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save MedioContacto : {}", medioContactoDTO);
        if (medioContactoDTO.getId() != null) {
            throw new BadRequestAlertException("A new medioContacto cannot already have an ID", ENTITY_NAME, "idexists");
        }
        medioContactoDTO = medioContactoService.save(medioContactoDTO);
        return ResponseEntity.created(new URI("/api/medio-contactos/" + medioContactoDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, medioContactoDTO.getId().toString()))
            .body(medioContactoDTO);
    }

    /**
     * {@code PUT  /medio-contactos/:id} : Updates an existing medioContacto.
     *
     * @param id the id of the medioContactoDTO to save.
     * @param medioContactoDTO the medioContactoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated medioContactoDTO,
     * or with status {@code 400 (Bad Request)} if the medioContactoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the medioContactoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MedioContactoDTO> updateMedioContacto(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MedioContactoDTO medioContactoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update MedioContacto : {}, {}", id, medioContactoDTO);
        if (medioContactoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, medioContactoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!medioContactoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        medioContactoDTO = medioContactoService.update(medioContactoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, medioContactoDTO.getId().toString()))
            .body(medioContactoDTO);
    }

    /**
     * {@code PATCH  /medio-contactos/:id} : Partial updates given fields of an existing medioContacto, field will ignore if it is null
     *
     * @param id the id of the medioContactoDTO to save.
     * @param medioContactoDTO the medioContactoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated medioContactoDTO,
     * or with status {@code 400 (Bad Request)} if the medioContactoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the medioContactoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the medioContactoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MedioContactoDTO> partialUpdateMedioContacto(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MedioContactoDTO medioContactoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update MedioContacto : {}, {}", id, medioContactoDTO);
        if (medioContactoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, medioContactoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!medioContactoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MedioContactoDTO> result = medioContactoService.partialUpdate(medioContactoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, medioContactoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /medio-contactos} : get all the Medio Contactos.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Medio Contactos in body.
     */
    @GetMapping("")
    public List<MedioContactoDTO> getAllMedioContactos(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get all MedioContactos");
        if (eagerload) {
            return medioContactoService.findAllWithEagerRelationships();
        } else {
            return medioContactoService.findAll();
        }
    }

    /**
     * {@code GET  /medio-contactos/:id} : get the "id" medioContacto.
     *
     * @param id the id of the medioContactoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the medioContactoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MedioContactoDTO> getMedioContacto(@PathVariable("id") Long id) {
        LOG.debug("REST request to get MedioContacto : {}", id);
        Optional<MedioContactoDTO> medioContactoDTO = medioContactoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(medioContactoDTO);
    }

    /**
     * {@code DELETE  /medio-contactos/:id} : delete the "id" medioContacto.
     *
     * @param id the id of the medioContactoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedioContacto(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete MedioContacto : {}", id);
        medioContactoService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
