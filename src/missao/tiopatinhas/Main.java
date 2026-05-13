package missao.tiopatinhas;

import missao.tiopatinhas.model.*;
import missao.tiopatinhas.service.ApiCotacaoService;

import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        try {
            System.out.println("=== TESTES DO SISTEMA ===\n");

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
            usuario.adicionarCarteira(carteira);

            Criptomoeda bitcoin = new Criptomoeda(
                1,
                "Bitcoin",
                "BTC",
                "Principal criptomoeda do mercado"
            );

            Criptomoeda ethereum = new Criptomoeda(
                2,
                "Ethereum",
                "ETH",
                "Plataforma descentralizada"
            );

            Ativo ativoBitcoin = new Ativo(1, carteira, bitcoin, 0.5, 300000.00);
            carteira.adicionarAtivo(ativoBitcoin);

            Ativo ativoEthereum = new Ativo(2, carteira, ethereum, 10.0, 20000.00);
            carteira.adicionarAtivo(ativoEthereum);

            ApiCotacaoService apiService = new ApiCotacaoService();
            Cotacao cotacaoBitcoin = apiService.buscarCotacao(bitcoin);
            Cotacao cotacaoEthereum = apiService.buscarCotacao(ethereum);

            ativoBitcoin.calcularValorAtual(cotacaoBitcoin.getPrecoAtual());
            ativoBitcoin.calcularValorInvestido();
            ativoEthereum.calcularValorAtual(350000.00);
            ativoEthereum.calcularValorInvestido();

            Compra compra1 = new Compra(
                1,
                carteira,
                bitcoin,
                0.5,
                300000.00,
                LocalDateTime.now()
            );
            carteira.adicionarTransacao(compra1);

            Compra compra2 = new Compra(
                2,
                carteira,
                ethereum,
                10.0,
                20000.00,
                LocalDateTime.now()
            );
            carteira.adicionarTransacao(compra2);

            Aporte aporte1 = new Aporte(
                1,
                carteira,
                150000.00,
                LocalDateTime.now(),
                "Aporte inicial"
            );
            carteira.adicionarAporte(aporte1);

            Aporte aporte2 = new Aporte(
                2,
                carteira,
                200000.00,
                LocalDateTime.now(),
                "Aporte adicional"
            );
            carteira.adicionarAporte(aporte2, true);

            Favorito fav1 = new Favorito(1, usuario, bitcoin, LocalDateTime.now());
            usuario.getFavoritos().add(fav1);

            Favorito fav2 = new Favorito(2, usuario, ethereum, LocalDateTime.now());
            usuario.getFavoritos().add(fav2);

            System.out.println("--- Usuario ---");
            usuario.exibirDados();
            usuario.exibirDados(true);

            System.out.println("\n--- Criptomoedas Favoritas ---");
            for (Favorito fav : usuario.getFavoritos()) {
                System.out.println("  " + fav.getCriptomoeda().getNome() + " (" + fav.getCriptomoeda().getSimbolo() + ")");
            }

            System.out.println("\n--- Transacoes ---");
            compra1.exibirDados();
            compra1.exibirDados(true);
            System.out.println();
            compra2.exibirDados();
            compra2.exibirDados(true);

            System.out.println("\n--- Carteira ---");
            System.out.println("  Ativos: " + carteira.getAtivos().size());
            System.out.println("  Transacoes: " + carteira.getTransacoes().size());
            System.out.println("  Aportes: " + carteira.getAportes().size());
            System.out.println("  Valor total investido: R$ " + String.format("%.2f", carteira.calcularValorTotalInvestido()));
            System.out.println("  Valor atual: R$ " + String.format("%.2f", carteira.calcularValorAtual()));
            System.out.println("  Lucro/Prejuizo: R$ " + String.format("%.2f", carteira.calcularLucroPrejuizo()));

            System.out.println("\n=== TESTE EXECUTADO COM SUCESSO ===");

        } catch (Exception e) {
            System.out.println("ERRO DURANTE EXECUCAO: " + e.getMessage());
            e.printStackTrace();
        }
    }
}