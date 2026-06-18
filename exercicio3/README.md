# Exercício 3 - TI-2

Este projeto consiste em uma aplicação completa com:
- **Front-end**: Vite (Vanilla JavaScript + CSS3 moderno)
- **Back-end**: Java (Spark Framework)
- **Banco de Dados**: PostgreSQL

## 🛠️ Pré-requisitos
Certifique-se de ter os seguintes softwares instalados na sua máquina:
- **Node.js** e **npm**
- **Java JDK 11** (ou superior)
- **Apache Maven**
- **PostgreSQL** em execução na porta `5432`

## ⚙️ 1. Configurando o Banco de Dados

1. Certifique-se de que o serviço do banco de dados está rodando no seu Linux:
   ```bash
   sudo systemctl start postgresql
   ```
2. Crie as variáveis de ambiente:
   - Faça uma cópia do arquivo `.env.example` e renomeie para `.env`.
   - Abra o `.env` e coloque os dados nos campos correspondentes.
   
3. Crie a tabela do projeto executando o script SQL (na raiz do projeto):
   ```bash
   psql -U postgres -f src/main/resources/schema.sql
   ```
   *Nota: O script criará a tabela `produto` diretamente no banco `postgres` padrão.*

## ☕ 2. Rodando o Back-end (Java)

1. Abra um terminal na **raiz do projeto** (`/exercicio3`).
2. Baixe as dependências e compile o código com o Maven:
   ```bash
   mvn compile
   ```
3. Inicie o servidor da API Java:
   ```bash
   mvn exec:java -Dexec.mainClass="app.Aplicacao"
   ```
   A API agora estará rodando em `http://localhost:4567`.

## 🎨 3. Rodando o Front-end (Vite)

1. Abra um **novo terminal** (não feche o back-end).
2. Navegue até a pasta onde o front-end está instalado:
   ```bash
   cd src/main/resources
   ```
3. Instale as dependências do Node (necessário apenas na primeira vez):
   ```bash
   npm install
   ```
4. Inicie o servidor de desenvolvimento do Vite:
   ```bash
   npm run dev
   ```
   Acesse a URL informada no terminal (geralmente `http://localhost:5173`) no seu navegador.

---

## 🚀 Como testar

Com o front-end e o back-end rodando simultaneamente:
1. Acesse o site no seu navegador.
2. Cadastre produtos no formulário e veja eles aparecerem instantaneamente.
3. Utilize os botões **Editar** e **Excluir** para testar as operações de CRUD.
4. Você pode visualizar os dados reais conectando algum software no banco `postgres`.