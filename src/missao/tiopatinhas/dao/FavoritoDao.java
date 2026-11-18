package missao.tiopatinhas.dao;

import missao.tiopatinhas.factory.ConnectionFactory;
import missao.tiopatinhas.model.Criptomoeda;
import missao.tiopatinhas.model.Favorito;
import missao.tiopatinhas.model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class FavoritoDao {

    private final Connection conexao;

    public FavoritoDao() throws SQLException {
        conexao = ConnectionFactory.getConnection();
    }

    // Permite que varios DAOs compartilhem a mesma conexao
    public FavoritoDao(Connection conexao) {
        this.conexao = conexao;
    }

    // INSERT
    public void inserir(Favorito favorito) throws SQLException {

        String sql = """
                INSERT INTO Favorito
                (
                    id,
                    usuario_id,
                    criptomoeda_id,
                    dataAdicionado
                )
                VALUES (?, ?, ?, ?)
                """;

        try (PreparedStatement stm = conexao.prepareStatement(sql)) {

            stm.setInt(1, favorito.getId());
            stm.setInt(2, favorito.getUsuario().getId());
            stm.setInt(3, favorito.getCriptomoeda().getId());
            definirDataHora(stm, 4, favorito.getDataAdicionado());

            stm.executeUpdate();
        }
    }

    // SELECT (todos)
    public List<Favorito> listar() throws SQLException {

        String sql = "SELECT * FROM Favorito ORDER BY id";
        List<Favorito> favoritos = new ArrayList<>();

        try (
                PreparedStatement stm = conexao.prepareStatement(sql);
                ResultSet result = stm.executeQuery()
        ) {

            while (result.next()) {
                favoritos.add(mapear(result));
            }
        }

        return favoritos;
    }

    // SELECT (um registro) - devolve null quando o ID nao existe
    public Favorito buscarPorId(int id) throws SQLException {

        String sql = "SELECT * FROM Favorito WHERE id = ?";

        try (PreparedStatement stm = conexao.prepareStatement(sql)) {

            stm.setInt(1, id);

            try (ResultSet result = stm.executeQuery()) {
                return result.next() ? mapear(result) : null;
            }
        }
    }

    // UPDATE - devolve true se o registro existia e foi alterado
    public boolean alterar(Favorito favorito) throws SQLException {

        String sql = """
                UPDATE Favorito
                SET usuario_id = ?,
                    criptomoeda_id = ?,
                    dataAdicionado = ?
                WHERE id = ?
                """;

        try (PreparedStatement stm = conexao.prepareStatement(sql)) {

            stm.setInt(1, favorito.getUsuario().getId());
            stm.setInt(2, favorito.getCriptomoeda().getId());
            definirDataHora(stm, 3, favorito.getDataAdicionado());
            stm.setInt(4, favorito.getId());

            return stm.executeUpdate() > 0;
        }
    }

    // DELETE - devolve true se o registro existia e foi excluido
    public boolean excluir(int id) throws SQLException {

        String sql = "DELETE FROM Favorito WHERE id = ?";

        try (PreparedStatement stm = conexao.prepareStatement(sql)) {

            stm.setInt(1, id);

            return stm.executeUpdate() > 0;
        }
    }

    // Proximo ID livre da tabela
    public int proximoId() throws SQLException {

        String sql = "SELECT NVL(MAX(id), 0) + 1 FROM Favorito";

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

    private Favorito mapear(ResultSet result) throws SQLException {

        Usuario usuario = new Usuario();
        usuario.setId(result.getInt("usuario_id"));

        Criptomoeda criptomoeda = new Criptomoeda();
        criptomoeda.setId(result.getInt("criptomoeda_id"));

        Timestamp dataAdicionado = result.getTimestamp("dataAdicionado");

        return new Favorito(
                result.getInt("id"),
                usuario,
                criptomoeda,
                dataAdicionado != null ? dataAdicionado.toLocalDateTime() : null
        );
    }

    private void definirDataHora(
            PreparedStatement stm,
            int indice,
            java.time.LocalDateTime dataHora
    ) throws SQLException {

        if (dataHora != null) {
            stm.setTimestamp(indice, Timestamp.valueOf(dataHora));
        } else {
            stm.setNull(indice, Types.TIMESTAMP);
        }
    }
}
