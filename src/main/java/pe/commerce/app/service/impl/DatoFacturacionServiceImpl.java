package pe.commerce.app.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.commerce.app.domain.DatoFacturacion;
import pe.commerce.app.repository.DatoFacturacionRepository;
import pe.commerce.app.service.DatoFacturacionService;
import pe.commerce.app.service.dto.DatoFacturacionDTO;
import pe.commerce.app.service.mapper.DatoFacturacionMapper;

/**
 * Service Implementation for managing {@link pe.commerce.app.domain.DatoFacturacion}.
 */
@Service
@Transactional
public class DatoFacturacionServiceImpl implements DatoFacturacionService {

    private static final Logger LOG = LoggerFactory.getLogger(DatoFacturacionServiceImpl.class);

    private final DatoFacturacionRepository datoFacturacionRepository;

    private final DatoFacturacionMapper datoFacturacionMapper;

    public DatoFacturacionServiceImpl(DatoFacturacionRepository datoFacturacionRepository, DatoFacturacionMapper datoFacturacionMapper) {
        this.datoFacturacionRepository = datoFacturacionRepository;
        this.datoFacturacionMapper = datoFacturacionMapper;
    }

    @Override
    public DatoFacturacionDTO save(DatoFacturacionDTO datoFacturacionDTO) {
        LOG.debug("Request to save DatoFacturacion : {}", datoFacturacionDTO);
        DatoFacturacion datoFacturacion = datoFacturacionMapper.toEntity(datoFacturacionDTO);
        datoFacturacion = datoFacturacionRepository.save(datoFacturacion);
        return datoFacturacionMapper.toDto(datoFacturacion);
    }

    @Override
    public DatoFacturacionDTO update(DatoFacturacionDTO datoFacturacionDTO) {
        LOG.debug("Request to update DatoFacturacion : {}", datoFacturacionDTO);
        DatoFacturacion datoFacturacion = datoFacturacionMapper.toEntity(datoFacturacionDTO);
        datoFacturacion = datoFacturacionRepository.save(datoFacturacion);
        return datoFacturacionMapper.toDto(datoFacturacion);
    }

    @Override
    public Optional<DatoFacturacionDTO> partialUpdate(DatoFacturacionDTO datoFacturacionDTO) {
        LOG.debug("Request to partially update DatoFacturacion : {}", datoFacturacionDTO);

        return datoFacturacionRepository
            .findById(datoFacturacionDTO.getId())
            .map(existingDatoFacturacion -> {
                datoFacturacionMapper.partialUpdate(existingDatoFacturacion, datoFacturacionDTO);

                return existingDatoFacturacion;
            })
            .map(datoFacturacionRepository::save)
            .map(datoFacturacionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DatoFacturacionDTO> findAll() {
        LOG.debug("Request to get all DatoFacturacions");
        return datoFacturacionRepository
            .findAll()
            .stream()
            .map(datoFacturacionMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    public List<DatoFacturacionDTO> findAllWithEagerRelationships() {
        return datoFacturacionRepository
            .findAllWithEagerRelationships()
            .stream()
            .map(datoFacturacionMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DatoFacturacionDTO> findOne(Long id) {
        LOG.debug("Request to get DatoFacturacion : {}", id);
        return datoFacturacionRepository.findOneWithEagerRelationships(id).map(datoFacturacionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete DatoFacturacion : {}", id);
        datoFacturacionRepository.deleteById(id);
    }
}
