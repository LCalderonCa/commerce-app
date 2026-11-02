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
import pe.commerce.app.repository.CarritoItemRepository;
import pe.commerce.app.service.CarritoItemService;
import pe.commerce.app.service.dto.CarritoItemDTO;
import pe.commerce.app.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link pe.commerce.app.domain.CarritoItem}.
 */
@RestController
@RequestMapping("/api/carrito-items")
public class CarritoItemResource {

    private static final Logger LOG = LoggerFactory.getLogger(CarritoItemResource.class);

    private static final String ENTITY_NAME = "carritoItem";

    @Value("${jhipster.clientApp.name:commerceApp}")
    private String applicationName;

    private final CarritoItemService carritoItemService;

    private final CarritoItemRepository carritoItemRepository;

    public CarritoItemResource(CarritoItemService carritoItemService, CarritoItemRepository carritoItemRepository) {
        this.carritoItemService = carritoItemService;
        this.carritoItemRepository = carritoItemRepository;
    }

    /**
     * {@code POST  /carrito-items} : Create a new carritoItem.
     *
     * @param carritoItemDTO the carritoItemDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new carritoItemDTO, or with status {@code 400 (Bad Request)} if the carritoItem already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CarritoItemDTO> createCarritoItem(@Valid @RequestBody CarritoItemDTO carritoItemDTO) throws URISyntaxException {
        LOG.debug("REST request to save CarritoItem : {}", carritoItemDTO);
        if (carritoItemDTO.getId() != null) {
            throw new BadRequestAlertException("A new carritoItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        carritoItemDTO = carritoItemService.save(carritoItemDTO);
        return ResponseEntity.created(new URI("/api/carrito-items/" + carritoItemDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, carritoItemDTO.getId().toString()))
            .body(carritoItemDTO);
    }

    /**
     * {@code PUT  /carrito-items/:id} : Updates an existing carritoItem.
     *
     * @param id the id of the carritoItemDTO to save.
     * @param carritoItemDTO the carritoItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated carritoItemDTO,
     * or with status {@code 400 (Bad Request)} if the carritoItemDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the carritoItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CarritoItemDTO> updateCarritoItem(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CarritoItemDTO carritoItemDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update CarritoItem : {}, {}", id, carritoItemDTO);
        if (carritoItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, carritoItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!carritoItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        carritoItemDTO = carritoItemService.update(carritoItemDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, carritoItemDTO.getId().toString()))
            .body(carritoItemDTO);
    }

    /**
     * {@code PATCH  /carrito-items/:id} : Partial updates given fields of an existing carritoItem, field will ignore if it is null
     *
     * @param id the id of the carritoItemDTO to save.
     * @param carritoItemDTO the carritoItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated carritoItemDTO,
     * or with status {@code 400 (Bad Request)} if the carritoItemDTO is not valid,
     * or with status {@code 404 (Not Found)} if the carritoItemDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the carritoItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CarritoItemDTO> partialUpdateCarritoItem(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CarritoItemDTO carritoItemDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update CarritoItem : {}, {}", id, carritoItemDTO);
        if (carritoItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, carritoItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!carritoItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CarritoItemDTO> result = carritoItemService.partialUpdate(carritoItemDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, carritoItemDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /carrito-items} : get all the Carrito Items.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Carrito Items in body.
     */
    @GetMapping("")
    public List<CarritoItemDTO> getAllCarritoItems(
        @RequestParam(name = "filter", required = false) String filter,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        if ("pedidodetalle-is-null".equals(filter)) {
            LOG.debug("REST request to get all CarritoItems where pedidoDetalle is null");
            return carritoItemService.findAllWherePedidoDetalleIsNull();
        }
        LOG.debug("REST request to get all CarritoItems");
        if (eagerload) {
            return carritoItemService.findAllWithEagerRelationships();
        } else {
            return carritoItemService.findAll();
        }
    }

    /**
     * {@code GET  /carrito-items/:id} : get the "id" carritoItem.
     *
     * @param id the id of the carritoItemDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the carritoItemDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CarritoItemDTO> getCarritoItem(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CarritoItem : {}", id);
        Optional<CarritoItemDTO> carritoItemDTO = carritoItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(carritoItemDTO);
    }

    /**
     * {@code DELETE  /carrito-items/:id} : delete the "id" carritoItem.
     *
     * @param id the id of the carritoItemDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCarritoItem(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CarritoItem : {}", id);
        carritoItemService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
