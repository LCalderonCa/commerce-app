package pe.commerce.app.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.commerce.app.domain.Cliente;
import pe.commerce.app.repository.ClienteRepository;
import pe.commerce.app.service.ClienteService;
import pe.commerce.app.service.dto.ClienteDTO;
import pe.commerce.app.service.mapper.ClienteMapper;

/**
 * Service Implementation for managing {@link pe.commerce.app.domain.Cliente}.
 */
@Service
@Transactional
public class ClienteServiceImpl implements ClienteService {

    private static final Logger LOG = LoggerFactory.getLogger(ClienteServiceImpl.class);

    private final ClienteRepository clienteRepository;

    private final ClienteMapper clienteMapper;

    public ClienteServiceImpl(ClienteRepository clienteRepository, ClienteMapper clienteMapper) {
        this.clienteRepository = clienteRepository;
        this.clienteMapper = clienteMapper;
    }

    @Override
    public ClienteDTO save(ClienteDTO clienteDTO) {
        LOG.debug("Request to save Cliente : {}", clienteDTO);
        Cliente cliente = clienteMapper.toEntity(clienteDTO);
        cliente = clienteRepository.save(cliente);
        return clienteMapper.toDto(cliente);
    }

    @Override
    public ClienteDTO update(ClienteDTO clienteDTO) {
        LOG.debug("Request to update Cliente : {}", clienteDTO);
        Cliente cliente = clienteMapper.toEntity(clienteDTO);
        cliente = clienteRepository.save(cliente);
        return clienteMapper.toDto(cliente);
    }

    @Override
    public Optional<ClienteDTO> partialUpdate(ClienteDTO clienteDTO) {
        LOG.debug("Request to partially update Cliente : {}", clienteDTO);

        return clienteRepository
            .findById(clienteDTO.getId())
            .map(existingCliente -> {
                clienteMapper.partialUpdate(existingCliente, clienteDTO);

                return existingCliente;
            })
            .map(clienteRepository::save)
            .map(clienteMapper::toDto);
    }

    public Page<ClienteDTO> findAllWithEagerRelationships(Pageable pageable) {
        return clienteRepository.findAllWithEagerRelationships(pageable).map(clienteMapper::toDto);
    }

    /**
     *  Get all the clientes where Carrito is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ClienteDTO> findAllWhereCarritoIsNull() {
        LOG.debug("Request to get all clientes where Carrito is null");
        return StreamSupport.stream(clienteRepository.findAll().spliterator(), false)
            .filter(cliente -> cliente.getCarrito() == null)
            .map(clienteMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ClienteDTO> findOne(Long id) {
        LOG.debug("Request to get Cliente : {}", id);
        return clienteRepository.findOneWithEagerRelationships(id).map(clienteMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Cliente : {}", id);
        clienteRepository.deleteById(id);
    }
}
