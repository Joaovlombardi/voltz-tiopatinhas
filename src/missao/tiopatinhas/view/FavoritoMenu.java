package missao.tiopatinhas.view;

import missao.tiopatinhas.dao.CriptomoedaDao;
import missao.tiopatinhas.dao.FavoritoDao;
import missao.tiopatinhas.dao.UsuarioDao;
import missao.tiopatinhas.model.Criptomoeda;
import missao.tiopatinhas.model.Favorito;
import missao.tiopatinhas.model.Usuario;
import missao.tiopatinhas.util.Console;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class FavoritoMenu extends MenuBase {

    private final FavoritoDao favoritoDao;
    private final UsuarioDao usuarioDao;
    private final CriptomoedaDao criptomoedaDao;

    public FavoritoMenu(FavoritoDao favoritoDao, UsuarioDao usuarioDao, CriptomoedaDao criptomoedaDao) {
        super("Favoritos");

        this.favoritoDao = favoritoDao;
        this.usuarioDao = usuarioDao;
        this.criptomoedaDao = criptomoedaDao;
    }

    @Override
    protected void incluir() throws SQLException {

        System.out.println("--- Novo favorito ---");

        Usuario usuario = selecionarUsuario(usuarioDao, null);

        if (usuario == null) {
            return;
        }

        Criptomoeda criptomoeda = selecionarCriptomoeda(criptomoedaDao, null);

        if (criptomoeda == null) {
            return;
        }

        if (jaExiste(usuario.getId(), criptomoeda.getId(), 0)) {
            Console.erro(usuario.getNome() + " ja favoritou " + criptomoeda.getNome() + ".");
            return;
        }

        Favorito favorito = new Favorito(
                favoritoDao.proximoId(),
                usuario,
                criptomoeda,
                LocalDateTime.now()
        );

        favoritoDao.inserir(favorito);

        Console.sucesso(criptomoeda.getNome() + " adicionada aos favoritos de "
                + usuario.getNome() + " (ID " + favorito.getId() + ").");
    }

    @Override
    protected void alterar() throws SQLException {

        int id = Console.lerId("ID do favorito a alterar");
        Favorito favorito = favoritoDao.buscarPorId(id);

        if (favorito == null) {
            Console.erro("Favorito nao encontrado.");
            return;
        }

        System.out.println("Pressione ENTER para manter o valor atual.");

        Usuario usuario = selecionarUsuario(usuarioDao, favorito.getUsuario().getId());

        if (usuario == null) {
            return;
        }

        Criptomoeda criptomoeda = selecionarCriptomoeda(criptomoedaDao, favorito.getCriptomoeda().getId());

        if (criptomoeda == null) {
            return;
        }

        if (jaExiste(usuario.getId(), criptomoeda.getId(), id)) {
            Console.erro(usuario.getNome() + " ja favoritou " + criptomoeda.getNome() + ".");
            return;
        }

        favorito.setUsuario(usuario);
        favorito.setCriptomoeda(criptomoeda);

        if (favoritoDao.alterar(favorito)) {
            Console.sucesso("Favorito alterado.");
        } else {
            Console.erro("Favorito nao encontrado.");
        }
    }

    @Override
    protected void excluir() throws SQLException {

        int id = Console.lerId("ID do favorito a excluir");
        Favorito favorito = favoritoDao.buscarPorId(id);

        if (favorito == null) {
            Console.erro("Favorito nao encontrado.");
            return;
        }

        if (!Console.confirmar("Excluir o favorito " + id + "?")) {
            Console.aviso("Exclusao cancelada.");
            return;
        }

        if (favoritoDao.excluir(id)) {
            Console.sucesso("Favorito excluido.");
        } else {
            Console.erro("Favorito nao encontrado.");
        }
    }

    @Override
    protected void listarTodos() throws SQLException {

        List<Favorito> favoritos = favoritoDao.listar();

        if (favoritos.isEmpty()) {
            Console.aviso("Nenhum favorito cadastrado.");
            return;
        }

        Map<Integer, String> nomes = nomesUsuarios(usuarioDao);
        Map<Integer, String> simbolos = simbolosCriptomoedas(criptomoedaDao);

        System.out.printf("%-5s %-30s %-10s %-16s%n", "ID", "USUARIO", "CRIPTO", "ADICIONADO EM");

        for (Favorito favorito : favoritos) {
            System.out.printf(
                    "%-5d %-30s %-10s %-16s%n",
                    favorito.getId(),
                    nomes.getOrDefault(favorito.getUsuario().getId(), "?"),
                    simbolos.getOrDefault(favorito.getCriptomoeda().getId(), "?"),
                    Console.data(favorito.getDataAdicionado())
            );
        }

        System.out.println("Total: " + favoritos.size() + " favorito(s).");
    }

    @Override
    protected void exibirUm() throws SQLException {

        int id = Console.lerId("ID do favorito");
        Favorito favorito = favoritoDao.buscarPorId(id);

        if (favorito == null) {
            Console.erro("Favorito nao encontrado.");
            return;
        }

        Usuario usuario = usuarioDao.buscarPorId(favorito.getUsuario().getId());
        Criptomoeda criptomoeda = criptomoedaDao.buscarPorId(favorito.getCriptomoeda().getId());

        System.out.println("ID:            " + favorito.getId());
        System.out.println("Usuario:       " + (usuario != null ? usuario.getNome() : "?"));
        System.out.println("Criptomoeda:   " + (criptomoeda != null
                ? criptomoeda.getNome() + " (" + criptomoeda.getSimbolo() + ")"
                : "?"));
        System.out.println("Adicionado em: " + Console.data(favorito.getDataAdicionado()));
    }

    private boolean jaExiste(int usuarioId, int criptomoedaId, int idIgnorado) throws SQLException {

        return favoritoDao.listar().stream()
                .anyMatch(f -> f.getId() != idIgnorado
                        && f.getUsuario().getId() == usuarioId
                        && f.getCriptomoeda().getId() == criptomoedaId);
    }
}
