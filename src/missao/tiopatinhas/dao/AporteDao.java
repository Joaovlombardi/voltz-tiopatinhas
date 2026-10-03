package missao.tiopatinhas.dao;

import missao.tiopatinhas.factory.ConnectionFactory;
import missao.tiopatinhas.model.Aporte;
import missao.tiopatinhas.model.Carteira;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class AporteDao {

    private final Connection conexao;

    public AporteDao() throws SQLException {
        conexao = ConnectionFactory.getConnection();
    }

    public void inserir(Aporte aporte) throws SQLException {

        String sql = """
                INSERT INTO Aporte
                (
                    id,
                    carteira_id,
                    valor,
                    dataHora,
                    descricao
                )
                VALUES (?, ?, ?, ?, ?)
                """;

        try (PreparedStatement stm = conexao.prepareStatement(sql)) {

            stm.setInt(1, aporte.getId());
            stm.setInt(2, aporte.getCarteira().getId());
            stm.setDouble(3, aporte.getValor());
            definirDataHora(stm, 4, aporte.getDataHora());
            stm.setString(5, aporte.getDescricao());

            stm.executeUpdate();
        }
    }

    public List<Aporte> listar() throws SQLException {

        String sql = "SELECT * FROM Aporte ORDER BY id";
        List<Aporte> aportes = new ArrayList<>();

        try (
                PreparedStatement stm = conexao.prepareStatement(sql);
                ResultSet result = stm.executeQuery()
        ) {

            while (result.next()) {

                Carteira carteira = new Carteira();
                carteira.setId(result.getInt("carteira_id"));

                Timestamp dataHora = result.getTimestamp("dataHora");

                Aporte aporte = new Aporte(
                        result.getInt("id"),
                        carteira,
                        result.getDouble("valor"),
                        dataHora != null ? dataHora.toLocalDateTime() : null,
                        result.getString("descricao")
                );

                aportes.add(aporte);
            }
        }

        return aportes;
    }

    public void alterar(Aporte aporte) throws SQLException {

        String sql = """
                UPDATE Aporte
                SET carteira_id = ?,
                    valor = ?,
                    dataHora = ?,
                    descricao = ?
                WHERE id = ?
                """;

        try (PreparedStatement stm = conexao.prepareStatement(sql)) {

            stm.setInt(1, aporte.getCarteira().getId());
            stm.setDouble(2, aporte.getValor());
            definirDataHora(stm, 3, aporte.getDataHora());
            stm.setString(4, aporte.getDescricao());
            stm.setInt(5, aporte.getId());

            stm.executeUpdate();
        }
    }

    public void excluir(int id) throws SQLException {

        String sql = "DELETE FROM Aporte WHERE id = ?";

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
