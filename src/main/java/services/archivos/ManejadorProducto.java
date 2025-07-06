package services.archivos;

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
     * Retorna una lista de productos con stock mayor a cero de Productos.txt.
     * @param productos Lista de productos a filtrar
     * @return ArrayList<Producto> con stock > 0
     */
    public static ArrayList<Producto> filtrarProductosConStock(ArrayList<Producto> productos) {
        ArrayList<Producto> productosConStock = new ArrayList<>();
        for (Producto producto : productos) {
            if (producto.getStock() > 0) {
                productosConStock.add(producto);
            }
        }
        return productosConStock;
    }

    /**
     * Obtiene y muestra por consola las categorías de productos que tienen al menos un producto disponible en la base de datos (Productos.txt).
     * Solo se listan categorías que realmente tienen productos registrados con stock mayor a cero.
     *
     * @return ArrayList<CategoriaProducto> con las categorías disponibles actualmente en el sistema
     */
    public static ArrayList<CategoriaProducto> mostrarCategoriasDisponibles() {
        System.out.println("\nCategorías disponibles:");
        ArrayList<Producto> productosDB = cargarProductos();
        ArrayList<Producto> productosConStock = filtrarProductosConStock(productosDB);
        ArrayList<CategoriaProducto> categoriasDisponibles = new ArrayList<>();

        for (Producto producto : productosConStock) {
            CategoriaProducto categoria = producto.getCategoriaProducto();
            if (!categoriasDisponibles.contains(categoria)) {
                categoriasDisponibles.add(categoria);
            }
        }

        // Mostrar las categorías encontradas en la consola
        for (int i = 0; i < categoriasDisponibles.size(); i++) {
            System.out.println((i + 1) + ". " + categoriasDisponibles.get(i));
        }

        return categoriasDisponibles;
    }

    /**
     * Obtiene productos por categoría
     * @param productos Lista de todos los productos
     * @param categoria Categoría a filtrar
     * @return ArrayList con productos de la categoría especificada
     */
    public static ArrayList<Producto> obtenerProductosPorCategoria(ArrayList<Producto> productos, CategoriaProducto categoria) {
        ArrayList<Producto> productosConStock = filtrarProductosConStock(productos);
        ArrayList<Producto> productosFiltrados = new ArrayList<>();
        for (Producto producto : productosConStock) {
            if (producto.getCategoriaProducto() == categoria) {
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

       /*public void mostrarCategoriaProducto(){
        System.out.println(categoriaProducto);
    }*/
} 