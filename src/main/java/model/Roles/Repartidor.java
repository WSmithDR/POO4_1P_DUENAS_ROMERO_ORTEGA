package model.Roles;

import java.util.ArrayList;
import model.Enums.Rol;
import model.Pedido;

public class Repartidor extends Usuario {
    private String nombreEmpresa;

    public Repartidor(String cedula, String user_name, ArrayList<String> nombres, ArrayList<String> apellidos, String correo, String contrasenia, String nombreEmpresa){
        super(Rol.REPARTIDOR, cedula, user_name, nombres, apellidos, correo, contrasenia);
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
        services.ManejadorPedido.consultarPedidosAsignados(this, pedidos);
    }

    /**
     * Implementación del método abstracto de Usuario
     * Permite al repartidor gestionar sus pedidos asignados
     * @param pedidos Lista de todos los pedidos del sistema
     */
    @Override
    public void gestionarPedido(ArrayList<Pedido> pedidos) {
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        services.ManejadorPedido.gestionarPedido(this, pedidos, scanner);
        // No cerrar el scanner aquí ya que podría estar en uso en otros lugares
    }
}
