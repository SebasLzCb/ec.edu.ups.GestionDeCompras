package ec.edu.ups.modelo;

import java.util.ArrayList;
import java.util.List;

public class Carrito {
    private int codigo;
    private List<ItemCarrito> items = new ArrayList<>();

    public Carrito() {}

    public Carrito(int codigo) {
        this.codigo = codigo;
    }

    public int getCodigo() {
        return codigo;
    }
    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public List<ItemCarrito> getItems() {
        return items;
    }
    public void setItems(List<ItemCarrito> items) {
        this.items = items;
    }

    public void agregarItem(ItemCarrito item) {
        this.items.add(item);
    }

    public double getSubtotal() {
        return items.stream()
                .mapToDouble(ItemCarrito::getSubtotal)
                .sum();
    }

    public double getIva() {
        return getSubtotal() * 0.12;
    }

    public double getTotal() {
        return getSubtotal() + getIva();
    }

    @Override
    public String toString() {
        return "Carrito #" + codigo
                + " (items=" + items.size()
                + ", subtotal=" + getSubtotal() + ")";
    }
}
