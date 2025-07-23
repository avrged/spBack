package org.sazonpt.model;

public class Usuario {
    private int id_usuario;
    private String nombre;
    private String correo;
    private String contrasena;
    private String tipo;
    private String status;

    public Usuario() {
        this.status = "activo";
    }

    // Constructor sin status (usa "activo" por defecto)
    public Usuario(String nombre, String correo, String contrasena, String tipo){
        this.nombre = nombre;
        this.correo = correo;
        this.contrasena = contrasena;
        this.tipo = tipo;
        this.status = "activo";
    }

    public Usuario(int id_usuario, String nombre, String correo, String contrasena, String tipo, String status){
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
    public String getNombre(){return nombre;}

    public void setCorreo(String correo){this.correo=correo;}
    public String getCorreo(){return correo;}

    public void setContrasena(String contrasena){this.contrasena=contrasena;}
    public String getContrasena(){return contrasena;}

    public void setTipo(String tipo){this.tipo=tipo;}
    public String getTipo(){return tipo;}

    public void setStatus(String status){this.status = status;}
    public String getStatus(){return status;}
}
