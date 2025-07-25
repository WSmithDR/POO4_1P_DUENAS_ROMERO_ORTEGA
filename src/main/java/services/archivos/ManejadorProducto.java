package services.archivos;

import model.Producto;
import model.Enums.CategoriaProducto;

import java.util.ArrayList;
import java.util.Locale;
import utils.Printers;


public class ManejadorProducto {
    private static final String PRODUCTOS_FILE = "database/Productos.txt";

    /**
     * Carga todos los productos desde el archivo de productos.
     * @return Lista de productos cargados
     */
    public static ArrayList<Producto> cargarProductos() {
        ArrayList<String> lineas = ManejadorArchivos.LeeFichero(PRODUCTOS_FILE);
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
                    CategoriaProducto categoria = CategoriaProducto.valueOf(partes[1]);
                    if (categoria == null)
                        continue;
                    String nombre = partes[2];
                    double precio = Double.parseDouble(partes[3]);
                    int stock = Integer.parseInt(partes[4]);
                    Producto producto = new Producto(codigo, nombre, precio, categoria, stock);
                    productos.add(producto);
                    codigosAgregados.add(codigo);
                }
            }
        }
        return productos;
    }

    /**
     * Retorna una lista de productos con stock mayor a cero.
     * @param productos Lista de productos a filtrar
     * @return Lista de productos con stock > 0
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
     * Obtiene y muestra por consola las categorías de productos que tienen al menos un producto disponible.
     * @return Lista de categorías disponibles actualmente en el sistema
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
        Printers.printTitle("CATEGORÍAS DISPONIBLES");
        
        if (categoriasDisponibles.isEmpty()) {
            Printers.printInfo("NO hay categorias que mostrar");
            Printers.printInfo("Todos los productos estan fuera de stock");
        }else{
            for (int i = 0; i < categoriasDisponibles.size(); i++) {
                System.out.println((i + 1) + ". " + categoriasDisponibles.get(i));
            }
        }

        return categoriasDisponibles;
    }

    /**
     * Obtiene productos por categoría.
     * @param productos Lista de todos los productos
     * @param categoria Categoría a filtrar
     * @return Lista de productos de la categoría especificada
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
     * Busca un producto por código.
     * @param codigo Código del producto a buscar
     * @return Producto encontrado o null si no existe
     */
    public static Producto buscarProductoPorCodigo(String codigo) {
        for (Producto producto : cargarProductos()) {
            if (producto.getCodigo().equalsIgnoreCase(codigo)) {
                return producto;
            }
        }
        return null;
    }

    /**
     * Actualiza el stock de un producto en el archivo de productos.
     * @param productoActualizado El producto con el stock actualizado que se va a registrar en el archivo
     */
    public static void actualizarStockProductoEnArchivo(Producto productoActualizado) {
        String nuevaLinea = String.format(Locale.US, "%s|%s|%s|%.2f|%d",
                productoActualizado.getCodigo(),
                productoActualizado.getCategoriaProducto(),
                productoActualizado.getNombre(),
                productoActualizado.getPrecio(),
                productoActualizado.getStock());
        ManejadorArchivos.EscribirArchivo(PRODUCTOS_FILE, nuevaLinea);
    }
}