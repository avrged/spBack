
package org.sazonpt.model;

public class Estadistica {
    private int id_estadistica;
    private Integer nacional;
    private Integer extranjero;
    private String correo;

    public Estadistica() {}

    public Estadistica(int id_estadistica, Integer nacional, Integer extranjero, String correo) {
        this.id_estadistica = id_estadistica;
        this.nacional = nacional;
        this.extranjero = extranjero;
        this.correo = correo;
    }

    public int getId_estadistica() { return id_estadistica; }
    public Integer getNacional() { return nacional; }
    public Integer getExtranjero() { return extranjero; }
    public String getCorreo() { return correo; }

    public void setId_estadistica(int id_estadistica) { this.id_estadistica = id_estadistica; }
    public void setNacional(Integer nacional) { this.nacional = nacional; }
    public void setExtranjero(Integer extranjero) { this.extranjero = extranjero; }
    public void setCorreo(String correo) { this.correo = correo; }
}
