package br.com.ti2.exercicio2;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Principal {

    private final DAO dao;
    private final Scanner scanner;

    public Principal() {
        this.dao = new DAO();
        this.scanner = new Scanner(System.in);
    }

    public static void main(String[] args) {
        new Principal().start();
    }

    private void start() {
        try {
            dao.createTableIfNotExists();
            boolean running = true;

            while (running) {
                exibirMenu();
                String opcao = scanner.nextLine();

                switch (opcao) {
                    case "1" -> inserir();
                    case "2" -> listar();
                    case "3" -> excluir();
                    case "4" -> atualizar();
                    case "0" -> running = false;
                    default  -> System.out.println("Opcao invalida. Tente novamente.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro no banco de dados: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }

    private void exibirMenu() {
        System.out.println();
        System.out.println("=== CRUD de Usuarios ===");
        System.out.println("1 - Inserir");
        System.out.println("2 - Listar");
        System.out.println("3 - Excluir");
        System.out.println("4 - Atualizar");
        System.out.println("0 - Sair");
        System.out.print("Escolha: ");
    }

    private void inserir() throws SQLException {
        Usuario u = lerDados(null);
        dao.inserir(u);
        System.out.println("Usuario inserido com ID: " + u.getId());
    }

    private void listar() throws SQLException {
        List<Usuario> lista = dao.listar();
        if (lista.isEmpty()) {
            System.out.println("Nenhum usuario encontrado.");
            return;
        }
        lista.forEach(System.out::println);
    }

    private void excluir() throws SQLException {
        int id = lerInt("ID do usuario a excluir: ");
        if (dao.excluir(id)) {
            System.out.println("Usuario excluido com sucesso.");
        } else {
            System.out.println("Usuario nao encontrado.");
        }
    }

    private void atualizar() throws SQLException {
        int id = lerInt("ID do usuario a atualizar: ");
        Optional<Usuario> existente = dao.buscarPorId(id);

        if (existente.isEmpty()) {
            System.out.println("Usuario nao encontrado.");
            return;
        }

        Usuario atualizado = lerDados(existente.get());
        atualizado.setId(id);

        if (dao.atualizar(atualizado)) {
            System.out.println("Usuario atualizado com sucesso.");
        } else {
            System.out.println("Nenhum registro foi alterado.");
        }
    }

    private Usuario lerDados(Usuario base) {
        Usuario u = base == null ? new Usuario()
            : new Usuario(base.getId(), base.getNome(), base.getEmail(), base.getSenha(), base.getDescricao());

        u.setNome(lerObrigatorio("Nome", u.getNome()));
        u.setEmail(lerObrigatorio("Email", u.getEmail()));
        u.setSenha(lerObrigatorio("Senha", u.getSenha()));
        u.setDescricao(lerOpcional("Descricao", u.getDescricao()));
        return u;
    }

    private String lerObrigatorio(String campo, String atual) {
        while (true) {
            String valor = lerOpcional(campo, atual);
            if (!valor.isBlank()) {
                return valor;
            }
            System.out.println(campo + " e obrigatorio.");
        }
    }

    private String lerOpcional(String campo, String atual) {
        System.out.print(atual == null ? campo + ": " : campo + " [" + atual + "]: ");
        String valor = scanner.nextLine().trim();
        return (valor.isBlank() && atual != null) ? atual : valor;
    }

    private int lerInt(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Informe um numero inteiro valido.");
            }
        }
    }
}
