package org.sazonpt.model;

import java.time.LocalDateTime;

public class Usuario {
    private int id_usuario;
    private String email;
    private String password_hash;
    private String nombre;
    private String telefono;
    private String avatar_url;
    private boolean activo;
    private String tipo_usuario;    // Nuevo campo para el tipo de usuario
    private LocalDateTime creado_en;
    private LocalDateTime actualizado_en;
    private LocalDateTime eliminado_en;
    
    public Usuario() {
        this.activo = true; // Valor por defecto
    }
    
    public Usuario(String email, String password_hash, String nombre, String telefono) {
        this.email = email;
        this.password_hash = password_hash;
        this.nombre = nombre;
        this.telefono = telefono;
        this.activo = true;
        this.creado_en = LocalDateTime.now();
        this.actualizado_en = LocalDateTime.now();
    }
    
    public int getId_usuario() {
        return id_usuario;
    }
    
    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPassword_hash() {
        return password_hash;
    }
    
    public void setPassword_hash(String password_hash) {
        this.password_hash = password_hash;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getTelefono() {
        return telefono;
    }
    
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    
    public String getAvatar_url() {
        return avatar_url;
    }
    
    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }
    
    public boolean isActivo() {
        return activo;
    }
    
    public void setActivo(boolean activo) {
        this.activo = activo;
    }
    
    public String getTipo_usuario() {
        return tipo_usuario;
    }
    
    public void setTipo_usuario(String tipo_usuario) {
        this.tipo_usuario = tipo_usuario;
    }
    
    public LocalDateTime getCreado_en() {
        return creado_en;
    }
    
    public void setCreado_en(LocalDateTime creado_en) {
        this.creado_en = creado_en;
    }
    
    public LocalDateTime getActualizado_en() {
        return actualizado_en;
    }
    
    public void setActualizado_en(LocalDateTime actualizado_en) {
        this.actualizado_en = actualizado_en;
    }
    
    public LocalDateTime getEliminado_en() {
        return eliminado_en;
    }
    
    public void setEliminado_en(LocalDateTime eliminado_en) {
        this.eliminado_en = eliminado_en;
    }
}
