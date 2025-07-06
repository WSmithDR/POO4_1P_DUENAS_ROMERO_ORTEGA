package model.Roles;

import java.util.ArrayList;
import java.util.Scanner;

import model.Enums.Rol;
import persistence.ManejoArchivos;

public class Cliente extends Usuario {
   private String numero_celular;   
   private String direccion;

   Scanner sc = new Scanner(System.in);

   public Cliente(String cedula, String user_name, ArrayList<String> nombres, ArrayList<String> apellidos, String correo, String contrasenia,String numero_celular, String direccion){
      super(Rol.CLIENTE, cedula, user_name, nombres, apellidos, correo, contrasenia);
      this.numero_celular = numero_celular;
      this.direccion = direccion;
   }

   //getters
   public String getNumeroCelular(){
      return this.numero_celular;
   }

   public String getDireccion(){
      return this.direccion;
   }

   //setters
   public void setNumeroCelular(String numero_celular){
      this.numero_celular = numero_celular;
   }

   public void setDireccion(String direccion){
      this.direccion = direccion;
   }

   public void gestionarPedido() {
      String archivo = "Pedidos.txt";
      ArrayList<String> pedidos = ManejoArchivos.LeeFichero(archivo);
      
      if (pedidos == null || pedidos.isEmpty()) {
         System.out.println("No se pudo leer el archivo de pedidos o está vacío.");
         return;
      }
      
      System.out.println("=====CONSULTA DE ESTADO DE PEDIDO=====");
      System.out.println("Ingrese el código del pedido:");
      String codPedido = sc.nextLine().trim();
      
      if (codPedido.isEmpty()) {
         System.out.println("El código del pedido no puede estar vacío.");
         return;
      }
      
      boolean pedidoEncontrado = false;
      
      for (String linea : pedidos) {
         if (linea.trim().isEmpty()) {
            continue;
         }
         
         String[] datos = linea.split(",");
         
         if (datos[0].equalsIgnoreCase(codPedido)) {
            pedidoEncontrado = true;
            
            // Buscar el nombre del producto usando el código del producto
            String nombreProducto = buscarNombreProducto(datos[2]);
            
            System.out.println("\n=== CONSULTA DE ESTADO DEL PEDIDO ===");
            System.out.println("Código del pedido: " + datos[0]);
            System.out.println("Fecha del pedido: " + datos[1]);
            System.out.println("Código del producto: " + datos[2]);
            System.out.println("Nombre del producto: " + nombreProducto);
            System.out.println("Cantidad: " + datos[3]);
            System.out.println("Valor pagado: $" + datos[4]);
            System.out.println("Estado actual: " + datos[5]);
            System.out.println("Repartidor: " + datos[6]);
            System.out.println("===========================================\n");
            
            // Mostrar mensaje según el estado del pedido
            mostrarMensajeSegunEstado(datos[5]);
            break;
         }
      }
      
      if (!pedidoEncontrado) {
         System.out.println("No se encontró ningún pedido con el código: " + codPedido);
      }
   }
   
   private String buscarNombreProducto(String codigoProducto) {
      String archivoProductos = "Productos.txt";
      ArrayList<String> productos = ManejoArchivos.LeeFichero(archivoProductos);
      
      for (String linea : productos) {
         if (linea.trim().isEmpty()) {
            continue;
         }
         
         String[] datosP = linea.split(",");
         
         // Verificar que  el código coincida y retornar la posición 2 del array
         if (datosP[0].equalsIgnoreCase(codigoProducto)) {
            return datosP[2];
         }
      }
      return "Producto no encontrado";
   }
   
   private void mostrarMensajeSegunEstado(String estado) {
      switch (estado.toUpperCase()) {
         case "EN PREPARACIÓN":
            System.out.println("Su pedido está siendo preparado para su envío.");
            break;
         case "EN RUTA":
            System.out.println("Su pedido está en camino hacia su dirección.");
            break;
         case "ENTREGADO":
            System.out.println("Su pedido ha sido entregado exitosamente.");
            break;
         default:
            System.out.println("Estado del pedido: " + estado);
            break;
      }
   }
}
