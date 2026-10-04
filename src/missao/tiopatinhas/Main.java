package missao.tiopatinhas;

import missao.tiopatinhas.model.*;
import missao.tiopatinhas.service.ApiCotacaoService;
import missao.tiopatinhas.dao.CriptomoedaDao;
import missao.tiopatinhas.dao.UsuarioDao;
import missao.tiopatinhas.dao.CarteiraDao;
import missao.tiopatinhas.dao.AtivoDao;
import missao.tiopatinhas.dao.TransacaoDao;
import missao.tiopatinhas.dao.AporteDao;
import missao.tiopatinhas.dao.FavoritoDao;
import missao.tiopatinhas.dao.CotacaoDao;

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

            testarCrudUsuarioCarteira();
            testarCrudAtivo();
            testarCrudTransacaoAporte();
            testarCrudFavoritoCotacao();

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
            // ================= BANCO DE DADOS - CRUD CRIPTOMOEDA =================
            testarCrudCriptomoeda();

            System.out.println("\n=== TESTE EXECUTADO COM SUCESSO ===");

        } catch (Exception e) {
            System.out.println("ERRO DURANTE EXECUCAO: " + e.getMessage());
            e.printStackTrace();
        }
    }
    private static void testarCrudCriptomoeda() {

    CriptomoedaDao dao = null;

    try {
        dao = new CriptomoedaDao();

        // Limpa o registro caso o teste já tenha sido executado
        dao.excluir(99);

        System.out.println("\n=== TESTE CRUD CRIPTOMOEDA NO BANCO ===");

        // INSERT
        Criptomoeda criptomoedaTeste = new Criptomoeda(
                99,
                "Cardano",
                "ADA",
                "Criptomoeda inserida pelo Main"
        );

        dao.inserir(criptomoedaTeste);
        System.out.println("INSERT realizado com sucesso.");

        // SELECT
        System.out.println("\n--- CRIPTOMOEDAS APOS INSERT ---");

        for (Criptomoeda cripto : dao.listar()) {
            System.out.println(
                    cripto.getId() + " | "
                            + cripto.getNome() + " | "
                            + cripto.getSimbolo() + " | "
                            + cripto.getDescricao()
            );
        }

        // UPDATE
        criptomoedaTeste.setNome("Cardano Atualizada");
        criptomoedaTeste.setDescricao("Descricao atualizada pelo Main");

        dao.alterar(criptomoedaTeste);
        System.out.println("\nUPDATE realizado com sucesso.");

        // SELECT novamente
        System.out.println("\n--- CRIPTOMOEDAS APOS UPDATE ---");

        for (Criptomoeda cripto : dao.listar()) {
            System.out.println(
                    cripto.getId() + " | "
                            + cripto.getNome() + " | "
                            + cripto.getSimbolo() + " | "
                            + cripto.getDescricao()
            );
        }

        // DELETE
        dao.excluir(99);
        System.out.println("\nDELETE realizado com sucesso.");

        // SELECT final
        System.out.println("\n--- CRIPTOMOEDAS APOS DELETE ---");

        for (Criptomoeda cripto : dao.listar()) {
            System.out.println(
                    cripto.getId() + " | "
                            + cripto.getNome() + " | "
                            + cripto.getSimbolo() + " | "
                            + cripto.getDescricao()
            );
        }

    } catch (Exception e) {

        System.out.println(
                "Erro ao testar CRUD de Criptomoeda: "
                        + e.getMessage()
        );

    } finally {

        if (dao != null) {
            try {
                dao.fecharConexao();
            } catch (Exception e) {
                System.out.println(
                        "Erro ao fechar conexao: "
                                + e.getMessage()
                );
            }
        }
    }
}

    private static void testarCrudUsuarioCarteira() {

        UsuarioDao usuarioDao = null;
        CarteiraDao carteiraDao = null;

        final int ID_TESTE = 98;

        try {

            usuarioDao = new UsuarioDao();
            carteiraDao = new CarteiraDao();

            System.out.println(
                    "\n=== TESTE CRUD USUARIO E CARTEIRA NO BANCO ==="
            );

            carteiraDao.excluir(ID_TESTE);
            usuarioDao.excluir(ID_TESTE);

            Usuario usuarioTeste = new Usuario(
                    ID_TESTE,
                    "Usuario Teste Voltz",
                    "98765432198",
                    "(11) 98888-9898",
                    "usuario98@voltz.com",
                    "hash_teste_98",
                    LocalDateTime.now()
            );

            usuarioDao.inserir(usuarioTeste);

            System.out.println(
                    "Usuario: INSERT realizado com sucesso."
            );

            System.out.println(
                    "\n--- USUARIO APOS INSERT ---"
            );

            for (Usuario u : usuarioDao.listar()) {

                if (u.getId() == ID_TESTE) {

                    System.out.println(
                            u.getId()
                                    + " | "
                                    + u.getNome()
                                    + " | "
                                    + u.getCpf()
                                    + " | "
                                    + u.getEmail()
                    );
                }
            }

            Carteira carteiraTeste = new Carteira(
                    ID_TESTE,
                    usuarioTeste,
                    1000.00,
                    1150.00,
                    150.00,
                    LocalDateTime.now()
            );

            carteiraDao.inserir(carteiraTeste);

            System.out.println(
                    "Carteira: INSERT realizado com sucesso."
            );

            System.out.println(
                    "\n--- CARTEIRA APOS INSERT ---"
            );

            for (Carteira c : carteiraDao.listar()) {

                if (c.getId() == ID_TESTE) {

                    System.out.println(
                            c.getId()
                                    + " | usuario_id="
                                    + c.getUsuario().getId()
                                    + " | investido="
                                    + c.getValorTotalInvestido()
                                    + " | atual="
                                    + c.getValorAtual()
                                    + " | lucro/prejuizo="
                                    + c.getLucroPrejuizo()
                    );
                }
            }

            usuarioTeste.setNome(
                    "Usuario Teste Voltz Atualizado"
            );

            usuarioTeste.setTelefone(
                    "(21) 97777-9898"
            );

            usuarioTeste.setEmail(
                    "usuario98.atualizado@voltz.com"
            );

            usuarioTeste.setSenhaHash(
                    "hash_teste_98_atualizado"
            );

            usuarioDao.alterar(usuarioTeste);

            System.out.println(
                    "Usuario: UPDATE realizado com sucesso."
            );

            carteiraTeste.setValorTotalInvestido(
                    1500.00
            );

            carteiraTeste.setValorAtual(
                    1800.00
            );

            carteiraTeste.setLucroPrejuizo(
                    300.00
            );

            carteiraTeste.setDataAtualizacao(
                    LocalDateTime.now()
            );

            carteiraDao.alterar(
                    carteiraTeste
            );

            System.out.println(
                    "Carteira: UPDATE realizado com sucesso."
            );

            System.out.println(
                    "\n--- DADOS APOS UPDATE ---"
            );

            for (Usuario u : usuarioDao.listar()) {

                if (u.getId() == ID_TESTE) {

                    System.out.println(
                            "Usuario atualizado: "
                                    + u.getNome()
                                    + " | "
                                    + u.getEmail()
                    );
                }
            }

            for (Carteira c : carteiraDao.listar()) {

                if (c.getId() == ID_TESTE) {

                    System.out.println(
                            "Carteira atualizada: investido="
                                    + c.getValorTotalInvestido()
                                    + " | atual="
                                    + c.getValorAtual()
                                    + " | lucro/prejuizo="
                                    + c.getLucroPrejuizo()
                    );
                }
            }

            carteiraDao.excluir(ID_TESTE);

            System.out.println(
                    "Carteira: DELETE realizado com sucesso."
            );

            usuarioDao.excluir(ID_TESTE);

            System.out.println(
                    "Usuario: DELETE realizado com sucesso."
            );

        } catch (Exception e) {

            System.out.println(
                    "Erro ao testar CRUD de Usuario/Carteira: "
                            + e.getMessage()
            );

            e.printStackTrace();

        } finally {

            if (carteiraDao != null) {

                try {
                    carteiraDao.fecharConexao();

                } catch (Exception e) {

                    System.out.println(
                            "Erro ao fechar conexao de Carteira: "
                                    + e.getMessage()
                    );
                }
            }

            if (usuarioDao != null) {

                try {
                    usuarioDao.fecharConexao();

                } catch (Exception e) {

                    System.out.println(
                            "Erro ao fechar conexao de Usuario: "
                                    + e.getMessage()
                    );
                }
            }
        }
    }

    private static void testarCrudAtivo() {

        AtivoDao ativoDao = null;
        UsuarioDao usuarioDao = null;
        CarteiraDao carteiraDao = null;
        CriptomoedaDao criptomoedaDao = null;

        final int ID_TESTE = 97;

        try {

            ativoDao = new AtivoDao();
            usuarioDao = new UsuarioDao();
            carteiraDao = new CarteiraDao();
            criptomoedaDao = new CriptomoedaDao();

            System.out.println(
                    "\n=== TESTE CRUD ATIVO NO BANCO ==="
            );

            ativoDao.excluir(ID_TESTE);
            carteiraDao.excluir(ID_TESTE);
            usuarioDao.excluir(ID_TESTE);
            criptomoedaDao.excluir(ID_TESTE);

            Usuario usuarioTesteAtivo = new Usuario(
                    ID_TESTE,
                    "Usuario Teste Ativo",
                    "98765432197",
                    "(11) 97777-9797",
                    "ativo97@voltz.com",
                    "hash_ativo_97",
                    LocalDateTime.now()
            );

            usuarioDao.inserir(usuarioTesteAtivo);

            Carteira carteiraTesteAtivo = new Carteira(
                    ID_TESTE,
                    usuarioTesteAtivo,
                    2000.00,
                    2200.00,
                    200.00,
                    LocalDateTime.now()
            );

            carteiraDao.inserir(carteiraTesteAtivo);

            Criptomoeda criptomoedaTesteAtivo = new Criptomoeda(
                    ID_TESTE,
                    "Teste Ativo Coin",
                    "TAC",
                    "Criptomoeda temporaria para teste do Ativo"
            );

            criptomoedaDao.inserir(criptomoedaTesteAtivo);

            Ativo ativoTeste = new Ativo(
                    ID_TESTE,
                    carteiraTesteAtivo,
                    criptomoedaTesteAtivo,
                    2.0,
                    500.00
            );

            ativoTeste.setValorAtual(
                    1100.00
            );

            ativoDao.inserir(ativoTeste);

            System.out.println(
                    "Ativo: INSERT realizado com sucesso."
            );

            System.out.println(
                    "\n--- ATIVO APOS INSERT ---"
            );

            for (Ativo a : ativoDao.listar()) {

                if (a.getId() == ID_TESTE) {

                    System.out.println(
                            a.getId()
                                    + " | carteira_id="
                                    + a.getCarteira().getId()
                                    + " | criptomoeda_id="
                                    + a.getCriptomoeda().getId()
                                    + " | quantidade="
                                    + a.getQuantidade()
                                    + " | precoMedio="
                                    + a.getPrecoMedio()
                                    + " | valorInvestido="
                                    + a.getValorInvestido()
                                    + " | valorAtual="
                                    + a.getValorAtual()
                    );
                }
            }

            ativoTeste.setQuantidade(
                    3.0
            );

            ativoTeste.setPrecoMedio(
                    550.00
            );

            ativoTeste.setValorInvestido(
                    1650.00
            );

            ativoTeste.setValorAtual(
                    1800.00
            );

            ativoDao.alterar(ativoTeste);

            System.out.println(
                    "Ativo: UPDATE realizado com sucesso."
            );

            System.out.println(
                    "\n--- ATIVO APOS UPDATE ---"
            );

            for (Ativo a : ativoDao.listar()) {

                if (a.getId() == ID_TESTE) {

                    System.out.println(
                            a.getId()
                                    + " | quantidade="
                                    + a.getQuantidade()
                                    + " | precoMedio="
                                    + a.getPrecoMedio()
                                    + " | valorInvestido="
                                    + a.getValorInvestido()
                                    + " | valorAtual="
                                    + a.getValorAtual()
                    );
                }
            }

            ativoDao.excluir(ID_TESTE);

            System.out.println(
                    "Ativo: DELETE realizado com sucesso."
            );

            carteiraDao.excluir(ID_TESTE);
            usuarioDao.excluir(ID_TESTE);
            criptomoedaDao.excluir(ID_TESTE);

        } catch (Exception e) {

            System.out.println(
                    "Erro ao testar CRUD de Ativo: "
                            + e.getMessage()
            );

            e.printStackTrace();

        } finally {

            if (ativoDao != null) {
                try {
                    ativoDao.fecharConexao();
                } catch (Exception e) {
                    System.out.println(
                            "Erro ao fechar conexao de Ativo: "
                                    + e.getMessage()
                    );
                }
            }

            if (carteiraDao != null) {
                try {
                    carteiraDao.fecharConexao();
                } catch (Exception e) {
                    System.out.println(
                            "Erro ao fechar conexao de Carteira: "
                                    + e.getMessage()
                    );
                }
            }

            if (usuarioDao != null) {
                try {
                    usuarioDao.fecharConexao();
                } catch (Exception e) {
                    System.out.println(
                            "Erro ao fechar conexao de Usuario: "
                                    + e.getMessage()
                    );
                }
            }

            if (criptomoedaDao != null) {
                try {
                    criptomoedaDao.fecharConexao();
                } catch (Exception e) {
                    System.out.println(
                            "Erro ao fechar conexao de Criptomoeda: "
                                    + e.getMessage()
                    );
                }
            }
        }
    }

    private static void testarCrudTransacaoAporte() {

        TransacaoDao transacaoDao = null;
        AporteDao aporteDao = null;
        UsuarioDao usuarioDao = null;
        CarteiraDao carteiraDao = null;
        CriptomoedaDao criptomoedaDao = null;

        final int ID_TESTE = 96;
        final int ID_VENDA_TESTE = 196;

        try {

            transacaoDao = new TransacaoDao();
            aporteDao = new AporteDao();
            usuarioDao = new UsuarioDao();
            carteiraDao = new CarteiraDao();
            criptomoedaDao = new CriptomoedaDao();

            System.out.println(
                    "\n=== TESTE CRUD TRANSACAO E APORTE NO BANCO ==="
            );

            transacaoDao.excluir(ID_VENDA_TESTE);
            transacaoDao.excluir(ID_TESTE);
            aporteDao.excluir(ID_TESTE);
            carteiraDao.excluir(ID_TESTE);
            usuarioDao.excluir(ID_TESTE);
            criptomoedaDao.excluir(ID_TESTE);

            Usuario usuarioTeste = new Usuario(
                    ID_TESTE,
                    "Usuario Teste Transacao",
                    "98765432196",
                    "(11) 96666-9696",
                    "transacao96@voltz.com",
                    "hash_transacao_96",
                    LocalDateTime.now()
            );

            usuarioDao.inserir(usuarioTeste);

            Carteira carteiraTeste = new Carteira(
                    ID_TESTE,
                    usuarioTeste,
                    3000.00,
                    3300.00,
                    300.00,
                    LocalDateTime.now()
            );

            carteiraDao.inserir(carteiraTeste);

            Criptomoeda criptomoedaTeste = new Criptomoeda(
                    ID_TESTE,
                    "Teste Transacao Coin",
                    "TTC",
                    "Criptomoeda temporaria para teste de Transacao"
            );

            criptomoedaDao.inserir(criptomoedaTeste);

            Aporte aporteTeste = new Aporte(
                    ID_TESTE,
                    carteiraTeste,
                    2500.00,
                    LocalDateTime.now(),
                    "Aporte temporario para teste"
            );

            aporteDao.inserir(aporteTeste);

            System.out.println(
                    "Aporte: INSERT realizado com sucesso."
            );

            Compra compraTeste = new Compra(
                    ID_TESTE,
                    carteiraTeste,
                    criptomoedaTeste,
                    2.0,
                    500.00,
                    LocalDateTime.now()
            );

            transacaoDao.inserir(compraTeste);

            Venda vendaTeste = new Venda(
                    ID_VENDA_TESTE,
                    carteiraTeste,
                    criptomoedaTeste,
                    0.5,
                    600.00,
                    LocalDateTime.now()
            );

            transacaoDao.inserir(vendaTeste);

            System.out.println(
                    "Transacao: INSERT de COMPRA e VENDA realizado com sucesso."
            );

            System.out.println(
                    "\n--- APORTE APOS INSERT ---"
            );

            for (Aporte aporte : aporteDao.listar()) {

                if (aporte.getId() == ID_TESTE) {

                    System.out.println(
                            aporte.getId()
                                    + " | carteira_id="
                                    + aporte.getCarteira().getId()
                                    + " | valor="
                                    + aporte.getValor()
                                    + " | descricao="
                                    + aporte.getDescricao()
                    );
                }
            }

            System.out.println(
                    "\n--- TRANSACOES APOS INSERT ---"
            );

            for (Transacao transacao : transacaoDao.listar()) {

                if (
                        transacao.getId() == ID_TESTE
                                || transacao.getId() == ID_VENDA_TESTE
                ) {

                    System.out.println(
                            transacao.getId()
                                    + " | carteira_id="
                                    + transacao.getCarteira().getId()
                                    + " | criptomoeda_id="
                                    + transacao.getCriptomoeda().getId()
                                    + " | tipo="
                                    + transacao.getTipo()
                                    + " | quantidade="
                                    + transacao.getQuantidade()
                                    + " | precoUnitario="
                                    + transacao.getPrecoUnitario()
                                    + " | valorTotal="
                                    + transacao.getValorTotal()
                    );
                }
            }

            aporteTeste.setValor(
                    3000.00
            );

            aporteTeste.setDataHora(
                    LocalDateTime.now()
            );

            aporteTeste.setDescricao(
                    "Aporte atualizado pelo Main"
            );

            aporteDao.alterar(aporteTeste);

            System.out.println(
                    "Aporte: UPDATE realizado com sucesso."
            );

            compraTeste.setQuantidade(
                    3.0
            );

            compraTeste.setPrecoUnitario(
                    550.00
            );

            compraTeste.setValorTotal(
                    compraTeste.calcularValorTotal()
            );

            compraTeste.setDataHora(
                    LocalDateTime.now()
            );

            transacaoDao.alterar(compraTeste);

            System.out.println(
                    "Transacao: UPDATE realizado com sucesso."
            );

            System.out.println(
                    "\n--- DADOS APOS UPDATE ---"
            );

            for (Aporte aporte : aporteDao.listar()) {

                if (aporte.getId() == ID_TESTE) {

                    System.out.println(
                            "Aporte atualizado: valor="
                                    + aporte.getValor()
                                    + " | descricao="
                                    + aporte.getDescricao()
                    );
                }
            }

            for (Transacao transacao : transacaoDao.listar()) {

                if (transacao.getId() == ID_TESTE) {

                    System.out.println(
                            "Transacao atualizada: tipo="
                                    + transacao.getTipo()
                                    + " | quantidade="
                                    + transacao.getQuantidade()
                                    + " | precoUnitario="
                                    + transacao.getPrecoUnitario()
                                    + " | valorTotal="
                                    + transacao.getValorTotal()
                    );
                }
            }

            transacaoDao.excluir(ID_VENDA_TESTE);
            transacaoDao.excluir(ID_TESTE);

            System.out.println(
                    "Transacao: DELETE realizado com sucesso."
            );

            aporteDao.excluir(ID_TESTE);

            System.out.println(
                    "Aporte: DELETE realizado com sucesso."
            );

            carteiraDao.excluir(ID_TESTE);
            usuarioDao.excluir(ID_TESTE);
            criptomoedaDao.excluir(ID_TESTE);

        } catch (Exception e) {

            System.out.println(
                    "Erro ao testar CRUD de Transacao/Aporte: "
                            + e.getMessage()
            );

            e.printStackTrace();

        } finally {

            if (transacaoDao != null) {
                try {
                    transacaoDao.fecharConexao();
                } catch (Exception e) {
                    System.out.println(
                            "Erro ao fechar conexao de Transacao: "
                                    + e.getMessage()
                    );
                }
            }

            if (aporteDao != null) {
                try {
                    aporteDao.fecharConexao();
                } catch (Exception e) {
                    System.out.println(
                            "Erro ao fechar conexao de Aporte: "
                                    + e.getMessage()
                    );
                }
            }

            if (carteiraDao != null) {
                try {
                    carteiraDao.fecharConexao();
                } catch (Exception e) {
                    System.out.println(
                            "Erro ao fechar conexao de Carteira: "
                                    + e.getMessage()
                    );
                }
            }

            if (usuarioDao != null) {
                try {
                    usuarioDao.fecharConexao();
                } catch (Exception e) {
                    System.out.println(
                            "Erro ao fechar conexao de Usuario: "
                                    + e.getMessage()
                    );
                }
            }

            if (criptomoedaDao != null) {
                try {
                    criptomoedaDao.fecharConexao();
                } catch (Exception e) {
                    System.out.println(
                            "Erro ao fechar conexao de Criptomoeda: "
                                    + e.getMessage()
                    );
                }
            }
        }
    }

    private static void testarCrudFavoritoCotacao() {

        FavoritoDao favoritoDao = null;
        CotacaoDao cotacaoDao = null;
        UsuarioDao usuarioDao = null;
        CriptomoedaDao criptomoedaDao = null;

        final int ID_TESTE = 95;

        try {

            favoritoDao = new FavoritoDao();
            cotacaoDao = new CotacaoDao();
            usuarioDao = new UsuarioDao();
            criptomoedaDao = new CriptomoedaDao();

            System.out.println(
                    "\n=== TESTE CRUD FAVORITO E COTACAO NO BANCO ==="
            );

            favoritoDao.excluir(ID_TESTE);
            cotacaoDao.excluir(ID_TESTE);
            usuarioDao.excluir(ID_TESTE);
            criptomoedaDao.excluir(ID_TESTE);

            Usuario usuarioTeste = new Usuario(
                    ID_TESTE,
                    "Usuario Teste Favorito",
                    "98765432195",
                    "(11) 95555-9595",
                    "favorito95@voltz.com",
                    "hash_favorito_95",
                    LocalDateTime.now()
            );

            usuarioDao.inserir(usuarioTeste);

            Criptomoeda criptomoedaTeste = new Criptomoeda(
                    ID_TESTE,
                    "Teste Favorito Coin",
                    "TFC",
                    "Criptomoeda temporaria para teste de Favorito e Cotacao"
            );

            criptomoedaDao.inserir(criptomoedaTeste);

            Favorito favoritoTeste = new Favorito(
                    ID_TESTE,
                    usuarioTeste,
                    criptomoedaTeste,
                    LocalDateTime.now()
            );

            favoritoDao.inserir(favoritoTeste);

            System.out.println(
                    "Favorito: INSERT realizado com sucesso."
            );

            Cotacao cotacaoTeste = new Cotacao(
                    ID_TESTE,
                    criptomoedaTeste,
                    150.00,
                    1.50,
                    -2.30,
                    8.75,
                    LocalDateTime.now()
            );

            cotacaoDao.inserir(cotacaoTeste);

            System.out.println(
                    "Cotacao: INSERT realizado com sucesso."
            );

            System.out.println(
                    "\n--- FAVORITO APOS INSERT ---"
            );

            for (Favorito favorito : favoritoDao.listar()) {

                if (favorito.getId() == ID_TESTE) {

                    System.out.println(
                            favorito.getId()
                                    + " | usuario_id="
                                    + favorito.getUsuario().getId()
                                    + " | criptomoeda_id="
                                    + favorito.getCriptomoeda().getId()
                                    + " | dataAdicionado="
                                    + favorito.getDataAdicionado()
                    );
                }
            }

            System.out.println(
                    "\n--- COTACAO APOS INSERT ---"
            );

            for (Cotacao cotacao : cotacaoDao.listar()) {

                if (cotacao.getId() == ID_TESTE) {

                    System.out.println(
                            cotacao.getId()
                                    + " | criptomoeda_id="
                                    + cotacao.getCriptomoeda().getId()
                                    + " | precoAtual="
                                    + cotacao.getPrecoAtual()
                                    + " | variacao24h="
                                    + cotacao.getVariacao24h()
                                    + " | variacao7d="
                                    + cotacao.getVariacao7d()
                                    + " | variacao30d="
                                    + cotacao.getVariacao30d()
                    );
                }
            }

            favoritoTeste.setDataAdicionado(
                    LocalDateTime.now().minusDays(1)
            );

            favoritoDao.alterar(favoritoTeste);

            System.out.println(
                    "Favorito: UPDATE realizado com sucesso."
            );

            cotacaoTeste.setPrecoAtual(
                    175.50
            );

            cotacaoTeste.setVariacao24h(
                    3.20
            );

            cotacaoTeste.setVariacao7d(
                    5.10
            );

            cotacaoTeste.setVariacao30d(
                    12.40
            );

            cotacaoTeste.setDataConsulta(
                    LocalDateTime.now()
            );

            cotacaoDao.alterar(cotacaoTeste);

            System.out.println(
                    "Cotacao: UPDATE realizado com sucesso."
            );

            System.out.println(
                    "\n--- DADOS APOS UPDATE ---"
            );

            for (Favorito favorito : favoritoDao.listar()) {

                if (favorito.getId() == ID_TESTE) {

                    System.out.println(
                            "Favorito atualizado: dataAdicionado="
                                    + favorito.getDataAdicionado()
                    );
                }
            }

            for (Cotacao cotacao : cotacaoDao.listar()) {

                if (cotacao.getId() == ID_TESTE) {

                    System.out.println(
                            "Cotacao atualizada: precoAtual="
                                    + cotacao.getPrecoAtual()
                                    + " | variacao24h="
                                    + cotacao.getVariacao24h()
                                    + " | variacao7d="
                                    + cotacao.getVariacao7d()
                                    + " | variacao30d="
                                    + cotacao.getVariacao30d()
                    );
                }
            }

            favoritoDao.excluir(ID_TESTE);

            System.out.println(
                    "Favorito: DELETE realizado com sucesso."
            );

            cotacaoDao.excluir(ID_TESTE);

            System.out.println(
                    "Cotacao: DELETE realizado com sucesso."
            );

            usuarioDao.excluir(ID_TESTE);
            criptomoedaDao.excluir(ID_TESTE);

        } catch (Exception e) {

            System.out.println(
                    "Erro ao testar CRUD de Favorito/Cotacao: "
                            + e.getMessage()
            );

            e.printStackTrace();

        } finally {

            if (favoritoDao != null) {
                try {
                    favoritoDao.fecharConexao();
                } catch (Exception e) {
                    System.out.println(
                            "Erro ao fechar conexao de Favorito: "
                                    + e.getMessage()
                    );
                }
            }

            if (cotacaoDao != null) {
                try {
                    cotacaoDao.fecharConexao();
                } catch (Exception e) {
                    System.out.println(
                            "Erro ao fechar conexao de Cotacao: "
                                    + e.getMessage()
                    );
                }
            }

            if (usuarioDao != null) {
                try {
                    usuarioDao.fecharConexao();
                } catch (Exception e) {
                    System.out.println(
                            "Erro ao fechar conexao de Usuario: "
                                    + e.getMessage()
                    );
                }
            }

            if (criptomoedaDao != null) {
                try {
                    criptomoedaDao.fecharConexao();
                } catch (Exception e) {
                    System.out.println(
                            "Erro ao fechar conexao de Criptomoeda: "
                                    + e.getMessage()
                    );
                }
            }
        }
    }
}
