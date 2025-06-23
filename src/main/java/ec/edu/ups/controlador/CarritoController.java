// src/main/java/ec/edu/ups/controlador/CarritoController.java
package ec.edu.ups.controlador;

import ec.edu.ups.dao.CarritoDAO;
import ec.edu.ups.dao.ProductoDAO;
import ec.edu.ups.modelo.Carrito;
import ec.edu.ups.modelo.ItemCarrito;
import ec.edu.ups.modelo.Producto;
import ec.edu.ups.modelo.Usuario;
import ec.edu.ups.vista.Carrito.CarritoAñadirView;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class CarritoController {

    private final CarritoAñadirView view;
    private final ProductoDAO productoDAO;
    private final CarritoDAO carritoDAO;
    private final Carrito carrito;

    public CarritoController(CarritoAñadirView view,
                             ProductoDAO productoDAO,
                             CarritoDAO carritoDAO,
                             Usuario usuarioActual) {
        this.view        = view;
        this.productoDAO = productoDAO;
        this.carritoDAO  = carritoDAO;
        this.carrito     = new Carrito(1, usuarioActual);

        configurarListeners();
        cargarTabla();
    }

    private void configurarListeners() {
        view.getBtnBuscar().addActionListener(e -> buscarProducto());
        view.getBtnAñadir().addActionListener(e -> anadirProducto());
        view.getBtnLimpiar().addActionListener(e -> limpiarCampos());
        view.getBtnGuardar().addActionListener(e -> guardarCarrito());
    }

    private void buscarProducto() {
        try {
            int codigo = Integer.parseInt(view.getTxtCodigo().getText().trim());
            Producto p = productoDAO.buscarPorCodigo(codigo);
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

    private void anadirProducto() {
        try {
            int codigo = Integer.parseInt(view.getTxtCodigo().getText().trim());
            Producto producto = productoDAO.buscarPorCodigo(codigo);
            if (producto == null) {
                JOptionPane.showMessageDialog(view, "Busca primero un producto válido");
                return;
            }
            int cantidad = Integer.parseInt(
                    view.getCbxCantidad()
                            .getSelectedItem()
                            .toString()
            );
            carrito.agregarProducto(producto, cantidad);
            cargarTabla();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(view, "Cantidad inválida");
        }
    }

    private void cargarTabla() {
        DefaultTableModel m = (DefaultTableModel)view.getTableCarrito().getModel();
        m.setRowCount(0);
        for (ItemCarrito it : carrito.obtenerItems()) {
            m.addRow(new Object[]{
                    it.getProducto().getCodigo(),
                    it.getProducto().getNombre(),
                    it.getCantidad(),
                    it.getSubtotal()
            });
        }
        mostrarTotales();
    }

    private void mostrarTotales() {
        view.getTxtSubtotal().setText(String.format("%.2f", carrito.calcularSubtotal()));
        view.getTxtIva().setText    (String.format("%.2f", carrito.calcularIVA()));
        view.getTxtTotal().setText  (String.format("%.2f", carrito.calcularTotal()));
    }

    private void limpiarCampos() {
        view.getTxtCodigo().setText("");
        view.getTxtNombre().setText("");
        view.getTxtPrecio().setText("");
        view.getCbxCantidad().setSelectedIndex(0);
        cargarTabla();
    }

    private void guardarCarrito() {
        if (carrito.obtenerItems().isEmpty()) {
            JOptionPane.showMessageDialog(view, "El carrito está vacío");
            return;
        }
        carritoDAO.crear(carrito);
        JOptionPane.showMessageDialog(
                view,
                "Guardados " + carrito.obtenerItems().size() + " ítems en el carrito"
        );
        carrito.obtenerItems().clear();
        cargarTabla();
    }
}

