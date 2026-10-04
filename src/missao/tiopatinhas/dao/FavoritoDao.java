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

    public List<Favorito> listar() throws SQLException {

        String sql = "SELECT * FROM Favorito ORDER BY id";
        List<Favorito> favoritos = new ArrayList<>();

        try (
                PreparedStatement stm = conexao.prepareStatement(sql);
                ResultSet result = stm.executeQuery()
        ) {

            while (result.next()) {

                Usuario usuario = new Usuario();
                usuario.setId(result.getInt("usuario_id"));

                Criptomoeda criptomoeda = new Criptomoeda();
                criptomoeda.setId(result.getInt("criptomoeda_id"));

                Timestamp dataAdicionado = result.getTimestamp("dataAdicionado");

                Favorito favorito = new Favorito(
                        result.getInt("id"),
                        usuario,
                        criptomoeda,
                        dataAdicionado != null ? dataAdicionado.toLocalDateTime() : null
                );

                favoritos.add(favorito);
            }
        }

        return favoritos;
    }

    public void alterar(Favorito favorito) throws SQLException {

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

            stm.executeUpdate();
        }
    }

    public void excluir(int id) throws SQLException {

        String sql = "DELETE FROM Favorito WHERE id = ?";

        try (PreparedStatement stm = conexao.prepareStatement(sql)) {
            stm.setInt(1, id);
            stm.executeUpdate();
        }
    }

    public void fecharConexao() throws SQLException {

        if (conexao != null && !conexao.isClosed()) {
            conexao.close();
        }
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
