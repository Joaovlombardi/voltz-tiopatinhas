package missao.tiopatinhas.model;

import java.util.ArrayList;
import java.util.List;

public class Criptomoeda {
    private int id; // PK
    private String nome;
    private String simbolo;
    private String descricao;
    private List<Cotacao> cotacoes; // One-to-Many: Uma criptomoeda pode ter várias cotações históricas
    private List<Favorito> favoritos; // Many-to-Many: Uma criptomoeda pode ser favoritada por vários usuários

    public Criptomoeda() {
        this.cotacoes = new ArrayList<>();
        this.favoritos = new ArrayList<>();
    }

    public Criptomoeda(int id, String nome, String simbolo, String descricao) {
        this.id = id;
        this.nome = nome;
        this.simbolo = simbolo;
        this.descricao = descricao;
        this.cotacoes = new ArrayList<>();
        this.favoritos = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }


    public String getSimbolo() {
        return simbolo;
    }

    public void setSimbolo(String simbolo) {
        this.simbolo = simbolo;
    }


    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public List<Cotacao> getCotacoes() {
        return cotacoes;
    }

    public void setCotacoes(List<Cotacao> cotacoes) {
        this.cotacoes = cotacoes;
    }

    public List<Favorito> getFavoritos() {
        return favoritos;
    }

    public void setFavoritos(List<Favorito> favoritos) {
        this.favoritos = favoritos;
    }
}
