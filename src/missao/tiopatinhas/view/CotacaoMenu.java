package missao.tiopatinhas.view;

import missao.tiopatinhas.dao.CotacaoDao;
import missao.tiopatinhas.dao.CriptomoedaDao;
import missao.tiopatinhas.model.Cotacao;
import missao.tiopatinhas.model.Criptomoeda;
import missao.tiopatinhas.util.Console;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class CotacaoMenu extends MenuBase {

    private final CotacaoDao cotacaoDao;
    private final CriptomoedaDao criptomoedaDao;

    public CotacaoMenu(CotacaoDao cotacaoDao, CriptomoedaDao criptomoedaDao) {
        super("Cotacoes");

        this.cotacaoDao = cotacaoDao;
        this.criptomoedaDao = criptomoedaDao;
    }

    @Override
    protected void incluir() throws SQLException {

        System.out.println("--- Nova cotacao ---");

        Criptomoeda criptomoeda = selecionarCriptomoeda(criptomoedaDao, null);

        if (criptomoeda == null) {
            return;
        }

        Cotacao cotacao = new Cotacao(
                cotacaoDao.proximoId(),
                criptomoeda,
                Console.lerValor("Preco atual (R$)", null),
                Console.lerVariacao("Variacao em 24h (%)", null),
                Console.lerVariacao("Variacao em 7 dias (%)", null),
                Console.lerVariacao("Variacao em 30 dias (%)", null),
                LocalDateTime.now()
        );

        cotacaoDao.inserir(cotacao);

        Console.sucesso("Cotacao de " + criptomoeda.getSimbolo()
                + " cadastrada com o ID " + cotacao.getId() + ".");
    }

    @Override
    protected void alterar() throws SQLException {

        int id = Console.lerId("ID da cotacao a alterar");
        Cotacao cotacao = cotacaoDao.buscarPorId(id);

        if (cotacao == null) {
            Console.erro("Cotacao nao encontrada.");
            return;
        }

        System.out.println("Pressione ENTER para manter o valor atual.");

        Criptomoeda criptomoeda = selecionarCriptomoeda(criptomoedaDao, cotacao.getCriptomoeda().getId());

        if (criptomoeda == null) {
            return;
        }

        cotacao.setCriptomoeda(criptomoeda);
        cotacao.setPrecoAtual(Console.lerValor("Preco atual (R$)", cotacao.getPrecoAtual()));
        cotacao.setVariacao24h(Console.lerVariacao("Variacao em 24h (%)", cotacao.getVariacao24h()));
        cotacao.setVariacao7d(Console.lerVariacao("Variacao em 7 dias (%)", cotacao.getVariacao7d()));
        cotacao.setVariacao30d(Console.lerVariacao("Variacao em 30 dias (%)", cotacao.getVariacao30d()));
        cotacao.setDataConsulta(LocalDateTime.now());

        if (cotacaoDao.alterar(cotacao)) {
            Console.sucesso("Cotacao alterada.");
        } else {
            Console.erro("Cotacao nao encontrada.");
        }
    }

    @Override
    protected void excluir() throws SQLException {

        int id = Console.lerId("ID da cotacao a excluir");
        Cotacao cotacao = cotacaoDao.buscarPorId(id);

        if (cotacao == null) {
            Console.erro("Cotacao nao encontrada.");
            return;
        }

        if (!Console.confirmar("Excluir a cotacao " + id + " de "
                + Console.moeda(cotacao.getPrecoAtual()) + "?")) {
            Console.aviso("Exclusao cancelada.");
            return;
        }

        if (cotacaoDao.excluir(id)) {
            Console.sucesso("Cotacao excluida.");
        } else {
            Console.erro("Cotacao nao encontrada.");
        }
    }

    @Override
    protected void listarTodos() throws SQLException {

        List<Cotacao> cotacoes = cotacaoDao.listar();

        if (cotacoes.isEmpty()) {
            Console.aviso("Nenhuma cotacao cadastrada.");
            return;
        }

        Map<Integer, String> simbolos = simbolosCriptomoedas(criptomoedaDao);

        System.out.printf(
                "%-5s %-8s %18s %10s %10s %10s %-16s%n",
                "ID", "CRIPTO", "PRECO ATUAL", "24H", "7 DIAS", "30 DIAS", "CONSULTA"
        );

        for (Cotacao cotacao : cotacoes) {
            System.out.printf(
                    "%-5d %-8s %18s %10s %10s %10s %-16s%n",
                    cotacao.getId(),
                    simbolos.getOrDefault(cotacao.getCriptomoeda().getId(), "?"),
                    Console.moeda(cotacao.getPrecoAtual()),
                    Console.percentual(cotacao.getVariacao24h()),
                    Console.percentual(cotacao.getVariacao7d()),
                    Console.percentual(cotacao.getVariacao30d()),
                    Console.data(cotacao.getDataConsulta())
            );
        }

        System.out.println("Total: " + cotacoes.size() + " cotacao(oes).");
    }

    @Override
    protected void exibirUm() throws SQLException {

        int id = Console.lerId("ID da cotacao");
        Cotacao cotacao = cotacaoDao.buscarPorId(id);

        if (cotacao == null) {
            Console.erro("Cotacao nao encontrada.");
            return;
        }

        Criptomoeda criptomoeda = criptomoedaDao.buscarPorId(cotacao.getCriptomoeda().getId());

        System.out.println("ID:               " + cotacao.getId());
        System.out.println("Criptomoeda:      " + (criptomoeda != null
                ? criptomoeda.getNome() + " (" + criptomoeda.getSimbolo() + ")"
                : "?"));
        System.out.println("Preco atual:      " + Console.moeda(cotacao.getPrecoAtual()));
        System.out.println("Variacao 24h:     " + Console.percentual(cotacao.getVariacao24h()));
        System.out.println("Variacao 7 dias:  " + Console.percentual(cotacao.getVariacao7d()));
        System.out.println("Variacao 30 dias: " + Console.percentual(cotacao.getVariacao30d()));
        System.out.println("Consultada em:    " + Console.data(cotacao.getDataConsulta()));
    }
}
