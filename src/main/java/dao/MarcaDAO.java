package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.Marca;

public class MarcaDAO {
    
    private final Connection con;

    public MarcaDAO(Connection con) {
        this.con = con;
    }

    public void create(Marca marca) throws SQLException {
        String sql = "INSERT INTO Marca(nombre) VALUES (?)";
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, marca.getNombre());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    marca.setId(rs.getLong(1));
                }
            }
        }
    }

    public Marca read(long id) throws SQLException {
        String sql = "SELECT * FROM Marca WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Marca(rs.getLong("id"), rs.getString("nombre"));
                }
            }
        }
        return null;
    }

    public boolean update(Marca marca) throws SQLException {
        String sql = "UPDATE Marca SET nombre = ? WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, marca.getNombre());
            ps.setLong(2, marca.getId());

            int filas = ps.executeUpdate();
            return filas > 0;
        }
    }

    public boolean delete(long id) throws SQLException {
        String sql = "DELETE FROM Marca WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, id);

            int filas = ps.executeUpdate();
            return filas > 0;
        }
    }

    public List<Marca> list() throws SQLException {
        List<Marca> marcas = new ArrayList<>();
        String sql = "SELECT * FROM Marca";
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                marcas.add(new Marca(rs.getLong("id"), rs.getString("nombre")));
            }
        }
        return marcas;
    }
}