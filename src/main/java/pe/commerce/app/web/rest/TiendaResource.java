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
import pe.commerce.app.repository.TiendaRepository;
import pe.commerce.app.service.TiendaService;
import pe.commerce.app.service.dto.TiendaDTO;
import pe.commerce.app.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link pe.commerce.app.domain.Tienda}.
 */
@RestController
@RequestMapping("/api/tiendas")
public class TiendaResource {

    private static final Logger LOG = LoggerFactory.getLogger(TiendaResource.class);

    private static final String ENTITY_NAME = "tienda";

    @Value("${jhipster.clientApp.name:commerceApp}")
    private String applicationName;

    private final TiendaService tiendaService;

    private final TiendaRepository tiendaRepository;

    public TiendaResource(TiendaService tiendaService, TiendaRepository tiendaRepository) {
        this.tiendaService = tiendaService;
        this.tiendaRepository = tiendaRepository;
    }

    /**
     * {@code POST  /tiendas} : Create a new tienda.
     *
     * @param tiendaDTO the tiendaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tiendaDTO, or with status {@code 400 (Bad Request)} if the tienda already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TiendaDTO> createTienda(@Valid @RequestBody TiendaDTO tiendaDTO) throws URISyntaxException {
        LOG.debug("REST request to save Tienda : {}", tiendaDTO);
        if (tiendaDTO.getId() != null) {
            throw new BadRequestAlertException("A new tienda cannot already have an ID", ENTITY_NAME, "idexists");
        }
        tiendaDTO = tiendaService.save(tiendaDTO);
        return ResponseEntity.created(new URI("/api/tiendas/" + tiendaDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, tiendaDTO.getId().toString()))
            .body(tiendaDTO);
    }

    /**
     * {@code PUT  /tiendas/:id} : Updates an existing tienda.
     *
     * @param id the id of the tiendaDTO to save.
     * @param tiendaDTO the tiendaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tiendaDTO,
     * or with status {@code 400 (Bad Request)} if the tiendaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tiendaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TiendaDTO> updateTienda(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TiendaDTO tiendaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Tienda : {}, {}", id, tiendaDTO);
        if (tiendaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tiendaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tiendaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        tiendaDTO = tiendaService.update(tiendaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tiendaDTO.getId().toString()))
            .body(tiendaDTO);
    }

    /**
     * {@code PATCH  /tiendas/:id} : Partial updates given fields of an existing tienda, field will ignore if it is null
     *
     * @param id the id of the tiendaDTO to save.
     * @param tiendaDTO the tiendaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tiendaDTO,
     * or with status {@code 400 (Bad Request)} if the tiendaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the tiendaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the tiendaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TiendaDTO> partialUpdateTienda(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TiendaDTO tiendaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update Tienda : {}, {}", id, tiendaDTO);
        if (tiendaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tiendaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tiendaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TiendaDTO> result = tiendaService.partialUpdate(tiendaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tiendaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /tiendas} : get all the Tiendas.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Tiendas in body.
     */
    @GetMapping("")
    public List<TiendaDTO> getAllTiendas(@RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload) {
        LOG.debug("REST request to get all Tiendas");
        if (eagerload) {
            return tiendaService.findAllWithEagerRelationships();
        } else {
            return tiendaService.findAll();
        }
    }

    /**
     * {@code GET  /tiendas/:id} : get the "id" tienda.
     *
     * @param id the id of the tiendaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tiendaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TiendaDTO> getTienda(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Tienda : {}", id);
        Optional<TiendaDTO> tiendaDTO = tiendaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tiendaDTO);
    }

    /**
     * {@code DELETE  /tiendas/:id} : delete the "id" tienda.
     *
     * @param id the id of the tiendaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTienda(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Tienda : {}", id);
        tiendaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
