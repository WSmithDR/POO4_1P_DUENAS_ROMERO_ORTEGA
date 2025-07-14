package model;

import java.util.Date;
import model.Enums.EstadoPedido;
import services.archivos.ManejadorPedido;
import utils.ManejoFechas;
import java.util.Locale;
import utils.Printers;


public class Pedido {
    private Date fechaPedido;
    private String codProducto;
    private double totalPagado;
    private int cantidadProducto;
    private String codRepartidor;
    private EstadoPedido estadoPedido;
    private String codigoPedido;
    private String codCliente;


    /** Contador estático para generar códigos únicos de pedidos */
    public static int contadorPedido = ManejadorPedido.obtenerUltimoNumeroPedido();

 
    public Pedido(String codCliente, String codRepartidor, String codProducto, int cantidad, double total) {
        this.fechaPedido = new Date();
        this.codProducto = codProducto;
        this.totalPagado = total;
        this.cantidadProducto = cantidad;
        this.codRepartidor = codRepartidor;
        this.estadoPedido = EstadoPedido.EN_PREPARACION;
        this.codigoPedido = generarCodigoPedido();
        this.codCliente = codCliente;
    }

    /**
     * Genera un código único para el pedido.
     * Incrementa el contador estático y retorna un código en formato "PED" + número.
     * @return Código único del pedido en formato String
     */
    public String generarCodigoPedido() {
        contadorPedido++;
        return String.format("PED%d", contadorPedido);
    }
    
    /**
     * Obtiene el código actual del pedido sin generar uno nuevo.
     * @return Código actual del pedido
     */
    public String obtenerCodigo() {
        return this.codigoPedido;
    }

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
     * Obtiene el código del producto asociado a este pedido.
     * @return Código del producto
     */
    public String getCodProducto() { return codProducto; }

    /**
     * Obtiene el código del repartidor asignado a este pedido.
     * @return Código del repartidor
     */
    public String getCodRepartidor() { return codRepartidor; }

    /**
     * Obtiene el código del cliente que realizó este pedido.
     * @return Código del cliente
     */
    public String getCodCliente() { return codCliente; }
    

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
     * Devuelve una representación en formato de archivo del pedido, con el orden:
     * CodigoPedido|Fecha|CodigoProducto|Cantidad|ValorPagado|Estado|CodigoRepartidor|CodigoCliente
     * @return Cadena con los datos del pedido en el formato especificado
     */
    public String toFileFormat() {
        return String.format(Locale.US, "%s|%s|%s|%d|%.2f|%s|%s|%s",
                codigoPedido,
                ManejoFechas.setFechaSimple(fechaPedido),
                codProducto,
                cantidadProducto,   
                totalPagado,
                estadoPedido.name(),
                codRepartidor != null ? codRepartidor : "",
                codCliente != null ? codCliente : "");
    }

    /**
     * Compara si dos pedidos son iguales según su código de pedido (ignorando mayúsculas/minúsculas).
     * @param obj Objeto a comparar
     * @return true si los códigos de pedido son iguales, false en caso contrario
     */
    @Override
    public boolean equals(Object obj){
        if(this==obj) return true;
        if(this.getClass()==obj.getClass()){
            Pedido pedido = (Pedido) obj;
            return this.codigoPedido.equalsIgnoreCase(pedido.codigoPedido);
        }else{
            return false;
        }
    }

        /**
     * Muestra un mensaje personalizado según el estado del pedido.
     * @param estado Estado actual del pedido
     */
    public static void mostrarMensajeSegunEstado(EstadoPedido estado) {
        if (estado == null) {
            Printers.printInfo("Estado del pedido: No disponible");
            return;
        }

        switch (estado) {
            case EN_PREPARACION:
                Printers.printInfo("Su pedido está siendo preparado para su envío.");
                break;
            case EN_CAMINO:
                Printers.printInfo("Su pedido está en camino hacia su dirección.");
                break;
            case ENTREGADO:
                Printers.printSuccess("Su pedido ha sido entregado exitosamente.");
                break;
            case CANCELADO:
                Printers.printInfo("Su pedido ha sido cancelado.");
                break;
        }
    }
}
