package org.sazonpt.model;

import java.time.LocalDateTime;

public class Restaurante {
    private int id_restaurante;
    private int id_restaurantero;
    private String nombre;
    private String direccion;
    private String telefono;
    private String horario; // JSON como String
    private String menu_url;
    private EstadoRestaurante estado;
    private Integer aprobado_por; // Puede ser null
    private LocalDateTime aprobado_en;
    private LocalDateTime creado_en;
    private LocalDateTime actualizado_en;
    private LocalDateTime eliminado_en;

    // Enum para el estado del restaurante
    public enum EstadoRestaurante {
        PENDIENTE("pendiente"),
        APROBADO("aprobado"),
        RECHAZADO("rechazado");

        private final String valor;

        EstadoRestaurante(String valor) {
            this.valor = valor;
        }

        public String getValor() {
            return valor;
        }

        public static EstadoRestaurante fromValor(String valor) {
            for (EstadoRestaurante estado : EstadoRestaurante.values()) {
                if (estado.valor.equals(valor)) {
                    return estado;
                }
            }
            throw new IllegalArgumentException("Estado no válido: " + valor);
        }
    }

    public Restaurante() {
        this.estado = EstadoRestaurante.PENDIENTE;
        this.creado_en = LocalDateTime.now();
        this.actualizado_en = LocalDateTime.now();
    }

    public Restaurante(int id_restaurantero, String nombre, String direccion, String telefono) {
        this();
        this.id_restaurantero = id_restaurantero;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
    }

    public int getId_restaurante() {
        return id_restaurante;
    }

    public void setId_restaurante(int id_restaurante) {
        this.id_restaurante = id_restaurante;
    }

    public int getId_restaurantero() {
        return id_restaurantero;
    }

    public void setId_restaurantero(int id_restaurantero) {
        this.id_restaurantero = id_restaurantero;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getMenu_url() {
        return menu_url;
    }

    public void setMenu_url(String menu_url) {
        this.menu_url = menu_url;
    }

    public EstadoRestaurante getEstado() {
        return estado;
    }

    public void setEstado(EstadoRestaurante estado) {
        this.estado = estado;
    }

    public Integer getAprobado_por() {
        return aprobado_por;
    }

    public void setAprobado_por(Integer aprobado_por) {
        this.aprobado_por = aprobado_por;
    }

    public LocalDateTime getAprobado_en() {
        return aprobado_en;
    }

    public void setAprobado_en(LocalDateTime aprobado_en) {
        this.aprobado_en = aprobado_en;
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

    // Métodos de utilidad
    public boolean estaAprobado() {
        return estado == EstadoRestaurante.APROBADO;
    }

    public boolean estaPendiente() {
        return estado == EstadoRestaurante.PENDIENTE;
    }

    public boolean estaRechazado() {
        return estado == EstadoRestaurante.RECHAZADO;
    }

    public boolean estaEliminado() {
        return eliminado_en != null;
    }

    public void aprobar(int adminId) {
        this.estado = EstadoRestaurante.APROBADO;
        this.aprobado_por = adminId;
        this.aprobado_en = LocalDateTime.now();
        this.actualizado_en = LocalDateTime.now();
    }

    public void rechazar(int adminId) {
        this.estado = EstadoRestaurante.RECHAZADO;
        this.aprobado_por = adminId;
        this.aprobado_en = LocalDateTime.now();
        this.actualizado_en = LocalDateTime.now();
    }

    public void marcarComoEliminado() {
        this.eliminado_en = LocalDateTime.now();
        this.actualizado_en = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Restaurante{" +
                "id_restaurante=" + id_restaurante +
                ", nombre='" + nombre + '\'' +
                ", estado=" + estado +
                ", id_restaurantero=" + id_restaurantero +
                '}';
    }
}
