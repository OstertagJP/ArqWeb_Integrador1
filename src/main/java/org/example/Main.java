package org.example;

import org.example.readerCSV.readerCSV;

public class Main {
    public static void main(String[] args) {
        System.out.println("Iniciando la aplicación...");

        readerCSV lector = new readerCSV();

        try {
            lector.populateDB();
            System.out.println("Todos los datos se cargaron correctamente.");
        } catch (Exception e) {
            System.err.println("Error al cargar los datos desde CSV: " + e.getMessage());
        }

        System.out.println("Aplicación finalizada.");
    }
}
