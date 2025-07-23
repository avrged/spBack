
package org.sazonpt.model;

public class Estadistica {
    private int id_estadistica;
    private Integer nacional;
    private Integer extranjero;
    private String correo;
    private int descargas;
    private int comida;
    private int ubicacion;
    private int recomendacion;
    private int horario;
    private int vista;

    public Estadistica() {}

    public Estadistica(int id_estadistica, Integer nacional, Integer extranjero, String correo, int descargas, int comida, int ubicacion, int recomendacion, int horario, int vista) {
        this.id_estadistica = id_estadistica;
        this.nacional = nacional;
        this.extranjero = extranjero;
        this.correo = correo;
        this.descargas = descargas;
        this.comida = comida;
        this.ubicacion = ubicacion;
        this.recomendacion = recomendacion;
        this.horario = horario;
        this.vista = vista;
    }

    public int getId_estadistica() { return id_estadistica; }
    public Integer getNacional() { return nacional; }
    public Integer getExtranjero() { return extranjero; }
    public String getCorreo() { return correo; }
    public int getDescargas() { return descargas; }
    public int getComida() { return comida; }
    public int getUbicacion() { return ubicacion; }
    public int getRecomendacion() { return recomendacion; }
    public int getHorario() { return horario; }
    public int getVista() { return vista; }

    public void setId_estadistica(int id_estadistica) { this.id_estadistica = id_estadistica; }
    public void setNacional(Integer nacional) { this.nacional = nacional; }
    public void setExtranjero(Integer extranjero) { this.extranjero = extranjero; }
    public void setCorreo(String correo) { this.correo = correo; }
    public void setDescargas(int descargas) { this.descargas = descargas; }
    public void setComida(int comida) { this.comida = comida; }
    public void setUbicacion(int ubicacion) { this.ubicacion = ubicacion; }
    public void setRecomendacion(int recomendacion) { this.recomendacion = recomendacion; }
    public void setHorario(int horario) { this.horario = horario; }
    public void setVista(int vista) { this.vista = vista; }
}
