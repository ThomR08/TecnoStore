package model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Venta {
    private long id;
    private Cliente cliente;
    private LocalDateTime fecha;
    private BigDecimal subtotal;
    private BigDecimal iva;
    private BigDecimal total;

    public Venta() {
    }

    public Venta(long id, Cliente cliente, LocalDateTime fecha, BigDecimal subtotal, BigDecimal iva, BigDecimal total) {
        this.id = id;
        this.cliente = cliente;
        this.fecha = fecha;
        this.subtotal = subtotal;
        this.iva = iva;
        this.total = total;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getIva() {
        return iva;
    }

    public void setIva(BigDecimal iva) {
        this.iva = iva;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "Venta{" +
                "id=" + id +
                ", cliente=" + cliente +
                ", fecha=" + fecha +
                ", subtotal=" + subtotal +
                ", iva=" + iva +
                ", total=" + total +
                '}';
    }
}
