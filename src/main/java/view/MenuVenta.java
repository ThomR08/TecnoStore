package view;

import model.Venta;
import model.DetalleDeVenta;
import model.Celular;
import service.GestorVenta;
import java.util.ArrayList;
import java.util.List;

public class MenuVenta extends MenuBase {

    private final GestorVenta gestor = new GestorVenta();

    @Override
    public void iniciar() {
        int opcion;
        do {
            System.out.println("""
                    
                    ==== GESTIÓN DE VENTAS ====
                    
                    1. Registrar venta completa
                    2. Listar ventas
                    
                    0. Volver
                    """);
            opcion = input.leerIntRango("Opción: ", 0, 4);
            try {
                switch (opcion) {
                    case 1 ->
                        registrarVentaCompleta();
                    case 2 ->
                        listar();
                }
            } catch (Exception e) {
                System.out.println("\nNo se pudo realizar la accion " + e.getMessage());
            }
        } while (opcion != 0);
    }

    private void registrarVentaCompleta() throws Exception {
        System.out.println("\n=== REGISTRO DE VENTA ===");

        long clienteId = input.leerLong("ID del cliente: ");

        List<DetalleDeVenta> detalles = new ArrayList<>();
        boolean agregarMas = true;

        while (agregarMas) {
            System.out.println("\n--- Agregar producto ---");
            long celularId = input.leerLong("ID del celular: ");
            int cantidad = input.leerInt("Cantidad: ");

            Celular celular = new Celular();
            celular.setId(celularId);

            DetalleDeVenta detalle = new DetalleDeVenta();
            detalle.setCelular(celular);
            detalle.setCantidad(cantidad);

            detalles.add(detalle);

            char respuesta;
            
            do {
                respuesta = Character.toLowerCase(
                        input.leerChar("¿Agregar otro producto? (s/n): ")
                );
                
                if (respuesta != 's' && respuesta != 'n') {
                    System.out.println("    Error: Ingrese solo 's' o 'n'.");
                }

            } while (respuesta != 's' && respuesta != 'n');

            agregarMas = (respuesta == 's');
        }

        // Registrar la venta completa
        gestor.registrarVenta(clienteId, detalles);

        System.out.println("\n✅ Venta registrada exitosamente");
    }

private boolean listar() throws Exception {

    List<Venta> ventas = gestor.listarConDetalles();

    if (ventas.isEmpty()) {
        System.out.println("\nNo hay ventas registradas.");
        return false;
    }

    System.out.println("");
    for (Venta venta : ventas) {

        System.out.println("""
                ================================
                FACTURA N° %d
                Cliente: %s
                Fecha: %s
                --------------------------------
                """.formatted(
                        venta.getId(),
                        venta.getCliente().getNombre(),
                        venta.getFecha()
                ));

        for (DetalleDeVenta d : venta.getDetalles()) {
            System.out.println(
                    d.getCelular().getMarca().getNombre() + " "
                    + d.getCelular().getModelo()
                    + " x" + d.getCantidad()
                    + " - $" + d.getSubtotal()
            );
        }

        System.out.println("--------------------------------");
        System.out.println("Subtotal: $" + venta.getSubtotal());
        System.out.println("IVA: $" + venta.getIva());
        System.out.println("TOTAL: $" + venta.getTotal());
        System.out.println("================================\n");
    }
    return true;
}
