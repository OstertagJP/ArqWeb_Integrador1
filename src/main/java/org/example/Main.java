package org.example;

import org.example.DAO.Factura_ProductoDAO;
import org.example.DTO.ClienteDTO;
import org.example.DTO.ProductoDTO;
import org.example.db.Conexion;
import org.example.readerCSV.readerCSV;
import org.example.esquema.EsquemaDB;
import org.example.DAO.ClienteDAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Conexion conexion = Conexion.getInstancia(); // Obtener la instancia única
        Connection conn = conexion.getConnection();

        Scanner scanner = new Scanner(System.in);
        boolean salir = false;

        while (!salir) {
            mostrarMenu();
            String input = scanner.nextLine();

            switch (input) {
                case "1":
                    inicializarBaseDeDatos();  // DROP, CREATE y carga CSV
                    break;
                case "2":
                    // Lógica para mostrar producto que más recaudó
                    System.out.println("📄 Mostrar producto que más recaudó.");
                    try {
                        Factura_ProductoDAO fpDAO =
                                new Factura_ProductoDAO(Conexion.getInstancia().getConnection());
                        ProductoDTO top = fpDAO.getProductoMayorRecaudacion();

                        if (top != null) {
                            System.out.println(top);  // como tu toString() ya muestra nombre y recaudado
                        } else {
                            System.out.println("No se encontraron ventas registradas.");
                        }
                    } catch (SQLException e) {
                        System.out.println("❌ Error al obtener el producto de mayor recaudación.");
                        e.printStackTrace();
                    }
                    break;
                case "3":
                    // Lógica para mostrar clientes según mayor monto de facturación
                    mostrarClientesMayorFacturacion();
                    break;
                case "0":
                    salir = true;
                    System.out.println("👋 Saliendo del programa...");
                    break;
                default:
                    System.out.println("❌ Opción inválida. Por favor, intentá de nuevo.");
            }
            System.out.println(); // Espacio entre ciclos
        }

        scanner.close();
    }

    private static void mostrarMenu() {
        System.out.println("===== MENÚ PRINCIPAL =====");
        System.out.println("1. Cargar datos desde CSV");
        System.out.println("2. Mostrar producto que más recaudó.");
        System.out.println("3. Mostrar clientes según mayor monto de facturación.");
        System.out.println("0. Salir");
        System.out.print("Seleccioná una opción: ");
    }

    private static void inicializarBaseDeDatos() {
        System.out.println("🧹 Eliminando tablas anteriores (si existen)...");
        EsquemaDB.dropTables();

        System.out.println("🧱 Creando nuevas tablas...");
        EsquemaDB.crearTablas();

        System.out.println("📥 Cargando datos desde archivos CSV...");
        readerCSV loader = new readerCSV();
        loader.populateDB();

        System.out.println("✅ Base de datos inicializada correctamente.");
    }

    private static void mostrarClientesMayorFacturacion() {
        try {
            ClienteDAO clienteDAO = new ClienteDAO(Conexion.getInstancia().getConnection());
            List<ClienteDTO> clientes = clienteDAO.getClientesMayorFacturacion();
            if (clientes.isEmpty()) {
                System.out.println("No hay clientes registrados o no se ha registrado facturación.");
                return;
            }
            System.out.println("📊 Clientes con mayor facturación:");
            for (ClienteDTO cliente : clientes) {
                System.out.println(cliente.toString());  // Llamar a toString() directamente
                System.out.println("----------");
            }
        } catch (Exception e) {
            System.out.println("❌ Ocurrió un error al obtener los datos de los clientes.");
            e.printStackTrace();
        }
    }
}

