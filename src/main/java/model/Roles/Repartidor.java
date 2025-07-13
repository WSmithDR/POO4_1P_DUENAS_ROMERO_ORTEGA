package model.Roles;

import java.util.ArrayList;
import java.util.Scanner;

import app.Sistema;
import model.Enums.EstadoPedido;
import model.Enums.Rol;
import services.archivos.ManejadorPedido;
import services.email.ManejadorEmail;
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
            String nombreEmpresa) {
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
     * 
     * @return Nombre de la empresa
     */
    public String getNombreEmpresa() {
        return this.nombreEmpresa;
    }

    /**
     * Establece el nombre de la empresa a la que pertenece el repartidor.
     * 
     * @param nombreEmpresa Nombre de la empresa
     */
    public void setNombreEmpresa(String nombreEmpresa) {
        this.nombreEmpresa = nombreEmpresa;
    }

    /**
     * Consulta los pedidos asignados a un repartidor
     * 
     * @param repartidor Repartidor que consulta
     * @param pedidos    Lista de todos los pedidos
     */
    public void consultarPedidosAsignados() {
        System.out.println("\n===== PEDIDOS ASIGNADOS =====");
        System.out.println("Buscando pedidos asignados no entregados...\n");

        ArrayList<Pedido> pedidosAsignados = new ArrayList<>();

        // Buscar pedidos asignados a este repartidor que no estén entregados
        for (Pedido pedido : ManejadorPedido.cargarPedidosRepartidor(this)) {
            if (!(pedido.getEstadoPedido() == EstadoPedido.ENTREGADO) &&
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
        System.out.println("Recuerde que solo puede gestionar los pedidos que se encuentren "
                + EstadoPedido.EN_PREPARACION.getDescripcion() + " o " + EstadoPedido.EN_CAMINO.getDescripcion() + ".");
    }

    /**
     * Muestra las opciones de estado disponibles según el estado actual
     * 
     * @param pedido  Pedido a modificar
     * @param scanner Scanner para leer entrada
     */
    private static void mostrarOpcionesEstado(Pedido pedido, Scanner scanner) {
        EstadoPedido estadoActual = pedido.getEstadoPedido();
        ManejadorEmail manejadorEmail = new ManejadorEmail();
        if (estadoActual == EstadoPedido.EN_PREPARACION) {
            System.out.println("\nSeleccione el nuevo estado:");
            System.out.println("1. " + EstadoPedido.EN_CAMINO.getDescripcion());
            System.out.println("2. " + EstadoPedido.ENTREGADO.getDescripcion());
            System.out.print("Opción: ");
            int opcion = Integer.parseInt(scanner.nextLine());
            if (opcion == 1) {
                pedido.setEstadoPedido(EstadoPedido.EN_CAMINO);
                System.out.println("Estado actualizado correctamente a " + EstadoPedido.EN_CAMINO.getDescripcion() + ".");
                Sistema.notificar(pedido.getCliente(), pedido, EstadoPedido.EN_CAMINO, manejadorEmail);
                services.archivos.ManejadorPedido.guardarPedido(pedido);
            } else if (opcion == 2) {
                System.out.println("\nError: No puede cambiar directamente de " + EstadoPedido.EN_PREPARACION.getDescripcion() + " a " + EstadoPedido.ENTREGADO.getDescripcion() + ". Debe cambiar primero a " + EstadoPedido.EN_CAMINO.getDescripcion() + ".");
                mostrarOpcionesEstado(pedido, scanner); 
                // Mostrar opciones nuevamente aplicando recursividad
            } else {
                System.out.println("Opción inválida.");
            }
        } else if (estadoActual == EstadoPedido.EN_CAMINO) {
            System.out.println("\nSeleccione el nuevo estado:");
            System.out.println("1. " + EstadoPedido.ENTREGADO.getDescripcion());
            System.out.print("Opción: ");
            int opcion = Integer.parseInt(scanner.nextLine());
            if (opcion == 1) {
                pedido.setEstadoPedido(EstadoPedido.ENTREGADO);
                System.out.println("Estado actualizado correctamente a " + EstadoPedido.ENTREGADO.getDescripcion() + ".");
                Sistema.notificar(pedido.getCliente(), pedido, EstadoPedido.ENTREGADO, manejadorEmail);
                services.archivos.ManejadorPedido.guardarPedido(pedido);
            } else {
                System.out.println("Opción inválida.");
            }
        } else {
            System.out.println("No se pueden realizar cambios en el estado actual: " + estadoActual.getDescripcion());
        }
    }

    /**
     * Busca un pedido asignado a este repartidor por su código (no case sensitive)
     * 
     * @param codigoPedido Código del pedido a buscar
     * @return El pedido encontrado o null si no existe
     */
    private Pedido buscarPedidoPorCodigo(String codigoPedido) {
        for (Pedido pedido : ManejadorPedido.cargarPedidosRepartidor(this)) {
            if (pedido.getCodigoPedido().equalsIgnoreCase(codigoPedido)) {
                return pedido;
            }
        }
        return null;
    }

    /**
     * Permite al repartidor cambiar el estado de un pedido
     * 
     * @param repartidor Repartidor que cambia el estado
     * @param scanner    Scanner para leer entrada
     */
    private void cambiarEstadoPedido(Repartidor repartidor, Scanner scanner) {
        System.out.println("===== GESTIONAR ESTADO DE PEDIDO =====");
        boolean continuar = true;
        while (continuar) {
            System.out.println("Ingrese el código del pedido que desea gestionar");
            System.out.println("O escriba \"salir\" para cancelar la operacion");
            System.out.print("Codigo: ");
            String codigoPedido = scanner.nextLine().trim();
            if (codigoPedido.equalsIgnoreCase("salir")) {
                System.out.println("Operación cancelada.");
                continuar = false;
                continue;
            }
            // Buscar el pedido (no case sensitive)
            Pedido pedidoAModificar = buscarPedidoPorCodigo(codigoPedido);
            if (pedidoAModificar == null) {
                System.out.println("Código no válido.");
                System.out.print("¿Desea salir? (Escriba 'si' para salir o presione Enter para intentar de nuevo): ");
                String respuesta = scanner.nextLine().trim();
                if (respuesta.equalsIgnoreCase("si")) {
                    System.out.println("Operación cancelada.");
                    continuar = false;
                }
                // Si no, el ciclo continúa
            } else {
                System.out.println("\nPedido encontrado:");
                System.out
                        .println("Fecha del pedido: " + ManejoFechas.setFechaSimple(pedidoAModificar.getFechaPedido()) +
                                " \nCódigo del producto: " + pedidoAModificar.getProducto().getCodigo() +
                                " \nEstado actual: " + pedidoAModificar.getEstadoPedido());
                // Mostrar opciones según el estado actual
                mostrarOpcionesEstado(pedidoAModificar, scanner);
                continuar = false;
            }
        }
    }

    /**
     * Implementación del método abstracto de Usuario
     * Permite al repartidor gestionar sus pedidos asignados
     * 
     * @param pedidos Lista de todos los pedidos del sistema
     */
    @Override
    public void gestionarPedido(Scanner scanner) {
        System.out.println("\n===== GESTIÓN DE PEDIDOS - REPARTIDOR =====");
        System.out.println("Repartidor: " + this.getNombre() + " " + this.getApellido());
        System.out.println("Empresa: " + this.getNombreEmpresa());

        
        boolean continuar = true;
        do {
            consultarPedidosAsignados();
            // Opción para cambiar estado de pedidos
            System.out.print("\n¿Desea cambiar el estado de algún pedido? (s/n): ");
            String respuesta = scanner.nextLine().toLowerCase().trim();
            if (respuesta.equals("s") || respuesta.equals("si")) {
                cambiarEstadoPedido(this, scanner);
            }else if(respuesta.equalsIgnoreCase("n") || respuesta.equalsIgnoreCase("no")){
                continuar = false;
            }
            else {
                System.out.println("!Escriba una respuesta valida!");
                System.out.println("O escriba \"salir\" para cancelar");
                System.out.println("Oprima ENTER para continuar intentandolo");
                System.out.print("Tu respuesta: ");
                String salir = scanner.nextLine().trim();
                if(salir.equalsIgnoreCase("salir")){
                    continuar = false;
                }
            }
        } while (continuar);

    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
