package org.sazonpt.model;

public class Restaurantero {
    private Usuario usuario;
    private Integer id_usuario;
    
    // Constructor vacío
    public Restaurantero() {
    }
    
    // Constructor con Usuario completo
    public Restaurantero(Usuario usuario) {
        this.usuario = usuario;
        this.id_usuario = usuario != null ? usuario.getId_usuario() : null;
    }
    
    // Constructor solo con ID
    public Restaurantero(Integer id_usuario) {
        this.id_usuario = id_usuario;
    }
    
    // Getters y Setters
    public Usuario getUsuario() {
        return usuario;
    }
    
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        this.id_usuario = usuario != null ? usuario.getId_usuario() : null;
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
        return "Restaurantero{" +
                "id_usuario=" + id_usuario +
                ", usuario=" + (usuario != null ? usuario.getNombre() : "null") +
                '}';
    }
    
    // Métodos equals y hashCode basados en el ID
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Restaurantero restaurantero = (Restaurantero) obj;
        return id_usuario != null && id_usuario.equals(restaurantero.id_usuario);
    }
    
    @Override
    public int hashCode() {
        return id_usuario != null ? id_usuario.hashCode() : 0;
    }
}
