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

    // Permite que varios DAOs compartilhem a mesma conexao
    public AporteDao(Connection conexao) {
        this.conexao = conexao;
    }

    // INSERT
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

    // SELECT (todos)
    public List<Aporte> listar() throws SQLException {

        String sql = "SELECT * FROM Aporte ORDER BY id";
        List<Aporte> aportes = new ArrayList<>();

        try (
                PreparedStatement stm = conexao.prepareStatement(sql);
                ResultSet result = stm.executeQuery()
        ) {

            while (result.next()) {
                aportes.add(mapear(result));
            }
        }

        return aportes;
    }

    // SELECT (um registro) - devolve null quando o ID nao existe
    public Aporte buscarPorId(int id) throws SQLException {

        String sql = "SELECT * FROM Aporte WHERE id = ?";

        try (PreparedStatement stm = conexao.prepareStatement(sql)) {

            stm.setInt(1, id);

            try (ResultSet result = stm.executeQuery()) {
                return result.next() ? mapear(result) : null;
            }
        }
    }

    // UPDATE - devolve true se o registro existia e foi alterado
    public boolean alterar(Aporte aporte) throws SQLException {

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

            return stm.executeUpdate() > 0;
        }
    }

    // DELETE - devolve true se o registro existia e foi excluido
    public boolean excluir(int id) throws SQLException {

        String sql = "DELETE FROM Aporte WHERE id = ?";

        try (PreparedStatement stm = conexao.prepareStatement(sql)) {

            stm.setInt(1, id);

            return stm.executeUpdate() > 0;
        }
    }

    // Proximo ID livre da tabela
    public int proximoId() throws SQLException {

        String sql = "SELECT NVL(MAX(id), 0) + 1 FROM Aporte";

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

    private Aporte mapear(ResultSet result) throws SQLException {

        Carteira carteira = new Carteira();
        carteira.setId(result.getInt("carteira_id"));

        Timestamp dataHora = result.getTimestamp("dataHora");

        return new Aporte(
                result.getInt("id"),
                carteira,
                result.getDouble("valor"),
                dataHora != null ? dataHora.toLocalDateTime() : null,
                result.getString("descricao")
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
