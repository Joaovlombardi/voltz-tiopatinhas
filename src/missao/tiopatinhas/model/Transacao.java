package missao.tiopatinhas.model;

import java.time.LocalDateTime;

public class Transacao {

    private int id;
    private Carteira carteira;
    private Criptomoeda criptomoeda;
    private String tipo;
    private double quantidade;
    private double precoUnitario;
    private double valorTotal;
    private LocalDateTime dataHora;

    public Transacao() {
    }

    public Transacao(int id, Carteira carteira, Criptomoeda criptomoeda, String tipo,
                     double quantidade, double precoUnitario, LocalDateTime dataHora) {
        this.id = id;
        this.carteira = carteira;
        this.criptomoeda = criptomoeda;
        this.tipo = tipo;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
        this.dataHora = dataHora;
        this.valorTotal = calcularValorTotal();
    }

    public double calcularValorTotal() {
        this.valorTotal = this.quantidade * this.precoUnitario;
        return this.valorTotal;
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


    public Criptomoeda getCriptomoeda() {
        return criptomoeda;
    }

    public void setCriptomoeda(Criptomoeda criptomoeda) {
        this.criptomoeda = criptomoeda;
    }


    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }


    public double getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(double quantidade) {
        this.quantidade = quantidade;
    }


    public double getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(double precoUnitario) {
        this.precoUnitario = precoUnitario;
    }


    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }


    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }
}