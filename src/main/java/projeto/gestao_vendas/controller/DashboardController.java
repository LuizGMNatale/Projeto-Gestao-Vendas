package projeto.gestao_vendas.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpSession;

@Controller
public class DashboardController {

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        Object usuario = session.getAttribute("user");

        if (usuario == null) {
            return "redirect:/login"; 
        }

        model.addAttribute("usuario", usuario);
        return "dashboard";
    }
}
