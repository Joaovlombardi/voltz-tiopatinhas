package missao.tiopatinhas.view;

import missao.tiopatinhas.dao.AporteDao;
import missao.tiopatinhas.dao.AtivoDao;
import missao.tiopatinhas.dao.CarteiraDao;
import missao.tiopatinhas.dao.TransacaoDao;
import missao.tiopatinhas.dao.UsuarioDao;
import missao.tiopatinhas.model.Aporte;
import missao.tiopatinhas.model.Ativo;
import missao.tiopatinhas.model.Carteira;
import missao.tiopatinhas.model.Usuario;
import missao.tiopatinhas.util.Console;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CarteiraMenu extends MenuBase {

    private final CarteiraDao carteiraDao;
    private final UsuarioDao usuarioDao;
    private final AtivoDao ativoDao;
    private final TransacaoDao transacaoDao;
    private final AporteDao aporteDao;

    public CarteiraMenu(
            CarteiraDao carteiraDao,
            UsuarioDao usuarioDao,
            AtivoDao ativoDao,
            TransacaoDao transacaoDao,
            AporteDao aporteDao
    ) {
        super("Carteiras");

        this.carteiraDao = carteiraDao;
        this.usuarioDao = usuarioDao;
        this.ativoDao = ativoDao;
        this.transacaoDao = transacaoDao;
        this.aporteDao = aporteDao;
    }

    @Override
    protected String[] opcoesExtras() {
        return new String[]{"Recalcular valores a partir dos ativos"};
    }

    @Override
    protected void executarExtra(int indice) throws SQLException {
        recalcular();
    }

    @Override
    protected void incluir() throws SQLException {

        System.out.println("--- Nova carteira ---");

        Usuario usuario = selecionarUsuario(usuarioDao, null);

        if (usuario == null) {
            return;
        }

        // A carteira nasce zerada; os valores vem dos ativos (opcao "Recalcular")
        Carteira carteira = new Carteira(
                carteiraDao.proximoId(),
                usuario,
                0,
                0,
                0,
                LocalDateTime.now()
        );

        carteiraDao.inserir(carteira);

        Console.sucesso("Carteira cadastrada com o ID " + carteira.getId()
                + " para " + usuario.getNome() + ".");
    }

    @Override
    protected void alterar() throws SQLException {

        int id = Console.lerId("ID da carteira a alterar");
        Carteira carteira = carteiraDao.buscarPorId(id);

        if (carteira == null) {
            Console.erro("Carteira nao encontrada.");
            return;
        }

        System.out.println("O unico dado editavel da carteira e o dono;"
                + " os valores sao calculados a partir dos ativos.");

        Usuario usuario = selecionarUsuario(usuarioDao, carteira.getUsuario().getId());

        if (usuario == null) {
            return;
        }

        carteira.setUsuario(usuario);
        carteira.setDataAtualizacao(LocalDateTime.now());

        if (carteiraDao.alterar(carteira)) {
            Console.sucesso("Carteira alterada. Dono: " + usuario.getNome() + ".");
        } else {
            Console.erro("Carteira nao encontrada.");
        }
    }

    @Override
    protected void excluir() throws SQLException {

        int id = Console.lerId("ID da carteira a excluir");
        Carteira carteira = carteiraDao.buscarPorId(id);

        if (carteira == null) {
            Console.erro("Carteira nao encontrada.");
            return;
        }

        long ativos = ativosDaCarteira(id).size();

        long transacoes = transacaoDao.listar().stream()
                .filter(t -> t.getCarteira().getId() == id)
                .count();

        long aportes = aportesDaCarteira(id).size();

        if (ativos + transacoes + aportes > 0) {
            Console.erro("Nao e possivel excluir a carteira " + id + ": existem "
                    + ativos + " ativo(s), " + transacoes + " transacao(oes) e "
                    + aportes + " aporte(s) vinculados. Exclua-os primeiro.");
            return;
        }

        if (!Console.confirmar("Excluir a carteira " + id + "?")) {
            Console.aviso("Exclusao cancelada.");
            return;
        }

        if (carteiraDao.excluir(id)) {
            Console.sucesso("Carteira excluida.");
        } else {
            Console.erro("Carteira nao encontrada.");
        }
    }

    @Override
    protected void listarTodos() throws SQLException {

        List<Carteira> carteiras = carteiraDao.listar();

        if (carteiras.isEmpty()) {
            Console.aviso("Nenhuma carteira cadastrada.");
            return;
        }

        Map<Integer, String> nomes = nomesUsuarios(usuarioDao);

        System.out.printf(
                "%-5s %-25s %18s %18s %18s%n",
                "ID", "DONO", "INVESTIDO", "VALOR ATUAL", "LUCRO/PREJUIZO"
        );

        for (Carteira carteira : carteiras) {
            System.out.printf(
                    "%-5d %-25s %18s %18s %18s%n",
                    carteira.getId(),
                    nomes.getOrDefault(carteira.getUsuario().getId(), "?"),
                    Console.moeda(carteira.getValorTotalInvestido()),
                    Console.moeda(carteira.getValorAtual()),
                    Console.moeda(carteira.getLucroPrejuizo())
            );
        }

        System.out.println("Total: " + carteiras.size() + " carteira(s).");
    }

    @Override
    protected void exibirUm() throws SQLException {

        int id = Console.lerId("ID da carteira");
        Carteira carteira = carteiraDao.buscarPorId(id);

        if (carteira == null) {
            Console.erro("Carteira nao encontrada.");
            return;
        }

        Usuario dono = usuarioDao.buscarPorId(carteira.getUsuario().getId());

        long transacoes = transacaoDao.listar().stream()
                .filter(t -> t.getCarteira().getId() == id)
                .count();

        double totalAportado = 0;

        for (Aporte aporte : aportesDaCarteira(id)) {
            totalAportado += aporte.getValor();
        }

        System.out.println("ID:               " + carteira.getId());
        System.out.println("Dono:             " + (dono != null ? dono.getNome() : "?"));
        System.out.println("Valor investido:  " + Console.moeda(carteira.getValorTotalInvestido()));
        System.out.println("Valor atual:      " + Console.moeda(carteira.getValorAtual()));
        System.out.println("Lucro/Prejuizo:   " + Console.moeda(carteira.getLucroPrejuizo()));
        System.out.println("Atualizada em:    " + Console.data(carteira.getDataAtualizacao()));
        System.out.println("Ativos:           " + ativosDaCarteira(id).size());
        System.out.println("Transacoes:       " + transacoes);
        System.out.println("Total aportado:   " + Console.moeda(totalAportado));
    }

    /** Soma os ativos da carteira e grava investido, valor atual e lucro/prejuizo. */
    private void recalcular() throws SQLException {

        int id = Console.lerId("ID da carteira a recalcular");
        Carteira carteira = carteiraDao.buscarPorId(id);

        if (carteira == null) {
            Console.erro("Carteira nao encontrada.");
            return;
        }

        carteira.setAtivos(ativosDaCarteira(id));
        carteira.calcularLucroPrejuizo();
        carteira.setDataAtualizacao(LocalDateTime.now());

        carteiraDao.alterar(carteira);

        Console.sucesso("Carteira recalculada com " + carteira.getAtivos().size() + " ativo(s).");
        System.out.println("Valor investido: " + Console.moeda(carteira.getValorTotalInvestido()));
        System.out.println("Valor atual:     " + Console.moeda(carteira.getValorAtual()));
        System.out.println("Lucro/Prejuizo:  " + Console.moeda(carteira.getLucroPrejuizo()));
    }

    private List<Ativo> ativosDaCarteira(int carteiraId) throws SQLException {

        List<Ativo> ativos = new ArrayList<>();

        for (Ativo ativo : ativoDao.listar()) {
            if (ativo.getCarteira().getId() == carteiraId) {
                ativos.add(ativo);
            }
        }

        return ativos;
    }

    private List<Aporte> aportesDaCarteira(int carteiraId) throws SQLException {

        List<Aporte> aportes = new ArrayList<>();

        for (Aporte aporte : aporteDao.listar()) {
            if (aporte.getCarteira().getId() == carteiraId) {
                aportes.add(aporte);
            }
        }

        return aportes;
    }
}
