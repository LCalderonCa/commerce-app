package pe.commerce.app.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.commerce.app.domain.Inventario;
import pe.commerce.app.repository.InventarioRepository;
import pe.commerce.app.service.InventarioService;
import pe.commerce.app.service.dto.InventarioDTO;
import pe.commerce.app.service.mapper.InventarioMapper;

/**
 * Service Implementation for managing {@link pe.commerce.app.domain.Inventario}.
 */
@Service
@Transactional
public class InventarioServiceImpl implements InventarioService {

    private static final Logger LOG = LoggerFactory.getLogger(InventarioServiceImpl.class);

    private final InventarioRepository inventarioRepository;

    private final InventarioMapper inventarioMapper;

    public InventarioServiceImpl(InventarioRepository inventarioRepository, InventarioMapper inventarioMapper) {
        this.inventarioRepository = inventarioRepository;
        this.inventarioMapper = inventarioMapper;
    }

    @Override
    public InventarioDTO save(InventarioDTO inventarioDTO) {
        LOG.debug("Request to save Inventario : {}", inventarioDTO);
        Inventario inventario = inventarioMapper.toEntity(inventarioDTO);
        inventario = inventarioRepository.save(inventario);
        return inventarioMapper.toDto(inventario);
    }

    @Override
    public InventarioDTO update(InventarioDTO inventarioDTO) {
        LOG.debug("Request to update Inventario : {}", inventarioDTO);
        Inventario inventario = inventarioMapper.toEntity(inventarioDTO);
        inventario = inventarioRepository.save(inventario);
        return inventarioMapper.toDto(inventario);
    }

    @Override
    public Optional<InventarioDTO> partialUpdate(InventarioDTO inventarioDTO) {
        LOG.debug("Request to partially update Inventario : {}", inventarioDTO);

        return inventarioRepository
            .findById(inventarioDTO.getId())
            .map(existingInventario -> {
                inventarioMapper.partialUpdate(existingInventario, inventarioDTO);

                return existingInventario;
            })
            .map(inventarioRepository::save)
            .map(inventarioMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventarioDTO> findAll() {
        LOG.debug("Request to get all Inventarios");
        return inventarioRepository.findAll().stream().map(inventarioMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    public List<InventarioDTO> findAllWithEagerRelationships() {
        return inventarioRepository
            .findAllWithEagerRelationships()
            .stream()
            .map(inventarioMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<InventarioDTO> findOne(Long id) {
        LOG.debug("Request to get Inventario : {}", id);
        return inventarioRepository.findOneWithEagerRelationships(id).map(inventarioMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Inventario : {}", id);
        inventarioRepository.deleteById(id);
    }
}
