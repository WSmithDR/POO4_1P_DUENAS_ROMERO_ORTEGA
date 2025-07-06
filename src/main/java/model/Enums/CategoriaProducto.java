package model.Enums;

public enum CategoriaProducto {
    ROPA("Ropa y Vestimenta"),
    TECNOLOGIA("Tecnología"),
    DEPORTE("Deportes"),
    HOGAR("Hogar y Jardín");

    private final String descripcion;

    CategoriaProducto(String descripcion) {
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
