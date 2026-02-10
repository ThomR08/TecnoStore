package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.Cliente;
import model.TipoDocumento;

public class ClienteDAO {

    private final Connection con;

    public ClienteDAO(Connection con) {
        this.con = con;
    }
    
    public void create(Cliente cliente) throws SQLException {
        String sql = "INSERT INTO Cliente(nombre, documento, tipo_documento, correo, telefono) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, cliente.getNombre());
            ps.setString(2, cliente.getDocumento());
            ps.setString(3, cliente.getTipoDocumento().name());
            ps.setString(4, cliente.getCorreo());
            ps.setString(5, cliente.getTelefono());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    cliente.setId(rs.getLong(1));
                }
            }
        }
    }
    
    public Cliente read(long id) throws SQLException {
        String sql = "SELECT * FROM Cliente WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    TipoDocumento tipo = TipoDocumento.valueOf(rs.getString("tipo_documento"));

                    return new Cliente(
                            rs.getLong("id"),
                            rs.getString("nombre"),
                            rs.getString("documento"),
                            tipo,
                            rs.getString("correo"),
                            rs.getString("telefono")
                    );
                }
            }
        }
        return null;
    }
    
    public boolean update(Cliente cliente) throws SQLException {
        String sql = "UPDATE Cliente SET nombre=?, documento=?, tipo_documento=?, correo=?, telefono=? WHERE id=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, cliente.getNombre());
            ps.setString(2, cliente.getDocumento());
            ps.setString(3, cliente.getTipoDocumento().name());
            ps.setString(4, cliente.getCorreo());
            ps.setString(5, cliente.getTelefono());
            ps.setLong(6, cliente.getId());

            int filas = ps.executeUpdate();
            return filas > 0;
        }
    }
    
    public boolean delete(long id) throws SQLException {
        String sql = "DELETE FROM Cliente WHERE id=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, id);
            int filas = ps.executeUpdate();
            return filas > 0;
        }
    }
    
    public List<Cliente> list() throws SQLException {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM Cliente";
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                TipoDocumento tipo = TipoDocumento.valueOf(rs.getString("tipo_documento"));

                clientes.add(new Cliente(
                        rs.getLong("id"),
                        rs.getString("nombre"),
                        rs.getString("documento"),
                        tipo,
                        rs.getString("correo"),
                        rs.getString("telefono")
                ));
            }
        }
        return clientes;
    }
}