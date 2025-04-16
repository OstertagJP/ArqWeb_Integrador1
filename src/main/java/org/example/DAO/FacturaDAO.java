package org.example.DAO;

import org.example.entities.Factura;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FacturaDAO {

    private final Connection conn;

    public FacturaDAO(Connection conn) {
        this.conn = conn;
    }

    // Agrega una factura a la base de datos
    public void agregarFactura(Factura factura) throws SQLException {
        String sql = "INSERT INTO factura(idCliente) VALUES (?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, factura.getIdCliente());
            stmt.executeUpdate();
        }
    }

    // Devuelve una lista de todas las facturas en la base de datos
    public List<Factura> getFacturas() {
        String consulta = "SELECT * FROM factura";
        List<Factura> facturas = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(consulta);
             ResultSet resultado = ps.executeQuery()) {

            while (resultado.next()) {
                int idFactura = resultado.getInt("idFactura");
                int idCliente = resultado.getInt("idCliente");

                Factura f = new Factura(idFactura, idCliente);
                facturas.add(f);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return facturas;
    }

    public boolean existeFactura(int idFactura) {
        String sql = "SELECT 1 FROM factura WHERE idFactura = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idFactura);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}

