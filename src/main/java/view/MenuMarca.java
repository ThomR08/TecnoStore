package view;

import model.Marca;
import controller.GestorMarca;

import java.util.List;

public class MenuMarca extends MenuBase {

    private final GestorMarca gestor = new GestorMarca();

    @Override
    public void iniciar() {

        int opcion;

        do {

            System.out.println("""
                    
                    ==== GESTIÓN DE MARCAS ====
                    
                    1. Registrar marca
                    2. Listar marcas
                    3. Actualizar marca
                    4. Eliminar marca
                    
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

        String nombre = input.leerString("Nombre marca: ");

        Marca marca = new Marca();
        marca.setNombre(nombre);

        gestor.registrar(marca);

        System.out.println("\nMarca registrada con id: "+marca.getId());
    }

    private boolean listar() throws Exception {

        List<Marca> marcas = gestor.listar();
        if (marcas.isEmpty()) {
            System.out.println("\nNo hay marcas registradas.");
            return false;
        }
        System.out.println("\n--- MARCAS ---");
        marcas.forEach(m -> System.out.println(m.getId() + " - " + m.getNombre()));
        return true;
    }

    private void actualizar() throws Exception {

        if (!listar()) {
            System.out.println("\nNo hay celulares registrados.");
            return;
        }

        long id = input.leerLong("\nID a actualizar: ");
        String nombre = input.leerString("Nuevo nombre: ");

        Marca marca = new Marca(id, nombre);

        if (gestor.actualizar(marca)) {
            System.out.println("\nMarca actualizada");
        } else {
            System.out.println("\nMarca no encontrada");
        }
    }

    private void eliminar() throws Exception {

        if (!listar()) {
            System.out.println("\nNo hay celulares registrados.");
            return;
        }

        long id = input.leerLong("\nID a eliminar: ");

        if (gestor.eliminar(id)) {
            System.out.println("\nMarca eliminada");
        } else {
            System.out.println("\nMarca no encontrada");
        }
    }
}
