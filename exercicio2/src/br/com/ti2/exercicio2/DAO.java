package br.com.ti2.exercicio2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DAO {

    private static final String URL  = "jdbc:postgresql://localhost:5432/exercicio2";
    private static final String USER = "postgres";
    private static final String PASS = "postgres";

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    public void createTableIfNotExists() throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS usuarios (
                id        SERIAL       PRIMARY KEY,
                nome      VARCHAR(120) NOT NULL,
                email     VARCHAR(150) NOT NULL UNIQUE,
                senha     VARCHAR(255) NOT NULL,
                descricao TEXT
            )
            """;

        try (Connection conn = getConnection();
             Statement st = conn.createStatement()) {
            st.execute(sql);
        }
    }

    public Usuario inserir(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO usuarios (nome, email, senha, descricao) VALUES (?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            st.setString(1, usuario.getNome());
            st.setString(2, usuario.getEmail());
            st.setString(3, usuario.getSenha());
            st.setString(4, usuario.getDescricao());
            st.executeUpdate();

            try (ResultSet keys = st.getGeneratedKeys()) {
                if (keys.next()) {
                    usuario.setId(keys.getInt(1));
                }
            }

            return usuario;
        }
    }

    public List<Usuario> listar() throws SQLException {
        String sql = "SELECT id, nome, email, senha, descricao FROM usuarios ORDER BY id";
        List<Usuario> lista = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement st = conn.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        }

        return lista;
    }

    public Optional<Usuario> buscarPorId(int id) throws SQLException {
        String sql = "SELECT id, nome, email, senha, descricao FROM usuarios WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, id);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapear(rs));
                }
            }
        }

        return Optional.empty();
    }

    public boolean atualizar(Usuario usuario) throws SQLException {
        String sql = """
            UPDATE usuarios
            SET nome = ?, email = ?, senha = ?, descricao = ?
            WHERE id = ?
            """;

        try (Connection conn = getConnection();
             PreparedStatement st = conn.prepareStatement(sql)) {
            st.setString(1, usuario.getNome());
            st.setString(2, usuario.getEmail());
            st.setString(3, usuario.getSenha());
            st.setString(4, usuario.getDescricao());
            st.setInt(5, usuario.getId());
            return st.executeUpdate() > 0;
        }
    }

    public boolean excluir(int id) throws SQLException {
        String sql = "DELETE FROM usuarios WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, id);
            return st.executeUpdate() > 0;
        }
    }

    private Usuario mapear(ResultSet rs) throws SQLException {
        return new Usuario(
            rs.getInt("id"),
            rs.getString("nome"),
            rs.getString("email"),
            rs.getString("senha"),
            rs.getString("descricao"));
    }
}
