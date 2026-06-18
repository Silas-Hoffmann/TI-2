package model;

import java.time.LocalDate;

public class Produto {
    private int id;
    private String descricao;
    private float preco;
    private int quantidade;
    private LocalDate dataFabricacao;

    public Produto() {}

    public Produto(int id, String descricao, float preco, int quantidade, LocalDate dataFabricacao) {
        this.id = id;
        this.descricao = descricao;
        this.preco = preco;
        this.quantidade = quantidade;
        this.dataFabricacao = dataFabricacao;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public float getPreco() { return preco; }
    public void setPreco(float preco) { this.preco = preco; }

    public int getQuantidade() { return quantidade; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }

    public LocalDate getDataFabricacao() { return dataFabricacao; }
    public void setDataFabricacao(LocalDate dataFabricacao) { this.dataFabricacao = dataFabricacao; }
}
