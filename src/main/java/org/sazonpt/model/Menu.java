package org.sazonpt.model;

public class Menu {
    private int id_menu;
    private String telefono;
    private String ruta_archivo;

    public Menu() {
    }

    public Menu(int id_menu, String telefono, String ruta_archivo) {
        this.id_menu = id_menu;
        this.telefono = telefono;
        this.ruta_archivo = ruta_archivo;
    }

    public int getId_menu() {return id_menu;}
    public String getTelefono() {return telefono;}
    public String getRuta_archivo() {return ruta_archivo;}

    public void setId_menu(int id_menu) {this.id_menu = id_menu;}
    public void setTelefono(String telefono) {this.telefono = telefono;}
    public void setRuta_archivo(String ruta_archivo) {this.ruta_archivo = ruta_archivo;}

}
