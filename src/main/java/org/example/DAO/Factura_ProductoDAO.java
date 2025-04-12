package org.example.DAO;

import org.example.db.Conexion;
import org.example.entities.Factura_Producto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Factura_ProductoDAO {
    private Connection conn;

    public Factura_ProductoDAO(Connection conn) {
        this.conn = conn;
    }

    public void insertFactura_Producto(Factura_Producto factura_producto) {
        String sql = "INSERT INTO factura_producto(id_factura, id_producto, cantidad) VALUES (?,?,?)";

        try (Connection conn = Conexion.getInstancia().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, factura_producto.getId_factura());
            stmt.setInt(2, factura_producto.getId_producto());
            stmt.setInt(3, factura_producto.getCantidad());


            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}