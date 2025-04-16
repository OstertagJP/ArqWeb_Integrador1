package org.example.esquema;

import org.example.db.Conexion;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class EsquemaDB {

    public static void dropTables() {
        try (Connection conn = Conexion.getInstancia().getConnection();
             Statement stmt = conn.createStatement()) {

            // Borrar en orden por dependencias (evita errores de FK)
            stmt.executeUpdate("DROP TABLE IF EXISTS factura_producto");
            stmt.executeUpdate("DROP TABLE IF EXISTS factura");
            stmt.executeUpdate("DROP TABLE IF EXISTS producto");
            stmt.executeUpdate("DROP TABLE IF EXISTS cliente");

            System.out.println("✅ Tablas eliminadas correctamente.");

            conn.commit();
        } catch (SQLException e) {
            System.out.println("❌ Error al eliminar las tablas: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void crearTablas() {
        try (Connection conn = Conexion.getInstancia().getConnection();
             Statement stmt = conn.createStatement()) {

            // Tabla CLIENTE
            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS cliente (
                    idCliente INT AUTO_INCREMENT PRIMARY KEY,
                    nombre VARCHAR(500) NOT NULL,
                    email VARCHAR(150) NOT NULL UNIQUE
                );
            """);

            // Tabla PRODUCTO
            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS producto (
                    idProducto INT AUTO_INCREMENT PRIMARY KEY,
                    nombre VARCHAR(45) NOT NULL,
                    valor FLOAT NOT NULL
                );
            """);

            // Tabla FACTURA
            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS factura (
                    idFactura INT AUTO_INCREMENT PRIMARY KEY,
                    idCliente INT,
                    FOREIGN KEY (idCliente) REFERENCES cliente(idCliente)
                );
            """);

            // Tabla FACTURA_PRODUCTO (relación muchos a muchos)
            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS factura_producto (
                    idFactura INT,
                    idProducto INT,
                    cantidad INT NOT NULL,
                    PRIMARY KEY (idFactura, idProducto),
                    FOREIGN KEY (idFactura) REFERENCES factura(idFactura),
                    FOREIGN KEY (idProducto) REFERENCES producto(idProducto)
                );
            """);

            System.out.println("✅ Tablas creadas correctamente.");
            conn.commit();

        } catch (SQLException e) {
            System.out.println("❌ Error al crear las tablas: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

