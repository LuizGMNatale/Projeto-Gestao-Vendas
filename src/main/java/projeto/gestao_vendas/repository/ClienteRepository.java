package projeto.gestao_vendas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import projeto.gestao_vendas.model.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    boolean existsByCpfCnpjAndIdNot(String cpfCnpj, Long id);

    @Query("SELECT COUNT(c) FROM Cliente c")
    Long contarClientes();
}
