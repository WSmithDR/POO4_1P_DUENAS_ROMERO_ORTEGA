package utils;

public class Printers {
    public static void printSeparator() {
        System.out.println("========================================");
    }

    public static void printTitle(String title) {
        printSeparator();
        System.out.println(title);
        printSeparator();
    }

    public static void printSuccess(String message) {
        System.out.println("[ÉXITO] " + message);
    }

    public static void printError(String message) {
        System.out.println("[ERROR] " + message);
    }

    public static void printLine() {
        System.out.println("-------------------------------------------------------");
    }
} 