package org.sazonpt.model;

public class Administrador {
    private Usuario usuario;
    private Integer id_usuario;
    
    // Constructor vacío
    public Administrador() {
    }
    
    // Constructor con Usuario completo
    public Administrador(Usuario usuario) {
        this.usuario = usuario;
        this.id_usuario = usuario != null ? usuario.getIdUsuario() : null;
    }
    
    // Constructor solo con ID
    public Administrador(Integer id_usuario) {
        this.id_usuario = id_usuario;
    }
    
    // Getters y Setters
    public Usuario getUsuario() {
        return usuario;
    }
    
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        this.id_usuario = usuario != null ? usuario.getIdUsuario() : null;
    }
    
    public Integer getId_usuario() {
        return id_usuario;
    }
    
    public void setId_usuario(Integer id_usuario) {
        this.id_usuario = id_usuario;
    }
    
    // Métodos de conveniencia para acceder a datos del usuario
    public String getNombre() {
        return usuario != null ? usuario.getNombre() : null;
    }
    
    public String getCorreo() {
        return usuario != null ? usuario.getCorreo() : null;
    }
    
    public String getTipo() {
        return usuario != null ? usuario.getTipo() : null;
    }
    
    // Método toString
    @Override
    public String toString() {
        return "Administrador{" +
                "id_usuario=" + id_usuario +
                ", usuario=" + (usuario != null ? usuario.getNombre() : "null") +
                '}';
    }
    
    // Métodos equals y hashCode basados en el ID
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Administrador admin = (Administrador) obj;
        return id_usuario != null && id_usuario.equals(admin.id_usuario);
    }
    
    @Override
    public int hashCode() {
        return id_usuario != null ? id_usuario.hashCode() : 0;
    }
}
