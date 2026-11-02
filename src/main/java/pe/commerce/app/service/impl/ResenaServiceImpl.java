package pe.commerce.app.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.commerce.app.domain.Resena;
import pe.commerce.app.repository.ResenaRepository;
import pe.commerce.app.service.ResenaService;
import pe.commerce.app.service.dto.ResenaDTO;
import pe.commerce.app.service.mapper.ResenaMapper;

/**
 * Service Implementation for managing {@link pe.commerce.app.domain.Resena}.
 */
@Service
@Transactional
public class ResenaServiceImpl implements ResenaService {

    private static final Logger LOG = LoggerFactory.getLogger(ResenaServiceImpl.class);

    private final ResenaRepository resenaRepository;

    private final ResenaMapper resenaMapper;

    public ResenaServiceImpl(ResenaRepository resenaRepository, ResenaMapper resenaMapper) {
        this.resenaRepository = resenaRepository;
        this.resenaMapper = resenaMapper;
    }

    @Override
    public ResenaDTO save(ResenaDTO resenaDTO) {
        LOG.debug("Request to save Resena : {}", resenaDTO);
        Resena resena = resenaMapper.toEntity(resenaDTO);
        resena = resenaRepository.save(resena);
        return resenaMapper.toDto(resena);
    }

    @Override
    public ResenaDTO update(ResenaDTO resenaDTO) {
        LOG.debug("Request to update Resena : {}", resenaDTO);
        Resena resena = resenaMapper.toEntity(resenaDTO);
        resena = resenaRepository.save(resena);
        return resenaMapper.toDto(resena);
    }

    @Override
    public Optional<ResenaDTO> partialUpdate(ResenaDTO resenaDTO) {
        LOG.debug("Request to partially update Resena : {}", resenaDTO);

        return resenaRepository
            .findById(resenaDTO.getId())
            .map(existingResena -> {
                resenaMapper.partialUpdate(existingResena, resenaDTO);

                return existingResena;
            })
            .map(resenaRepository::save)
            .map(resenaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ResenaDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Resenas");
        return resenaRepository.findAll(pageable).map(resenaMapper::toDto);
    }

    public Page<ResenaDTO> findAllWithEagerRelationships(Pageable pageable) {
        return resenaRepository.findAllWithEagerRelationships(pageable).map(resenaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ResenaDTO> findOne(Long id) {
        LOG.debug("Request to get Resena : {}", id);
        return resenaRepository.findOneWithEagerRelationships(id).map(resenaMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Resena : {}", id);
        resenaRepository.deleteById(id);
    }
}
