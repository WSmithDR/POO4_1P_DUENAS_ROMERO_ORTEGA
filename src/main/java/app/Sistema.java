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
     * Inicia el sistema: carga usuarios, productos
     * y pedidos, y muestra la pantalla de inicio de sesión.
     * 
     * @param scanner Scanner para leer la entrada del usuario desde la consola
     */
    public static void iniciar(Scanner scanner) {
        usuarios = ManejadorUsuario.cargarUsuarios(usuarios);
        productos = ManejadorProducto.cargarProductos();
        pedidos = ManejadorPedido.cargarPedidos(usuarios, productos);

        boolean salir = false;
        while (!salir) {
            salir = iniciarSesion(scanner); // Retorna true solo si el usuario elige salir del sistema
        }
        System.out.println("¡Hasta luego!");
    }

    /**
     * Maneja el proceso de inicio de sesión del usuario,
     * solicitando credenciales
     * y autenticando según el rol.
     * 
     * @param scanner Scanner para leer la entrada del usuario
     */
        private static boolean iniciarSesion(Scanner scanner) {
        while (true) {
            System.out.println("\n1. Iniciar sesión");
            System.out.println("2. Salir del sistema");
            System.out.print("Seleccione una opción: ");
            String opcion = scanner.nextLine();

            if (opcion.equals("2")) {
                return true; // Salir del sistema
            } else if (opcion.equals("1")) {
                System.out.println("\n===== INICIO DE SESIÓN =====");
                break; // Continuar con el login
            } else {
                System.out.println("Opción no válida. Intente nuevamente.");
            }
        }

    boolean sesionIniciada = false;
    while (!sesionIniciada) {
        System.out.print("Usuario: ");
            String userInput = scanner.nextLine();
            System.out.print("Contraseña: ");
            String passInput = scanner.nextLine();

            boolean usuarioEncontrado = false;

            for (Usuario u : usuarios) {
                if (u.getUser_name().equals(userInput) && u.getContrasenia().equals(passInput)) {
                    usuarioEncontrado = true;
                    System.out.println("\nUsuario autenticado correctamente.");

                    if (u instanceof Cliente c) {
                        System.out.println(String.format("\nRol detectado: %s", Rol.CLIENTE));
                        System.out.println("\nBienvenido, " + c.getNombre() + " " + c.getApellido());
                        System.out.println("Celular registrado: " + c.getNumeroCelular());
                        System.out.print("\n¿Este número de celular es correcto? (S/N): ");
                        String verif = scanner.nextLine();
                        if (verif.equalsIgnoreCase("S")) {
                            System.out.println("\n¡Identidad confirmada!");
                            mostrarMenu(c, scanner);
                            sesionIniciada = true;
                        } else {
                            System.out.println("\nVerificación fallida. Cerrando sesión.");
                            sesionIniciada = true;
                        }
                    } else if (u instanceof Repartidor r) {
                        System.out.println("\nRol detectado: REPARTIDOR");
                        System.out.println("\nBienvenido, " + r.getNombre() + " " + r.getApellido());
                        System.out.println("Empresa asignada: " + r.getNombreEmpresa());
                        System.out.print("\n¿Esta empresa es correcta? (S/N): ");
                        String verif = scanner.nextLine();
                        if (verif.equalsIgnoreCase("S")) {
                            System.out.println("\n¡Identidad confirmada!");
                            mostrarMenu(r, scanner);
                            sesionIniciada = true;
                        } else {
                            System.out.println("\nVerificación fallida");
                            System.out.println("Por motivos de seguridad se cerrará la sesion");
                            System.out.println();
                            System.out.println("\nCerrando sesión...");
                            sesionIniciada = true;
                        }
                    }
                    break;
                }
            }

            if (!usuarioEncontrado) {
                System.out.println("\nUsuario o contraseña incorrectos. Intente nuevamente.");
                System.out.println();
            }
        }
        return false; // No salir del sistema, solo volver al login
    }

    /**
     * Muestra y maneja el menú principal para usuarios con rol de Cliente.
     * Permite al cliente realizar compras y gestionar pedidos.
     * 
     * @param cliente El objeto Cliente autenticado
     * @param scanner Scanner para leer la entrada del usuario
     */
    private static void mostrarMenu(Cliente cliente, Scanner scanner) {
        boolean continuar = true;

        while (continuar) {
            System.out.println("\n=== Menú Cliente ===");
            System.out.println("1. Comprar");
            System.out.println("2. Gestionar pedido");
            System.out.println("3. Cerrar sesión");
            System.out.print("\nSeleccione una opción: ");

            String opcion = scanner.nextLine();

            switch (opcion) {
                case "1":
                    cliente.realizarCompra(productos, usuarios, pedidos, scanner);
                    break;
                case "2":
                    cliente.gestionarPedido(pedidos, scanner);
                    break;
                case "3":
                    System.out.println("\nCerrando sesión...");
                    continuar = false;
                    break;
                default:
                    System.out.println("\nOpción no válida. Intente nuevamente.");
                    break;
            }
        }
    }

    /**
     * Muestra y maneja el menú principal para usuarios con rol de Repartidor.
     * Permite al repartidor gestionar pedidos y consultar asignaciones.
     * 
     * @param repartidor El objeto Repartidor autenticado
     * @param scanner    Scanner para leer la entrada del usuario
     */
    private static void mostrarMenu(Repartidor repartidor, Scanner scanner) {
        boolean continuar = true;

        while (continuar) {
            System.out.println("\n=== Menú Repartidor ===");
            System.out.println("1. Gestionar pedido");
            System.out.println("2. Consultar pedidos asignados");
            System.out.println("3. Cerrar sesión");
            System.out.print("\nSeleccione una opción: ");

            String opcion = scanner.nextLine();

            switch (opcion) {
                case "1":
                    repartidor.gestionarPedido(pedidos, scanner);
                    break;
                case "2":
                    repartidor.consultarPedidosAsignados(pedidos);
                    break;
                case "3":
                    System.out.println("\nCerrando sesión...");
                    continuar = false;
                    break;
                default:
                    System.out.println("\nOpción no válida. Intente nuevamente.");
                    break;
            }
        }
    }

    /**
     * Notifica al cliente cuando este realiza un pedido, enviando un correo
     * electrónico con los detalles del pedido realizado.
     * 
     * @param cliente         El objeto Cliente que realiza el pedido
     * @param pedidoRealizado Contiene la información del pedido que realizó el
     *                        cliente
     * @param manejadorEmail  Instancia de ManejadorEmail para enviar el correo
     */
    public static void notificar(
            Cliente cliente,
            Pedido pedidoRealizado,
            ManejadorEmail manejadorEmail) {
        String asunto = "Pedido realizado";
        String cuerpo = String.format(
                "<html><body>"+"El/La cliente <strong>%s %s</strong> ha realizado un pedido con código <strong>%s</strong> el día <strong>%s</strong>."+
                        "<br><br>"+
                        "Producto: <strong>%s</strong>"+
                        "<br>"+
                        "Cantidad: <strong>%d</strong>"+
                        "<br>"+
                        "Valor pagado: <strong>$%.2f</strong>"+
                        "<br>"+
                        "Estado inicial: <strong>%s</strong>"+
                        "<br><br>"+
                        "Gracias por su compra. Recibirá actualizaciones del estado de su pedido por este medio."+"</body></html>",
                cliente.getNombre(),
                cliente.getApellido(),
                pedidoRealizado.getCodigoPedido(),
                pedidoRealizado.getFechaPedido(),
                pedidoRealizado.getProducto().getCodigo(),
                pedidoRealizado.getCantidadProducto(),
                pedidoRealizado.getTotalPagado(),
                pedidoRealizado.getEstadoPedido());
        System.out.println("Enviando correo al "+Rol.CLIENTE+" "+cliente.getCorreo());
        manejadorEmail.enviarCorreo(cliente.getCorreo(), asunto, cuerpo);
    }

    /**
     * Notifica al repartidor cuando se le asigna un nuevo pedido, enviando un
     * correo electrónico con los detalles del pedido asignado.
     * 
     * @param repartidor     El repartidor al que se le asigna el pedido
     * @param pedidoAsignado El pedido que ha sido asignado
     * @param manejadorEmail Instancia de ManejadorEmail para enviar el correo
     */
    public static void notificar(
            Repartidor repartidor,
            Pedido pedidoAsignado,
            ManejadorEmail manejadorEmail) {
        String asunto = "Nuevo pedido asignado";
        String cuerpo = String.format(
                "<html><body>"+"Estimado/a <strong>%s %s</strong>,"+
                "<br>"+
                        "Se le ha asignado un nuevo pedido con los siguientes detalles:"+
                        "<br><br>"+
                        "Código del pedido: <strong>%s</strong>"+
                        "<br>"+
                        "Fecha del pedido: <strong>%s</strong>"+
                        "<br>"+
                        "Cliente: <strong>%s %s</strong>,"+
                        "<br>"+ 
                        "Estado actual: <strong>%s</strong>"+
                        "<br><br>"+
                        "Por favor, prepare la logística necesaria para la entrega." +
                        "Gracias por su trabajo."+"</body></html>",
                repartidor.getNombre(),
                repartidor.getApellido(),
                pedidoAsignado.getCodigoPedido(),
                pedidoAsignado.getFechaPedido(),
                pedidoAsignado.getCliente().getNombre(),
                pedidoAsignado.getCliente().getApellido(),
                pedidoAsignado.getEstadoPedido());
        System.out.println("Enviando correo al "+Rol.REPARTIDOR+" "+repartidor.getCorreo());
        manejadorEmail.enviarCorreo(repartidor.getCorreo(), asunto, cuerpo);
    }

    /**
     * Notifica al cliente sobre un cambio en el estado de su pedido, enviando un
     * correo electrónico informando el nuevo estado del pedido.
     * 
     * @param cliente        El cliente al que se le notifica
     * @param pedido         El pedido cuyo estado ha cambiado
     * @param nuevoEstado    El nuevo estado del pedido
     * @param manejadorEmail Instancia de ManejadorEmail para enviar el correo
     */
    public static void notificar(
            Cliente cliente,
            Pedido pedido,
            EstadoPedido nuevoEstado,
            ManejadorEmail manejadorEmail) {
        String asunto = "Actualización del estado de su pedido";
        String cuerpo = String.format(
                "<html><body>"+"Estimado/a <strong>%s %s</strong>,"+
                "<br>"+
                        "Le informamos que el estado de su pedido con código <strong>%s</strong> ha cambiado a: <strong>%s</strong>."+
                        "<br><br>"+
                        "Fecha del pedido: <strong>%s</strong>"+
                        "<br>"+
                        "Producto: <strong>%s</strong>"+
                        "<br>"+
                        "Repartidor asignado: <strong>%s</strong>"+
                        "<br>"+
                        "Gracias por confiar en nosotros."+"</body></html>",
                cliente.getNombre(), cliente.getApellido(),
                pedido.getCodigoPedido(), nuevoEstado,
                pedido.getFechaPedido(),
                pedido.getProducto().getCodigo(),
                pedido.getRepartidor().getCodigoUnico());
        System.out.println("Enviando correo al "+Rol.CLIENTE+" "+cliente.getCorreo());
        manejadorEmail.enviarCorreo(cliente.getCorreo(), asunto, cuerpo);
    }
}