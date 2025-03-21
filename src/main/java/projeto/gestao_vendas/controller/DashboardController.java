package projeto.gestao_vendas.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import projeto.gestao_vendas.repository.ClienteRepository;
import projeto.gestao_vendas.repository.ProdutoRepository;

@Controller
public class DashboardController {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        Object usuario = session.getAttribute("user");

        if (usuario == null) {
            return "redirect:/"; 
        }

        Long quantidadeProdutos = produtoRepository.count();
        Long totalClientes = clienteRepository.contarClientes(); // Obtendo a contagem correta de clientes

        model.addAttribute("quantidadeProdutos", quantidadeProdutos);
        model.addAttribute("totalClientes", totalClientes); // Passando para a view
        model.addAttribute("usuario", usuario);
        
        return "dashboard"; 
    }
}
