package projeto.gestao_vendas.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "item_venda")
@Getter
@Setter
public class ItemVenda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "venda_id", nullable = false)
    @NotNull(message = "A venda é obrigatória.")
    @JsonBackReference
    private Venda venda;

    @ManyToOne
    @JoinColumn(name = "produto_id", nullable = false)
    @NotNull(message = "O produto é obrigatório.")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Produto produto;

    @Column(nullable = false)
    @Min(value = 1, message = "A quantidade deve ser pelo menos 1.")
    private Integer quantidade;

    @Column(nullable = false)
    @NotNull(message = "O preço unitário é obrigatório.")
    private Double precoUnitario;

    public Double getSubtotal() {
        return this.quantidade * this.precoUnitario;
    }
}
