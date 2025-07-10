package org.sazonpt.model;

public class Administrador extends Usuario{
    private int codigo_usuario;

    public Administrador(int id_usuario, String nombre, String correo, String contrasena, String tipo, int codigo_usuario){
        super(id_usuario, nombre, correo, contrasena, tipo);
        this.codigo_usuario = codigo_usuario;
    }

    
    public int getCodigo_admin() {return codigo_usuario;}
}
