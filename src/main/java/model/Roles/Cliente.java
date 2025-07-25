package model.Roles;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

import app.Sistema;
import model.Enums.Rol;
import model.Enums.CategoriaProducto;
import model.Producto;
import model.Pedido;
import services.archivos.ManejadorPedido;
import services.archivos.ManejadorProducto;
import services.archivos.ManejadorUsuario;
import services.email.ManejadorEmail;
import utils.ManejoFechas;
import utils.Printers;

import java.util.Random;

/**
 * Clase que representa a un cliente del sistema.
 */
public class Cliente extends Usuario {
   private String numero_celular;
   private String direccion;

   /**
    * Constructor de la clase Cliente.
    * @param codigoUnico Código único del cliente
    * @param cedula Cédula del cliente
    * @param nombre Nombre del cliente
    * @param apellido Apellido del cliente
    * @param user_name Nombre de usuario
    * @param correo Correo electrónico
    * @param contrasenia Contraseña
    * @param numero_celular Número de celular
    * @param direccion Dirección del cliente
    */
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
    * 
    * @return Número de celular
    */
   public String getNumeroCelular() {
      return this.numero_celular;
   }

   /**
    * Obtiene la dirección del cliente.
    * 
    * @return Dirección del cliente
    */
   public String getDireccion() {
      return this.direccion;
   }

   /**
    * Establece el número de celular del cliente.
    * 
    * @param numero_celular Número de celular
    */
   public void setNumeroCelular(String numero_celular) {
      this.numero_celular = numero_celular;
   }

   /**
    * Establece la dirección del cliente.
    * 
    * @param direccion Dirección del cliente
    */
   public void setDireccion(String direccion) {
      this.direccion = direccion;
   }

   /**
    * Muestra la información detallada de un pedido.
    * @param pedido Pedido a mostrar
    */
   private static void mostrarInformacionPedido(Pedido pedido) {
      // Obtener nombre del producto
      Producto productoPedido = ManejadorProducto.buscarProductoPorCodigo(pedido.getCodProducto());
      String nombreProducto = null;
      if (productoPedido != null) {
         nombreProducto = productoPedido.getNombre();
      } else {
         Printers.printInfo("No se pudo hallar el producto del pedido");
      }
      // Obtener nombre del repartidor
      Repartidor repartidor = ManejadorUsuario.buscarRepartidorPorCodigoUnico(pedido.getCodRepartidor());
      String nombreRepartidor = null;
      
      if (repartidor != null) {
         nombreRepartidor = String.format(
            Locale.US,
            "%s %s",
            repartidor.getNombre(),
            repartidor.getApellido());
      } else {
         Printers.printInfo("No se pudo hallar el repartidor del pedido");
      }
      Printers.printLine();
      System.out.println("Fecha del pedido: " + ManejoFechas.setFechaSimple(pedido.getFechaPedido()));
      System.out.println("Producto comprado: " + (nombreProducto != null ? nombreProducto : "-") + " (Código: " + pedido.getCodProducto() + ")");
      System.out.println("Cantidad: " + pedido.getCantidadProducto());
      System.out.println("Valor pagado: $" + String.format("%.2f", pedido.getTotalPagado()));
      System.out.println("Estado actual: " + pedido.getEstadoPedido());
      System.out.println("Repartidor: " + (nombreRepartidor != null ? nombreRepartidor : "-") + " (Código: " + pedido.getCodRepartidor() + ")");
      System.out.println();
      Printers.printLine();
      // Mostrar mensaje según el estado del pedido
      Pedido.mostrarMensajeSegunEstado(pedido.getEstadoPedido());
   }

   /**
    * Consulta un pedido específico para un cliente.
    * @param codCliente Código único del cliente
    * @param codPedido Código del pedido a consultar
    */
   private void consultarPedidoCliente(String codCliente, String codPedido) {
      boolean pedidoEncontrado = false;
      for (Pedido pedido : ManejadorPedido.cargarPedidosCliente(codCliente)) {
         if (pedido.getCodigoPedido().equalsIgnoreCase(codPedido)) {
            pedidoEncontrado = true;
            mostrarInformacionPedido(pedido);
            break;
         }
      }
      if (!pedidoEncontrado) {
         Printers.printInfo("No se encontró ningún pedido con el código: " + codPedido + " para este cliente.");
      }
   }

   /**
    * Muestra la lista de pedidos del cliente con formato similar al repartidor.
    */
   private void mostrarListaPedidos() {
      ArrayList<Pedido> pedidos = ManejadorPedido.cargarPedidosCliente(this.getCodigoUnico());
      if (pedidos.isEmpty()) {
         Printers.printInfo("No tienes pedidos registrados.");
         return;
      }
      Printers.printTitle("TUS PEDIDOS");
      for (int i = 0; i < pedidos.size(); i++) {
         Pedido pedido = pedidos.get(i);
         System.out.println((i + 1) + ". Código: " + pedido.getCodigoPedido());
         System.out.println("   Fecha: " + utils.ManejoFechas.setFechaSimple(pedido.getFechaPedido()));
         System.out.println("   Estado: " + pedido.getEstadoPedido());
         Printers.printLine();
      }
   }

   /**
    * Gestiona la consulta de estado de pedidos del cliente.
    * @param scanner Scanner para leer la entrada del usuario
    */
   @Override
   public void gestionarPedido(Scanner scanner) {
      Printers.printTitle("CONSULTA DE ESTADO DE PEDIDO");
      mostrarListaPedidos();
      ArrayList<Pedido> pedidos = ManejadorPedido.cargarPedidosCliente(this.getCodigoUnico());
      if (pedidos.isEmpty()) {
         return;
      }
      System.out.print("Ingrese el código del pedido: ");
      String codPedido = scanner.nextLine().trim();

      if (codPedido.isEmpty()) {
         Printers.printError("El código del pedido no puede estar vacío.");
         return;
      }

      consultarPedidoCliente(this.getCodigoUnico(), codPedido);
   }

   /**
    * Realiza el proceso de compra para el cliente.
    * @param usuarios Lista de usuarios para buscar repartidores
    * @param scanner Scanner para leer entrada del usuario
    */
   public void realizarCompra(ArrayList<Usuario> usuarios,
         Scanner scanner) {
      ArrayList<Producto> productos = ManejadorProducto.cargarProductos();
      Printers.printTitle("COMPRAR PRODUCTO");

      CategoriaProducto categoriaElegida = seleccionarCategoria(productos, scanner);
      if (categoriaElegida == null)
         return;

      Producto productoElegido = seleccionarProducto(productos, categoriaElegida, scanner);
      if (productoElegido == null)
         return;

      int cantidad = seleccionarCantidad(productoElegido, scanner);
      if (cantidad <= 0) {
         Printers.printError("Cantidad inválida");
         return;
      }

      if (!confirmarCompra(productoElegido, cantidad, scanner))
         return;

      procesarCompra(productoElegido, cantidad, usuarios);
   }

   /**
    * Le pide al cliente que elija una categoría de producto.
    * @param productos Lista de productos disponibles
    * @param scanner Scanner para leer entrada del usuario
    * @return Categoría seleccionada o null si no es válida
    */
   private CategoriaProducto seleccionarCategoria(ArrayList<Producto> productos, Scanner scanner) {
      ArrayList<CategoriaProducto> catProdDisponibles = ManejadorProducto.mostrarCategoriasDisponibles();

      System.out.print("Elige una categoría (1-" + catProdDisponibles.size() + "): ");
      int opcionCategoria = Integer.parseInt(scanner.nextLine()) - 1;

      if (opcionCategoria < 0 || opcionCategoria >= catProdDisponibles.size()) {
         Printers.printError("Opción inválida");
         return null;
      }

      return catProdDisponibles.get(opcionCategoria);
   }

   /**
    * Le pide al cliente que elija un producto de una categoría.
    * @param productos Lista de productos disponibles
    * @param categoria Categoría seleccionada
    * @param scanner Scanner para leer entrada del usuario
    * @return Producto seleccionado o null si no es válido
    */
   private Producto seleccionarProducto(ArrayList<Producto> productos, CategoriaProducto categoria, Scanner scanner) {
      ArrayList<Producto> productosCategoria = ManejadorProducto.obtenerProductosPorCategoria(productos, categoria);

      if (productosCategoria.isEmpty()) {
         Printers.printInfo("No hay productos en esta categoría");
         return null;
      }
      Printers.printLine();
      System.out.println("\nProductos disponibles:");
      for (int i = 0; i < productosCategoria.size(); i++) {
         Producto p = productosCategoria.get(i);
         System.out.println((i + 1) + ". " + p.getNombre() + " - $" + p.getPrecio() + " (Stock: " + p.getStock() + ")");
      }

      System.out.print("Elige un producto (1-" + productosCategoria.size() + "): ");
      int opcionProducto = Integer.parseInt(scanner.nextLine()) - 1;

      if (opcionProducto < 0 || opcionProducto >= productosCategoria.size()) {
         Printers.printError("Opción inválida");
         return null;
      }

      return productosCategoria.get(opcionProducto);
   }

   /**
    * Le pide al cliente que ingrese la cantidad de productos a comprar.
    * @param producto Producto seleccionado
    * @param scanner Scanner para leer entrada del usuario
    * @return Cantidad seleccionada
    */
   private int seleccionarCantidad(Producto producto, Scanner scanner) {
      System.out.print("¿Cuántos quieres comprar? (máximo " + producto.getStock() + "): ");
      int cantidad = Integer.parseInt(scanner.nextLine());

      if (cantidad <= 0 || cantidad > producto.getStock()) {
         Printers.printError("Cantidad inválida");
         return -1;
      }

      return cantidad;
   }

   /**
    * Confirma la compra con el cliente antes de procesarla.
    * @param producto Producto seleccionado
    * @param cantidad Cantidad seleccionada
    * @param scanner Scanner para leer entrada del usuario
    * @return true si el cliente confirma la compra, false en caso contrario
    */
   private boolean confirmarCompra(Producto producto, int cantidad, Scanner scanner) {
      double total = producto.getPrecio() * cantidad;
      System.out.println("\nTotal a pagar: $" + total);

      System.out.print("¿Confirmar compra? (s/n): ");
      String confirmar = scanner.nextLine().toLowerCase();

      if (!confirmar.equals("s") && !confirmar.equals("si")) {
         Printers.printInfo("Compra cancelada");
         return false;
      }

      return true;
   }

   /**
    * Procesa la compra: asigna repartidor, crea pedido y actualiza stock.
    * @param producto Producto seleccionado
    * @param cantidad Cantidad seleccionada
    * @param usuarios Lista de usuarios para buscar repartidores
    */
   private void procesarCompra(Producto producto, int cantidad, ArrayList<Usuario> usuarios) {
      // Buscar repartidor disponible
      Repartidor repartidorElegido = buscarRepartidorAleatorio(usuarios);
      if (repartidorElegido == null) {
         Printers.printInfo("No hay repartidores disponibles");
         return;
      }

      // Calcular total
      double total = producto.getPrecio() * cantidad;

      // Crear y guardar pedido
      Pedido nuevoPedido = new Pedido(this.getCodigoUnico(), repartidorElegido.getCodigoUnico(), producto.getCodigo(), cantidad, total);
      producto.reducirStock(cantidad);
      ManejadorProducto.actualizarStockProductoEnArchivo(producto);
      ManejadorPedido.guardarPedido(nuevoPedido);
      // pedidos.add(nuevoPedido);

      // Enviar notificaciones
      enviarNotificaciones(repartidorElegido, nuevoPedido);

      // Mostrar confirmación
      mostrarConfirmacionCompra(repartidorElegido, nuevoPedido);
   }

   /**
    * Busca un repartidor al azar de la lista de usuarios.
    * @param usuarios Lista de usuarios
    * @return Repartidor seleccionado o null si no hay disponibles
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
    * Envía notificaciones por email al cliente y repartidor sobre el pedido.
    * @param repartidor Repartidor asignado
    * @param pedido Pedido realizado
    */
   private void enviarNotificaciones(Repartidor repartidor, Pedido pedido) {
      ManejadorEmail manejadorEmail = new ManejadorEmail();
      if (repartidor != null && pedido != null) {
         Sistema.notificar(repartidor, pedido, manejadorEmail);
         Sistema.notificar(this, pedido, manejadorEmail);
      }
   }

   /**
    * Muestra la confirmación de compra al cliente.
    * @param repartidor Repartidor asignado
    * @param pedido Pedido realizado
    */
   private void mostrarConfirmacionCompra(Repartidor repartidor, Pedido pedido) {
      System.out.println("¡Compra exitosa!");
      System.out.println("Repartidor: " + repartidor.getNombre() + " " + repartidor.getApellido());
      System.out.println("Código de pedido: " + pedido.getCodigoPedido());
   }

   /**
    * Compara si dos clientes son iguales según la lógica de la superclase.
    * @param obj Objeto a comparar
    * @return true si son iguales, false en caso contrario
    */
   @Override
   public boolean equals(Object obj) {
      return super.equals(obj);
   }

}
