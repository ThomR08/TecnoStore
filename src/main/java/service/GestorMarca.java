package service;

import dao.DBConnection;
import dao.MarcaDAO;
import model.Marca;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class GestorMarca {

    private final MarcaDAO dao;

    public GestorMarca() {

        try {
            Connection con = DBConnection.getConnection();
            dao = new MarcaDAO(con);
        } catch (SQLException e) {
            throw new RuntimeException("No se pudo inicializar GestorMarca", e);
        }
    }

    public void registrar(Marca marca) throws Exception {

        if (marca.getNombre() == null || marca.getNombre().isBlank()) {
            throw new Exception("Nombre obligatorio");
        }

        dao.create(marca);
    }

    public List<Marca> listar() throws SQLException {
        return dao.list();
    }

    public boolean actualizar(Marca marca) throws Exception {

        if (marca.getId() <= 0) {
            throw new Exception("ID inválido");
        }

        if (marca.getNombre().isBlank()) {
            throw new Exception("Nombre obligatorio");
        }

        return dao.update(marca);
    }

    public boolean eliminar(long id) throws Exception {

        if (id <= 0) {
            throw new Exception("ID inválido");
        }

        return dao.delete(id);
    }
}
