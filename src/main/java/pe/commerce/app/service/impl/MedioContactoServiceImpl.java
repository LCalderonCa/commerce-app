package pe.commerce.app.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.commerce.app.domain.MedioContacto;
import pe.commerce.app.repository.MedioContactoRepository;
import pe.commerce.app.service.MedioContactoService;
import pe.commerce.app.service.dto.MedioContactoDTO;
import pe.commerce.app.service.mapper.MedioContactoMapper;

/**
 * Service Implementation for managing {@link pe.commerce.app.domain.MedioContacto}.
 */
@Service
@Transactional
public class MedioContactoServiceImpl implements MedioContactoService {

    private static final Logger LOG = LoggerFactory.getLogger(MedioContactoServiceImpl.class);

    private final MedioContactoRepository medioContactoRepository;

    private final MedioContactoMapper medioContactoMapper;

    public MedioContactoServiceImpl(MedioContactoRepository medioContactoRepository, MedioContactoMapper medioContactoMapper) {
        this.medioContactoRepository = medioContactoRepository;
        this.medioContactoMapper = medioContactoMapper;
    }

    @Override
    public MedioContactoDTO save(MedioContactoDTO medioContactoDTO) {
        LOG.debug("Request to save MedioContacto : {}", medioContactoDTO);
        MedioContacto medioContacto = medioContactoMapper.toEntity(medioContactoDTO);
        medioContacto = medioContactoRepository.save(medioContacto);
        return medioContactoMapper.toDto(medioContacto);
    }

    @Override
    public MedioContactoDTO update(MedioContactoDTO medioContactoDTO) {
        LOG.debug("Request to update MedioContacto : {}", medioContactoDTO);
        MedioContacto medioContacto = medioContactoMapper.toEntity(medioContactoDTO);
        medioContacto = medioContactoRepository.save(medioContacto);
        return medioContactoMapper.toDto(medioContacto);
    }

    @Override
    public Optional<MedioContactoDTO> partialUpdate(MedioContactoDTO medioContactoDTO) {
        LOG.debug("Request to partially update MedioContacto : {}", medioContactoDTO);

        return medioContactoRepository
            .findById(medioContactoDTO.getId())
            .map(existingMedioContacto -> {
                medioContactoMapper.partialUpdate(existingMedioContacto, medioContactoDTO);

                return existingMedioContacto;
            })
            .map(medioContactoRepository::save)
            .map(medioContactoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedioContactoDTO> findAll() {
        LOG.debug("Request to get all MedioContactos");
        return medioContactoRepository.findAll().stream().map(medioContactoMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    public List<MedioContactoDTO> findAllWithEagerRelationships() {
        return medioContactoRepository
            .findAllWithEagerRelationships()
            .stream()
            .map(medioContactoMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MedioContactoDTO> findOne(Long id) {
        LOG.debug("Request to get MedioContacto : {}", id);
        return medioContactoRepository.findOneWithEagerRelationships(id).map(medioContactoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete MedioContacto : {}", id);
        medioContactoRepository.deleteById(id);
    }
}
