package service;

import dao.DBConnection;
import dao.CelularDAO;
import model.Celular;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class GestorCelular {

    private final CelularDAO dao;

    public GestorCelular() {
        try {
            Connection con = DBConnection.getConnection();
            dao = new CelularDAO(con);
        } catch (SQLException e) {
            throw new RuntimeException("No se pudo inicializar GestorCelular", e);
        }
    }

    public void registrar(Celular celular) throws Exception {
        if (celular.getMarca() == null || celular.getMarca().getId() <= 0) {
            throw new Exception("Marca obligatoria");
        }
        if (celular.getModelo() == null || celular.getModelo().isBlank()) {
            throw new Exception("Modelo obligatorio");
        }
        if (celular.getPrecio() == null || celular.getPrecio().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new Exception("Precio inválido");
        }
        if (celular.getStock() < 0) {
            throw new Exception("Stock inválido");
        }
        if (celular.getSistemaOperativo() == null) {
            throw new Exception("Sistema operativo obligatorio");
        }
        if (celular.getGama() == null) {
            throw new Exception("Gama obligatoria");
        }
        dao.create(celular);
    }

    public List<Celular> listar() throws SQLException {
        return dao.list();
    }

    public boolean actualizar(Celular celular) throws Exception {
        if (celular.getId() <= 0) {
            throw new Exception("ID inválido");
        }
        if (celular.getMarca() == null || celular.getMarca().getId() <= 0) {
            throw new Exception("Marca obligatoria");
        }
        if (celular.getModelo() == null || celular.getModelo().isBlank()) {
            throw new Exception("Modelo obligatorio");
        }
        if (celular.getPrecio() == null || celular.getPrecio().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new Exception("Precio inválido");
        }
        if (celular.getStock() < 0) {
            throw new Exception("Stock inválido");
        }
        if (celular.getSistemaOperativo() == null) {
            throw new Exception("Sistema operativo obligatorio");
        }
        if (celular.getGama() == null) {
            throw new Exception("Gama obligatoria");
        }
        return dao.update(celular);
    }

    public boolean eliminar(long id) throws Exception {
        if (id <= 0) {
            throw new Exception("ID inválido");
        }
        return dao.delete(id);
    }
}