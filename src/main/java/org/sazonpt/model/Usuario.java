package org.sazonpt.model;

public class Usuario {
    private Integer id_usuario;
    private String nombre;
    private String correo;
    private String contrasena;
    private String tipo;
    
    // Constructor vacío
    public Usuario() {
    }
    
    // Constructor con todos los parámetros
    public Usuario(Integer id_usuario, String nombre, String correo, String contrasena, String tipo) {
        this.id_usuario = id_usuario;
        this.nombre = nombre;
        this.correo = correo;
        this.contrasena = contrasena;
        this.tipo = tipo;
    }
    
    // Constructor sin ID (útil para crear nuevos usuarios)
    public Usuario(String nombre, String correo, String contrasena, String tipo) {
        this.nombre = nombre;
        this.correo = correo;
        this.contrasena = contrasena;
        this.tipo = tipo;
    }
    
    // Getters y Setters
    public Integer getIdUsuario() {
        return id_usuario;
    }
    
    public void setIdUsuario(Integer id_usuario) {
        this.id_usuario = id_usuario;
    }
    
    public Integer getId_usuario() {
        return id_usuario;
    }
    
    public void setId_usuario(Integer id_usuario) {
        this.id_usuario = id_usuario;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getCorreo() {
        return correo;
    }
    
    public void setCorreo(String correo) {
        this.correo = correo;
    }
    
    public String getContrasena() {
        return contrasena;
    }
    
    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
    
    public String getTipo() {
        return tipo;
    }
    
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
    // Método toString para facilitar el debugging
    @Override
    public String toString() {
        return "Usuario{" +
                "id_usuario=" + id_usuario +
                ", nombre='" + nombre + '\'' +
                ", correo='" + correo + '\'' +
                ", tipo='" + tipo + '\'' +
                '}';
    }
    
    // Método equals y hashCode basados en el ID
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Usuario usuario = (Usuario) obj;
        return id_usuario != null && id_usuario.equals(usuario.id_usuario);
    }
    
    @Override
    public int hashCode() {
        return id_usuario != null ? id_usuario.hashCode() : 0;
    }
}
