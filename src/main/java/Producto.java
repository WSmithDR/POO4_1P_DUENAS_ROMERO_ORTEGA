import Enums.CategoriaProducto;
import Enums.Rol;
public class Producto {
 private String nombre;
    private double precio;
    private int stock;
    private String codigo;
    private CategoriaProducto categoria;

    public Producto(String nombre, double precio, int stock, String codigo, CategoriaProducto categoria) {
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
        this.codigo = codigo;
        this.categoria = categoria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public CategoriaProducto getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaProducto categoria) {
        this.categoria = categoria;
    }
   
    public void mostrarCategoria(){
        System.out.println(categoria);
    }

    public void elegirCategoria(Rol rol){
        
    }
    
}

