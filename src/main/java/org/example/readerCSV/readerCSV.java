package org.example.readerCSV;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.example.DAO.ClienteDAO;
import org.example.DAO.FacturaDAO;
import org.example.DAO.Factura_ProductoDAO;
import org.example.DAO.ProductoDAO;
import org.example.db.Conexion;
import org.example.entities.Cliente;
import org.example.entities.Factura;
import org.example.entities.Factura_Producto;
import org.example.entities.Producto;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.SQLException;

public class readerCSV {

    private Connection conn;
    private ClienteDAO clienteDAO;
    private ProductoDAO productoDAO;
    private FacturaDAO facturaDAO;
    private Factura_ProductoDAO facturaProductoDAO;

    public readerCSV() {
        try {
            this.conn = Conexion.getInstancia().getConnection();
            this.conn.setAutoCommit(false);

            this.clienteDAO = new ClienteDAO(conn);
            this.productoDAO = new ProductoDAO(conn);
            this.facturaDAO = new FacturaDAO(conn);
            this.facturaProductoDAO = new Factura_ProductoDAO(conn);

        } catch (SQLException e) {
            System.err.println("Error al obtener conexión: " + e.getMessage());
        }
    }

    private Iterable<CSVRecord> getData(String archivo) throws IOException {
        String path = "src\\main\\resources\\" + archivo;
        Reader in = new FileReader(path);
        CSVParser csvParser = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(in);
        return csvParser.getRecords();
    }

    public void populateDB() {
        try {
            System.out.println("Populating DB...");

            cargarClientes();
            cargarProductos();
            cargarFacturas();
            cargarFacturasProductos();

            conn.commit();
            System.out.println("✅ Todos los datos fueron cargados exitosamente");

        } catch (Exception e) {
            System.err.println("Error al poblar la base de datos: " + e.getMessage());
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    System.err.println("Error al hacer rollback: " + ex.getMessage());
                }
            }
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                    conn = null;
                } catch (SQLException e) {
                    System.err.println("Error al cerrar conexión: " + e.getMessage());
                }
            }
        }
    }

    private void cargarClientes() throws IOException {
        System.out.println("Cargando clientes...");

        for (CSVRecord row : getData("clientes.csv")) {
            try {
                int idCliente = Integer.parseInt(row.get("idCliente"));
                String nombre = row.get("nombre");
                String email = row.get("email");

                if (!clienteDAO.existeCliente(idCliente)) {
                    Cliente cliente = new Cliente(idCliente, nombre, email);
                    clienteDAO.agregarCliente(cliente);
                } else {
                    System.out.println("⚠️ Cliente con ID " + idCliente + " ya existe. Saltando...");
                }
            } catch (Exception e) {
                System.err.println("Error al procesar cliente: " + e.getMessage());
            }
        }

        System.out.println("✅ Clientes insertados");
    }

    private void cargarProductos() throws IOException {
        System.out.println("Cargando productos...");

        for (CSVRecord row : getData("productos.csv")) {
            try {
                int idProducto = Integer.parseInt(row.get("idProducto"));
                String nombre = row.get("nombre");
                float valor = Float.parseFloat(row.get("valor"));

                if (!productoDAO.existeProducto(idProducto)) {
                    Producto producto = new Producto(idProducto, nombre, valor);
                    productoDAO.agregarProducto(producto);
                } else {
                    System.out.println("⚠️ Producto con ID " + idProducto + " ya existe. Saltando...");
                }
            } catch (Exception e) {
                System.err.println("Error al procesar producto: " + e.getMessage());
            }
        }

        System.out.println("✅ Productos insertados");
    }

    private void cargarFacturas() throws IOException {
        System.out.println("Cargando facturas...");

        for (CSVRecord row : getData("facturas.csv")) {
            try {
                int idFactura = Integer.parseInt(row.get("idFactura"));
                int idCliente = Integer.parseInt(row.get("idCliente"));

                if (!facturaDAO.existeFactura(idFactura)) {
                    Factura factura = new Factura(idFactura, idCliente);
                    facturaDAO.agregarFactura(factura);
                } else {
                    System.out.println("⚠️ Factura con ID " + idFactura + " ya existe. Saltando...");
                }
            } catch (Exception e) {
                System.err.println("Error al procesar factura: " + e.getMessage());
            }
        }

        System.out.println("✅ Facturas insertadas");
    }

    private void cargarFacturasProductos() throws IOException {
        System.out.println("Cargando relaciones factura-producto...");

        for (CSVRecord row : getData("facturas-productos.csv")) {
            try {
                int idFactura = Integer.parseInt(row.get("idFactura"));
                int idProducto = Integer.parseInt(row.get("idProducto"));
                int cantidad = Integer.parseInt(row.get("cantidad"));

                if (!facturaProductoDAO.existeRelacion(idFactura, idProducto)) {
                    Factura_Producto facturaProducto = new Factura_Producto(idFactura, idProducto, cantidad);
                    facturaProductoDAO.insertFactura_Producto(facturaProducto);
                } else {
                    System.out.println("⚠️ Relación factura-producto ya existe: Factura " + idFactura + ", Producto " + idProducto);
                }
            } catch (Exception e) {
                System.err.println("Error al procesar factura-producto: " + e.getMessage());
            }
        }

        System.out.println("✅ Relaciones factura-producto insertadas");
    }
}
