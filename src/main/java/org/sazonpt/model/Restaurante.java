package org.sazonpt.model;

public class Restaurante {
    private int id_restaurante;
    private String nombre;
    private String horario;
    private String telefono;
    private String etiquetas;
    private int id_solicitud;
    private int id_restaurantero;
    private int id_zona;

    public enum EstadoRestaurante {
        ACTIVO("activo"),
        INACTIVO("inactivo"),
        SUSPENDIDO("suspendido");

        private final String valor;

        EstadoRestaurante(String valor) {
            this.valor = valor;
        }

        public String getValor() {
            return valor;
        }

        public static EstadoRestaurante fromString(String valor) {
            for (EstadoRestaurante estado : EstadoRestaurante.values()) {
                if (estado.valor.equalsIgnoreCase(valor)) {
                    return estado;
                }
            }
            throw new IllegalArgumentException("Estado de restaurante no válido: " + valor);
        }
    }

    // Constructor vacío
    public Restaurante() {}

    // Constructor completo
    public Restaurante(int id_restaurante, String nombre, String horario, String telefono, 
                      String etiquetas, int id_solicitud, int id_restaurantero, int id_zona) {
        this.id_restaurante = id_restaurante;
        this.nombre = nombre;
        this.horario = horario;
        this.telefono = telefono;
        this.etiquetas = etiquetas;
        this.id_solicitud = id_solicitud;
        this.id_restaurantero = id_restaurantero;
        this.id_zona = id_zona;
    }

    // Constructor para crear desde solicitud aprobada
    public Restaurante(String nombre, String horario, String telefono, String etiquetas,
                      int id_solicitud, int id_restaurantero, int id_zona) {
        this.nombre = nombre;
        this.horario = horario;
        this.telefono = telefono;
        this.etiquetas = etiquetas;
        this.id_solicitud = id_solicitud;
        this.id_restaurantero = id_restaurantero;
        this.id_zona = id_zona;
    }

    // Getters
    public int getId_restaurante() { return id_restaurante; }
    public String getNombre() { return nombre; }
    public String getHorario() { return horario; }
    public String getTelefono() { return telefono; }
    public String getEtiquetas() { return etiquetas; }
    public int getId_solicitud() { return id_solicitud; }
    public int getId_restaurantero() { return id_restaurantero; }
    public int getId_zona() { return id_zona; }

    // Setters
    public void setId_restaurante(int id_restaurante) { this.id_restaurante = id_restaurante; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setHorario(String horario) { this.horario = horario; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public void setEtiquetas(String etiquetas) { this.etiquetas = etiquetas; }
    public void setId_solicitud(int id_solicitud) { this.id_solicitud = id_solicitud; }
    public void setId_restaurantero(int id_restaurantero) { this.id_restaurantero = id_restaurantero; }
    public void setId_zona(int id_zona) { this.id_zona = id_zona; }

    // Método toString
    @Override
    public String toString() {
        return "Restaurante{" +
                "id_restaurante=" + id_restaurante +
                ", nombre='" + nombre + '\'' +
                ", horario='" + horario + '\'' +
                ", telefono='" + telefono + '\'' +
                ", etiquetas='" + etiquetas + '\'' +
                ", id_solicitud=" + id_solicitud +
                ", id_restaurantero=" + id_restaurantero +
                ", id_zona=" + id_zona +
                '}';
    }

    // Métodos equals y hashCode basados en el ID
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Restaurante restaurante = (Restaurante) obj;
        return id_restaurante == restaurante.id_restaurante;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id_restaurante);
    }
}