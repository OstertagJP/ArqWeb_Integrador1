package org.example.DAO;

import org.example.db.Conexion;
import org.example.entities.Producto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ProductoDAO {
    private Connection conn;

    public ProductoDAO(Connection conn) {
        this.conn = conn;
    }

    //Agrega una producto a la base de datos

    public void agregarProducto(Producto producto) {
        String sql = "INSERT INTO producto(nombre, valor) VALUES (?, ?)";

        try (Connection conn = Conexion.getInstancia().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, producto.getNombre());
            stmt.setFloat(2, producto.getValor());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
