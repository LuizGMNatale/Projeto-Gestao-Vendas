
# Projeto Gestão Vendas

Este é o projeto de **Gestão de Vendas** para a empresa Tom & Jerry Distribuidora.

## Acesso ao Sistema

O usuário padrão para login no sistema é:

- **Usuário:** admin
- **Senha:** 123456

## Tecnologias Utilizadas

- **Backend:** Spring Boot
- **Frontend:** HTML, CSS e JavaScript
- **Banco de Dados:** MySQL

## Tutorial para Clonar e Rodar o Projeto com Docker

Para rodar o projeto **Gestão Vendas** utilizando o Docker, siga os passos abaixo:

### 1. Baixar a Imagem Docker

Use o seguinte comando para fazer o **pull** da imagem do Docker Hub:

```bash
docker pull luizgmnatale/gestao-vendas:v1.0
```

### 2. Rodar o Contêiner Docker

Após o download da imagem, execute o contêiner com o comando abaixo:

```bash
docker-compose up -d
```

Esse comando irá rodar o projeto em segundo plano.

### 3. Acessar o Sistema

Após o contêiner estar em funcionamento, você pode acessar o sistema no seu navegador, utilizando a URL:

```
http://localhost:8080
```

### 4. Parar e Remover o Contêiner

Para parar o contêiner, use o comando:

```bash
docker-compose down
```
