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

    // Permite que varios DAOs compartilhem a mesma conexao
    public CotacaoDao(Connection conexao) {
        this.conexao = conexao;
    }

    // INSERT
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

    // SELECT (todos)
    public List<Cotacao> listar() throws SQLException {

        String sql = "SELECT * FROM Cotacao ORDER BY id";
        List<Cotacao> cotacoes = new ArrayList<>();

        try (
                PreparedStatement stm = conexao.prepareStatement(sql);
                ResultSet result = stm.executeQuery()
        ) {

            while (result.next()) {
                cotacoes.add(mapear(result));
            }
        }

        return cotacoes;
    }

    // SELECT (um registro) - devolve null quando o ID nao existe
    public Cotacao buscarPorId(int id) throws SQLException {

        String sql = "SELECT * FROM Cotacao WHERE id = ?";

        try (PreparedStatement stm = conexao.prepareStatement(sql)) {

            stm.setInt(1, id);

            try (ResultSet result = stm.executeQuery()) {
                return result.next() ? mapear(result) : null;
            }
        }
    }

    // Cotacao mais recente de uma criptomoeda - devolve null se nao houver nenhuma
    public Cotacao buscarUltimaPorCriptomoeda(int criptomoedaId) throws SQLException {

        String sql = """
                SELECT * FROM Cotacao
                WHERE criptomoeda_id = ?
                ORDER BY dataConsulta DESC NULLS LAST, id DESC
                """;

        try (PreparedStatement stm = conexao.prepareStatement(sql)) {

            stm.setInt(1, criptomoedaId);

            try (ResultSet result = stm.executeQuery()) {
                return result.next() ? mapear(result) : null;
            }
        }
    }

    // UPDATE - devolve true se o registro existia e foi alterado
    public boolean alterar(Cotacao cotacao) throws SQLException {

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

            return stm.executeUpdate() > 0;
        }
    }

    // DELETE - devolve true se o registro existia e foi excluido
    public boolean excluir(int id) throws SQLException {

        String sql = "DELETE FROM Cotacao WHERE id = ?";

        try (PreparedStatement stm = conexao.prepareStatement(sql)) {

            stm.setInt(1, id);

            return stm.executeUpdate() > 0;
        }
    }

    // Proximo ID livre da tabela
    public int proximoId() throws SQLException {

        String sql = "SELECT NVL(MAX(id), 0) + 1 FROM Cotacao";

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

    private Cotacao mapear(ResultSet result) throws SQLException {

        Criptomoeda criptomoeda = new Criptomoeda();
        criptomoeda.setId(result.getInt("criptomoeda_id"));

        Timestamp dataConsulta = result.getTimestamp("dataConsulta");

        return new Cotacao(
                result.getInt("id"),
                criptomoeda,
                result.getDouble("precoAtual"),
                result.getDouble("variacao24h"),
                result.getDouble("variacao7d"),
                result.getDouble("variacao30d"),
                dataConsulta != null ? dataConsulta.toLocalDateTime() : null
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
