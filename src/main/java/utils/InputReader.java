package utils;

import java.math.BigDecimal;
import java.util.Scanner;

public class InputReader {

    private static InputReader instance;
    private Scanner scanner;

    private InputReader() {
        scanner = new Scanner(System.in);
    }

    public static InputReader getInstance() {
        if (instance == null) {
            instance = new InputReader();
        }
        return instance;
    }

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
                scanner.nextLine();
                return valor;
            } else {
                System.out.println("❌ Error: Debe ingresar un número entero.");
                scanner.nextLine();
            }
        }
    }

    // ========================
    // long
    // ========================
    public long leerLong(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            if (scanner.hasNextLong()) {
                long valor = scanner.nextLong();
                scanner.nextLine();
                return valor;
            } else {
                System.out.println("❌ Error: Debe ingresar un número válido.");
                scanner.nextLine();
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
    // BigDecimal
    // ========================
    public BigDecimal leerBigDecimal(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                String entrada = scanner.nextLine();
                return new BigDecimal(entrada);
            } catch (Exception e) {
                System.out.println("❌ Error: Debe ingresar un valor monetario válido.");
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

    public int leerIntRango(String mensaje, int min, int max) {
        while (true) {

            int valor = leerInt(mensaje);

            if (valor >= min && valor <= max) {
                return valor;
            } else {
                System.out.println("❌Error: Debe estar entre " + min + " y " + max);
            }
        }
    }

    // ========================
    // cerrar
    // ========================
    public void cerrar() {
        scanner.close();
    }
}
