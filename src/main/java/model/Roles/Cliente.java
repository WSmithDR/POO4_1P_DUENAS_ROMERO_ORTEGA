package model.Roles;

import java.util.ArrayList;
import java.util.Scanner;

import app.Sistema;
import model.Enums.Rol;
import model.Enums.CategoriaProducto;
import model.Producto;
import model.Pedido;
import services.archivos.ManejadorPedido;
import services.archivos.ManejadorProducto;
import services.email.ManejadorEmail;
import java.util.Random;

public class Cliente extends Usuario {
   private String numero_celular;
   private String direccion;

   public Cliente(
         String codigoUnico,
         String cedula,
         String nombre,
         String apellido,
         String user_name,
         String correo,
         String contrasenia,
         String numero_celular,
         String direccion) {
      super(
            codigoUnico,
            cedula,
            nombre,
            apellido,
            user_name,
            correo,
            contrasenia,
            Rol.CLIENTE
            );
      this.numero_celular = numero_celular;
      this.direccion = direccion;
   }

   /**
    * Obtiene el número de celular del cliente.
    * @return Número de celular
    */
   public String getNumeroCelular() {
      return this.numero_celular;
   }

   /**
    * Obtiene la dirección del cliente.
    * @return Dirección del cliente
    */
   public String getDireccion() {
      return this.direccion;
   }

   /**
    * Establece el número de celular del cliente.
    * @param numero_celular Número de celular
    */
   public void setNumeroCelular(String numero_celular) {
      this.numero_celular = numero_celular;
   }

   /**
    * Establece la dirección del cliente.
    * @param direccion Dirección del cliente
    */
   public void setDireccion(String direccion) {
      this.direccion = direccion;
   }

   /**
    * Gestiona la consulta de estado de pedidos del cliente
    * 
    * @param pedidos Lista de pedidos disponibles para consultar
    */
   @Override
   public void gestionarPedido(ArrayList<Pedido> pedidos, Scanner scanner) {
      ManejadorPedido.gestionarPedido(this, pedidos, scanner);
   }

   /**
    * Realiza el proceso de compra para el cliente
    * 
    * @param productos Lista de productos disponibles
    * @param usuarios  Lista de usuarios para buscar repartidores
    * @param pedidos   Lista de pedidos para agregar el nuevo pedido
    * @param scanner   Scanner para leer entrada del usuario
    */
   public void realizarCompra(ArrayList<Producto> productos, ArrayList<Usuario> usuarios, ArrayList<Pedido> pedidos,
         Scanner scanner) {
      System.out.println("\n=== COMPRAR PRODUCTO ===");

      // 1. Mostrar categorías de productos que estan disponibles
      ArrayList<CategoriaProducto> catProdDisponibles = 
      ManejadorProducto.mostrarCategoriasDisponibles();   
      // 2. Seleccionar categoría
      System.out.print("Elige una categoría (1-" + catProdDisponibles.size() + "): ");
      int opcionCategoria = Integer.parseInt(scanner.nextLine()) - 1;
      if (opcionCategoria < 0 || opcionCategoria >= catProdDisponibles.size()) {
         System.out.println("Opción inválida");
         return;
      }

      // 3. Mostrar productos de esa categoría
      ArrayList<Producto> productosCategoria = 
      ManejadorProducto.
      obtenerProductosPorCategoria(
         productos,
         catProdDisponibles.get(opcionCategoria)
         );
      if (productosCategoria.isEmpty()) {
         System.out.println("No hay productos en esta categoría");
         return;
      }

      System.out.println("\nProductos disponibles:");
      for (int i = 0; i < productosCategoria.size(); i++) {
         Producto p = productosCategoria.get(i);
         System.out.println((i + 1) + ". " + p.getNombre() + " - $" + p.getPrecio() + " (Stock: " + p.getStock() + ")");
      }

      // 4. Seleccionar producto
      System.out.print("Elige un producto (1-" + productosCategoria.size() + "): ");
      int opcionProducto = Integer.parseInt(scanner.nextLine()) - 1;
      if (opcionProducto < 0 || opcionProducto >= productosCategoria.size()) {
         System.out.println("Opción inválida");
         return;
      }

      Producto productoElegido = productosCategoria.get(opcionProducto);

      // 5. Pedir cantidad
      System.out.print("¿Cuántos quieres comprar? (máximo " + productoElegido.getStock() + "): ");
      int cantidad = Integer.parseInt(scanner.nextLine());
      if (cantidad <= 0 || cantidad > productoElegido.getStock()) {
         System.out.println("Cantidad inválida");
         return;
      }

      // 6. Calcular total
      double total = productoElegido.getPrecio() * cantidad;
      System.out.println("\nTotal a pagar: $" + total);

      // 7. Pedir número de tarjeta
      String numeroTarjeta = "";
      boolean tarjetaValida = false;
      while (!tarjetaValida) {
          System.out.print("Ingrese su número de tarjeta (16 dígitos): ");
          numeroTarjeta = scanner.nextLine();
          if (numeroTarjeta.length() == 16 && numeroTarjeta.matches("\\d+")) {
              tarjetaValida = true;
          } else {
              System.out.println("Número de tarjeta inválido. Debe tener 16 dígitos numéricos.");
          }
      }

      // 8. Simular pago
      System.out.println("Pago realizado exitosamente.");

      // 9. Confirmar compra
      System.out.print("¿Confirmar compra? (s/n): ");
      String confirmar = scanner.nextLine().toLowerCase();
      if (!confirmar.equals("s") && !confirmar.equals("si")) {
         System.out.println("Compra cancelada");
         return;
      }

      // 8. Buscar/Asginar repartidor
      ArrayList<Repartidor> repartidores = new ArrayList<>();
      for (Usuario u : usuarios) {
         if (u instanceof Repartidor) {
            Repartidor repartidor = (Repartidor) u;
            repartidores.add(repartidor);
         }
      }

      if (repartidores.isEmpty()) {
         System.out.println("No hay repartidores disponibles");
         return;
      } else {
         // 9. Elegir repartidor al azar
         Random random = new Random();
         Repartidor repartidorElegido = repartidores.get(random.nextInt(repartidores.size()));
         // 10. Crear pedido
         Pedido nuevoPedido = new Pedido(this, repartidorElegido, productoElegido, cantidad, total);
         productoElegido.reducirStock(cantidad);
         ManejadorProducto.actualizarStockProductoEnArchivo(productoElegido);
         ManejadorPedido.guardarPedido(nuevoPedido);
         pedidos.add(nuevoPedido);

         ManejadorEmail manejadorEmail = new ManejadorEmail();
         if (repartidorElegido != null && nuevoPedido != null) {
            Sistema.notificar(repartidorElegido, nuevoPedido, manejadorEmail);
            Sistema.notificar(this, nuevoPedido, manejadorEmail);
            System.out.println("¡Compra exitosa!");
            System.out.println(
                  "Repartidor: " + repartidorElegido.getNombre() + " " + repartidorElegido.getApellido());
            System.out.println("Código de pedido: " + nuevoPedido.getCodigoPedido());
            return;
         } else {
            System.out.println("Error al procesar la compra.");
            return;
         }
      }
   }

}
