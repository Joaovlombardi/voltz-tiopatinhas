package missao.tiopatinhas.view;

import missao.tiopatinhas.dao.CarteiraDao;
import missao.tiopatinhas.dao.CriptomoedaDao;
import missao.tiopatinhas.dao.TransacaoDao;
import missao.tiopatinhas.dao.UsuarioDao;
import missao.tiopatinhas.model.Carteira;
import missao.tiopatinhas.model.Compra;
import missao.tiopatinhas.model.Criptomoeda;
import missao.tiopatinhas.model.Transacao;
import missao.tiopatinhas.model.Venda;
import missao.tiopatinhas.util.Console;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TransacaoMenu extends MenuBase {

    private static final String COMPRA = "COMPRA";
    private static final String VENDA = "VENDA";

    private final TransacaoDao transacaoDao;
    private final CarteiraDao carteiraDao;
    private final UsuarioDao usuarioDao;
    private final CriptomoedaDao criptomoedaDao;

    public TransacaoMenu(
            TransacaoDao transacaoDao,
            CarteiraDao carteiraDao,
            UsuarioDao usuarioDao,
            CriptomoedaDao criptomoedaDao
    ) {
        super("Transacoes");

        this.transacaoDao = transacaoDao;
        this.carteiraDao = carteiraDao;
        this.usuarioDao = usuarioDao;
        this.criptomoedaDao = criptomoedaDao;
    }

    @Override
    protected String[] opcoesExtras() {
        return new String[]{"Exibir transacoes de uma carteira"};
    }

    @Override
    protected void executarExtra(int indice) throws SQLException {
        listarPorCarteira();
    }

    @Override
    protected void incluir() throws SQLException {

        System.out.println("--- Nova transacao ---");

        Carteira carteira = selecionarCarteira(carteiraDao, usuarioDao, null);

        if (carteira == null) {
            return;
        }

        Criptomoeda criptomoeda = selecionarCriptomoeda(criptomoedaDao, null);

        if (criptomoeda == null) {
            return;
        }

        String tipo = lerTipo(null);
        double quantidade = Console.lerQuantidade("Quantidade", null);
        double precoUnitario = Console.lerValor("Preco unitario (R$)", null);

        Transacao transacao = criar(
                tipo,
                transacaoDao.proximoId(),
                carteira,
                criptomoeda,
                quantidade,
                precoUnitario,
                LocalDateTime.now()
        );

        if (foraDoLimite(transacao)) {
            return;
        }

        transacaoDao.inserir(transacao);

        Console.sucesso(tipo + " registrada com o ID " + transacao.getId()
                + ". Valor total: " + Console.moeda(transacao.getValorTotal()) + ".");
    }

    @Override
    protected void alterar() throws SQLException {

        int id = Console.lerId("ID da transacao a alterar");
        Transacao atual = transacaoDao.buscarPorId(id);

        if (atual == null) {
            Console.erro("Transacao nao encontrada.");
            return;
        }

        System.out.println("Pressione ENTER para manter o valor atual.");

        Carteira carteira = selecionarCarteira(carteiraDao, usuarioDao, atual.getCarteira().getId());

        if (carteira == null) {
            return;
        }

        Criptomoeda criptomoeda = selecionarCriptomoeda(criptomoedaDao, atual.getCriptomoeda().getId());

        if (criptomoeda == null) {
            return;
        }

        String tipo = lerTipo(atual.getTipo());
        double quantidade = Console.lerQuantidade("Quantidade", atual.getQuantidade());
        double precoUnitario = Console.lerValor("Preco unitario (R$)", atual.getPrecoUnitario());

        // Compra e Venda sao classes diferentes, entao o objeto e recriado com o tipo escolhido.
        // A data original da transacao e preservada.
        Transacao transacao = criar(
                tipo,
                id,
                carteira,
                criptomoeda,
                quantidade,
                precoUnitario,
                atual.getDataHora()
        );

        if (foraDoLimite(transacao)) {
            return;
        }

        if (transacaoDao.alterar(transacao)) {
            Console.sucesso("Transacao alterada. Valor total: "
                    + Console.moeda(transacao.getValorTotal()) + ".");
        } else {
            Console.erro("Transacao nao encontrada.");
        }
    }

    @Override
    protected void excluir() throws SQLException {

        int id = Console.lerId("ID da transacao a excluir");
        Transacao transacao = transacaoDao.buscarPorId(id);

        if (transacao == null) {
            Console.erro("Transacao nao encontrada.");
            return;
        }

        if (!Console.confirmar("Excluir a " + transacao.getTipo() + " " + id + " de "
                + Console.moeda(transacao.getValorTotal()) + "?")) {
            Console.aviso("Exclusao cancelada.");
            return;
        }

        if (transacaoDao.excluir(id)) {
            Console.sucesso("Transacao excluida.");
        } else {
            Console.erro("Transacao nao encontrada.");
        }
    }

    @Override
    protected void listarTodos() throws SQLException {
        exibirLista(transacaoDao.listar());
    }

    @Override
    protected void exibirUm() throws SQLException {

        int id = Console.lerId("ID da transacao");
        Transacao transacao = transacaoDao.buscarPorId(id);

        if (transacao == null) {
            Console.erro("Transacao nao encontrada.");
            return;
        }

        Criptomoeda criptomoeda = criptomoedaDao.buscarPorId(transacao.getCriptomoeda().getId());

        System.out.println("ID:             " + transacao.getId());
        System.out.println("Tipo:           " + transacao.getTipo());
        System.out.println("Carteira:       " + descricoesCarteiras(carteiraDao, usuarioDao)
                .getOrDefault(transacao.getCarteira().getId(), "?"));
        System.out.println("Criptomoeda:    " + (criptomoeda != null
                ? criptomoeda.getNome() + " (" + criptomoeda.getSimbolo() + ")"
                : "?"));
        System.out.println("Quantidade:     " + Console.decimal(transacao.getQuantidade()));
        System.out.println("Preco unitario: " + Console.moeda(transacao.getPrecoUnitario()));
        System.out.println("Valor total:    " + Console.moeda(transacao.getValorTotal()));
        System.out.println("Data/Hora:      " + Console.data(transacao.getDataHora()));
    }

    private void listarPorCarteira() throws SQLException {

        Carteira carteira = selecionarCarteira(carteiraDao, usuarioDao, null);

        if (carteira == null) {
            return;
        }

        List<Transacao> transacoes = new ArrayList<>();

        for (Transacao transacao : transacaoDao.listar()) {
            if (transacao.getCarteira().getId() == carteira.getId()) {
                transacoes.add(transacao);
            }
        }

        exibirLista(transacoes);
    }

    private void exibirLista(List<Transacao> transacoes) throws SQLException {

        if (transacoes.isEmpty()) {
            Console.aviso("Nenhuma transacao encontrada.");
            return;
        }

        Map<Integer, String> carteiras = descricoesCarteiras(carteiraDao, usuarioDao);
        Map<Integer, String> simbolos = simbolosCriptomoedas(criptomoedaDao);

        System.out.printf(
                "%-5s %-7s %-32s %-8s %14s %16s %18s %-16s%n",
                "ID", "TIPO", "CARTEIRA", "CRIPTO", "QUANTIDADE", "PRECO UNIT.", "VALOR TOTAL", "DATA/HORA"
        );

        for (Transacao transacao : transacoes) {
            System.out.printf(
                    "%-5d %-7s %-32s %-8s %14s %16s %18s %-16s%n",
                    transacao.getId(),
                    transacao.getTipo(),
                    carteiras.getOrDefault(transacao.getCarteira().getId(), "?"),
                    simbolos.getOrDefault(transacao.getCriptomoeda().getId(), "?"),
                    Console.decimal(transacao.getQuantidade()),
                    Console.moeda(transacao.getPrecoUnitario()),
                    Console.moeda(transacao.getValorTotal()),
                    Console.data(transacao.getDataHora())
            );
        }

        System.out.println("Total: " + transacoes.size() + " transacao(oes).");
    }

    private String lerTipo(String atual) {

        String rotulo = atual != null
                ? "Tipo (1 - Compra, 2 - Venda) [" + atual + "]: "
                : "Tipo (1 - Compra, 2 - Venda): ";

        while (true) {
            String entrada = Console.lerLinha(rotulo);

            if (entrada.isEmpty() && atual != null) {
                return atual;
            }

            if (entrada.equals("1")) {
                return COMPRA;
            }

            if (entrada.equals("2")) {
                return VENDA;
            }

            Console.erro("Tipo invalido. Digite 1 para Compra ou 2 para Venda.");
        }
    }

    private Transacao criar(
            String tipo,
            int id,
            Carteira carteira,
            Criptomoeda criptomoeda,
            double quantidade,
            double precoUnitario,
            LocalDateTime dataHora
    ) {

        if (COMPRA.equals(tipo)) {
            return new Compra(id, carteira, criptomoeda, quantidade, precoUnitario, dataHora);
        }

        return new Venda(id, carteira, criptomoeda, quantidade, precoUnitario, dataHora);
    }

    private boolean foraDoLimite(Transacao transacao) {

        if (transacao.getValorTotal() > Console.LIMITE_VALOR) {
            Console.erro("Quantidade x preco resulta em um valor acima do limite aceito pelo sistema.");
            return true;
        }

        return false;
    }
}
