package ec.edu.ups.modelo;

import java.io.Serializable;

/**
 * La clase {@code Producto} representa un artículo disponible para la venta en el sistema
 * de carrito de compras. Contiene información básica como un código único, nombre y precio.
 * Implementa {@link Serializable} para permitir su persistencia en archivos binarios.
 */
public class Producto implements Serializable {
    // Código único del producto
    private int codigo;
    // Nombre del producto
    private String nombre;
    // Precio del producto
    private double precio;

    /**
     * Constructor predeterminado de la clase {@code Producto}.
     * Inicializa el código, nombre y precio con valores predeterminados (0, null, 0.0).
     */
    public Producto() {
    }

    /**
     * Constructor para crear una nueva instancia de {@code Producto} con todos sus atributos.
     *
     * @param codigo El código único del producto.
     * @param nombre El nombre del producto.
     * @param precio El precio del producto.
     */
    public Producto(int codigo, String nombre, double precio) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.precio = precio;
    }

    /**
     * Establece el código único del producto.
     *
     * @param codigo El nuevo código del producto.
     */
    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    /**
     * Establece el nombre del producto.
     *
     * @param nombre El nuevo nombre del producto.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Establece el precio del producto.
     *
     * @param precio El nuevo precio del producto.
     */
    public void setPrecio(double precio) {
        this.precio = precio;
    }

    /**
     * Obtiene el código único del producto.
     *
     * @return El código del producto.
     */
    public int getCodigo() {
        return codigo;
    }

    /**
     * Obtiene el nombre del producto.
     *
     * @return El nombre del producto.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Obtiene el precio del producto.
     *
     * @return El precio del producto.
     */
    public double getPrecio() {
        return precio;
    }

    /**
     * Devuelve una representación en cadena de este objeto {@code Producto}.
     * El formato es "código - nombre - $precio".
     *
     * @return Una cadena que representa el objeto Producto.
     */
    @Override
    public String toString() {
        return codigo + " - " + nombre + " - $" + precio;
    }

}