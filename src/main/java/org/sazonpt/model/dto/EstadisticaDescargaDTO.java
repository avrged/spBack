package org.sazonpt.model.dto;

public class EstadisticaDescargaDTO {
    private String origen;
    private String opinion;
    private int totalDescargas;

    public EstadisticaDescargaDTO() {}

    public EstadisticaDescargaDTO(String origen, String opinion, int totalDescargas) {
        this.origen = origen;
        this.opinion = opinion;
        this.totalDescargas = totalDescargas;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }

    public int getTotalDescargas() {
        return totalDescargas;
    }

    public void setTotalDescargas(int totalDescargas) {
        this.totalDescargas = totalDescargas;
    }

    @Override
    public String toString() {
        return "EstadisticaDescargaDTO{" +
                "origen='" + origen + '\'' +
                ", opinion='" + opinion + '\'' +
                ", totalDescargas=" + totalDescargas +
                '}';
    }
}
