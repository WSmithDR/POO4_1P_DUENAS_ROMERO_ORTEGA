package model;

import model.Enums.EstadoPedido;
import model.Roles.Cliente;
import model.Roles.Repartidor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Clase que representa un pedido en el sistema
 */
public class Pedido {
    private String codigoPedido;
    private Cliente cliente;
    private Repartidor repartidor;
    private Producto producto;
    private int cantidad;
    private double valorPagado;
    private LocalDateTime fechaPedido;
    private EstadoPedido estado;

    /**
     * Constructor de la clase Pedido
     * @param cliente Cliente que realiza el pedido
     * @param repartidor Repartidor asignado al pedido
     * @param producto Producto comprado
     * @param cantidad Cantidad comprada
     * @param valorPagado Valor total pagado
     */
    public Pedido(Cliente cliente, Repartidor repartidor, Producto producto, int cantidad, double valorPagado) {
        this.codigoPedido = generarCodigoPedido();
        this.cliente = cliente;
        this.repartidor = repartidor;
        this.producto = producto;
        this.cantidad = cantidad;
        this.valorPagado = valorPagado;
        this.fechaPedido = LocalDateTime.now();
        this.estado = EstadoPedido.EN_PREPARACION;
    }

    /**
     * Genera un código único para el pedido
     * @return Código del pedido
     */
    private String generarCodigoPedido() {
        return "PED" + System.currentTimeMillis();
    }

    // Getters
    public String getCodigoPedido() {
        return codigoPedido;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Repartidor getRepartidor() {
        return repartidor;
    }

    public Producto getProducto() {
        return producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public double getValorPagado() {
        return valorPagado;
    }

    public LocalDateTime getFechaPedido() {
        return fechaPedido;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    // Setters
    public void setCodigoPedido(String codigoPedido) {
        this.codigoPedido = codigoPedido;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public void setRepartidor(Repartidor repartidor) {
        this.repartidor = repartidor;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public void setValorPagado(double valorPagado) {
        this.valorPagado = valorPagado;
    }

    public void setFechaPedido(LocalDateTime fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public void setEstado(EstadoPedido estado) {
        this.estado = estado;
    }

    /**
     * Convierte el pedido a formato de archivo
     * @return String con formato para guardar en archivo
     */
    public String toFileFormat() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return codigoPedido + "|" + 
               cliente.getCedula() + "|" + 
               repartidor.getCedula() + "|" + 
               producto.getCodigo() + "|" + 
               cantidad + "|" + 
               valorPagado + "|" + 
               fechaPedido.format(formatter) + "|" + 
               estado.toString();
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return "Pedido #" + codigoPedido + 
               " - " + producto.getNombre() + 
               " x" + cantidad + 
               " - $" + valorPagado + 
               " - " + estado + 
               " - " + fechaPedido.format(formatter);
    }
}
