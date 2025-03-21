package projeto.gestao_vendas.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projeto.gestao_vendas.model.Cliente;
import projeto.gestao_vendas.repository.ClienteRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    public Optional<Cliente> buscarPorId(Long id) {
        return clienteRepository.findById(id);
    }

    public Cliente salvarOuAtualizar(Cliente cliente) {
        if (clienteRepository.existsByCpfCnpjAndIdNot(cliente.getCpfCnpj(), cliente.getId() == null ? 0L : cliente.getId())) {
            throw new RuntimeException("Já existe um cliente com este CPF/CNPJ.");
        }
        return clienteRepository.save(cliente);
    }

    public void excluir(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new IllegalArgumentException("Cliente não encontrado.");
        }
        clienteRepository.deleteById(id);
    }
}
