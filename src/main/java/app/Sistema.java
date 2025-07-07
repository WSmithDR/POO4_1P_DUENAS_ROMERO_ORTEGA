package app;

import model.Pedido;
import model.Roles.Cliente;
import model.Roles.Repartidor;
import model.Roles.Usuario;

import java.util.ArrayList;
import java.util.Scanner;

import services.archivos.ManejadorPedido;
import services.archivos.ManejadorProducto;
import services.archivos.ManejadorUsuario;
import services.email.ManejadorEmail;
import model.Producto;
import model.Enums.EstadoPedido;
import model.Enums.Rol;

public class Sistema {
    private static ArrayList<Usuario> usuarios = new ArrayList<>();
    private static ArrayList<Producto> productos = new ArrayList<>();
    private static ArrayList<Pedido> pedidos = new ArrayList<>();

    /**
     * Método principal que inicia el sistema
     * Carga los usuarios, productos y pedidos, luego muestra la pantalla de iniciar sesión
     */
    public static void iniciar(Scanner scanner) {
        usuarios = ManejadorUsuario.cargarUsuarios(usuarios);
        productos = ManejadorProducto.cargarProductos();
        pedidos = ManejadorPedido.cargarPedidos(usuarios, productos);
        iniciarSesion(scanner);
    }

    /**
     * Maneja el inicio de sesión del usuario
     * Pide usuario y contraseña hasta que sean correctos
     */
    private static void iniciarSesion(Scanner scanner) {
        boolean sesionIniciada = false;
        
        while (!sesionIniciada) {
            System.out.println("===== INICIO DE SESIÓN =====");
            System.out.print("Usuario: ");
            String userInput = scanner.nextLine();
            System.out.print("Contraseña: ");
            String passInput = scanner.nextLine();

            Usuario usuarioAutenticado = autenticarUsuario(userInput, passInput);
            
            if (usuarioAutenticado != null) {
                sesionIniciada = procesarAutenticacion(usuarioAutenticado, scanner);
            } else {
                System.out.println("Usuario o contraseña incorrectos. Intente nuevamente.");
                System.out.println();
            }
        }
    }

    /**
     * Busca si existe un usuario con esas credenciales
     */
    private static Usuario autenticarUsuario(String userInput, String passInput) {
        for (Usuario u : usuarios) {
            if (u.getUser_name().equals(userInput) && u.getContrasenia().equals(passInput)) {
                return u;
            }
        }
        return null;
    }

    /**
     * Procesa el inicio de sesión según si es cliente o repartidor
     */
    private static boolean procesarAutenticacion(Usuario usuario, Scanner scanner) {
        System.out.println("Usuario autenticado correctamente.");

        if (usuario instanceof Cliente cliente) {
            return autenticarCliente(cliente, scanner);
        } else if (usuario instanceof Repartidor repartidor) {
            return autenticarRepartidor(repartidor, scanner);
        }
        
        return false;
    }

    /**
     * Verifica que el cliente sea cliente
     */
    private static boolean autenticarCliente(Cliente cliente, Scanner scanner) {
        System.out.println(String.format("Rol detectado: %s", Rol.CLIENTE));
        System.out.println("Bienvenido, " + cliente.getNombre() + " " + cliente.getApellido());
        System.out.println("Celular registrado: " + cliente.getNumeroCelular());
        System.out.print("¿Este número de celular es correcto? (S/N): ");
        
        
        String verif = scanner.nextLine();
        if (verif.equalsIgnoreCase("S")) {
            System.out.println("\nIdentidad confirmada.");
            mostrarMenu(cliente, scanner);
            return true;
        } else {
            System.out.println("Verificación fallida. Cerrando sesión.");
            return true;
        }
    }

    /**
     * Verifica que el repartidor sea repartidor
     */
    private static boolean autenticarRepartidor(Repartidor repartidor, Scanner scanner) {
        System.out.println("Rol detectado: REPARTIDOR");
        System.out.println("Bienvenido, " + repartidor.getNombre() + " " + repartidor.getApellido());
        System.out.println("Empresa asignada: " + repartidor.getNombreEmpresa());
        System.out.print("¿Esta empresa es correcta? (S/N): ");
        
        String verif = scanner.nextLine();
        if (verif.equalsIgnoreCase("S")) {
            mostrarMenu(repartidor, scanner);
            return true;
        } else {
            System.out.println("Verificación fallida");
            System.out.println("Por motivos de seguridad se cerrando sesión.");
            System.out.println("\nSaliendo del sistema...");
            return true;
        }
    }

    /**
     * Muestra el menú del cliente
     */
    private static void mostrarMenu(Cliente cliente, Scanner scanner) {
        mostrarMenuGenerico("Cliente", scanner, opcion -> {
            switch (opcion) {
                case "1":
                    cliente.realizarCompra(productos, usuarios, pedidos, scanner);
                    break;
                case "2":
                    cliente.gestionarPedido(pedidos, scanner);
                    break;
                default:
                    return false;
            }
            return true;
        });
    }

    /**
     * Muestra el menú del repartidor
     */
    private static void mostrarMenu(Repartidor repartidor, Scanner scanner) {
        mostrarMenuGenerico("Repartidor", scanner, opcion -> {
            switch (opcion) {
                case "1":
                    repartidor.gestionarPedido(pedidos, scanner);
                    break;
                case "2":
                    repartidor.consultarPedidosAsignados(pedidos);
                    break;
                default:
                    return false;
            }
            return true;
        });
    }

    /**
     * Este método muestra el menú para cualquier tipo de usuario
     */
    private static void mostrarMenuGenerico(String tipoUsuario, Scanner scanner, MenuHandler handler) {
        boolean continuar = true;

        while (continuar) {
            System.out.println("\n=== Menú " + tipoUsuario + " ===");
            
            if (tipoUsuario.equals("Cliente")) {
                System.out.println("1. Comprar");
                System.out.println("2. Gestionar pedido");
            } else {
                System.out.println("1. Gestionar pedido");
                System.out.println("2. Consultar pedidos asignados");
            }
            
            System.out.println("3. Salir");
            System.out.print("Seleccione una opción: ");

            String opcion = scanner.nextLine();

            if (opcion.equals("3")) {
                System.out.println("Cerrando sesión...");
                continuar = false;
            } else if (!handler.handleOption(opcion)) {
                System.out.println("Opción no válida. Intente nuevamente.");
            }
        }
    }

    /**
     * Esta interfaz me ayuda a manejar las opciones del menú
     */
    @FunctionalInterface
    private interface MenuHandler {
        boolean handleOption(String opcion);
    }

    /**
     * Le manda un email al cliente cuando hace un pedido
     */
    public static void notificar(
            Cliente cliente,
            Pedido pedidoRealizado,
            ManejadorEmail manejadorEmail) {
        String asunto = "Pedido realizado";
        String cuerpo = String.format(
                "El cliente %s %s ha realizado un pedido con código %s el día %s.\n\n" +
                        "Producto: %s\n" +
                        "Cantidad: %d\n" +
                        "Valor pagado: $%.2f\n" +
                        "Estado inicial: %s\n\n" +
                        "Gracias por su compra. Recibirá actualizaciones del estado de su pedido por este medio.",
                cliente.getNombre(),
                cliente.getApellido(),
                pedidoRealizado.getCodigoPedido(),
                pedidoRealizado.getFechaPedido(),
                pedidoRealizado.getProducto().getCodigo(),
                pedidoRealizado.getCantidadProducto(),
                pedidoRealizado.getTotalPagado(),
                pedidoRealizado.getEstadoPedido());
        manejadorEmail.enviarCorreo(cliente.getCorreo(), asunto, cuerpo);
    }

    /**
     * Le avisa al repartidor que tiene un nuevo pedido
     */
    public static void notificar(
            Repartidor repartidor,
            Pedido pedidoAsignado,
            ManejadorEmail manejadorEmail) {
        String asunto = "Nuevo pedido asignado";
        String cuerpo = String.format(
                "Estimado/a %s %s,\n\n" +
                        "Se le ha asignado un nuevo pedido con los siguientes detalles:\n\n" +
                        "Código del pedido: %s\n" +
                        "Fecha del pedido: %s\n" +
                        "Cliente: %s %s\n" +
                        "Estado actual: %s\n\n" +
                        "Por favor, prepare la logística necesaria para la entrega.\n\n" +
                        "Gracias por su trabajo.",
                repartidor.getNombre(),
                repartidor.getApellido(),
                pedidoAsignado.getCodigoPedido(),
                pedidoAsignado.getFechaPedido(),
                pedidoAsignado.getCliente().getNombre(),
                pedidoAsignado.getCliente().getApellido(),
                pedidoAsignado.getEstadoPedido());
        manejadorEmail.enviarCorreo(repartidor.getCorreo(), asunto, cuerpo);
    }

    /**
     * Le avisa al cliente cuando cambia el estado de su pedido
     */
    public static void notificar(
            Cliente cliente,
            Pedido pedido,
            EstadoPedido nuevoEstado,
            ManejadorEmail manejadorEmail) {
        String asunto = "Actualización del estado de su pedido";
        String cuerpo = String.format(
                "Estimado/a %s %s,\n\n" +
                        "Le informamos que el estado de su pedido con código %s ha cambiado a: %s.\n\n" +
                        "Fecha del pedido: %s\n" +
                        "Producto: %s\n" +
                        "Repartidor asignado: %s\n\n" +
                        "Gracias por confiar en nosotros.",
                cliente.getNombre(), cliente.getApellido(),
                pedido.getCodigoPedido(), nuevoEstado,
                pedido.getFechaPedido(),
                pedido.getProducto().getCodigo(),
                pedido.getRepartidor().getCodigoUnico());

        manejadorEmail.enviarCorreo(cliente.getCorreo(), asunto, cuerpo);
    }
}
