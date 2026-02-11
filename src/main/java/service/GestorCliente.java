package service;

import dao.DBConnection;
import dao.ClienteDAO;
import model.Cliente;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class GestorCliente {

    private final ClienteDAO dao;

    public GestorCliente() {
        try {
            Connection con = DBConnection.getConnection();
            dao = new ClienteDAO(con);
        } catch (SQLException e) {
            throw new RuntimeException("No se pudo inicializar GestorCliente", e);
        }
    }

    public void registrar(Cliente cliente) throws Exception {
        if (cliente.getNombre() == null || cliente.getNombre().isBlank()) {
            throw new Exception("Nombre obligatorio");
        }
        if (cliente.getDocumento() == null || cliente.getDocumento().isBlank()) {
            throw new Exception("Documento obligatorio");
        }
        if (cliente.getTipoDocumento() == null) {
            throw new Exception("Tipo de documento obligatorio");
        }
        if (cliente.getCorreo() == null || cliente.getCorreo().isBlank()) {
            throw new Exception("Correo obligatorio");
        }
        dao.create(cliente);
    }

    public List<Cliente> listar() throws SQLException {
        return dao.list();
    }

    public boolean actualizar(Cliente cliente) throws Exception {
        if (cliente.getId() <= 0) {
            throw new Exception("ID inválido");
        }
        if (cliente.getNombre() == null || cliente.getNombre().isBlank()) {
            throw new Exception("Nombre obligatorio");
        }
        if (cliente.getDocumento() == null || cliente.getDocumento().isBlank()) {
            throw new Exception("Documento obligatorio");
        }
        if (cliente.getTipoDocumento() == null) {
            throw new Exception("Tipo de documento obligatorio");
        }
        if (cliente.getCorreo() == null || cliente.getCorreo().isBlank()) {
            throw new Exception("Correo obligatorio");
        }
        return dao.update(cliente);
    }

    public boolean eliminar(long id) throws Exception {
        if (id <= 0) {
            throw new Exception("ID inválido");
        }
        return dao.delete(id);
    }
}