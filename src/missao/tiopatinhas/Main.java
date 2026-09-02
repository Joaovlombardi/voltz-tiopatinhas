package missao.tiopatinhas;

import missao.tiopatinhas.model.*;
import missao.tiopatinhas.service.ApiCotacaoService;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;

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
                    LocalDateTime.now());

            Carteira carteira = new Carteira(1, usuario);
            usuario.adicionarCarteira(carteira);

            Criptomoeda bitcoin = new Criptomoeda(
                    1,
                    "Bitcoin",
                    "BTC",
                    "Principal criptomoeda do mercado");

            Criptomoeda ethereum = new Criptomoeda(
                    2,
                    "Ethereum",
                    "ETH",
                    "Plataforma descentralizada");

            // ================= ARRAYLIST =================
            // ArrayList usando a classe Usuario
            ArrayList<Usuario> listaUsuarios = new ArrayList<>();
            listaUsuarios.add(usuario);

            // ArrayList usando a classe Criptomoeda
            ArrayList<Criptomoeda> listaCriptomoedas = new ArrayList<>();
            listaCriptomoedas.add(bitcoin);
            listaCriptomoedas.add(ethereum);

            // Testando a iteração dos ArrayLists
            System.out.println("\n--- Teste ArrayList - Usuarios ---");
            for (Usuario u : listaUsuarios) {
                System.out.println("Usuario na lista: " + u.getNome());
            }

            System.out.println("\n--- Teste ArrayList - Criptomoedas ---");
            for (Criptomoeda c : listaCriptomoedas) {
                System.out.println("Criptomoeda na lista: " + c.getNome() + " (" + c.getSimbolo() + ")");
            }

            // ================= HASHMAP =================
            // HashMap usando a classe Usuario
            HashMap<Integer, Usuario> mapaUsuarios = new HashMap<>();
            mapaUsuarios.put(usuario.getId(), usuario);

            // HashMap usando a classe Criptomoeda
            HashMap<Integer, Criptomoeda> mapaCriptomoedas = new HashMap<>();
            mapaCriptomoedas.put(bitcoin.getId(), bitcoin);
            mapaCriptomoedas.put(ethereum.getId(), ethereum);

            // Testando busca com get()
            System.out.println("\n--- Teste HashMap ---");
            System.out.println("Usuario encontrado: " + mapaUsuarios.get(1).getNome());
            System.out.println("Criptomoeda encontrada: " + mapaCriptomoedas.get(1).getNome());

            // Testando remove()
            mapaCriptomoedas.remove(2);
            System.out.println("Ethereum removido do HashMap.");

            Ativo ativoBitcoin = new Ativo(1, carteira, bitcoin, 0.5, 300000.00);
            carteira.adicionarAtivo(ativoBitcoin);

            Ativo ativoEthereum = new Ativo(2, carteira, ethereum, 10.0, 20000.00);
            carteira.adicionarAtivo(ativoEthereum);

            ApiCotacaoService apiService = new ApiCotacaoService();
            Cotacao cotacaoBitcoin = apiService.buscarCotacao(bitcoin);
            Cotacao cotacaoEthereum = apiService.buscarCotacao(ethereum);

            ativoBitcoin.calcularValorAtual(cotacaoBitcoin.getPrecoAtual());
            ativoBitcoin.calcularValorInvestido();
            ativoEthereum.calcularValorAtual(cotacaoEthereum.getPrecoAtual());
            ativoEthereum.calcularValorInvestido();

            Compra compra1 = new Compra(
                    1,
                    carteira,
                    bitcoin,
                    0.5,
                    300000.00,
                    LocalDateTime.now());
            carteira.adicionarTransacao(compra1);

            Compra compra2 = new Compra(
                    2,
                    carteira,
                    ethereum,
                    10.0,
                    20000.00,
                    LocalDateTime.now());
            carteira.adicionarTransacao(compra2);

            Aporte aporte1 = new Aporte(
                    1,
                    carteira,
                    150000.00,
                    LocalDateTime.now(),
                    "Aporte inicial");
            carteira.adicionarAporte(aporte1);

            Aporte aporte2 = new Aporte(
                    2,
                    carteira,
                    200000.00,
                    LocalDateTime.now(),
                    "Aporte adicional");
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
                System.out.println(
                        "  " + fav.getCriptomoeda().getNome() + " (" + fav.getCriptomoeda().getSimbolo() + ")");
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
            System.out.println(
                    "  Valor total investido: R$ " + String.format("%.2f", carteira.calcularValorTotalInvestido()));
            System.out.println("  Valor atual: R$ " + String.format("%.2f", carteira.calcularValorAtual()));
            System.out.println("  Lucro/Prejuizo: R$ " + String.format("%.2f", carteira.calcularLucroPrejuizo()));

            // ================= MANIPULACAO DE ARQUIVOS =================
            try {
                FileWriter writer = new FileWriter("dados_voltz.txt");

                writer.write("=== DADOS DO SISTEMA VOLTZ ===\n\n");

                writer.write("=== USUARIOS NO HASHMAP ===\n");
                for (Integer chave : mapaUsuarios.keySet()) {
                    Usuario usuarioHashMap = mapaUsuarios.get(chave);
                    writer.write("Chave: " + chave + "\n");
                    writer.write("Usuario: " + usuarioHashMap.getNome() + "\n");
                    writer.write("Email: " + usuarioHashMap.getEmail() + "\n\n");
                }

                writer.write("=== CRIPTOMOEDAS NO HASHMAP ===\n");
                for (Integer chave : mapaCriptomoedas.keySet()) {
                    Criptomoeda criptoHashMap = mapaCriptomoedas.get(chave);
                    writer.write("Chave: " + chave + "\n");
                    writer.write("Criptomoeda: " + criptoHashMap.getNome() + "\n");
                    writer.write("Simbolo: " + criptoHashMap.getSimbolo() + "\n\n");
                }

                writer.write("\n=== USUARIOS NO ARRAYLIST ===\n");
                for (Usuario u : listaUsuarios) {
                    writer.write("Usuario: " + u.getNome() + "\n");
                    writer.write("Email: " + u.getEmail() + "\n\n");
                }

                writer.write("=== CRIPTOMOEDAS NO ARRAYLIST ===\n");
                for (Criptomoeda c : listaCriptomoedas) {
                    writer.write("Criptomoeda: " + c.getNome() + "\n");
                    writer.write("Simbolo: " + c.getSimbolo() + "\n\n");
                }

                writer.write("=== DADOS DA CARTEIRA ===\n");
                writer.write("Quantidade de ativos: " + carteira.getAtivos().size() + "\n");
                writer.write("Quantidade de transacoes: " + carteira.getTransacoes().size() + "\n");
                writer.write("Quantidade de aportes: " + carteira.getAportes().size() + "\n");

                writer.close();

                System.out.println("\nArquivo dados_voltz.txt criado com sucesso!");

                // ================= LEITURA DO ARQUIVO TXT =================
                BufferedReader reader = new BufferedReader(new FileReader("dados_voltz.txt"));

                String linha;

                System.out.println("\n=== CONTEUDO LIDO DO ARQUIVO TXT ===");

                while ((linha = reader.readLine()) != null) {
                    System.out.println(linha);
                }

                reader.close();

            } catch (IOException e) {
                System.out.println("Erro ao manipular arquivo: " + e.getMessage());
            }

             // Testando remove() depois de gravar o arquivo
            listaCriptomoedas.remove(ethereum);
            System.out.println("Ethereum removido do ArrayList.");
            System.out.println("\n=== TESTE EXECUTADO COM SUCESSO ===");

        } catch (Exception e) {
            System.out.println("ERRO DURANTE EXECUCAO: " + e.getMessage());
            e.printStackTrace();
        }
    }
}