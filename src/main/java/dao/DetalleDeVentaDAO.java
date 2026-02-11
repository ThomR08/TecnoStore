package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.*;

public class DetalleDeVentaDAO {

    private final Connection con;

    public DetalleDeVentaDAO(Connection con) {
        this.con = con;
    }
    
    public void create(DetalleDeVenta detalle) throws SQLException {

        String sql = """
                     INSERT INTO DetalleDeVenta(venta_id, celular_id, cantidad, precio, subtotal) 
                     VALUES (?, ?, ?, ?, ?)
                     """;

        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setLong(1, detalle.getVenta().getId());
            ps.setLong(2, detalle.getCelular().getId());
            ps.setInt(3, detalle.getCantidad());
            ps.setBigDecimal(4, detalle.getPrecio());
            ps.setBigDecimal(5, detalle.getSubtotal());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    detalle.setId(rs.getLong(1));
                }
            }
        }
    }
    
    public DetalleDeVenta read(long id) throws SQLException {

        String sql = """
                     SELECT d.id AS detalle_id, d.cantidad, d.precio, d.subtotal, 
                            v.id AS venta_id, 
                            c.id AS celular_id, c.modelo, c.precio AS precio_celular, 
                            c.stock, c.sistema_operativo, c.gama, 
                            m.id AS marca_id, m.nombre AS marca_nombre 
                     FROM DetalleDeVenta d 
                     JOIN Venta v ON d.venta_id = v.id 
                     JOIN Celular c ON d.celular_id = c.id 
                     JOIN Marca m ON c.marca = m.id 
                     WHERE d.id = ?
                     """;

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    Marca marca = new Marca(
                            rs.getLong("marca_id"),
                            rs.getString("marca_nombre")
                    );

                    Celular celular = new Celular(
                            rs.getLong("celular_id"),
                            marca,
                            rs.getString("modelo"),
                            rs.getBigDecimal("precio_celular"),
                            rs.getInt("stock"),
                            SistemaOperativo.valueOf(rs.getString("sistema_operativo")),
                            Gama.valueOf(rs.getString("gama"))
                    );

                    Venta venta = new Venta();
                    venta.setId(rs.getLong("venta_id"));

                    return new DetalleDeVenta(
                            rs.getLong("detalle_id"),
                            venta,
                            celular,
                            rs.getInt("cantidad"),
                            rs.getBigDecimal("precio"),
                            rs.getBigDecimal("subtotal")
                    );
                }
            }
        }

        return null;
    }
    
    public boolean update(DetalleDeVenta detalle) throws SQLException {

        String sql = """
                     UPDATE DetalleDeVenta 
                     SET venta_id=?, celular_id=?, cantidad=?, precio=?, subtotal=? 
                     WHERE id=?
                     """;

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, detalle.getVenta().getId());
            ps.setLong(2, detalle.getCelular().getId());
            ps.setInt(3, detalle.getCantidad());
            ps.setBigDecimal(4, detalle.getPrecio());
            ps.setBigDecimal(5, detalle.getSubtotal());
            ps.setLong(6, detalle.getId());

            return ps.executeUpdate() > 0;
        }
    }
    
    public boolean delete(long id) throws SQLException {

        String sql = "DELETE FROM DetalleDeVenta WHERE id=?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        }
    }
    
    public List<DetalleDeVenta> list() throws SQLException {

        List<DetalleDeVenta> detalles = new ArrayList<>();

        String sql = """
                     SELECT d.id AS detalle_id, d.cantidad, d.precio, d.subtotal,
                            v.id AS venta_id, 
                            c.id AS celular_id, c.modelo, 
                            c.stock, c.sistema_operativo, c.gama, 
                            m.id AS marca_id, m.nombre AS marca_nombre 
                     FROM DetalleDeVenta d 
                     JOIN Venta v ON d.venta_id = v.id 
                     JOIN Celular c ON d.celular_id = c.id 
                     JOIN Marca m ON c.marca = m.id
                     """;

        try (PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Marca marca = new Marca(
                        rs.getLong("marca_id"),
                        rs.getString("marca_nombre")
                );

                Celular celular = new Celular(
                        rs.getLong("celular_id"),
                        marca,
                        rs.getString("modelo"),
                        null,
                        rs.getInt("stock"),
                        SistemaOperativo.valueOf(rs.getString("sistema_operativo")),
                        Gama.valueOf(rs.getString("gama"))
                );

                Venta venta = new Venta();
                venta.setId(rs.getLong("venta_id"));

                detalles.add(new DetalleDeVenta(
                        rs.getLong("detalle_id"),
                        venta,
                        celular,
                        rs.getInt("cantidad"),
                        rs.getBigDecimal("precio"),
                        rs.getBigDecimal("subtotal")
                ));
            }
        }

        return detalles;
    }

    public List<DetalleDeVenta> listByVenta(long ventaId) throws SQLException {
        List<DetalleDeVenta> lista = new ArrayList<>();

        String sql = """
                     SELECT d.*, c.modelo, c.precio, m.nombre AS marca_nombre 
                     FROM DetalleDeVenta d 
                     JOIN Celular c ON d.celular_id = c.id 
                     JOIN Marca m ON c.marca = m.id 
                     WHERE d.venta_id = ?
                     """;

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, ventaId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {

                    Marca marca = new Marca();
                    marca.setNombre(rs.getString("marca_nombre"));

                    Celular celular = new Celular();
                    celular.setId(rs.getLong("celular_id"));
                    celular.setModelo(rs.getString("modelo"));
                    celular.setPrecio(rs.getBigDecimal("precio"));
                    celular.setMarca(marca);

                    DetalleDeVenta detalle = new DetalleDeVenta();
                    detalle.setId(rs.getLong("id"));
                    detalle.setCantidad(rs.getInt("cantidad"));
                    detalle.setPrecio(rs.getBigDecimal("precio"));
                    detalle.setSubtotal(rs.getBigDecimal("subtotal"));
                    detalle.setCelular(celular);

                    lista.add(detalle);
                }
            }
        }

        return lista;
    }
}
