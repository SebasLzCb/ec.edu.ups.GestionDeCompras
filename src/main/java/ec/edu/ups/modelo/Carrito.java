package ec.edu.ups.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

/**
 * La clase {@code Carrito} representa un carrito de compras en el sistema.
 * Contiene información sobre los productos añadidos (ítems), la fecha de creación,
 * el usuario al que pertenece y métodos para calcular subtotales, IVA y el total.
 * Implementa {@link Serializable} para permitir la persistencia en archivos binarios.
 */
public class Carrito implements Serializable {

    // Constante para el porcentaje de IVA aplicado a los productos
    private final double IVA = 0.12;
    // Contador estático para asignar códigos únicos a cada nuevo carrito
    private static int contador = 1;

    // Código único del carrito
    private int codigo;
    // Fecha y hora de creación del carrito
    private GregorianCalendar fechaCreacion;
    // Lista de ítems de productos que contiene el carrito
    private List<ItemCarrito> items;
    // Usuario al que pertenece este carrito
    private Usuario usuario;

    /**
     * Constructor predeterminado de la clase {@code Carrito}.
     * Inicializa un nuevo carrito asignándole un código único, una lista vacía de ítems,
     * y la fecha de creación actual.
     */
    public Carrito() {
        this.codigo = contador++; // Asigna un código único e incrementa el contador
        this.items = new ArrayList<>(); // Inicializa la lista de ítems vacía
        this.fechaCreacion = new GregorianCalendar(); // Establece la fecha de creación actual
    }

    /**
     * Obtiene el código único de este carrito.
     *
     * @return El código del carrito.
     */
    public int getCodigo() {
        return codigo;
    }

    /**
     * Establece el código único para este carrito.
     *
     * @param codigo El nuevo código del carrito.
     */
    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    /**
     * Obtiene la fecha y hora de creación de este carrito.
     *
     * @return Un objeto {@link GregorianCalendar} que representa la fecha de creación.
     */
    public GregorianCalendar getFechaCreacion() {
        return fechaCreacion;
    }

    /**
     * Establece la fecha y hora de creación para este carrito.
     *
     * @param fechaCreacion El nuevo objeto {@link GregorianCalendar} para la fecha de creación.
     */
    public void setFechaCreacion(GregorianCalendar fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    /**
     * Obtiene el usuario al que pertenece este carrito.
     *
     * @return El objeto {@link Usuario} asociado a este carrito.
     */
    public Usuario getUsuario() {
        return usuario;
    }

    /**
     * Establece el usuario al que pertenece este carrito.
     *
     * @param usuario El objeto {@link Usuario} a asociar con este carrito.
     */
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    /**
     * Agrega un producto al carrito con la cantidad especificada.
     * Crea un nuevo {@link ItemCarrito} y lo añade a la lista de ítems del carrito.
     *
     * @param producto El {@link Producto} a agregar.
     * @param cantidad La cantidad de unidades del producto.
     */
    public void agregarProducto(Producto producto, int cantidad) {
        items.add(new ItemCarrito(producto, cantidad));
    }

    /**
     * Elimina un producto del carrito utilizando su código.
     * Itera sobre los ítems del carrito y remueve el primer {@link ItemCarrito}
     * cuyo producto coincide con el código especificado.
     *
     * @param codigoProducto El código del producto a eliminar del carrito.
     */
    public void eliminarProducto(int codigoProducto) {
        Iterator<ItemCarrito> it = items.iterator();
        while (it.hasNext()) {
            if (it.next().getProducto().getCodigo() == codigoProducto) {
                it.remove(); // Elimina el elemento actual de la colección
                break; // Sale una vez que el producto es encontrado y eliminado
            }
        }
    }

    /**
     * Vacía completamente el carrito, eliminando todos los ítems de su lista.
     */
    public void vaciarCarrito() {
        items.clear();
    }

    /**
     * Obtiene la lista de todos los ítems de productos en el carrito.
     *
     * @return Una {@link List} de objetos {@link ItemCarrito} que representan los productos en el carrito.
     */
    public List<ItemCarrito> obtenerItems() {
        return items;
    }

    /**
     * Verifica si el carrito está vacío (no contiene ningún ítem).
     *
     * @return {@code true} si el carrito no tiene ítems, {@code false} en caso contrario.
     */
    public boolean estaVacio() {
        return items.isEmpty();
    }

    /**
     * Calcula el subtotal de todos los ítems en el carrito (suma de precio * cantidad para cada ítem).
     * No incluye el IVA.
     *
     * @return El subtotal del carrito.
     */
    public double calcularSubtotal() {
        double subtotal = 0;
        for (ItemCarrito item : items) {
            subtotal += item.getProducto().getPrecio() * item.getCantidad();
        }
        return subtotal;
    }

    /**
     * Calcula el monto del IVA (Impuesto al Valor Agregado) para el subtotal del carrito.
     *
     * @return El monto del IVA.
     */
    public double calcularIVA() {
        return calcularSubtotal() * IVA;
    }

    /**
     * Calcula el total final del carrito, sumando el subtotal y el IVA.
     *
     * @return El total del carrito.
     */
    public double calcularTotal() {
        return calcularSubtotal() + calcularIVA();
    }

    /**
     * Devuelve una representación en cadena de este objeto {@code Carrito}.
     * Incluye el código, la fecha de creación, el nombre de usuario asociado
     * y el total calculado del carrito.
     *
     * @return Una cadena que representa el objeto Carrito.
     */
    @Override
    public String toString() {
        return "Carrito{" +
                "codigo=" + codigo +
                ", fecha=" + fechaCreacion.getTime() +
                ", usuario=" + (usuario != null ? usuario.getUsername() : "N/A") +
                ", total=" + calcularTotal() +
                '}';
    }
}