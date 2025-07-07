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
     * Convierte un string de categoría a su enum correspondiente
     */
    private static CategoriaProducto convertirStringACategoria(String categoriaString) {
        // Mapeo directo de las categorías del archivo
        switch (categoriaString.trim()) {
            case "Tecnología":
            case "TECNOLOGIA":
                return CategoriaProducto.TECNOLOGIA;
            case "Ropa":
            case "ROPA":
                return CategoriaProducto.ROPA;
            case "Deportes":
            case "DEPORTE":
                return CategoriaProducto.DEPORTE;
            case "Hogar":
            case "HOGAR":
                return CategoriaProducto.HOGAR;
            default:
                System.out.println("Categoría no reconocida: " + categoriaString + ", usando TECNOLOGIA por defecto");
                return CategoriaProducto.TECNOLOGIA;
        }
    }

    /**
     * Carga todos los productos desde el archivo
     * 
     * @return ArrayList con todos los productos
     */
    public static ArrayList<Producto> cargarProductos() {
        ArrayList<String> lineas = ManejoArchivos.LeeFichero(PRODUCTOS_FILE);
        ArrayList<Producto> productos = new ArrayList<>();
        ArrayList<String> codigosAgregados = new ArrayList<>();

        // Empezamos desde la última línea hacia la primera (salta encabezado si
        // existe).
        // Esto lo hacemos ya que solo se tomara la ultima version de un producto
        // ya que estaremos agregando el mismo producto pero con un stock diferente.
        for (int i = lineas.size() - 1; i > 0; i--) {
            String linea = lineas.get(i);
            String[] partes = linea.split("\\|");
            if (partes.length >= 5) {
                String codigo = partes[0];
                if (!codigosAgregados.contains(codigo)) {
                    try {
                        // Intentar el formato: Código|Categoría|Nombre|Precio|Stock
                        String categoriaStr = partes[1];
                        String nombre = partes[2];
                        double precio = Double.parseDouble(partes[3]);
                        int stock = Integer.parseInt(partes[4]);
                        
                        // Convertir la categoría del archivo al enum
                        CategoriaProducto categoria = convertirStringACategoria(categoriaStr);
                        
                        Producto producto = new Producto(codigo, nombre, precio, categoria, stock);
                        productos.add(producto);
                        codigosAgregados.add(codigo);
                    } catch (NumberFormatException e) {
                        // Si falla, intentar el formato: Código|Nombre|Precio|Categoría|Stock
                        try {
                            String nombre = partes[1];
                            double precio = Double.parseDouble(partes[2]);
                            String categoriaStr = partes[3];
                            int stock = Integer.parseInt(partes[4]);
                            
                            // Convertir la categoría del archivo al enum
                            CategoriaProducto categoria = convertirStringACategoria(categoriaStr);
                            
                            Producto producto = new Producto(codigo, nombre, precio, categoria, stock);
                            productos.add(producto);
                            codigosAgregados.add(codigo);
                        } catch (NumberFormatException e2) {
                            // Si ambos formatos fallan, saltar esta línea
                            System.out.println("Formato no reconocido en línea: " + linea);
                        }
                    }
                }
            }
        }
        return productos;
    }

    /**
     * Retorna una lista de productos con stock mayor a cero de Productos.txt.
     * 
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
     * Obtiene y muestra por consola las categorías de productos que tienen al menos
     * un producto disponible en la base de datos (Productos.txt).
     * Solo se listan categorías que realmente tienen productos registrados con
     * stock mayor a cero.
     *
     * @return ArrayList<CategoriaProducto> con las categorías disponibles
     *         actualmente en el sistema
     */
    public static ArrayList<CategoriaProducto> mostrarCategoriasDisponibles() {
        ArrayList<Producto> productosDB = cargarProductos();
        ArrayList<Producto> productosConStock = filtrarProductosConStock(productosDB);
        ArrayList<CategoriaProducto> categoriasDisponibles = new ArrayList<>();

        for (Producto producto : productosConStock) {
            CategoriaProducto categoria = producto.getCategoriaProducto();
            if (!categoriasDisponibles.contains(categoria)) {
                categoriasDisponibles.add(categoria);
            }
        }
        if (categoriasDisponibles.size() > 0) {
            System.out.println("\nCategorías disponibles:");
            // Mostrar las categorías encontradas en la consola
            for (int i = 0; i < categoriasDisponibles.size(); i++) {
                CategoriaProducto categoria = categoriasDisponibles.get(i);
                System.out.println((i + 1) + ". " + categoria.getDescripcion());
            }
        } else {
            System.out.println("NO hay categorias que mostrar");
            System.out.println("Todos los productos estan fuera de stock");
        }

        return categoriasDisponibles;
    }

    /**
     * Obtiene productos por categoría
     * 
     * @param productos Lista de todos los productos
     * @param categoria Categoría a filtrar
     * @return ArrayList con productos de la categoría especificada
     */
    public static ArrayList<Producto> obtenerProductosPorCategoria(ArrayList<Producto> productos,
            CategoriaProducto categoria) {
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
     * 
     * @param productos Lista de productos
     * @param codigo    Código del producto a buscar
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

    public static void actualizarStockProductoEnArchivo(Producto productoActualizado) {
        String nuevaLinea = String.format("%s|%s|%s|%s|%d",
                productoActualizado.getCodigo(),
                productoActualizado.getNombre(),
                productoActualizado.getPrecio(),
                productoActualizado.getCategoriaProducto().getName(),
                productoActualizado.getStock());
        ManejoArchivos.EscribirArchivo(PRODUCTOS_FILE, nuevaLinea);
    }
}