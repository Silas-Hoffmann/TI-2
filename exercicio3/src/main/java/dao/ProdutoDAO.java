package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Produto;
import io.github.cdimascio.dotenv.Dotenv;

public class ProdutoDAO {
    private Connection conexao;
    private Dotenv dotenv;

    public ProdutoDAO() {
        conexao = null;
        dotenv = Dotenv.load();
    }

    // abrir conexao
    public boolean conectar() {
        String driverName = "org.postgresql.Driver";
        String serverName = dotenv.get("DB_HOST");
        String mydatabase = dotenv.get("DB_NAME");
        int porta = Integer.parseInt(dotenv.get("DB_PORT"));
        String url = "jdbc:postgresql://" + serverName + ":" + porta +"/" + mydatabase;
        String username = dotenv.get("DB_USER");
        String password = dotenv.get("DB_PASSWORD");

        try {
            Class.forName(driverName);
            conexao = DriverManager.getConnection(url, username, password);
            return (conexao != null);
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Conexão NÃO efetuada com o postgres -- " + e.getMessage());
            return false;
        }
    }

    // fechar conexao
    public boolean close() {
        try {
            if(conexao != null) { conexao.close(); return true; }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return false;
    }

    // inserir produto
    public boolean insert(Produto produto) {
        boolean status = false;
        try {
            String sql = "INSERT INTO produto (descricao, preco, quantidade, data_fabricacao) "
                       + "VALUES ('" + produto.getDescricao() + "', "
                       + produto.getPreco() + ", " + produto.getQuantidade() + ", '" + produto.getDataFabricacao() + "');";
            Statement st = conexao.createStatement();
            st.executeUpdate(sql);
            st.close();
            status = true;
        } catch (SQLException u) {  
            throw new RuntimeException(u);
        }
        return status;
    }

    // atualizar produto
    public boolean update(Produto produto) {
        boolean status = false;
        try {
            String sql = "UPDATE produto SET descricao = '" + produto.getDescricao() + "', "
                       + "preco = " + produto.getPreco() + ", "
                       + "quantidade = " + produto.getQuantidade() + ", "
                       + "data_fabricacao = '" + produto.getDataFabricacao() + "' "
                       + "WHERE id = " + produto.getId();
            Statement st = conexao.createStatement();
            st.executeUpdate(sql);
            st.close();
            status = true;
        } catch (SQLException u) {  
            throw new RuntimeException(u);
        }
        return status;
    }

    // deletar produto
    public boolean delete(int id) {
        boolean status = false;
        try {
            String sql = "DELETE FROM produto WHERE id = " + id;
            Statement st = conexao.createStatement();
            st.executeUpdate(sql);
            st.close();
            status = true;
        } catch (SQLException u) {  
            throw new RuntimeException(u);
        }
        return status;
    }

    // listar produto
    public List<Produto> getAll() {
        List<Produto> produtos = new ArrayList<>();
        try {
            Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            String sql = "SELECT * FROM produto";
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()) {
                Produto p = new Produto(rs.getInt("id"), rs.getString("descricao"), (float)rs.getDouble("preco"), 
                                        rs.getInt("quantidade"), rs.getDate("data_fabricacao").toLocalDate());
                produtos.add(p);
            }
            st.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return produtos;
    }
}
