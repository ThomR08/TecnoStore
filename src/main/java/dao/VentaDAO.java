package dao;

import dto.VentasMensualesDTO;
import java.math.BigDecimal;
import java.sql.*;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import model.Cliente;
import model.TipoDocumento;
import model.Venta;

public class VentaDAO {

    private final Connection con;

    public VentaDAO(Connection con) {
        this.con = con;
    }
    
    public void create(Venta venta) throws SQLException {

        String sql = """
                     INSERT INTO Venta(cliente_id, subtotal, iva, total)
                     VALUES (?, ?, ?, ?)
                     """;

        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setLong(1, venta.getCliente().getId());
            ps.setBigDecimal(2, venta.getSubtotal());
            ps.setBigDecimal(3, venta.getIva());
            ps.setBigDecimal(4, venta.getTotal());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    venta.setId(rs.getLong(1));
                }
            }
        }
    }
    
    public Venta read(long id) throws SQLException {

        String sql = """
                     SELECT v.id AS venta_id, v.fecha, v.subtotal, v.iva, v.total, 
                         c.id AS cliente_id, c.nombre, c.documento, c.tipo_documento, c.correo, c.telefono
                     FROM Venta v
                     JOIN Cliente c ON v.cliente_id = c.id
                     WHERE v.id = ?
                     """;

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    Cliente cliente = new Cliente(
                            rs.getLong("cliente_id"),
                            rs.getString("nombre"),
                            rs.getString("documento"),
                            TipoDocumento.valueOf(rs.getString("tipo_documento")),
                            rs.getString("correo"),
                            rs.getString("telefono")
                    );

                    return new Venta(
                            rs.getLong("venta_id"),
                            cliente,
                            rs.getTimestamp("fecha").toLocalDateTime(),
                            rs.getBigDecimal("subtotal"),
                            rs.getBigDecimal("iva"),
                            rs.getBigDecimal("total")
                    );
                }
            }
        }

        return null;
    }
    
    public List<Venta> list() throws SQLException {

        List<Venta> ventas = new ArrayList<>();

        String sql = """
                     SELECT v.id AS venta_id, v.fecha, v.subtotal, v.iva, v.total, 
                            c.id AS cliente_id, c.nombre, c.documento, c.tipo_documento, c.correo, c.telefono 
                     FROM Venta v 
                     JOIN Cliente c ON v.cliente_id = c.id
                     """;

        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Cliente cliente = new Cliente(
                        rs.getLong("cliente_id"),
                        rs.getString("nombre"),
                        rs.getString("documento"),
                        TipoDocumento.valueOf(rs.getString("tipo_documento")),
                        rs.getString("correo"),
                        rs.getString("telefono")
                );

                ventas.add(new Venta(
                        rs.getLong("venta_id"),
                        cliente,
                        rs.getTimestamp("fecha").toLocalDateTime(),
                        rs.getBigDecimal("subtotal"),
                        rs.getBigDecimal("iva"),
                        rs.getBigDecimal("total")
                ));
            }
        }

        return ventas;
    }
    
    public List<VentasMensualesDTO> ventasPorMes() throws SQLException {

        List<VentasMensualesDTO> lista = new ArrayList<>();

        String sql = """
                     SELECT 
                        YEAR(fecha) AS anio, 
                        MONTH(fecha) AS mes, 
                        COUNT(id) AS cantidad_ventas, 
                        SUM(total) AS total_vendido 
                     FROM Venta 
                     GROUP BY YEAR(fecha), MONTH(fecha) 
                     ORDER BY anio DESC, mes DESC
                     """;

        try (PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                int anio = rs.getInt("anio");
                int mes = rs.getInt("mes");
                int cantidad = rs.getInt("cantidad_ventas");
                BigDecimal total = rs.getBigDecimal("total_vendido");

                YearMonth periodo = YearMonth.of(anio, mes);

                lista.add(new VentasMensualesDTO(periodo, cantidad, total));
            }
        }

        return lista;
    }
}