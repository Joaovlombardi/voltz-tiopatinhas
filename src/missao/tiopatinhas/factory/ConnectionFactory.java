package missao.tiopatinhas.factory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    private static final String URL =
            "jdbc:oracle:thin:@oracle.fiap.com.br:1521:orcl";

    private static final String USUARIO =
            System.getenv("FIAP_DB_USER");

    private static final String SENHA =
            System.getenv("FIAP_DB_PASSWORD");

    public static Connection getConnection() throws SQLException {

        if (USUARIO == null || SENHA == null) {
            throw new SQLException("Credenciais do banco não foram configuradas.");
        }

        return DriverManager.getConnection(URL, USUARIO, SENHA);
    }
}
