package Pedido;

import java.util.Date;
import Enums.Rol;
import Roles.Repartidor;

public class Pedido {
    private Date fecha;
    private String codigoProducto;
    private double precio;
    private int stock;
    private CategoriaProducto categoria;
    private Repartidor repartidor;
    private String estado;
    private String codigoPedido;
    private int contadorPedido;

    
    public void cambiarEstado (String nuevoEstado){
    }

    public void generarCodigo (){
    }


    //Getters
    public Date getFecha (){
        return this.fecha;
    }

    public String getCodigoProduto (){
        return this.codigoProducto;
    }

    public double getPrecio (){
        return this.precio;
    }

    public int getStock (){
        return this.stock;
    }

    public CategoriaProducto getcategoria (){
        return this.categoria;
    }

    public Repartidor getRepartidor (){
        return this.repartidor;
    }

    public String getEstado (){
        return this.estado;
    }

    public String codigoPedido (){
        return this.codigoPedido;
    }

    public int getContadorPedido (){
        return this.contadorPedido;
    }


    //Setters
    public void setFecha (Date fecha){
        this.fecha=fecha;
    }

    public void setCodigoProducto (String codigoProducto){
        this.codigoProducto=codigoProducto;
    }

    public void setPrecio (double precio){
        this.precio=precio;
    }

    public void setStock (int stock){
        this.stock=stock;
    }

    public void setCategoria (CategoriaProducto categoria){
        this.categoria=categoria;
    }

    public void setRepartidor (Repartidor repartidor){
        this.repartidor=repartidor;
    }

    public void setEstado (String estado){
        this.estado=estado;
    }

    public void setCodigoPedido (String codigoPedido){
        this.codigoPedido=codigoPedido;
    }

    public void setContadorPedido (int contadorPedido){
        this.contadorPedido=contadorPedido;
    }

}
