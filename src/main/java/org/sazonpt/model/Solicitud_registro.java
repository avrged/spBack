package org.sazonpt.model;

import java.time.LocalDateTime;

public class Solicitud_registro {
    private int id_solicitud;
    private LocalDateTime fecha;
    private String estado;
    private String nombre_propuesto_restaurante;
    private String correo;
    private String nombre_propietario;
    private String horario_atencion;
    private String direccion;
    private int id_restaurantero;

    public enum EstadoSolicitud {
        PENDIENTE("pendiente"),
        APROBADA("aprobada"),
        RECHAZADA("rechazada");

        private final String valor;

        EstadoSolicitud(String valor) {
            this.valor = valor;
        }

        public String getValor() {
            return valor;
        }

        public static EstadoSolicitud fromString(String valor) {
            for (EstadoSolicitud estado : EstadoSolicitud.values()) {
                if (estado.valor.equalsIgnoreCase(valor)) {
                    return estado;
                }
            }
            throw new IllegalArgumentException("Estado de solicitud no válido: " + valor);
        }
    }

    // Constructor vacío
    public Solicitud_registro() {}

    // Constructor completo
    public Solicitud_registro(int id_solicitud, LocalDateTime fecha, String estado, 
                             String nombre_propuesto_restaurante, String correo, 
                             String nombre_propietario, String horario_atencion, 
                             String direccion, int id_restaurantero) {
        this.id_solicitud = id_solicitud;
        this.fecha = fecha;
        this.estado = estado;
        this.nombre_propuesto_restaurante = nombre_propuesto_restaurante;
        this.correo = correo;
        this.nombre_propietario = nombre_propietario;
        this.horario_atencion = horario_atencion;
        this.direccion = direccion;
        this.id_restaurantero = id_restaurantero;
    }

    // Getters
    public int getId_solicitud() { return id_solicitud; }
    public LocalDateTime getFecha() { return fecha; }
    public String getEstado() { return estado; }
    public String getNombre_propuesto_restaurante() { return nombre_propuesto_restaurante; }
    public String getCorreo() { return correo; }
    public String getNombre_propietario() { return nombre_propietario; }
    public String getHorario_atencion() { return horario_atencion; }
    public String getDireccion() { return direccion; }
    public int getId_restaurantero() { return id_restaurantero; }

    // Setters
    public void setId_solicitud(int id_solicitud) { this.id_solicitud = id_solicitud; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
    public void setEstado(String estado) { this.estado = estado; }
    public void setNombre_propuesto_restaurante(String nombre_propuesto_restaurante) { 
        this.nombre_propuesto_restaurante = nombre_propuesto_restaurante; 
    }
    public void setCorreo(String correo) { this.correo = correo; }
    public void setNombre_propietario(String nombre_propietario) { 
        this.nombre_propietario = nombre_propietario; 
    }
    public void setHorario_atencion(String horario_atencion) { 
        this.horario_atencion = horario_atencion; 
    }
    public void setDireccion(String direccion) { 
        this.direccion = direccion; 
    }
    public void setId_restaurantero(int id_restaurantero) { 
        this.id_restaurantero = id_restaurantero; 
    }
}
