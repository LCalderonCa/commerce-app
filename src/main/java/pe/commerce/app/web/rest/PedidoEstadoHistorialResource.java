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
import pe.commerce.app.repository.PedidoEstadoHistorialRepository;
import pe.commerce.app.service.PedidoEstadoHistorialService;
import pe.commerce.app.service.dto.PedidoEstadoHistorialDTO;
import pe.commerce.app.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link pe.commerce.app.domain.PedidoEstadoHistorial}.
 */
@RestController
@RequestMapping("/api/pedido-estado-historials")
public class PedidoEstadoHistorialResource {

    private static final Logger LOG = LoggerFactory.getLogger(PedidoEstadoHistorialResource.class);

    private static final String ENTITY_NAME = "pedidoEstadoHistorial";

    @Value("${jhipster.clientApp.name:commerceApp}")
    private String applicationName;

    private final PedidoEstadoHistorialService pedidoEstadoHistorialService;

    private final PedidoEstadoHistorialRepository pedidoEstadoHistorialRepository;

    public PedidoEstadoHistorialResource(
        PedidoEstadoHistorialService pedidoEstadoHistorialService,
        PedidoEstadoHistorialRepository pedidoEstadoHistorialRepository
    ) {
        this.pedidoEstadoHistorialService = pedidoEstadoHistorialService;
        this.pedidoEstadoHistorialRepository = pedidoEstadoHistorialRepository;
    }

    /**
     * {@code POST  /pedido-estado-historials} : Create a new pedidoEstadoHistorial.
     *
     * @param pedidoEstadoHistorialDTO the pedidoEstadoHistorialDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pedidoEstadoHistorialDTO, or with status {@code 400 (Bad Request)} if the pedidoEstadoHistorial already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PedidoEstadoHistorialDTO> createPedidoEstadoHistorial(
        @Valid @RequestBody PedidoEstadoHistorialDTO pedidoEstadoHistorialDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save PedidoEstadoHistorial : {}", pedidoEstadoHistorialDTO);
        if (pedidoEstadoHistorialDTO.getId() != null) {
            throw new BadRequestAlertException("A new pedidoEstadoHistorial cannot already have an ID", ENTITY_NAME, "idexists");
        }
        pedidoEstadoHistorialDTO = pedidoEstadoHistorialService.save(pedidoEstadoHistorialDTO);
        return ResponseEntity.created(new URI("/api/pedido-estado-historials/" + pedidoEstadoHistorialDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, pedidoEstadoHistorialDTO.getId().toString()))
            .body(pedidoEstadoHistorialDTO);
    }

    /**
     * {@code PUT  /pedido-estado-historials/:id} : Updates an existing pedidoEstadoHistorial.
     *
     * @param id the id of the pedidoEstadoHistorialDTO to save.
     * @param pedidoEstadoHistorialDTO the pedidoEstadoHistorialDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pedidoEstadoHistorialDTO,
     * or with status {@code 400 (Bad Request)} if the pedidoEstadoHistorialDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pedidoEstadoHistorialDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PedidoEstadoHistorialDTO> updatePedidoEstadoHistorial(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PedidoEstadoHistorialDTO pedidoEstadoHistorialDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update PedidoEstadoHistorial : {}, {}", id, pedidoEstadoHistorialDTO);
        if (pedidoEstadoHistorialDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pedidoEstadoHistorialDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pedidoEstadoHistorialRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        pedidoEstadoHistorialDTO = pedidoEstadoHistorialService.update(pedidoEstadoHistorialDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pedidoEstadoHistorialDTO.getId().toString()))
            .body(pedidoEstadoHistorialDTO);
    }

    /**
     * {@code PATCH  /pedido-estado-historials/:id} : Partial updates given fields of an existing pedidoEstadoHistorial, field will ignore if it is null
     *
     * @param id the id of the pedidoEstadoHistorialDTO to save.
     * @param pedidoEstadoHistorialDTO the pedidoEstadoHistorialDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pedidoEstadoHistorialDTO,
     * or with status {@code 400 (Bad Request)} if the pedidoEstadoHistorialDTO is not valid,
     * or with status {@code 404 (Not Found)} if the pedidoEstadoHistorialDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the pedidoEstadoHistorialDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PedidoEstadoHistorialDTO> partialUpdatePedidoEstadoHistorial(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PedidoEstadoHistorialDTO pedidoEstadoHistorialDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update PedidoEstadoHistorial : {}, {}", id, pedidoEstadoHistorialDTO);
        if (pedidoEstadoHistorialDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pedidoEstadoHistorialDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pedidoEstadoHistorialRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PedidoEstadoHistorialDTO> result = pedidoEstadoHistorialService.partialUpdate(pedidoEstadoHistorialDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pedidoEstadoHistorialDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /pedido-estado-historials} : get all the Pedido Estado Historials.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Pedido Estado Historials in body.
     */
    @GetMapping("")
    public List<PedidoEstadoHistorialDTO> getAllPedidoEstadoHistorials(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get all PedidoEstadoHistorials");
        if (eagerload) {
            return pedidoEstadoHistorialService.findAllWithEagerRelationships();
        } else {
            return pedidoEstadoHistorialService.findAll();
        }
    }

    /**
     * {@code GET  /pedido-estado-historials/:id} : get the "id" pedidoEstadoHistorial.
     *
     * @param id the id of the pedidoEstadoHistorialDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pedidoEstadoHistorialDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PedidoEstadoHistorialDTO> getPedidoEstadoHistorial(@PathVariable("id") Long id) {
        LOG.debug("REST request to get PedidoEstadoHistorial : {}", id);
        Optional<PedidoEstadoHistorialDTO> pedidoEstadoHistorialDTO = pedidoEstadoHistorialService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pedidoEstadoHistorialDTO);
    }

    /**
     * {@code DELETE  /pedido-estado-historials/:id} : delete the "id" pedidoEstadoHistorial.
     *
     * @param id the id of the pedidoEstadoHistorialDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePedidoEstadoHistorial(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete PedidoEstadoHistorial : {}", id);
        pedidoEstadoHistorialService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
