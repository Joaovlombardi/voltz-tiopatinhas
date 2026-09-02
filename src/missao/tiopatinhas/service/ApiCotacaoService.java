package missao.tiopatinhas.service;

import missao.tiopatinhas.model.Cotacao;
import missao.tiopatinhas.model.Criptomoeda;

import java.time.LocalDateTime;

public class ApiCotacaoService {

    public Cotacao buscarCotacao(String simbolo) {
        Criptomoeda criptomoeda = new Criptomoeda(1, "Bitcoin", simbolo, "Criptomoeda obtida por serviço externo");

        return new Cotacao(
                1,
                criptomoeda,
                350000.00,
                2.5,
                6.2,
                11.8,
                LocalDateTime.now()
        );
    }

    public Cotacao buscarCotacao(Criptomoeda criptomoeda) {
        return new Cotacao(
                1,
                criptomoeda,
                350000.00,
                2.5,
                6.2,
                11.8,
                LocalDateTime.now()
        );
    }

    public void atualizarCotacoes() {
        System.out.println("Cotações atualizadas com sucesso.");
    }
}