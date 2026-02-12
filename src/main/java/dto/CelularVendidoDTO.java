package dto;

import model.Celular;

public class CelularVendidoDTO {

    private Celular celular;
    private int totalVendido;

    public CelularVendidoDTO(Celular celular, int totalVendido) {
        this.celular = celular;
        this.totalVendido = totalVendido;
    }

    public Celular getCelular() {
        return celular;
    }

    public int getTotalVendido() {
        return totalVendido;
    }

    public void setCelular(Celular celular) {
        this.celular = celular;
    }

    public void setTotalVendido(int totalVendido) {
        this.totalVendido = totalVendido;
    }
}
