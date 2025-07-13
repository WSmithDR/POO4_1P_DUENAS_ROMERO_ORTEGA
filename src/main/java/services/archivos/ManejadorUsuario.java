package services.archivos;

import java.util.ArrayList;

import model.Enums.Rol;
import model.Roles.Cliente;
import model.Roles.Repartidor;
import model.Roles.Usuario;

/**
 * Clase que maneja la carga y gestión de usuarios, clientes y repartidores desde archivos.
 */
public class ManejadorUsuario {
    private static final String USUARIOS_FILE = "resources/Usuarios.txt";
    private static final String CLIENTES_FILE = "resources/Clientes.txt";
    private static final String REPARTIDORES_FILE = "resources/Repartidores.txt";

    /**
     * Carga los usuarios desde el archivo de usuarios
     * 
     * @param usuarios ArrayList de usuarios
     * @return ArrayList de usuarios
     */
    public static ArrayList<Usuario> cargarUsuarios() {
        ArrayList<Usuario> usuarios = new ArrayList<>();

        try {
            ArrayList<String> lineas = ManejadorArchivos.LeeFichero(USUARIOS_FILE);

            for (int i = 1; i < lineas.size(); i++) {
                String linea = lineas.get(i);

                String[] partes = linea.split("\\|");

                String codigoUnico = partes[0];
                String cedula = partes[1];
                String nombre = partes[2];
                String apellido = partes[3];
                String username = partes[4];
                String contrasenia = partes[5];
                String correo = partes[6];
                String rolChar = partes[7];
                ArrayList<String> nombres = new ArrayList<>();
                if (!nombre.isEmpty())
                    nombres.add(nombre);

                ArrayList<String> apellidos = new ArrayList<>();
                if (!apellido.isEmpty())
                    apellidos.add(apellido);

                if (rolChar.equals("C")) {
                    partes[7] = Rol.CLIENTE.toString();
                }
                if (rolChar.equals("R")) {
                    partes[7] = Rol.REPARTIDOR.toString();
                }
                if (partes[7].equals(Rol.CLIENTE.toString())) {

                    Cliente cliente = cargarDatosCliente(codigoUnico, cedula, nombre, apellido, username, correo,
                            contrasenia);
                    if (cliente != null) {

                        usuarios.add(cliente);

                    }
                } else if (partes[7].equals(Rol.REPARTIDOR.toString())) {

                    Repartidor repartidor = cargarDatosRepartidor(codigoUnico, cedula, nombre, apellido, username, correo,
                            contrasenia);
                    if (repartidor != null) {

                        usuarios.add(repartidor);

                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error cargando usuarios: " + e.getMessage());
        }
        return usuarios;
    }

    /**
     * Lectura de datos del archivo cliente.
     * @param codigoUnico Código único del cliente
     * @param cedula Cédula del cliente
     * @param nombre Nombre del cliente
     * @param apellido Apellido del cliente
     * @param username Nombre de usuario
     * @param correo Correo electrónico
     * @param contrasenia Contraseña
     * @return Cliente encontrado o null si no existe
     */
    public static Cliente cargarDatosCliente(
            String codigoUnico,
            String cedula,
            String nombre,
            String apellido,
            String username,
            String correo,
            String contrasenia) {

        ArrayList<String> lineas = ManejadorArchivos.LeeFichero(CLIENTES_FILE);
        Cliente clienteEncontrado = null;
        for (int i = 1; i < lineas.size(); i++) {
            String linea = lineas.get(i);
            String[] partes = linea.split("\\|");
            if (partes[1].equals(cedula)) {
                String celular = partes[4];
                String direccion = partes[5];
                clienteEncontrado = new Cliente(codigoUnico, cedula, nombre, apellido, username, correo, contrasenia, celular,
                        direccion);
            }
        }
        return clienteEncontrado;
    }

    /**
     * Lectura de datos del archivo repartidor.
     * @param codigoUnico Código único del repartidor
     * @param cedula Cédula del repartidor
     * @param nombre Nombre del repartidor
     * @param apellido Apellido del repartidor
     * @param username Nombre de usuario
     * @param correo Correo electrónico
     * @param contrasenia Contraseña
     * @return Repartidor encontrado o null si no existe
     */
    public static Repartidor cargarDatosRepartidor(
            String codigoUnico,
            String cedula,
            String nombre,
            String apellido,
            String username,
            String correo, String contrasenia) {
        ArrayList<String> lineas = ManejadorArchivos.LeeFichero(REPARTIDORES_FILE);
        Repartidor repartidorEncontrado = null;

        for (int i = 1; i < lineas.size(); i++) {
            String linea = lineas.get(i);
            String[] partes = linea.split("\\|");
            if (partes[0].equals(codigoUnico)) {
                String empresa = partes[4];
                String cedulaArchivo = partes[1];
                repartidorEncontrado = new Repartidor(codigoUnico, cedulaArchivo, nombre, apellido, username, correo, contrasenia, empresa);
            }
        }
        return repartidorEncontrado;
    }

       /**
     * Busca un cliente por código único
     * @param codigoUnico Código único del cliente
     * @return Cliente encontrado o null
     */
    public static Cliente buscarClientePorCodigoUnico(String codigoUnico) {
        for (Usuario usuario : ManejadorUsuario.cargarUsuarios()) {
            if (usuario instanceof Cliente && usuario.getCodigoUnico().equals(codigoUnico)) {
                return (Cliente) usuario;
            }
        }
        return null;
    }

    public static Repartidor buscarRepartidorPorCodigoUnico(String codigoUnico) {
        for (Usuario usuario : ManejadorUsuario.cargarUsuarios()) {
            if (usuario instanceof Repartidor && usuario.getCodigoUnico().equals(codigoUnico)) {
                return (Repartidor) usuario;
            }
        }
        return null;
    }

}