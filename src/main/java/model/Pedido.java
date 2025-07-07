package model;

import java.util.Date;
import model.Enums.EstadoPedido;
import model.Roles.Cliente;
import model.Roles.Repartidor;
import services.archivos.ManejadorPedido;
import utils.ManejoFechas;

/**
 * Clase que representa un pedido en el sistema
 */
public class Pedido {
    private Date fechaPedido;
    private Producto producto;
    private double totalPagado;
    private int cantidadProducto;
    private Repartidor repartidor;
    private EstadoPedido estadoPedido;
    private String codigoPedido;
    private Cliente cliente;

    /** Contador estático para generar códigos únicos de pedidos */
    public static int contadorPedido = ManejadorPedido.cantidadRegistroPedidos();

    /**
     * Constructor de la clase Pedido.
     * Crea un nuevo pedido con los datos proporcionados y establece el estado inicial
     * como EN_PREPARACION.
     * 
     * @param cliente Cliente que realiza el pedido
     * @param repartidor Repartidor asignado para la entrega
     * @param producto Producto solicitado
     * @param cantidad Cantidad de productos solicitados
     * @param total Monto total a pagar
     */
    public Pedido(Cliente cliente, Repartidor repartidor, Producto producto, int cantidad, double total) {
        this.fechaPedido = new Date();
        this.producto = producto;
        this.totalPagado = total;
        this.cantidadProducto = cantidad;
        this.repartidor = repartidor;
        this.estadoPedido = EstadoPedido.EN_PREPARACION;
        this.codigoPedido = generarCodigoPedido();
        this.cliente = cliente;
    }

    /**
     * Genera un código único para el pedido.
     * Incrementa el contador estático y retorna un código en formato "PED" + número.
     * 
     * @return Código único del pedido en formato String
     */
    public String generarCodigoPedido() {
        contadorPedido++;
        return String.format("PED%d", contadorPedido);
    }
    
    /**
     * Obtiene el código actual del pedido sin generar uno nuevo.
     * 
     * @return Código actual del pedido
     */
    public String obtenerCodigo() {
        return this.codigoPedido;
    }

    /**
     * Obtiene la fecha del pedido en formato simple (dd/MM/yyyy).
     * 
     * @return Fecha del pedido en formato String
     */
    /*public String getFechaSimple() {
        return ManejoFechas.getFechaSimple(this.fechaPedido);
    }*/

    /**
     * Obtiene la fecha del pedido.
     * @return Fecha del pedido
     */
    public Date getFechaPedido() {
        return this.fechaPedido;
    }

    /**
     * Obtiene el total pagado por el pedido.
     * @return Total pagado
     */
    public double getTotalPagado() {
        return this.totalPagado;
    }

    /**
     * Obtiene la cantidad de productos en el pedido.
     * @return Cantidad de productos
     */
    public int getCantidadProducto() {
        return this.cantidadProducto;
    }

    /**
     * Obtiene el estado del pedido.
     * @return Estado del pedido
     */
    public EstadoPedido getEstadoPedido() {
        return this.estadoPedido;
    }

    /**
     * Obtiene el código del pedido.
     * @return Código del pedido
     */
    public String getCodigoPedido() {
        return this.codigoPedido;
    }

    /**
     * Obtiene el cliente que realizó el pedido.
     * @return Cliente del pedido
     */
    public Cliente getCliente() {
        return this.cliente;
    }

    /**
     * Obtiene el repartidor asignado al pedido.
     * @return Repartidor del pedido
     */
    public Repartidor getRepartidor(){
        return this.repartidor;
    }

    /**
     * Obtiene el producto solicitado en el pedido.
     * @return Producto del pedido
     */
    public Producto getProducto(){
        return this.producto;
    }

    /**
     * Establece la fecha del pedido.
     * @param fechaPedido Fecha del pedido
     */
    public void setFechaPedido(Date fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    /**
     * Establece el total pagado por el pedido.
     * @param totalPagado Total pagado
     */
    public void setTotalPagado(double totalPagado) {
        this.totalPagado = totalPagado;
    }

    /**
     * Establece la cantidad de productos en el pedido.
     * @param cantidadProducto Cantidad de productos
     */
    public void setCantidadProducto(int cantidadProducto) {
        this.cantidadProducto = cantidadProducto;
    }

    /**
     * Establece el estado del pedido.
     * @param estadoPedido Estado del pedido
     */
    public void setEstadoPedido(EstadoPedido estadoPedido) {
        this.estadoPedido = estadoPedido;
    }

    /**
     * Establece el código del pedido.
     * @param codigoPedido Código del pedido
     */
    public void setCodigoPedido(String codigoPedido) {
        this.codigoPedido = codigoPedido;
    }

    /**
     * Convierte el pedido a formato de archivo para persistencia.
     * Los campos se separan por el carácter '|' en el siguiente orden:
     * código_pedido|cédula_cliente|cédula_repartidor|código_producto|cantidad|total|fecha|estado
     * 
     * @return String con el pedido en formato de archivo
     */
    public String toFileFormat() {
        return String.join("|",
            codigoPedido,
            cliente.getCedula(),
            repartidor.getCodigoUnico(),
            producto.getCodigo(),
            String.valueOf(cantidadProducto),
            String.valueOf(totalPagado),
            ManejoFechas.setFechaSimple(fechaPedido),
            String.valueOf(estadoPedido)
        );
    }
}
