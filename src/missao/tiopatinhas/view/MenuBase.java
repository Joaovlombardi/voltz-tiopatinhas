package missao.tiopatinhas.view;

import missao.tiopatinhas.dao.CarteiraDao;
import missao.tiopatinhas.dao.CriptomoedaDao;
import missao.tiopatinhas.dao.UsuarioDao;
import missao.tiopatinhas.model.Carteira;
import missao.tiopatinhas.model.Criptomoeda;
import missao.tiopatinhas.model.Usuario;
import missao.tiopatinhas.util.Console;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Base de todos os submenus do sistema. Define o laco do submenu com as cinco
 * operacoes obrigatorias; cada modulo implementa apenas o que cada operacao faz.
 */
public abstract class MenuBase {

    private static final int OPCOES_PADRAO = 5;

    private final String titulo;

    protected MenuBase(String titulo) {
        this.titulo = titulo;
    }

    public String getTitulo() {
        return titulo;
    }

    protected abstract void incluir() throws SQLException;

    protected abstract void alterar() throws SQLException;

    protected abstract void excluir() throws SQLException;

    protected abstract void listarTodos() throws SQLException;

    protected abstract void exibirUm() throws SQLException;

    /** Opcoes alem das cinco padrao. Por padrao, nenhuma. */
    protected String[] opcoesExtras() {
        return new String[0];
    }

    /** Executa a opcao extra de indice informado (0 e a primeira). */
    protected void executarExtra(int indice) throws SQLException {
    }

    public void exibir() {

        String[] extras = opcoesExtras();

        while (true) {

            Console.titulo(titulo);
            System.out.println("1 - Incluir");
            System.out.println("2 - Alterar");
            System.out.println("3 - Excluir");
            System.out.println("4 - Exibir todos");
            System.out.println("5 - Exibir um registro");

            for (int i = 0; i < extras.length; i++) {
                System.out.println((OPCOES_PADRAO + 1 + i) + " - " + extras[i]);
            }

            System.out.println("0 - Voltar");

            int opcao = Console.lerOpcao("Escolha uma opcao: ", 0, OPCOES_PADRAO + extras.length);

            if (opcao == 0) {
                return;
            }

            System.out.println();

            try {
                switch (opcao) {
                    case 1 -> incluir();
                    case 2 -> alterar();
                    case 3 -> excluir();
                    case 4 -> listarTodos();
                    case 5 -> exibirUm();
                    default -> executarExtra(opcao - OPCOES_PADRAO - 1);
                }
            } catch (SQLException e) {
                Console.erro(traduzirErro(e));
            }
        }
    }

    /** Converte os erros mais comuns do Oracle em mensagens que o usuario entende. */
    private static String traduzirErro(SQLException e) {

        return switch (e.getErrorCode()) {
            case 1 -> "Ja existe um registro com esses dados (valor duplicado).";
            case 1438, 12899 -> "Valor maior do que o permitido pelo banco de dados.";
            case 2290 -> "O valor informado nao respeita as regras do banco de dados.";
            case 2291 -> "O registro relacionado nao existe mais.";
            case 2292 -> "Nao e possivel excluir: existem registros vinculados a este.";
            case 17002, 17008 -> "A conexao com o banco foi perdida. Reinicie o programa.";
            default -> "Erro no banco de dados: " + e.getMessage();
        };
    }

    // ================= APOIO PARA CHAVES ESTRANGEIRAS =================

    protected static Map<Integer, String> nomesUsuarios(UsuarioDao usuarioDao) throws SQLException {

        Map<Integer, String> nomes = new HashMap<>();

        for (Usuario usuario : usuarioDao.listar()) {
            nomes.put(usuario.getId(), usuario.getNome());
        }

        return nomes;
    }

    protected static Map<Integer, String> simbolosCriptomoedas(CriptomoedaDao criptomoedaDao) throws SQLException {

        Map<Integer, String> simbolos = new HashMap<>();

        for (Criptomoeda criptomoeda : criptomoedaDao.listar()) {
            simbolos.put(criptomoeda.getId(), criptomoeda.getSimbolo());
        }

        return simbolos;
    }

    /** Descricao de cada carteira no formato "Carteira 1 (Miguel Monteiro)". */
    protected static Map<Integer, String> descricoesCarteiras(
            CarteiraDao carteiraDao,
            UsuarioDao usuarioDao
    ) throws SQLException {

        Map<Integer, String> nomes = nomesUsuarios(usuarioDao);
        Map<Integer, String> descricoes = new HashMap<>();

        for (Carteira carteira : carteiraDao.listar()) {
            descricoes.put(
                    carteira.getId(),
                    "Carteira " + carteira.getId() + " ("
                            + nomes.getOrDefault(carteira.getUsuario().getId(), "?") + ")"
            );
        }

        return descricoes;
    }

    /**
     * Mostra os usuarios cadastrados e pede o ID de um deles.
     * Devolve null se nao houver usuarios ou se a operacao for cancelada.
     */
    protected static Usuario selecionarUsuario(UsuarioDao usuarioDao, Integer idAtual) throws SQLException {

        List<Usuario> usuarios = usuarioDao.listar();

        if (usuarios.isEmpty()) {
            Console.erro("Nenhum usuario cadastrado. Cadastre um usuario primeiro.");
            return null;
        }

        System.out.println("Usuarios disponiveis:");

        for (Usuario usuario : usuarios) {
            System.out.println("  " + usuario.getId() + " - " + usuario.getNome());
        }

        while (true) {
            int id = Console.lerIdOuCancelar("ID do usuario", idAtual);

            if (id == 0) {
                Console.aviso("Operacao cancelada.");
                return null;
            }

            for (Usuario usuario : usuarios) {
                if (usuario.getId() == id) {
                    return usuario;
                }
            }

            Console.erro("Usuario nao encontrado. Escolha um ID da lista.");
        }
    }

    /**
     * Mostra as carteiras cadastradas e pede o ID de uma delas.
     * Devolve null se nao houver carteiras ou se a operacao for cancelada.
     */
    protected static Carteira selecionarCarteira(
            CarteiraDao carteiraDao,
            UsuarioDao usuarioDao,
            Integer idAtual
    ) throws SQLException {

        List<Carteira> carteiras = carteiraDao.listar();

        if (carteiras.isEmpty()) {
            Console.erro("Nenhuma carteira cadastrada. Cadastre uma carteira primeiro.");
            return null;
        }

        Map<Integer, String> nomes = nomesUsuarios(usuarioDao);

        System.out.println("Carteiras disponiveis:");

        for (Carteira carteira : carteiras) {
            System.out.println("  " + carteira.getId() + " - Carteira de "
                    + nomes.getOrDefault(carteira.getUsuario().getId(), "?"));
        }

        while (true) {
            int id = Console.lerIdOuCancelar("ID da carteira", idAtual);

            if (id == 0) {
                Console.aviso("Operacao cancelada.");
                return null;
            }

            for (Carteira carteira : carteiras) {
                if (carteira.getId() == id) {
                    return carteira;
                }
            }

            Console.erro("Carteira nao encontrada. Escolha um ID da lista.");
        }
    }

    /**
     * Mostra as criptomoedas cadastradas e pede o ID de uma delas.
     * Devolve null se nao houver criptomoedas ou se a operacao for cancelada.
     */
    protected static Criptomoeda selecionarCriptomoeda(
            CriptomoedaDao criptomoedaDao,
            Integer idAtual
    ) throws SQLException {

        List<Criptomoeda> criptomoedas = criptomoedaDao.listar();

        if (criptomoedas.isEmpty()) {
            Console.erro("Nenhuma criptomoeda cadastrada. Cadastre uma criptomoeda primeiro.");
            return null;
        }

        System.out.println("Criptomoedas disponiveis:");

        for (Criptomoeda criptomoeda : criptomoedas) {
            System.out.println("  " + criptomoeda.getId() + " - "
                    + criptomoeda.getNome() + " (" + criptomoeda.getSimbolo() + ")");
        }

        while (true) {
            int id = Console.lerIdOuCancelar("ID da criptomoeda", idAtual);

            if (id == 0) {
                Console.aviso("Operacao cancelada.");
                return null;
            }

            for (Criptomoeda criptomoeda : criptomoedas) {
                if (criptomoeda.getId() == id) {
                    return criptomoeda;
                }
            }

            Console.erro("Criptomoeda nao encontrada. Escolha um ID da lista.");
        }
    }
}
