package pe.commerce.app.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.commerce.app.domain.Persona;
import pe.commerce.app.repository.PersonaRepository;
import pe.commerce.app.service.PersonaService;
import pe.commerce.app.service.dto.PersonaDTO;
import pe.commerce.app.service.mapper.PersonaMapper;

/**
 * Service Implementation for managing {@link pe.commerce.app.domain.Persona}.
 */
@Service
@Transactional
public class PersonaServiceImpl implements PersonaService {

    private static final Logger LOG = LoggerFactory.getLogger(PersonaServiceImpl.class);

    private final PersonaRepository personaRepository;

    private final PersonaMapper personaMapper;

    public PersonaServiceImpl(PersonaRepository personaRepository, PersonaMapper personaMapper) {
        this.personaRepository = personaRepository;
        this.personaMapper = personaMapper;
    }

    @Override
    public PersonaDTO save(PersonaDTO personaDTO) {
        LOG.debug("Request to save Persona : {}", personaDTO);
        Persona persona = personaMapper.toEntity(personaDTO);
        persona = personaRepository.save(persona);
        return personaMapper.toDto(persona);
    }

    @Override
    public PersonaDTO update(PersonaDTO personaDTO) {
        LOG.debug("Request to update Persona : {}", personaDTO);
        Persona persona = personaMapper.toEntity(personaDTO);
        persona = personaRepository.save(persona);
        return personaMapper.toDto(persona);
    }

    @Override
    public Optional<PersonaDTO> partialUpdate(PersonaDTO personaDTO) {
        LOG.debug("Request to partially update Persona : {}", personaDTO);

        return personaRepository
            .findById(personaDTO.getId())
            .map(existingPersona -> {
                personaMapper.partialUpdate(existingPersona, personaDTO);

                return existingPersona;
            })
            .map(personaRepository::save)
            .map(personaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PersonaDTO> findAll() {
        LOG.debug("Request to get all Personas");
        return personaRepository.findAll().stream().map(personaMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get all the personas where Cliente is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<PersonaDTO> findAllWhereClienteIsNull() {
        LOG.debug("Request to get all personas where Cliente is null");
        return StreamSupport.stream(personaRepository.findAll().spliterator(), false)
            .filter(persona -> persona.getCliente() == null)
            .map(personaMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get all the personas where Empleado is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<PersonaDTO> findAllWhereEmpleadoIsNull() {
        LOG.debug("Request to get all personas where Empleado is null");
        return StreamSupport.stream(personaRepository.findAll().spliterator(), false)
            .filter(persona -> persona.getEmpleado() == null)
            .map(personaMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PersonaDTO> findOne(Long id) {
        LOG.debug("Request to get Persona : {}", id);
        return personaRepository.findById(id).map(personaMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Persona : {}", id);
        personaRepository.deleteById(id);
    }
}
