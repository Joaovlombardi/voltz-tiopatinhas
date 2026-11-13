package missao.tiopatinhas.view;

import missao.tiopatinhas.dao.CarteiraDao;
import missao.tiopatinhas.dao.FavoritoDao;
import missao.tiopatinhas.dao.UsuarioDao;
import missao.tiopatinhas.model.Usuario;
import missao.tiopatinhas.util.Console;
import missao.tiopatinhas.util.Validador;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class UsuarioMenu extends MenuBase {

    private final UsuarioDao usuarioDao;
    private final CarteiraDao carteiraDao;
    private final FavoritoDao favoritoDao;

    public UsuarioMenu(UsuarioDao usuarioDao, CarteiraDao carteiraDao, FavoritoDao favoritoDao) {
        super("Usuarios");

        this.usuarioDao = usuarioDao;
        this.carteiraDao = carteiraDao;
        this.favoritoDao = favoritoDao;
    }

    @Override
    protected void incluir() throws SQLException {

        System.out.println("--- Novo usuario ---");

        List<Usuario> existentes = usuarioDao.listar();

        String nome = Console.lerTexto("Nome", null, 100);
        String cpf = lerCpf(null, 0, existentes);
        String telefone = lerTelefone(null);
        String email = lerEmail(null, 0, existentes);
        String senha = lerSenha(false);

        Usuario usuario = new Usuario(
                usuarioDao.proximoId(),
                nome,
                cpf,
                telefone,
                email,
                Validador.gerarHash(senha),
                LocalDateTime.now()
        );

        usuarioDao.inserir(usuario);

        Console.sucesso("Usuario cadastrado com o ID " + usuario.getId() + ".");
    }

    @Override
    protected void alterar() throws SQLException {

        int id = Console.lerId("ID do usuario a alterar");
        Usuario usuario = usuarioDao.buscarPorId(id);

        if (usuario == null) {
            Console.erro("Usuario nao encontrado.");
            return;
        }

        System.out.println("Pressione ENTER para manter o valor atual.");

        List<Usuario> existentes = usuarioDao.listar();

        usuario.setNome(Console.lerTexto("Nome", usuario.getNome(), 100));
        usuario.setCpf(lerCpf(usuario.getCpf(), id, existentes));
        usuario.setTelefone(lerTelefone(usuario.getTelefone()));
        usuario.setEmail(lerEmail(usuario.getEmail(), id, existentes));

        String novaSenha = lerSenha(true);

        if (novaSenha != null) {
            usuario.setSenhaHash(Validador.gerarHash(novaSenha));
        }

        if (usuarioDao.alterar(usuario)) {
            Console.sucesso("Usuario alterado.");
        } else {
            Console.erro("Usuario nao encontrado.");
        }
    }

    @Override
    protected void excluir() throws SQLException {

        int id = Console.lerId("ID do usuario a excluir");
        Usuario usuario = usuarioDao.buscarPorId(id);

        if (usuario == null) {
            Console.erro("Usuario nao encontrado.");
            return;
        }

        long carteiras = carteiraDao.listar().stream()
                .filter(c -> c.getUsuario().getId() == id)
                .count();

        long favoritos = favoritoDao.listar().stream()
                .filter(f -> f.getUsuario().getId() == id)
                .count();

        if (carteiras > 0 || favoritos > 0) {
            Console.erro("Nao e possivel excluir " + usuario.getNome() + ": o usuario possui "
                    + carteiras + " carteira(s) e " + favoritos + " favorito(s). Exclua-os primeiro.");
            return;
        }

        if (!Console.confirmar("Excluir o usuario " + usuario.getNome() + "?")) {
            Console.aviso("Exclusao cancelada.");
            return;
        }

        if (usuarioDao.excluir(id)) {
            Console.sucesso("Usuario excluido.");
        } else {
            Console.erro("Usuario nao encontrado.");
        }
    }

    @Override
    protected void listarTodos() throws SQLException {

        List<Usuario> usuarios = usuarioDao.listar();

        if (usuarios.isEmpty()) {
            Console.aviso("Nenhum usuario cadastrado.");
            return;
        }

        System.out.printf("%-5s %-30s %-12s %-30s%n", "ID", "NOME", "CPF", "EMAIL");

        for (Usuario usuario : usuarios) {
            System.out.printf(
                    "%-5d %-30s %-12s %-30s%n",
                    usuario.getId(),
                    usuario.getNome(),
                    usuario.getCpf(),
                    usuario.getEmail()
            );
        }

        System.out.println("Total: " + usuarios.size() + " usuario(s).");
    }

    @Override
    protected void exibirUm() throws SQLException {

        int id = Console.lerId("ID do usuario");
        Usuario usuario = usuarioDao.buscarPorId(id);

        if (usuario == null) {
            Console.erro("Usuario nao encontrado.");
            return;
        }

        long carteiras = carteiraDao.listar().stream()
                .filter(c -> c.getUsuario().getId() == id)
                .count();

        long favoritos = favoritoDao.listar().stream()
                .filter(f -> f.getUsuario().getId() == id)
                .count();

        System.out.println("ID:            " + usuario.getId());
        System.out.println("Nome:          " + usuario.getNome());
        System.out.println("CPF:           " + usuario.getCpf());
        System.out.println("Telefone:      " + Console.texto(usuario.getTelefone()));
        System.out.println("Email:         " + usuario.getEmail());
        System.out.println("Cadastrado em: " + Console.data(usuario.getDataCriacao()));
        System.out.println("Carteiras:     " + carteiras);
        System.out.println("Favoritos:     " + favoritos);
    }

    // ================= LEITURA COM VALIDACAO =================

    private String lerCpf(String atual, int idIgnorado, List<Usuario> existentes) {

        String rotulo = atual != null
                ? "CPF [" + atual + "]: "
                : "CPF (somente numeros): ";

        while (true) {
            String entrada = Console.lerLinha(rotulo);

            if (entrada.isEmpty() && atual != null) {
                return atual;
            }

            String cpf = Validador.somenteDigitos(entrada);

            if (!Validador.cpfValido(cpf)) {
                Console.erro("CPF invalido. Informe os 11 digitos de um CPF verdadeiro.");
                continue;
            }

            boolean duplicado = existentes.stream()
                    .anyMatch(u -> u.getId() != idIgnorado && cpf.equals(u.getCpf()));

            if (duplicado) {
                Console.erro("Ja existe um usuario com este CPF.");
                continue;
            }

            return cpf;
        }
    }

    private String lerEmail(String atual, int idIgnorado, List<Usuario> existentes) {

        while (true) {
            String entrada = Console.lerTexto("Email", atual, 100);

            // ENTER manteve o email ja gravado: nao precisa validar de novo
            if (entrada.equals(atual)) {
                return atual;
            }

            String email = entrada.toLowerCase();

            if (!Validador.emailValido(email)) {
                Console.erro("Email invalido. Exemplo: nome@dominio.com");
                continue;
            }

            boolean duplicado = existentes.stream()
                    .anyMatch(u -> u.getId() != idIgnorado && email.equalsIgnoreCase(u.getEmail()));

            if (duplicado) {
                Console.erro("Ja existe um usuario com este email.");
                continue;
            }

            return email;
        }
    }

    private String lerTelefone(String atual) {

        while (true) {
            String telefone = Console.lerTextoOpcional("Telefone com DDD", atual, 20);

            // Campo opcional: vazio e aceito; o valor ja gravado tambem nao e revalidado
            if (telefone == null || telefone.equals(atual) || Validador.telefoneValido(telefone)) {
                return telefone;
            }

            Console.erro("Telefone invalido. Informe DDD + numero, ex.: (11) 99999-8888");
        }
    }

    /** Na alteracao a senha e opcional: ENTER devolve null e a senha atual e mantida. */
    private String lerSenha(boolean opcional) {

        String rotulo = opcional
                ? "Nova senha (ENTER mantem a atual): "
                : "Senha (minimo " + Validador.TAMANHO_MINIMO_SENHA + " caracteres): ";

        while (true) {
            String senha = Console.lerLinha(rotulo);

            if (senha.isEmpty() && opcional) {
                return null;
            }

            if (Validador.senhaValida(senha)) {
                return senha;
            }

            Console.erro("A senha deve ter pelo menos "
                    + Validador.TAMANHO_MINIMO_SENHA + " caracteres.");
        }
    }
}
