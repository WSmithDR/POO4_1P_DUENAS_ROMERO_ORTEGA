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
    * Muestra la información detallada de un pedido
    * 
    * @param pedido Pedido a mostrar
    */
   private static void mostrarInformacionPedido(Pedido pedido) {
      // Obtener nombre del producto
      Producto productoPedido = ManejadorProducto.buscarProductoPorCodigo(pedido.getProducto().getCodigo());
      String nombreProducto = null;
      if (productoPedido != null) {
         nombreProducto = productoPedido.getNombre();
      } else {
         Printers.printWarning("No se pudo hallar el producto del pedido");
      }
      
      // Obtener nombre del repartidor
      Repartidor repartidor = ManejadorUsuario.buscarRepartidorPorCodigoUnico(pedido.getRepartidor().getCodigoUnico());
      String nombreRepartidor = null;
      
      if (repartidor != null) {
         nombreRepartidor = String.format(
            Locale.US,
            "%s %s",
            repartidor.getNombre(),
            repartidor.getApellido());
         }else{
            Printers.printWarning("No se pudo hallar el repartidor del pedido");
         }
         
      Printers.printLine();
      //System.out.println(String.format("=====PEDIDO: %s ======",pedido.getCodigoPedido()));   
      System.out.println("Fecha del pedido: " + ManejoFechas.setFechaSimple(pedido.getFechaPedido()));
      System.out.println("Producto comprado: " + nombreProducto + " (Código: " + pedido.getProducto().getCodigo()+ ")");
            System.out.println("Cantidad: " + pedido.getCantidadProducto());
      System.out.println("Valor pagado: $" + String.format("%.2f", pedido.getTotalPagado()));
      System.out.println("Estado actual: " + pedido.getEstadoPedido());
      System.out.println("Repartidor: " + nombreRepartidor);
      System.out.println();
      Printers.printLine();
      // Mostrar mensaje según el estado del pedido
      Pedido.mostrarMensajeSegunEstado(pedido.getEstadoPedido());
   }

   /**
    * Consulta un pedido específico para un cliente
    * 
    * @param cliente   Cliente que consulta
    * @param pedidos   Lista de pedidos
    * @param codPedido Código del pedido a consultar
    */
   private void consultarPedidoCliente(Cliente cliente, String codPedido) {
      boolean pedidoEncontrado = false;
      for (Pedido pedido : ManejadorPedido.cargarPedidosCliente(cliente)) {
         if (pedido.getCodigoPedido().equalsIgnoreCase(codPedido) &&
               pedido.getCliente().equals(cliente)) {
            pedidoEncontrado = true;
            mostrarInformacionPedido(pedido);
            break;
         }
      }
      if (!pedidoEncontrado) {
         Printers.printWarning("No se encontró ningún pedido con el código: " + codPedido + " para este cliente.");
      }
   }

   /**
    * Muestra la lista de pedidos del cliente con formato similar al repartidor
    */
   private void mostrarListaPedidos() {
      ArrayList<Pedido> pedidos = services.archivos.ManejadorPedido.cargarPedidosCliente(this);
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

   @Override
   public void gestionarPedido(Scanner scanner) {
      Printers.printTitle("CONSULTA DE ESTADO DE PEDIDO");
      mostrarListaPedidos();
      ArrayList<Pedido> pedidos = services.archivos.ManejadorPedido.cargarPedidosCliente(this);
      if (pedidos.isEmpty()) {
         return;
      }
      System.out.print("Ingrese el código del pedido: ");
      String codPedido = scanner.nextLine().trim();

      if (codPedido.isEmpty()) {
         Printers.printError("El código del pedido no puede estar vacío.");
         return;
      }

      consultarPedidoCliente(this, codPedido);
   }

   /**
    * Realiza el proceso de compra para el cliente
    * 
    * @param productos Lista de productos disponibles
    * @param usuarios  Lista de usuarios para buscar repartidores
    * @param pedidos   Lista de pedidos para agregar el nuevo pedido
    * @param scanner   Scanner para leer entrada del usuario
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
    * Le pide al cliente que elija una categoría
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
    * Le pide al cliente que elija un producto de esa categoría
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
    * Le pide al cliente cuántos quiere comprar
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
    * Le pregunta al cliente si confirma la compra
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
    * Hace todo el proceso final: asigna repartidor, crea pedido y actualiza stock
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
      Pedido nuevoPedido = new Pedido(this, repartidorElegido, producto, cantidad, total);
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

   @Override
   public boolean equals(Object obj) {
      return super.equals(obj);
   }

}
