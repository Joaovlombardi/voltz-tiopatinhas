package missao.tiopatinhas.dao;

import missao.tiopatinhas.factory.ConnectionFactory;
import missao.tiopatinhas.model.Carteira;
import missao.tiopatinhas.model.Compra;
import missao.tiopatinhas.model.Criptomoeda;
import missao.tiopatinhas.model.Transacao;
import missao.tiopatinhas.model.Venda;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class TransacaoDao {

    private final Connection conexao;

    public TransacaoDao() throws SQLException {
        conexao = ConnectionFactory.getConnection();
    }

    public void inserir(Transacao transacao) throws SQLException {

        String sql = """
                INSERT INTO Transacao
                (
                    id,
                    carteira_id,
                    criptomoeda_id,
                    tipo,
                    quantidade,
                    precoUnitario,
                    valorTotal,
                    dataHora
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement stm = conexao.prepareStatement(sql)) {

            stm.setInt(1, transacao.getId());
            stm.setInt(2, transacao.getCarteira().getId());
            stm.setInt(3, transacao.getCriptomoeda().getId());
            stm.setString(4, transacao.getTipo());
            stm.setDouble(5, transacao.getQuantidade());
            stm.setDouble(6, transacao.getPrecoUnitario());
            stm.setDouble(7, transacao.getValorTotal());
            definirDataHora(stm, 8, transacao.getDataHora());

            stm.executeUpdate();
        }
    }

    public List<Transacao> listar() throws SQLException {

        String sql = "SELECT * FROM Transacao ORDER BY id";
        List<Transacao> transacoes = new ArrayList<>();

        try (
                PreparedStatement stm = conexao.prepareStatement(sql);
                ResultSet result = stm.executeQuery()
        ) {

            while (result.next()) {

                Carteira carteira = new Carteira();
                carteira.setId(result.getInt("carteira_id"));

                Criptomoeda criptomoeda = new Criptomoeda();
                criptomoeda.setId(result.getInt("criptomoeda_id"));

                Timestamp dataHora = result.getTimestamp("dataHora");

                Transacao transacao = criarTransacao(
                        result.getString("tipo"),
                        result.getInt("id"),
                        carteira,
                        criptomoeda,
                        result.getDouble("quantidade"),
                        result.getDouble("precoUnitario"),
                        dataHora != null ? dataHora.toLocalDateTime() : null
                );

                transacao.setValorTotal(result.getDouble("valorTotal"));
                transacoes.add(transacao);
            }
        }

        return transacoes;
    }

    public void alterar(Transacao transacao) throws SQLException {

        String sql = """
                UPDATE Transacao
                SET carteira_id = ?,
                    criptomoeda_id = ?,
                    tipo = ?,
                    quantidade = ?,
                    precoUnitario = ?,
                    valorTotal = ?,
                    dataHora = ?
                WHERE id = ?
                """;

        try (PreparedStatement stm = conexao.prepareStatement(sql)) {

            stm.setInt(1, transacao.getCarteira().getId());
            stm.setInt(2, transacao.getCriptomoeda().getId());
            stm.setString(3, transacao.getTipo());
            stm.setDouble(4, transacao.getQuantidade());
            stm.setDouble(5, transacao.getPrecoUnitario());
            stm.setDouble(6, transacao.getValorTotal());
            definirDataHora(stm, 7, transacao.getDataHora());
            stm.setInt(8, transacao.getId());

            stm.executeUpdate();
        }
    }

    public void excluir(int id) throws SQLException {

        String sql = "DELETE FROM Transacao WHERE id = ?";

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

    private Transacao criarTransacao(
            String tipo,
            int id,
            Carteira carteira,
            Criptomoeda criptomoeda,
            double quantidade,
            double precoUnitario,
            java.time.LocalDateTime dataHora
    ) throws SQLException {

        if ("COMPRA".equals(tipo)) {
            return new Compra(
                    id,
                    carteira,
                    criptomoeda,
                    quantidade,
                    precoUnitario,
                    dataHora
            );
        }

        if ("VENDA".equals(tipo)) {
            return new Venda(
                    id,
                    carteira,
                    criptomoeda,
                    quantidade,
                    precoUnitario,
                    dataHora
            );
        }

        throw new SQLException("Tipo de transacao desconhecido: " + tipo);
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
