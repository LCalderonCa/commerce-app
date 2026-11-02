package pe.commerce.app.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.commerce.app.domain.Cupon;
import pe.commerce.app.repository.CuponRepository;
import pe.commerce.app.service.CuponService;
import pe.commerce.app.service.dto.CuponDTO;
import pe.commerce.app.service.mapper.CuponMapper;

/**
 * Service Implementation for managing {@link pe.commerce.app.domain.Cupon}.
 */
@Service
@Transactional
public class CuponServiceImpl implements CuponService {

    private static final Logger LOG = LoggerFactory.getLogger(CuponServiceImpl.class);

    private final CuponRepository cuponRepository;

    private final CuponMapper cuponMapper;

    public CuponServiceImpl(CuponRepository cuponRepository, CuponMapper cuponMapper) {
        this.cuponRepository = cuponRepository;
        this.cuponMapper = cuponMapper;
    }

    @Override
    public CuponDTO save(CuponDTO cuponDTO) {
        LOG.debug("Request to save Cupon : {}", cuponDTO);
        Cupon cupon = cuponMapper.toEntity(cuponDTO);
        cupon = cuponRepository.save(cupon);
        return cuponMapper.toDto(cupon);
    }

    @Override
    public CuponDTO update(CuponDTO cuponDTO) {
        LOG.debug("Request to update Cupon : {}", cuponDTO);
        Cupon cupon = cuponMapper.toEntity(cuponDTO);
        cupon = cuponRepository.save(cupon);
        return cuponMapper.toDto(cupon);
    }

    @Override
    public Optional<CuponDTO> partialUpdate(CuponDTO cuponDTO) {
        LOG.debug("Request to partially update Cupon : {}", cuponDTO);

        return cuponRepository
            .findById(cuponDTO.getId())
            .map(existingCupon -> {
                cuponMapper.partialUpdate(existingCupon, cuponDTO);

                return existingCupon;
            })
            .map(cuponRepository::save)
            .map(cuponMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CuponDTO> findAll() {
        LOG.debug("Request to get all Cupons");
        return cuponRepository.findAll().stream().map(cuponMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CuponDTO> findOne(Long id) {
        LOG.debug("Request to get Cupon : {}", id);
        return cuponRepository.findById(id).map(cuponMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Cupon : {}", id);
        cuponRepository.deleteById(id);
    }
}
