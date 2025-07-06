package app;


import model.Roles.Cliente;
import model.Roles.Repartidor;
import model.Roles.Usuario;

import java.util.Scanner;


import java.util.*;
import services.ManejadorUsuario;
import services.ManejadorProducto;
import services.ManejadorPedido;
import model.Producto;
import model.Pedido;

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
}
