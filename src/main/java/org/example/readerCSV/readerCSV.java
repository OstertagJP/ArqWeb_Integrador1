package org.example.readerCSV;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.example.DAO.FacturaDAO;
import org.example.DAO.Factura_ProductoDAO;
import org.example.DAO.ProductoDAO;
import org.example.db.Conexion;
import org.example.entities.Cliente;
import org.example.DAO.ClienteDAO;
import org.example.entities.Factura;
import org.example.entities.Factura_Producto;
import org.example.entities.Producto;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.SQLException;

public class readerCSV {

   // private static Conexion instancia;
    private Connection conn;

    // Constructor que inicializa la conexión
    public readerCSV() {
        try {
            this.conn = Conexion.getInstancia().getConnection();
            // Desactivar auto-commit para manejar transacciones
            this.conn.setAutoCommit(false);
        } catch (SQLException e) {
            System.err.println("Error al obtener conexión: " + e.getMessage());
        }
    }
    // DAOs inicializados con la conexión válida
    private ClienteDAO getClienteDAO() {
        return new ClienteDAO(conn);
    }

    private ProductoDAO getProductoDAO() {
        return new ProductoDAO(conn);
    }

    private FacturaDAO getFacturaDAO() {
        return new FacturaDAO(conn);
    }

    private Factura_ProductoDAO getFacturaProductoDAO() {
        return new Factura_ProductoDAO(conn);
    }

    private Iterable<CSVRecord> getData(String archivo) throws IOException {
        String path = "src\\main\\resources\\" + archivo;
        Reader in = new FileReader(path);

        CSVParser csvParser = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(in);
        return csvParser.getRecords();
    }

    public void populateDB()  {
        try {
            System.out.println("Populating DB...");

            cargarClientes();

            cargarProductos();

            cargarFacturas();

            cargarFacturasProductos();

            // Confirmar transacción si todo va bien
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
            // Cerrar conexión
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    System.err.println("Error al cerrar conexión: " + e.getMessage());
                }
            }
        }
    }

        private void cargarClientes() throws IOException {
            ClienteDAO clienteDAO = getClienteDAO();
            System.out.println("Cargando clientes...");

            for (CSVRecord row : getData("clientes.csv")) {
                try {
                    int idCliente = Integer.parseInt(row.get("idCliente"));
                    String nombre = row.get("nombre");
                    String email = row.get("email");

                    Cliente cliente = new Cliente(idCliente, nombre, email);
                    clienteDAO.agregarCliente(cliente);
                } catch (Exception e) {
                    System.err.println("Error al procesar cliente: " + e.getMessage());
                }
            }

            System.out.println("✅ Clientes insertados");
        }

        private void cargarProductos() throws IOException {
            ProductoDAO productoDAO = getProductoDAO();
            System.out.println("Cargando productos...");

            for (CSVRecord row : getData("productos.csv")) {
                try {
                    int idProducto = Integer.parseInt(row.get("idProducto"));
                    String nombre = row.get("nombre");
                    float valor = Float.parseFloat(row.get("valor"));

                    Producto producto = new Producto(idProducto, nombre, valor);
                    productoDAO.agregarProducto(producto);
                } catch (Exception e) {
                    System.err.println("Error al procesar producto: " + e.getMessage());
                }
            }

            System.out.println("✅ Productos insertados");
        }

        private void cargarFacturas() throws IOException {
            FacturaDAO facturaDAO = getFacturaDAO();
            System.out.println("Cargando facturas...");

            for (CSVRecord row : getData("facturas.csv")) {
                try {
                    int idFactura = Integer.parseInt(row.get("idFactura"));
                    int idCliente = Integer.parseInt(row.get("idCliente"));

                    Factura factura = new Factura(idFactura, idCliente);
                    facturaDAO.agregarFactura(factura);
                } catch (Exception e) {
                    System.err.println("Error al procesar factura: " + e.getMessage());
                }
            }

            System.out.println("✅ Facturas insertadas");
        }

        private void cargarFacturasProductos() throws IOException {
            Factura_ProductoDAO facturaProductoDAO = getFacturaProductoDAO();
            System.out.println("Cargando relaciones factura-producto...");

            for (CSVRecord row : getData("facturas-productos.csv")) {
                try {
                    int idFactura = Integer.parseInt(row.get("idFactura"));
                    int idProducto = Integer.parseInt(row.get("idProducto"));
                    int cantidad = Integer.parseInt(row.get("cantidad"));

                    Factura_Producto facturaProducto = new Factura_Producto(idFactura, idProducto, cantidad);
                    facturaProductoDAO.insertFactura_Producto(facturaProducto);
                } catch (Exception e) {
                    System.err.println("Error al procesar factura-producto: " + e.getMessage());
                }
            }

            System.out.println("✅ Relaciones factura-producto insertadas");
        }
    }

