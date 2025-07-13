package utils;

public class Printers {
    public static void printSeparator() {
        System.out.println("========================================");
    }

    public static void printTitle(String title) {
        System.out.println();
        printSeparator();
        System.out.println(title);
        printSeparator();
        System.out.println();
    }

    public static void printSuccess(String message) {
        System.out.println("[ÉXITO] " + message);
    }

    public static void printLine() {
        System.out.println("-------------------------------------------------------");
    }

    public static void printInfo(String message) {
        System.out.println("[INFO] " + message);
    }

    public static void printError(String message) {
        System.out.println("[ERROR] " + message);
    }

    public static void printWarning(String message) {
        System.out.println("[WARNING] " + message);
    }

} 