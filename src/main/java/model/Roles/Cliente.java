package model.Roles;

import java.util.ArrayList;
import java.util.Scanner;
import model.Enums.Rol;
import model.Enums.CategoriaProducto;
import model.Producto;
import model.Pedido;
import services.ManejadorProducto;
import services.ManejadorPedido;
import model.Enums.EstadoPedido;
import persistence.ManejoArchivos;
import java.util.Random;

public class Cliente extends Usuario {
   private String numero_celular;
   private String direccion;

   public Cliente(
         String cedula,
         String user_name,
         ArrayList<String> nombres,
         ArrayList<String> apellidos,
         String correo,
         String contrasenia,
         String numero_celular,
         String direccion) {
      super(
            Rol.CLIENTE,
            cedula,
            user_name,
            nombres,
            apellidos,
            correo,
            contrasenia);
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
    * @param scanner Scanner para leer la entrada del usuario
    */
   public void gestionarPedido(Scanner scanner) {
      String archivo = "Pedidos.txt";
      ArrayList<String> pedidos = ManejoArchivos.LeeFichero(archivo);

      if (pedidos == null || pedidos.isEmpty()) {
         System.out.println("No se pudo leer el archivo de pedidos o está vacío.");
         return;
      }

      System.out.println("===== CONSULTA DE ESTADO DE PEDIDO =====");
      System.out.println("Ingrese el código del pedido:");
      String codPedido = scanner.nextLine().trim();

      if (codPedido.isEmpty()) {
         System.out.println("El código del pedido no puede estar vacío.");
         return;
      }

      consultarPedido(pedidos, codPedido);
   }

   /**
    * Consulta un pedido específico en la lista de pedidos
    * 
    * @param pedidos   Lista de pedidos
    * @param codPedido Código del pedido a consultar
    */
   private void consultarPedido(ArrayList<String> pedidos, String codPedido) {
      boolean pedidoEncontrado = false;

      for (String linea : pedidos) {
         if (linea.trim().isEmpty()) {
            continue;
         }

         String[] datos = linea.split("\\|");

         // Validar que el array tenga suficientes elementos
         if (datos.length < 8 || datos.length > 8) {
            System.out.println("Formato de datos inválido en el archivo de pedidos.");
            continue;
         }

         if (datos[0].equalsIgnoreCase(codPedido)) {
            pedidoEncontrado = true;
            mostrarInformacionPedido(datos);
            break;
         }
      }

      if (!pedidoEncontrado) {
         System.out.println("No se encontró ningún pedido con el código: " + codPedido);
      }
   }

   /**
    * Muestra la información detallada de un pedido
    * 
    * @param datos Array con los datos del pedido
    */
   private void mostrarInformacionPedido(String[] datos) {
      EstadoPedido estadoActual = EstadoPedido.valueOf(datos[5]);

      // Buscar el nombre del producto usando el código del producto
      String nombreProducto = buscarNombreProducto(datos[2]);

      System.out.println("\n=== CONSULTA DE ESTADO DEL PEDIDO ===");
      System.out.println("Código del pedido: " + datos[0]);
      System.out.println("Fecha del pedido: " + datos[1]);
      System.out.println("Código del producto: " + datos[2]);
      System.out.println("Nombre del producto: " + nombreProducto);
      System.out.println("Cantidad: " + datos[3]);
      System.out.println("Valor pagado: $" + datos[4]);
      System.out.println("Estado actual: " + estadoActual);
      System.out.println("Repartidor: " + datos[6]);
      System.out.println("===========================================\n");

      // Mostrar mensaje según el estado del pedido
      mostrarMensajeSegunEstado(estadoActual);
   }

   /**
    * Busca el nombre de un producto por su código
    * 
    * @param codigoProducto Código del producto a buscar
    * @return Nombre del producto o "Producto no encontrado" si no existe
    */
   private String buscarNombreProducto(String codigoProducto) {
      String archivoProductos = "Productos.txt";
      ArrayList<String> productos = ManejoArchivos.LeeFichero(archivoProductos);

      if (productos == null || productos.isEmpty()) {
         return "Error al leer archivo de productos";
      }

      for (String linea : productos) {
         if (linea.trim().isEmpty()) {
            continue;
         }

         String[] datosP = linea.split("\\|");

         // Validar que el array tenga suficientes elementos
         if (datosP.length < 5 || datosP.length > 5) {
            System.out.println("Formato de datos inválido en el archivo de pedidos.");
            continue;
         }

         // Verificar que el código coincida y retornar la posición 2 del array
         if (datosP[0].equalsIgnoreCase(codigoProducto)) {
            return datosP[2];
         }
      }
      return "Producto no encontrado";
   }

   /**
    * Muestra un mensaje personalizado según el estado del pedido
    * 
    * @param estado Estado actual del pedido
    */
   private void mostrarMensajeSegunEstado(EstadoPedido estado) {
      if (estado == null) {
         System.out.println("Estado del pedido: No disponible");
         return;
      }

      switch (estado) {
         case EN_PREPARACION:
            System.out.println("Su pedido está siendo preparado para su envío.");
            break;
         case EN_CAMINO:
            System.out.println("Su pedido está en camino hacia su dirección.");
            break;
         case ENTREGADO:
            System.out.println("Su pedido ha sido entregado exitosamente.");
            break;
         case CANCELADO:
            System.out.println("Su pedido ha sido cancelado.");
            break;
      }
   }

   /**
    * Realiza el proceso de compra para el cliente
    * 
    * @param productos Lista de productos disponibles
    * @param usuarios  Lista de usuarios para buscar repartidores
    * @param pedidos   Lista de pedidos para agregar el nuevo pedido
    * @param scanner   Scanner para leer entrada del usuario
    */
   public boolean realizarCompra(ArrayList<Producto> productos, ArrayList<Usuario> usuarios, ArrayList<Pedido> pedidos,
         Scanner scanner) {
      System.out.println("\n=== COMPRAR PRODUCTO ===");

      // 1. Mostrar categorías
      CategoriaProducto[] categorias = Producto.mostrarCategoriasDisponibles();

      // 2. Seleccionar categoría
      System.out.print("Elige una categoría (1-" + categorias.length + "): ");
      int opcionCategoria = Integer.parseInt(scanner.nextLine()) - 1;
      if (opcionCategoria < 0 || opcionCategoria >= categorias.length) {
         System.out.println("Opción inválida");
         return false;
      }

      // 3. Mostrar productos de esa categoría
      ArrayList<Producto> productosCategoria = ManejadorProducto.obtenerProductosPorCategoria(productos,
            categorias[opcionCategoria]);
      if (productosCategoria.isEmpty()) {
         System.out.println("No hay productos en esta categoría");
         return false;
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
         return false;
      }

      Producto productoElegido = productosCategoria.get(opcionProducto);

      // 5. Pedir cantidad
      System.out.print("¿Cuántos quieres comprar? (máximo " + productoElegido.getStock() + "): ");
      int cantidad = Integer.parseInt(scanner.nextLine());
      if (cantidad <= 0 || cantidad > productoElegido.getStock()) {
         System.out.println("Cantidad inválida");
         return false;
      }

      // 6. Calcular total
      double total = productoElegido.getPrecio() * cantidad;
      System.out.println("\nTotal a pagar: $" + total);

      // 7. Confirmar compra
      System.out.print("¿Confirmar compra? (s/n): ");
      String confirmar = scanner.nextLine().toLowerCase();
      if (!confirmar.equals("s") && !confirmar.equals("si")) {
         System.out.println("Compra cancelada");
         return false;
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
         return false;
      } else {
         // 9. Elegir repartidor al azar
         Random random = new Random();
         Repartidor repartidorElegido = repartidores.get(random.nextInt(repartidores.size()));

         // 10. Crear pedido
         Pedido nuevoPedido = new Pedido(this, repartidorElegido, productoElegido, cantidad, total);
         productoElegido.reducirStock(cantidad);
         ManejadorPedido.guardarPedido(nuevoPedido);
         pedidos.add(nuevoPedido);

         System.out.println("¡Compra exitosa!");
         System.out.println(
               "Repartidor: " + repartidorElegido.getNombres().get(0) + " " + repartidorElegido.getApellidos().get(0));
         System.out.println("Código de pedido: " + nuevoPedido.getCodigoPedido());
         return true;
      }
   }

}
