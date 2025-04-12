package org.example;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;


public class Main {
    public static void main(String[] args) {
        System.out.println("Hello, World!");
    }



public class ProductoLoader {

    public static void cargarProductosDesdeCSV(String nombreArchivo) {
        try {
            CSVParser parser = CSVFormat.DEFAULT
                    .withHeader()
                    .parse(new FileReader(nombreArchivo));

            for (CSVRecord row : parser) {
                System.out.println(row.get("idProducto"));
                System.out.println(row.get("nombre"));
                System.out.println(row.get("valor"));
            }

        } catch (IOException e) {
            System.out.println("Error al leer el archivo CSV: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        cargarProductosDesdeCSV("productos.csv");
    }
}
}
