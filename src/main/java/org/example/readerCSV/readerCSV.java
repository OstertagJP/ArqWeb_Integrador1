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

    private static Conexion instancia;
    private Connection conn;

    ClienteDAO cliDAO = new ClienteDAO(conn);
    FacturaDAO facDAO = new FacturaDAO(conn);
    Factura_ProductoDAO facProDAO = new Factura_ProductoDAO(conn);
    ProductoDAO proDAO = new ProductoDAO(conn);

    private Iterable<CSVRecord> getData(String archivo) throws IOException {
        String path = "src\\main\\resources\\" + archivo;
        Reader in = new FileReader(path);
        String[] header = {};  // Puedes configurar tu encabezado personalizado aquí si es necesario
        CSVParser csvParser = CSVFormat.EXCEL.withHeader(header).parse(in);

        Iterable<CSVRecord> records = csvParser.getRecords();
        return records;
    }

    public void populateDB() throws Exception {
        try {
            System.out.println("Populating DB...");
            for(CSVRecord row : getData("clientes.csv")) {
                if(row.size() >= 3) { // Verificar que hay al menos 3 campos en el CSVRecord
                    String idCliente = row.get(0);
                    String nombre = row.get(1);
                    String email = row.get(2);
                    if(!idCliente.isEmpty() && !nombre.isEmpty() && !email.isEmpty()) {
                        try {
                            int id_cliente = Integer.parseInt(idCliente);

                            Cliente cliente = new Cliente(id_cliente, nombre, email);
                            cliDAO.agregarCliente(cliente);

                            //insertDireccion(direccion, conn);
                        } catch (NumberFormatException e) {
                            System.err.println("Error de formato en datos de clientes: " + e.getMessage());
                        }
                    }
                }
            }
            System.out.println("Clientes insertados");

            for(CSVRecord row : getData("facturas.csv")) {
                if(row.size() >= 2) { // Verificar que hay al menos 3 campos en el CSVRecord
                    String idFactura = row.get(0);
                    String idCliente = row.get(1);
                    if(!idFactura.isEmpty() && !idCliente.isEmpty()) {
                        try {
                            int id_factura = Integer.parseInt(idFactura);
                            int id_cliente = Integer.parseInt(idCliente);

                            Factura factura = new Factura(id_factura, id_cliente);
                            facDAO.agregarFactura(factura);

                            //insertDireccion(direccion, conn);
                        } catch (NumberFormatException e) {
                            System.err.println("Error de formato en datos de facturas: " + e.getMessage());
                        }
                    }
                }
            }

            System.out.println("Facturas insertadas");

            for(CSVRecord row : getData("facturas-productos.csv")) {
                if(row.size() >= 3) { // Verificar que hay al menos 3 campos en el CSVRecord
                    String idFactura = row.get(0);
                    String idProducto = row.get(1);
                    String cantidad = row.get(2);
                    if(!idFactura.isEmpty() && !idProducto.isEmpty() && !cantidad.isEmpty()) {
                        try {
                            int id_factura = Integer.parseInt(idFactura);
                            int id_producto = Integer.parseInt(idProducto);
                            int cant= Integer.parseInt(cantidad);

                            Factura_Producto factura_producto = new Factura_Producto(id_factura, id_producto, cant);
                            facProDAO.insertFactura_Producto(factura_producto);

                            //insertDireccion(direccion, conn);
                        } catch (NumberFormatException e) {
                            System.err.println("Error de formato en datos de fact-productos: " + e.getMessage());
                        }
                    }
                }
            }
            System.out.println("Facturas-Producto insertadas");

            for(CSVRecord row : getData("productos.csv")) {
                if(row.size() >= 3) { // Verificar que hay al menos 3 campos en el CSVRecord
                    String idProducto = row.get(0);
                    String nombre = row.get(1);
                    String valor = row.get(2);
                    if(!idProducto.isEmpty() && !nombre.isEmpty() && !valor.isEmpty()) {
                        try {
                            int producto = Integer.parseInt(idProducto);
                            float val= Float.parseFloat(valor);

                            Producto prod = new Producto(producto, nombre, val);
                            proDAO.agregarProducto(prod);

                        } catch (NumberFormatException e) {
                            System.err.println("Error de formato en datos de productos: " + e.getMessage());
                        }
                    }
                }
            }
            System.out.println("Productos insertados");

        } catch (Exception e) {
            System.err.println("Error al poblar la base de datos: " + e.getMessage());
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    System.err.println("Error al hacer rollback: " + ex.getMessage());
                }
            }
        }
    }
}
