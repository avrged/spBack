package org.sazonpt.model;

public class Restaurantero {
    private int id_restaurantero;   // PK/FK referencia a usuario.id_usuario
    private String rfc;             // VARCHAR(13)
    private boolean verificado;     // BOOLEAN DEFAULT FALSE

    // Constructor vacío
    public Restaurantero() {
        this.verificado = false; // Valor por defecto
    }

    // Constructor completo
    public Restaurantero(int id_restaurantero, String rfc, boolean verificado) {
        this.id_restaurantero = id_restaurantero;
        this.rfc = rfc;
        this.verificado = verificado;
    }

    // Constructor sin verificado (default false)
    public Restaurantero(int id_restaurantero, String rfc) {
        this.id_restaurantero = id_restaurantero;
        this.rfc = rfc;
        this.verificado = false;
    }

    // Getters y Setters
    public int getId_restaurantero() {
        return id_restaurantero;
    }

    public void setId_restaurantero(int id_restaurantero) {
        this.id_restaurantero = id_restaurantero;
    }

    public String getRfc() {
        return rfc;
    }

    public void setRfc(String rfc) {
        this.rfc = rfc;
    }

    public boolean isVerificado() {
        return verificado;
    }

    public void setVerificado(boolean verificado) {
        this.verificado = verificado;
    }

    // Métodos de utilidad
    public void verificar() {
        this.verificado = true;
    }

    public void desverificar() {
        this.verificado = false;
    }

    @Override
    public String toString() {
        return "Restaurantero{" +
                "id_restaurantero=" + id_restaurantero +
                ", rfc='" + rfc + '\'' +
                ", verificado=" + verificado +
                '}';
    }
}
