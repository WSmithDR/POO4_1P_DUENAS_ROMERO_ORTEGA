package model;

import java.util.Date;
import model.Enums.EstadoPedido;
import model.Roles.Cliente;
import model.Roles.Repartidor;
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
    public static int contadorPedido = 0;

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

    //Getters
    public Date getFechaPedido() {
        return this.fechaPedido;
    }

    public double getTotalPagado() {
        return this.totalPagado;
    }

    public int getCantidadProducto() {
        return this.cantidadProducto;
    }

    public EstadoPedido getEstadoPedido() {
        return this.estadoPedido;
    }

    public String getCodigoPedido() {
        return this.codigoPedido;
    }

    public Cliente getCliente() {
        return this.cliente;
    }

    public Repartidor getRepartidor(){
        return this.repartidor;
    }

    public Producto getProducto(){
        return this.producto;
    }

    //Setters
    public void setFechaPedido(Date fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public void setTotalPagado(double totalPagado) {
        this.totalPagado = totalPagado;
    }

    public void setCantidadProducto(int cantidadProducto) {
        this.cantidadProducto = cantidadProducto;
    }

    public void setEstadoPedido(EstadoPedido estadoPedido) {
        this.estadoPedido = estadoPedido;
    }

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
