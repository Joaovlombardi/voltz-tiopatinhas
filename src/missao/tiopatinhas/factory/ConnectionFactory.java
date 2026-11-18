package missao.tiopatinhas.factory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    private static final String URL_PADRAO =
            "jdbc:oracle:thin:@oracle.fiap.com.br:1521:orcl";

    // Opcional: permite apontar para outro banco sem alterar o codigo.
    private static final String URL =
            System.getenv("FIAP_DB_URL") != null
                    ? System.getenv("FIAP_DB_URL")
                    : URL_PADRAO;

    private static final String USUARIO =
            System.getenv("FIAP_DB_USER");

    private static final String SENHA =
            System.getenv("FIAP_DB_PASSWORD");

    public static Connection getConnection() throws SQLException {

        if (USUARIO == null || SENHA == null) {
            throw new SQLException(
                    "Credenciais do banco nao foram configuradas. "
                            + "Defina as variaveis de ambiente FIAP_DB_USER e FIAP_DB_PASSWORD."
            );
        }

        return DriverManager.getConnection(URL, USUARIO, SENHA);
    }
}
