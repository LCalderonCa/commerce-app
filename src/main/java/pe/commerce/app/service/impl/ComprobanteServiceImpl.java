package pe.commerce.app.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.commerce.app.domain.Comprobante;
import pe.commerce.app.repository.ComprobanteRepository;
import pe.commerce.app.service.ComprobanteService;
import pe.commerce.app.service.dto.ComprobanteDTO;
import pe.commerce.app.service.mapper.ComprobanteMapper;

/**
 * Service Implementation for managing {@link pe.commerce.app.domain.Comprobante}.
 */
@Service
@Transactional
public class ComprobanteServiceImpl implements ComprobanteService {

    private static final Logger LOG = LoggerFactory.getLogger(ComprobanteServiceImpl.class);

    private final ComprobanteRepository comprobanteRepository;

    private final ComprobanteMapper comprobanteMapper;

    public ComprobanteServiceImpl(ComprobanteRepository comprobanteRepository, ComprobanteMapper comprobanteMapper) {
        this.comprobanteRepository = comprobanteRepository;
        this.comprobanteMapper = comprobanteMapper;
    }

    @Override
    public ComprobanteDTO save(ComprobanteDTO comprobanteDTO) {
        LOG.debug("Request to save Comprobante : {}", comprobanteDTO);
        Comprobante comprobante = comprobanteMapper.toEntity(comprobanteDTO);
        comprobante = comprobanteRepository.save(comprobante);
        return comprobanteMapper.toDto(comprobante);
    }

    @Override
    public ComprobanteDTO update(ComprobanteDTO comprobanteDTO) {
        LOG.debug("Request to update Comprobante : {}", comprobanteDTO);
        Comprobante comprobante = comprobanteMapper.toEntity(comprobanteDTO);
        comprobante = comprobanteRepository.save(comprobante);
        return comprobanteMapper.toDto(comprobante);
    }

    @Override
    public Optional<ComprobanteDTO> partialUpdate(ComprobanteDTO comprobanteDTO) {
        LOG.debug("Request to partially update Comprobante : {}", comprobanteDTO);

        return comprobanteRepository
            .findById(comprobanteDTO.getId())
            .map(existingComprobante -> {
                comprobanteMapper.partialUpdate(existingComprobante, comprobanteDTO);

                return existingComprobante;
            })
            .map(comprobanteRepository::save)
            .map(comprobanteMapper::toDto);
    }

    public Page<ComprobanteDTO> findAllWithEagerRelationships(Pageable pageable) {
        return comprobanteRepository.findAllWithEagerRelationships(pageable).map(comprobanteMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ComprobanteDTO> findOne(Long id) {
        LOG.debug("Request to get Comprobante : {}", id);
        return comprobanteRepository.findOneWithEagerRelationships(id).map(comprobanteMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Comprobante : {}", id);
        comprobanteRepository.deleteById(id);
    }
}
