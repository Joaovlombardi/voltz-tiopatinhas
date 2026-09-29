package missao.tiopatinhas.dao;

import missao.tiopatinhas.factory.ConnectionFactory;
import missao.tiopatinhas.model.Carteira;
import missao.tiopatinhas.model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class CarteiraDao {

    private final Connection conexao;

    public CarteiraDao()
            throws SQLException {

        conexao =
                ConnectionFactory.getConnection();
    }

    // INSERT
    public void inserir(Carteira carteira)
            throws SQLException {

        String sql = """
                INSERT INTO Carteira
                (
                    id,
                    usuario_id,
                    valorTotalInvestido,
                    valorAtual,
                    lucroPrejuizo,
                    dataAtualizacao
                )
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement stm =
                     conexao.prepareStatement(sql)) {

            stm.setInt(
                    1,
                    carteira.getId()
            );

            stm.setInt(
                    2,
                    carteira
                            .getUsuario()
                            .getId()
            );

            stm.setDouble(
                    3,
                    carteira
                            .getValorTotalInvestido()
            );

            stm.setDouble(
                    4,
                    carteira.getValorAtual()
            );

            stm.setDouble(
                    5,
                    carteira.getLucroPrejuizo()
            );

            if (
                    carteira.getDataAtualizacao()
                            != null
            ) {

                stm.setTimestamp(
                        6,
                        Timestamp.valueOf(
                                carteira
                                        .getDataAtualizacao()
                        )
                );

            } else {

                stm.setNull(
                        6,
                        Types.TIMESTAMP
                );
            }

            stm.executeUpdate();
        }
    }

    // SELECT
    public List<Carteira> listar()
            throws SQLException {

        String sql =
                "SELECT * FROM Carteira ORDER BY id";

        List<Carteira> carteiras =
                new ArrayList<>();

        try (
                PreparedStatement stm =
                        conexao.prepareStatement(sql);

                ResultSet result =
                        stm.executeQuery()
        ) {

            while (result.next()) {

                Usuario usuario =
                        new Usuario();

                usuario.setId(
                        result.getInt(
                                "usuario_id"
                        )
                );

                Timestamp dataAtualizacao =
                        result.getTimestamp(
                                "dataAtualizacao"
                        );

                Carteira carteira =
                        new Carteira(
                                result.getInt("id"),
                                usuario,
                                result.getDouble(
                                        "valorTotalInvestido"
                                ),
                                result.getDouble(
                                        "valorAtual"
                                ),
                                result.getDouble(
                                        "lucroPrejuizo"
                                ),
                                dataAtualizacao != null
                                        ? dataAtualizacao
                                        .toLocalDateTime()
                                        : null
                        );

                carteiras.add(carteira);
            }
        }

        return carteiras;
    }

    // UPDATE
    public void alterar(Carteira carteira)
            throws SQLException {

        String sql = """
                UPDATE Carteira
                SET usuario_id = ?,
                    valorTotalInvestido = ?,
                    valorAtual = ?,
                    lucroPrejuizo = ?,
                    dataAtualizacao = ?
                WHERE id = ?
                """;

        try (PreparedStatement stm =
                     conexao.prepareStatement(sql)) {

            stm.setInt(
                    1,
                    carteira
                            .getUsuario()
                            .getId()
            );

            stm.setDouble(
                    2,
                    carteira
                            .getValorTotalInvestido()
            );

            stm.setDouble(
                    3,
                    carteira.getValorAtual()
            );

            stm.setDouble(
                    4,
                    carteira.getLucroPrejuizo()
            );

            if (
                    carteira.getDataAtualizacao()
                            != null
            ) {

                stm.setTimestamp(
                        5,
                        Timestamp.valueOf(
                                carteira
                                        .getDataAtualizacao()
                        )
                );

            } else {

                stm.setNull(
                        5,
                        Types.TIMESTAMP
                );
            }

            stm.setInt(
                    6,
                    carteira.getId()
            );

            stm.executeUpdate();
        }
    }

    // DELETE
    public void excluir(int id)
            throws SQLException {

        String sql =
                "DELETE FROM Carteira WHERE id = ?";

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