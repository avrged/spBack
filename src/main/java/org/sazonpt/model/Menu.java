package org.sazonpt.model;

public class Menu {
    private int id_menu;
    private int codigo_restaurante;
    private String ruta_archivo;
    private boolean estado;

    public Menu(int id_menu, int codigo_restaurante, String ruta_archivo, boolean estado) {
        this.id_menu = id_menu;
        this.codigo_restaurante = codigo_restaurante;
        this.ruta_archivo = ruta_archivo;
        this.estado = estado;
    }

    public int getIdMenu() {return id_menu;}
    public int  getCodigo_restaurante() {return codigo_restaurante;}
    public String getRuta_archivo() {return ruta_archivo;}

    public void setEstadoMenu(boolean estado) {this.estado = estado;}
    public boolean getEstado() {return estado;}
}
