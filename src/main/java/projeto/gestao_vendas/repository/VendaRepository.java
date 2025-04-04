package projeto.gestao_vendas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import projeto.gestao_vendas.model.Venda;

import java.util.List;

public interface VendaRepository extends JpaRepository<Venda, Long> {

    @Query("SELECT SUM(v.total) FROM Venda v")
    Double somarValorTotal();
    List<Venda> findTop5ByOrderByDataHoraDesc();
}

