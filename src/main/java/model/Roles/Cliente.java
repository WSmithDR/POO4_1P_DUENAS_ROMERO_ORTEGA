package model.Roles;

import java.util.ArrayList;
import java.util.Scanner;
import model.Enums.Rol;
import model.Enums.CategoriaProducto;
import model.Producto;
import model.Pedido;
import services.ManejadorProducto;
import services.ManejadorPedido;
import java.util.Random;

public class Cliente extends Usuario {
   private String numero_celular;
   private String direccion;

   public Cliente(String cedula, String user_name, ArrayList<String> nombres, ArrayList<String> apellidos,
         String correo, String contrasenia, String numero_celular, String direccion) {
      super(Rol.CLIENTE, cedula, user_name, nombres, apellidos, correo, contrasenia);
      this.numero_celular = numero_celular;
      this.direccion = direccion;
   }

   // getters
   public String getNumeroCelular() {
      return this.numero_celular;
   }

   public String getDireccion() {
      return this.direccion;
   }

   // setters
   public void setNumeroCelular(String numero_celular) {
      this.numero_celular = numero_celular;
   }

   public void setDireccion(String direccion) {
      this.direccion = direccion;
   }

   /**
    * Gestiona la consulta de estado de pedidos del cliente
    * 
    * @param pedidos Lista de pedidos disponibles para consultar
    */
   @Override
   public void gestionarPedido(ArrayList<Pedido> pedidos) {
      Scanner scanner = new Scanner(System.in);
      services.ManejadorPedido.gestionarPedido(this, pedidos, scanner);
      // No cerrar el scanner aquí ya que podría estar en uso en otros lugares
   }

   /**
    * Realiza el proceso de compra para el cliente
    * @param productos Lista de productos disponibles
    * @param usuarios Lista de usuarios para buscar repartidores
    * @param pedidos Lista de pedidos para agregar el nuevo pedido
    * @param scanner Scanner para leer entrada del usuario
    */
   public void realizarCompra(ArrayList<Producto> productos, ArrayList<Usuario> usuarios, ArrayList<Pedido> pedidos, Scanner scanner) {
      System.out.println("\n=== COMPRAR PRODUCTO ===");
      
      // 1. Mostrar categorías
      System.out.println("\nCategorías disponibles:");
      CategoriaProducto[] categorias = CategoriaProducto.values();
      for (int i = 0; i < categorias.length; i++) {
          System.out.println((i + 1) + ". " + categorias[i]);
      }
      
      // 2. Seleccionar categoría
      System.out.print("Elige una categoría (1-" + categorias.length + "): ");
      int opcionCategoria = Integer.parseInt(scanner.nextLine()) - 1;
      if (opcionCategoria < 0 || opcionCategoria >= categorias.length) {
          System.out.println("Opción inválida");
          return;
      }
      
      // 3. Mostrar productos de esa categoría
      ArrayList<Producto> productosCategoria = ManejadorProducto.obtenerProductosPorCategoria(productos, categorias[opcionCategoria]);
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
      
      // 7. Confirmar compra
      System.out.print("¿Confirmar compra? (s/n): ");
      String confirmar = scanner.nextLine().toLowerCase();
      if (!confirmar.equals("s") && !confirmar.equals("si")) {
          System.out.println("Compra cancelada");
          return;
      }
      
      // 8. Buscar repartidor
      ArrayList<Repartidor> repartidores = new ArrayList<>();
      for (Usuario u : usuarios) {
          if (u instanceof Repartidor) {
              repartidores.add((Repartidor) u);
          }
      }
      
      if (repartidores.isEmpty()) {
          System.out.println("No hay repartidores disponibles");
          return;
      }
      
      // 9. Elegir repartidor al azar
      Random random = new Random();
      Repartidor repartidorElegido = repartidores.get(random.nextInt(repartidores.size()));
      
      // 10. Crear pedido
      Pedido nuevoPedido = new Pedido(this, repartidorElegido, productoElegido, cantidad, total);
      productoElegido.reducirStock(cantidad);
      ManejadorPedido.guardarPedido(nuevoPedido);
      pedidos.add(nuevoPedido);
      
      System.out.println("¡Compra exitosa!");
      System.out.println("Repartidor: " + repartidorElegido.getNombres().get(0) + " " + repartidorElegido.getApellidos().get(0));
      System.out.println("Código de pedido: " + nuevoPedido.getCodigoPedido());
   }
}
