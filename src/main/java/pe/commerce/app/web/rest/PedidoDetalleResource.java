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
import pe.commerce.app.repository.PedidoDetalleRepository;
import pe.commerce.app.service.PedidoDetalleService;
import pe.commerce.app.service.dto.PedidoDetalleDTO;
import pe.commerce.app.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link pe.commerce.app.domain.PedidoDetalle}.
 */
@RestController
@RequestMapping("/api/pedido-detalles")
public class PedidoDetalleResource {

    private static final Logger LOG = LoggerFactory.getLogger(PedidoDetalleResource.class);

    private static final String ENTITY_NAME = "pedidoDetalle";

    @Value("${jhipster.clientApp.name:commerceApp}")
    private String applicationName;

    private final PedidoDetalleService pedidoDetalleService;

    private final PedidoDetalleRepository pedidoDetalleRepository;

    public PedidoDetalleResource(PedidoDetalleService pedidoDetalleService, PedidoDetalleRepository pedidoDetalleRepository) {
        this.pedidoDetalleService = pedidoDetalleService;
        this.pedidoDetalleRepository = pedidoDetalleRepository;
    }

    /**
     * {@code POST  /pedido-detalles} : Create a new pedidoDetalle.
     *
     * @param pedidoDetalleDTO the pedidoDetalleDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pedidoDetalleDTO, or with status {@code 400 (Bad Request)} if the pedidoDetalle already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PedidoDetalleDTO> createPedidoDetalle(@Valid @RequestBody PedidoDetalleDTO pedidoDetalleDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save PedidoDetalle : {}", pedidoDetalleDTO);
        if (pedidoDetalleDTO.getId() != null) {
            throw new BadRequestAlertException("A new pedidoDetalle cannot already have an ID", ENTITY_NAME, "idexists");
        }
        pedidoDetalleDTO = pedidoDetalleService.save(pedidoDetalleDTO);
        return ResponseEntity.created(new URI("/api/pedido-detalles/" + pedidoDetalleDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, pedidoDetalleDTO.getId().toString()))
            .body(pedidoDetalleDTO);
    }

    /**
     * {@code PUT  /pedido-detalles/:id} : Updates an existing pedidoDetalle.
     *
     * @param id the id of the pedidoDetalleDTO to save.
     * @param pedidoDetalleDTO the pedidoDetalleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pedidoDetalleDTO,
     * or with status {@code 400 (Bad Request)} if the pedidoDetalleDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pedidoDetalleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PedidoDetalleDTO> updatePedidoDetalle(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PedidoDetalleDTO pedidoDetalleDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update PedidoDetalle : {}, {}", id, pedidoDetalleDTO);
        if (pedidoDetalleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pedidoDetalleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pedidoDetalleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        pedidoDetalleDTO = pedidoDetalleService.update(pedidoDetalleDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pedidoDetalleDTO.getId().toString()))
            .body(pedidoDetalleDTO);
    }

    /**
     * {@code PATCH  /pedido-detalles/:id} : Partial updates given fields of an existing pedidoDetalle, field will ignore if it is null
     *
     * @param id the id of the pedidoDetalleDTO to save.
     * @param pedidoDetalleDTO the pedidoDetalleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pedidoDetalleDTO,
     * or with status {@code 400 (Bad Request)} if the pedidoDetalleDTO is not valid,
     * or with status {@code 404 (Not Found)} if the pedidoDetalleDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the pedidoDetalleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PedidoDetalleDTO> partialUpdatePedidoDetalle(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PedidoDetalleDTO pedidoDetalleDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update PedidoDetalle : {}, {}", id, pedidoDetalleDTO);
        if (pedidoDetalleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pedidoDetalleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pedidoDetalleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PedidoDetalleDTO> result = pedidoDetalleService.partialUpdate(pedidoDetalleDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pedidoDetalleDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /pedido-detalles} : get all the Pedido Detalles.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Pedido Detalles in body.
     */
    @GetMapping("")
    public List<PedidoDetalleDTO> getAllPedidoDetalles(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get all PedidoDetalles");
        if (eagerload) {
            return pedidoDetalleService.findAllWithEagerRelationships();
        } else {
            return pedidoDetalleService.findAll();
        }
    }

    /**
     * {@code GET  /pedido-detalles/:id} : get the "id" pedidoDetalle.
     *
     * @param id the id of the pedidoDetalleDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pedidoDetalleDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PedidoDetalleDTO> getPedidoDetalle(@PathVariable("id") Long id) {
        LOG.debug("REST request to get PedidoDetalle : {}", id);
        Optional<PedidoDetalleDTO> pedidoDetalleDTO = pedidoDetalleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pedidoDetalleDTO);
    }

    /**
     * {@code DELETE  /pedido-detalles/:id} : delete the "id" pedidoDetalle.
     *
     * @param id the id of the pedidoDetalleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePedidoDetalle(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete PedidoDetalle : {}", id);
        pedidoDetalleService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
