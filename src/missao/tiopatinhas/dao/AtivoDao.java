package missao.tiopatinhas.dao;

import missao.tiopatinhas.factory.ConnectionFactory;
import missao.tiopatinhas.model.Ativo;
import missao.tiopatinhas.model.Carteira;
import missao.tiopatinhas.model.Criptomoeda;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

public class AtivoDao {

    private final Connection conexao;

    public AtivoDao() throws SQLException {
        conexao = ConnectionFactory.getConnection();
    }

    public void inserir(Ativo ativo) throws SQLException {

        String sql = """
            INSERT INTO Ativo
            (
                id,
                carteira_id,
                criptomoeda_id,
                quantidade,
                precoMedio,
                valorInvestido,
                valorAtual
            )
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """;

        try (PreparedStatement stm = conexao.prepareStatement(sql)) {

            stm.setInt(1, ativo.getId());

            stm.setInt(
                    2,
                    ativo.getCarteira().getId()
            );

            stm.setInt(
                    3,
                    ativo.getCriptomoeda().getId()
            );

            stm.setDouble(
                    4,
                    ativo.getQuantidade()
            );

            stm.setDouble(
                    5,
                    ativo.getPrecoMedio()
            );

            stm.setDouble(
                    6,
                    ativo.getValorInvestido()
            );

            stm.setDouble(
                    7,
                    ativo.getValorAtual()
            );

            stm.executeUpdate();
        }
    }

    public List<Ativo> listar() throws SQLException {

        String sql =
                "SELECT * FROM Ativo ORDER BY id";

        List<Ativo> ativos = new ArrayList<>();

        try (
                PreparedStatement stm =
                        conexao.prepareStatement(sql);

                ResultSet result =
                        stm.executeQuery()
        ) {

            while (result.next()) {

                Carteira carteira = new Carteira();
                carteira.setId(
                        result.getInt("carteira_id")
                );

                Criptomoeda criptomoeda =
                        new Criptomoeda();

                criptomoeda.setId(
                        result.getInt("criptomoeda_id")
                );

                Ativo ativo = new Ativo();

                ativo.setId(
                        result.getInt("id")
                );

                ativo.setCarteira(carteira);

                ativo.setCriptomoeda(
                        criptomoeda
                );

                ativo.setQuantidade(
                        result.getDouble("quantidade")
                );

                ativo.setPrecoMedio(
                        result.getDouble("precoMedio")
                );

                ativo.setValorInvestido(
                        result.getDouble("valorInvestido")
                );

                ativo.setValorAtual(
                        result.getDouble("valorAtual")
                );

                ativos.add(ativo);
            }
        }

        return ativos;
    }

    public void alterar(Ativo ativo) throws SQLException {

        String sql = """
            UPDATE Ativo
            SET carteira_id = ?,
                criptomoeda_id = ?,
                quantidade = ?,
                precoMedio = ?,
                valorInvestido = ?,
                valorAtual = ?
            WHERE id = ?
            """;

        try (PreparedStatement stm = conexao.prepareStatement(sql)) {

            stm.setInt(
                    1,
                    ativo.getCarteira().getId()
            );

            stm.setInt(
                    2,
                    ativo.getCriptomoeda().getId()
            );

            stm.setDouble(
                    3,
                    ativo.getQuantidade()
            );

            stm.setDouble(
                    4,
                    ativo.getPrecoMedio()
            );

            stm.setDouble(
                    5,
                    ativo.getValorInvestido()
            );

            stm.setDouble(
                    6,
                    ativo.getValorAtual()
            );

            stm.setInt(
                    7,
                    ativo.getId()
            );

            stm.executeUpdate();
        }
    }

    public void excluir(int id) throws SQLException {

        String sql =
                "DELETE FROM Ativo WHERE id = ?";

        try (PreparedStatement stm =
                     conexao.prepareStatement(sql)) {

            stm.setInt(1, id);

            stm.executeUpdate();
        }
    }

    public void fecharConexao() throws SQLException {

        if (conexao != null && !conexao.isClosed()) {
            conexao.close();
        }
    }
}