package model.Enums;

/**
 * Enum que representa los estados posibles de un pedido en el sistema.
 * Cada estado tiene una descripción amigable para mostrar al usuario.
 */
public enum EstadoPedido {
    EN_PREPARACION("En Preparación"),
    EN_CAMINO("En Camino"),
    ENTREGADO("Entregado"),
    CANCELADO("Cancelado");

    private final String descripcion;

    /**
     * Constructor del enum EstadoPedido.
     * @param descripcion forma en como se va a prensetar en la consola y guardar en el archivo
     */
    EstadoPedido(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Obtiene la descripción amigable del estado del pedido.
     * @return Descripción del estado
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Devuelve la descripción del estado como representación en String.
     * @return Descripción del estado
     */
    @Override
    public String toString() {
        return descripcion;
    }
}