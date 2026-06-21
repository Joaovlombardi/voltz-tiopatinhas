package missao.tiopatinhas.model;

/**
 * Entidade Associativa que representa a relação Many-to-Many entre Carteira e Criptomoeda.
 * Representa o saldo de uma criptomoeda específica em uma carteira.
 */
public class Ativo {

    private int id; // PK
    private Carteira carteira; // FK
    private Criptomoeda criptomoeda; // FK
    private double quantidade;
    private double precoMedio;
    private double valorInvestido;
    private double valorAtual;

    public Ativo() {
    }

    public Ativo(int id, Carteira carteira, Criptomoeda criptomoeda, double quantidade, double precoMedio) {
        this.id = id;
        this.carteira = carteira;
        this.criptomoeda = criptomoeda;
        this.quantidade = quantidade;
        this.precoMedio = precoMedio;
        this.valorInvestido = quantidade * precoMedio;
        this.valorAtual = 0.0;
    }

    public double calcularValorAtual(double precoAtualMercado) {
        this.valorAtual = this.quantidade * precoAtualMercado;
        return this.valorAtual;
    }

    public double calcularValorInvestido() {
        this.valorInvestido = this.quantidade * this.precoMedio;
        return this.valorInvestido;
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


    public double getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(double quantidade) {
        this.quantidade = quantidade;
    }


    public double getPrecoMedio() {
        return precoMedio;
    }

    public void setPrecoMedio(double precoMedio) {
        this.precoMedio = precoMedio;
    }


    public double getValorInvestido() {
        return valorInvestido;
    }

    public void setValorInvestido(double valorInvestido) {
        this.valorInvestido = valorInvestido;
    }


    public double getValorAtual() {
        return valorAtual;
    }

    public void setValorAtual(double valorAtual) {
        this.valorAtual = valorAtual;
    }
}