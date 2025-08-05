package org.sazonpt.model;

import java.time.LocalDateTime;

public class Comprobante {
    
    private int id_comprobante;
    private String tipo;
    private String ruta_archivo;
    private LocalDateTime fecha_subida;
    private int id_restaurante;
    private int id_solicitud;
    private int id_restaurantero;
    // private int id_zona;
    
    // Enum para tipos de comprobante
    public enum TipoComprobante {
        COMPROBANTE_DOMICILIO("comprobante_domicilio"),
        MENU_RESTAURANTE("menu_restaurante"),
        LICENCIA_FUNCIONAMIENTO("licencia_funcionamiento"),
        PERMISO_SANIDAD("permiso_sanidad"),
        CERTIFICADO_MANIPULACION("certificado_manipulacion"),
        RUC("ruc"),
        OTRO("otro");
        
        private final String valor;
        
        TipoComprobante(String valor) {
            this.valor = valor;
        }
        
        public String getValor() {
            return valor;
        }
        
        public static TipoComprobante fromString(String valor) {
            for (TipoComprobante tipo : TipoComprobante.values()) {
                if (tipo.valor.equalsIgnoreCase(valor)) {
                    return tipo;
                }
            }
            throw new IllegalArgumentException("Tipo de comprobante inválido: " + valor);
        }
    }
    
    // Constructor por defecto
    public Comprobante() {
    }
    
    // Constructor completo
    public Comprobante(int id_comprobante, String tipo, String ruta_archivo,
                      LocalDateTime fecha_subida, int id_restaurante, int id_solicitud, 
                      int id_restaurantero) {
        this.id_comprobante = id_comprobante;
        this.tipo = tipo;
        this.ruta_archivo = ruta_archivo;
        this.fecha_subida = fecha_subida;
        this.id_restaurante = id_restaurante;
        this.id_solicitud = id_solicitud;
        this.id_restaurantero = id_restaurantero;
    }
    
    // Constructor sin ID (para crear nuevos registros)
    public Comprobante(String tipo, String ruta_archivo, LocalDateTime fecha_subida, 
                      int id_restaurante, int id_solicitud, int id_restaurantero) {
        this.tipo = tipo;
        this.ruta_archivo = ruta_archivo;
        this.fecha_subida = fecha_subida;
        this.id_restaurante = id_restaurante;
        this.id_solicitud = id_solicitud;
        this.id_restaurantero = id_restaurantero;
    }
    
    // Getters y Setters
    public int getId_comprobante() {
        return id_comprobante;
    }
    
    public void setId_comprobante(int id_comprobante) {
        this.id_comprobante = id_comprobante;
    }
    
    public String getTipo() {
        return tipo;
    }
    
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
    public String getRuta_archivo() {
        return ruta_archivo;
    }
    
    public void setRuta_archivo(String ruta_archivo) {
        this.ruta_archivo = ruta_archivo;
    }
    
    public LocalDateTime getFecha_subida() {
        return fecha_subida;
    }
    
    public void setFecha_subida(LocalDateTime fecha_subida) {
        this.fecha_subida = fecha_subida;
    }
    
    public int getId_restaurante() {
        return id_restaurante;
    }
    
    public void setId_restaurante(int id_restaurante) {
        this.id_restaurante = id_restaurante;
    }
    
    public int getId_solicitud() {
        return id_solicitud;
    }
    
    public void setId_solicitud(int id_solicitud) {
        this.id_solicitud = id_solicitud;
    }
    
    public int getId_restaurantero() {
        return id_restaurantero;
    }
    
    public void setId_restaurantero(int id_restaurantero) {
        this.id_restaurantero = id_restaurantero;
    }
    
    // public int getId_zona() { return id_zona; }
    // public void setId_zona(int id_zona) { this.id_zona = id_zona; }
    
    @Override
    public String toString() {
        return "Comprobante{" +
                "id_comprobante=" + id_comprobante +
                ", tipo='" + tipo + '\'' +
                ", ruta_archivo='" + ruta_archivo + '\'' +
                ", fecha_subida=" + fecha_subida +
                ", id_restaurante=" + id_restaurante +
                ", id_solicitud=" + id_solicitud +
                ", id_restaurantero=" + id_restaurantero +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        Comprobante that = (Comprobante) o;
        
        if (id_comprobante != that.id_comprobante) return false;
        if (id_restaurante != that.id_restaurante) return false;
        if (id_solicitud != that.id_solicitud) return false;
        if (id_restaurantero != that.id_restaurantero) return false;
        return true;
    }
    
    @Override
    public int hashCode() {
        int result = id_comprobante;
        result = 31 * result + id_restaurante;
        result = 31 * result + id_solicitud;
        result = 31 * result + id_restaurantero;
        return result;
    }
}
