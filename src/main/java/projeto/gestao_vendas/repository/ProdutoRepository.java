package projeto.gestao_vendas.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import projeto.gestao_vendas.model.Produto;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    @Query("SELECT COUNT(p) FROM Produto p")
    Long contarProdutos();
    Optional<Produto> findByNome(String nome);
    
}

