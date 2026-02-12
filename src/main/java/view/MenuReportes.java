package view;

import dto.CelularVendidoDTO;
import dto.VentasMensualesDTO;
import model.Celular;
import controller.GestorCelular;
import controller.GestorVenta;

import java.util.List;

public class MenuReportes extends MenuBase {

    private final GestorCelular gestorCelular = new GestorCelular();
    private final GestorVenta gestorVenta = new GestorVenta();

    @Override
    public void iniciar() {

        int opcion;

        do {

            System.out.println("""
                    
                    ==== REPORTES ====
                    
                    1. Celulares con stock bajo (<5)
                    2. Top 3 celulares más vendidos
                    3. Ventas totales por mes
                    4. Generar reporte txt de ventas
                    5. Generar reporte csv de ventas
                    
                    0. Volver
                    """);

            opcion = input.leerIntRango("Opción: ", 0, 4);

            try {

                switch (opcion) {

                    case 1 -> stockBajo();
                    case 2 -> top3();
                    case 3 -> ventasPorMes();
                    case 4 -> generarArchivoTXT();
                    case 5 -> generarArchivoCSV();
                }

            } catch (Exception e) {
                System.out.println("\nNo se pudo generar el reporte: " + e.getMessage());
            }

        } while (opcion != 0);
    }
    
    private void stockBajo() throws Exception {

        List<Celular> lista = gestorCelular.celularesStockBajo();

        if (lista.isEmpty()) {
            System.out.println("\nNo hay celulares con stock bajo.");
            return;
        }

        System.out.println("\n--- CELULARES CON STOCK BAJO ---");

        lista.forEach(c -> System.out.println(
                c.getId() + " - "
                + c.getMarca().getNombre() + " "
                + c.getModelo()
                + " | Stock: " + c.getStock()
        ));
    }
    
    private void top3() throws Exception {

        List<CelularVendidoDTO> lista = gestorCelular.top3MasVendidos();

        if (lista.isEmpty()) {
            System.out.println("\nNo hay ventas registradas.");
            return;
        }

        System.out.println("\n--- TOP 3 CELULARES MÁS VENDIDOS ---");

        int posicion = 1;

        for (CelularVendidoDTO dto : lista) {

            Celular c = dto.getCelular();

            System.out.println(
                    posicion++ + ". "
                    + c.getMarca().getNombre() + " "
                    + c.getModelo()
                    + " | Cantidad vendida: " + dto.getTotalVendido()
            );
        }
    }
    
    private void ventasPorMes() throws Exception {

        List<VentasMensualesDTO> lista = gestorVenta.ventasPorMes();

        if (lista.isEmpty()) {
            System.out.println("\nNo hay ventas registradas.");
            return;
        }

        System.out.println("\n--- VENTAS POR MES ---");

        lista.forEach(dto -> System.out.println(
                dto.getPeriodo()
                + " | Cantidad ventas: " + dto.getCantidadVentas()
                + " | Total vendido: $" + dto.getTotalVendido()
        ));
    }
    
    private void generarArchivoTXT() throws Exception {

        gestorVenta.generarReporteVentasTXT();

        System.out.println("\nArchivo 'reporte_ventas.txt' generado correctamente.");
    }
    
    private void generarArchivoCSV() throws Exception {

        gestorVenta.generarReporteVentasCSV();

        System.out.println("\nArchivo 'reporte_ventas.csv' generado correctamente.");
    }
}