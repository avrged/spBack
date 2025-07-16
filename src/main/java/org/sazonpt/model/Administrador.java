package org.sazonpt.model;

public class Administrador extends Usuario{
    private int codigo_usuario;
    private int status;

    public Administrador(){
        super();
        this.status = 1; // Status por defecto
    }

    public Administrador(String nombre, String correo, String contrasena) {
        super(nombre, correo, contrasena, "administrador");
        this.status = 1; // Status por defecto
    }

    public Administrador(int id_usuario, String nombre, String correo, String contrasena, String tipo, int codigo_usuario, int status){
        super(id_usuario, nombre, correo, contrasena, tipo, status);
        this.codigo_usuario = codigo_usuario;
        this.status = status;
    }

    public void setCodigoUsuario(int codigo_usuario) {this.codigo_usuario = codigo_usuario;}
    public int getCodigo_admin() {return codigo_usuario;}
    public void setStatus(int status){this.status = status;}
    public int getStatus(){return status;}
}
