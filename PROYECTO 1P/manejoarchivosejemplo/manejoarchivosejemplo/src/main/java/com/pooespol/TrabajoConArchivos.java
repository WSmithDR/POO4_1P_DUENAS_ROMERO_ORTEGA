package com.pooespol;

public class TrabajoConArchivos {
    public static void main(String[] arg) {
        ManejoArchivos.EscribirArchivo("archivosalida.txt","Hola!");
        ManejoArchivos.EscribirArchivo("archivosalida.txt","Adios!");
        ManejoArchivos.EscribirArchivo("archivosalida.txt","NUevo!");
        ManejoArchivos.LeeFichero("archivosalida.txt");
  
        
        
    }

}
