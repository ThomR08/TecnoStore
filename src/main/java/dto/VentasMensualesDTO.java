package dto;

import java.math.BigDecimal;
import java.time.YearMonth;

public class VentasMensualesDTO {

    private YearMonth periodo;
    private int cantidadVentas;
    private BigDecimal totalVendido;

    public VentasMensualesDTO(YearMonth periodo, int cantidadVentas, BigDecimal totalVendido) {
        this.periodo = periodo;
        this.cantidadVentas = cantidadVentas;
        this.totalVendido = totalVendido;
    }

    public YearMonth getPeriodo() {
        return periodo;
    }

    public int getCantidadVentas() {
        return cantidadVentas;
    }

    public BigDecimal getTotalVendido() {
        return totalVendido;
    }

    public void setPeriodo(YearMonth periodo) {
        this.periodo = periodo;
    }

    public void setCantidadVentas(int cantidadVentas) {
        this.cantidadVentas = cantidadVentas;
    }

    public void setTotalVendido(BigDecimal totalVendido) {
        this.totalVendido = totalVendido;
    }
}
