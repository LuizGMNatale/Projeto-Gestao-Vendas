package projeto.gestao_vendas.controller;

import projeto.gestao_vendas.model.Usuario;
import projeto.gestao_vendas.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Usuario usuario) {
        Optional<Usuario> usuarioEncontrado = usuarioRepository.findByUsername(usuario.getUsername());
    
        if (usuarioEncontrado.isEmpty()) {
            return ResponseEntity.status(401).body(Collections.singletonMap("error", "Usuário ou senha incorretos"));
        }
    
        // Comparação correta da senha
        if (!usuarioEncontrado.get().getPassword().equals(usuario.getPassword())) {
            return ResponseEntity.status(401).body(Collections.singletonMap("error", "Usuário ou senha incorretos"));
        }
    
        return ResponseEntity.ok(Collections.singletonMap("message", "Login bem-sucedido!"));
    }
    
}
