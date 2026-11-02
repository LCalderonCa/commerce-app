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
import pe.commerce.app.repository.InventarioRepository;
import pe.commerce.app.service.InventarioService;
import pe.commerce.app.service.dto.InventarioDTO;
import pe.commerce.app.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link pe.commerce.app.domain.Inventario}.
 */
@RestController
@RequestMapping("/api/inventarios")
public class InventarioResource {

    private static final Logger LOG = LoggerFactory.getLogger(InventarioResource.class);

    private static final String ENTITY_NAME = "inventario";

    @Value("${jhipster.clientApp.name:commerceApp}")
    private String applicationName;

    private final InventarioService inventarioService;

    private final InventarioRepository inventarioRepository;

    public InventarioResource(InventarioService inventarioService, InventarioRepository inventarioRepository) {
        this.inventarioService = inventarioService;
        this.inventarioRepository = inventarioRepository;
    }

    /**
     * {@code POST  /inventarios} : Create a new inventario.
     *
     * @param inventarioDTO the inventarioDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new inventarioDTO, or with status {@code 400 (Bad Request)} if the inventario already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<InventarioDTO> createInventario(@Valid @RequestBody InventarioDTO inventarioDTO) throws URISyntaxException {
        LOG.debug("REST request to save Inventario : {}", inventarioDTO);
        if (inventarioDTO.getId() != null) {
            throw new BadRequestAlertException("A new inventario cannot already have an ID", ENTITY_NAME, "idexists");
        }
        inventarioDTO = inventarioService.save(inventarioDTO);
        return ResponseEntity.created(new URI("/api/inventarios/" + inventarioDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, inventarioDTO.getId().toString()))
            .body(inventarioDTO);
    }

    /**
     * {@code PUT  /inventarios/:id} : Updates an existing inventario.
     *
     * @param id the id of the inventarioDTO to save.
     * @param inventarioDTO the inventarioDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated inventarioDTO,
     * or with status {@code 400 (Bad Request)} if the inventarioDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the inventarioDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<InventarioDTO> updateInventario(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody InventarioDTO inventarioDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Inventario : {}, {}", id, inventarioDTO);
        if (inventarioDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, inventarioDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!inventarioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        inventarioDTO = inventarioService.update(inventarioDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, inventarioDTO.getId().toString()))
            .body(inventarioDTO);
    }

    /**
     * {@code PATCH  /inventarios/:id} : Partial updates given fields of an existing inventario, field will ignore if it is null
     *
     * @param id the id of the inventarioDTO to save.
     * @param inventarioDTO the inventarioDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated inventarioDTO,
     * or with status {@code 400 (Bad Request)} if the inventarioDTO is not valid,
     * or with status {@code 404 (Not Found)} if the inventarioDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the inventarioDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<InventarioDTO> partialUpdateInventario(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody InventarioDTO inventarioDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update Inventario : {}, {}", id, inventarioDTO);
        if (inventarioDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, inventarioDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!inventarioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InventarioDTO> result = inventarioService.partialUpdate(inventarioDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, inventarioDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /inventarios} : get all the Inventarios.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Inventarios in body.
     */
    @GetMapping("")
    public List<InventarioDTO> getAllInventarios(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get all Inventarios");
        if (eagerload) {
            return inventarioService.findAllWithEagerRelationships();
        } else {
            return inventarioService.findAll();
        }
    }

    /**
     * {@code GET  /inventarios/:id} : get the "id" inventario.
     *
     * @param id the id of the inventarioDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the inventarioDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<InventarioDTO> getInventario(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Inventario : {}", id);
        Optional<InventarioDTO> inventarioDTO = inventarioService.findOne(id);
        return ResponseUtil.wrapOrNotFound(inventarioDTO);
    }

    /**
     * {@code DELETE  /inventarios/:id} : delete the "id" inventario.
     *
     * @param id the id of the inventarioDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInventario(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Inventario : {}", id);
        inventarioService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
