package org.example.DAO;

import org.example.DTO.ClienteDTO;
import org.example.entities.Cliente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    private final Connection conn;

    public ClienteDAO(Connection conn) {
        this.conn = conn;
    }

    public void agregarCliente(Cliente cliente) throws SQLException {
        String sql = "INSERT INTO cliente(nombre, email) VALUES (?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cliente.getNombre());
            stmt.setString(2, cliente.getEmail());

            stmt.executeUpdate();
        }
    }

    // Retorna una lista de clientes ordenada descendentemente por total de facturación
    public List<ClienteDTO> getClientesMayorFacturacion() throws SQLException {
        String query = "SELECT c.nombre, c.email, SUM(fp.cantidad * p.valor) AS total_facturado " +
                "FROM cliente c " +
                "JOIN factura f ON c.idCliente = f.idCliente " +
                "JOIN factura_producto fp ON f.idFactura = fp.idFactura " +
                "JOIN producto p ON fp.idProducto = p.idProducto " +
                "GROUP BY c.idCliente, c.nombre, c.email " +
                "ORDER BY total_facturado DESC";

        List<ClienteDTO> clientes = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String nombre = rs.getString("nombre");
                String email = rs.getString("email");
                int totalFacturado = rs.getInt("total_facturado");

                ClienteDTO clienteDTO = new ClienteDTO(nombre, email, totalFacturado);
                clientes.add(clienteDTO);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return clientes;
    }

    public List<Cliente> obtenerTodos() {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM cliente";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Cliente cliente = new Cliente(
                        rs.getInt("idCliente"),
                        rs.getString("nombre"),
                        rs.getString("email")
                );
                clientes.add(cliente);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return clientes;
    }

    public boolean existeCliente(int idCliente) {
        String sql = "SELECT 1 FROM cliente WHERE idCliente = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idCliente);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
