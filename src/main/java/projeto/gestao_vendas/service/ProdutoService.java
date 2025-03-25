package projeto.gestao_vendas.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projeto.gestao_vendas.model.Produto;
import projeto.gestao_vendas.repository.ProdutoRepository;
import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    public List<Produto> listarProdutos() {
        return produtoRepository.findAll();
    }

    public Optional<Produto> buscarProdutoPorId(Long id) {
        return produtoRepository.findById(id);
    }

    public Produto salvarProduto(Produto produto) {
        Optional<Produto> produtoExistente = produtoRepository.findByNome(produto.getNome());

        if (produtoExistente.isPresent() && !produtoExistente.get().getId().equals(produto.getId())) {
            throw new IllegalArgumentException("Já existe um produto cadastrado com esse nome.");
        }

        return produtoRepository.save(produto);
    }

    public void deletarProduto(Long id) {
        produtoRepository.deleteById(id);
    }
}
