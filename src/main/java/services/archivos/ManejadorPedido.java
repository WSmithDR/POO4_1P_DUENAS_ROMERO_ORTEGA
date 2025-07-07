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
     * Asigna un repartidor al azar de la lista
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
     * Verifica si el archivo existe
     */
    private static boolean archivoExiste(String nombreArchivo) {
        try {
            java.io.File archivo = new java.io.File(nombreArchivo);
            return archivo.exists() && archivo.length() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Convierte un string de estado a su enum correspondiente
     */
    private static EstadoPedido convertirStringAEstado(String estadoString) {
        for (EstadoPedido estado : EstadoPedido.values()) {
            if (estado.getDescripcion().equals(estadoString)) {
                return estado;
            }
        }
        // Si no encuentra coincidencia, retornar EN_PREPARACION por defecto
        return EstadoPedido.EN_PREPARACION;
    }

    /**
     * Guarda un pedido en el archivo
     */
    public static void guardarPedido(Pedido pedido) {
        try {
            boolean archivoExiste = archivoExiste(PEDIDOS_FILE);
            boolean necesitaEncabezado = false;
            
            if (!archivoExiste) {
                // Si el archivo no existe, necesita encabezado
                necesitaEncabezado = true;
            } else {
                // Si existe, verificar si tiene encabezado
                try {
                    ArrayList<String> lineasExistentes = ManejoArchivos.LeeFichero(PEDIDOS_FILE);
                    boolean archivoVacio = lineasExistentes == null || lineasExistentes.isEmpty();
                    necesitaEncabezado = archivoVacio || !lineasExistentes.get(0).contains("codigoPedido");
                } catch (Exception e) {
                    // Si hay error al leer, asumir que necesita encabezado
                    necesitaEncabezado = true;
                }
            }
            
            // Si necesita encabezado, crear el encabezado primero
            if (necesitaEncabezado) {
                String encabezado = "codigoPedido|fecha|codigoProducto|cantidad|total|estado|codigoRepartidor";
                ManejoArchivos.EscribirArchivo(PEDIDOS_FILE, encabezado);
            }
            
            String lineaPedido = pedido.toFileFormat();
            ManejoArchivos.EscribirArchivo(PEDIDOS_FILE, lineaPedido);
        } catch (Exception e) {
            System.out.println("Error guardando pedido: " + e.getMessage());
        }
    }

    /**
     * Carga todos los pedidos desde el archivo
     * Nota: Como el archivo no incluye la cédula del cliente, solo muestra la información
     * de los pedidos sin crear objetos completos
     */
    public static ArrayList<Pedido> cargarPedidos(ArrayList<Usuario> usuarios, ArrayList<Producto> productos) {
        ArrayList<Pedido> pedidos = new ArrayList<>();
        
        try {
            // Verificar si el archivo existe antes de intentar leerlo
            if (!archivoExiste(PEDIDOS_FILE)) {
                System.out.println("Archivo de pedidos no encontrado. Se creará automáticamente cuando se registre el primer pedido.");
                return pedidos;
            }
            
            ArrayList<String> lineas = ManejoArchivos.LeeFichero(PEDIDOS_FILE);
            
            // Verificar que el archivo tenga contenido
            if (lineas == null || lineas.isEmpty()) {
                return pedidos;
            }
            
            // Saltar la primera línea (encabezado)
            for (int i = 1; i < lineas.size(); i++) {
                String linea = lineas.get(i);
                String[] partes = linea.split("\\|");
                
                // Verificar que tenga suficientes campos
                if (partes.length < 7) {
                    continue;
                }
                
                // El formato es: código_pedido|fecha|código_producto|cantidad|total|estado|código_repartidor
                // Para cargar pedidos completos necesitamos la cédula del cliente
                // Por ahora, vamos a crear pedidos con información básica
                Repartidor repartidor = buscarRepartidorPorCodigoUnico(usuarios, partes[6]);
                Producto producto = buscarProductoPorCodigo(productos, partes[2]);
                
                if (repartidor != null && producto != null) {
                    // Solo mostrar información del pedido sin crear objeto completo
                    // ya que no tenemos el cliente real
                    System.out.println("Pedido encontrado en archivo:");
                    System.out.println("  Código: " + partes[0]);
                    System.out.println("  Fecha: " + partes[1]);
                    System.out.println("  Producto: " + producto.getNombre());
                    System.out.println("  Cantidad: " + partes[3]);
                    System.out.println("  Total: $" + partes[4]);
                    System.out.println("  Estado: " + partes[5]);
                    System.out.println("  Repartidor: " + repartidor.getNombre() + " " + repartidor.getApellido());
                    System.out.println();
                }
            }
        } catch (Exception e) {
            System.out.println("Error cargando pedidos: " + e.getMessage());
        }
        
        return pedidos;
    }

    /**
     * Busca un cliente por cédula
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
     * Obtiene el nombre de un repartidor por su código único
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
     * Maneja la gestión de pedidos según si es cliente o repartidor
     */
    public static void gestionarPedido(Usuario usuario, ArrayList<Pedido> pedidos, Scanner scanner) {
        if (usuario instanceof Cliente) {
            gestionarPedidoCliente((Cliente) usuario, pedidos, scanner);
        } else if (usuario instanceof Repartidor) {
            gestionarPedidoRepartidor((Repartidor) usuario, pedidos, scanner);
        }
    }

    /**
     * Maneja la gestión de pedidos para clientes
     */
    private static void gestionarPedidoCliente(Cliente cliente, ArrayList<Pedido> pedidos, Scanner scanner) {
        System.out.println("===== CONSULTA DE ESTADO DE PEDIDO =====");
        System.out.print("Ingrese el código del pedido:");
        String codPedido = scanner.nextLine().trim();

        if (codPedido.isEmpty()) {
            System.out.println("El código del pedido no puede estar vacío.");
            return;
        }

        consultarPedidoCliente(cliente, pedidos, codPedido);
    }
    
    /**
     * Maneja la gestión de pedidos para repartidores
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
     * Busca un pedido específico para un cliente
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
     * Muestra los pedidos que tiene asignados un repartidor
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
     * Muestra toda la información de un pedido
     */
    private static void mostrarInformacionPedido(Pedido pedido) {
        // Obtener nombre del producto
        String nombreProducto = obtenerNombreProducto(pedido.getProducto().getCodigo());
        
        // Obtener nombre del repartidor
        String nombreRepartidor = obtenerNombreRepartidor(pedido.getRepartidor().getCodigoUnico());
        
        System.out.println("Fecha del pedido: " + ManejoFechas.setFechaSimple(pedido.getFechaPedido()));
        System.out.println("Producto comprado: " + nombreProducto + " (Código: " + pedido.getProducto().getCodigo() + ")");
        System.out.println("Cantidad: " + pedido.getCantidadProducto());
        System.out.println("Valor pagado: $" + String.format("%.2f", pedido.getTotalPagado()));
        System.out.println("Estado actual: " + pedido.getEstadoPedido());
        System.out.println("Repartidor: " + nombreRepartidor);
        System.out.println();

        // Mostrar mensaje según el estado del pedido
        mostrarMensajeSegunEstado(pedido.getEstadoPedido());
    }
    
    /**
     * Le dice al cliente qué significa cada estado
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
     * Le permite al repartidor cambiar el estado de un pedido
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
     * Le muestra al repartidor qué estados puede cambiar
     */
    private static void mostrarOpcionesEstado(Pedido pedido, Scanner scanner) {
        EstadoPedido estadoActual = pedido.getEstadoPedido();
        ManejadorEmail manejadorEmail = new ManejadorEmail();
        
        if (estadoActual == EstadoPedido.EN_PREPARACION) {
            procesarCambioEstadoPreparacion(pedido, scanner, manejadorEmail);
        } else if (estadoActual == EstadoPedido.EN_CAMINO) {
            procesarCambioEstadoEnCamino(pedido, scanner, manejadorEmail);
        } else {
            System.out.println("No se pueden realizar cambios en el estado actual: " + estadoActual);
        }
    }

    /**
     * Maneja el cambio cuando el pedido está en preparación
     */
    private static void procesarCambioEstadoPreparacion(Pedido pedido, Scanner scanner, ManejadorEmail manejadorEmail) {
        System.out.println("\nSeleccione el nuevo estado:");
        System.out.println("1. EN CAMINO");
        System.out.println("2. ENTREGADO");
        System.out.print("Opción: ");
        
        int opcion = Integer.parseInt(scanner.nextLine());
        if (opcion == 1) {
            cambiarEstadoPedido(pedido, EstadoPedido.EN_CAMINO, "EN CAMINO", manejadorEmail);
        } else if (opcion == 2) {
            System.out.println("\nError: No puede cambiar directamente de EN PREPARACIÓN a ENTREGADO. Debe cambiar primero a EN CAMINO.");
            mostrarOpcionesEstado(pedido, scanner);
        } else {
            System.out.println("Opción inválida.");
        }
    }

    /**
     * Maneja el cambio cuando el pedido está en camino
     */
    private static void procesarCambioEstadoEnCamino(Pedido pedido, Scanner scanner, ManejadorEmail manejadorEmail) {
        System.out.println("\nSeleccione el nuevo estado:");
        System.out.println("1. ENTREGADO");
        System.out.print("Opción: ");
        
        int opcion = Integer.parseInt(scanner.nextLine());
        if (opcion == 1) {
            cambiarEstadoPedido(pedido, EstadoPedido.ENTREGADO, "ENTREGADO", manejadorEmail);
        } else {
            System.out.println("Opción inválida.");
        }
    }

    /**
     * Cambia el estado y le avisa al cliente
     */
    private static void cambiarEstadoPedido(Pedido pedido, EstadoPedido nuevoEstado, String nombreEstado, ManejadorEmail manejadorEmail) {
        pedido.setEstadoPedido(nuevoEstado);
        System.out.println("Estado actualizado correctamente a " + nombreEstado + ".");
        Sistema.notificar(pedido.getCliente(), pedido, nuevoEstado, manejadorEmail);
    }
} 