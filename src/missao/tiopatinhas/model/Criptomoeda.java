package missao.tiopatinhas.model;

public class Criptomoeda {
    private int id;
    private String nome;
    private String simbolo;
    private String descricao;

    public Criptomoeda() {
    }

    public Criptomoeda(int id, String nome, String simbolo, String descricao) {
        this.id = id;
        this.nome = nome;
        this.simbolo = simbolo;
        this.descricao = descricao;
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
}
