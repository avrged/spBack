package org.sazonpt.model;

public class Usuario {
    private int id_usuario;
    private String nombre;
    private String correo;
    private String contrasena;
    private String tipo;
    private int status;

    // Constructor vacío requerido por Jackson
    public Usuario() {
        this.status = 1; // Status por defecto
    }

    // Constructor sin status (usa 1 por defecto)
    public Usuario(String nombre, String correo, String contrasena, String tipo){
        this.nombre = nombre;
        this.correo = correo;
        this.contrasena = contrasena;
        this.tipo = tipo;
        this.status = 1; // Status por defecto
    }

    public Usuario(int id_usuario, String nombre, String correo, String contrasena, String tipo, int status){
        this.id_usuario=id_usuario;
        this.nombre=nombre;
        this.correo=correo;
        this.contrasena=contrasena;
        this.tipo=tipo;
        this.status = status;
    }

    public void setIdUsuario(int id_usuario){this.id_usuario = id_usuario;}
    public int getId_usuario() {return id_usuario;}
    
    public void setNombre(String nombre) {this.nombre = nombre;}
    public String getNombreU(){return nombre;}

    public void setCorreo(String correo){this.correo=correo;}
    public String getCorreo(){return correo;}

    public void setContrasena(String contrasena){this.contrasena=contrasena;}
    public String getContrasena(){return contrasena;}

    public void setTipo(String tipo){this.tipo=tipo;}
    public String getTipo(){return tipo;}

    public void setStatus(int status){this.status = status;}
    public int getStatus(){return status;}
}
