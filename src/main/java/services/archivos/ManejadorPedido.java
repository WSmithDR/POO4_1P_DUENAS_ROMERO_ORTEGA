package services.archivos;

import model.Pedido;
import model.Producto;
import model.Roles.Cliente;
import model.Roles.Repartidor;
import utils.ManejoFechas;
import utils.Printers;

import java.util.ArrayList;
import java.util.Random;
import java.io.File;


public class ManejadorPedido {
    private static final String PEDIDOS_FILE = "database/Pedidos.txt";

    /**
     * Asigna un repartidor aleatorio de la lista de repartidores disponibles.
     * @param repartidores Lista de repartidores disponibles
     * @return Repartidor seleccionado aleatoriamente, o null si la lista está vacía
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
     * Guarda un pedido en el archivo de pedidos. Si el archivo no existe, lo crea con encabezado.
     * @param pedido Pedido a guardar
     */
    public static void guardarPedido(Pedido pedido) {
        try {
            File archivo = new File(PEDIDOS_FILE);
            if (!archivo.exists()) {
                ManejadorArchivos.EscribirArchivo(PEDIDOS_FILE, "CodigoPedido|Fecha|CodigoProducto|Cantidad|ValorPagado|Estado|CodigoRepartidor|CodigoCliente");
            }
            String lineaPedido = pedido.toFileFormat();
            ManejadorArchivos.EscribirArchivo(PEDIDOS_FILE, lineaPedido);
        } catch (Exception e) {
            Printers.printError("Error guardando pedido: " + e.getMessage());
        }
    }

    /**
     * Carga todos los pedidos asignados a un repartidor específico desde el archivo.
     * @param repartidor Repartidor cuyos pedidos se desean cargar
     * @return Lista de pedidos asignados a ese repartidor
     */
    public static ArrayList<Pedido> cargarPedidosRepartidor(Repartidor repartidor) {
        ArrayList<Pedido> pedidos = new ArrayList<>();
        try {
            ArrayList<String> lineas = ManejadorArchivos.LeeFichero(PEDIDOS_FILE);
            ArrayList<String> codigosAgregados = new ArrayList<>();
            // Recorre de derecha a izquierda (última línea a la primera)
            for (int i = lineas.size() - 1; i > 0; i--) {
                String linea = lineas.get(i);
                String[] partes = linea.split("\\|");
                String codigoPedido = partes[0];
                String codigoRepartidor = partes[6];
                if (!codigosAgregados.contains(codigoPedido) && repartidor.getCodigoUnico().equals(codigoRepartidor)) {
                    String fecha = partes[1];
                    String codigoProducto = partes[2];
                    int cantidad = Integer.parseInt(partes[3]);
                    double valorPagado = Double.parseDouble(partes[4]);
                    String estadoStr = partes[5];
                    String codigoUnicoCliente = partes[7];

                    Producto producto = ManejadorProducto.buscarProductoPorCodigo(codigoProducto);
                    Cliente cliente = ManejadorUsuario.buscarClientePorCodigoUnico(codigoUnicoCliente);

                    if (producto != null && repartidor != null && cliente != null) {
                        Pedido pedido = new Pedido(codigoUnicoCliente, codigoRepartidor, codigoProducto, cantidad, valorPagado);
                        pedido.setCodigoPedido(codigoPedido);
                        pedido.setFechaPedido(ManejoFechas.parseFechaSimple(fecha));
                        pedido.setEstadoPedido(Enum.valueOf(model.Enums.EstadoPedido.class, estadoStr));
                        pedidos.add(pedido);
                        codigosAgregados.add(codigoPedido);
                    }
                }
            }
        } catch (Exception e) {
            Printers.printError("Error cargando pedidos: " + e.getMessage());
        }
        return pedidos;
    }
    
    /**
     * Carga todos los pedidos de un cliente específico desde el archivo.
     * @param codigoCliente Código único del cliente
     * @return Lista de pedidos de ese cliente
     */
    public static ArrayList<Pedido> cargarPedidosCliente(String codigoCliente) {
        ArrayList<Pedido> pedidosCliente = new ArrayList<>();
        try {
            ArrayList<String> lineas = ManejadorArchivos.LeeFichero(PEDIDOS_FILE);
            ArrayList<String> codigosAgregados = new ArrayList<>();
            for (int i = lineas.size() - 1; i > 0; i--) {
                String linea = lineas.get(i);
                String[] partes = linea.split("\\|");
                String codigoPedido = partes[0];
                String codigoClienteArchivo = partes[7];
                if (!codigosAgregados.contains(codigoPedido) && codigoCliente.equals(codigoClienteArchivo)) {
                    String fecha = partes[1];
                    String codigoProducto = partes[2];
                    int cantidad = Integer.parseInt(partes[3]);
                    double valorPagado = Double.parseDouble(partes[4]);
                    String estadoStr = partes[5];
                    String codigoRepartidor = partes[6];


                    Pedido pedido = new Pedido(codigoCliente, codigoRepartidor, codigoProducto, cantidad, valorPagado);
                    pedido.setCodigoPedido(codigoPedido);
                    pedido.setFechaPedido(ManejoFechas.parseFechaSimple(fecha));
                    pedido.setEstadoPedido(Enum.valueOf(model.Enums.EstadoPedido.class, estadoStr));
                    pedidosCliente.add(pedido);
                    codigosAgregados.add(codigoPedido);
                }
            }
        } catch (Exception e) {
            Printers.printError("Error cargando pedidos del cliente: " + e.getMessage());
        }
        return pedidosCliente;
    }

    /**
     * Obtiene el mayor número de pedido registrado en el archivo, para generar el siguiente código único.
     * @return El número más grande encontrado en los códigos de pedido (PEDn)
     */
    public static int obtenerUltimoNumeroPedido() {
        ArrayList<String> lineas = ManejadorArchivos.LeeFichero(PEDIDOS_FILE);
        int max = 0;
        if (lineas != null && lineas.size() > 1) {
            for (int i = 1; i < lineas.size(); i++) {
                String[] partes = lineas.get(i).split("\\|");
                String codigo = partes[0]; // Ejemplo: PED12
                if (codigo.startsWith("PED")) {
                        int num = Integer.parseInt(codigo.substring(3));
                        if (num > max) max = num;

                }else{
                    Printers.printError("Error de logica en el cotador de pedidos");
                }
            }
        }
        return max;
    }
}