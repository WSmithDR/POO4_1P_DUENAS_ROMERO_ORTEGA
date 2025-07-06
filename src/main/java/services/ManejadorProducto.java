package services;

import model.Producto;
import model.Enums.CategoriaProducto;
import persistence.ManejoArchivos;

import java.util.ArrayList;

/**
 * Clase que maneja la carga y gestión de productos
 */
public class ManejadorProducto {
    private static final String PRODUCTOS_FILE = "resources/Productos.txt";

    /**
     * Carga todos los productos desde el archivo
     * @return ArrayList con todos los productos
     */
    public static ArrayList<Producto> cargarProductos() {
        ArrayList<Producto> productos = new ArrayList<>();
        
        try {
            ArrayList<String> lineas = ManejoArchivos.LeeFichero(PRODUCTOS_FILE);
            
            // Saltar la primera línea (encabezado)
            for (int i = 1; i < lineas.size(); i++) {
                String linea = lineas.get(i);
                String[] partes = linea.split("\\|");
                
                String codigo = partes[0];
                String nombre = partes[1];
                double precio = Double.parseDouble(partes[2]);
                CategoriaProducto categoria = CategoriaProducto.valueOf(partes[3].toUpperCase());
                int stock = Integer.parseInt(partes[4]);
                
                Producto producto = new Producto(codigo, nombre, precio, categoria, stock);
                productos.add(producto);
            }
        } catch (Exception e) {
            System.out.println("Error cargando productos: " + e.getMessage());
        }
        
        return productos;
    }

    /**
     * Obtiene productos por categoría
     * @param productos Lista de todos los productos
     * @param categoria Categoría a filtrar
     * @return ArrayList con productos de la categoría especificada
     */
    public static ArrayList<Producto> obtenerProductosPorCategoria(ArrayList<Producto> productos, CategoriaProducto categoria) {
        ArrayList<Producto> productosFiltrados = new ArrayList<>();
        
        for (Producto producto : productos) {
            if (producto.getCategoria() == categoria) {
                productosFiltrados.add(producto);
            }
        }
        
        return productosFiltrados;
    }

    /**
     * Busca un producto por código
     * @param productos Lista de productos
     * @param codigo Código del producto a buscar
     * @return Producto encontrado o null si no existe
     */
    public static Producto buscarProductoPorCodigo(ArrayList<Producto> productos, String codigo) {
        for (Producto producto : productos) {
            if (producto.getCodigo().equals(codigo)) {
                return producto;
            }
        }
        return null;
    }
} 