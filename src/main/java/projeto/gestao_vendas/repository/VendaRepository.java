package projeto.gestao_vendas.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import projeto.gestao_vendas.model.Venda;

import java.time.LocalDateTime;
import java.util.List;

public interface VendaRepository extends JpaRepository<Venda, Long> {

    @Query("SELECT SUM(v.total) FROM Venda v")
    Double somarValorTotal();

    List<Venda> findTop5ByOrderByDataHoraDesc();

    @Query("SELECT SUM(v.total) FROM Venda v WHERE v.dataHora BETWEEN :inicio AND :fim")
    Double somarTotalPorPeriodo(@Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);

    Page<Venda> findByDataHoraBetween(LocalDateTime inicio, LocalDateTime fim, Pageable pageable);
}
