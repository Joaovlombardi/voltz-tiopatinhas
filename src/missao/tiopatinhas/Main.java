package missao.tiopatinhas;

import missao.tiopatinhas.model.*;
import missao.tiopatinhas.service.ApiCotacaoService;

import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        Usuario usuario = new Usuario(
                1,
                "João Vitor",
                "123.456.789-00",
                "(11) 99999-9999",
                "joao@email.com",
                "senha123",
                LocalDateTime.now()
        );

        Carteira carteira = new Carteira(1, usuario);
        usuario.vincularCarteira(carteira);

        Criptomoeda bitcoin = new Criptomoeda(
                1,
                "Bitcoin",
                "BTC",
                "Principal criptomoeda do mercado"
        );

        Ativo ativoBitcoin = new Ativo(
                1,
                carteira,
                bitcoin,
                0.5,
                300000.00
        );

        ApiCotacaoService apiCotacaoService = new ApiCotacaoService();
        Cotacao cotacaoBitcoin = apiCotacaoService.buscarCotacao(bitcoin);

        ativoBitcoin.calcularValorAtual(cotacaoBitcoin.getPrecoAtual());
        ativoBitcoin.calcularValorInvestido();

        carteira.adicionarAtivo(ativoBitcoin);

        Transacao transacaoCompra = new Transacao(
                1,
                carteira,
                bitcoin,
                "COMPRA",
                0.5,
                300000.00,
                LocalDateTime.now()
        );

        carteira.adicionarTransacao(transacaoCompra);

        Aporte aporte = new Aporte(
                1,
                carteira,
                150000.00,
                LocalDateTime.now(),
                "Aporte inicial para compra de Bitcoin"
        );

        carteira.adicionarAporte(aporte);

        System.out.println("Usuário: " + usuario.getNome());
        System.out.println("Criptomoeda: " + bitcoin.getNome());
        System.out.println("Preço atual: " + cotacaoBitcoin.getPrecoAtual());
        System.out.println("Valor investido: " + carteira.calcularValorTotalInvestido());
        System.out.println("Valor atual: " + carteira.calcularValorAtual());
        System.out.println("Lucro/Prejuízo: " + carteira.calcularLucroPrejuizo());
    }
}