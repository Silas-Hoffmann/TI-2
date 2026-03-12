package br.com.ti2.exercicio2;

public class Usuario {

    private Integer id;
    private String nome;
    private String email;
    private String senha;
    private String descricao;

    public Usuario() {
    }

    public Usuario(Integer id, String nome, String email, String senha, String descricao) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.descricao = descricao;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return "Usuario{" +
            "id=" + id +
            ", nome='" + nome + '\'' +
            ", email='" + email + '\'' +
            ", descricao='" + descricao + '\'' +
            "}";
    }
}
