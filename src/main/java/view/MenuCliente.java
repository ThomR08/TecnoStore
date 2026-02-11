package view;

import model.Cliente;
import model.TipoDocumento;
import service.GestorCliente;
import java.util.List;

public class MenuCliente extends MenuBase {

    private final GestorCliente gestor = new GestorCliente();

    @Override
    public void iniciar() {
        int opcion;
        do {
            System.out.println("""
                    
                    ==== GESTIÓN DE CLIENTES ====
                    
                    1. Registrar cliente
                    2. Listar clientes
                    3. Actualizar cliente
                    4. Eliminar cliente
                    
                    0. Volver
                    """);
            opcion = input.leerIntRango("Opción: ", 0, 4);
            try {
                switch (opcion) {
                    case 1 ->
                        registrar();
                    case 2 ->
                        listar();
                    case 3 ->
                        actualizar();
                    case 4 ->
                        eliminar();
                }
            } catch (Exception e) {
                System.out.println("\nNo se pudo realizar la accion " + e.getMessage());
            }
        } while (opcion != 0);
    }

    private void registrar() throws Exception {
        String nombre = input.leerString("Nombre: ");
        String documento = input.leerString("Documento: ");
        
        System.out.println("\nTipo de Documento:");
        System.out.println("1. "+TipoDocumento.CC.toString());
        System.out.println("2. "+TipoDocumento.CE.toString());
        System.out.println("3. "+TipoDocumento.TI.toString());
        System.out.println("4. "+TipoDocumento.PASAPORTE.toString());
        int opcionTipo = input.leerIntRango("Opción: ", 1, 4);
        TipoDocumento tipoDoc = switch (opcionTipo) {
            case 1 -> TipoDocumento.CC;
            case 2 -> TipoDocumento.CE;
            case 3 -> TipoDocumento.TI;
            case 4 -> TipoDocumento.PASAPORTE;
            default -> TipoDocumento.CC;
        };
        
        String correo = input.leerString("Correo: ");
        String telefono = input.leerString("Teléfono (opcional): ");
        
        Cliente cliente = new Cliente();
        cliente.setNombre(nombre);
        cliente.setDocumento(documento);
        cliente.setTipoDocumento(tipoDoc);
        cliente.setCorreo(correo);
        cliente.setTelefono(telefono.isBlank() ? null : telefono);
        
        gestor.registrar(cliente);
        System.out.println("\nCliente registrado con id: "+cliente.getId());
    }

    private boolean listar() throws Exception {
        List<Cliente> clientes = gestor.listar();
        if (clientes.isEmpty()) {
            System.out.println("\nNo hay clientes registrados.");
            return false;
        }
        System.out.println("\n--- CLIENTES ---");
        clientes.forEach(c -> System.out.println(c.getId() + " - " + 
            c.getNombre() + 
            " | Doc: " + c.getDocumento() + " (" + c.getTipoDocumento() + ")" +
            " | Correo: " + c.getCorreo() + 
            " | Tel: " + (c.getTelefono() != null ? c.getTelefono() : "N/A")));
        return true;
    }

    private void actualizar() throws Exception {
        if (!listar()) {
            return;
        }
        long id = input.leerLong("\nID a actualizar: ");
        String nombre = input.leerString("Nuevo nombre: ");
        String documento = input.leerString("Nuevo documento: ");
        
        System.out.println("\nTipo de Documento:");
        System.out.println("1. "+TipoDocumento.CC.toString());
        System.out.println("2. "+TipoDocumento.CE.toString());
        System.out.println("3. "+TipoDocumento.TI.toString());
        System.out.println("4. "+TipoDocumento.PASAPORTE.toString());
        int opcionTipo = input.leerIntRango("Opción: ", 1, 4);
        TipoDocumento tipoDoc = switch (opcionTipo) {
            case 1 -> TipoDocumento.CC;
            case 2 -> TipoDocumento.CE;
            case 3 -> TipoDocumento.TI;
            case 4 -> TipoDocumento.PASAPORTE;
            default -> TipoDocumento.CC;
        };
        
        String correo = input.leerString("Nuevo correo: ");
        String telefono = input.leerString("Nuevo teléfono (opcional): ");
        
        Cliente cliente = new Cliente(id, nombre, documento, tipoDoc, correo, telefono.isBlank() ? null : telefono);
        
        if (gestor.actualizar(cliente)) {
            System.out.println("\nCliente actualizado");
        } else {
            System.out.println("\nCliente no encontrado");
        }
    }

    private void eliminar() throws Exception {
        if (!listar()) {
            return;
        }
        long id = input.leerLong("\nID a eliminar: ");
        if (gestor.eliminar(id)) {
            System.out.println("\nCliente eliminado");
        } else {
            System.out.println("❌\nCliente no encontrado");
        }
    }
}