package projeto.gestao_vendas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "cliente")
@Getter
@Setter
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    @NotBlank(message = "O nome é obrigatório.")
    @Size(min = 3, max = 255, message = "O nome deve ter entre 3 e 255 caracteres.")
    private String nome;

    @Column(nullable = false, unique = true, length = 20)
    @NotBlank(message = "O CPF/CNPJ é obrigatório.")
    @Pattern(regexp = "^(\\d{11}|\\d{14})$", message = "O CPF deve ter 11 dígitos e o CNPJ 14 dígitos.")
    private String cpfCnpj;

    @Column(length = 255)
    @NotBlank(message = "O endereço é obrigatório.")
    private String endereco;

    @Column(nullable = false, length = 15)
    @NotBlank(message = "O telefone é obrigatório.")
    @Pattern(regexp = "^(\\d{10,15})$", message = "O telefone deve ter entre 10 e 15 dígitos.")
    private String telefone;

    @Column(nullable = false, length = 100)
    @NotBlank(message = "O e-mail é obrigatório.")
    @Email(message = "O e-mail informado não é válido.")
    private String email;
}
