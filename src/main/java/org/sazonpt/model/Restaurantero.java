package org.sazonpt.model;

public class Restaurantero extends Usuario{
    private int codigo_usuario;
    private int status;

    // Constructor vacío requerido por Jackson
    public Restaurantero() {
        super();
    }

    public Restaurantero(int id_usuario, String nombre, String correo, String contrasena, String tipo, int codigo_usuario, int status){
        super(id_usuario, nombre, correo, contrasena, tipo, status);
        this.codigo_usuario = codigo_usuario;
        this.status = status;
    }

    public void setCodigoUsuario(int codigo_usuario) {this.codigo_usuario = codigo_usuario;}
    public void setStatus(int status){this.status = status;}
    public int getStatus(){return status;}
    public int getCodigorestaurantero(){return codigo_usuario;}
}
