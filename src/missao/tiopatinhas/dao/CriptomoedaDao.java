package missao.tiopatinhas.dao;

import missao.tiopatinhas.factory.ConnectionFactory;
import missao.tiopatinhas.model.Criptomoeda;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CriptomoedaDao {

    private final Connection conexao;

    public CriptomoedaDao() throws SQLException {
        conexao = ConnectionFactory.getConnection();
    }

    // Permite que varios DAOs compartilhem a mesma conexao
    public CriptomoedaDao(Connection conexao) {
        this.conexao = conexao;
    }

    // INSERT
    public void inserir(Criptomoeda criptomoeda) throws SQLException {

        String sql = """
                INSERT INTO Criptomoeda
                (id, nome, simbolo, descricao)
                VALUES (?, ?, ?, ?)
                """;

        try (PreparedStatement stm = conexao.prepareStatement(sql)) {

            stm.setInt(1, criptomoeda.getId());
            stm.setString(2, criptomoeda.getNome());
            stm.setString(3, criptomoeda.getSimbolo());
            stm.setString(4, criptomoeda.getDescricao());

            stm.executeUpdate();
        }
    }

    // SELECT (todos)
    public List<Criptomoeda> listar() throws SQLException {

        String sql = "SELECT * FROM Criptomoeda ORDER BY id";
        List<Criptomoeda> lista = new ArrayList<>();

        try (
                PreparedStatement stm = conexao.prepareStatement(sql);
                ResultSet result = stm.executeQuery()
        ) {

            while (result.next()) {
                lista.add(mapear(result));
            }
        }

        return lista;
    }

    // SELECT (um registro) - devolve null quando o ID nao existe
    public Criptomoeda buscarPorId(int id) throws SQLException {

        String sql = "SELECT * FROM Criptomoeda WHERE id = ?";

        try (PreparedStatement stm = conexao.prepareStatement(sql)) {

            stm.setInt(1, id);

            try (ResultSet result = stm.executeQuery()) {
                return result.next() ? mapear(result) : null;
            }
        }
    }

    // UPDATE - devolve true se o registro existia e foi alterado
    public boolean alterar(Criptomoeda criptomoeda) throws SQLException {

        String sql = """
                UPDATE Criptomoeda
                SET nome = ?, simbolo = ?, descricao = ?
                WHERE id = ?
                """;

        try (PreparedStatement stm = conexao.prepareStatement(sql)) {

            stm.setString(1, criptomoeda.getNome());
            stm.setString(2, criptomoeda.getSimbolo());
            stm.setString(3, criptomoeda.getDescricao());
            stm.setInt(4, criptomoeda.getId());

            return stm.executeUpdate() > 0;
        }
    }

    // DELETE - devolve true se o registro existia e foi excluido
    public boolean excluir(int id) throws SQLException {

        String sql = "DELETE FROM Criptomoeda WHERE id = ?";

        try (PreparedStatement stm = conexao.prepareStatement(sql)) {

            stm.setInt(1, id);

            return stm.executeUpdate() > 0;
        }
    }

    // Proximo ID livre da tabela
    public int proximoId() throws SQLException {

        String sql = "SELECT NVL(MAX(id), 0) + 1 FROM Criptomoeda";

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

    private Criptomoeda mapear(ResultSet result) throws SQLException {

        return new Criptomoeda(
                result.getInt("id"),
                result.getString("nome"),
                result.getString("simbolo"),
                result.getString("descricao")
        );
    }
}
