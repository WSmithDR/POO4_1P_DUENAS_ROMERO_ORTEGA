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

   public void gestionarPedido () {
      String archivo="Pedidos.txt";
      ArrayList<String> pedidos = ManejoArchivos.LeeFichero(archivo);
      System.out.println("=====CONSULTA DE ESTADO DE PEDIDO=====");
      System.out.println("Ingrese el código del pedido:");
      String codPedido=sc.nextLine();

      for(String linea:pedidos){
         if(linea.trim().isEmpty()){
            continue;
         }
         String[] datos=linea.split(",");

         if(datos[0].equalsIgnoreCase(codPedido)){
            System.out.println(" ");
            System.out.println("Fecha del pedido: "+datos[1]);
            System.out.println("Producto comprado: "+datos[2]);
            System.out.println("Cantidad: "+datos[3]);
            System.out.println("Valor pagado: "+datos[4]);
            System.out.println("Estado actual: "+datos[5]);
            System.out.println("Repartidor: "+datos[6]);
            System.out.println(" ");
            System.out.println(" ");
            System.out.println("Su pedido está siendo preparado para su envío.");

         }
      }

      

      
      
      
      
      




   }





}
