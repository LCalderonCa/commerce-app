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
import pe.commerce.app.repository.UbigeoRepository;
import pe.commerce.app.service.UbigeoService;
import pe.commerce.app.service.dto.UbigeoDTO;
import pe.commerce.app.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link pe.commerce.app.domain.Ubigeo}.
 */
@RestController
@RequestMapping("/api/ubigeos")
public class UbigeoResource {

    private static final Logger LOG = LoggerFactory.getLogger(UbigeoResource.class);

    private static final String ENTITY_NAME = "ubigeo";

    @Value("${jhipster.clientApp.name:commerceApp}")
    private String applicationName;

    private final UbigeoService ubigeoService;

    private final UbigeoRepository ubigeoRepository;

    public UbigeoResource(UbigeoService ubigeoService, UbigeoRepository ubigeoRepository) {
        this.ubigeoService = ubigeoService;
        this.ubigeoRepository = ubigeoRepository;
    }

    /**
     * {@code POST  /ubigeos} : Create a new ubigeo.
     *
     * @param ubigeoDTO the ubigeoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ubigeoDTO, or with status {@code 400 (Bad Request)} if the ubigeo already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<UbigeoDTO> createUbigeo(@Valid @RequestBody UbigeoDTO ubigeoDTO) throws URISyntaxException {
        LOG.debug("REST request to save Ubigeo : {}", ubigeoDTO);
        if (ubigeoDTO.getId() != null) {
            throw new BadRequestAlertException("A new ubigeo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ubigeoDTO = ubigeoService.save(ubigeoDTO);
        return ResponseEntity.created(new URI("/api/ubigeos/" + ubigeoDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, ubigeoDTO.getId().toString()))
            .body(ubigeoDTO);
    }

    /**
     * {@code PUT  /ubigeos/:id} : Updates an existing ubigeo.
     *
     * @param id the id of the ubigeoDTO to save.
     * @param ubigeoDTO the ubigeoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ubigeoDTO,
     * or with status {@code 400 (Bad Request)} if the ubigeoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ubigeoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<UbigeoDTO> updateUbigeo(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UbigeoDTO ubigeoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Ubigeo : {}, {}", id, ubigeoDTO);
        if (ubigeoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ubigeoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ubigeoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ubigeoDTO = ubigeoService.update(ubigeoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ubigeoDTO.getId().toString()))
            .body(ubigeoDTO);
    }

    /**
     * {@code PATCH  /ubigeos/:id} : Partial updates given fields of an existing ubigeo, field will ignore if it is null
     *
     * @param id the id of the ubigeoDTO to save.
     * @param ubigeoDTO the ubigeoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ubigeoDTO,
     * or with status {@code 400 (Bad Request)} if the ubigeoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the ubigeoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the ubigeoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UbigeoDTO> partialUpdateUbigeo(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UbigeoDTO ubigeoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update Ubigeo : {}, {}", id, ubigeoDTO);
        if (ubigeoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ubigeoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ubigeoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UbigeoDTO> result = ubigeoService.partialUpdate(ubigeoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ubigeoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /ubigeos} : get all the Ubigeos.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Ubigeos in body.
     */
    @GetMapping("")
    public List<UbigeoDTO> getAllUbigeos() {
        LOG.debug("REST request to get all Ubigeos");
        return ubigeoService.findAll();
    }

    /**
     * {@code GET  /ubigeos/:id} : get the "id" ubigeo.
     *
     * @param id the id of the ubigeoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ubigeoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UbigeoDTO> getUbigeo(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Ubigeo : {}", id);
        Optional<UbigeoDTO> ubigeoDTO = ubigeoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ubigeoDTO);
    }

    /**
     * {@code DELETE  /ubigeos/:id} : delete the "id" ubigeo.
     *
     * @param id the id of the ubigeoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUbigeo(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Ubigeo : {}", id);
        ubigeoService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
