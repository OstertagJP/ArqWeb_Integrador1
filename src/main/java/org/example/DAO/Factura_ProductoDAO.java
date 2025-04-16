package org.example.DAO;

import org.example.DTO.ProductoDTO;
import org.example.entities.Factura_Producto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Factura_ProductoDAO {
    private final Connection conn;

    public Factura_ProductoDAO(Connection conn) {
        this.conn = conn;
    }

    public void insertFactura_Producto(Factura_Producto factura_producto) throws SQLException {
        String sql = "INSERT INTO factura_producto(idFactura, idProducto, cantidad) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, factura_producto.getId_factura());
            stmt.setInt(2, factura_producto.getId_producto());
            stmt.setInt(3, factura_producto.getCantidad());

            stmt.executeUpdate();
        }
    }

    public boolean existeRelacion(int idFactura, int idProducto) {
        String sql = "SELECT 1 FROM factura_producto WHERE idFactura = ? AND idProducto = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idFactura);
            stmt.setInt(2, idProducto);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public ProductoDTO getProductoMayorRecaudacion() throws SQLException {
        String sql =
                "SELECT p.nombre, " +
                        "       SUM(fp.cantidad * p.valor) AS total_recaudado " +
                        "FROM factura_producto fp " +
                        "JOIN producto p ON fp.idProducto = p.idProducto " +
                        "GROUP BY p.idProducto, p.nombre " +
                        "ORDER BY total_recaudado DESC " +
                        "LIMIT 1";

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                String nombre = rs.getString("nombre");
                float recaudado = rs.getFloat("total_recaudado");
                // Usa el constructor de tu DTO:
                return new ProductoDTO(nombre, recaudado);
            }
            return null;
        }
    }
}

