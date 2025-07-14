package utils;

public class Time {

    /**
     * Hace una pausa de 1.2 segundos en la ejecución del hilo actual.
     * Es para dar efecto de carga o procesameinto en el programa
     */
    public static void sleep(){
        try {
            Thread.sleep(1200);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
