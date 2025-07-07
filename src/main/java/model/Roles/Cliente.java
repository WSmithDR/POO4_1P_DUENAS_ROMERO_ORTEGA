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
            Rol.CLIENTE);
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
    * Consulta el estado de los pedidos del cliente
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

      CategoriaProducto categoriaElegida = seleccionarCategoria(productos, scanner);
      if (categoriaElegida == null)
         return;

      Producto productoElegido = seleccionarProducto(productos, categoriaElegida, scanner);
      if (productoElegido == null)
         return;

      int cantidad = seleccionarCantidad(productoElegido, scanner);
      if (cantidad <= 0)
         return;

      if (!confirmarCompra(productoElegido, cantidad, scanner))
         return;

      procesarCompra(productoElegido, cantidad, usuarios, pedidos);
   }

   /**
    * Le pide al cliente que elija una categoría
    */
   private CategoriaProducto seleccionarCategoria(ArrayList<Producto> productos, Scanner scanner) {
      ArrayList<CategoriaProducto> catProdDisponibles = ManejadorProducto.mostrarCategoriasDisponibles();

      System.out.print("Elige una categoría (1-" + catProdDisponibles.size() + "): ");
      int opcionCategoria = Integer.parseInt(scanner.nextLine()) - 1;

      if (opcionCategoria < 0 || opcionCategoria >= catProdDisponibles.size()) {
         System.out.println("Opción inválida");
         return null;
      }

      return catProdDisponibles.get(opcionCategoria);
   }

   /**
    * Le pide al cliente que elija un producto de esa categoría
    */
   private Producto seleccionarProducto(ArrayList<Producto> productos, CategoriaProducto categoria, Scanner scanner) {
      ArrayList<Producto> productosCategoria = ManejadorProducto.obtenerProductosPorCategoria(productos, categoria);

      if (productosCategoria.isEmpty()) {
         System.out.println("No hay productos en esta categoría");
         return null;
      }

      System.out.println("\nProductos disponibles:");
      for (int i = 0; i < productosCategoria.size(); i++) {
         Producto p = productosCategoria.get(i);
         System.out.println((i + 1) + ". " + p.getNombre() + " - $" + p.getPrecio() + " (Stock: " + p.getStock() + ")");
      }

      System.out.print("Elige un producto (1-" + productosCategoria.size() + "): ");
      int opcionProducto = Integer.parseInt(scanner.nextLine()) - 1;

      if (opcionProducto < 0 || opcionProducto >= productosCategoria.size()) {
         System.out.println("Opción inválida");
         return null;
      }

      return productosCategoria.get(opcionProducto);
   }

   /**
    * Le pide al cliente cuántos quiere comprar
    */
   private int seleccionarCantidad(Producto producto, Scanner scanner) {
      System.out.print("¿Cuántos quieres comprar? (máximo " + producto.getStock() + "): ");
      int cantidad = Integer.parseInt(scanner.nextLine());

      if (cantidad <= 0 || cantidad > producto.getStock()) {
         System.out.println("Cantidad inválida");
         return -1;
      }

      return cantidad;
   }

   /**
    * Le pregunta al cliente si confirma la compra
    */
   private boolean confirmarCompra(Producto producto, int cantidad, Scanner scanner) {
      double total = producto.getPrecio() * cantidad;
      System.out.println("\nTotal a pagar: $" + total);

      System.out.print("¿Confirmar compra? (s/n): ");
      String confirmar = scanner.nextLine().toLowerCase();

      if (!confirmar.equals("s") && !confirmar.equals("si")) {
         System.out.println("Compra cancelada");
         return false;
      }

      return true;
   }

   /**
    * Hace todo el proceso final: asigna repartidor, crea pedido y actualiza stock
    */
   private void procesarCompra(Producto producto, int cantidad, ArrayList<Usuario> usuarios,
         ArrayList<Pedido> pedidos) {
      // Buscar repartidor disponible
      Repartidor repartidorElegido = buscarRepartidorAleatorio(usuarios);
      if (repartidorElegido == null) {
         System.out.println("No hay repartidores disponibles");
         return;
      }

      // Calcular total
      double total = producto.getPrecio() * cantidad;

      // Crear y guardar pedido
      Pedido nuevoPedido = new Pedido(this, repartidorElegido, producto, cantidad, total);
      producto.reducirStock(cantidad);
      ManejadorProducto.actualizarStockProductoEnArchivo(producto);
      ManejadorPedido.guardarPedido(nuevoPedido);
      pedidos.add(nuevoPedido);

      // Enviar notificaciones
      enviarNotificaciones(repartidorElegido, nuevoPedido);

      // Mostrar confirmación
      mostrarConfirmacionCompra(repartidorElegido, nuevoPedido);
   }

   /**
    * Busca un repartidor al azar de la lista
    */
   private Repartidor buscarRepartidorAleatorio(ArrayList<Usuario> usuarios) {
      ArrayList<Repartidor> repartidores = new ArrayList<>();

      for (Usuario u : usuarios) {
         if (u instanceof Repartidor) {
            Repartidor repartidor = (Repartidor) u;
            repartidores.add(repartidor);
         }
      }

      if (repartidores.isEmpty()) {
         return null;
      }

      Random random = new Random();
      return repartidores.get(random.nextInt(repartidores.size()));
   }

   /**
    * Manda los emails al cliente y repartidor
    */
   private void enviarNotificaciones(Repartidor repartidor, Pedido pedido) {
      ManejadorEmail manejadorEmail = new ManejadorEmail();
      if (repartidor != null && pedido != null) {
         Sistema.notificar(repartidor, pedido, manejadorEmail);
         Sistema.notificar(this, pedido, manejadorEmail);
      }
   }

   /**
    * Le dice al cliente que la compra fue exitosa
    */
   private void mostrarConfirmacionCompra(Repartidor repartidor, Pedido pedido) {
      System.out.println("¡Compra exitosa!");
      System.out.println("Repartidor: " + repartidor.getNombre() + " " + repartidor.getApellido());
      System.out.println("Código de pedido: " + pedido.getCodigoPedido());
   }

}
