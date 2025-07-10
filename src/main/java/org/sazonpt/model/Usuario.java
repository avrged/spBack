package org.sazonpt.model;

public class Usuario {
    private int id_usuario;
    private String nombre;
    private String correo;
    private String contrasena;
    private String tipo;

    public Usuario(int id_usuario, String nombre, String correo, String contrasena, String tipo){
        this.id_usuario=id_usuario;
        this.nombre=nombre;
        this.correo=correo;
        this.contrasena=contrasena;
        this.tipo=tipo;
    }

    public int getId_usuario() {return id_usuario;}
    public String getNombreU(){return nombre;}

    public void setCorreo(String correo){this.correo=correo;}
    public String getCorreo(){return correo;}

    public void setContrasena(String contrasena){this.contrasena=contrasena;}
    public String getContrasena(){return contrasena;}

    public void setTipo(String tipo){this.tipo=tipo;}
    public String getTipo(){return tipo;}
}
