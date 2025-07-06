package services;


import java.util.ArrayList;

import model.Enums.Rol;
import model.Roles.Cliente;
import model.Roles.Repartidor;
import model.Roles.Usuario;
import persistence.ManejoArchivos;

public class ManejadorUsuario {
    private static final String USUARIOS_FILE = "resources/Usuarios.txt";
    private static final String CLIENTES_FILE = "resources/Clientes.txt";
    private static final String REPARTIDORES_FILE = "resources/Repartidores.txt";

    /**
     * Carga los usuarios desde el archivo de usuarios
     * @param usuarios ArrayList de usuarios
     * @return ArrayList de usuarios
     */
    public static ArrayList<Usuario> cargarUsuarios(ArrayList<Usuario> usuarios) {

        try {
            ArrayList<String> lineas = ManejoArchivos.LeeFichero(USUARIOS_FILE);
            
            
            for (int i = 1; i < lineas.size(); i++) {
                String linea = lineas.get(i);
                
                String[] partes = linea.split("\\|");
            
                String codigo=partes[0];
                String cedula = partes[1];
                String nombre = partes[2];
                String apellido = partes[3];
                String username = partes[4];
                String contrasenia = partes[5];
                String correo= partes[6];
                String rolChar= partes[7];
                ArrayList<String> nombres = new ArrayList<>();
                if (!nombre.isEmpty())
                    nombres.add(nombre);

                ArrayList<String> apellidos = new ArrayList<>();
                if (!apellido.isEmpty())
                    apellidos.add(apellido);
                
              
        
                if(rolChar.equals("C")){
                    partes[7]=Rol.CLIENTE.toString();
                }
                if(rolChar.equals("R")){
                    partes[7]=Rol.REPARTIDOR.toString();
                }
                if (partes[7].equals(Rol.CLIENTE.toString())) {
                    
                    Cliente cliente = cargarDatosCliente(cedula, username, nombres, apellidos, correo, contrasenia);
                    if (cliente != null) {
                        
                        usuarios.add(cliente);
                    
                        
                    }
                } else if (partes[7].equals(Rol.REPARTIDOR.toString())) {
                    
                    Repartidor repartidor = cargarDatosRepartidor(cedula, username, nombres, apellidos, correo,
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
     * Lectura de datos del archivo cliente
     **/
    public static Cliente cargarDatosCliente(String cedula, String username, ArrayList<String> nombres,
            ArrayList<String> apellidos,
            String correo, String contrasenia) {

        ArrayList<String> lineas = ManejoArchivos.LeeFichero(CLIENTES_FILE);
        Cliente clienteEncontrado = null;
        for (int i = 1; i < lineas.size(); i++) {
            String linea = lineas.get(i);
            String[] partes = linea.split("\\|");
            if (partes[1].equals(cedula)) {
                String celular = partes[4];
                String direccion = partes[5];
                clienteEncontrado = new Cliente(cedula, username, nombres, apellidos, correo, contrasenia, celular,
                        direccion);
            }
        }
        return clienteEncontrado;
    }

    /**
     * Lectura de datos del archivo repartidor
     **/
    public static Repartidor cargarDatosRepartidor(String cedula, String username, ArrayList<String> nombres,
            ArrayList<String> apellidos,
            String correo, String contrasenia) {
            ArrayList<String> lineas = ManejoArchivos.LeeFichero(REPARTIDORES_FILE);
            Repartidor repartidorEncontrado = null;
            
            for (int i = 1; i < lineas.size(); i++) {
                String linea = lineas.get(i);
                String[] partes = linea.split("\\|");
                if (partes[1].equals(cedula)) {
                    String empresa = partes[4];
                    repartidorEncontrado = new Repartidor(cedula, username, nombres, apellidos, correo, contrasenia, empresa);
                }
            }
            return repartidorEncontrado;
        }

}