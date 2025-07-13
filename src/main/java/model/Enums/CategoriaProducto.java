package model.Enums;

import utils.Printers;

public enum CategoriaProducto {
    TECNOLOGIA("Tecnología"),
    ROPA("Ropa"),
    DEPORTES("Deportes"),
    HOGAR("Hogar");

    private final String descripcion;

    /**
     * Constructor del enum CategoriaProducto.
     * @param descripcion version de como esta el valor en el archivo Productos.txt
     */
    CategoriaProducto(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Obtiene la descripción amigable de la categoría.
     * @return Descripción de la categoría
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Obtiene la categoría correspondiente a partir de una descripción.
     * Si no se encuentra, imprime una advertencia y retorna null.
     * @param descripcion Descripción de la categoría a buscar
     * @return CategoriaProducto correspondiente o null si no existe
     */
    public static CategoriaProducto fromDescripcion(String descripcion) {
        for (CategoriaProducto categoria : CategoriaProducto.values()) {
            if (categoria.getDescripcion().equalsIgnoreCase(descripcion)) {
                return categoria;
            }
        }
        Printers.printInfo("No se encontró una categoría para la descripción: " + descripcion);
        return null;
    }

    /**
     * Devuelve la descripción de la categoría como representación en String.
     * @return Descripción de la categoría
     */
    @Override
    public String toString() {
        return descripcion;
    }
}
