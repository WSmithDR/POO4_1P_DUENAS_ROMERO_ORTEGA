package services.archivos;

import model.Pedido;
import model.Producto;
import model.Roles.Cliente;
import model.Roles.Repartidor;
import model.Roles.Usuario;
import persistence.ManejoArchivos;
import utils.ManejoFechas;
import model.Enums.EstadoPedido;

import java.util.ArrayList;
import java.util.Random;
import java.io.File;

/**
 * Clase que maneja la gestión de pedidos, incluyendo asignación de repartidores,
 * almacenamiento, carga, consulta y cambio de estado de pedidos.
 */
public class ManejadorPedido {
    private static final String PEDIDOS_FILE = "PROYECTO 1P/resources/Pedidos.txt";

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
        File archivo = new File(PEDIDOS_FILE);
        if (!archivo.exists()) {
            ManejoArchivos.EscribirArchivo(PEDIDOS_FILE, "CodigoPedido|Fecha|CodigoProducto|Cantidad|ValorPagado|Estado|CodigoRepartidor|CodigoCliente");
        }
        String lineaPedido = pedido.toFileFormat();
        ManejoArchivos.EscribirArchivo(PEDIDOS_FILE, lineaPedido);
    }

    /**
     * Carga todos los pedidos desde el archivo
     * @param usuarios Lista de usuarios para buscar clientes y repartidores
     * @param productos Lista de productos para buscar productos
     * @return ArrayList con todos los pedidos
     */
    public static ArrayList<Pedido> cargarPedidos(ArrayList<Usuario> usuarios, ArrayList<Producto> productos) {
        ArrayList<Pedido> pedidos = new ArrayList<>();
        ArrayList<String> codigosAgregados = new ArrayList<>();
        
        ArrayList<String> lineas = ManejoArchivos.LeeFichero(PEDIDOS_FILE);
        
        // Empezamos desde la última línea hacia la primera (salta encabezado si existe).
        // Esto lo hacemos ya que solo se tomará la última versión de un pedido
        // ya que estaremos agregando el mismo pedido pero con un estado diferente.
        for (int i = lineas.size() - 1; i > 0; i--) {
            String linea = lineas.get(i);
            String[] partes = linea.split("\\|");
            if (partes.length < 8) continue;
            
            String codigoPedido = partes[0];
            if (!codigosAgregados.contains(codigoPedido)) {
                String fecha = partes[1];
                String codigoProducto = partes[2];
                int cantidad = Integer.parseInt(partes[3]);
                double valorPagado = Double.parseDouble(partes[4]);
                String estadoStr = partes[5];
                String codigoRepartidor = partes[6];
                String codigoUnicoCliente = partes[7];

                Producto producto = buscarProductoPorCodigo(productos, codigoProducto);
                Repartidor repartidor = buscarRepartidorPorCodigoUnico(usuarios, codigoRepartidor);
                Cliente cliente = buscarClientePorCodigoUnico(usuarios, codigoUnicoCliente);

                if (producto != null && repartidor != null && cliente != null) {
                    Pedido pedido = new Pedido(cliente, repartidor, producto, cantidad, valorPagado);
                    pedido.setCodigoPedido(codigoPedido);
                    pedido.setFechaPedido(ManejoFechas.parseFechaSimple(fecha));
                    pedido.setEstadoPedido(EstadoPedido.valueOf(estadoStr));
                    pedidos.add(pedido);
                    codigosAgregados.add(codigoPedido);
                }
            }
        }
        return pedidos;
    }

    /**
     * Busca un cliente por código único
     * @param usuarios Lista de usuarios
     * @param codigoUnico Código único del cliente
     * @return Cliente encontrado o null
     */
    private static Cliente buscarClientePorCodigoUnico(ArrayList<Usuario> usuarios, String codigoUnico) {
        for (Usuario usuario : usuarios) {
            if (usuario instanceof Cliente && usuario.getCodigoUnico().equals(codigoUnico)) {
                return (Cliente) usuario;
            }
        }
        return null;
    }

    /**
     * Busca un repartidor por código único
     * @param usuarios Lista de usuarios
     * @param codigoUnico Código único del repartidor
     * @return Repartidor encontrado o null
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

    /**
     * Actualiza el estado de un pedido en el archivo agregando una nueva línea
     * con el estado actualizado, manteniendo el mismo código de pedido.
     * @param pedido Pedido con el estado actualizado
     */
    public static void actualizarEstadoPedidoEnArchivo(Pedido pedido) {
        String nuevaLinea = pedido.toFileFormat();
        ManejoArchivos.EscribirArchivo(PEDIDOS_FILE, nuevaLinea);
    }

    /**
     * Consulta el estado de un pedido directamente desde el archivo
     * @param codigoPedido Código del pedido a consultar
     * @param codigoUnicoCliente Código único del cliente para verificar propiedad
     * @return Estado del pedido o null si no se encuentra
     */
    public static EstadoPedido consultarEstadoPedidoDesdeArchivo(String codigoPedido, String codigoUnicoCliente) {
        ArrayList<String> lineas = ManejoArchivos.LeeFichero(PEDIDOS_FILE);
        
        // Buscar desde la última línea hacia la primera para obtener el estado más reciente
        for (int i = lineas.size() - 1; i > 0; i--) {
            String linea = lineas.get(i);
            String[] partes = linea.split("\\|");
            if (partes.length >= 8) {
                String codigo = partes[0];
                String codigoCliente = partes[7];
                
                if (codigo.equals(codigoPedido) && codigoCliente.equals(codigoUnicoCliente)) {
                    return EstadoPedido.valueOf(partes[5]);
                }
            }
        }
        return null;
    }

    /**
     * Obtiene la información completa de un pedido desde el archivo
     * @param codigoPedido Código del pedido a consultar
     * @param codigoUnicoCliente Código único del cliente para verificar propiedad
     * @return Array con la información del pedido o null si no se encuentra
     */
    public static String[] obtenerInformacionPedidoDesdeArchivo(String codigoPedido, String codigoUnicoCliente) {
        ArrayList<String> lineas = ManejoArchivos.LeeFichero(PEDIDOS_FILE);
        
        // Buscar desde la última línea hacia la primera para obtener la información más reciente
        for (int i = lineas.size() - 1; i > 0; i--) {
            String linea = lineas.get(i);
            String[] partes = linea.split("\\|");
            if (partes.length >= 8) {
                String codigo = partes[0];
                String codigoCliente = partes[7];
                
                if (codigo.equals(codigoPedido) && codigoCliente.equals(codigoUnicoCliente)) {
                    return partes;
                }
            }
        }
        return null;
    }

    /**
     * Retorna la cantidad de registros de pedidos almacenados en el archivo.
     * @return Número de pedidos registrados
     */
    public static int cantidadRegistroPedidos() {
        ArrayList<String> lineas = ManejoArchivos.LeeFichero(PEDIDOS_FILE);
        if (lineas == null || lineas.size() <= 1) {
            return 0;
        }
        return lineas.size() - 1;
    }
}