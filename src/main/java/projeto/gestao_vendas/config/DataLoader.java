package projeto.gestao_vendas.config;


import projeto.gestao_vendas.model.Usuario;
import projeto.gestao_vendas.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public void run(String... args) throws Exception {
        if (usuarioRepository.count() == 0) {
            Usuario usuario = new Usuario();
            usuario.setUsername("admin");
            usuario.setPassword("123456");

            usuarioRepository.save(usuario);
            System.out.println("Usuário padrão criado com sucesso!");
        } else {
            System.out.println("Usuário já existente no banco.");
        }
    }
}
