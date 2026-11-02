package pe.commerce.app.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.commerce.app.domain.DocumentoIdentidad;
import pe.commerce.app.repository.DocumentoIdentidadRepository;
import pe.commerce.app.service.DocumentoIdentidadService;
import pe.commerce.app.service.dto.DocumentoIdentidadDTO;
import pe.commerce.app.service.mapper.DocumentoIdentidadMapper;

/**
 * Service Implementation for managing {@link pe.commerce.app.domain.DocumentoIdentidad}.
 */
@Service
@Transactional
public class DocumentoIdentidadServiceImpl implements DocumentoIdentidadService {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentoIdentidadServiceImpl.class);

    private final DocumentoIdentidadRepository documentoIdentidadRepository;

    private final DocumentoIdentidadMapper documentoIdentidadMapper;

    public DocumentoIdentidadServiceImpl(
        DocumentoIdentidadRepository documentoIdentidadRepository,
        DocumentoIdentidadMapper documentoIdentidadMapper
    ) {
        this.documentoIdentidadRepository = documentoIdentidadRepository;
        this.documentoIdentidadMapper = documentoIdentidadMapper;
    }

    @Override
    public DocumentoIdentidadDTO save(DocumentoIdentidadDTO documentoIdentidadDTO) {
        LOG.debug("Request to save DocumentoIdentidad : {}", documentoIdentidadDTO);
        DocumentoIdentidad documentoIdentidad = documentoIdentidadMapper.toEntity(documentoIdentidadDTO);
        documentoIdentidad = documentoIdentidadRepository.save(documentoIdentidad);
        return documentoIdentidadMapper.toDto(documentoIdentidad);
    }

    @Override
    public DocumentoIdentidadDTO update(DocumentoIdentidadDTO documentoIdentidadDTO) {
        LOG.debug("Request to update DocumentoIdentidad : {}", documentoIdentidadDTO);
        DocumentoIdentidad documentoIdentidad = documentoIdentidadMapper.toEntity(documentoIdentidadDTO);
        documentoIdentidad = documentoIdentidadRepository.save(documentoIdentidad);
        return documentoIdentidadMapper.toDto(documentoIdentidad);
    }

    @Override
    public Optional<DocumentoIdentidadDTO> partialUpdate(DocumentoIdentidadDTO documentoIdentidadDTO) {
        LOG.debug("Request to partially update DocumentoIdentidad : {}", documentoIdentidadDTO);

        return documentoIdentidadRepository
            .findById(documentoIdentidadDTO.getId())
            .map(existingDocumentoIdentidad -> {
                documentoIdentidadMapper.partialUpdate(existingDocumentoIdentidad, documentoIdentidadDTO);

                return existingDocumentoIdentidad;
            })
            .map(documentoIdentidadRepository::save)
            .map(documentoIdentidadMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DocumentoIdentidadDTO> findAll() {
        LOG.debug("Request to get all DocumentoIdentidads");
        return documentoIdentidadRepository
            .findAll()
            .stream()
            .map(documentoIdentidadMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    public List<DocumentoIdentidadDTO> findAllWithEagerRelationships() {
        return documentoIdentidadRepository
            .findAllWithEagerRelationships()
            .stream()
            .map(documentoIdentidadMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DocumentoIdentidadDTO> findOne(Long id) {
        LOG.debug("Request to get DocumentoIdentidad : {}", id);
        return documentoIdentidadRepository.findOneWithEagerRelationships(id).map(documentoIdentidadMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete DocumentoIdentidad : {}", id);
        documentoIdentidadRepository.deleteById(id);
    }
}
