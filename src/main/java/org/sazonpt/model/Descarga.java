package org.sazonpt.model;

public class Descarga {
    private int id_descarga;
    private int cantidad_descargas;
    private String origen;
    private String opinion;
    private int id_restaurantero;

    public Descarga() {}

    public Descarga(int id_descarga, int cantidad_descargas, String origen, 
                   String opinion, int id_restaurantero) {
        this.id_descarga = id_descarga;
        this.cantidad_descargas = cantidad_descargas;
        this.origen = origen;
        this.opinion = opinion;
        this.id_restaurantero = id_restaurantero;
    }

    public Descarga(int cantidad_descargas, String origen, String opinion, int id_restaurantero) {
        this.cantidad_descargas = cantidad_descargas;
        this.origen = origen;
        this.opinion = opinion;
        this.id_restaurantero = id_restaurantero;
    }

    public int getId_descarga() { return id_descarga; }
    public int getCantidad_descargas() { return cantidad_descargas; }
    public String getOrigen() { return origen; }
    public String getOpinion() { return opinion; }
    public int getId_restaurantero() { return id_restaurantero; }

    public void setId_descarga(int id_descarga) { this.id_descarga = id_descarga; }
    public void setCantidad_descargas(int cantidad_descargas) { this.cantidad_descargas = cantidad_descargas; }
    public void setOrigen(String origen) { this.origen = origen; }
    public void setOpinion(String opinion) { this.opinion = opinion; }
    public void setId_restaurantero(int id_restaurantero) { this.id_restaurantero = id_restaurantero; }

    public enum Origen {
        NACIONAL("Nacional"),
        EXTRANJERO("Extranjero");

        private final String valor;

        Origen(String valor) {
            this.valor = valor;
        }

        public String getValor() {
            return valor;
        }

        public static boolean esValido(String origen) {
            for (Origen o : values()) {
                if (o.valor.equals(origen)) {
                    return true;
                }
            }
            return false;
        }
    }

    public enum TipoOpinion {
        LA_COMIDA("La comida"),
        LA_UBICACION("La ubicacion"),
        RECOMENDACION("Recomendacion"),
        EL_HORARIO("El horario"),
        LA_VISTA("La vista");

        private final String valor;

        TipoOpinion(String valor) {
            this.valor = valor;
        }

        public String getValor() {
            return valor;
        }

        public static boolean esValido(String opinion) {
            for (TipoOpinion o : values()) {
                if (o.valor.equals(opinion)) {
                    return true;
                }
            }
            return false;
        }
    }

    @Override
    public String toString() {
        return "descarga{" +
                "id_descarga=" + id_descarga +
                ", cantidad_descargas=" + cantidad_descargas +
                ", origen='" + origen + '\'' +
                ", opinion='" + opinion + '\'' +
                ", id_restaurantero=" + id_restaurantero +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Descarga descarga = (Descarga) obj;
        return id_descarga == descarga.id_descarga;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id_descarga);
    }
}
