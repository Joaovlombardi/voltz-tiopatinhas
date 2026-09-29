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

                stm.setTimestamp(
                        7,
                        Timestamp.valueOf(usuario.getDataCriacao())
                );

            } else {

                stm.setNull(
                        7,
                        Types.TIMESTAMP
                );
            }

            stm.executeUpdate();
        }
    }

    // SELECT
    public List<Usuario> listar() throws SQLException {

        String sql =
                "SELECT * FROM Usuario ORDER BY id";

        List<Usuario> usuarios =
                new ArrayList<>();

        try (
                PreparedStatement stm =
                        conexao.prepareStatement(sql);

                ResultSet result =
                        stm.executeQuery()
        ) {

            while (result.next()) {

                Timestamp dataCriacao =
                        result.getTimestamp("dataCriacao");

                Usuario usuario = new Usuario(
                        result.getInt("id"),
                        result.getString("nome"),
                        result.getString("cpf"),
                        result.getString("telefone"),
                        result.getString("email"),
                        result.getString("senhaHash"),
                        dataCriacao != null
                                ? dataCriacao.toLocalDateTime()
                                : null
                );

                usuarios.add(usuario);
            }
        }

        return usuarios;
    }

    // UPDATE
    public void alterar(Usuario usuario)
            throws SQLException {

        String sql = """
                UPDATE Usuario
                SET nome = ?,
                    cpf = ?,
                    telefone = ?,
                    email = ?,
                    senhaHash = ?
                WHERE id = ?
                """;

        try (PreparedStatement stm =
                     conexao.prepareStatement(sql)) {

            stm.setString(
                    1,
                    usuario.getNome()
            );

            stm.setString(
                    2,
                    usuario.getCpf()
            );

            stm.setString(
                    3,
                    usuario.getTelefone()
            );

            stm.setString(
                    4,
                    usuario.getEmail()
            );

            stm.setString(
                    5,
                    usuario.getSenhaHash()
            );

            stm.setInt(
                    6,
                    usuario.getId()
            );

            stm.executeUpdate();
        }
    }

    // DELETE
    public void excluir(int id)
            throws SQLException {

        String sql =
                "DELETE FROM Usuario WHERE id = ?";

        try (PreparedStatement stm =
                     conexao.prepareStatement(sql)) {

            stm.setInt(1, id);

            stm.executeUpdate();
        }
    }

    public void fecharConexao()
            throws SQLException {

        if (
                conexao != null
                        && !conexao.isClosed()
        ) {
            conexao.close();
        }
    }
}