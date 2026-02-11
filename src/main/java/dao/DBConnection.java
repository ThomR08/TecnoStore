package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String IP = "localhost";
    private static final String PORT = "3306";
    private static final String DB = "TecnoStore";
    private static final String USER = "root";
    private static final String PASSWORD = "123450";

    private static Connection connection;

    private DBConnection() {
    }

    public static Connection getConnection() throws SQLException {

        if (connection == null || connection.isClosed()) {

            String url = "jdbc:mysql://" + IP + ":" + PORT + "/" + DB + "?useSSL=false&serverTimezone=America/Bogota";

            try {
                connection = DriverManager.getConnection(url, USER, PASSWORD);
            } catch (SQLException e) {
                throw new SQLException("❌ Error al conectar con la base de datos: " + e.getMessage(), e);
            }
        }

        return connection;
    }
    
    public static void closeConnection() {

        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.out.println("❌ Error cerrando conexión: " + e.getMessage());
        }
    }
}
