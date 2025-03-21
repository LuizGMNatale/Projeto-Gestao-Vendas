package projeto.gestao_vendas.controller;

import org.springframework.beans.factory.annotation.Autowired;
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
            return "clientes-form"; // Mantém os dados na tela
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
            return "clientes-form"; // Mantém os dados na tela
        }
    }

    @PostMapping("/excluir/{id}")
    public String excluirCliente(@PathVariable Long id) {
        clienteService.excluir(id);
        return "redirect:/clientes";
    }
}
