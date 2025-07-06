package model;

import java.text.SimpleDateFormat;
import java.util.Date;

import model.Enums.CategoriaProducto;

public class Pedido {
    private Date fechaPedido;
    private String codigoProducto;
    private double totalPagado;
    private int cantidadProducto;
    private CategoriaProducto categoria;
    private String codRepartidor;
    private String estadoPedido;
    private String codigoPedido;
    private static int contadorPedido = 0;

    
    public Pedido(Date fechaPedido, String codigoProducto, double totalPagado, int cantidadProducto, CategoriaProducto categoria,
                        String codRepartidor, String estadoPedido) {
        this.fechaPedido = fechaPedido;
        this.codigoProducto = codigoProducto;
        this.totalPagado = totalPagado;
        this.cantidadProducto = cantidadProducto;
        this.categoria = categoria;
        this.codRepartidor = codRepartidor;
        this.estadoPedido = estadoPedido;
        this.codigoPedido = generarCodigo();

        contadorPedido++;
    }
    
    public void cambiarEstado(String nuevoEstado) {
        this.estadoPedido = nuevoEstado;
    }

    public String generarCodigo() {
        return String.format("PED%d", contadorPedido);
    }
    
    

    // Método para obtener el código actual sin generar uno nuevo
    public String obtenerCodigo() {
        return this.codigoPedido;
    }

    //Getters
    public Date getFechaPedido() {
        return this.fechaPedido;
    }

    public String getCodigoProducto() {
        return this.codigoProducto;
    }

    public double getTotalPagado() {
        return this.totalPagado;
    }

    public int getCantidadProducto() {
        return this.cantidadProducto;
    }

    public CategoriaProducto getCategoria() {
        return this.categoria;
    }

    public String getCodRepartidor() {
        return this.codRepartidor;
    }

    public String getEstadoPedido() {
        return this.estadoPedido;
    }

    public String getCodigoPedido() {
        return this.codigoPedido;
    }

    //Setters
    public void setFechaPedido(Date fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public void setCodigoProducto(String codigoProducto) {
        this.codigoProducto = codigoProducto;
    }

    public void setTotalPagado(double totalPagado) {
        this.totalPagado = totalPagado;
    }

    public void setCantidadProducto(int cantidadProducto) {
        this.cantidadProducto = cantidadProducto;
    }

    public void setCategoria(CategoriaProducto categoria) {
        this.categoria = categoria;
    }

    public void setCodRepartidor(String codRepartidor) {
        this.codRepartidor = codRepartidor;
    }

    public void setEstadoPedido(String estadoPedido) {
        this.estadoPedido = estadoPedido;
    }

    public void setCodigoPedido(String codigoPedido) {
        this.codigoPedido = codigoPedido;
    }

}
