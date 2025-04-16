package org.example.DTO;

public class ClienteDTO {
    private String nombre;
    private String email;
    private int facturado;

    public ClienteDTO() {}

    public ClienteDTO(String nombre, String email, int totalFacturado) {
        this.nombre = nombre;
        this.email = email;
        this.facturado = totalFacturado;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmail(){
        return email;
    }

    public int getTotalFacturado() {
        return facturado;
    }

    @Override
    public String toString(){
        return String.format("Nombre: %s, Email: %s, Total facturado: $%d", this.nombre, this.email, this.facturado);
    }

}
