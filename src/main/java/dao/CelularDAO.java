package dao;

import dto.CelularVendidoDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.Celular;
import model.Gama;
import model.Marca;
import model.SistemaOperativo;

public class CelularDAO {

    private final Connection con;

    public CelularDAO(Connection con) {
        this.con = con;
    }

    public void create(Celular celular) throws SQLException {
        String sql = "INSERT INTO Celular(marca, modelo, precio, stock, sistema_operativo, gama) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, celular.getMarca().getId());
            ps.setString(2, celular.getModelo());
            ps.setBigDecimal(3, celular.getPrecio());
            ps.setInt(4, celular.getStock());
            ps.setString(5, celular.getSistemaOperativo().name());
            ps.setString(6, celular.getGama().name());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    celular.setId(rs.getLong(1));
                }
            }
        }
    }

    public Celular read(long id) throws SQLException {
        String sql = """
                     SELECT c.id AS celular_id, c.modelo, c.precio, c.stock, c.sistema_operativo, c.gama, 
                            m.id AS marca_id, m.nombre AS marca_nombre 
                     FROM Celular c 
                     JOIN Marca m ON c.marca = m.id 
                     WHERE c.id = ?
                     """;

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Marca marca = new Marca(rs.getLong("marca_id"), rs.getString("marca_nombre"));
                    SistemaOperativo so = SistemaOperativo.valueOf(rs.getString("sistema_operativo"));
                    Gama gama = Gama.valueOf(rs.getString("gama"));

                    return new Celular(
                            rs.getLong("celular_id"),
                            marca,
                            rs.getString("modelo"),
                            rs.getBigDecimal("precio"),
                            rs.getInt("stock"),
                            so,
                            gama
                    );
                }
            }
        }
        return null;
    }

    public boolean update(Celular celular) throws SQLException {
        String sql = "UPDATE Celular SET marca=?, modelo=?, precio=?, stock=?, sistema_operativo=?, gama=? WHERE id=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, celular.getMarca().getId());
            ps.setString(2, celular.getModelo());
            ps.setBigDecimal(3, celular.getPrecio());
            ps.setInt(4, celular.getStock());
            ps.setString(5, celular.getSistemaOperativo().name());
            ps.setString(6, celular.getGama().name());
            ps.setLong(7, celular.getId());

            int filas = ps.executeUpdate();
            return filas > 0;
        }
    }

    public boolean delete(long id) throws SQLException {
        String sql = "DELETE FROM Celular WHERE id=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, id);

            int filas = ps.executeUpdate();
            return filas > 0;
        }
    }

    public List<Celular> list() throws SQLException {
        List<Celular> celulares = new ArrayList<>();
        String sql = """
                     SELECT c.id AS celular_id, c.modelo, c.precio, c.stock, c.sistema_operativo, c.gama, 
                            m.id AS marca_id, m.nombre AS marca_nombre 
                     FROM Celular c 
                     JOIN Marca m ON c.marca = m.id
                     """;

        try (PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Marca marca = new Marca(rs.getLong("marca_id"), rs.getString("marca_nombre"));
                SistemaOperativo so = SistemaOperativo.valueOf(rs.getString("sistema_operativo"));
                Gama gama = Gama.valueOf(rs.getString("gama"));

                celulares.add(new Celular(
                        rs.getLong("celular_id"),
                        marca,
                        rs.getString("modelo"),
                        rs.getBigDecimal("precio"),
                        rs.getInt("stock"),
                        so,
                        gama
                ));
            }
        }
        return celulares;
    }

    public List<Celular> celularesStockBajo() throws SQLException {

        List<Celular> celulares = new ArrayList<>();

        String sql = """
                     SELECT 
                        c.id AS celular_id,
                        c.modelo,
                        c.precio,
                        c.stock,
                        c.sistema_operativo,
                        c.gama,
                        m.id AS marca_id,
                        m.nombre AS marca_nombre
                     FROM Celular c
                     JOIN Marca m ON c.marca = m.id
                     WHERE c.stock < 5
                     ORDER BY c.stock ASC
                     """;

        try (PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Marca marca = new Marca(rs.getLong("marca_id"), rs.getString("marca_nombre"));
                SistemaOperativo so = SistemaOperativo.valueOf(rs.getString("sistema_operativo"));
                Gama gama = Gama.valueOf(rs.getString("gama"));

                Celular celular = new Celular(
                        rs.getLong("celular_id"),
                        marca,
                        rs.getString("modelo"),
                        rs.getBigDecimal("precio"),
                        rs.getInt("stock"),
                        so,
                        gama
                );

                celulares.add(celular);
            }
        }
        return celulares;
    }

    public List<CelularVendidoDTO> top3MasVendidos() throws SQLException {

        List<CelularVendidoDTO> lista = new ArrayList<>();

        String sql = """
        SELECT 
            c.id,
            c.modelo,
            c.precio,
            c.stock,
            c.sistema_operativo,
            c.gama,
            m.id AS marca_id,
            m.nombre AS marca_nombre,
            t.total_vendido
        FROM (
            SELECT celular_id, SUM(cantidad) AS total_vendido
            FROM DetalleDeVenta
            GROUP BY celular_id
            ORDER BY total_vendido DESC
            LIMIT 3
        ) t
        JOIN Celular c ON t.celular_id = c.id
        JOIN Marca m ON c.marca = m.id
        ORDER BY t.total_vendido DESC
    """;

        try (PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Marca marca = new Marca(rs.getLong("marca_id"), rs.getString("marca_nombre"));
                SistemaOperativo so = SistemaOperativo.valueOf(rs.getString("sistema_operativo"));
                Gama gama = Gama.valueOf(rs.getString("gama"));

                Celular celular = new Celular(
                        rs.getLong("id"),
                        marca,
                        rs.getString("modelo"),
                        rs.getBigDecimal("precio"),
                        rs.getInt("stock"),
                        so,
                        gama
                );

                int totalVendido = rs.getInt("total_vendido");

                lista.add(new CelularVendidoDTO(celular, totalVendido));
            }
        }

        return lista;
    }
}
