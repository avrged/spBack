package org.sazonpt.model;

import java.time.LocalDateTime;

public class Solicitud_registro {
    
    private int id_solicitud;
    private int id_restaurantero;
    private EstadoSolicitud estado;
    private String datos_restaurante; // JSON como String
    private Integer revisado_por;
    private LocalDateTime revisado_en;
    private LocalDateTime creado_en;
    
    public enum EstadoSolicitud {
        PENDIENTE("pendiente"),
        APROBADO("aprobado"),
        RECHAZADO("rechazado");
        
        private final String valor;
        
        EstadoSolicitud(String valor) {
            this.valor = valor;
        }
        
        public String getValor() {
            return valor;
        }
        
        public static EstadoSolicitud fromValor(String valor) {
            for (EstadoSolicitud estado : EstadoSolicitud.values()) {
                if (estado.valor.equals(valor)) {
                    return estado;
                }
            }
            throw new IllegalArgumentException("Estado de solicitud no válido: " + valor);
        }
    }
    
    public Solicitud_registro() {
        this.estado = EstadoSolicitud.PENDIENTE;
        this.creado_en = LocalDateTime.now();
    }
    
    public Solicitud_registro(int id_restaurantero) {
        this();
        this.id_restaurantero = id_restaurantero;
    }
    
    // Constructor completo con datos del restaurante
    public Solicitud_registro(int id_restaurantero, String datos_restaurante) {
        this(id_restaurantero);
        this.datos_restaurante = datos_restaurante;
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
    
    public EstadoSolicitud getEstado() {
        return estado;
    }
    
    public void setEstado(EstadoSolicitud estado) {
        this.estado = estado;
    }
    
    public String getDatos_restaurante() {
        return datos_restaurante;
    }
    
    public void setDatos_restaurante(String datos_restaurante) {
        this.datos_restaurante = datos_restaurante;
    }
    
    public Integer getRevisado_por() {
        return revisado_por;
    }
    
    public void setRevisado_por(Integer revisado_por) {
        this.revisado_por = revisado_por;
    }
    
    public LocalDateTime getRevisado_en() {
        return revisado_en;
    }
    
    public void setRevisado_en(LocalDateTime revisado_en) {
        this.revisado_en = revisado_en;
    }
    
    public LocalDateTime getCreado_en() {
        return creado_en;
    }
    
    public void setCreado_en(LocalDateTime creado_en) {
        this.creado_en = creado_en;
    }
    
    // Métodos de utilidad
    public boolean estaPendiente() {
        return estado == EstadoSolicitud.PENDIENTE;
    }
    
    public boolean estaAprobado() {
        return estado == EstadoSolicitud.APROBADO;
    }
    
    public boolean estaRechazado() {
        return estado == EstadoSolicitud.RECHAZADO;
    }
    
    public boolean hasSidoRevisado() {
        return revisado_por != null && revisado_en != null;
    }

    public void aprobar(int adminId) {
        this.estado = EstadoSolicitud.APROBADO;
        this.revisado_por = adminId;
        this.revisado_en = LocalDateTime.now();
    }

    public void rechazar(int adminId, String motivo) {
        this.estado = EstadoSolicitud.RECHAZADO;
        this.revisado_por = adminId;
        this.revisado_en = LocalDateTime.now();
        // El motivo se almacena en datos_restaurante si es necesario
    }
    
    public void rechazar(int adminId) {
        rechazar(adminId, null);
    }
    
    // NUEVO: Métodos para manejar JSON
    public boolean tieneDatosRestaurante() {
        return datos_restaurante != null && !datos_restaurante.trim().isEmpty();
    }
    
    @Override
    public String toString() {
        return "Solicitud_registro{" +
                "id_solicitud=" + id_solicitud +
                ", id_restaurantero=" + id_restaurantero +
                ", estado=" + estado +
                ", revisado_por=" + revisado_por +
                ", creado_en=" + creado_en +
                ", tieneDatos=" + tieneDatosRestaurante() +
                '}';
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Solicitud_registro that = (Solicitud_registro) obj;
        return id_solicitud == that.id_solicitud;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(id_solicitud);
    }
}
