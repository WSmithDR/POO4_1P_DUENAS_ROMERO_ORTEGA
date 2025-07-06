package app;


import model.Pedido;
import model.Roles.Cliente;
import model.Roles.Repartidor;
import model.Roles.Usuario;

import java.util.ArrayList;
import java.util.Scanner;

import services.ManejadorEmail;
import services.ManejadorUsuario;
import services.ManejadorProducto;
import services.ManejadorPedido;
import model.Producto;

public class Sistema {
    private static ArrayList<Usuario> usuarios= new ArrayList<>();
    private static ArrayList<Producto> productos = new ArrayList<>();
    private static ArrayList<Pedido> pedidos = new ArrayList<>();
    private static Scanner scanner= new Scanner(System.in);

    /**
     * Método principal que inicia el sistema
     * Carga los usuarios desde la clase ManejadorUsuario y muestra la pantalla de inicio de sesión
     */
    public static void iniciar() {
        usuarios = ManejadorUsuario.cargarUsuarios(usuarios);
        productos = ManejadorProducto.cargarProductos();
        pedidos = ManejadorPedido.cargarPedidos(usuarios, productos);
        iniciarSesion(scanner);
    }

    /**
     * Maneja el proceso de inicio de sesión del usuario
     * Solicita credenciales y autentica al usuario según su rol
     * 
     * @param scanner Scanner para leer la entrada del usuario
     */
    private static void iniciarSesion(Scanner scanner) {
        System.out.println("===== INICIO DE SESIÓN =====");
        System.out.print("Usuario: ");
        String userInput = scanner.nextLine();
        System.out.print("Contraseña: ");
        String passInput = scanner.nextLine();

        boolean usuarioEncontrado = false;
        
        for (Usuario u : usuarios) {
            if (u.getUser_name().equals(userInput) && u.getContrasenia().equals(passInput)) {
                usuarioEncontrado = true;
                System.out.println("Usuario autenticado correctamente.");
                
                if (u instanceof Cliente c) {
                    System.out.println("Rol detectado: CLIENTE");
                    System.out.println("Bienvenido, " + c.getNombres().get(0) + " " + c.getApellidos().get(0));
                    System.out.println("Celular registrado: " + c.getNumeroCelular());
                    System.out.print("¿Este número de celular es correcto? (S/N): ");
                    String verif = scanner.nextLine();
                    if (verif.equalsIgnoreCase("S")) {
                        mostrarMenuCliente(c);
                    } else {
                        System.out.println("Verificación fallida. Cerrando sesión.");
                    }
                } else if (u instanceof Repartidor r) {
                    System.out.println("Rol detectado: REPARTIDOR");
                    System.out.println("Bienvenido, " + r.getNombres().get(0) + " " + r.getApellidos().get(0));
                    System.out.println("Empresa asignada: " + r.getNombreEmpresa());
                    System.out.print("¿Esta empresa es correcta? (S/N): ");
                    String verif = scanner.nextLine();
                    if (verif.equalsIgnoreCase("S")) {
                        mostrarMenuRepartidor(r);
                    } else {
                        System.out.println("Verificación fallida. Cerrando sesión.");
                    }
                }
                break;
            }
        }
        
        if (!usuarioEncontrado) {
            System.out.println("Usuario o contraseña incorrectos. Intente nuevamente.");
        }
    }

    /**
     * Muestra y maneja el menú principal para usuarios con rol de Cliente
     * Permite al cliente realizar compras y gestionar pedidos
     * 
     * @param cliente El objeto Cliente autenticado
     */
    private static void mostrarMenuCliente(Cliente cliente) {
        boolean continuar = true;
        
        while (continuar) {
            System.out.println("\n=== Menú Cliente ===");
            System.out.println("1. Comprar");
            System.out.println("2. Gestionar pedido");
            System.out.println("3. Salir");
            System.out.print("Seleccione una opción: ");
            
            String opcion = scanner.nextLine();
            
            switch (opcion) {
                case "1":
                    cliente.realizarCompra(productos, usuarios, pedidos, scanner);
                    break;
                case "2":
                    System.out.println("Función de gestión de pedido en desarrollo...");
                    break;
                case "3":
                    System.out.println("Cerrando sesión...");
                    continuar = false;
                    break;
                default:
                    System.out.println("Opción no válida. Intente nuevamente.");
                    break;
            }
        }
    }

    /**
     * Muestra y maneja el menú principal para usuarios con rol de Repartidor
     * Permite al repartidor gestionar pedidos y consultar asignaciones
     * 
     * @param repartidor El objeto Repartidor autenticado
     */
    private static void mostrarMenuRepartidor(Repartidor repartidor) {
        boolean continuar = true;
        
        while (continuar) {
            System.out.println("\n=== Menú Repartidor ===");
            System.out.println("1. Gestionar pedido");
            System.out.println("2. Consultar pedidos asignados");
            System.out.println("3. Salir");
            System.out.print("Seleccione una opción: ");
            
            String opcion = scanner.nextLine();
            
            switch (opcion) {
                case "1":
                    System.out.println("Función de gestión de pedido en desarrollo...");
                    break;
                case "2":
                    System.out.println("Función de consulta de pedidos en desarrollo...");
                    break;
                case "3":
                    System.out.println("Cerrando sesión...");
                    continuar = false;
                    break;
                default:
                    System.out.println("Opción no válida. Intente nuevamente.");
                    break;
            }
        }
    }

    /**
     * Notifica al cliente cuando este realiza un pedido
     * @param cliente El objeto Cliente que realiza el pedido
     * @param pedidoRealizado Contiene la informacion del pedido que realizo el cliente
     */
    public void notificar(Cliente cliente, Pedido pedidoRealizado) {
        ManejadorEmail manejadorEmail = new ManejadorEmail();
        String asunto = "Pedido realizado";
        String cuerpo = String.format(
            "El cliente %s %s ha realizado un pedido con código %s el día %s.\n\n" +
            "Producto: %s\n" +
            "Cantidad: %d\n" +
            "Valor pagado: $%.2f\n" +
            "Estado inicial: %s\n\n" +
            "Gracias por su compra. Recibirá actualizaciones del estado de su pedido por este medio.",
            cliente.getNombres(), cliente.getApellidos(),
            pedidoRealizado.codigoPedido(),
            pedidoRealizado.getFechaPedido(),
            pedidoRealizado.getCodigoProducto(),
            pedidoRealizado.getCantidadProducto(),
            pedidoRealizado.getTotalPagado(),
            pedidoRealizado.getEstadoPedido()
        );
        manejadorEmail.enviarCorreo(cliente.getCorreo(), asunto, cuerpo);
    }

    public void notificar(Repartidor repartidor, Pedido pedidoAsignado) {
        ManejadorEmail manejadorEmail = new ManejadorEmail();
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
            repartidor.getNombres(), 
            repartidor.getApellidos(),
            pedidoAsignado.getCodigoPedido(),
            pedidoAsignado.getFechaPedido(),
            pedidoAsignado.getCliente().getNombres(), 
            pedidoAsignado.getCliente().getApellidos(),
            pedidoAsignado.getEstadoPedido()
        );
        manejadorEmail.enviarCorreo(repartidor.getCorreo(), asunto, cuerpo);
    }

    public void notificar(Cliente cliente, Pedido pedido, String nuevoEstado) {
        ManejadorEmail manejadorEmail = new ManejadorEmail();
        String asunto = "Actualización del estado de su pedido";
        String cuerpo = String.format(
            "Estimado/a %s %s,\n\n" +
            "Le informamos que el estado de su pedido con código %s ha cambiado a: %s.\n\n" +
            "Fecha del pedido: %s\n" +
            "Producto: %s\n" +
            "Repartidor asignado: %s\n\n" +
            "Gracias por confiar en nosotros.",
            cliente.getNombres(), cliente.getApellidos(),
            pedido.codigoPedido(), nuevoEstado,
            pedido.getFechaPedido(),
            pedido.getCodigoProduto(),
            pedido.getCodRepartidor()
        );

        manejadorEmail.enviarCorreo(cliente.getCorreo(), asunto, cuerpo);
    }

    
}
