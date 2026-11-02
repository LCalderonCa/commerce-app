package pe.commerce.app.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.commerce.app.domain.Carrito;
import pe.commerce.app.repository.CarritoRepository;
import pe.commerce.app.service.CarritoService;
import pe.commerce.app.service.dto.CarritoDTO;
import pe.commerce.app.service.mapper.CarritoMapper;

/**
 * Service Implementation for managing {@link pe.commerce.app.domain.Carrito}.
 */
@Service
@Transactional
public class CarritoServiceImpl implements CarritoService {

    private static final Logger LOG = LoggerFactory.getLogger(CarritoServiceImpl.class);

    private final CarritoRepository carritoRepository;

    private final CarritoMapper carritoMapper;

    public CarritoServiceImpl(CarritoRepository carritoRepository, CarritoMapper carritoMapper) {
        this.carritoRepository = carritoRepository;
        this.carritoMapper = carritoMapper;
    }

    @Override
    public CarritoDTO save(CarritoDTO carritoDTO) {
        LOG.debug("Request to save Carrito : {}", carritoDTO);
        Carrito carrito = carritoMapper.toEntity(carritoDTO);
        carrito = carritoRepository.save(carrito);
        return carritoMapper.toDto(carrito);
    }

    @Override
    public CarritoDTO update(CarritoDTO carritoDTO) {
        LOG.debug("Request to update Carrito : {}", carritoDTO);
        Carrito carrito = carritoMapper.toEntity(carritoDTO);
        carrito = carritoRepository.save(carrito);
        return carritoMapper.toDto(carrito);
    }

    @Override
    public Optional<CarritoDTO> partialUpdate(CarritoDTO carritoDTO) {
        LOG.debug("Request to partially update Carrito : {}", carritoDTO);

        return carritoRepository
            .findById(carritoDTO.getId())
            .map(existingCarrito -> {
                carritoMapper.partialUpdate(existingCarrito, carritoDTO);

                return existingCarrito;
            })
            .map(carritoRepository::save)
            .map(carritoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CarritoDTO> findAll() {
        LOG.debug("Request to get all Carritos");
        return carritoRepository.findAll().stream().map(carritoMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    public List<CarritoDTO> findAllWithEagerRelationships() {
        return carritoRepository
            .findAllWithEagerRelationships()
            .stream()
            .map(carritoMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CarritoDTO> findOne(Long id) {
        LOG.debug("Request to get Carrito : {}", id);
        return carritoRepository.findOneWithEagerRelationships(id).map(carritoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Carrito : {}", id);
        carritoRepository.deleteById(id);
    }
}
