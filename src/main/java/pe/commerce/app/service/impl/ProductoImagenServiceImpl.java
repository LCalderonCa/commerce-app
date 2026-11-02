package pe.commerce.app.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.commerce.app.domain.ProductoImagen;
import pe.commerce.app.repository.ProductoImagenRepository;
import pe.commerce.app.service.ProductoImagenService;
import pe.commerce.app.service.dto.ProductoImagenDTO;
import pe.commerce.app.service.mapper.ProductoImagenMapper;

/**
 * Service Implementation for managing {@link pe.commerce.app.domain.ProductoImagen}.
 */
@Service
@Transactional
public class ProductoImagenServiceImpl implements ProductoImagenService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductoImagenServiceImpl.class);

    private final ProductoImagenRepository productoImagenRepository;

    private final ProductoImagenMapper productoImagenMapper;

    public ProductoImagenServiceImpl(ProductoImagenRepository productoImagenRepository, ProductoImagenMapper productoImagenMapper) {
        this.productoImagenRepository = productoImagenRepository;
        this.productoImagenMapper = productoImagenMapper;
    }

    @Override
    public ProductoImagenDTO save(ProductoImagenDTO productoImagenDTO) {
        LOG.debug("Request to save ProductoImagen : {}", productoImagenDTO);
        ProductoImagen productoImagen = productoImagenMapper.toEntity(productoImagenDTO);
        productoImagen = productoImagenRepository.save(productoImagen);
        return productoImagenMapper.toDto(productoImagen);
    }

    @Override
    public ProductoImagenDTO update(ProductoImagenDTO productoImagenDTO) {
        LOG.debug("Request to update ProductoImagen : {}", productoImagenDTO);
        ProductoImagen productoImagen = productoImagenMapper.toEntity(productoImagenDTO);
        productoImagen = productoImagenRepository.save(productoImagen);
        return productoImagenMapper.toDto(productoImagen);
    }

    @Override
    public Optional<ProductoImagenDTO> partialUpdate(ProductoImagenDTO productoImagenDTO) {
        LOG.debug("Request to partially update ProductoImagen : {}", productoImagenDTO);

        return productoImagenRepository
            .findById(productoImagenDTO.getId())
            .map(existingProductoImagen -> {
                productoImagenMapper.partialUpdate(existingProductoImagen, productoImagenDTO);

                return existingProductoImagen;
            })
            .map(productoImagenRepository::save)
            .map(productoImagenMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoImagenDTO> findAll() {
        LOG.debug("Request to get all ProductoImagens");
        return productoImagenRepository
            .findAll()
            .stream()
            .map(productoImagenMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    public List<ProductoImagenDTO> findAllWithEagerRelationships() {
        return productoImagenRepository
            .findAllWithEagerRelationships()
            .stream()
            .map(productoImagenMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductoImagenDTO> findOne(Long id) {
        LOG.debug("Request to get ProductoImagen : {}", id);
        return productoImagenRepository.findOneWithEagerRelationships(id).map(productoImagenMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete ProductoImagen : {}", id);
        productoImagenRepository.deleteById(id);
    }
}
