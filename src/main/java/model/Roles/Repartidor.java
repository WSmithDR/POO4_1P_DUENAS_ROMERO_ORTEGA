package model.Roles;

import java.util.ArrayList;
import java.util.Scanner;

import model.Enums.Rol;
import model.Enums.EstadoPedido;
import services.archivos.ManejadorPedido;
import services.email.ManejadorEmail;
import app.Sistema;
import utils.ManejoFechas;
import model.Pedido;

public class Repartidor extends Usuario {
    private String nombreEmpresa;

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
        System.out.println("\n===== PEDIDOS ASIGNADOS =====");
        System.out.println("Buscando pedidos asignados no entregados...\n");
        
        ArrayList<Pedido> pedidosAsignados = new ArrayList<>();
        
        // Buscar pedidos asignados a este repartidor que no estén entregados
        for (Pedido pedido : pedidos) {
            if (pedido.getRepartidor().getCodigoUnico().equals(this.getCodigoUnico()) && 
                !(pedido.getEstadoPedido() == EstadoPedido.ENTREGADO) &&
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
            System.out.println("   Fecha del pedido: " + ManejoFechas.setFechaSimple(pedido.getFechaPedido()));
            System.out.println("   Estado actual: " + pedido.getEstadoPedido());
            System.out.println();
        }
        
        System.out.println("Total de pedidos pendientes: " + pedidosAsignados.size());
        System.out.println("Recuerde que solo puede gestionar los pedidos que se encuentren EN PREPARACIÓN o EN RUTA.");
    }

    /**
     * Implementación del método abstracto de Usuario
     * Permite al repartidor gestionar sus pedidos asignados
     * @param pedidos Lista de todos los pedidos del sistema
     */
    @Override
    public void gestionarPedido(ArrayList<Pedido> pedidos, Scanner scanner) {
        System.out.println("\n===== GESTIÓN DE PEDIDOS - REPARTIDOR =====");
        System.out.println("Repartidor: " + this.getNombre() + " " + this.getApellido());
        System.out.println("Empresa: " + this.getNombreEmpresa());
        
        consultarPedidosAsignados(pedidos);
        
        // Opción para cambiar estado de pedidos
        System.out.println("\n¿Desea cambiar el estado de algún pedido? (s/n): ");
        String respuesta = scanner.nextLine().toLowerCase();
        if (respuesta.equals("s") || respuesta.equals("si")) {
            cambiarEstadoPedido(pedidos, scanner);
        }
    }

    /**
     * Permite al repartidor cambiar el estado de un pedido
     * @param pedidos Lista de pedidos
     * @param scanner Scanner para leer entrada
     */
    private void cambiarEstadoPedido(ArrayList<Pedido> pedidos, Scanner scanner) {
        System.out.println("===== GESTIONAR ESTADO DE PEDIDO =====");
        System.out.print("Ingrese el código del pedido que desea gestionar: ");
        String codigoPedido = scanner.nextLine().trim();
        
        // Buscar el pedido
        Pedido pedidoAModificar = null;
        for (Pedido pedido : pedidos) {
            if (pedido.getCodigoPedido().equals(codigoPedido) && 
                pedido.getRepartidor().getCodigoUnico().equals(this.getCodigoUnico())) {
                pedidoAModificar = pedido;
                break;
            }
        }
        
        if (pedidoAModificar == null) {
            System.out.println("Pedido no encontrado o no asignado a este repartidor.");
            return;
        }
        
        System.out.println("\nPedido encontrado:");
        System.out.println("Fecha del pedido: " + ManejoFechas.setFechaSimple(pedidoAModificar.getFechaPedido()) + 
                          " \nCódigo del producto: " + pedidoAModificar.getProducto().getCodigo() + 
                          " \nEstado actual: " + pedidoAModificar.getEstadoPedido());
        
        // Mostrar opciones según el estado actual
        mostrarOpcionesEstado(pedidoAModificar, scanner);
    }
    
    /**
     * Muestra las opciones de estado disponibles según el estado actual
     * @param pedido Pedido a modificar
     * @param scanner Scanner para leer entrada
     */
    private void mostrarOpcionesEstado(Pedido pedido, Scanner scanner) {
        EstadoPedido estadoActual = pedido.getEstadoPedido();
        ManejadorEmail manejadorEmail = new ManejadorEmail();
        
        if (estadoActual == EstadoPedido.EN_PREPARACION) {
            System.out.println("\nSeleccione el nuevo estado:");
            System.out.println("1. EN CAMINO");
            System.out.println("2. ENTREGADO");
            System.out.print("Opción: ");
            int opcion = Integer.parseInt(scanner.nextLine());
            if (opcion == 1) {
                pedido.setEstadoPedido(EstadoPedido.EN_CAMINO);
                System.out.println("Estado actualizado correctamente a EN CAMINO.");
                // Guardar el nuevo estado en el archivo
                ManejadorPedido.actualizarEstadoPedidoEnArchivo(pedido);
                Sistema.notificar(pedido.getCliente(), pedido, EstadoPedido.EN_CAMINO, manejadorEmail);
            } else if (opcion == 2) {
                System.out.println("\nError: No puede cambiar directamente de EN PREPARACIÓN a ENTREGADO. Debe cambiar primero a EN CAMINO.");
                mostrarOpcionesEstado(pedido, scanner); // Mostrar opciones nuevamente
            } else {
                System.out.println("Opción inválida.");
            }
        } else if (estadoActual == EstadoPedido.EN_CAMINO) {
            System.out.println("\nSeleccione el nuevo estado:");
            System.out.println("1. ENTREGADO");
            System.out.print("Opción: ");
            int opcion = Integer.parseInt(scanner.nextLine());
            if (opcion == 1) {
                pedido.setEstadoPedido(EstadoPedido.ENTREGADO);
                System.out.println("Estado actualizado correctamente a ENTREGADO.");
                // Guardar el nuevo estado en el archivo
                ManejadorPedido.actualizarEstadoPedidoEnArchivo(pedido);
                Sistema.notificar(pedido.getCliente(), pedido, EstadoPedido.ENTREGADO, manejadorEmail);
            } else {
                System.out.println("Opción inválida.");
            }
        } else {
            System.out.println("No se pueden realizar cambios en el estado actual: " + estadoActual);
        }
    }
}
