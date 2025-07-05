package app;

import model.Enums.Rol;
import model.Roles.Cliente;
import model.Roles.Repartidor;
import model.Roles.Usuario;

import java.io.*;
import java.util.*;

public class Sistema {
    private List<Usuario> usuarios;
    private Scanner scanner;
    public final String USUARIOS_FILE = "resources/Usuarios.txt";
    public final String CLIENTES_FILE = "resources/Clientes.txt";
    public final String REPARTIDORES_FILE = "resources/Repartidores.txt";

    

    public Sistema() {
        usuarios = new ArrayList<>();
        scanner = new Scanner(System.in);
    }

    public void iniciar() {
        cargarUsuarios();
        iniciarSesion();
    }

    private void cargarUsuarios() {
        try (BufferedReader br = new BufferedReader(new FileReader(USUARIOS_FILE))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split("|");
                String cedula = partes[0];
                String username = partes[1];
                ArrayList<String> nombres = new ArrayList<>();
                if (!partes[2].isEmpty()) nombres.add(partes[2]);
                if (!partes[3].isEmpty()) nombres.add(partes[3]);

                ArrayList<String> apellidos = new ArrayList<>();
                if (!partes[4].isEmpty()) apellidos.add(partes[4]);
                if (!partes[5].isEmpty()) apellidos.add(partes[5]);

                String correo = partes[6];
                String contrasenia = partes[7];
                Rol rol = Rol.valueOf(partes[8].toUpperCase());

                if (rol == Rol.CLIENTE) {
                    Cliente cliente = cargarDatosCliente(cedula, username, nombres, apellidos, correo, contrasenia);
                    if (cliente != null) usuarios.add(cliente);
                } else if (rol == Rol.REPARTIDOR) {
                    Repartidor repartidor = cargarDatosRepartidor(cedula, username, nombres, apellidos, correo, contrasenia);
                    if (repartidor != null) usuarios.add(repartidor);
                }
            }
        } catch (IOException e) {
            System.out.println("Error leyendo usuarios.txt: " + e.getMessage());
        }
    }

    private Cliente cargarDatosCliente(String cedula, String username, ArrayList<String> nombres, ArrayList<String> apellidos,
                                       String correo, String contrasenia) {
        try (BufferedReader br = new BufferedReader(new FileReader(CLIENTES_FILE))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split("|");
                if (partes[0].equals(cedula)) {
                    String celular = partes[1];
                    String direccion = partes[2];
                    return new Cliente(cedula, username, nombres, apellidos, correo, contrasenia, celular, direccion);
                }
            }
        } catch (IOException e) {
            System.out.println("Error leyendo clientes.txt: " + e.getMessage());
        }
        return null;
    }

    private Repartidor cargarDatosRepartidor(String cedula, String username, ArrayList<String> nombres, ArrayList<String> apellidos,
                                             String correo, String contrasenia) {
        try (BufferedReader br = new BufferedReader(new FileReader(REPARTIDORES_FILE))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes[0].equals(cedula)) {
                    String empresa = partes[1];
                    return new Repartidor(cedula, username, nombres, apellidos, correo, contrasenia, empresa);
                }
            }
        } catch (IOException e) {
            System.out.println("Error leyendo repartidores.txt: " + e.getMessage());
        }
        return null;
    }

    private void iniciarSesion() {
        System.out.println("===== INICIO DE SESIÓN =====");
        System.out.print("Usuario: ");
        String userInput = scanner.nextLine();
        System.out.print("Contraseña: ");
        String passInput = scanner.nextLine();

        for (Usuario u : usuarios) {
            if (u.getUser_name().equals(userInput) && u.getContrasenia().equals(passInput)) {
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
                return;
            }
        }

        System.out.println("Usuario o contraseña incorrectos.");
    }

    private void mostrarMenuCliente(Cliente cliente) {
        System.out.println("=== Menú Cliente ===");
        System.out.println("1. Comprar");
        System.out.println("2. Gestionar pedido");
        System.out.println("3. Salir");
        
    }

    private void mostrarMenuRepartidor(Repartidor repartidor) {
        System.out.println("=== Menú Repartidor ===");
        System.out.println("1. Gestionar pedido");
        System.out.println("2. Consultar pedidos asignados");
        System.out.println("3. Salir");
        
    }
}

