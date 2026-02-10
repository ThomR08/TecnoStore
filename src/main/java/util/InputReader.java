package util;

import java.util.Scanner;

public class InputReader {

    private Scanner scanner = new Scanner(System.in);

    public InputReader() {}

    // ========================
    // String
    // ========================
    public String leerString(String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine();
    }

    // ========================
    // int
    // ========================
    public int leerInt(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            if (scanner.hasNextInt()) {
                int valor = scanner.nextInt();
                scanner.nextLine(); // limpiar buffer
                return valor;
            } else {
                System.out.println("❌ Error: Debe ingresar un número entero.");
                scanner.nextLine(); // descartar entrada inválida
            }
        }
    }

    // ========================
    // double
    // ========================
    public double leerDouble(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            if (scanner.hasNextDouble()) {
                double valor = scanner.nextDouble();
                scanner.nextLine();
                return valor;
            } else {
                System.out.println("❌ Error: Debe ingresar un número decimal.");
                scanner.nextLine();
            }
        }
    }

    // ========================
    // char
    // ========================
    public char leerChar(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String entrada = scanner.nextLine();
            if (entrada.length() == 1) {
                return entrada.charAt(0);
            } else {
                System.out.println("❌ Error: Debe ingresar un solo carácter.");
            }
        }
    }

    // ========================
    // Cerrar scanner
    // ========================
    public void cerrar() {
        scanner.close();
    }
}
