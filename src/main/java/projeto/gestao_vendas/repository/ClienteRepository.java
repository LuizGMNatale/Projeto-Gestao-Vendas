package projeto.gestao_vendas.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import projeto.gestao_vendas.model.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    boolean existsByCpfCnpjAndIdNot(String cpfCnpj, Long id);

    @Query("SELECT COUNT(c) FROM Cliente c")
    Long contarClientes();

    List<Cliente> findByNomeContainingIgnoreCase(String nome);

    Optional<Cliente> findByCpfCnpj(String cpfCnpj);

    boolean existsByNomeAndCpfCnpj(String nome, String cpfCnpj);

}
