package org.sazonpt.model;

public class Menu {
    
    private int id_menu;
    private String ruta_archivo;
    private String ruta_menu;  // Campo adicional solicitado
    private String estado;
    private int id_restaurante;
    private int id_solicitud;
    private int id_restaurantero;
    
    // Enum para estados del menú
    public enum EstadoMenu {
        ACTIVO("activo"),
        INACTIVO("inactivo"),
        PENDIENTE("pendiente"),
        REVISION("revision");
        
        private final String valor;
        
        EstadoMenu(String valor) {
            this.valor = valor;
        }
        
        public String getValor() {
            return valor;
        }
        
        public static EstadoMenu fromString(String valor) {
            for (EstadoMenu estado : EstadoMenu.values()) {
                if (estado.valor.equalsIgnoreCase(valor)) {
                    return estado;
                }
            }
            throw new IllegalArgumentException("Estado de menú inválido: " + valor);
        }
    }
    
    // Constructor por defecto
    public Menu() {
    }
    
    // Constructor completo
    public Menu(int id_menu, String ruta_archivo, String ruta_menu, String estado, 
                int id_restaurante, int id_solicitud, int id_restaurantero) {
        this.id_menu = id_menu;
        this.ruta_archivo = ruta_archivo;
        this.ruta_menu = ruta_menu;
        this.estado = estado;
        this.id_restaurante = id_restaurante;
        this.id_solicitud = id_solicitud;
        this.id_restaurantero = id_restaurantero;
    }
    
    // Constructor sin ID (para crear nuevos registros)
    public Menu(String ruta_archivo, String ruta_menu, String estado, 
                int id_restaurante, int id_solicitud, int id_restaurantero) {
        this.ruta_archivo = ruta_archivo;
        this.ruta_menu = ruta_menu;
        this.estado = estado;
        this.id_restaurante = id_restaurante;
        this.id_solicitud = id_solicitud;
        this.id_restaurantero = id_restaurantero;
    }
    
    // Getters y Setters
    public int getId_menu() {
        return id_menu;
    }
    
    public void setId_menu(int id_menu) {
        this.id_menu = id_menu;
    }
    
    public String getRuta_archivo() {
        return ruta_archivo;
    }
    
    public void setRuta_archivo(String ruta_archivo) {
        this.ruta_archivo = ruta_archivo;
    }
    
    public String getRuta_menu() {
        return ruta_menu;
    }
    
    public void setRuta_menu(String ruta_menu) {
        this.ruta_menu = ruta_menu;
    }
    
    public String getEstado() {
        return estado;
    }
    
    public void setEstado(String estado) {
        this.estado = estado;
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
    
    @Override
    public String toString() {
        return "Menu{" +
                "id_menu=" + id_menu +
                ", ruta_archivo='" + ruta_archivo + '\'' +
                ", ruta_menu='" + ruta_menu + '\'' +
                ", estado='" + estado + '\'' +
                ", id_restaurante=" + id_restaurante +
                ", id_solicitud=" + id_solicitud +
                ", id_restaurantero=" + id_restaurantero +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        Menu menu = (Menu) o;
        
        if (id_menu != menu.id_menu) return false;
        if (id_restaurante != menu.id_restaurante) return false;
        if (id_solicitud != menu.id_solicitud) return false;
        return id_restaurantero == menu.id_restaurantero;
    }
    
    @Override
    public int hashCode() {
        int result = id_menu;
        result = 31 * result + id_restaurante;
        result = 31 * result + id_solicitud;
        result = 31 * result + id_restaurantero;
        return result;
    }
}
