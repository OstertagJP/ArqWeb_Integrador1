package org.example.DTO;

public class ProductoDTO {
    private String nombre;
    private float recaudado;

    public ProductoDTO() {};

    public ProductoDTO(String nombre, float recaudado) {
        this.nombre = nombre;
        this.recaudado = recaudado;
    }

    public String getNombre() {
        return nombre;
    }

    public float getRecaudado() {
        return recaudado;
    }

    @Override
    public String toString() {
        return "Nombre: " + this.nombre + ", recaudado: " + this.recaudado;
    }
}