package model;

import java.util.Date;

import model.Enums.CategoriaProducto;
import model.Enums.EstadoPedido;
import model.Roles.Cliente;
import model.Roles.Repartidor;
import utils.ManejoFechas;

/**
 * Clase que representa un pedido en el sistema
 */
public class Pedido {
    private Date fechaPedido;
    private String codigoProducto;
    private double totalPagado;
    private int cantidadProducto;
    private CategoriaProducto categoria;
    private String codRepartidor;
    private EstadoPedido estadoPedido;
    private String codigoPedido;
    private Cliente cliente;

    public static int contadorPedido = 0;

    
    public Pedido(
        Date fechaPedido, 
        String codigoProducto, 
        double totalPagado, 
        int cantidadProducto, 
        CategoriaProducto categoriaProducto,
        String codRepartidor, 
        Cliente cliente,
        EstadoPedido estadoPedido
        ) {
        this.fechaPedido = fechaPedido;
        this.codigoProducto = codigoProducto;
        this.totalPagado = totalPagado;
        this.cantidadProducto = cantidadProducto;
        this.categoria = categoriaProducto;
        this.codRepartidor = codRepartidor;
        this.estadoPedido = estadoPedido;
        this.codigoPedido = generarCodigo();
        this.cliente = cliente;

        contadorPedido++;
    }
    
    public Pedido(Cliente cliente, Repartidor repartidor, Producto producto, int cantidad, double total) {
        this.fechaPedido = new Date();
        this.codigoProducto = producto.getCodigo();
        this.totalPagado = total;
        this.cantidadProducto = cantidad;
        this.categoria = producto.getCategoria();
        this.codRepartidor = repartidor.getCedula();
        this.estadoPedido = EstadoPedido.EN_PREPARACION;
        this.codigoPedido = generarCodigo();
        this.cliente = cliente;

        contadorPedido++;
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

    public EstadoPedido getEstadoPedido() {
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

    public void setEstadoPedido(EstadoPedido estadoPedido) {
        this.estadoPedido = estadoPedido;
    }

    public void setCodigoPedido(String codigoPedido) {
        this.codigoPedido = codigoPedido;
    }

    public String toFileFormat() {
        return String.join("|",
            codigoPedido,
            cliente.getCedula(),
            codRepartidor,
            codigoPedido,
            String.valueOf(cantidadProducto),
            String.valueOf(totalPagado),
            ManejoFechas.getFechaSimple(fechaPedido),
            String.valueOf(estadoPedido)
        );
    }
        
}
