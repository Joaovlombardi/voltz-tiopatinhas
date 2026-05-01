package missao.tiopatinhas.model;

import java.time.LocalDateTime;

public class Cotacao {
    private int id; // PK
    private Criptomoeda criptomoeda; // FK
    private double precoAtual;
    private double variacao24h;
    private double variacao7d;
    private double variacao30d;
    private LocalDateTime dataConsulta;

    public Cotacao() {
    }

    public Cotacao(int id, Criptomoeda criptomoeda, double precoAtual, double variacao24h,
                   double variacao7d, double variacao30d, LocalDateTime dataConsulta) {
        this.id = id;
        this.criptomoeda = criptomoeda;
        this.precoAtual = precoAtual;
        this.variacao24h = variacao24h;
        this.variacao7d = variacao7d;
        this.variacao30d = variacao30d;
        this.dataConsulta = dataConsulta;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Criptomoeda getCriptomoeda() {
        return criptomoeda;
    }

    public void setCriptomoeda(Criptomoeda criptomoeda) {
        this.criptomoeda = criptomoeda;
    }

    public double getPrecoAtual() {
        return precoAtual;
    }

    public void setPrecoAtual(double precoAtual) {
        this.precoAtual = precoAtual;
    }

    public double getVariacao24h() {
        return variacao24h;
    }

    public void setVariacao24h(double variacao24h) {
        this.variacao24h = variacao24h;
    }

    public double getVariacao7d() {
        return variacao7d;
    }

    public void setVariacao7d(double variacao7d) {
        this.variacao7d = variacao7d;
    }

    public double getVariacao30d() {
        return variacao30d;
    }

    public void setVariacao30d(double variacao30d) {
        this.variacao30d = variacao30d;
    }

    public LocalDateTime getDataConsulta() {
        return dataConsulta;
    }

    public void setDataConsulta(LocalDateTime dataConsulta) {
        this.dataConsulta = dataConsulta;
    }
}