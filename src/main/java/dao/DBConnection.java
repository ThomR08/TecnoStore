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

    public Connection conectar() throws SQLException {
        String url = "jdbc:mysql://" + IP + ":" + PORT + "/" + DB + "?useSSL=false&serverTimezone=America/Bogota";

        try {
            return DriverManager.getConnection(url, USER, PASSWORD);
        } catch (SQLException e) {
            throw new SQLException("❌ Error al conectar con la base de datos: " + e.getMessage(), e);
        }
    }
}
