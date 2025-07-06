package model.Roles;

import java.util.ArrayList;
import model.Enums.Rol;
import model.Enums.EstadoPedido;
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
        System.out.println("\n===== PEDIDOS ASIGNADOS =====");
        System.out.println("Buscando pedidos asignados no entregados...\n");
        
        ArrayList<Pedido> pedidosAsignados = new ArrayList<>();
        
        // Buscar pedidos asignados a este repartidor que no estén entregados
        for (Pedido pedido : pedidos) {
            if (pedido.getRepartidor().getCodigoUnico().equals(this.codigoUnico) && 
                !(pedido.getEstadoPedido()==EstadoPedido.ENTREGADO) &&
                !(pedido.getEstadoPedido().equals(EstadoPedido.CANCELADO))) {
                pedidosAsignados.add(pedido);
            }
        }
        
        if (pedidosAsignados.isEmpty()) {
            System.out.println("No tienes pedidos asignados pendientes.");
            return;
        }
        
        System.out.println("Pedidos encontrados:\n");
        
        // Mostrar cada pedido
        for (int i = 0; i < pedidosAsignados.size(); i++) {
            Pedido pedido = pedidosAsignados.get(i);
            System.out.println((i + 1) + ". Código: " + pedido.getCodigoPedido());
            System.out.println("   Fecha del pedido: " + pedido.getFechaPedido());
            System.out.println("   Estado actual: " + pedido.getEstadoPedido());
            System.out.println();
        }
        
        System.out.println("Total de pedidos pendientes: " + pedidosAsignados.size());
        System.out.println("Recuerde que solo puede gestionar los pedidos que se encuentren EN PREPARACIÓN o EN RUTA.");
    }

    public void gestionarPedido(){
        
    }
}
