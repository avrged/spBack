package org.sazonpt.model;

public class Menu {
    private int id_menu;
    private int id_restaurante;
    private String ruta_archivo;
    private String estado;

    // Constructor por defecto
    public Menu() {
    }

    public Menu(int id_menu, int id_restaurante, String ruta_archivo, String estado) {
        this.id_menu = id_menu;
        this.id_restaurante = id_restaurante;
        this.ruta_archivo = ruta_archivo;
        this.estado = estado;
    }

    // Getters nuevos
    public int getIdMenu() {return id_menu;}
    public int getId_restaurante() {return id_restaurante;}
    public String getRuta_archivo() {return ruta_archivo;}
    public String getEstado() {return estado;}

    // Setters nuevos
    public void setId_menu(int id_menu) {this.id_menu = id_menu;}
    public void setId_restaurante(int id_restaurante) {this.id_restaurante = id_restaurante;}
    public void setRuta_archivo(String ruta_archivo) {this.ruta_archivo = ruta_archivo;}
    public void setEstado(String estado) {this.estado = estado;}

    // Métodos de compatibilidad (mantener nombres antiguos)
    public int getCodigo_restaurante() {return id_restaurante;}
    public void setEstadoMenu(String estado) {this.estado = estado;}
}
