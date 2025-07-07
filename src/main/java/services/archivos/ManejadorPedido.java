package services.archivos;

import model.Pedido;
import model.Producto;
import model.Roles.Cliente;
import model.Roles.Repartidor;
import model.Roles.Usuario;
import persistence.ManejoArchivos;
import utils.ManejoFechas;
import model.Enums.EstadoPedido;
import services.email.ManejadorEmail;
import app.Sistema;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * Clase que maneja la gestión de pedidos
 */
public class ManejadorPedido {
    private static final String PEDIDOS_FILE = "resources/Pedidos.txt";

    /**
     * Asigna un repartidor aleatorio de la lista
     * @param repartidores Lista de repartidores disponibles
     * @return Repartidor seleccionado aleatoriamente
     */
    public static Repartidor asignarRepartidorAleatorio(ArrayList<Repartidor> repartidores) {
        if (repartidores.isEmpty()) {
            return null;
        }
        
        Random random = new Random();
        int indiceAleatorio = random.nextInt(repartidores.size());
        return repartidores.get(indiceAleatorio);
    }

    /**
     * Guarda un pedido en el archivo
     * @param pedido Pedido a guardar
     */
    public static void guardarPedido(Pedido pedido) {
        try {
            String lineaPedido = pedido.toFileFormat();
            ManejoArchivos.EscribirArchivo(PEDIDOS_FILE, lineaPedido);
        } catch (Exception e) {
            System.out.println("Error guardando pedido: " + e.getMessage());
        }
    }

    /**
     * Carga todos los pedidos desde el archivo
     * @param usuarios Lista de usuarios para buscar clientes y repartidores
     * @param productos Lista de productos para buscar productos
     * @return ArrayList con todos los pedidos
     */
    public static ArrayList<Pedido> cargarPedidos(ArrayList<Usuario> usuarios, ArrayList<Producto> productos) {
        ArrayList<Pedido> pedidos = new ArrayList<>();
        
        try {
            ArrayList<String> lineas = ManejoArchivos.LeeFichero(PEDIDOS_FILE);
            
            // Saltar la primera línea (encabezado)
            for (int i = 1; i < lineas.size(); i++) {
                String linea = lineas.get(i);
                String[] partes = linea.split("\\|");
                
                // Buscar cliente y repartidor por cédula
                Cliente cliente = buscarClientePorCedula(usuarios, partes[1]);
                Repartidor repartidor = buscarRepartidorPorCodigoUnico(usuarios, partes[2]);
                Producto producto = buscarProductoPorCodigo(productos, partes[3]);
                
                if (cliente != null && repartidor != null && producto != null) {
                    int cantidad = Integer.parseInt(partes[4]);
                    double valorPagado = Double.parseDouble(partes[5]);
                    
                    Pedido pedido = new Pedido(cliente, repartidor, producto, cantidad, valorPagado);
                    pedido.setCodigoPedido(partes[0]); // Usar el código original del archivo
                    pedidos.add(pedido);
                }
            }
        } catch (Exception e) {
            System.out.println("Error cargando pedidos: " + e.getMessage());
        }
        
        return pedidos;
    }

    /**
     * Busca un cliente por cédula
     * @param usuarios Lista de usuarios
     * @param cedula Cédula del cliente
     * @return Cliente encontrado o null
     */
    private static Cliente buscarClientePorCedula(ArrayList<Usuario> usuarios, String cedula) {
        for (Usuario usuario : usuarios) {
            if (usuario instanceof Cliente && usuario.getCedula().equals(cedula)) {
                return (Cliente) usuario;
            }
        }
        return null;
    }

    /**
     * Busca un repartidor por código único
     * @param usuarios Lista de usuarios
     * @param codigoUnico Código único del repartidor
     * @return Repartidor encontrado o null
     */
    private static Repartidor buscarRepartidorPorCodigoUnico(ArrayList<Usuario> usuarios, String codigoUnico) {
        for (Usuario usuario : usuarios) {
            if (usuario instanceof Repartidor && usuario.getCodigoUnico().equals(codigoUnico)) {
                return (Repartidor) usuario;
            }
        }
        return null;
    }

    /**
     * Busca un producto por código
     * @param productos Lista de productos
     * @param codigo Código del producto
     * @return Producto encontrado o null
     */
    private static Producto buscarProductoPorCodigo(ArrayList<Producto> productos, String codigo) {
        for (Producto producto : productos) {
            if (producto.getCodigo().equals(codigo)) {
                return producto;
            }
        }
        return null;
    }
    
    /**
     * Obtiene el nombre de un producto por su código
     * @param codigoProducto Código del producto
     * @return Nombre del producto o "Producto no encontrado" si no existe
     */
    private static String obtenerNombreProducto(String codigoProducto) {
        String archivoProductos = "resources/Productos.txt";
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
                continue;
            }

            // Verificar que el código coincida y retornar la posición 2 del array (nombre)
            if (datosP[0].equalsIgnoreCase(codigoProducto)) {
                return datosP[1];
            }
        }
        return "Producto no encontrado";
    }
    
    /**
     * Obtiene el nombre de un repartidor por su cédula
     * @param codUnico codigo unico del reapartidor
     * @return Nombre del repartidor o "Repartidor no encontrado" si no existe
     */
    private static String obtenerNombreRepartidor(String codUnico) {
        String archivoUsuarios = "resources/Usuarios.txt";
        ArrayList<String> lineas = ManejoArchivos.LeeFichero(archivoUsuarios);

        if (lineas == null || lineas.isEmpty()) {
            return "Error al leer archivo de usuarios";
        } else {
            for (String linea : lineas) {
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
        }
        return "Repartidor no encontrado";
    }
    
    /**
     * Método general para gestionar pedidos según el tipo de usuario
     * @param usuario Usuario que gestiona los pedidos (Cliente o Repartidor)
     * @param pedidos Lista de pedidos disponibles
     * @param scanner Scanner para leer entrada del usuario
     */
    public static void gestionarPedido(Usuario usuario, ArrayList<Pedido> pedidos, Scanner scanner) {
        if (usuario instanceof Cliente) {
            gestionarPedidoCliente((Cliente) usuario, pedidos, scanner);
        } else if (usuario instanceof Repartidor) {
            gestionarPedidoRepartidor((Repartidor) usuario, pedidos, scanner);
        } else {
            System.out.println("Tipo de usuario no soportado para gestión de pedidos.");
        }
    }
    
    /**
     * Gestiona pedidos específicamente para clientes
     * @param cliente Cliente que gestiona sus pedidos
     * @param pedidos Lista de todos los pedidos
     * @param scanner Scanner para leer entrada del usuario
     */
    private static void gestionarPedidoCliente(Cliente cliente, ArrayList<Pedido> pedidos, Scanner scanner) {
        System.out.println("===== CONSULTA DE ESTADO DE PEDIDO =====");
        System.out.println("Ingrese el código del pedido:");
        String codPedido = scanner.nextLine().trim();

        if (codPedido.isEmpty()) {
            System.out.println("El código del pedido no puede estar vacío.");
            return;
        }

        consultarPedidoCliente(cliente, pedidos, codPedido);
    }
    
    /**
     * Gestiona pedidos específicamente para repartidores
     * @param repartidor Repartidor que gestiona sus pedidos
     * @param pedidos Lista de todos los pedidos
     * @param scanner Scanner para leer entrada del usuario
     */
    private static void gestionarPedidoRepartidor(Repartidor repartidor, ArrayList<Pedido> pedidos, Scanner scanner) {
        System.out.println("\n===== GESTIÓN DE PEDIDOS - REPARTIDOR =====");
        System.out.println("Repartidor: " + repartidor.getNombre() + " " + repartidor.getApellido());
        System.out.println("Empresa: " + repartidor.getNombreEmpresa());
        
        consultarPedidosAsignados(repartidor, pedidos);
        
        // Opción para cambiar estado de pedidos
        System.out.println("\n¿Desea cambiar el estado de algún pedido? (s/n): ");
        String respuesta = scanner.nextLine().toLowerCase();
        if (respuesta.equals("s") || respuesta.equals("si")) {
            cambiarEstadoPedido(repartidor, pedidos, scanner);
        }
    }
    
    /**
     * Consulta un pedido específico para un cliente
     * @param cliente Cliente que consulta
     * @param pedidos Lista de pedidos
     * @param codPedido Código del pedido a consultar
     */
    private static void consultarPedidoCliente(Cliente cliente, ArrayList<Pedido> pedidos, String codPedido) {
        boolean pedidoEncontrado = false;

        for (Pedido pedido : pedidos) {
            if (pedido.getCodigoPedido().equalsIgnoreCase(codPedido) && 
                pedido.getCliente().getCedula().equals(cliente.getCedula())) {
                pedidoEncontrado = true;
                mostrarInformacionPedido(pedido);
                break;
            }
        }

        if (!pedidoEncontrado) {
            System.out.println("No se encontró ningún pedido con el código: " + codPedido + " para este cliente.");
        }
    }
    
    /**
     * Consulta los pedidos asignados a un repartidor
     * @param repartidor Repartidor que consulta
     * @param pedidos Lista de todos los pedidos
     */
    public static void consultarPedidosAsignados(Repartidor repartidor, ArrayList<Pedido> pedidos) {
        System.out.println("\n===== PEDIDOS ASIGNADOS =====");
        System.out.println("Buscando pedidos asignados no entregados...\n");
        
        ArrayList<Pedido> pedidosAsignados = new ArrayList<>();
        
        // Buscar pedidos asignados a este repartidor que no estén entregados
        for (Pedido pedido : pedidos) {
            if (pedido.getRepartidor().getCodigoUnico().equals(repartidor.getCodigoUnico()) && 
                !(pedido.getEstadoPedido() == EstadoPedido.ENTREGADO) &&
                !(pedido.getEstadoPedido().equals(EstadoPedido.CANCELADO))) {
                pedidosAsignados.add(pedido);
            }
        }
        
        if (pedidosAsignados.isEmpty()) {
            System.out.println("No tienes pedidos asignados pendientes.");
            return;
        }
        
        System.out.println("Pedidos encontrados:\n");
        
        // Mostrar cada pedido
        for (int i = 0; i < pedidosAsignados.size(); i++) {
            Pedido pedido = pedidosAsignados.get(i);
            System.out.println((i + 1) + ". Código: " + pedido.getCodigoPedido());
            System.out.println("   Fecha del pedido: " + ManejoFechas.setFechaSimple(pedido.getFechaPedido()));
            System.out.println("   Estado actual: " + pedido.getEstadoPedido());
            System.out.println();
        }
        
        System.out.println("Total de pedidos pendientes: " + pedidosAsignados.size());
        System.out.println("Recuerde que solo puede gestionar los pedidos que se encuentren EN PREPARACIÓN o EN RUTA.");
    }
    
    /**
     * Muestra la información detallada de un pedido
     * @param pedido Pedido a mostrar
     */
    private static void mostrarInformacionPedido(Pedido pedido) {
        // Obtener nombre del producto
        String nombreProducto = obtenerNombreProducto(pedido.getProducto().getCodigo());
        
        // Obtener nombre del repartidor
        String nombreRepartidor = obtenerNombreRepartidor(pedido.getRepartidor().getCodigoUnico());
        
        System.out.println("Fecha del pedido: " + ManejoFechas.setFechaSimple(pedido.getFechaPedido()));
        System.out.println("Producto comprado: " + nombreProducto + " (Código: " + pedido.getProducto().getCodigo() + ") Cantidad: " + pedido.getCantidadProducto());
        System.out.println("Valor pagado: $" + String.format("%.2f", pedido.getTotalPagado()));
        System.out.println("Estado actual: " + pedido.getEstadoPedido());
        System.out.println("Repartidor: " + nombreRepartidor);
        System.out.println();

        // Mostrar mensaje según el estado del pedido
        mostrarMensajeSegunEstado(pedido.getEstadoPedido());
    }
    
    /**
     * Muestra un mensaje personalizado según el estado del pedido
     * @param estado Estado actual del pedido
     */
    private static void mostrarMensajeSegunEstado(EstadoPedido estado) {
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
     * Permite al repartidor cambiar el estado de un pedido
     * @param repartidor Repartidor que cambia el estado
     * @param pedidos Lista de pedidos
     * @param scanner Scanner para leer entrada
     */
    private static void cambiarEstadoPedido(Repartidor repartidor, ArrayList<Pedido> pedidos, Scanner scanner) {
        System.out.println("===== GESTIONAR ESTADO DE PEDIDO =====");
        System.out.print("Ingrese el código del pedido que desea gestionar: ");
        String codigoPedido = scanner.nextLine().trim();
        
        // Buscar el pedido
        Pedido pedidoAModificar = null;
        for (Pedido pedido : pedidos) {
            if (pedido.getCodigoPedido().equals(codigoPedido) && 
                pedido.getRepartidor().getCodigoUnico().equals(repartidor.getCodigoUnico())) {
                pedidoAModificar = pedido;
                break;
            }
        }
        
        if (pedidoAModificar == null) {
            System.out.println("Pedido no encontrado o no asignado a este repartidor.");
            return;
        }
        
        System.out.println("\nPedido encontrado:");
        System.out.println("Fecha del pedido: " + ManejoFechas.setFechaSimple(pedidoAModificar.getFechaPedido()) + 
                          " \nCódigo del producto: " + pedidoAModificar.getProducto().getCodigo() + 
                          " \nEstado actual: " + pedidoAModificar.getEstadoPedido());
        
        // Mostrar opciones según el estado actual
        mostrarOpcionesEstado(pedidoAModificar, scanner);
    }
    
    /**
     * Muestra las opciones de estado disponibles según el estado actual
     * @param pedido Pedido a modificar
     * @param scanner Scanner para leer entrada
     */
    private static void mostrarOpcionesEstado(Pedido pedido, Scanner scanner) {
        EstadoPedido estadoActual = pedido.getEstadoPedido();
        ManejadorEmail manejadorEmail = new ManejadorEmail();
        if (estadoActual == EstadoPedido.EN_PREPARACION) {
            System.out.println("\nSeleccione el nuevo estado:");
            System.out.println("1. EN CAMINO");
            System.out.println("2. ENTREGADO");
            System.out.print("Opción: ");
            int opcion = Integer.parseInt(scanner.nextLine());
            if (opcion == 1) {
                pedido.setEstadoPedido(EstadoPedido.EN_CAMINO);
                System.out.println("Estado actualizado correctamente a EN CAMINO.");
                Sistema.notificar(pedido.getCliente(), pedido, EstadoPedido.EN_CAMINO, manejadorEmail);
            } else if (opcion == 2) {
                System.out.println("\nError: No puede cambiar directamente de EN PREPARACIÓN a ENTREGADO. Debe cambiar primero a EN CAMINO.");
                mostrarOpcionesEstado(pedido, scanner); // Mostrar opciones nuevamente
            } else {
                System.out.println("Opción inválida.");
            }
        } else if (estadoActual == EstadoPedido.EN_CAMINO) {
            System.out.println("\nSeleccione el nuevo estado:");
            System.out.println("1. ENTREGADO");
            System.out.print("Opción: ");
            int opcion = Integer.parseInt(scanner.nextLine());
            if (opcion == 1) {
                pedido.setEstadoPedido(EstadoPedido.ENTREGADO);
                System.out.println("Estado actualizado correctamente a ENTREGADO.");
                Sistema.notificar(pedido.getCliente(), pedido, EstadoPedido.ENTREGADO, manejadorEmail);
            } else {
                System.out.println("Opción inválida.");
            }
        } else {
            System.out.println("No se pueden realizar cambios en el estado actual: " + estadoActual);
        }
    }
} 