package org.sazonpt.model;

public class Restaurantero {
    private String rfc;
    private boolean verificado;

    public Restaurantero(String rfc, boolean verificado) {
        this.rfc = rfc;
        this.verificado = verificado;
    }

    public String getRfc() {
        return rfc;
    }

    public void setRfc(String rfc) {
        this.rfc = rfc;
    }

    public boolean isVerificado() {
        return verificado;
    }

    public void setVerificado(boolean verificado) {
        this.verificado = verificado;
    }
}
