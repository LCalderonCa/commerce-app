package pe.commerce.app.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.commerce.app.domain.Marca;
import pe.commerce.app.repository.MarcaRepository;
import pe.commerce.app.service.MarcaService;
import pe.commerce.app.service.dto.MarcaDTO;
import pe.commerce.app.service.mapper.MarcaMapper;

/**
 * Service Implementation for managing {@link pe.commerce.app.domain.Marca}.
 */
@Service
@Transactional
public class MarcaServiceImpl implements MarcaService {

    private static final Logger LOG = LoggerFactory.getLogger(MarcaServiceImpl.class);

    private final MarcaRepository marcaRepository;

    private final MarcaMapper marcaMapper;

    public MarcaServiceImpl(MarcaRepository marcaRepository, MarcaMapper marcaMapper) {
        this.marcaRepository = marcaRepository;
        this.marcaMapper = marcaMapper;
    }

    @Override
    public MarcaDTO save(MarcaDTO marcaDTO) {
        LOG.debug("Request to save Marca : {}", marcaDTO);
        Marca marca = marcaMapper.toEntity(marcaDTO);
        marca = marcaRepository.save(marca);
        return marcaMapper.toDto(marca);
    }

    @Override
    public MarcaDTO update(MarcaDTO marcaDTO) {
        LOG.debug("Request to update Marca : {}", marcaDTO);
        Marca marca = marcaMapper.toEntity(marcaDTO);
        marca = marcaRepository.save(marca);
        return marcaMapper.toDto(marca);
    }

    @Override
    public Optional<MarcaDTO> partialUpdate(MarcaDTO marcaDTO) {
        LOG.debug("Request to partially update Marca : {}", marcaDTO);

        return marcaRepository
            .findById(marcaDTO.getId())
            .map(existingMarca -> {
                marcaMapper.partialUpdate(existingMarca, marcaDTO);

                return existingMarca;
            })
            .map(marcaRepository::save)
            .map(marcaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MarcaDTO> findAll() {
        LOG.debug("Request to get all Marcas");
        return marcaRepository.findAll().stream().map(marcaMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MarcaDTO> findOne(Long id) {
        LOG.debug("Request to get Marca : {}", id);
        return marcaRepository.findById(id).map(marcaMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Marca : {}", id);
        marcaRepository.deleteById(id);
    }
}
