package service;

import dao.DBConnection;
import dao.VentaDAO;
import dao.DetalleDeVentaDAO;
import dao.CelularDAO;
import dao.ClienteDAO;
import model.Venta;
import model.DetalleDeVenta;
import model.Cliente;
import model.Celular;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class GestorVenta {

    private final VentaDAO dao;

    public GestorVenta() {
        try {
            Connection con = DBConnection.getConnection();
            dao = new VentaDAO(con);
        } catch (SQLException e) {
            throw new RuntimeException("No se pudo inicializar GestorVenta", e);
        }
    }
    
    public void registrarVenta(long clienteId, List<DetalleDeVenta> detalles) throws Exception {
        if (detalles == null || detalles.isEmpty()) {
            throw new Exception("La venta debe tener al menos un detalle");
        }

        Connection con = null;
        boolean autoCommitOriginal = true;

        try {
            // Obtener conexión
            con = DBConnection.getConnection();
            autoCommitOriginal = con.getAutoCommit();
            con.setAutoCommit(false);

            // Crear DAOs con la misma conexión
            ClienteDAO clienteDAO = new ClienteDAO(con);
            CelularDAO celularDAO = new CelularDAO(con);
            VentaDAO ventaDAO = new VentaDAO(con);
            DetalleDeVentaDAO detalleDAO = new DetalleDeVentaDAO(con);

            // 1. Validar que el cliente existe
            Cliente cliente = clienteDAO.read(clienteId);
            if (cliente == null) {
                throw new Exception("Cliente no encontrado");
            }

            // 2. Validar cada detalle y calcular totales
            BigDecimal subtotalGeneral = BigDecimal.ZERO;

            for (DetalleDeVenta detalle : detalles) {
                // Validar celular
                long celularId = detalle.getCelular().getId();
                Celular celular = celularDAO.read(celularId);

                if (celular == null) {
                    throw new Exception("Celular con ID " + celularId + " no encontrado");
                }

                // Validar stock
                if (celular.getStock() < detalle.getCantidad()) {
                    throw new Exception("Stock insuficiente para " + celular.getMarca().getNombre() + " " + celular.getModelo() + ". Stock disponible: " + celular.getStock() + ", solicitado: " + detalle.getCantidad());
                }

                // Establecer precio actual del celular
                detalle.setPrecio(celular.getPrecio());

                // Calcular subtotal del detalle
                BigDecimal subtotalDetalle = celular.getPrecio().multiply(new BigDecimal(detalle.getCantidad()));
                detalle.setSubtotal(subtotalDetalle);

                // Acumular subtotal general
                subtotalGeneral = subtotalGeneral.add(subtotalDetalle);

                // Establecer el celular completo en el detalle
                detalle.setCelular(celular);
            }

            // 3. Calcular IVA (19%) y total
            BigDecimal iva = subtotalGeneral.multiply(new BigDecimal("0.19")).setScale(0, RoundingMode.HALF_UP);
            BigDecimal total = subtotalGeneral.add(iva);

            // 4. Crear la venta
            Venta venta = new Venta();
            venta.setCliente(cliente);
            venta.setSubtotal(subtotalGeneral);
            venta.setIva(iva);
            venta.setTotal(total);

            // 5. Insertar la venta y obtener el ID generado
            ventaDAO.create(venta);

            // 6. Insertar todos los detalles y actualizar stock
            for (DetalleDeVenta detalle : detalles) {
                // Establecer el ID de la venta en el detalle
                detalle.setVenta(venta);

                // Insertar el detalle
                detalleDAO.create(detalle);

                // Descontar stock
                detalle.getCelular().setStock(detalle.getCelular().getStock() - detalle.getCantidad());
                celularDAO.update(detalle.getCelular());
            }

            // 7. Commit de la transacción
            con.commit();

        } catch (Exception e) {
            // 8. Rollback en caso de error
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException ex) {
                    throw new Exception("Error al hacer rollback: " + ex.getMessage(), ex);
                }
            }
            throw e;

        } finally {
            // 9. Restaurar autoCommit
            if (con != null) {
                try {
                    con.setAutoCommit(autoCommitOriginal);
                } catch (SQLException e) {
                    System.err.println("Error al restaurar autoCommit: " + e.getMessage());
                }
            }
        }
    }

    public void registrar(Venta venta) throws Exception {
        if (venta.getCliente() == null || venta.getCliente().getId() <= 0) {
            throw new Exception("Cliente obligatorio");
        }
        if (venta.getSubtotal() == null || venta.getSubtotal().compareTo(java.math.BigDecimal.ZERO) < 0) {
            throw new Exception("Subtotal inválido");
        }
        if (venta.getIva() == null || venta.getIva().compareTo(java.math.BigDecimal.ZERO) < 0) {
            throw new Exception("IVA inválido");
        }
        if (venta.getTotal() == null || venta.getTotal().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new Exception("Total inválido");
        }
        dao.create(venta);
    }

    public List<Venta> listar() throws SQLException {
        return dao.list();
    }

    public boolean actualizar(Venta venta) throws Exception {
        if (venta.getId() <= 0) {
            throw new Exception("ID inválido");
        }
        if (venta.getCliente() == null || venta.getCliente().getId() <= 0) {
            throw new Exception("Cliente obligatorio");
        }
        if (venta.getSubtotal() == null || venta.getSubtotal().compareTo(java.math.BigDecimal.ZERO) < 0) {
            throw new Exception("Subtotal inválido");
        }
        if (venta.getIva() == null || venta.getIva().compareTo(java.math.BigDecimal.ZERO) < 0) {
            throw new Exception("IVA inválido");
        }
        if (venta.getTotal() == null || venta.getTotal().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new Exception("Total inválido");
        }
        return dao.update(venta);
    }

    public boolean eliminar(long id) throws Exception {
        if (id <= 0) {
            throw new Exception("ID inválido");
        }
        return dao.delete(id);
    }

    public List<Venta> listarConDetalles() throws SQLException {

        Connection con = DBConnection.getConnection();
        
        DetalleDeVentaDAO detalleDAO = new DetalleDeVentaDAO(con);

        List<Venta> ventas = dao.list();

        for (Venta venta : ventas) {
            List<DetalleDeVenta> detalles = detalleDAO.listByVenta(venta.getId());
            venta.setDetalles(detalles);
        }

        return ventas;
    }
}
