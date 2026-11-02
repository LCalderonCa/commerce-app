package pe.commerce.app.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.commerce.app.domain.CarritoItem;
import pe.commerce.app.repository.CarritoItemRepository;
import pe.commerce.app.service.CarritoItemService;
import pe.commerce.app.service.dto.CarritoItemDTO;
import pe.commerce.app.service.mapper.CarritoItemMapper;

/**
 * Service Implementation for managing {@link pe.commerce.app.domain.CarritoItem}.
 */
@Service
@Transactional
public class CarritoItemServiceImpl implements CarritoItemService {

    private static final Logger LOG = LoggerFactory.getLogger(CarritoItemServiceImpl.class);

    private final CarritoItemRepository carritoItemRepository;

    private final CarritoItemMapper carritoItemMapper;

    public CarritoItemServiceImpl(CarritoItemRepository carritoItemRepository, CarritoItemMapper carritoItemMapper) {
        this.carritoItemRepository = carritoItemRepository;
        this.carritoItemMapper = carritoItemMapper;
    }

    @Override
    public CarritoItemDTO save(CarritoItemDTO carritoItemDTO) {
        LOG.debug("Request to save CarritoItem : {}", carritoItemDTO);
        CarritoItem carritoItem = carritoItemMapper.toEntity(carritoItemDTO);
        carritoItem = carritoItemRepository.save(carritoItem);
        return carritoItemMapper.toDto(carritoItem);
    }

    @Override
    public CarritoItemDTO update(CarritoItemDTO carritoItemDTO) {
        LOG.debug("Request to update CarritoItem : {}", carritoItemDTO);
        CarritoItem carritoItem = carritoItemMapper.toEntity(carritoItemDTO);
        carritoItem = carritoItemRepository.save(carritoItem);
        return carritoItemMapper.toDto(carritoItem);
    }

    @Override
    public Optional<CarritoItemDTO> partialUpdate(CarritoItemDTO carritoItemDTO) {
        LOG.debug("Request to partially update CarritoItem : {}", carritoItemDTO);

        return carritoItemRepository
            .findById(carritoItemDTO.getId())
            .map(existingCarritoItem -> {
                carritoItemMapper.partialUpdate(existingCarritoItem, carritoItemDTO);

                return existingCarritoItem;
            })
            .map(carritoItemRepository::save)
            .map(carritoItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CarritoItemDTO> findAll() {
        LOG.debug("Request to get all CarritoItems");
        return carritoItemRepository.findAll().stream().map(carritoItemMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    public List<CarritoItemDTO> findAllWithEagerRelationships() {
        return carritoItemRepository
            .findAllWithEagerRelationships()
            .stream()
            .map(carritoItemMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get all the carritoItems where PedidoDetalle is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<CarritoItemDTO> findAllWherePedidoDetalleIsNull() {
        LOG.debug("Request to get all carritoItems where PedidoDetalle is null");
        return StreamSupport.stream(carritoItemRepository.findAll().spliterator(), false)
            .filter(carritoItem -> carritoItem.getPedidoDetalle() == null)
            .map(carritoItemMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CarritoItemDTO> findOne(Long id) {
        LOG.debug("Request to get CarritoItem : {}", id);
        return carritoItemRepository.findOneWithEagerRelationships(id).map(carritoItemMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete CarritoItem : {}", id);
        carritoItemRepository.deleteById(id);
    }
}
