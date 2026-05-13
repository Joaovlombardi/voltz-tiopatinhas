package missao.tiopatinhas.model;

import java.time.LocalDateTime;

public class Aporte {

    private int id; // PK
    private Carteira carteira; // FK
    private double valor;
    private LocalDateTime dataHora;
    private String descricao;

    public Aporte() {
    }

    public Aporte(int id, Carteira carteira, double valor, LocalDateTime dataHora, String descricao) {
        this.id = id;
        this.carteira = carteira;
        this.valor = valor;
        this.dataHora = dataHora;
        this.descricao = descricao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public Carteira getCarteira() {
        return carteira;
    }

    public void setCarteira(Carteira carteira) {
        this.carteira = carteira;
    }


    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }


    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }


    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}