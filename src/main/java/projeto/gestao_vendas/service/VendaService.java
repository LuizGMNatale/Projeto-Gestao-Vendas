package projeto.gestao_vendas.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projeto.gestao_vendas.model.Venda;
import projeto.gestao_vendas.model.ItemVenda;
import projeto.gestao_vendas.model.Produto;
import projeto.gestao_vendas.repository.VendaRepository;
import projeto.gestao_vendas.repository.ItemVendaRepository;
import projeto.gestao_vendas.repository.ProdutoRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class VendaService {

    private final VendaRepository vendaRepository;
    private final ItemVendaRepository itemVendaRepository;
    private final ProdutoRepository produtoRepository;

    public VendaService(VendaRepository vendaRepository, ItemVendaRepository itemVendaRepository,
            ProdutoRepository produtoRepository) {
        this.vendaRepository = vendaRepository;
        this.itemVendaRepository = itemVendaRepository;
        this.produtoRepository = produtoRepository;
    }

    @Transactional
    public Venda registrarVenda(Venda venda) {
        if (venda.getCliente() == null) {
            throw new IllegalArgumentException("A venda deve estar associada a um cliente.");
        }

        if (venda.getItens().isEmpty()) {
            throw new IllegalArgumentException("A venda deve conter pelo menos um produto.");
        }

        System.out.println("Iniciando processamento da venda...");

        double total = 0.0;
        List<ItemVenda> itensProcessados = new ArrayList<>();

        for (ItemVenda item : venda.getItens()) {
            if (item.getProduto() == null || item.getProduto().getId() == null) {
                throw new IllegalArgumentException("Produto não informado corretamente em um dos itens da venda.");
            }

            Produto produto = produtoRepository.findById(item.getProduto().getId())
                    .orElseThrow(
                            () -> new IllegalArgumentException("Produto não encontrado: " + item.getProduto().getId()));

            System.out.println("Produto carregado: ID = " + produto.getId() +
                    " | Nome = " + produto.getNome() +
                    " | Preço = " + produto.getPreco());

            if (produto.getQuantidadeEmEstoque() < item.getQuantidade()) {
                throw new IllegalArgumentException("Estoque insuficiente para o produto: " + produto.getNome());
            }

            produto.setQuantidadeEmEstoque(produto.getQuantidadeEmEstoque() - item.getQuantidade());
            produtoRepository.save(produto);

            item.setProduto(produto);
            item.setVenda(venda);

            total += produto.getPreco() * item.getQuantidade();
            itensProcessados.add(item);
        }

        venda.setTotal(total);
        venda = vendaRepository.save(venda);
        itemVendaRepository.saveAll(itensProcessados);
        venda.setItens(itensProcessados);

        System.out.println("Venda registrada com sucesso! ID: " + venda.getId());
        return venda;
    }

}
