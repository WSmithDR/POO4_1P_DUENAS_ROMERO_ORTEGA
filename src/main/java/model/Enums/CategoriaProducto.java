package model.Enums;

public enum CategoriaProducto {
    TECNOLOGIA("Tecnología"),
    ROPA("Ropa"),
    DEPORTES("Deportes"),
    HOGAR("Hogar");

    private final String descripcion;

    CategoriaProducto(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public static CategoriaProducto fromDescripcion(String descripcion) {
        for (CategoriaProducto categoria : CategoriaProducto.values()) {
            if (categoria.getDescripcion().equalsIgnoreCase(descripcion)) {
                return categoria;
            }
        }
        System.out.println("[ADVERTENCIA] No se encontró una categoría para la descripción: " + descripcion);
        return null;
    }

    @Override
    public String toString() {
        return descripcion;
    }
}
