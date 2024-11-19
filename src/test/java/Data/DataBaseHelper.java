package Data;

import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseHelper {
    private static final QueryRunner QUERY_RUNNER = new QueryRunner();

    private DataBaseHelper() {
    }

    private static Connection getConnect() throws SQLException {
        return DriverManager.getConnection(System.getProperty("app.url"), "app", "pass");
    }

    @SneakyThrows
    public static String getCreditStatus() {
        var statusSQL = "SELECT status FROM credit_request_entity ORDER BY created DESC LIMIT 1";
        var connect = getConnect();
        return QUERY_RUNNER.query(connect, statusSQL, new ScalarHandler<>());
    }

    @SneakyThrows
    public static String getBuyStatus() {
        var statusSQL = "SELECT status FROM payment_entity ORDER BY created DESC LIMIT 1";
        var connect = getConnect();
        return QUERY_RUNNER.query(connect, statusSQL, new ScalarHandler<>());
    }

    @SneakyThrows
    public static void cleanDataBase() {
        var connection = getConnect();
        QUERY_RUNNER.execute(connection, "DELETE FROM credit_request_entity");
        QUERY_RUNNER.execute(connection, "DELETE FROM order_entity");
        QUERY_RUNNER.execute(connection, "DELETE FROM payment_entity");
    }
}
