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

    public CarteiraDao() throws SQLException {
        conexao = ConnectionFactory.getConnection();
    }

    // Permite que varios DAOs compartilhem a mesma conexao
    public CarteiraDao(Connection conexao) {
        this.conexao = conexao;
    }

    // INSERT
    public void inserir(Carteira carteira) throws SQLException {

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

        try (PreparedStatement stm = conexao.prepareStatement(sql)) {

            stm.setInt(1, carteira.getId());
            stm.setInt(2, carteira.getUsuario().getId());
            stm.setDouble(3, carteira.getValorTotalInvestido());
            stm.setDouble(4, carteira.getValorAtual());
            stm.setDouble(5, carteira.getLucroPrejuizo());
            definirDataHora(stm, 6, carteira.getDataAtualizacao());

            stm.executeUpdate();
        }
    }

    // SELECT (todos)
    public List<Carteira> listar() throws SQLException {

        String sql = "SELECT * FROM Carteira ORDER BY id";
        List<Carteira> carteiras = new ArrayList<>();

        try (
                PreparedStatement stm = conexao.prepareStatement(sql);
                ResultSet result = stm.executeQuery()
        ) {

            while (result.next()) {
                carteiras.add(mapear(result));
            }
        }

        return carteiras;
    }

    // SELECT (um registro) - devolve null quando o ID nao existe
    public Carteira buscarPorId(int id) throws SQLException {

        String sql = "SELECT * FROM Carteira WHERE id = ?";

        try (PreparedStatement stm = conexao.prepareStatement(sql)) {

            stm.setInt(1, id);

            try (ResultSet result = stm.executeQuery()) {
                return result.next() ? mapear(result) : null;
            }
        }
    }

    // UPDATE - devolve true se o registro existia e foi alterado
    public boolean alterar(Carteira carteira) throws SQLException {

        String sql = """
                UPDATE Carteira
                SET usuario_id = ?,
                    valorTotalInvestido = ?,
                    valorAtual = ?,
                    lucroPrejuizo = ?,
                    dataAtualizacao = ?
                WHERE id = ?
                """;

        try (PreparedStatement stm = conexao.prepareStatement(sql)) {

            stm.setInt(1, carteira.getUsuario().getId());
            stm.setDouble(2, carteira.getValorTotalInvestido());
            stm.setDouble(3, carteira.getValorAtual());
            stm.setDouble(4, carteira.getLucroPrejuizo());
            definirDataHora(stm, 5, carteira.getDataAtualizacao());
            stm.setInt(6, carteira.getId());

            return stm.executeUpdate() > 0;
        }
    }

    // DELETE - devolve true se o registro existia e foi excluido
    public boolean excluir(int id) throws SQLException {

        String sql = "DELETE FROM Carteira WHERE id = ?";

        try (PreparedStatement stm = conexao.prepareStatement(sql)) {

            stm.setInt(1, id);

            return stm.executeUpdate() > 0;
        }
    }

    // Proximo ID livre da tabela
    public int proximoId() throws SQLException {

        String sql = "SELECT NVL(MAX(id), 0) + 1 FROM Carteira";

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

    private Carteira mapear(ResultSet result) throws SQLException {

        Usuario usuario = new Usuario();
        usuario.setId(result.getInt("usuario_id"));

        Timestamp dataAtualizacao = result.getTimestamp("dataAtualizacao");

        return new Carteira(
                result.getInt("id"),
                usuario,
                result.getDouble("valorTotalInvestido"),
                result.getDouble("valorAtual"),
                result.getDouble("lucroPrejuizo"),
                dataAtualizacao != null ? dataAtualizacao.toLocalDateTime() : null
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
