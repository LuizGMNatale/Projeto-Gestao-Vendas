document.addEventListener("DOMContentLoaded", function () {
    const inputProduto = document.getElementById("produto");
    const listaSugestoes = document.getElementById("listaSugestoes");
    const btnAdicionar = document.getElementById("addProduto");
    const listaProdutos = document.getElementById("listaProdutos");
    const totalVenda = document.getElementById("totalVenda");

    let produtosSelecionados = [];
    let todosProdutos = [];

    // Buscar lista de produtos para o dropdown
    fetch("/produtos/todos")
        .then(response => response.json())
        .then(produtos => {
            todosProdutos = produtos;
        })
        .catch(error => console.error("Erro ao carregar produtos:", error));

    // Exibir dropdown ao clicar no input
    inputProduto.addEventListener("focus", function () {
        atualizarDropdown("");
    });

    // Evento para filtrar sugestões conforme digitação
    inputProduto.addEventListener("input", function () {
        atualizarDropdown(inputProduto.value.toLowerCase());
    });

    function atualizarDropdown(termo) {
        listaSugestoes.innerHTML = "";

        const sugestoesFiltradas = todosProdutos.filter(produto =>
            produto.toLowerCase().includes(termo)
        );

        if (sugestoesFiltradas.length === 0) {
            listaSugestoes.classList.remove("show");
            return;
        }

        sugestoesFiltradas.forEach(produto => {
            const item = document.createElement("a");
            item.classList.add("dropdown-item");
            item.textContent = produto;
            item.href = "#";
            item.addEventListener("click", function (event) {
                event.preventDefault();
                inputProduto.value = produto;
                listaSugestoes.classList.remove("show");
            });
            listaSugestoes.appendChild(item);
        });

        listaSugestoes.classList.add("show");
    }

    // Fechar dropdown ao clicar fora
    document.addEventListener("click", function (event) {
        if (!inputProduto.contains(event.target) && !listaSugestoes.contains(event.target)) {
            listaSugestoes.classList.remove("show");
        }
    });

    btnAdicionar.addEventListener("click", function () {
        const nomeProduto = inputProduto.value.trim();
        if (nomeProduto === "") {
            alert("Digite o nome do produto!");
            return;
        }

        buscarProduto(nomeProduto);
        inputProduto.value = "";
    });

    function buscarProduto(nomeProduto) {
        fetch(`/produtos/nome/${nomeProduto}`)
            .then(response => {
                if (!response.ok) {
                    throw new Error("Produto não encontrado!");
                }
                return response.json();
            })
            .then(produto => {
                adicionarProdutoNaTabela(produto);
            })
            .catch(error => {
                alert(error.message); // Exibe um alerta quando o produto não for encontrado
            });
    }

    function adicionarProdutoNaTabela(produto) {
        let produtoExistente = produtosSelecionados.find(p => p.id === produto.id);

        if (produtoExistente) {
            produtoExistente.quantidade += 1;
        } else {
            produto.quantidade = 1;
            produtosSelecionados.push(produto);
        }

        atualizarTabela();
    }

    function atualizarTabela() {
        listaProdutos.innerHTML = "";
        let total = 0;

        produtosSelecionados.forEach(produto => {
            total += produto.quantidade * produto.preco;

            const row = document.createElement("tr");
            row.innerHTML = `
                <td>${produto.nome}</td>
                <td>
                    <input type="number" min="1" value="${produto.quantidade}" 
                           class="form-control quantidade-produto" data-id="${produto.id}">
                </td>
                <td>R$ ${produto.preco.toFixed(2)}</td>
                <td>
                    <button class="btn btn-danger btn-sm remover-produto" data-id="${produto.id}">Remover</button>
                </td>
            `;

            listaProdutos.appendChild(row);
        });

        totalVenda.textContent = total.toFixed(2);
        configurarEventosTabela();
    }


    function configurarEventosTabela() {
        const idsProdutos = produtosSelecionados.map(p => p.id);

        fetch("/produtos/verificar-estoque", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(idsProdutos)
        })
            .then(response => response.json())
            .then(estoqueMap => {
                document.querySelectorAll(".quantidade-produto").forEach(input => {
                    input.addEventListener("change", function () {
                        const id = this.dataset.id;
                        const produto = produtosSelecionados.find(p => p.id == id);
                        const estoqueDisponivel = estoqueMap[id];

                        let novaQuantidade = parseInt(this.value) || 1;

                        if (novaQuantidade > estoqueDisponivel) {
                            alert(`Estoque insuficiente! Disponível: ${estoqueDisponivel}`);
                            novaQuantidade = estoqueDisponivel;
                        }

                        produto.quantidade = Math.max(1, novaQuantidade);
                        this.value = produto.quantidade;
                        atualizarTabela();
                    });
                });

                document.querySelectorAll(".remover-produto").forEach(btn => {
                    btn.addEventListener("click", function () {
                        produtosSelecionados = produtosSelecionados.filter(p => p.id != this.dataset.id);
                        atualizarTabela();
                    });
                });
            })
            .catch(error => {
                console.error("Erro ao verificar estoque:", error);
            });
    }


    // --- Lógica para busca de clientes ---
    const inputCliente = document.getElementById("clienteNome");
    const inputCpfCnpj = document.getElementById("clienteCpfCnpj");
    const listaSugestoesClientes = document.getElementById("listaSugestoesClientes");
    const listaSugestoesCpf = document.getElementById("listaSugestoesCpf");
    let todosClientes = [];

    // Buscar lista de clientes
    fetch("/clientes/todos")
        .then(response => response.json())
        .then(clientes => {
            todosClientes = clientes;
        })
        .catch(error => console.error("Erro ao carregar clientes:", error));

    // Exibir dropdown ao clicar no input do nome
    inputCliente.addEventListener("focus", function () {
        atualizarDropdownClientes("");
    });

    // Exibir dropdown ao clicar no input do CPF/CNPJ
    inputCpfCnpj.addEventListener("focus", function () {
        atualizarDropdownCpf("");
    });

    // Fechar dropdowns ao clicar fora
    document.addEventListener("click", function (event) {
        if (!inputCliente.contains(event.target) && !listaSugestoesClientes.contains(event.target)) {
            listaSugestoesClientes.classList.remove("show");
        }
        if (!inputCpfCnpj.contains(event.target) && !listaSugestoesCpf.contains(event.target)) {
            listaSugestoesCpf.classList.remove("show");
        }
    });

    // Evento para filtrar sugestões conforme digitação do nome
    inputCliente.addEventListener("input", function () {
        atualizarDropdownClientes(inputCliente.value.toLowerCase());
    });

    // Evento para filtrar sugestões conforme digitação do CPF/CNPJ
    inputCpfCnpj.addEventListener("input", function () {
        atualizarDropdownCpf(inputCpfCnpj.value.replace(/\D/g, '')); 
    });

    function atualizarDropdownClientes(termo) {
        listaSugestoesClientes.innerHTML = "";

        const sugestoesFiltradas = todosClientes.filter(cliente =>
            cliente.nome.toLowerCase().includes(termo)
        );

        if (sugestoesFiltradas.length === 0) {
            listaSugestoesClientes.classList.remove("show");
            return;
        }

        sugestoesFiltradas.forEach(cliente => {
            const item = document.createElement("a");
            item.classList.add("dropdown-item");
            item.textContent = cliente.nome;
            item.href = "#";
            item.addEventListener("click", function (event) {
                event.preventDefault();
                inputCliente.value = cliente.nome;
                inputCpfCnpj.value = cliente.cpfCnpj; 
                listaSugestoesClientes.classList.remove("show");
            });
            listaSugestoesClientes.appendChild(item);
        });

        listaSugestoesClientes.classList.add("show");
    }

    function atualizarDropdownCpf(termo) {
        listaSugestoesCpf.innerHTML = "";

        const sugestoesFiltradas = todosClientes.filter(cliente =>
            cliente.cpfCnpj.replace(/\D/g, '').includes(termo)
        );

        if (sugestoesFiltradas.length === 0) {
            listaSugestoesCpf.classList.remove("show");
            return;
        }

        sugestoesFiltradas.forEach(cliente => {
            const item = document.createElement("a");
            item.classList.add("dropdown-item");
            item.textContent = cliente.cpfCnpj;
            item.href = "#";
            item.addEventListener("click", function (event) {
                event.preventDefault();
                inputCpfCnpj.value = cliente.cpfCnpj;
                inputCliente.value = cliente.nome; 

                listaSugestoesCpf.classList.remove("show");

                validarCpfCnpj();
            });
            listaSugestoesCpf.appendChild(item);
        });

        listaSugestoesCpf.classList.add("show");
    }


    function validarCpfCnpj() {
        let campo = $("#clienteCpfCnpj");
        let valor = campo.val().replace(/\D/g, '');
        let mensagemErro = $("#cpfCnpjErro");

        if (valor.length === 0) {
            campo.css("border", "");
            if (mensagemErro.length > 0) {
                mensagemErro.remove();
            }
            return true;
        }

        if (valor.length === 11) {
            campo.mask("000.000.000-00");
        } else if (valor.length === 14) {
            campo.mask("00.000.000/0000-00");
        } else {
            if (mensagemErro.length === 0) {
                campo.after('<div id="cpfCnpjErro" class="text-danger mt-1">CPF/CNPJ inválido!</div>');
            } else {
                mensagemErro.text("CPF/CNPJ inválido!").css("color", "red");
            }
            campo.css("border", "2px solid red");
            return false;
        }

        if (mensagemErro.length > 0 && mensagemErro.text() === "CPF/CNPJ inválido!") {
            mensagemErro.remove();
        }
        campo.css("border", "");
        return true;
    }

    $(document).ready(function () {
        // Aplica a validação ao perder o foco do campo
        $("#clienteCpfCnpj").on("focusout", function () {
            validarCpfCnpj();
        });

        // Remove máscara ao focar no campo
        $("#clienteCpfCnpj").on("focusin", function () {
            $(this).unmask();
        });

        $("#vendaForm").on("submit", function (e) {
            if (!validarCpfCnpj()) {
                e.preventDefault(); // Impede envio se o CPF/CNPJ for inválido
            }
            $("#clienteCpfCnpj").val($("#clienteCpfCnpj").val().replace(/\D/g, ''));
        });
    });



    document.getElementById("finalizarVenda").addEventListener("click", function () {
        const btnFinalizar = this;
        btnFinalizar.disabled = true;
        btnFinalizar.innerHTML = `<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span> Finalizando...`;

        if (produtosSelecionados.length === 0) {
            alert("Adicione pelo menos um produto para finalizar a venda!");
            resetarBotao();
            return;
        }

        if (inputCliente.value.trim() === "" || inputCpfCnpj.value.trim() === "") {
            alert("Selecione um cliente antes de finalizar a venda!");
            resetarBotao();
            return;
        }

        const nomeCliente = inputCliente.value.trim();
        const cpfCnpjCliente = inputCpfCnpj.value.trim().replace(/\D/g, '');

        fetch(`/clientes/buscar?nome=${encodeURIComponent(nomeCliente)}&cpfCnpj=${encodeURIComponent(cpfCnpjCliente)}`)
            .then(response => {
                if (!response.ok) {
                    throw new Error("Cliente não encontrado!");
                }
                return response.json();
            })
            .then(cliente => {
                if (!cliente || !cliente.id) {
                    alert("O cliente informado não está cadastrado. Cadastre-o antes de finalizar a venda!");
                    resetarBotao();
                    return;
                }

                const venda = {
                    cliente: { id: cliente.id },
                    itens: produtosSelecionados.map(produto => ({
                        produto: { id: produto.id },
                        quantidade: produto.quantidade,
                        precoUnitario: produto.preco
                    }))
                };

                console.log("Venda sendo enviada:", JSON.stringify(venda, null, 2));

                return fetch("/vendas/finalizar", {
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify(venda)
                });
            })
            .then(response => {
                if (!response.ok) {
                    return response.json().then(err => {
                        throw new Error(err.erro || "Erro desconhecido");
                    });
                }
                return response.json();
            })
            .then(data => {
                alert("Venda finalizada com sucesso!");

                // Limpar interface
                produtosSelecionados = [];
                atualizarTabela();
                inputCliente.value = "";
                inputCpfCnpj.value = "";
                totalVenda.textContent = "0.00";

                resetarBotao();
            })
            .catch(error => {
                alert(`Erro ao finalizar a venda: ${error.message}`);
                resetarBotao();
            });

        function resetarBotao() {
            btnFinalizar.disabled = false;
            btnFinalizar.innerHTML = "Finalizar Venda";
        }

    });



});
