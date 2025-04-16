package org.example;

import org.example.readerCSV.readerCSV;
import org.example.esquema.EsquemaDB;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
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
                    break;
                case "3":
                    // Lógica para mostrar clientes según mayor monto de facturación
                    System.out.println("📄 Mostrar clientes según mayor monto de facturación.");
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
}
