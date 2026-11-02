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
import pe.commerce.app.repository.DocumentoIdentidadRepository;
import pe.commerce.app.service.DocumentoIdentidadService;
import pe.commerce.app.service.dto.DocumentoIdentidadDTO;
import pe.commerce.app.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link pe.commerce.app.domain.DocumentoIdentidad}.
 */
@RestController
@RequestMapping("/api/documento-identidads")
public class DocumentoIdentidadResource {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentoIdentidadResource.class);

    private static final String ENTITY_NAME = "documentoIdentidad";

    @Value("${jhipster.clientApp.name:commerceApp}")
    private String applicationName;

    private final DocumentoIdentidadService documentoIdentidadService;

    private final DocumentoIdentidadRepository documentoIdentidadRepository;

    public DocumentoIdentidadResource(
        DocumentoIdentidadService documentoIdentidadService,
        DocumentoIdentidadRepository documentoIdentidadRepository
    ) {
        this.documentoIdentidadService = documentoIdentidadService;
        this.documentoIdentidadRepository = documentoIdentidadRepository;
    }

    /**
     * {@code POST  /documento-identidads} : Create a new documentoIdentidad.
     *
     * @param documentoIdentidadDTO the documentoIdentidadDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new documentoIdentidadDTO, or with status {@code 400 (Bad Request)} if the documentoIdentidad already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<DocumentoIdentidadDTO> createDocumentoIdentidad(@Valid @RequestBody DocumentoIdentidadDTO documentoIdentidadDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save DocumentoIdentidad : {}", documentoIdentidadDTO);
        if (documentoIdentidadDTO.getId() != null) {
            throw new BadRequestAlertException("A new documentoIdentidad cannot already have an ID", ENTITY_NAME, "idexists");
        }
        documentoIdentidadDTO = documentoIdentidadService.save(documentoIdentidadDTO);
        return ResponseEntity.created(new URI("/api/documento-identidads/" + documentoIdentidadDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, documentoIdentidadDTO.getId().toString()))
            .body(documentoIdentidadDTO);
    }

    /**
     * {@code PUT  /documento-identidads/:id} : Updates an existing documentoIdentidad.
     *
     * @param id the id of the documentoIdentidadDTO to save.
     * @param documentoIdentidadDTO the documentoIdentidadDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated documentoIdentidadDTO,
     * or with status {@code 400 (Bad Request)} if the documentoIdentidadDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the documentoIdentidadDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DocumentoIdentidadDTO> updateDocumentoIdentidad(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DocumentoIdentidadDTO documentoIdentidadDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update DocumentoIdentidad : {}, {}", id, documentoIdentidadDTO);
        if (documentoIdentidadDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, documentoIdentidadDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!documentoIdentidadRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        documentoIdentidadDTO = documentoIdentidadService.update(documentoIdentidadDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, documentoIdentidadDTO.getId().toString()))
            .body(documentoIdentidadDTO);
    }

    /**
     * {@code PATCH  /documento-identidads/:id} : Partial updates given fields of an existing documentoIdentidad, field will ignore if it is null
     *
     * @param id the id of the documentoIdentidadDTO to save.
     * @param documentoIdentidadDTO the documentoIdentidadDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated documentoIdentidadDTO,
     * or with status {@code 400 (Bad Request)} if the documentoIdentidadDTO is not valid,
     * or with status {@code 404 (Not Found)} if the documentoIdentidadDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the documentoIdentidadDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DocumentoIdentidadDTO> partialUpdateDocumentoIdentidad(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DocumentoIdentidadDTO documentoIdentidadDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update DocumentoIdentidad : {}, {}", id, documentoIdentidadDTO);
        if (documentoIdentidadDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, documentoIdentidadDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!documentoIdentidadRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DocumentoIdentidadDTO> result = documentoIdentidadService.partialUpdate(documentoIdentidadDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, documentoIdentidadDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /documento-identidads} : get all the Documento Identidads.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Documento Identidads in body.
     */
    @GetMapping("")
    public List<DocumentoIdentidadDTO> getAllDocumentoIdentidads(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get all DocumentoIdentidads");
        if (eagerload) {
            return documentoIdentidadService.findAllWithEagerRelationships();
        } else {
            return documentoIdentidadService.findAll();
        }
    }

    /**
     * {@code GET  /documento-identidads/:id} : get the "id" documentoIdentidad.
     *
     * @param id the id of the documentoIdentidadDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the documentoIdentidadDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DocumentoIdentidadDTO> getDocumentoIdentidad(@PathVariable("id") Long id) {
        LOG.debug("REST request to get DocumentoIdentidad : {}", id);
        Optional<DocumentoIdentidadDTO> documentoIdentidadDTO = documentoIdentidadService.findOne(id);
        return ResponseUtil.wrapOrNotFound(documentoIdentidadDTO);
    }

    /**
     * {@code DELETE  /documento-identidads/:id} : delete the "id" documentoIdentidad.
     *
     * @param id the id of the documentoIdentidadDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocumentoIdentidad(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete DocumentoIdentidad : {}", id);
        documentoIdentidadService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
