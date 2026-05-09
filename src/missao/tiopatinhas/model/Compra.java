package missao.tiopatinhas.model;

import java.time.LocalDateTime;

public class Compra extends Transacao { // herança

    public Compra(int id, Carteira carteira, Criptomoeda criptomoeda,
                  double quantidade, double precoUnitario,
                  LocalDateTime dataHora) {

        super(id, carteira, criptomoeda, "COMPRA",
                quantidade, precoUnitario, dataHora);
    }

    @Override // polimorfismo
    public double calcularValorTotal() {
        return getQuantidade() * getPrecoUnitario();
    }

    @Override
    public void exibirDados() {
        System.out.println("Compra realizada.");
    }
}