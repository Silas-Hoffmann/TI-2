# Exercício 4 - Análise de Sentimentos e Banco de Dados

Este projeto faz parte do Exercício 4 de Computação em Nuvem e Serviços Cognitivos. Ele é uma aplicação Java CLI (Command Line Interface) que se conecta a um banco de dados PostgreSQL hospedado no Microsoft Azure e consome a API do Azure Cognitive Services para realizar a Análise de Sentimentos em textos.

## Pré-requisitos
- Java 11 ou superior
- Maven
- Um arquivo `.env` configurado na raiz do repositório (`../.env` em relação a esta pasta) com as seguintes propriedades:
  ```env
  DB_HOST=seu_host_do_postgres
  DB_PORT=5432
  DB_NAME=postgres
  DB_USER=seu_usuario
  DB_PASSWORD=sua_senha
  DB_SSLMODE=disable
  
  AZURE_KEY=sua_chave_azure
  AZURE_ENDPOINT=seu_endpoint_azure
  ```

## Como Executar
Siga as etapas abaixo para compilar e iniciar o menu interativo:

1. Abra o terminal e navegue até a pasta deste exercício (`exercicio4`).
2. Execute o comando Maven abaixo para iniciar a aplicação:
   ```bash
   mvn compile exec:java -Dexec.mainClass="com.ti2.App"
   ```

A aplicação cuidará automaticamente de se conectar ao banco de dados e criar a tabela `textos` (caso ela ainda não exista). Depois, um menu interativo será aberto no seu terminal, contendo opções para:
1. **Adicionar texto:** Digitar e persistir um texto no PostgreSQL.
2. **Excluir texto:** Deletar um texto salvo pelo seu respectivo ID numérico.
3. **Analisar sentimentos:** Ler todos os textos do banco e enviá-los em lote para a inteligência artificial do Azure avaliar se o sentimento de cada frase é positivo, negativo ou neutro.
4. **Listar textos:** Exibir todo o conteúdo já salvo no banco, porém de forma imediata (sem consumir a cota da API de sentimentos).
0. **Sair.**
