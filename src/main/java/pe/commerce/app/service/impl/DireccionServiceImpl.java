package pe.commerce.app.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.commerce.app.domain.Direccion;
import pe.commerce.app.repository.DireccionRepository;
import pe.commerce.app.service.DireccionService;
import pe.commerce.app.service.dto.DireccionDTO;
import pe.commerce.app.service.mapper.DireccionMapper;

/**
 * Service Implementation for managing {@link pe.commerce.app.domain.Direccion}.
 */
@Service
@Transactional
public class DireccionServiceImpl implements DireccionService {

    private static final Logger LOG = LoggerFactory.getLogger(DireccionServiceImpl.class);

    private final DireccionRepository direccionRepository;

    private final DireccionMapper direccionMapper;

    public DireccionServiceImpl(DireccionRepository direccionRepository, DireccionMapper direccionMapper) {
        this.direccionRepository = direccionRepository;
        this.direccionMapper = direccionMapper;
    }

    @Override
    public DireccionDTO save(DireccionDTO direccionDTO) {
        LOG.debug("Request to save Direccion : {}", direccionDTO);
        Direccion direccion = direccionMapper.toEntity(direccionDTO);
        direccion = direccionRepository.save(direccion);
        return direccionMapper.toDto(direccion);
    }

    @Override
    public DireccionDTO update(DireccionDTO direccionDTO) {
        LOG.debug("Request to update Direccion : {}", direccionDTO);
        Direccion direccion = direccionMapper.toEntity(direccionDTO);
        direccion = direccionRepository.save(direccion);
        return direccionMapper.toDto(direccion);
    }

    @Override
    public Optional<DireccionDTO> partialUpdate(DireccionDTO direccionDTO) {
        LOG.debug("Request to partially update Direccion : {}", direccionDTO);

        return direccionRepository
            .findById(direccionDTO.getId())
            .map(existingDireccion -> {
                direccionMapper.partialUpdate(existingDireccion, direccionDTO);

                return existingDireccion;
            })
            .map(direccionRepository::save)
            .map(direccionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DireccionDTO> findAll() {
        LOG.debug("Request to get all Direccions");
        return direccionRepository.findAll().stream().map(direccionMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    public List<DireccionDTO> findAllWithEagerRelationships() {
        return direccionRepository
            .findAllWithEagerRelationships()
            .stream()
            .map(direccionMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DireccionDTO> findOne(Long id) {
        LOG.debug("Request to get Direccion : {}", id);
        return direccionRepository.findOneWithEagerRelationships(id).map(direccionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Direccion : {}", id);
        direccionRepository.deleteById(id);
    }
}
