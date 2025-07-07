package model.Roles;

import java.util.ArrayList;
import java.util.Scanner;

import model.Enums.Rol;
import services.archivos.ManejadorPedido;
import model.Pedido;

public class Repartidor extends Usuario {
    private String nombreEmpresa;

    /**
     * Constructor de la clase Repartidor.
     * @param codigoUnico Código único del repartidor
     * @param cedula Cédula del repartidor
     * @param nombre Nombre del repartidor
     * @param apellido Apellido del repartidor
     * @param user_name Nombre de usuario
     * @param correo Correo electrónico
     * @param contrasenia Contraseña
     * @param nombreEmpresa Nombre de la empresa a la que pertenece
     */
    public Repartidor(
        String codigoUnico,
        String cedula,
        String nombre,
        String apellido,
        String user_name,
        String correo,
        String contrasenia,
        String nombreEmpresa
        ){
        super(
            codigoUnico,
            cedula,
            nombre,
            apellido,
            user_name,
            correo,
            contrasenia,
            Rol.REPARTIDOR);
        this.nombreEmpresa = nombreEmpresa;
    }

    /**
     * Obtiene el nombre de la empresa a la que pertenece el repartidor.
     * @return Nombre de la empresa
     */
    public String getNombreEmpresa() {
        return this.nombreEmpresa;
    }

    /**
     * Establece el nombre de la empresa a la que pertenece el repartidor.
     * @param nombreEmpresa Nombre de la empresa
     */
    public void setNombreEmpresa(String nombreEmpresa) {
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
