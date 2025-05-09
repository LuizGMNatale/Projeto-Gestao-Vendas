package projeto.gestao_vendas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "venda")
@Getter
@Setter
public class Venda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    @NotNull(message = "O cliente é obrigatório.")
    private Cliente cliente;

    @OneToMany(mappedBy = "venda", cascade = CascadeType.PERSIST, orphanRemoval = true)
    @JsonManagedReference
    private List<ItemVenda> itens = new ArrayList<>();

    @Column(nullable = false)
    private LocalDateTime dataHora;

    @Column(nullable = false)
    private Double total = 0.0;

    public Venda() {
       
    }

    public void addItem(ItemVenda item) {
        item.setVenda(this);
        this.itens.add(item);
        recalcularTotal();
    }

    public void removeItem(ItemVenda item) {
        this.itens.remove(item);
        recalcularTotal();
    }

    public void recalcularTotal() {
        this.total = itens.stream().mapToDouble(ItemVenda::getSubtotal).sum();
    }

    public int getQuantidadeTotal() {
        return itens.stream().mapToInt(ItemVenda::getQuantidade).sum();
    }
    
}
