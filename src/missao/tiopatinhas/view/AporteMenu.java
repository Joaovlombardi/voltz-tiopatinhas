package missao.tiopatinhas.view;

import missao.tiopatinhas.dao.AporteDao;
import missao.tiopatinhas.dao.CarteiraDao;
import missao.tiopatinhas.dao.UsuarioDao;
import missao.tiopatinhas.model.Aporte;
import missao.tiopatinhas.model.Carteira;
import missao.tiopatinhas.util.Console;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class AporteMenu extends MenuBase {

    private final AporteDao aporteDao;
    private final CarteiraDao carteiraDao;
    private final UsuarioDao usuarioDao;

    public AporteMenu(AporteDao aporteDao, CarteiraDao carteiraDao, UsuarioDao usuarioDao) {
        super("Aportes");

        this.aporteDao = aporteDao;
        this.carteiraDao = carteiraDao;
        this.usuarioDao = usuarioDao;
    }

    @Override
    protected void incluir() throws SQLException {

        System.out.println("--- Novo aporte ---");

        Carteira carteira = selecionarCarteira(carteiraDao, usuarioDao, null);

        if (carteira == null) {
            return;
        }

        double valor = Console.lerValor("Valor do aporte (R$)", null);
        String descricao = Console.lerTextoOpcional("Descricao", null, 200);

        Aporte aporte = new Aporte(
                aporteDao.proximoId(),
                carteira,
                valor,
                LocalDateTime.now(),
                descricao
        );

        aporteDao.inserir(aporte);

        Console.sucesso("Aporte de " + Console.moeda(valor)
                + " registrado com o ID " + aporte.getId() + ".");
    }

    @Override
    protected void alterar() throws SQLException {

        int id = Console.lerId("ID do aporte a alterar");
        Aporte aporte = aporteDao.buscarPorId(id);

        if (aporte == null) {
            Console.erro("Aporte nao encontrado.");
            return;
        }

        System.out.println("Pressione ENTER para manter o valor atual.");

        Carteira carteira = selecionarCarteira(carteiraDao, usuarioDao, aporte.getCarteira().getId());

        if (carteira == null) {
            return;
        }

        aporte.setCarteira(carteira);
        aporte.setValor(Console.lerValor("Valor do aporte (R$)", aporte.getValor()));
        aporte.setDescricao(Console.lerTextoOpcional("Descricao", aporte.getDescricao(), 200));

        if (aporteDao.alterar(aporte)) {
            Console.sucesso("Aporte alterado.");
        } else {
            Console.erro("Aporte nao encontrado.");
        }
    }

    @Override
    protected void excluir() throws SQLException {

        int id = Console.lerId("ID do aporte a excluir");
        Aporte aporte = aporteDao.buscarPorId(id);

        if (aporte == null) {
            Console.erro("Aporte nao encontrado.");
            return;
        }

        if (!Console.confirmar("Excluir o aporte " + id + " de "
                + Console.moeda(aporte.getValor()) + "?")) {
            Console.aviso("Exclusao cancelada.");
            return;
        }

        if (aporteDao.excluir(id)) {
            Console.sucesso("Aporte excluido.");
        } else {
            Console.erro("Aporte nao encontrado.");
        }
    }

    @Override
    protected void listarTodos() throws SQLException {

        List<Aporte> aportes = aporteDao.listar();

        if (aportes.isEmpty()) {
            Console.aviso("Nenhum aporte cadastrado.");
            return;
        }

        Map<Integer, String> carteiras = descricoesCarteiras(carteiraDao, usuarioDao);

        System.out.printf(
                "%-5s %-32s %18s %-16s %s%n",
                "ID", "CARTEIRA", "VALOR", "DATA/HORA", "DESCRICAO"
        );

        double total = 0;

        for (Aporte aporte : aportes) {
            System.out.printf(
                    "%-5d %-32s %18s %-16s %s%n",
                    aporte.getId(),
                    carteiras.getOrDefault(aporte.getCarteira().getId(), "?"),
                    Console.moeda(aporte.getValor()),
                    Console.data(aporte.getDataHora()),
                    Console.texto(aporte.getDescricao())
            );

            total += aporte.getValor();
        }

        System.out.println("Total: " + aportes.size() + " aporte(s), somando "
                + Console.moeda(total) + ".");
    }

    @Override
    protected void exibirUm() throws SQLException {

        int id = Console.lerId("ID do aporte");
        Aporte aporte = aporteDao.buscarPorId(id);

        if (aporte == null) {
            Console.erro("Aporte nao encontrado.");
            return;
        }

        System.out.println("ID:        " + aporte.getId());
        System.out.println("Carteira:  " + descricoesCarteiras(carteiraDao, usuarioDao)
                .getOrDefault(aporte.getCarteira().getId(), "?"));
        System.out.println("Valor:     " + Console.moeda(aporte.getValor()));
        System.out.println("Data/Hora: " + Console.data(aporte.getDataHora()));
        System.out.println("Descricao: " + Console.texto(aporte.getDescricao()));
    }
}
