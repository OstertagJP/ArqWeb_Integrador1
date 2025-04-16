package org.example.DAO;

import org.example.entities.Producto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductoDAO {
    private final Connection conn;

    public ProductoDAO(Connection conn) {
        this.conn = conn;
    }

    // Agrega un producto a la base de datos
    public void agregarProducto(Producto producto) throws SQLException {
        String sql = "INSERT INTO producto(nombre, valor) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, producto.getNombre());
            stmt.setFloat(2, producto.getValor());
            stmt.executeUpdate();
        }
    }

    public boolean existeProducto(int idProducto) {
        String sql = "SELECT 1 FROM producto WHERE idProducto = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idProducto);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}

