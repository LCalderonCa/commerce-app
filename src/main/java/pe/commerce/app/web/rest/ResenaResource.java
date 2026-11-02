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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pe.commerce.app.repository.ResenaRepository;
import pe.commerce.app.service.ResenaService;
import pe.commerce.app.service.dto.ResenaDTO;
import pe.commerce.app.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link pe.commerce.app.domain.Resena}.
 */
@RestController
@RequestMapping("/api/resenas")
public class ResenaResource {

    private static final Logger LOG = LoggerFactory.getLogger(ResenaResource.class);

    private static final String ENTITY_NAME = "resena";

    @Value("${jhipster.clientApp.name:commerceApp}")
    private String applicationName;

    private final ResenaService resenaService;

    private final ResenaRepository resenaRepository;

    public ResenaResource(ResenaService resenaService, ResenaRepository resenaRepository) {
        this.resenaService = resenaService;
        this.resenaRepository = resenaRepository;
    }

    /**
     * {@code POST  /resenas} : Create a new resena.
     *
     * @param resenaDTO the resenaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new resenaDTO, or with status {@code 400 (Bad Request)} if the resena already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ResenaDTO> createResena(@Valid @RequestBody ResenaDTO resenaDTO) throws URISyntaxException {
        LOG.debug("REST request to save Resena : {}", resenaDTO);
        if (resenaDTO.getId() != null) {
            throw new BadRequestAlertException("A new resena cannot already have an ID", ENTITY_NAME, "idexists");
        }
        resenaDTO = resenaService.save(resenaDTO);
        return ResponseEntity.created(new URI("/api/resenas/" + resenaDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, resenaDTO.getId().toString()))
            .body(resenaDTO);
    }

    /**
     * {@code PUT  /resenas/:id} : Updates an existing resena.
     *
     * @param id the id of the resenaDTO to save.
     * @param resenaDTO the resenaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated resenaDTO,
     * or with status {@code 400 (Bad Request)} if the resenaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the resenaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ResenaDTO> updateResena(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ResenaDTO resenaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Resena : {}, {}", id, resenaDTO);
        if (resenaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, resenaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!resenaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        resenaDTO = resenaService.update(resenaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, resenaDTO.getId().toString()))
            .body(resenaDTO);
    }

    /**
     * {@code PATCH  /resenas/:id} : Partial updates given fields of an existing resena, field will ignore if it is null
     *
     * @param id the id of the resenaDTO to save.
     * @param resenaDTO the resenaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated resenaDTO,
     * or with status {@code 400 (Bad Request)} if the resenaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the resenaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the resenaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ResenaDTO> partialUpdateResena(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ResenaDTO resenaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update Resena : {}, {}", id, resenaDTO);
        if (resenaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, resenaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!resenaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ResenaDTO> result = resenaService.partialUpdate(resenaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, resenaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /resenas} : get all the Resenas.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Resenas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ResenaDTO>> getAllResenas(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of Resenas");
        Page<ResenaDTO> page;
        if (eagerload) {
            page = resenaService.findAllWithEagerRelationships(pageable);
        } else {
            page = resenaService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /resenas/:id} : get the "id" resena.
     *
     * @param id the id of the resenaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the resenaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ResenaDTO> getResena(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Resena : {}", id);
        Optional<ResenaDTO> resenaDTO = resenaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(resenaDTO);
    }

    /**
     * {@code DELETE  /resenas/:id} : delete the "id" resena.
     *
     * @param id the id of the resenaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResena(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Resena : {}", id);
        resenaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
