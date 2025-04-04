package projeto.gestao_vendas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import projeto.gestao_vendas.model.Produto;
import projeto.gestao_vendas.service.ProdutoService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    @GetMapping
    public String listarProdutos(Model model) {
        List<Produto> produtos = produtoService.listarProdutos();
        model.addAttribute("produtos", produtos);
        return "produtos";
    }

    @GetMapping("/novo")
    public String exibirFormularioCadastro(Model model) {
        model.addAttribute("produto", new Produto());
        return "produto-form";
    }

    @PostMapping("/salvar")
    public String salvarProduto(@Valid @ModelAttribute Produto produto, BindingResult result,
            RedirectAttributes attributes) {
        try {
            produtoService.salvarProduto(produto);
            attributes.addFlashAttribute("mensagem", "Produto salvo com sucesso!");
            return "redirect:/produtos";
        } catch (IllegalArgumentException e) {
            result.rejectValue("nome", "error.produto", e.getMessage());
        }
        return "produto-form";
    }

    @GetMapping("/editar/{id}")
    public String exibirFormularioEdicao(@PathVariable Long id, Model model) {
        Optional<Produto> produto = produtoService.buscarProdutoPorId(id);
        if (produto.isPresent()) {
            model.addAttribute("produto", produto.get());
            return "produto-form";
        }
        return "redirect:/produtos";
    }

    @PostMapping("/excluir/{id}")
    public String excluirProduto(@PathVariable Long id) {
        produtoService.deletarProduto(id);
        return "redirect:/produtos";
    }

    @GetMapping("/nome/{nome}")
    public ResponseEntity<Produto> buscarPorNome(@PathVariable String nome) {
        Optional<Produto> produtoOpt = produtoService.buscarPorNome(nome);
        return produtoOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/todos")
    public ResponseEntity<List<String>> listarTodosOsProdutos() {
        List<String> nomesProdutos = produtoService.listarProdutos().stream()
                .map(Produto::getNome)
                .toList();
        return ResponseEntity.ok(nomesProdutos);
    }

    @PostMapping("/verificar-estoque")
    @ResponseBody
    public ResponseEntity<Map<Long, Integer>> verificarEstoque(@RequestBody List<Long> produtoIds) {
        Map<Long, Integer> estoqueMap = produtoService.verificarEstoque(produtoIds);
        return ResponseEntity.ok(estoqueMap);
    }

}
