package projeto.gestao_vendas.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import projeto.gestao_vendas.model.ItemVenda;
import projeto.gestao_vendas.model.Venda;
import projeto.gestao_vendas.service.VendaService;

@Controller
@RequestMapping("/vendas")
public class VendaController {

    private final VendaService vendaService;

    public VendaController(VendaService vendaService) {
        this.vendaService = vendaService;
    }

    // Carrega a página de vendas
    @GetMapping
    public String exibirTelaVendas(Model model) {
        model.addAttribute("venda", new Venda());
        return "vendas";
    }

    @PostMapping("/finalizar")
    @ResponseBody
    public ResponseEntity<?> finalizarVenda(@RequestBody Venda venda) {

        System.out.println("Recebido no /vendas/finalizar:");
        System.out.println("Cliente ID: " + (venda.getCliente() != null ? venda.getCliente().getId() : "null"));
        for (ItemVenda item : venda.getItens()) {
            System.out.println("Produto ID: " + (item.getProduto() != null ? item.getProduto().getId() : "null"));
            System.out.println("Quantidade: " + item.getQuantidade());
            System.out.println("Preço unitário: " + item.getPrecoUnitario());
        }

        try {
            Venda vendaSalva = vendaService.registrarVenda(venda);

            Map<String, Object> resposta = new HashMap<>();
            resposta.put("mensagem", "Venda realizada com sucesso!");
            resposta.put("vendaId", vendaSalva.getId());
            resposta.put("total", vendaSalva.getTotal());
            resposta.put("clienteId", vendaSalva.getCliente().getId());

            List<Map<String, Object>> itens = new ArrayList<>();
            for (ItemVenda item : vendaSalva.getItens()) {
                Map<String, Object> itemMap = new HashMap<>();
                itemMap.put("produtoId", item.getProduto().getId());
                itemMap.put("quantidade", item.getQuantidade());
                itemMap.put("precoUnitario", item.getPrecoUnitario());
                itens.add(itemMap);
            }
            resposta.put("itens", itens);

            return ResponseEntity.ok(resposta);

        } catch (IllegalArgumentException e) {
            // Retorna erro 400 com a mensagem da exceção
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("erro", e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(500)
                    .body(Map.of("erro", "Erro interno no servidor."));
        }
    }

}
