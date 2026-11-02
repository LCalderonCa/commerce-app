package pe.commerce.app.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.commerce.app.domain.Ubigeo;
import pe.commerce.app.repository.UbigeoRepository;
import pe.commerce.app.service.UbigeoService;
import pe.commerce.app.service.dto.UbigeoDTO;
import pe.commerce.app.service.mapper.UbigeoMapper;

/**
 * Service Implementation for managing {@link pe.commerce.app.domain.Ubigeo}.
 */
@Service
@Transactional
public class UbigeoServiceImpl implements UbigeoService {

    private static final Logger LOG = LoggerFactory.getLogger(UbigeoServiceImpl.class);

    private final UbigeoRepository ubigeoRepository;

    private final UbigeoMapper ubigeoMapper;

    public UbigeoServiceImpl(UbigeoRepository ubigeoRepository, UbigeoMapper ubigeoMapper) {
        this.ubigeoRepository = ubigeoRepository;
        this.ubigeoMapper = ubigeoMapper;
    }

    @Override
    public UbigeoDTO save(UbigeoDTO ubigeoDTO) {
        LOG.debug("Request to save Ubigeo : {}", ubigeoDTO);
        Ubigeo ubigeo = ubigeoMapper.toEntity(ubigeoDTO);
        ubigeo = ubigeoRepository.save(ubigeo);
        return ubigeoMapper.toDto(ubigeo);
    }

    @Override
    public UbigeoDTO update(UbigeoDTO ubigeoDTO) {
        LOG.debug("Request to update Ubigeo : {}", ubigeoDTO);
        Ubigeo ubigeo = ubigeoMapper.toEntity(ubigeoDTO);
        ubigeo = ubigeoRepository.save(ubigeo);
        return ubigeoMapper.toDto(ubigeo);
    }

    @Override
    public Optional<UbigeoDTO> partialUpdate(UbigeoDTO ubigeoDTO) {
        LOG.debug("Request to partially update Ubigeo : {}", ubigeoDTO);

        return ubigeoRepository
            .findById(ubigeoDTO.getId())
            .map(existingUbigeo -> {
                ubigeoMapper.partialUpdate(existingUbigeo, ubigeoDTO);

                return existingUbigeo;
            })
            .map(ubigeoRepository::save)
            .map(ubigeoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UbigeoDTO> findAll() {
        LOG.debug("Request to get all Ubigeos");
        return ubigeoRepository.findAll().stream().map(ubigeoMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UbigeoDTO> findOne(Long id) {
        LOG.debug("Request to get Ubigeo : {}", id);
        return ubigeoRepository.findById(id).map(ubigeoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Ubigeo : {}", id);
        ubigeoRepository.deleteById(id);
    }
}
