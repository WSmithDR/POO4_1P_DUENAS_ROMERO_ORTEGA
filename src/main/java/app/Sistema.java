package app;

import model.Pedido;
import model.Roles.Cliente;
import model.Roles.Repartidor;
import model.Roles.Usuario;

import java.util.ArrayList;
import java.util.Scanner;

import services.archivos.ManejadorUsuario;
import services.email.ManejadorEmail;
import model.Enums.EstadoPedido;

import utils.Printers;
import utils.Time;

public class Sistema {
    private static ArrayList<Usuario> usuarios = new ArrayList<>();
    /**
     * Inicia el sistema: carga usuarios, productos
     * y pedidos, y muestra la pantalla de inicio de sesión.
     * 
     * @param scanner Scanner para leer la entrada del usuario desde la consola
     */
    public static void iniciar(Scanner scanner) {
        usuarios = ManejadorUsuario.cargarUsuarios();

        boolean salir = false;
        while (!salir) {
            Printers.printSeparator();
            Printers.printTitle(String.format(
                "BIENVENIDO/A A \n"+
                "DUENAS_ROMERERO_ORTEGA \n"+
                "DELIVERY SYSTEM"
                ));
            Printers.printSeparator();
            System.out.println("1. Iniciar sesión");
            System.out.println("2. Salir del sistema");
            System.out.print("Seleccione una opción: ");
            String opcion = scanner.nextLine();
            if (opcion.equals("2")) {
                salir = true;
            } else if (opcion.equals("1")) {
                salir = iniciarSesion(scanner); // Retorna true solo si el usuario elige salir del sistema desde el login
            } else {
                Printers.printError("Opción no válida. Intente nuevamente.");
            }
        }
        Printers.printSeparator();
        Printers.printInfo("Apagando sistema...");
        Time.sleep();
        
        Printers.printInfo("¡Hasta luego!");
        Printers.printSeparator();
    }

    /**
     * Maneja el proceso de inicio de sesión del usuario,
     * solicitando credenciales
     * y autenticando según el rol.
     * 
     * @param scanner Scanner para leer la entrada del usuario
     */
    private static boolean iniciarSesion(Scanner scanner) {
        Printers.printTitle("Inicio de sesión");
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
                    Printers.printSuccess("Usuario autenticado correctamente.");

                    if (u instanceof Cliente c) {
                        Printers.printTitle("Bienvenido cliente");
                        System.out.println("Nombre: " + c.getNombre() + " " + c.getApellido());
                        System.out.println("Celular registrado: " + c.getNumeroCelular());
                        System.out.print("\n¿Este número de celular es correcto? (S/N): ");
                        String verif = scanner.nextLine();
                        if (verif.equalsIgnoreCase("S")) {
                            Printers.printSuccess("¡Identidad confirmada!");
                            mostrarMenu(c, scanner);
                            sesionIniciada = true;
                        } else {
                            Printers.printError("Verificación fallida. Cerrando sesión.");
                            Time.sleep();
                            sesionIniciada = true;
                        }
                    } else if (u instanceof Repartidor r) {
                        Printers.printTitle("Bienvenido repartidor");
                        System.out.println("Nombre: " + r.getNombre() + " " + r.getApellido());
                        System.out.println("Empresa asignada: " + r.getNombreEmpresa());
                        System.out.print("\n¿Esta empresa es correcta? (S/N): ");
                        String verif = scanner.nextLine();
                        if (verif.equalsIgnoreCase("S")) {
                            Printers.printSuccess("¡Identidad confirmada!");
                            mostrarMenu(r, scanner);
                            sesionIniciada = true;
                        } else {
                            Printers.printError("Verificación fallida. Cerrando sesión...");
                            Time.sleep();
                            sesionIniciada = true;
                        }
                    }
                    break;
                }
            }

            if (!usuarioEncontrado) {
                Printers.printError("Usuario o contraseña incorrectos. Intente nuevamente.");
                System.out.print("¿Desea salir? (Escriba 'si' para salir o presione Enter para intentar de nuevo): ");
                String respuesta = scanner.nextLine().trim();
                if (respuesta.equalsIgnoreCase("si")) {
                    Printers.printInfo("Regresando al menú principal...");
                    return false;
                }
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
            Printers.printTitle("Menú cliente");
            System.out.println("1. Comprar");
            System.out.println("2. Gestionar pedido");
            System.out.println("3. Cerrar sesión");
            System.out.print("\nSeleccione una opción: ");

            String opcion = scanner.nextLine();

            switch (opcion) {
                case "1":
                    cliente.realizarCompra(usuarios, scanner);
                    break;
                case "2":
                    cliente.gestionarPedido(scanner);
                    break;
                case "3":
                    Printers.printSeparator();
                    Printers.printInfo("Cerrando sesión...");
                    Printers.printSeparator();
                    Time.sleep();
                    continuar = false;
                    break;
                default:
                    Printers.printError("Opción no válida. Intente nuevamente.");
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
            Printers.printTitle("Menú repartidor");
            System.out.println("1. Gestionar pedido");
            System.out.println("2. Consultar pedidos asignados");
            System.out.println("3. Cerrar sesión");
            System.out.print("\nSeleccione una opción: ");

            String opcion = scanner.nextLine();

            switch (opcion) {
                case "1":
                    repartidor.gestionarPedido(scanner);
                    break;
                case "2":
                    repartidor.consultarPedidosAsignados();
                    break;
                case "3":
                    Printers.printSeparator();
                    Printers.printInfo("Cerrando sesión...");
                    Printers.printSeparator();
                    Time.sleep();
                    continuar = false;
                    break;
                default:
                    Printers.printError("Opción no válida. Intente nuevamente.");
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