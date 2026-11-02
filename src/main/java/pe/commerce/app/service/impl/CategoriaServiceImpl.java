package pe.commerce.app.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.commerce.app.domain.Categoria;
import pe.commerce.app.repository.CategoriaRepository;
import pe.commerce.app.service.CategoriaService;
import pe.commerce.app.service.dto.CategoriaDTO;
import pe.commerce.app.service.mapper.CategoriaMapper;

/**
 * Service Implementation for managing {@link pe.commerce.app.domain.Categoria}.
 */
@Service
@Transactional
public class CategoriaServiceImpl implements CategoriaService {

    private static final Logger LOG = LoggerFactory.getLogger(CategoriaServiceImpl.class);

    private final CategoriaRepository categoriaRepository;

    private final CategoriaMapper categoriaMapper;

    public CategoriaServiceImpl(CategoriaRepository categoriaRepository, CategoriaMapper categoriaMapper) {
        this.categoriaRepository = categoriaRepository;
        this.categoriaMapper = categoriaMapper;
    }

    @Override
    public CategoriaDTO save(CategoriaDTO categoriaDTO) {
        LOG.debug("Request to save Categoria : {}", categoriaDTO);
        Categoria categoria = categoriaMapper.toEntity(categoriaDTO);
        categoria = categoriaRepository.save(categoria);
        return categoriaMapper.toDto(categoria);
    }

    @Override
    public CategoriaDTO update(CategoriaDTO categoriaDTO) {
        LOG.debug("Request to update Categoria : {}", categoriaDTO);
        Categoria categoria = categoriaMapper.toEntity(categoriaDTO);
        categoria = categoriaRepository.save(categoria);
        return categoriaMapper.toDto(categoria);
    }

    @Override
    public Optional<CategoriaDTO> partialUpdate(CategoriaDTO categoriaDTO) {
        LOG.debug("Request to partially update Categoria : {}", categoriaDTO);

        return categoriaRepository
            .findById(categoriaDTO.getId())
            .map(existingCategoria -> {
                categoriaMapper.partialUpdate(existingCategoria, categoriaDTO);

                return existingCategoria;
            })
            .map(categoriaRepository::save)
            .map(categoriaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoriaDTO> findAll() {
        LOG.debug("Request to get all Categorias");
        return categoriaRepository.findAll().stream().map(categoriaMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    public List<CategoriaDTO> findAllWithEagerRelationships() {
        return categoriaRepository
            .findAllWithEagerRelationships()
            .stream()
            .map(categoriaMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CategoriaDTO> findOne(Long id) {
        LOG.debug("Request to get Categoria : {}", id);
        return categoriaRepository.findOneWithEagerRelationships(id).map(categoriaMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Categoria : {}", id);
        categoriaRepository.deleteById(id);
    }
}
