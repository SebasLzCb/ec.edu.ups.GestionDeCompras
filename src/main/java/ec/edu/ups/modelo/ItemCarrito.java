package ec.edu.ups.modelo;

import java.io.Serializable;

/**
 * La clase {@code ItemCarrito} representa un producto individual dentro de un carrito de compras,
 * incluyendo el producto en sí y la cantidad de unidades de ese producto.
 * Implementa {@link Serializable} para permitir su persistencia en archivos binarios.
 */
public class ItemCarrito implements Serializable {
    // El producto que forma parte de este ítem del carrito
    private Producto producto;
    // La cantidad de unidades del producto en este ítem
    private int cantidad;

    /**
     * Constructor predeterminado de la clase {@code ItemCarrito}.
     * Inicializa el producto y la cantidad con valores predeterminados (null y 0).
     */
    public ItemCarrito() {
    }

    /**
     * Constructor para crear una nueva instancia de {@code ItemCarrito} con un producto y una cantidad específicos.
     *
     * @param producto El objeto {@link Producto} que se añade al ítem del carrito.
     * @param cantidad La cantidad de unidades de este producto.
     */
    public ItemCarrito(Producto producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
    }

    /**
     * Establece el producto asociado a este ítem del carrito.
     *
     * @param producto El nuevo objeto {@link Producto}.
     */
    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    /**
     * Establece la cantidad de unidades del producto en este ítem del carrito.
     *
     * @param cantidad La nueva cantidad de unidades.
     */
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    /**
     * Obtiene el producto asociado a este ítem del carrito.
     *
     * @return El objeto {@link Producto}.
     */
    public Producto getProducto() {
        return producto;
    }

    /**
     * Obtiene la cantidad de unidades del producto en este ítem del carrito.
     *
     * @return La cantidad de unidades.
     */
    public int getCantidad() {
        return cantidad;
    }

    /**
     * Calcula el subtotal de este ítem, que es el precio del producto multiplicado por la cantidad.
     *
     * @return El subtotal de este ítem del carrito.
     */
    public double getSubtotal() {
        // Asegura que el producto no sea null para evitar NullPointerException
        return (producto != null ? producto.getPrecio() : 0.0) * cantidad;
    }

    /**
     * Devuelve una representación en cadena de este objeto {@code ItemCarrito}.
     * El formato incluye la representación en cadena del producto, la cantidad y el subtotal calculado.
     *
     * @return Una cadena que representa el objeto ItemCarrito.
     */
    @Override
    public String toString() {
        return producto.toString() + " x " + cantidad + " = $" + getSubtotal();
    }

}
