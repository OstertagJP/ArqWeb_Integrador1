package org.example.entities;

public class Factura_Producto {
    int id_factura;
    int id_producto;
    int cantidad;

    public Factura_Producto(int id_factura, int id_producto, int cantidad) {
        this.id_factura = id_factura;
        this.id_producto = id_producto;
        this.cantidad = cantidad;
    }
    public int getId_producto() {
        return id_producto;
    }
    public int getCantidad() {
        return cantidad;
    }
    public int getId_factura() {
        return id_factura;
    }


}
