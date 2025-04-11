package org.example.esquema;

import org.example.db.Conexion;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Crear_Esquema {
    public static void crearTablas() {
        try (Connection conn = Conexion.getInstancia().getConnection(); Statement stmt = conn.createStatement()) {

            // Tabla CLIENTE
            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS cliente (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    nombre VARCHAR(100) NOT NULL,
                    email VARCHAR(100) NOT NULL UNIQUE
                );
            """);

            // Tabla PRODUCTO
            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS producto (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    nombre VARCHAR(100) NOT NULL,
                    precio DECIMAL(10,2) NOT NULL
                );
            """);

            // Tabla FACTURA
            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS factura (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    fecha DATE NOT NULL,
                    id_cliente INT,
                    FOREIGN KEY (id_cliente) REFERENCES cliente(id)
                );
            """);

            // Tabla FACTURA_PRODUCTO (relación muchos a muchos)
            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS factura_producto (
                    id_factura INT,
                    id_producto INT,
                    cantidad INT NOT NULL,
                    PRIMARY KEY (id_factura, id_producto),
                    FOREIGN KEY (id_factura) REFERENCES factura(id),
                    FOREIGN KEY (id_producto) REFERENCES producto(id)
                );
            """);

            System.out.println("✅ Tablas creadas correctamente.");

        } catch (SQLException e) {
            System.out.println("❌ Error al crear las tablas: " + e.getMessage());
        }
    }
}

