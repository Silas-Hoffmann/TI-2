# Exercicio 2 - CRUD de Usuarios com PostgreSQL

Projeto Java em modo console com integracao JDBC e PostgreSQL.

## Estrutura Atual

Arquivos da aplicacao:

- `src/br/com/ti2/exercicio2/Principal.java`: classe principal (main) com menu e fluxo do CRUD.
- `src/br/com/ti2/exercicio2/DAO.java`: acesso ao banco e operacoes CRUD.
- `src/br/com/ti2/exercicio2/Usuario.java`: entidade de usuario.

Configuracao do projeto:

- `pom.xml`: dependencia do driver PostgreSQL via Maven.

## Dependencia PostgreSQL

A importacao do PostgreSQL e feita no `pom.xml`:

```xml
<dependency>
   <groupId>org.postgresql</groupId>
   <artifactId>postgresql</artifactId>
   <version>42.7.7</version>
</dependency>
```

## Banco de Dados

Tabela usada pela aplicacao: `usuarios`

Campos:

- `id`
- `nome`
- `email`
- `senha`
- `descricao`

A tabela e criada automaticamente ao iniciar a aplicacao (`createTableIfNotExists`).

## Configuracao de Conexao

Atualmente fixa na classe `DAO.java`:

- URL: `jdbc:postgresql://localhost:5432/exercicio2`
- Usuario: `postgres`
- Senha: `postgres`

## Como Executar

1. Crie o banco no PostgreSQL:

```sql
CREATE DATABASE exercicio2;
```

2. No Eclipse/VS Code, atualize o projeto Maven para baixar as dependencias.

3. Execute a classe:

`br.com.ti2.exercicio2.Principal`

## Menu da Aplicacao

- `1 - Inserir`
- `2 - Listar`
- `3 - Excluir`
- `4 - Atualizar`
- `0 - Sair`

## Observacao

A senha está sendo gravada em texto, mesmo que o ideal é a criptografia da mesma