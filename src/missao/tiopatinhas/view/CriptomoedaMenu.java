package missao.tiopatinhas.view;

import missao.tiopatinhas.dao.AtivoDao;
import missao.tiopatinhas.dao.CotacaoDao;
import missao.tiopatinhas.dao.CriptomoedaDao;
import missao.tiopatinhas.dao.FavoritoDao;
import missao.tiopatinhas.dao.TransacaoDao;
import missao.tiopatinhas.model.Cotacao;
import missao.tiopatinhas.model.Criptomoeda;
import missao.tiopatinhas.util.Console;
import missao.tiopatinhas.util.Validador;

import java.sql.SQLException;
import java.util.List;

public class CriptomoedaMenu extends MenuBase {

    private final CriptomoedaDao criptomoedaDao;
    private final AtivoDao ativoDao;
    private final TransacaoDao transacaoDao;
    private final FavoritoDao favoritoDao;
    private final CotacaoDao cotacaoDao;

    public CriptomoedaMenu(
            CriptomoedaDao criptomoedaDao,
            AtivoDao ativoDao,
            TransacaoDao transacaoDao,
            FavoritoDao favoritoDao,
            CotacaoDao cotacaoDao
    ) {
        super("Criptomoedas");

        this.criptomoedaDao = criptomoedaDao;
        this.ativoDao = ativoDao;
        this.transacaoDao = transacaoDao;
        this.favoritoDao = favoritoDao;
        this.cotacaoDao = cotacaoDao;
    }

    @Override
    protected void incluir() throws SQLException {

        System.out.println("--- Nova criptomoeda ---");

        List<Criptomoeda> existentes = criptomoedaDao.listar();

        String nome = Console.lerTexto("Nome", null, 100);
        String simbolo = lerSimbolo(null, 0, existentes);
        String descricao = Console.lerTextoOpcional("Descricao", null, 200);

        Criptomoeda criptomoeda = new Criptomoeda(
                criptomoedaDao.proximoId(),
                nome,
                simbolo,
                descricao
        );

        criptomoedaDao.inserir(criptomoeda);

        Console.sucesso("Criptomoeda cadastrada com o ID " + criptomoeda.getId() + ".");
    }

    @Override
    protected void alterar() throws SQLException {

        int id = Console.lerId("ID da criptomoeda a alterar");
        Criptomoeda criptomoeda = criptomoedaDao.buscarPorId(id);

        if (criptomoeda == null) {
            Console.erro("Criptomoeda nao encontrada.");
            return;
        }

        System.out.println("Pressione ENTER para manter o valor atual.");

        List<Criptomoeda> existentes = criptomoedaDao.listar();

        criptomoeda.setNome(Console.lerTexto("Nome", criptomoeda.getNome(), 100));
        criptomoeda.setSimbolo(lerSimbolo(criptomoeda.getSimbolo(), id, existentes));
        criptomoeda.setDescricao(Console.lerTextoOpcional("Descricao", criptomoeda.getDescricao(), 200));

        if (criptomoedaDao.alterar(criptomoeda)) {
            Console.sucesso("Criptomoeda alterada.");
        } else {
            Console.erro("Criptomoeda nao encontrada.");
        }
    }

    @Override
    protected void excluir() throws SQLException {

        int id = Console.lerId("ID da criptomoeda a excluir");
        Criptomoeda criptomoeda = criptomoedaDao.buscarPorId(id);

        if (criptomoeda == null) {
            Console.erro("Criptomoeda nao encontrada.");
            return;
        }

        long ativos = ativoDao.listar().stream()
                .filter(a -> a.getCriptomoeda().getId() == id)
                .count();

        long transacoes = transacaoDao.listar().stream()
                .filter(t -> t.getCriptomoeda().getId() == id)
                .count();

        long favoritos = favoritoDao.listar().stream()
                .filter(f -> f.getCriptomoeda().getId() == id)
                .count();

        long cotacoes = cotacaoDao.listar().stream()
                .filter(c -> c.getCriptomoeda().getId() == id)
                .count();

        if (ativos + transacoes + favoritos + cotacoes > 0) {
            Console.erro("Nao e possivel excluir " + criptomoeda.getNome() + ": existem "
                    + ativos + " ativo(s), " + transacoes + " transacao(oes), "
                    + favoritos + " favorito(s) e " + cotacoes
                    + " cotacao(oes) vinculados. Exclua-os primeiro.");
            return;
        }

        if (!Console.confirmar("Excluir a criptomoeda " + criptomoeda.getNome() + "?")) {
            Console.aviso("Exclusao cancelada.");
            return;
        }

        if (criptomoedaDao.excluir(id)) {
            Console.sucesso("Criptomoeda excluida.");
        } else {
            Console.erro("Criptomoeda nao encontrada.");
        }
    }

    @Override
    protected void listarTodos() throws SQLException {

        List<Criptomoeda> criptomoedas = criptomoedaDao.listar();

        if (criptomoedas.isEmpty()) {
            Console.aviso("Nenhuma criptomoeda cadastrada.");
            return;
        }

        System.out.printf("%-5s %-20s %-10s %s%n", "ID", "NOME", "SIMBOLO", "DESCRICAO");

        for (Criptomoeda criptomoeda : criptomoedas) {
            System.out.printf(
                    "%-5d %-20s %-10s %s%n",
                    criptomoeda.getId(),
                    criptomoeda.getNome(),
                    criptomoeda.getSimbolo(),
                    Console.texto(criptomoeda.getDescricao())
            );
        }

        System.out.println("Total: " + criptomoedas.size() + " criptomoeda(s).");
    }

    @Override
    protected void exibirUm() throws SQLException {

        int id = Console.lerId("ID da criptomoeda");
        Criptomoeda criptomoeda = criptomoedaDao.buscarPorId(id);

        if (criptomoeda == null) {
            Console.erro("Criptomoeda nao encontrada.");
            return;
        }

        Cotacao ultimaCotacao = cotacaoDao.buscarUltimaPorCriptomoeda(id);

        System.out.println("ID:             " + criptomoeda.getId());
        System.out.println("Nome:           " + criptomoeda.getNome());
        System.out.println("Simbolo:        " + criptomoeda.getSimbolo());
        System.out.println("Descricao:      " + Console.texto(criptomoeda.getDescricao()));
        System.out.println("Ultima cotacao: " + (ultimaCotacao != null
                ? Console.moeda(ultimaCotacao.getPrecoAtual())
                        + " em " + Console.data(ultimaCotacao.getDataConsulta())
                : "sem cotacao cadastrada"));
    }

    private String lerSimbolo(String atual, int idIgnorado, List<Criptomoeda> existentes) {

        while (true) {
            String entrada = Console.lerTexto("Simbolo (ex.: BTC)", atual, 10);

            // ENTER manteve o simbolo ja gravado: nao precisa validar de novo
            if (entrada.equals(atual)) {
                return atual;
            }

            String simbolo = entrada.toUpperCase();

            if (!Validador.simboloValido(simbolo)) {
                Console.erro("Simbolo invalido. Use de 2 a 10 letras ou numeros, sem espacos.");
                continue;
            }

            boolean duplicado = existentes.stream()
                    .anyMatch(c -> c.getId() != idIgnorado && simbolo.equalsIgnoreCase(c.getSimbolo()));

            if (duplicado) {
                Console.erro("Ja existe uma criptomoeda com este simbolo.");
                continue;
            }

            return simbolo;
        }
    }
}
