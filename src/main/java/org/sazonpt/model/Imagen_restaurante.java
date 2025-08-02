package org.sazonpt.model;

import java.time.LocalDateTime;

public class Imagen_restaurante {
    
    private int id_imagen;
    private int id_restaurante;
    private String url;
    private TipoImagen tipo;
    private LocalDateTime creado_en;
    
    // Enum para el tipo de imagen
    public enum TipoImagen {
        PRINCIPAL("principal"),
        SECUNDARIA("secundaria"),
        PLATILLO("platillo");
        
        private final String valor;
        
        TipoImagen(String valor) {
            this.valor = valor;
        }
        
        public String getValor() {
            return valor;
        }
        
        // Método para convertir desde String
        public static TipoImagen fromValor(String valor) {
            for (TipoImagen tipo : TipoImagen.values()) {
                if (tipo.valor.equalsIgnoreCase(valor)) {
                    return tipo;
                }
            }
            throw new IllegalArgumentException("Tipo de imagen no válido: " + valor);
        }
    }
    
    // Constructores
    public Imagen_restaurante() {
        this.creado_en = LocalDateTime.now();
    }
    
    public Imagen_restaurante(int id_restaurante, String url, TipoImagen tipo) {
        this();
        this.id_restaurante = id_restaurante;
        this.url = url;
        this.tipo = tipo;
    }
    
    public Imagen_restaurante(int id_restaurante, String url, String tipo) {
        this(id_restaurante, url, TipoImagen.fromValor(tipo));
    }
    
    // Getters y Setters
    public int getId_imagen() {
        return id_imagen;
    }
    
    public void setId_imagen(int id_imagen) {
        this.id_imagen = id_imagen;
    }
    
    public int getId_restaurante() {
        return id_restaurante;
    }
    
    public void setId_restaurante(int id_restaurante) {
        this.id_restaurante = id_restaurante;
    }
    
    public String getUrl() {
        return url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
    
    public TipoImagen getTipo() {
        return tipo;
    }
    
    public void setTipo(TipoImagen tipo) {
        this.tipo = tipo;
    }
    
    public void setTipo(String tipo) {
        this.tipo = TipoImagen.fromValor(tipo);
    }
    
    public LocalDateTime getCreado_en() {
        return creado_en;
    }
    
    public void setCreado_en(LocalDateTime creado_en) {
        this.creado_en = creado_en;
    }
    
    // Métodos de utilidad
    public boolean esPrincipal() {
        return this.tipo == TipoImagen.PRINCIPAL;
    }
    
    public boolean esSecundaria() {
        return this.tipo == TipoImagen.SECUNDARIA;
    }
    
    public boolean esPlatillo() {
        return this.tipo == TipoImagen.PLATILLO;
    }
    
    public boolean tieneUrl() {
        return this.url != null && !this.url.trim().isEmpty();
    }

    public void validar() {
        if (id_restaurante <= 0) {
            throw new IllegalArgumentException("El ID del restaurante debe ser mayor a 0");
        }
        
        if (url == null || url.trim().isEmpty()) {
            throw new IllegalArgumentException("La URL de la imagen es obligatoria");
        }
        
        if (tipo == null) {
            throw new IllegalArgumentException("El tipo de imagen es obligatorio");
        }
        
        // Validación básica de URL
        if (!url.matches("^(https?://.*|[a-zA-Z0-9._-]+\\.(jpg|jpeg|png|gif|webp))$")) {
            throw new IllegalArgumentException("La URL de la imagen no tiene un formato válido");
        }
    }
    
    @Override
    public String toString() {
        return "Imagen_restaurante{" +
                "id_imagen=" + id_imagen +
                ", id_restaurante=" + id_restaurante +
                ", url='" + url + '\'' +
                ", tipo=" + (tipo != null ? tipo.getValor() : null) +
                ", creado_en=" + creado_en +
                '}';
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Imagen_restaurante that = (Imagen_restaurante) obj;
        return id_imagen == that.id_imagen;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(id_imagen);
    }
}
