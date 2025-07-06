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

    public static int contadorPedido = 0;
    
    public Pedido(
        Cliente cliente, 
        Repartidor repartidor, 
        Producto producto, 
        int cantidad, 
        double totalPagado) {
        this.fechaPedido = new Date();
        this.producto = producto;
        this.totalPagado = totalPagado;
        this.cantidadProducto = cantidad;
        this.repartidor = repartidor;
        this.estadoPedido = EstadoPedido.EN_PREPARACION;
        this.codigoPedido = generarCodigoPedido();
        this.cliente = cliente;

        contadorPedido++;
    }

    /**
     * Genera un código único para el pedido basado en el contador de pedidos actual.
     * El formato del código es "PED" seguido del número de pedido.
     * 
     * @return String con el código único del pedido en formato "PED{número}"
     */
    private String generarCodigoPedido() {
        return String.format("PED%d", contadorPedido);
    }
    
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

    public Cliente getCliente(){
        return this.cliente;
    }

    public Producto getProducto(){
        return this.producto;
    };

    public Repartidor getRepartidor(){
        return this.repartidor;
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

    public String toFileFormat() {
        return String.join("|",
            codigoPedido,
            cliente.getCodigoUnico(),
            repartidor.getCodigoUnico(),
            codigoPedido,
            String.valueOf(cantidadProducto),
            String.valueOf(totalPagado),
            ManejoFechas.setFechaSimple(fechaPedido),
            String.valueOf(estadoPedido)
        );
    }
        
}
