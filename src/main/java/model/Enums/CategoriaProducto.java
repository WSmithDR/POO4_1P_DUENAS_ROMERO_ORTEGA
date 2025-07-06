package model.Enums;

public enum CategoriaProducto {
    ROPA("ROPA","Ropa y Vestimenta"),
    TECNOLOGIA("TECNOLOGIA","Tecnología"),
    DEPORTE("DEPORTE","Deportes"),
    HOGAR("HOGAR","Hogar y Jardín");

    private final String name;
    private final String descripcion;

    CategoriaProducto(String name, String descripcion) {
        this.name = name;
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getName(){
        return name;
    }

    @Override
    public String toString() {
        return descripcion;
    }
}
