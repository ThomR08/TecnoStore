package controller;

import dao.DBConnection;
import dao.VentaDAO;
import dao.DetalleDeVentaDAO;
import dao.CelularDAO;
import dao.ClienteDAO;
import dto.VentasMensualesDTO;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import model.Venta;
import model.DetalleDeVenta;
import model.Cliente;
import model.Celular;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

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
    
    public List<VentasMensualesDTO> ventasPorMes() throws SQLException {
        return dao.ventasPorMes();
    }
    
    public void generarReporteVentasTXT() throws Exception {

        List<Venta> ventas = listarConDetalles();

        if (ventas.isEmpty()) {
            throw new Exception("No hay ventas registradas.");
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar reporte de ventas");

        fileChooser.setFileFilter(new FileNameExtensionFilter("Archivo de texto (*.txt)", "txt"));
        fileChooser.setSelectedFile(new File("reporte_ventas.txt"));

        int userSelection = fileChooser.showSaveDialog(null);

        if (userSelection != JFileChooser.APPROVE_OPTION) {
            throw new Exception("Operación cancelada por el usuario.");
        }

        File archivo = fileChooser.getSelectedFile();

        // Asegurar extensión .txt
        if (!archivo.getName().toLowerCase().endsWith(".txt")) {
            archivo = new File(archivo.getAbsolutePath() + ".txt");
        }

        DecimalFormat df = new DecimalFormat("#,##0.00");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo))) {

            writer.write("========== REPORTE DETALLADO DE VENTAS ==========\n\n");

            for (Venta venta : ventas) {

                writer.write("Venta ID: " + venta.getId() + "\n");
                writer.write("Fecha: " + venta.getFecha() + "\n");
                writer.write("Cliente: " + venta.getCliente().getNombre() + "\n");
                writer.write("---------------------------------------------\n");

                for (DetalleDeVenta detalle : venta.getDetalles()) {

                    writer.write(
                            detalle.getCelular().getMarca().getNombre() + " "
                            + detalle.getCelular().getModelo()
                            + " | Cantidad: " + detalle.getCantidad()
                            + " | Precio: $" + df.format(detalle.getPrecio())
                            + " | Subtotal: $" + df.format(detalle.getSubtotal())
                            + "\n"
                    );
                }

                writer.write("---------------------------------------------\n");
                writer.write("Subtotal: $" + df.format(venta.getSubtotal()) + "\n");
                writer.write("IVA: $" + df.format(venta.getIva()) + "\n");
                writer.write("TOTAL: $" + df.format(venta.getTotal()) + "\n");
                writer.write("=============================================\n\n");
            }
        }

        System.out.println("\nReporte generado en: " + archivo.getAbsolutePath());
    }

    public void generarReporteVentasCSV() throws Exception {

        List<Venta> ventas = listarConDetalles();

        if (ventas.isEmpty()) {
            throw new Exception("No hay ventas registradas.");
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar reporte de ventas (CSV)");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Archivo CSV (*.csv)", "csv"));
        fileChooser.setSelectedFile(new File("reporte_ventas.csv"));

        int seleccion = fileChooser.showSaveDialog(null);

        if (seleccion != JFileChooser.APPROVE_OPTION) {
            throw new Exception("Operación cancelada.");
        }

        File archivo = fileChooser.getSelectedFile();

        if (!archivo.getName().toLowerCase().endsWith(".csv")) {
            archivo = new File(archivo.getAbsolutePath() + ".csv");
        }

        DecimalFormat df = new DecimalFormat("#.00");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo))) {

            // Encabezado CSV
            writer.write("VentaID,Fecha,Cliente,Celular,Marca,Cantidad,PrecioUnitario,SubtotalDetalle,SubtotalVenta,IVA,TotalVenta");
            writer.newLine();

            for (Venta venta : ventas) {

                for (DetalleDeVenta detalle : venta.getDetalles()) {

                    writer.write(
                            venta.getId() + ","
                            + venta.getFecha() + ","
                            + venta.getCliente().getNombre() + ","
                            + detalle.getCelular().getModelo() + ","
                            + detalle.getCelular().getMarca().getNombre() + ","
                            + detalle.getCantidad() + ","
                            + df.format(detalle.getPrecio()) + ","
                            + df.format(detalle.getSubtotal()) + ","
                            + df.format(venta.getSubtotal()) + ","
                            + df.format(venta.getIva()) + ","
                            + df.format(venta.getTotal())
                    );

                    writer.newLine();
                }
            }
        }

        System.out.println("\nCSV generado en: " + archivo.getAbsolutePath());
    }

}