package br.com.devsuperior.apiclientes.services;

import br.com.devsuperior.apiclientes.dto.ClientesDTO;
import br.com.devsuperior.apiclientes.entities.Clientes;
import br.com.devsuperior.apiclientes.repositories.ClientesRepository;
import br.com.devsuperior.apiclientes.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class ClientesService {

    @Autowired
    private ClientesRepository repository;

    @Transactional
    public Page<ClientesDTO> buscarTodosPaginados(PageRequest pageRequest) {
        Page<Clientes> list = repository.findAll(pageRequest);
        return list.map(x -> new ClientesDTO(x));
    }

    @Transactional
    public ClientesDTO buscarPorId(Long id) {
        Optional<Clientes> obj = repository.findById(id);
        Clientes entity = obj.orElseThrow(() -> new ResourceNotFoundException("Registro não encontrado!"));
        return new ClientesDTO(entity);
    }


    @Transactional
    public ClientesDTO insert(ClientesDTO dto) {
        Clientes entity = new Clientes();
        copyDtoToEntity(dto, entity);
        entity = repository.save(entity);
        return new ClientesDTO(entity);
    }

    @Transactional
    public ClientesDTO update(Long id, ClientesDTO dto) {
        try {
            Clientes entity = repository.getOne(id); // Não precisar ir ao BD duas vezes
            copyDtoToEntity(dto, entity);
            entity = repository.save(entity);
            return new ClientesDTO(entity);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Id não encontrado: " + id);
        }
    }

    public void delete(Long id) {
        try {
            repository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("Id não encontrado: " + id);
        }
    }

    private void copyDtoToEntity(ClientesDTO dto, Clientes entity) {
        entity.setName(dto.getName());
        entity.setCpf(dto.getCpf());
        entity.setIncome(dto.getIncome());
        entity.setBirthDate(dto.getBirthDate());
        entity.setChildren(dto.getChildren());
    }

}
