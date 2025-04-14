package org.example.DAO;

import org.example.db.Conexion;
import org.example.entities.Cliente;
import org.example.entities.Factura;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FacturaDAO {

    private Connection conn;

    public FacturaDAO(Connection conn) {
        this.conn = conn;
    }

    //Agrega una factura a la base de datos

    public void agregarFactura(Factura factura) {
        String sql = "INSERT INTO factura(id_factura, id_cliente) VALUES (?,?)";

        try (Connection conn = Conexion.getInstancia().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, factura.getId());
            stmt.setInt(2, factura.getIdCliente());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    //Devuelver una lista de todas las facturas de la base de datos.

    //    public List<Factura> getFacturas()


    // Devuelve una lista de todas las facturas en la base de datos
    public List<Factura> getFacturas() {

        String consulta = "SELECT * FROM factura";
        List<Factura> facturas = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet resultado = null;

        try {
            ps = this.conn.prepareStatement(consulta);
            resultado = ps.executeQuery();

            while (resultado.next()) {
                int idFactura = resultado.getInt("id_factura");
                int idCliente = resultado.getInt("id_cliente");

                Factura f = new Factura(idFactura, idCliente);
                facturas.add(f);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (resultado != null) resultado.close();
                if (ps != null) ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return facturas;
    }
}