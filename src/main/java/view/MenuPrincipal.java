package view;

public class MenuPrincipal extends MenuBase {

    private final MenuMarca menuMarca;
    private final MenuCelular menuCelular;
    private final MenuCliente menuCliente;
    private final MenuVenta menuVenta;
    private final MenuReportes menuReportes;

    public MenuPrincipal() {

        menuMarca = new MenuMarca();
        menuCelular = new MenuCelular();
        menuCliente = new MenuCliente();
        menuVenta = new MenuVenta();
        menuReportes = new MenuReportes();
    }

    @Override
    public void iniciar() {

        int opcion;

        do {

            System.out.println("""
                           
                           ===== TECNOSTORE =====
                           
                           1. Marcas
                           2. Celulares
                           3. Clientes
                           4. Ventas
                           5. Reportes
                           
                           0. Salir
                               """);

            opcion = input.leerIntRango("Opcion: ", 0, 5);

            switch (opcion) {

                case 1 -> menuMarca.iniciar();
                case 2 -> menuCelular.iniciar();
                case 3 -> menuCliente.iniciar();
                case 4 -> menuVenta.iniciar();
                case 5 -> menuReportes.iniciar();
                case 0 -> System.out.println("Adios");
            }

        } while (opcion != 0);
    }
}