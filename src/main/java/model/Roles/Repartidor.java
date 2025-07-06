package model.Roles;

import java.util.ArrayList;
import java.util.Scanner;

import model.Enums.Rol;
import services.archivos.ManejadorPedido;
import model.Pedido;

public class Repartidor extends Usuario {
    private String nombreEmpresa;

    public Repartidor(
        String cedula, 
        String user_name, 
        String nombre, 
        String apellido, 
        String correo, 
        String contrasenia, 
        String nombreEmpresa,
        String codigoUnico
        ){
        super(
            Rol.REPARTIDOR, 
            cedula, 
            user_name, 
            nombre, 
            apellido, 
            correo, 
            contrasenia,
            codigoUnico);
        this.nombreEmpresa = nombreEmpresa;
    }

    //getters
    public String getNombreEmpresa(){
        return this.nombreEmpresa;
    }

    //setters
    public void setNombreEmpresa(String nombreEmpresa){
        this.nombreEmpresa = nombreEmpresa;
    }
    
    /**
     * Consulta los pedidos asignados al repartidor que no han sido entregados
     * @param pedidos Lista de todos los pedidos del sistema
     */
    public void consultarPedidosAsignados(ArrayList<Pedido> pedidos) {
        ManejadorPedido.consultarPedidosAsignados(this, pedidos);
    }

    /**
     * Implementación del método abstracto de Usuario
     * Permite al repartidor gestionar sus pedidos asignados
     * @param pedidos Lista de todos los pedidos del sistema
     */
    @Override
    public void gestionarPedido(ArrayList<Pedido> pedidos, Scanner scanner) {
        ManejadorPedido.gestionarPedido(this, pedidos, scanner);
    }
}
