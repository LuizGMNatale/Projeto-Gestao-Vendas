package projeto.gestao_vendas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import projeto.gestao_vendas.model.Cliente;
import projeto.gestao_vendas.service.ClienteService;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping
    public String listarClientes(Model model) {
        List<Cliente> clientes = clienteService.listarTodos();
        model.addAttribute("clientes", clientes);
        return "clientes";
    }

    @GetMapping("/novo")
    public String mostrarFormularioCadastro(Model model,
            @RequestParam(value = "cpfCnpjErro", required = false) String cpfCnpjErro) {
        model.addAttribute("cliente", new Cliente());
        model.addAttribute("cpfCnpjErro", cpfCnpjErro);
        return "clientes-form";
    }

    @PostMapping("/salvar")
    public String salvarCliente(@ModelAttribute Cliente cliente, Model model) {
        try {
            clienteService.salvarOuAtualizar(cliente);
            return "redirect:/clientes";
        } catch (RuntimeException e) {
            model.addAttribute("cliente", cliente);
            model.addAttribute("cpfCnpjErro", e.getMessage());
            return "clientes-form";
        }
    }

    @GetMapping("/editar/{id}")
    public String editarCliente(@PathVariable Long id, Model model,
            @RequestParam(value = "cpfCnpjErro", required = false) String cpfCnpjErro) {
        Optional<Cliente> cliente = clienteService.buscarPorId(id);
        if (cliente.isPresent()) {
            model.addAttribute("cliente", cliente.get());
            model.addAttribute("cpfCnpjErro", cpfCnpjErro);
            return "clientes-form";
        }
        return "redirect:/clientes";
    }

    @PostMapping("/atualizar/{id}")
    public String atualizarCliente(@PathVariable Long id, @ModelAttribute Cliente cliente, Model model) {
        try {
            cliente.setId(id);
            clienteService.salvarOuAtualizar(cliente);
            return "redirect:/clientes";
        } catch (RuntimeException e) {
            model.addAttribute("cliente", cliente);
            model.addAttribute("cpfCnpjErro", e.getMessage());
            return "clientes-form"; 
        }
    }

    @PostMapping("/excluir/{id}")
    public String excluirCliente(@PathVariable Long id) {
        clienteService.excluir(id);
        return "redirect:/clientes";
    }

    @GetMapping("/todos")
    @ResponseBody
    public List<Cliente> listarTodosClientes() {
        return clienteService.listarTodos();
    }

    @GetMapping("/buscarPorNome")
    @ResponseBody
    public List<Cliente> buscarPorNome(@RequestParam String nome) {
        return clienteService.buscarPorNome(nome);
    }

    @GetMapping("/buscarPorCpfCnpj")
    @ResponseBody
    public Cliente buscarPorCpfCnpj(@RequestParam String cpfCnpj) {
        return clienteService.buscarPorCpfCnpj(cpfCnpj).orElse(null);
    }

    @GetMapping("/verificar")
    @ResponseBody
    public ResponseEntity<?> verificarCliente(@RequestParam String nome, @RequestParam String cpfCnpj) {
        boolean existe = clienteService.verificarSeClienteExiste(nome, cpfCnpj);
        return ResponseEntity.ok(existe);
    }

    @GetMapping("/buscar")
    @ResponseBody
    public ResponseEntity<?> buscarCliente(@RequestParam(required = false) String nome,
            @RequestParam(required = false) String cpfCnpj) {
        if (cpfCnpj != null && !cpfCnpj.isEmpty()) {
            Optional<Cliente> cliente = clienteService.buscarPorCpfCnpj(cpfCnpj);
            return cliente.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        } else if (nome != null && !nome.isEmpty()) {
            List<Cliente> clientes = clienteService.buscarPorNome(nome);
            if (!clientes.isEmpty()) {
                return ResponseEntity.ok(clientes);
            }
        }
        return ResponseEntity.notFound().build();
    }

}
