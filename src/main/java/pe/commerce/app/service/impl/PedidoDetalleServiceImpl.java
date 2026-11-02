package pe.commerce.app.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.commerce.app.domain.PedidoDetalle;
import pe.commerce.app.repository.PedidoDetalleRepository;
import pe.commerce.app.service.PedidoDetalleService;
import pe.commerce.app.service.dto.PedidoDetalleDTO;
import pe.commerce.app.service.mapper.PedidoDetalleMapper;

/**
 * Service Implementation for managing {@link pe.commerce.app.domain.PedidoDetalle}.
 */
@Service
@Transactional
public class PedidoDetalleServiceImpl implements PedidoDetalleService {

    private static final Logger LOG = LoggerFactory.getLogger(PedidoDetalleServiceImpl.class);

    private final PedidoDetalleRepository pedidoDetalleRepository;

    private final PedidoDetalleMapper pedidoDetalleMapper;

    public PedidoDetalleServiceImpl(PedidoDetalleRepository pedidoDetalleRepository, PedidoDetalleMapper pedidoDetalleMapper) {
        this.pedidoDetalleRepository = pedidoDetalleRepository;
        this.pedidoDetalleMapper = pedidoDetalleMapper;
    }

    @Override
    public PedidoDetalleDTO save(PedidoDetalleDTO pedidoDetalleDTO) {
        LOG.debug("Request to save PedidoDetalle : {}", pedidoDetalleDTO);
        PedidoDetalle pedidoDetalle = pedidoDetalleMapper.toEntity(pedidoDetalleDTO);
        pedidoDetalle = pedidoDetalleRepository.save(pedidoDetalle);
        return pedidoDetalleMapper.toDto(pedidoDetalle);
    }

    @Override
    public PedidoDetalleDTO update(PedidoDetalleDTO pedidoDetalleDTO) {
        LOG.debug("Request to update PedidoDetalle : {}", pedidoDetalleDTO);
        PedidoDetalle pedidoDetalle = pedidoDetalleMapper.toEntity(pedidoDetalleDTO);
        pedidoDetalle = pedidoDetalleRepository.save(pedidoDetalle);
        return pedidoDetalleMapper.toDto(pedidoDetalle);
    }

    @Override
    public Optional<PedidoDetalleDTO> partialUpdate(PedidoDetalleDTO pedidoDetalleDTO) {
        LOG.debug("Request to partially update PedidoDetalle : {}", pedidoDetalleDTO);

        return pedidoDetalleRepository
            .findById(pedidoDetalleDTO.getId())
            .map(existingPedidoDetalle -> {
                pedidoDetalleMapper.partialUpdate(existingPedidoDetalle, pedidoDetalleDTO);

                return existingPedidoDetalle;
            })
            .map(pedidoDetalleRepository::save)
            .map(pedidoDetalleMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PedidoDetalleDTO> findAll() {
        LOG.debug("Request to get all PedidoDetalles");
        return pedidoDetalleRepository.findAll().stream().map(pedidoDetalleMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    public List<PedidoDetalleDTO> findAllWithEagerRelationships() {
        return pedidoDetalleRepository
            .findAllWithEagerRelationships()
            .stream()
            .map(pedidoDetalleMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PedidoDetalleDTO> findOne(Long id) {
        LOG.debug("Request to get PedidoDetalle : {}", id);
        return pedidoDetalleRepository.findOneWithEagerRelationships(id).map(pedidoDetalleMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete PedidoDetalle : {}", id);
        pedidoDetalleRepository.deleteById(id);
    }
}
