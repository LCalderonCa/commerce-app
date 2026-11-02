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
import pe.commerce.app.repository.PagoRepository;
import pe.commerce.app.service.PagoService;
import pe.commerce.app.service.dto.PagoDTO;
import pe.commerce.app.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link pe.commerce.app.domain.Pago}.
 */
@RestController
@RequestMapping("/api/pagos")
public class PagoResource {

    private static final Logger LOG = LoggerFactory.getLogger(PagoResource.class);

    private static final String ENTITY_NAME = "pago";

    @Value("${jhipster.clientApp.name:commerceApp}")
    private String applicationName;

    private final PagoService pagoService;

    private final PagoRepository pagoRepository;

    public PagoResource(PagoService pagoService, PagoRepository pagoRepository) {
        this.pagoService = pagoService;
        this.pagoRepository = pagoRepository;
    }

    /**
     * {@code POST  /pagos} : Create a new pago.
     *
     * @param pagoDTO the pagoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pagoDTO, or with status {@code 400 (Bad Request)} if the pago already has an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PagoDTO> createPago(@Valid @RequestBody PagoDTO pagoDTO) throws URISyntaxException {
        LOG.debug("REST request to save Pago : {}", pagoDTO);
        if (pagoDTO.getId() != null) {
            throw new BadRequestAlertException("A new pago cannot already have an ID", ENTITY_NAME, "idexists");
        }
        pagoDTO = pagoService.save(pagoDTO);
        return ResponseEntity.created(new URI("/api/pagos/" + pagoDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, pagoDTO.getId().toString()))
            .body(pagoDTO);
    }

    /**
     * {@code PUT  /pagos/:id} : Updates an existing pago.
     *
     * @param id the id of the pagoDTO to save.
     * @param pagoDTO the pagoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pagoDTO,
     * or with status {@code 400 (Bad Request)} if the pagoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pagoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PagoDTO> updatePago(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PagoDTO pagoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Pago : {}, {}", id, pagoDTO);
        if (pagoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pagoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pagoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        pagoDTO = pagoService.update(pagoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pagoDTO.getId().toString()))
            .body(pagoDTO);
    }

    /**
     * {@code PATCH  /pagos/:id} : Partial updates given fields of an existing pago, field will ignore if it is null
     *
     * @param id the id of the pagoDTO to save.
     * @param pagoDTO the pagoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pagoDTO,
     * or with status {@code 400 (Bad Request)} if the pagoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the pagoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the pagoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PagoDTO> partialUpdatePago(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PagoDTO pagoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partially update Pago : {}, {}", id, pagoDTO);
        if (pagoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pagoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pagoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PagoDTO> result = pagoService.partialUpdate(pagoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pagoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /pagos} : get all the Pagos.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Pagos in body.
     */
    @GetMapping("")
    public List<PagoDTO> getAllPagos(@RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload) {
        LOG.debug("REST request to get all Pagos");
        if (eagerload) {
            return pagoService.findAllWithEagerRelationships();
        } else {
            return pagoService.findAll();
        }
    }

    /**
     * {@code GET  /pagos/:id} : get the "id" pago.
     *
     * @param id the id of the pagoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pagoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PagoDTO> getPago(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Pago : {}", id);
        Optional<PagoDTO> pagoDTO = pagoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pagoDTO);
    }

    /**
     * {@code DELETE  /pagos/:id} : delete the "id" pago.
     *
     * @param id the id of the pagoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePago(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Pago : {}", id);
        pagoService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
