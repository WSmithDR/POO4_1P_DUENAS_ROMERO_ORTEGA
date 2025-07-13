package model.Roles;

import java.util.ArrayList;
import java.util.Scanner;

import app.Sistema;
import model.Enums.Rol;
import model.Enums.CategoriaProducto;
import model.Enums.EstadoPedido;
import model.Producto;
import model.Pedido;
import services.archivos.ManejadorPedido;
import services.archivos.ManejadorProducto;
import services.email.ManejadorEmail;
import persistence.ManejoArchivos;
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
    * Consulta el estado de los pedidos del cliente
    */
   @Override
   public void gestionarPedido(ArrayList<Pedido> pedidos, Scanner scanner) {
      System.out.println("===== CONSULTA DE ESTADO DE PEDIDO =====");
      System.out.println("Ingrese el código del pedido:");
      String codPedido = scanner.nextLine().trim();

      if (codPedido.isEmpty()) {
         System.out.println("El código del pedido no puede estar vacío.");
         return;
      }

      consultarPedidoDesdeArchivo(codPedido);
   }

   /**
    * Consulta un pedido específico desde el archivo
    * @param codPedido Código del pedido a consultar
    */
   private void consultarPedidoDesdeArchivo(String codPedido) {
      String[] infoPedido = ManejadorPedido.obtenerInformacionPedidoDesdeArchivo(codPedido, this.getCodigoUnico());
      
      if (infoPedido == null) {
         System.out.println("No se encontró ningún pedido con el código: " + codPedido + " para este cliente.");
         return;
      }

      mostrarInformacionPedidoDesdeArchivo(infoPedido);
   }

   /**
    * Muestra la información detallada de un pedido desde el archivo
    * @param infoPedido Array con la información del pedido
    */
   private void mostrarInformacionPedidoDesdeArchivo(String[] infoPedido) {
      String fecha = infoPedido[1];
      String codigoProducto = infoPedido[2];
      String cantidad = infoPedido[3];
      String valorPagado = infoPedido[4];
      String estadoStr = infoPedido[5];
      String codigoRepartidor = infoPedido[6];

      // Obtener nombre del producto
      String nombreProducto = obtenerNombreProducto(codigoProducto);
      
      // Obtener nombre del repartidor
      String nombreRepartidor = obtenerNombreRepartidor(codigoRepartidor);
      
      System.out.println("Fecha del pedido: " + fecha);
      System.out.println("Producto comprado: " + nombreProducto + " (Código: " + codigoProducto + ") Cantidad: " + cantidad);
      System.out.println("Valor pagado: $" + valorPagado);
      System.out.println("Estado actual: " + estadoStr);
      System.out.println("Repartidor: " + nombreRepartidor);
      System.out.println();

      // Mostrar mensaje según el estado del pedido
      mostrarMensajeSegunEstado(EstadoPedido.valueOf(estadoStr));
   }

   /**
    * Obtiene el nombre de un producto por su código
    * @param codigoProducto Código del producto
    * @return Nombre del producto o "Producto no encontrado" si no existe
    */
   private String obtenerNombreProducto(String codigoProducto) {
      String archivoProductos = "PROYECTO 1P/resources/Productos.txt";
               ArrayList<String> productos = ManejoArchivos.LeeFichero(archivoProductos);

      if (productos == null || productos.isEmpty()) {
         return "Error al leer archivo de productos";
      }

      // Buscar desde la última línea hacia la primera para obtener la versión más reciente
      for (int i = productos.size() - 1; i > 0; i--) {
         String linea = productos.get(i);
         if (linea.trim().isEmpty()) {
            continue;
         }

         String[] datosP = linea.split("\\|");

         // Validar que el array tenga suficientes elementos
         if (datosP.length < 5) {
            continue;
         }

         // Verificar que el código coincida y retornar la posición 2 del array (nombre)
         if (datosP[0].equalsIgnoreCase(codigoProducto)) {
            return datosP[2];
         }
      }
      return "Producto no encontrado";
   }
   
   /**
    * Obtiene el nombre de un repartidor por su código único
    * @param codUnico código único del repartidor
    * @return Nombre del repartidor o "Repartidor no encontrado" si no existe
    */
   private String obtenerNombreRepartidor(String codUnico) {
      String archivoUsuarios = "PROYECTO 1P/resources/Usuarios.txt";
               ArrayList<String> lineas = ManejoArchivos.LeeFichero(archivoUsuarios);

      if (lineas == null || lineas.isEmpty()) {
         return "Error al leer archivo de usuarios";
      }

      // Buscar desde la última línea hacia la primera para obtener la versión más reciente
      for (int i = lineas.size() - 1; i > 0; i--) {
         String linea = lineas.get(i);
         if (linea.trim().isEmpty()) {
            continue;
         }

            String[] datosU = linea.split("\\|");

            // Validar que el array tenga suficientes elementos
            if (datosU.length < 8) {
               continue;
            }

         // Verificar que sea un repartidor (R) y que el codUnico coincidan
         if (datosU[7].equals("R") && datosU[0].equals(codUnico)) {
            return datosU[2] + " " + datosU[3]; // nombres + apellidos
         }
      }
      return "Repartidor no encontrado";
   }
   
   /**
    * Muestra un mensaje personalizado según el estado del pedido
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
