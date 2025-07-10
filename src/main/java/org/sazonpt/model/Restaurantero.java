package org.sazonpt.model;

public class Restaurantero extends Usuario{
    private int codigo_usuario;
    private String rfc;

    public Restaurantero(int id_usuario, String nombre, String correo, String contrasena, String tipo, int codigo_usuario, String rfc){
        super(id_usuario, nombre, correo, contrasena, tipo);
        this.codigo_usuario = codigo_usuario;
        this.rfc = rfc;
    }

    public int getCodigorestaurantero(){return codigo_usuario;}
    public void setRfc(String rfc){this.rfc = rfc;}
    public String getRfc(){return rfc;}
}
