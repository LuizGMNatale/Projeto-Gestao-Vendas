package projeto.gestao_vendas.controller;

import jakarta.servlet.http.HttpSession;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import projeto.gestao_vendas.model.Venda;
import projeto.gestao_vendas.repository.ClienteRepository;
import projeto.gestao_vendas.repository.ProdutoRepository;
import projeto.gestao_vendas.repository.VendaRepository;

@Controller
public class DashboardController {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private VendaRepository vendaRepository;

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        Object usuario = session.getAttribute("user");

        if (usuario == null) {
            return "redirect:/";
        }

        Long quantidadeProdutos = produtoRepository.count();
        Long totalClientes = clienteRepository.contarClientes();
        Long quantidadeVendas = vendaRepository.count();

        Double valorTotalVendido = vendaRepository.somarValorTotal();

        List<Venda> ultimasVendas = vendaRepository.findTop5ByOrderByDataHoraDesc();

        model.addAttribute("quantidadeProdutos", quantidadeProdutos);
        model.addAttribute("totalClientes", totalClientes);
        model.addAttribute("quantidadeVendas", quantidadeVendas);
        model.addAttribute("valorTotalVendido", valorTotalVendido != null ? valorTotalVendido : 0);
        model.addAttribute("ultimasVendas", ultimasVendas);
        model.addAttribute("usuario", usuario);

        return "dashboard";
    }

}
