package missao.tiopatinhas.dao;

import missao.tiopatinhas.factory.ConnectionFactory;
import missao.tiopatinhas.model.Cotacao;
import missao.tiopatinhas.model.Criptomoeda;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class CotacaoDao {

    private final Connection conexao;

    public CotacaoDao() throws SQLException {
        conexao = ConnectionFactory.getConnection();
    }

    public void inserir(Cotacao cotacao) throws SQLException {

        String sql = """
                INSERT INTO Cotacao
                (
                    id,
                    criptomoeda_id,
                    precoAtual,
                    variacao24h,
                    variacao7d,
                    variacao30d,
                    dataConsulta
                )
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement stm = conexao.prepareStatement(sql)) {

            stm.setInt(1, cotacao.getId());
            stm.setInt(2, cotacao.getCriptomoeda().getId());
            stm.setDouble(3, cotacao.getPrecoAtual());
            stm.setDouble(4, cotacao.getVariacao24h());
            stm.setDouble(5, cotacao.getVariacao7d());
            stm.setDouble(6, cotacao.getVariacao30d());
            definirDataHora(stm, 7, cotacao.getDataConsulta());

            stm.executeUpdate();
        }
    }

    public List<Cotacao> listar() throws SQLException {

        String sql = "SELECT * FROM Cotacao ORDER BY id";
        List<Cotacao> cotacoes = new ArrayList<>();

        try (
                PreparedStatement stm = conexao.prepareStatement(sql);
                ResultSet result = stm.executeQuery()
        ) {

            while (result.next()) {

                Criptomoeda criptomoeda = new Criptomoeda();
                criptomoeda.setId(result.getInt("criptomoeda_id"));

                Timestamp dataConsulta = result.getTimestamp("dataConsulta");

                Cotacao cotacao = new Cotacao(
                        result.getInt("id"),
                        criptomoeda,
                        result.getDouble("precoAtual"),
                        result.getDouble("variacao24h"),
                        result.getDouble("variacao7d"),
                        result.getDouble("variacao30d"),
                        dataConsulta != null ? dataConsulta.toLocalDateTime() : null
                );

                cotacoes.add(cotacao);
            }
        }

        return cotacoes;
    }

    public void alterar(Cotacao cotacao) throws SQLException {

        String sql = """
                UPDATE Cotacao
                SET criptomoeda_id = ?,
                    precoAtual = ?,
                    variacao24h = ?,
                    variacao7d = ?,
                    variacao30d = ?,
                    dataConsulta = ?
                WHERE id = ?
                """;

        try (PreparedStatement stm = conexao.prepareStatement(sql)) {

            stm.setInt(1, cotacao.getCriptomoeda().getId());
            stm.setDouble(2, cotacao.getPrecoAtual());
            stm.setDouble(3, cotacao.getVariacao24h());
            stm.setDouble(4, cotacao.getVariacao7d());
            stm.setDouble(5, cotacao.getVariacao30d());
            definirDataHora(stm, 6, cotacao.getDataConsulta());
            stm.setInt(7, cotacao.getId());

            stm.executeUpdate();
        }
    }

    public void excluir(int id) throws SQLException {

        String sql = "DELETE FROM Cotacao WHERE id = ?";

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
