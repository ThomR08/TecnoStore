package model;

import java.math.BigDecimal;

public class DetalleDeVenta {
    private long id;
    private Venta venta;
    private Celular celular;
    private int cantidad;
    private BigDecimal precio;
    private BigDecimal subtotal;

    public DetalleDeVenta() {
    }

    public DetalleDeVenta(long id, Venta venta, Celular celular, int cantidad, BigDecimal precio, BigDecimal subtotal) {
        this.id = id;
        this.venta = venta;
        this.celular = celular;
        this.cantidad = cantidad;
        this.precio = precio;
        this.subtotal = subtotal;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Venta getVenta() {
        return venta;
    }

    public void setVenta(Venta venta) {
        this.venta = venta;
    }

    public Celular getCelular() {
        return celular;
    }

    public void setCelular(Celular celular) {
        this.celular = celular;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    @Override
    public String toString() {
        return "DetalleDeVenta{" +
                "id=" + id +
                ", venta=" + venta +
                ", celular=" + celular +
                ", cantidad=" + cantidad +
                ", precio=" + precio +
                ", subtotal=" + subtotal +
                '}';
    }
}