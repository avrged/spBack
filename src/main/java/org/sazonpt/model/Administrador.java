package org.sazonpt.model;

public class Administrador extends Usuario{
    private int id_usuario;
    private String status;

    public Administrador(){
        super();
        this.status = "activo"; // Status por defecto
    }

    public Administrador(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public Administrador(String nombre, String correo, String contrasena) {
        super(nombre, correo, contrasena, "administrador");
        this.status = "activo"; // Status por defecto
    }

    public Administrador(int id_usuario, String nombre, String correo, String contrasena, String tipo, int id_usuario_fk, String status){
        super(id_usuario, nombre, correo, contrasena, tipo, status);
        this.id_usuario = id_usuario_fk;
        this.status = status;
    }

    public void setId_usuario(int id_usuario) {this.id_usuario = id_usuario;}
    public int getId_usuario() {return id_usuario;}
    public void setStatus(String status){this.status = status;}
    public String getStatus(){return status;}
    
    // Métodos de compatibilidad (mantener nombres antiguos)
    public void setCodigoUsuario(int codigo_usuario) {this.id_usuario = codigo_usuario;}
}
