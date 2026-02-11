package view;

import model.Celular;
import model.Marca;
import model.SistemaOperativo;
import model.Gama;
import service.GestorCelular;
import java.math.BigDecimal;
import java.util.List;

public class MenuCelular extends MenuBase {

    private final GestorCelular gestor = new GestorCelular();

    @Override
    public void iniciar() {
        int opcion;
        do {
            System.out.println("""
                    
                    ==== GESTIÓN DE CELULARES ====
                    
                    1. Registrar celular
                    2. Listar celulares
                    3. Actualizar celular
                    4. Eliminar celular
                    
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
                System.out.println("\nNo se pudo realizar la accion: " + e.getMessage());
            }
        } while (opcion != 0);
    }

    private void registrar() throws Exception {
        long marcaId = input.leerLong("ID de marca: ");
        String modelo = input.leerString("Modelo: ");
        BigDecimal precio = input.leerBigDecimal("Precio: ");
        int stock = input.leerInt("Stock: ");

        System.out.println("\nSistema Operativo:");
        System.out.println("1. "+SistemaOperativo.ANDROID.toString());
        System.out.println("2. "+SistemaOperativo.IOS.toString());
        System.out.println("3. "+SistemaOperativo.HARMONY_OS.toString());
        int opcionSO = input.leerIntRango("Opción: ", 1, 3);
        SistemaOperativo so = switch (opcionSO) {
            case 1 ->
                SistemaOperativo.ANDROID;
            case 2 ->
                SistemaOperativo.IOS;
            case 3 ->
                SistemaOperativo.HARMONY_OS;
            default ->
                SistemaOperativo.ANDROID;
        };

        System.out.println("\nGama:");
        System.out.println("1. "+Gama.BAJA.toString());
        System.out.println("2. "+Gama.MEDIA.toString());
        System.out.println("3. "+Gama.ALTA.toString());
        int opcionGama = input.leerIntRango("Opción: ", 1, 3);
        Gama gama = switch (opcionGama) {
            case 1 ->
                Gama.BAJA;
            case 2 ->
                Gama.MEDIA;
            case 3 ->
                Gama.ALTA;
            default ->
                Gama.MEDIA;
        };

        Marca marca = new Marca();
        marca.setId(marcaId);

        Celular celular = new Celular();
        celular.setMarca(marca);
        celular.setModelo(modelo);
        celular.setPrecio(precio);
        celular.setStock(stock);
        celular.setSistemaOperativo(so);
        celular.setGama(gama);

        gestor.registrar(celular);
        System.out.println("\nCelular registrado con id: " + celular.getId());
    }

    private boolean listar() throws Exception {
        List<Celular> celulares = gestor.listar();
        if (celulares.isEmpty()) {
            System.out.println("\nNo hay celulares registrados.");
            return false;
        }
        System.out.println("\n--- CELULARES ---");
        celulares.forEach(c -> System.out.println(c.getId() + " - "
                + c.getMarca().getNombre() + " " + c.getModelo()
                + " | Precio: $" + c.getPrecio()
                + " | Stock: " + c.getStock()
                + " | SO: " + c.getSistemaOperativo()
                + " | Gama: " + c.getGama()));
        return true;
    }

    private void actualizar() throws Exception {
        if (!listar()) {
            return;
        }
        long id = input.leerLong("\nID a actualizar: ");
        long marcaId = input.leerLong("ID de marca: ");
        String modelo = input.leerString("Nuevo modelo: ");
        BigDecimal precio = input.leerBigDecimal("Nuevo precio: ");
        int stock = input.leerInt("Nuevo stock: ");

        System.out.println("\nSistema Operativo:");
        System.out.println("1. "+SistemaOperativo.ANDROID.toString());
        System.out.println("2. "+SistemaOperativo.IOS.toString());
        System.out.println("3. "+SistemaOperativo.HARMONY_OS.toString());
        int opcionSO = input.leerIntRango("Opción: ", 1, 3);
        SistemaOperativo so = switch (opcionSO) {
            case 1 ->
                SistemaOperativo.ANDROID;
            case 2 ->
                SistemaOperativo.IOS;
            case 3 ->
                SistemaOperativo.HARMONY_OS;
            default ->
                SistemaOperativo.ANDROID;
        };

        System.out.println("\nGama:");
        System.out.println("1. "+Gama.BAJA.toString());
        System.out.println("2. "+Gama.MEDIA.toString());
        System.out.println("3. "+Gama.ALTA.toString());
        int opcionGama = input.leerIntRango("Opción: ", 1, 3);
        Gama gama = switch (opcionGama) {
            case 1 ->
                Gama.BAJA;
            case 2 ->
                Gama.MEDIA;
            case 3 ->
                Gama.ALTA;
            default ->
                Gama.MEDIA;
        };

        Marca marca = new Marca();
        marca.setId(marcaId);

        Celular celular = new Celular(id, marca, modelo, precio, stock, so, gama);

        if (gestor.actualizar(celular)) {
            System.out.println("\nCelular actualizado");
        } else {
            System.out.println("\nCelular no encontrado");
        }
    }

    private void eliminar() throws Exception {
        if (!listar()) {
            return;
        }
        long id = input.leerLong("\nID a eliminar: ");
        if (gestor.eliminar(id)) {
            System.out.println("\nCelular eliminado");
        } else {
            System.out.println("❌\nCelular no encontrado");
        }
    }
}
