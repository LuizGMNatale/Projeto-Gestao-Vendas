package projeto.gestao_vendas.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import projeto.gestao_vendas.repository.ProdutoRepository;

@Controller
public class DashboardController {

    @Autowired
    private ProdutoRepository produtoRepository;

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        Object usuario = session.getAttribute("user");

        if (usuario == null) {
            return "redirect:/"; 
        }

        Long quantidadeProdutos = produtoRepository.count();
        model.addAttribute("quantidadeProdutos", quantidadeProdutos);
        
        model.addAttribute("usuario", usuario);
        return "dashboard"; 
    }
}
