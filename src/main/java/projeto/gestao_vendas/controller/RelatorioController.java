package projeto.gestao_vendas.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import projeto.gestao_vendas.model.Venda;
import projeto.gestao_vendas.service.VendaService;

@Controller
@RequestMapping("/relatorios")
public class RelatorioController {

    @Autowired
    private VendaService vendaService;

    @GetMapping
    public String exibirRelatorio(
            @RequestParam(name = "dataInicio", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam(name = "dataFim", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim,
            @RequestParam(name = "page", defaultValue = "0") int page,
            Model model) {

        Page<Venda> vendasPage = Page.empty();
        Double totalVendido = 0.0;
        boolean datasSelecionadas = false;

        if (dataInicio != null && dataFim != null) {
            if (dataFim.isBefore(dataInicio)) {
                model.addAttribute("erro", "A data final não pode ser anterior à data inicial.");
                return "relatorios";
            }

            datasSelecionadas = true;
            LocalDateTime inicio = dataInicio.atStartOfDay();
            LocalDateTime fim = dataFim.atTime(23, 59, 59);

            Pageable pageable = PageRequest.of(page, 10, Sort.by("dataHora").descending());
            vendasPage = vendaService.buscarPorPeriodo(inicio, fim, pageable);
            totalVendido = vendaService.calcularTotalVendidoPorPeriodo(inicio, fim);

            model.addAttribute("dataInicio", dataInicio);
            model.addAttribute("dataFim", dataFim);
        }

        model.addAttribute("vendas", vendasPage); // Aqui!
        model.addAttribute("paginaAtual", vendasPage.getNumber());
        model.addAttribute("totalPaginas", vendasPage.getTotalPages());
        model.addAttribute("totalVendido", totalVendido);
        model.addAttribute("datasSelecionadas", datasSelecionadas);
        

        return "relatorios";
    }

}
