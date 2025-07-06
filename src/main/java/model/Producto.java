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
     * Constructor de la clase Producto
     * @param codigo Código único del producto
     * @param nombre Nombre del producto
     * @param precio Precio del producto
     * @param categoria Categoría del producto
     * @param stock Cantidad disponible en stock
     */
    public Producto(String codigo, String nombre, double precio, CategoriaProducto categoriaProducto, int stock) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.precio = precio;
        this.categoriaProducto = categoriaProducto;
        this.stock = stock;
    }

    // Getters
    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public CategoriaProducto getCategoriaProducto() {
        return categoriaProducto;
    }

    public int getStock() {
        return stock;
    }

    // Setters
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public void setCategoriaProducto(CategoriaProducto categoria) {
        this.categoriaProducto = categoria;
    }

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

    @Override
    public String toString() {
        return codigo + " - " + nombre + " - $" + precio + " - Stock: " + stock;
    }

    
}

