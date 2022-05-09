package server.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static String dbName = "music";
    private static String url = "jdbc:mysql://192.168.75.129:3306/" + dbName;
    private static String user = "root";
    private static String passwd = "password";
    private static Connection cn = null;

    private DBConnection() {
        try {
            cn = DriverManager.getConnection(url, user, passwd);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Récupérer l’instance de la connexion
    public static Connection getConnection() {
        if (cn == null) {
            System.out.println("Open new Connection");
            new DBConnection();
        }
        return cn;
    }

    public static void close() {
        if (cn != null) {
            try {
                cn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
