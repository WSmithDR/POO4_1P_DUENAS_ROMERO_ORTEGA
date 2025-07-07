package model;

import model.Enums.CategoriaProducto;

/**
 * Clase que representa un producto en el sistema
 */
public class Producto {
    private String codigo;
    private String nombre;
    private double precio;
    private CategoriaProducto categoriaProducto;
    private int stock;

    /**
     * Constructor de la clase Producto.
     * @param codigo Código único del producto
     * @param nombre Nombre del producto
     * @param precio Precio del producto
     * @param categoriaProducto Categoría del producto
     * @param stock Cantidad disponible en stock
     */
    public Producto(String codigo, String nombre, double precio, CategoriaProducto categoriaProducto, int stock) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.precio = precio;
        this.categoriaProducto = categoriaProducto;
        this.stock = stock;
    }

    /**
     * Obtiene el código único del producto.
     * @return Código del producto
     */
    public String getCodigo() {
        return codigo;
    }

    /**
     * Obtiene el nombre del producto.
     * @return Nombre del producto
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Obtiene el precio del producto.
     * @return Precio del producto
     */
    public double getPrecio() {
        return precio;
    }

    /**
     * Obtiene la categoría del producto.
     * @return Categoría del producto
     */
    public CategoriaProducto getCategoriaProducto() {
        return categoriaProducto;
    }

    /**
     * Obtiene el stock disponible del producto.
     * @return Stock del producto
     */
    public int getStock() {
        return stock;
    }

    /**
     * Establece el código único del producto.
     * @param codigo Código del producto
     */
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    /**
     * Establece el nombre del producto.
     * @param nombre Nombre del producto
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Establece el precio del producto.
     * @param precio Precio del producto
     */
    public void setPrecio(double precio) {
        this.precio = precio;
    }

    /**
     * Establece la categoría del producto.
     * @param categoria Categoría del producto
     */
    public void setCategoriaProducto(CategoriaProducto categoria) {
        this.categoriaProducto = categoria;
    }

    /**
     * Establece el stock disponible del producto.
     * @param stock Stock del producto
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * Reduce el stock del producto
     * @param cantidad Cantidad a reducir
     * @return true si se pudo reducir, false si no hay suficiente stock
     */
    public boolean reducirStock(int cantidad) {
        if (stock >= cantidad) {
            stock -= cantidad;
            return true;
        }
        return false;
    }

    /**
     * Devuelve una representación en String del producto.
     * @return String con los datos del producto
     */
    @Override
    public String toString() {
        return codigo + " - " + nombre + " - $" + precio + " - Stock: " + stock;
    }

    
}

