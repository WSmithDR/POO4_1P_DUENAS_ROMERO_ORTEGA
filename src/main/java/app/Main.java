package app;

import java.util.Scanner;

public class Main {
    /**
     * Método principal que inicia la ejecución del programa.
     * Llama al método Sistema.iniciar() para comenzar el sistema
     * de gestión de entregas.
     * 
     * @param args Argumentos de línea de comandos (no utilizados en esta aplicación)
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Sistema.iniciar(scanner);
        scanner.close();
    }
}
