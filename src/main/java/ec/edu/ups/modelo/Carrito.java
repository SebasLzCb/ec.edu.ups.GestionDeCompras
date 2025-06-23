// src/main/java/ec/edu/ups/modelo/Carrito.java
package ec.edu.ups.modelo;

import java.util.ArrayList;
import java.util.List;

public class Carrito {
    private int codigo;
    private Usuario usuario;
    private final List<ItemCarrito> items = new ArrayList<>();
    private static final double IVA_RATE = 0.12;

    public Carrito(int codigo, Usuario usuario) {
        this.codigo  = codigo;
        this.usuario = usuario;
    }

    public int getCodigo() { return codigo; }
    public Usuario getUsuario() { return usuario; }

    public void agregarProducto(Producto p, int cantidad) {
        for (ItemCarrito it : items) {
            if (it.getProducto().getCodigo() == p.getCodigo()) {
                it.setCantidad(it.getCantidad() + cantidad);
                return;
            }
        }
        items.add(new ItemCarrito(p, cantidad));
    }

    public List<ItemCarrito> obtenerItems() {
        return items;
    }

    public double calcularSubtotal() {
        return items.stream()
                .mapToDouble(ItemCarrito::getSubtotal)
                .sum();
    }

    public double calcularIVA() {
        return calcularSubtotal() * IVA_RATE;
    }

    public double calcularTotal() {
        return calcularSubtotal() + calcularIVA();
    }
}
