package org.sazonpt.model.dto;

public class RegistroRestauranteDTO {
    
    // Datos del restaurante
    private String nombreRestaurante;
    private String propietario;
    private String correoElectronico;
    private String numeroCelular;
    private String facebook;
    private String instagram;
    private String direccion;
    private String horarios;
    
    // Archivos
    private String imagenPrincipal;
    private String imagenSecundaria;
    private String imagenPlatillo;
    private String comprobanteDomicilio;  // PDF
    private String menuRestaurante;       // PDF
    
    // IDs necesarios
    private int idRestaurantero;
    private int idZona;
    
    // Constructor por defecto
    public RegistroRestauranteDTO() {
    }
    
    // Constructor completo
    public RegistroRestauranteDTO(String nombreRestaurante, String propietario, String correoElectronico,
                                 String numeroCelular, String facebook, String instagram, String direccion,
                                 String horarios, String imagenPrincipal, String imagenSecundaria,
                                 String imagenPlatillo, String comprobanteDomicilio, String menuRestaurante,
                                 int idRestaurantero, int idZona) {
        this.nombreRestaurante = nombreRestaurante;
        this.propietario = propietario;
        this.correoElectronico = correoElectronico;
        this.numeroCelular = numeroCelular;
        this.facebook = facebook;
        this.instagram = instagram;
        this.direccion = direccion;
        this.horarios = horarios;
        this.imagenPrincipal = imagenPrincipal;
        this.imagenSecundaria = imagenSecundaria;
        this.imagenPlatillo = imagenPlatillo;
        this.comprobanteDomicilio = comprobanteDomicilio;
        this.menuRestaurante = menuRestaurante;
        this.idRestaurantero = idRestaurantero;
        this.idZona = idZona;
    }
    
    // Getters y Setters
    public String getNombreRestaurante() {
        return nombreRestaurante;
    }
    
    public void setNombreRestaurante(String nombreRestaurante) {
        this.nombreRestaurante = nombreRestaurante;
    }
    
    public String getPropietario() {
        return propietario;
    }
    
    public void setPropietario(String propietario) {
        this.propietario = propietario;
    }
    
    public String getCorreoElectronico() {
        return correoElectronico;
    }
    
    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }
    
    public String getNumeroCelular() {
        return numeroCelular;
    }
    
    public void setNumeroCelular(String numeroCelular) {
        this.numeroCelular = numeroCelular;
    }
    
    public String getFacebook() {
        return facebook;
    }
    
    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }
    
    public String getInstagram() {
        return instagram;
    }
    
    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }
    
    public String getDireccion() {
        return direccion;
    }
    
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    
    public String getHorarios() {
        return horarios;
    }
    
    public void setHorarios(String horarios) {
        this.horarios = horarios;
    }
    
    public String getImagenPrincipal() {
        return imagenPrincipal;
    }
    
    public void setImagenPrincipal(String imagenPrincipal) {
        this.imagenPrincipal = imagenPrincipal;
    }
    
    public String getImagenSecundaria() {
        return imagenSecundaria;
    }
    
    public void setImagenSecundaria(String imagenSecundaria) {
        this.imagenSecundaria = imagenSecundaria;
    }
    
    public String getImagenPlatillo() {
        return imagenPlatillo;
    }
    
    public void setImagenPlatillo(String imagenPlatillo) {
        this.imagenPlatillo = imagenPlatillo;
    }
    
    public String getComprobanteDomicilio() {
        return comprobanteDomicilio;
    }
    
    public void setComprobanteDomicilio(String comprobanteDomicilio) {
        this.comprobanteDomicilio = comprobanteDomicilio;
    }
    
    public String getMenuRestaurante() {
        return menuRestaurante;
    }
    
    public void setMenuRestaurante(String menuRestaurante) {
        this.menuRestaurante = menuRestaurante;
    }
    
    public int getIdRestaurantero() {
        return idRestaurantero;
    }
    
    public void setIdRestaurantero(int idRestaurantero) {
        this.idRestaurantero = idRestaurantero;
    }
    
    public int getIdZona() {
        return idZona;
    }
    
    public void setIdZona(int idZona) {
        this.idZona = idZona;
    }
    
    @Override
    public String toString() {
        return "RegistroRestauranteDTO{" +
                "nombreRestaurante='" + nombreRestaurante + '\'' +
                ", propietario='" + propietario + '\'' +
                ", correoElectronico='" + correoElectronico + '\'' +
                ", numeroCelular='" + numeroCelular + '\'' +
                ", facebook='" + facebook + '\'' +
                ", instagram='" + instagram + '\'' +
                ", direccion='" + direccion + '\'' +
                ", horarios='" + horarios + '\'' +
                ", idRestaurantero=" + idRestaurantero +
                ", idZona=" + idZona +
                '}';
    }
}
