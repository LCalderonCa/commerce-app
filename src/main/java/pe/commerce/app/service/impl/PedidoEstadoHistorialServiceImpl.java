package pe.commerce.app.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.commerce.app.domain.PedidoEstadoHistorial;
import pe.commerce.app.repository.PedidoEstadoHistorialRepository;
import pe.commerce.app.service.PedidoEstadoHistorialService;
import pe.commerce.app.service.dto.PedidoEstadoHistorialDTO;
import pe.commerce.app.service.mapper.PedidoEstadoHistorialMapper;

/**
 * Service Implementation for managing {@link pe.commerce.app.domain.PedidoEstadoHistorial}.
 */
@Service
@Transactional
public class PedidoEstadoHistorialServiceImpl implements PedidoEstadoHistorialService {

    private static final Logger LOG = LoggerFactory.getLogger(PedidoEstadoHistorialServiceImpl.class);

    private final PedidoEstadoHistorialRepository pedidoEstadoHistorialRepository;

    private final PedidoEstadoHistorialMapper pedidoEstadoHistorialMapper;

    public PedidoEstadoHistorialServiceImpl(
        PedidoEstadoHistorialRepository pedidoEstadoHistorialRepository,
        PedidoEstadoHistorialMapper pedidoEstadoHistorialMapper
    ) {
        this.pedidoEstadoHistorialRepository = pedidoEstadoHistorialRepository;
        this.pedidoEstadoHistorialMapper = pedidoEstadoHistorialMapper;
    }

    @Override
    public PedidoEstadoHistorialDTO save(PedidoEstadoHistorialDTO pedidoEstadoHistorialDTO) {
        LOG.debug("Request to save PedidoEstadoHistorial : {}", pedidoEstadoHistorialDTO);
        PedidoEstadoHistorial pedidoEstadoHistorial = pedidoEstadoHistorialMapper.toEntity(pedidoEstadoHistorialDTO);
        pedidoEstadoHistorial = pedidoEstadoHistorialRepository.save(pedidoEstadoHistorial);
        return pedidoEstadoHistorialMapper.toDto(pedidoEstadoHistorial);
    }

    @Override
    public PedidoEstadoHistorialDTO update(PedidoEstadoHistorialDTO pedidoEstadoHistorialDTO) {
        LOG.debug("Request to update PedidoEstadoHistorial : {}", pedidoEstadoHistorialDTO);
        PedidoEstadoHistorial pedidoEstadoHistorial = pedidoEstadoHistorialMapper.toEntity(pedidoEstadoHistorialDTO);
        pedidoEstadoHistorial = pedidoEstadoHistorialRepository.save(pedidoEstadoHistorial);
        return pedidoEstadoHistorialMapper.toDto(pedidoEstadoHistorial);
    }

    @Override
    public Optional<PedidoEstadoHistorialDTO> partialUpdate(PedidoEstadoHistorialDTO pedidoEstadoHistorialDTO) {
        LOG.debug("Request to partially update PedidoEstadoHistorial : {}", pedidoEstadoHistorialDTO);

        return pedidoEstadoHistorialRepository
            .findById(pedidoEstadoHistorialDTO.getId())
            .map(existingPedidoEstadoHistorial -> {
                pedidoEstadoHistorialMapper.partialUpdate(existingPedidoEstadoHistorial, pedidoEstadoHistorialDTO);

                return existingPedidoEstadoHistorial;
            })
            .map(pedidoEstadoHistorialRepository::save)
            .map(pedidoEstadoHistorialMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PedidoEstadoHistorialDTO> findAll() {
        LOG.debug("Request to get all PedidoEstadoHistorials");
        return pedidoEstadoHistorialRepository
            .findAll()
            .stream()
            .map(pedidoEstadoHistorialMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    public List<PedidoEstadoHistorialDTO> findAllWithEagerRelationships() {
        return pedidoEstadoHistorialRepository
            .findAllWithEagerRelationships()
            .stream()
            .map(pedidoEstadoHistorialMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PedidoEstadoHistorialDTO> findOne(Long id) {
        LOG.debug("Request to get PedidoEstadoHistorial : {}", id);
        return pedidoEstadoHistorialRepository.findOneWithEagerRelationships(id).map(pedidoEstadoHistorialMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete PedidoEstadoHistorial : {}", id);
        pedidoEstadoHistorialRepository.deleteById(id);
    }
}
