package org.sazonpt.model;

public class Restaurantero extends Usuario{
    private int id_usuario_fk;  // FK hacia la tabla usuario
    private int status;

    // Constructor vacío requerido por Jackson
    public Restaurantero() {
        super();
        this.status = 1; // Status por defecto
    }

    // Constructor sin status (usa 1 por defecto)
    public Restaurantero(String nombre, String correo, String contrasena) {
        super(nombre, correo, contrasena, "restaurantero");
        this.status = 1; // Status por defecto
    }

    public Restaurantero(int id_usuario, String nombre, String correo, String contrasena, String tipo, int id_usuario_fk, int status){
        super(id_usuario, nombre, correo, contrasena, tipo, status);
        this.id_usuario_fk = id_usuario_fk;
        this.status = status;
    }

    // Getters y Setters nuevos
    public void setId_usuario_fk(int id_usuario_fk) {this.id_usuario_fk = id_usuario_fk;}
    public int getId_usuario_fk() {return id_usuario_fk;}
    
    public void setStatus(int status){this.status = status;}
    public int getStatus(){return status;}

    // Métodos de compatibilidad (mantener nombres antiguos)
    public void setCodigoUsuario(int codigo_usuario) {this.id_usuario_fk = codigo_usuario;}
    public int getCodigorestaurantero(){return id_usuario_fk;}
}
