package missao.tiopatinhas.view;

import missao.tiopatinhas.dao.AtivoDao;
import missao.tiopatinhas.dao.CarteiraDao;
import missao.tiopatinhas.dao.CotacaoDao;
import missao.tiopatinhas.dao.CriptomoedaDao;
import missao.tiopatinhas.dao.UsuarioDao;
import missao.tiopatinhas.model.Ativo;
import missao.tiopatinhas.model.Carteira;
import missao.tiopatinhas.model.Cotacao;
import missao.tiopatinhas.model.Criptomoeda;
import missao.tiopatinhas.util.Console;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class AtivoMenu extends MenuBase {

    private final AtivoDao ativoDao;
    private final CarteiraDao carteiraDao;
    private final UsuarioDao usuarioDao;
    private final CriptomoedaDao criptomoedaDao;
    private final CotacaoDao cotacaoDao;

    public AtivoMenu(
            AtivoDao ativoDao,
            CarteiraDao carteiraDao,
            UsuarioDao usuarioDao,
            CriptomoedaDao criptomoedaDao,
            CotacaoDao cotacaoDao
    ) {
        super("Ativos");

        this.ativoDao = ativoDao;
        this.carteiraDao = carteiraDao;
        this.usuarioDao = usuarioDao;
        this.criptomoedaDao = criptomoedaDao;
        this.cotacaoDao = cotacaoDao;
    }

    @Override
    protected String[] opcoesExtras() {
        return new String[]{"Atualizar valores atuais pelas ultimas cotacoes"};
    }

    @Override
    protected void executarExtra(int indice) throws SQLException {
        atualizarPelasCotacoes();
    }

    @Override
    protected void incluir() throws SQLException {

        System.out.println("--- Novo ativo ---");

        Carteira carteira = selecionarCarteira(carteiraDao, usuarioDao, null);

        if (carteira == null) {
            return;
        }

        Criptomoeda criptomoeda = selecionarCriptomoeda(criptomoedaDao, null);

        if (criptomoeda == null) {
            return;
        }

        if (jaExiste(carteira.getId(), criptomoeda.getId(), 0)) {
            Console.erro("Esta carteira ja possui um ativo de " + criptomoeda.getSimbolo()
                    + ". Altere o ativo existente em vez de criar outro.");
            return;
        }

        double quantidade = Console.lerQuantidade("Quantidade", null);
        double precoMedio = Console.lerValor("Preco medio de compra (R$)", null);

        Ativo ativo = new Ativo(
                ativoDao.proximoId(),
                carteira,
                criptomoeda,
                quantidade,
                precoMedio
        );

        ativo.calcularValorAtual(precoDeMercado(criptomoeda));

        if (foraDoLimite(ativo)) {
            return;
        }

        ativoDao.inserir(ativo);

        Console.sucesso("Ativo cadastrado com o ID " + ativo.getId() + ".");
        exibirValores(ativo);
    }

    @Override
    protected void alterar() throws SQLException {

        int id = Console.lerId("ID do ativo a alterar");
        Ativo ativo = ativoDao.buscarPorId(id);

        if (ativo == null) {
            Console.erro("Ativo nao encontrado.");
            return;
        }

        System.out.println("Pressione ENTER para manter o valor atual.");

        Carteira carteira = selecionarCarteira(carteiraDao, usuarioDao, ativo.getCarteira().getId());

        if (carteira == null) {
            return;
        }

        Criptomoeda criptomoeda = selecionarCriptomoeda(criptomoedaDao, ativo.getCriptomoeda().getId());

        if (criptomoeda == null) {
            return;
        }

        if (jaExiste(carteira.getId(), criptomoeda.getId(), id)) {
            Console.erro("Esta carteira ja possui outro ativo de " + criptomoeda.getSimbolo() + ".");
            return;
        }

        ativo.setCarteira(carteira);
        ativo.setCriptomoeda(criptomoeda);
        ativo.setQuantidade(Console.lerQuantidade("Quantidade", ativo.getQuantidade()));
        ativo.setPrecoMedio(Console.lerValor("Preco medio de compra (R$)", ativo.getPrecoMedio()));

        ativo.calcularValorInvestido();
        ativo.calcularValorAtual(precoDeMercado(criptomoeda));

        if (foraDoLimite(ativo)) {
            return;
        }

        if (ativoDao.alterar(ativo)) {
            Console.sucesso("Ativo alterado.");
            exibirValores(ativo);
        } else {
            Console.erro("Ativo nao encontrado.");
        }
    }

    @Override
    protected void excluir() throws SQLException {

        int id = Console.lerId("ID do ativo a excluir");
        Ativo ativo = ativoDao.buscarPorId(id);

        if (ativo == null) {
            Console.erro("Ativo nao encontrado.");
            return;
        }

        if (!Console.confirmar("Excluir o ativo " + id + " (quantidade "
                + Console.decimal(ativo.getQuantidade()) + ")?")) {
            Console.aviso("Exclusao cancelada.");
            return;
        }

        if (ativoDao.excluir(id)) {
            Console.sucesso("Ativo excluido.");
        } else {
            Console.erro("Ativo nao encontrado.");
        }
    }

    @Override
    protected void listarTodos() throws SQLException {

        List<Ativo> ativos = ativoDao.listar();

        if (ativos.isEmpty()) {
            Console.aviso("Nenhum ativo cadastrado.");
            return;
        }

        Map<Integer, String> carteiras = descricoesCarteiras(carteiraDao, usuarioDao);
        Map<Integer, String> simbolos = simbolosCriptomoedas(criptomoedaDao);

        System.out.printf(
                "%-5s %-32s %-8s %14s %16s %18s %18s%n",
                "ID", "CARTEIRA", "CRIPTO", "QUANTIDADE", "PRECO MEDIO", "INVESTIDO", "VALOR ATUAL"
        );

        for (Ativo ativo : ativos) {
            System.out.printf(
                    "%-5d %-32s %-8s %14s %16s %18s %18s%n",
                    ativo.getId(),
                    carteiras.getOrDefault(ativo.getCarteira().getId(), "?"),
                    simbolos.getOrDefault(ativo.getCriptomoeda().getId(), "?"),
                    Console.decimal(ativo.getQuantidade()),
                    Console.moeda(ativo.getPrecoMedio()),
                    Console.moeda(ativo.getValorInvestido()),
                    Console.moeda(ativo.getValorAtual())
            );
        }

        System.out.println("Total: " + ativos.size() + " ativo(s).");
    }

    @Override
    protected void exibirUm() throws SQLException {

        int id = Console.lerId("ID do ativo");
        Ativo ativo = ativoDao.buscarPorId(id);

        if (ativo == null) {
            Console.erro("Ativo nao encontrado.");
            return;
        }

        Criptomoeda criptomoeda = criptomoedaDao.buscarPorId(ativo.getCriptomoeda().getId());

        System.out.println("ID:              " + ativo.getId());
        System.out.println("Carteira:        " + descricoesCarteiras(carteiraDao, usuarioDao)
                .getOrDefault(ativo.getCarteira().getId(), "?"));
        System.out.println("Criptomoeda:     " + (criptomoeda != null
                ? criptomoeda.getNome() + " (" + criptomoeda.getSimbolo() + ")"
                : "?"));
        System.out.println("Quantidade:      " + Console.decimal(ativo.getQuantidade()));
        System.out.println("Preco medio:     " + Console.moeda(ativo.getPrecoMedio()));
        exibirValores(ativo);
    }

    /** Recalcula o valor atual de todos os ativos cuja criptomoeda tem cotacao cadastrada. */
    private void atualizarPelasCotacoes() throws SQLException {

        int atualizados = 0;
        int semCotacao = 0;

        for (Ativo ativo : ativoDao.listar()) {

            Cotacao cotacao = cotacaoDao.buscarUltimaPorCriptomoeda(ativo.getCriptomoeda().getId());

            if (cotacao == null) {
                semCotacao++;
                continue;
            }

            ativo.calcularValorAtual(cotacao.getPrecoAtual());
            ativoDao.alterar(ativo);
            atualizados++;
        }

        Console.sucesso(atualizados + " ativo(s) atualizado(s).");

        if (semCotacao > 0) {
            Console.aviso(semCotacao + " ativo(s) sem cotacao cadastrada ficaram como estavam.");
        }
    }

    /** Usa a ultima cotacao cadastrada; se nao houver, pergunta o preco ao usuario. */
    private double precoDeMercado(Criptomoeda criptomoeda) throws SQLException {

        Cotacao cotacao = cotacaoDao.buscarUltimaPorCriptomoeda(criptomoeda.getId());

        if (cotacao != null) {
            System.out.println("Valor atual calculado pela ultima cotacao de "
                    + criptomoeda.getSimbolo() + ": " + Console.moeda(cotacao.getPrecoAtual()));
            return cotacao.getPrecoAtual();
        }

        Console.aviso("Nao ha cotacao cadastrada para " + criptomoeda.getSimbolo() + ".");

        return Console.lerValor("Preco atual de mercado (R$)", null);
    }

    private boolean jaExiste(int carteiraId, int criptomoedaId, int idIgnorado) throws SQLException {

        return ativoDao.listar().stream()
                .anyMatch(a -> a.getId() != idIgnorado
                        && a.getCarteira().getId() == carteiraId
                        && a.getCriptomoeda().getId() == criptomoedaId);
    }

    private boolean foraDoLimite(Ativo ativo) {

        if (ativo.getValorInvestido() > Console.LIMITE_VALOR
                || ativo.getValorAtual() > Console.LIMITE_VALOR) {
            Console.erro("Quantidade x preco resulta em um valor acima do limite aceito pelo sistema.");
            return true;
        }

        return false;
    }

    private void exibirValores(Ativo ativo) {

        System.out.println("Valor investido: " + Console.moeda(ativo.getValorInvestido()));
        System.out.println("Valor atual:     " + Console.moeda(ativo.getValorAtual()));
        System.out.println("Lucro/Prejuizo:  "
                + Console.moeda(ativo.getValorAtual() - ativo.getValorInvestido()));
    }
}
