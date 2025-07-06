package model.Enums;

/**
 * Enum que representa los estados posibles de un pedido
 */
public enum EstadoPedido {
    EN_PREPARACION("En Preparación"),
    EN_CAMINO("En Camino"),
    ENTREGADO("Entregado"),
    CANCELADO("Cancelado");

    private final String descripcion;

    EstadoPedido(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public String toString() {
        return descripcion;
    }
}