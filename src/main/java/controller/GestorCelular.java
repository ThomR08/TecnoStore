package controller;

import dao.DBConnection;
import dao.CelularDAO;
import dto.CelularVendidoDTO;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import model.Celular;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.stream.Stream;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

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

    public List<Celular> celularesStockBajo() throws SQLException {
        return dao.celularesStockBajo();
    }

    public List<CelularVendidoDTO> top3MasVendidos() throws SQLException {
        return dao.top3MasVendidos();
    }

    public void generarReporteStockTXT(List<Celular> celulares) throws Exception {

        if (celulares.isEmpty()) {
            throw new Exception("No hay ventas registradas.");
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar reporte de stock");

        fileChooser.setFileFilter(new FileNameExtensionFilter("Archivo de texto (*.txt)", "txt"));
        fileChooser.setSelectedFile(new File("reporte_stock.txt"));

        int userSelection = fileChooser.showSaveDialog(null);

        if (userSelection != JFileChooser.APPROVE_OPTION) {
            throw new Exception("Operación cancelada por el usuario.");
        }

        File archivo = fileChooser.getSelectedFile();

        // Asegurar extensión .txt
        if (!archivo.getName().toLowerCase().endsWith(".txt")) {
            archivo = new File(archivo.getAbsolutePath() + ".txt");
        }
        
        // Conversion a Stream
        Stream<Celular> streamCelulares = celulares.stream();

        DecimalFormat df = new DecimalFormat("#,##0.00");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo))) {

            writer.write("========== ALERTA DE STOCK BAJO ==========\n\n");
            
            streamCelulares.forEach(celular -> 
                {
                try {
                    writer.write("ID: " + celular.getId()
                            + " | Modelo: " + celular.getModelo()
                            + " | Marca: " + celular.getMarca().getNombre()
                            + " | Stock: " + celular.getStock()
                            + " | Precio: " + df.format(celular.getPrecio()) + "\n");
                } catch (IOException ex) {
                    System.getLogger(GestorCelular.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
                }
            });
        }

        System.out.println("\nReporte generado en: " + archivo.getAbsolutePath());
    }
}
