package projeto.gestao_vendas.controller;

import projeto.gestao_vendas.model.Usuario;
import projeto.gestao_vendas.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import java.util.Optional;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping("/login")
    public String login(@ModelAttribute Usuario usuario, HttpSession session, Model model) {
        Optional<Usuario> usuarioEncontrado = usuarioRepository.findByUsername(usuario.getUsername());

        if (usuarioEncontrado.isEmpty() || 
            !usuarioEncontrado.get().getPassword().equals(usuario.getPassword())) {
            model.addAttribute("error", "Usuário ou senha incorretos");
            return "/index"; 
        }

        session.setAttribute("user", usuarioEncontrado.get());
        return "redirect:/dashboard"; 
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/"; 
    }
}
