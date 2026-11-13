package missao.tiopatinhas;

import missao.tiopatinhas.dao.AporteDao;
import missao.tiopatinhas.dao.AtivoDao;
import missao.tiopatinhas.dao.CarteiraDao;
import missao.tiopatinhas.dao.CotacaoDao;
import missao.tiopatinhas.dao.CriptomoedaDao;
import missao.tiopatinhas.dao.FavoritoDao;
import missao.tiopatinhas.dao.TransacaoDao;
import missao.tiopatinhas.dao.UsuarioDao;
import missao.tiopatinhas.factory.ConnectionFactory;
import missao.tiopatinhas.util.Console;
import missao.tiopatinhas.view.AporteMenu;
import missao.tiopatinhas.view.AtivoMenu;
import missao.tiopatinhas.view.CarteiraMenu;
import missao.tiopatinhas.view.CotacaoMenu;
import missao.tiopatinhas.view.CriptomoedaMenu;
import missao.tiopatinhas.view.FavoritoMenu;
import missao.tiopatinhas.view.MenuBase;
import missao.tiopatinhas.view.TransacaoMenu;
import missao.tiopatinhas.view.UsuarioMenu;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Programa principal do VOLTZ - Missao Tio Patinhas.
 * Abre a conexao com o Oracle e exibe o menu com os modulos do sistema.
 */
public class Main {

    public static void main(String[] args) {

        System.out.println("==========================================");
        System.out.println("      VOLTZ - MISSAO TIO PATINHAS");
        System.out.println("  Gerenciador de carteira de criptomoedas");
        System.out.println("==========================================");

        // A mesma conexao e usada por todos os DAOs e fechada ao sair do programa
        try (Connection conexao = ConnectionFactory.getConnection()) {

            System.out.println("Conectado ao banco de dados.");

            menuPrincipal(criarModulos(conexao));

            System.out.println("\nPrograma encerrado. Ate logo!");

        } catch (SQLException e) {
            Console.erro("Nao foi possivel usar o banco de dados: " + e.getMessage());

        } catch (NoSuchElementException e) {
            // A entrada do teclado foi fechada (ex.: Ctrl+D / Ctrl+Z)
            System.out.println("\nEntrada encerrada. Programa finalizado.");
        }
    }

    private static List<MenuBase> criarModulos(Connection conexao) {

        UsuarioDao usuarioDao = new UsuarioDao(conexao);
        CarteiraDao carteiraDao = new CarteiraDao(conexao);
        CriptomoedaDao criptomoedaDao = new CriptomoedaDao(conexao);
        AtivoDao ativoDao = new AtivoDao(conexao);
        TransacaoDao transacaoDao = new TransacaoDao(conexao);
        AporteDao aporteDao = new AporteDao(conexao);
        FavoritoDao favoritoDao = new FavoritoDao(conexao);
        CotacaoDao cotacaoDao = new CotacaoDao(conexao);

        List<MenuBase> modulos = new ArrayList<>();

        modulos.add(new UsuarioMenu(usuarioDao, carteiraDao, favoritoDao));
        modulos.add(new CarteiraMenu(carteiraDao, usuarioDao, ativoDao, transacaoDao, aporteDao));
        modulos.add(new CriptomoedaMenu(criptomoedaDao, ativoDao, transacaoDao, favoritoDao, cotacaoDao));
        modulos.add(new AtivoMenu(ativoDao, carteiraDao, usuarioDao, criptomoedaDao, cotacaoDao));
        modulos.add(new TransacaoMenu(transacaoDao, carteiraDao, usuarioDao, criptomoedaDao));
        modulos.add(new AporteMenu(aporteDao, carteiraDao, usuarioDao));
        modulos.add(new FavoritoMenu(favoritoDao, usuarioDao, criptomoedaDao));
        modulos.add(new CotacaoMenu(cotacaoDao, criptomoedaDao));

        return modulos;
    }

    private static void menuPrincipal(List<MenuBase> modulos) {

        while (true) {

            Console.titulo("Menu principal");

            for (int i = 0; i < modulos.size(); i++) {
                System.out.println((i + 1) + " - " + modulos.get(i).getTitulo());
            }

            System.out.println("0 - Sair");

            int opcao = Console.lerOpcao("Escolha um modulo: ", 0, modulos.size());

            if (opcao == 0) {
                return;
            }

            modulos.get(opcao - 1).exibir();
        }
    }
}
