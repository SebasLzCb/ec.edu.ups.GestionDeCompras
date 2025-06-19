package ec.edu.ups.controlador;

import ec.edu.ups.modelo.Producto;
import ec.edu.ups.vista.CarritoAñadirView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

public class CarritoController {

    private final CarritoAñadirView view;
    private final ProductoController prodCtrl;
    private final List<ItemCarrito> carritoItems = new ArrayList<>();

    public CarritoController(CarritoAñadirView view, ProductoController prodCtrl) {
        this.view = view;
        this.prodCtrl = prodCtrl;
        configurarListeners();
        actualizarTotales();
    }

    private void configurarListeners() {
        view.getBtnBuscar().addActionListener(e -> buscarProducto());
        view.getBtnAñadir().addActionListener(e -> añadirAlCarrito());
        view.getBtnLimpiar().addActionListener(e -> limpiarCampos());
        view.getBtnGuardar().addActionListener(e -> guardarCarrito());
    }

    private void buscarProducto() {
        try {
            int codigo = Integer.parseInt(view.getTxtCodigo().getText().trim());
            Producto p = prodCtrl.buscarPorCodigo(codigo);
            if (p != null) {
                view.getTxtNombre().setText(p.getNombre());
                view.getTxtPrecio().setText(String.valueOf(p.getPrecio()));
            } else {
                JOptionPane.showMessageDialog(view, "Producto no encontrado");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(view, "Código inválido");
        }
    }

    private void añadirAlCarrito() {
        try {
            int codigo   = Integer.parseInt(view.getTxtCodigo().getText().trim());
            Producto p   = prodCtrl.buscarPorCodigo(codigo);
            int cantidad = Integer.parseInt(view.getTxtCantidad().getText().trim());
            if (p == null) {
                JOptionPane.showMessageDialog(view, "Busca un producto válido primero");
                return;
            }
            if (cantidad <= 0) throw new NumberFormatException();

            DefaultTableModel m = view.getTableModel();
            m.addRow(new Object[]{p.getCodigo(), p.getNombre(), p.getPrecio(), cantidad});
            carritoItems.add(new ItemCarrito(p.getCodigo(), p.getNombre(), p.getPrecio(), cantidad));
            actualizarTotales();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(view, "Cantidad inválida");
        }
    }

    private void limpiarCampos() {
        view.getTxtCodigo().setText("");
        view.getTxtNombre().setText("");
        view.getTxtPrecio().setText("");
        view.getTxtCantidad().setText("");
        actualizarTotales();
    }

    private void guardarCarrito() {
        if (carritoItems.isEmpty()) {
            JOptionPane.showMessageDialog(view, "El carrito está vacío");
            return;
        }
        StringBuilder sb = new StringBuilder();
        carritoItems.forEach(it -> sb.append(it.cantidad)
                .append("× ")
                .append(it.nombre)
                .append("\n"));
        JOptionPane.showMessageDialog(view,
                "Guardados " + carritoItems.size() + " ítems en el carrito:\n" + sb.toString()
        );
        view.getTableModel().setRowCount(0);
        carritoItems.clear();
        actualizarTotales();
    }

    private void actualizarTotales() {
        double subtotal = 0;
        DefaultTableModel m = view.getTableModel();
        for (int i = 0; i < m.getRowCount(); i++) {
            double precio = Double.parseDouble(m.getValueAt(i, 2).toString());
            int cantidad  = Integer.parseInt(m.getValueAt(i, 3).toString());
            subtotal     += precio * cantidad;
        }
        double iva   = subtotal * 0.12;
        double total = subtotal + iva;
        view.getTxtSubtotal().setText(String.format("%.2f", subtotal));
        view.getTxtIva().setText(String.format("%.2f", iva));
        view.getTxtTotal().setText(String.format("%.2f", total));
    }

    private static class ItemCarrito {
        final int codigo;
        final String nombre;
        final double precio;
        final int cantidad;
        ItemCarrito(int c, String n, double p, int q) {
            codigo = c; nombre = n; precio = p; cantidad = q;
        }
    }
}
