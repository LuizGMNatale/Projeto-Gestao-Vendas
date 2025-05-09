package projeto.gestao_vendas.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projeto.gestao_vendas.model.Cliente;
import projeto.gestao_vendas.repository.ClienteRepository;
import projeto.gestao_vendas.repository.VendaRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private VendaRepository vendaRepository;

    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    public Optional<Cliente> buscarPorId(Long id) {
        return clienteRepository.findById(id);
    }

    public Cliente salvarOuAtualizar(Cliente cliente) {
        if (clienteRepository.existsByCpfCnpjAndIdNot(cliente.getCpfCnpj(),
                cliente.getId() == null ? 0L : cliente.getId())) {
            throw new RuntimeException("Já existe um cliente com este CPF/CNPJ.");
        }
        return clienteRepository.save(cliente);
    }

    public void excluir(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new IllegalArgumentException("Cliente não encontrado.");
        }

        if (vendaRepository.existsByClienteId(id)) {
            throw new IllegalStateException("Este cliente está relacionado a uma venda e não pode ser excluído.");
        }

        clienteRepository.deleteById(id);
    }

    public List<Cliente> buscarPorNome(String nome) {
        return clienteRepository.findByNomeContainingIgnoreCase(nome);
    }

    public Optional<Cliente> buscarPorCpfCnpj(String cpfCnpj) {
        return clienteRepository.findByCpfCnpj(cpfCnpj);
    }

    public boolean verificarSeClienteExiste(String nome, String cpfCnpj) {
        return clienteRepository.existsByNomeAndCpfCnpj(nome, cpfCnpj);
    }

}
