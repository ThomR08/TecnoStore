package model;

import java.math.BigDecimal;

public class Celular {
    private long id;
    private Marca marca;
    private String modelo;
    private BigDecimal precio;
    private int stock;
    private SistemaOperativo sistemaOperativo;
    private Gama gama;

    public Celular() {
    }

    public Celular(long id, Marca marca, String modelo, BigDecimal precio, int stock, SistemaOperativo sistemaOperativo, Gama gama) {
        this.id = id;
        this.marca = marca;
        this.modelo = modelo;
        this.precio = precio;
        this.stock = stock;
        this.sistemaOperativo = sistemaOperativo;
        this.gama = gama;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Marca getMarca() {
        return marca;
    }

    public void setMarca(Marca marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public SistemaOperativo getSistemaOperativo() {
        return sistemaOperativo;
    }

    public void setSistemaOperativo(SistemaOperativo sistemaOperativo) {
        this.sistemaOperativo = sistemaOperativo;
    }

    public Gama getGama() {
        return gama;
    }

    public void setGama(Gama gama) {
        this.gama = gama;
    }

    @Override
    public String toString() {
        return "Celular{" +
                "id=" + id +
                ", marca=" + marca +
                ", modelo='" + modelo + '\'' +
                ", precio=" + precio +
                ", stock=" + stock +
                ", sistemaOperativo=" + sistemaOperativo +
                ", gama=" + gama +
                '}';
    }
}
