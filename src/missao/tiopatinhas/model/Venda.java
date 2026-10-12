package missao.tiopatinhas.model;

import java.time.LocalDateTime;

public class Venda extends Transacao {

    public Venda(int id, Carteira carteira, Criptomoeda criptomoeda,
                 double quantidade, double precoUnitario,
                 LocalDateTime dataHora) {

        super(id, carteira, criptomoeda, "VENDA",
                quantidade, precoUnitario, dataHora);
    }

    @Override
    public double calcularValorTotal() {
        return getQuantidade() * getPrecoUnitario();
    }

    @Override
    public void exibirDados() {
        System.out.println("Venda realizada.");
    }
}
