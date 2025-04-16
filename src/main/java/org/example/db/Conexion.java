package org.example.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    private static final String URL = "jdbc:mysql://localhost:3306/integrador1?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";

    private static Conexion instancia;
    private Connection conn;

    // Constructor privado (Singleton)
    private Conexion() {
        try {
            Class.forName(DRIVER);
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            conn.setAutoCommit(false); // Opcional, según la lógica
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para obtener la instancia única
    public static synchronized Conexion getInstancia() {
        if (instancia == null) {
            instancia = new Conexion();
        }
        return instancia;
    }

    // Método para acceder a la conexión
    public Connection getConnection() {
        try {
            if (conn == null || conn.isClosed()) {
                conn = DriverManager.getConnection(URL, USER, PASSWORD);
                conn.setAutoCommit(false); // Restaurar comportamiento anterior si se recrea
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    // Método para cerrar la conexión (opcional)
    public void cerrarConexion() {
        if (conn != null) {
            try {
                conn.close();
                conn = null;
                instancia = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
