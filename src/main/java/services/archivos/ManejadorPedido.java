package services.archivos;

import model.Pedido;
import model.Producto;
import model.Roles.Cliente;
import model.Roles.Repartidor;
import model.Roles.Usuario;
import persistence.ManejoArchivos;

import java.util.ArrayList;
import java.util.Random;

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
                Repartidor repartidor = buscarRepartidorPorCedula(usuarios, partes[2]);
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
     * Busca un repartidor por cédula
     * @param usuarios Lista de usuarios
     * @param cedula Cédula del repartidor
     * @return Repartidor encontrado o null
     */
    private static Repartidor buscarRepartidorPorCedula(ArrayList<Usuario> usuarios, String cedula) {
        for (Usuario usuario : usuarios) {
            if (usuario instanceof Repartidor && usuario.getCedula().equals(cedula)) {
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
} 