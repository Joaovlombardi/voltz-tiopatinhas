package missao.tiopatinhas.dao;

import missao.tiopatinhas.factory.ConnectionFactory;
import missao.tiopatinhas.model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDao {

    private final Connection conexao;

    public UsuarioDao() throws SQLException {
        conexao = ConnectionFactory.getConnection();
    }

    // Permite que varios DAOs compartilhem a mesma conexao
    public UsuarioDao(Connection conexao) {
        this.conexao = conexao;
    }

    // INSERT
    public void inserir(Usuario usuario) throws SQLException {

        String sql = """
                INSERT INTO Usuario
                (id, nome, cpf, telefone, email, senhaHash, dataCriacao)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement stm = conexao.prepareStatement(sql)) {

            stm.setInt(1, usuario.getId());
            stm.setString(2, usuario.getNome());
            stm.setString(3, usuario.getCpf());
            stm.setString(4, usuario.getTelefone());
            stm.setString(5, usuario.getEmail());
            stm.setString(6, usuario.getSenhaHash());

            if (usuario.getDataCriacao() != null) {
                stm.setTimestamp(7, Timestamp.valueOf(usuario.getDataCriacao()));
            } else {
                stm.setNull(7, Types.TIMESTAMP);
            }

            stm.executeUpdate();
        }
    }

    // SELECT (todos)
    public List<Usuario> listar() throws SQLException {

        String sql = "SELECT * FROM Usuario ORDER BY id";
        List<Usuario> usuarios = new ArrayList<>();

        try (
                PreparedStatement stm = conexao.prepareStatement(sql);
                ResultSet result = stm.executeQuery()
        ) {

            while (result.next()) {
                usuarios.add(mapear(result));
            }
        }

        return usuarios;
    }

    // SELECT (um registro) - devolve null quando o ID nao existe
    public Usuario buscarPorId(int id) throws SQLException {

        String sql = "SELECT * FROM Usuario WHERE id = ?";

        try (PreparedStatement stm = conexao.prepareStatement(sql)) {

            stm.setInt(1, id);

            try (ResultSet result = stm.executeQuery()) {
                return result.next() ? mapear(result) : null;
            }
        }
    }

    // UPDATE - devolve true se o registro existia e foi alterado
    public boolean alterar(Usuario usuario) throws SQLException {

        String sql = """
                UPDATE Usuario
                SET nome = ?,
                    cpf = ?,
                    telefone = ?,
                    email = ?,
                    senhaHash = ?
                WHERE id = ?
                """;

        try (PreparedStatement stm = conexao.prepareStatement(sql)) {

            stm.setString(1, usuario.getNome());
            stm.setString(2, usuario.getCpf());
            stm.setString(3, usuario.getTelefone());
            stm.setString(4, usuario.getEmail());
            stm.setString(5, usuario.getSenhaHash());
            stm.setInt(6, usuario.getId());

            return stm.executeUpdate() > 0;
        }
    }

    // DELETE - devolve true se o registro existia e foi excluido
    public boolean excluir(int id) throws SQLException {

        String sql = "DELETE FROM Usuario WHERE id = ?";

        try (PreparedStatement stm = conexao.prepareStatement(sql)) {

            stm.setInt(1, id);

            return stm.executeUpdate() > 0;
        }
    }

    // Proximo ID livre da tabela
    public int proximoId() throws SQLException {

        String sql = "SELECT NVL(MAX(id), 0) + 1 FROM Usuario";

        try (
                PreparedStatement stm = conexao.prepareStatement(sql);
                ResultSet result = stm.executeQuery()
        ) {
            result.next();
            return result.getInt(1);
        }
    }

    public void fecharConexao() throws SQLException {

        if (conexao != null && !conexao.isClosed()) {
            conexao.close();
        }
    }

    private Usuario mapear(ResultSet result) throws SQLException {

        Timestamp dataCriacao = result.getTimestamp("dataCriacao");

        return new Usuario(
                result.getInt("id"),
                result.getString("nome"),
                result.getString("cpf"),
                result.getString("telefone"),
                result.getString("email"),
                result.getString("senhaHash"),
                dataCriacao != null ? dataCriacao.toLocalDateTime() : null
        );
    }
}
