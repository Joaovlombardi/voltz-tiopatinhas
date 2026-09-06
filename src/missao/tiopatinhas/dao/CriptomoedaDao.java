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

    private Connection conexao;

    public CriptomoedaDao() throws SQLException {
        conexao = ConnectionFactory.getConnection();
    }

    // INSERT
    public void inserir(Criptomoeda criptomoeda) throws SQLException {

        String sql = """
                INSERT INTO Criptomoeda
                (id, nome, simbolo, descricao)
                VALUES (?, ?, ?, ?)
                """;

        PreparedStatement stm = conexao.prepareStatement(sql);

        stm.setInt(1, criptomoeda.getId());
        stm.setString(2, criptomoeda.getNome());
        stm.setString(3, criptomoeda.getSimbolo());
        stm.setString(4, criptomoeda.getDescricao());

        stm.executeUpdate();
        stm.close();
    }

    // SELECT
    public List<Criptomoeda> listar() throws SQLException {

        String sql = "SELECT * FROM Criptomoeda ORDER BY id";

        PreparedStatement stm = conexao.prepareStatement(sql);
        ResultSet result = stm.executeQuery();

        List<Criptomoeda> lista = new ArrayList<>();

        while (result.next()) {

            Criptomoeda criptomoeda = new Criptomoeda(
                    result.getInt("id"),
                    result.getString("nome"),
                    result.getString("simbolo"),
                    result.getString("descricao")
            );

            lista.add(criptomoeda);
        }

        result.close();
        stm.close();

        return lista;
    }

    // UPDATE
    public void alterar(Criptomoeda criptomoeda) throws SQLException {

        String sql = """
                UPDATE Criptomoeda
                SET nome = ?, simbolo = ?, descricao = ?
                WHERE id = ?
                """;

        PreparedStatement stm = conexao.prepareStatement(sql);

        stm.setString(1, criptomoeda.getNome());
        stm.setString(2, criptomoeda.getSimbolo());
        stm.setString(3, criptomoeda.getDescricao());
        stm.setInt(4, criptomoeda.getId());

        stm.executeUpdate();
        stm.close();
    }

    // DELETE
    public void excluir(int id) throws SQLException {

        String sql = "DELETE FROM Criptomoeda WHERE id = ?";

        PreparedStatement stm = conexao.prepareStatement(sql);

        stm.setInt(1, id);

        stm.executeUpdate();
        stm.close();
    }

    public void fecharConexao() throws SQLException {
        conexao.close();
    }
}
