package pe.commerce.app.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.commerce.app.domain.Tienda;
import pe.commerce.app.repository.TiendaRepository;
import pe.commerce.app.service.TiendaService;
import pe.commerce.app.service.dto.TiendaDTO;
import pe.commerce.app.service.mapper.TiendaMapper;

/**
 * Service Implementation for managing {@link pe.commerce.app.domain.Tienda}.
 */
@Service
@Transactional
public class TiendaServiceImpl implements TiendaService {

    private static final Logger LOG = LoggerFactory.getLogger(TiendaServiceImpl.class);

    private final TiendaRepository tiendaRepository;

    private final TiendaMapper tiendaMapper;

    public TiendaServiceImpl(TiendaRepository tiendaRepository, TiendaMapper tiendaMapper) {
        this.tiendaRepository = tiendaRepository;
        this.tiendaMapper = tiendaMapper;
    }

    @Override
    public TiendaDTO save(TiendaDTO tiendaDTO) {
        LOG.debug("Request to save Tienda : {}", tiendaDTO);
        Tienda tienda = tiendaMapper.toEntity(tiendaDTO);
        tienda = tiendaRepository.save(tienda);
        return tiendaMapper.toDto(tienda);
    }

    @Override
    public TiendaDTO update(TiendaDTO tiendaDTO) {
        LOG.debug("Request to update Tienda : {}", tiendaDTO);
        Tienda tienda = tiendaMapper.toEntity(tiendaDTO);
        tienda = tiendaRepository.save(tienda);
        return tiendaMapper.toDto(tienda);
    }

    @Override
    public Optional<TiendaDTO> partialUpdate(TiendaDTO tiendaDTO) {
        LOG.debug("Request to partially update Tienda : {}", tiendaDTO);

        return tiendaRepository
            .findById(tiendaDTO.getId())
            .map(existingTienda -> {
                tiendaMapper.partialUpdate(existingTienda, tiendaDTO);

                return existingTienda;
            })
            .map(tiendaRepository::save)
            .map(tiendaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TiendaDTO> findAll() {
        LOG.debug("Request to get all Tiendas");
        return tiendaRepository.findAll().stream().map(tiendaMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    public List<TiendaDTO> findAllWithEagerRelationships() {
        return tiendaRepository
            .findAllWithEagerRelationships()
            .stream()
            .map(tiendaMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TiendaDTO> findOne(Long id) {
        LOG.debug("Request to get Tienda : {}", id);
        return tiendaRepository.findOneWithEagerRelationships(id).map(tiendaMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Tienda : {}", id);
        tiendaRepository.deleteById(id);
    }
}
