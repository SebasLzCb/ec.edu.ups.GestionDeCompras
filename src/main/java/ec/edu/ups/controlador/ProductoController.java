package ec.edu.ups.controlador;

import ec.edu.ups.dao.ProductoDAO;
import ec.edu.ups.modelo.Producto;
import ec.edu.ups.vista.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.util.stream.Collectors;

public class ProductoController {

    private final ProductoView productoView;
    private final ProductoDAO productoDAO;
    private ProductoListaView productoListaView;
    private ProductoModView productoMod;
    private ProductoElimView productoElimView;
    private CarritoAñadirView carritoView;

    public ProductoController(ProductoView productoView,
                              ProductoDAO productoDAO,
                              ProductoListaView productoListaView) {
        this.productoView = productoView;
        this.productoDAO = productoDAO;
        setProductoListaView(productoListaView);
        configurarAlta();
    }

    public void limpiarCamposAlta() {
        productoView.limpiarCampos();
    }

    public void añadirProducto(int codigo, String nombre, double precio) {
        productoDAO.crear(new Producto(codigo, nombre, precio));
        productoView.mostrarMensaje("Producto añadido correctamente");
        refrescarTodo();
    }

    public void actualizarProducto(int codigo, String nombre, double precio) {
        productoDAO.actualizar(new Producto(codigo, nombre, precio));
        JOptionPane.showMessageDialog(null, "Producto actualizado");
        refrescarTodo();
    }

    public void eliminarProducto(int codigo) {
        productoDAO.eliminar(codigo);
        JOptionPane.showMessageDialog(null, "Producto eliminado");
        refrescarTodo();
    }

    public List<Producto> listarProductos() {
        return productoDAO.listarTodos();
    }

    public Producto buscarPorCodigo(int codigo) {
        return productoDAO.buscarPorCodigo(codigo);
    }

    public List<Producto> buscarPorNombreParcial(String texto) {
        String minus = texto.toLowerCase();
        return productoDAO.listarTodos().stream()
                .filter(p -> p.getNombre().toLowerCase().startsWith(minus))
                .collect(Collectors.toList());
    }

    public void mostrarProductoEnCarrito(Producto p, int cantidad) {
        DefaultTableModel m = carritoView.getTableModel();
        m.addRow(new Object[]{
                p.getCodigo(),
                p.getNombre(),
                p.getPrecio(),
                cantidad
        });
    }

    private void configurarAlta() {
        productoView.getBtnAceptar().addActionListener(e -> {
            try {
                int c = Integer.parseInt(productoView.getTxtCodigo().getText());
                String n = productoView.getTxtNombre().getText();
                double pr = Double.parseDouble(productoView.getTxtPrecio().getText());
                añadirProducto(c, n, pr);
                limpiarCamposAlta();
            } catch (NumberFormatException ex) {
                productoView.mostrarMensaje("Por favor ingresa valores numéricos válidos.");
            }
        });
    }

    public void setProductoListaView(ProductoListaView view) {
        this.productoListaView = view;
        view.getBtnRefrescar().addActionListener(e -> {
            view.listarProductos(listarProductos());
        });
        view.getBtnBuscar().addActionListener(e -> {
            String criterio = view.getComboFiltro().getSelectedItem().toString();
            String texto    = view.getTxtBusqueda().getText().trim();
            if ("Código".equals(criterio)) {
                try {
                    Producto p = buscarPorCodigo(Integer.parseInt(texto));
                    view.listarProductos(p != null ? List.of(p) : List.of());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(view, "Código inválido");
                }
            } else {
                view.listarProductos(buscarPorNombreParcial(texto));
            }
        });
        view.listarProductos(listarProductos());
    }

    public void setProductoMod(ProductoModView view) {
        this.productoMod = view;
        view.getBtnActualizar().addActionListener(e -> {
            int row = view.getTableProductos().getSelectedRow();
            if (row < 0) return;
            int c = (int) view.getTableProductos().getValueAt(row, 0);
            String n = (String) view.getTableProductos().getValueAt(row, 1);
            double pr = Double.parseDouble(view.getTableProductos().getValueAt(row, 2).toString());
            actualizarProducto(c, n, pr);
        });
        view.getBtnRefrescar().addActionListener(e -> {
            view.listarProductos(listarProductos());
        });
        view.listarProductos(listarProductos());
    }

    public void setProductoElimView(ProductoElimView view) {
        this.productoElimView = view;
        view.getBtnBuscar().addActionListener(e -> {
            DefaultTableModel m = view.getModelResultado();
            m.setRowCount(0);
            String criterio = view.getComboFiltro().getSelectedItem().toString();
            String texto    = view.getTxtBusqueda().getText().trim();
            if ("Código".equals(criterio)) {
                try {
                    Producto p = buscarPorCodigo(Integer.parseInt(texto));
                    if (p != null) m.addRow(new Object[]{p.getCodigo(), p.getNombre(), p.getPrecio()});
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(view, "Código inválido");
                }
            } else {
                buscarPorNombreParcial(texto).forEach(p ->
                        m.addRow(new Object[]{p.getCodigo(), p.getNombre(), p.getPrecio()})
                );
            }
        });
        view.getBtnEliminar().addActionListener(e -> {
            int row = view.getTablaResultado().getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(view, "Selecciona un producto");
                return;
            }
            int c = (int) view.getModelResultado().getValueAt(row, 0);
            eliminarProducto(c);
            view.getModelResultado().removeRow(row);
        });
    }

    public void setCarritoView(CarritoAñadirView view) {
        this.carritoView = view;
        view.getBtnBuscar().addActionListener(e -> {
            try {
                int c = Integer.parseInt(view.getTxtCodigo().getText());
                Producto p = buscarPorCodigo(c);
                if (p != null) {
                    view.getTxtNombre().setText(p.getNombre());
                    view.getTxtPrecio().setText(Double.toString(p.getPrecio()));
                }
            } catch (NumberFormatException ex) { /*ignore*/ }
        });
        view.getBtnAñadir().addActionListener(e -> {
            Producto p = buscarPorCodigo(Integer.parseInt(view.getTxtCodigo().getText()));
            int cantidad = Integer.parseInt(view.getTxtCantidad().getText());
            if (p != null) mostrarProductoEnCarrito(p, cantidad);
        });
        view.getBtnLimpiar().addActionListener(e -> {
            view.getTxtCodigo().setText("");
            view.getTxtNombre().setText("");
            view.getTxtPrecio().setText("");
            view.getTxtCantidad().setText("");
        });
        view.getBtnGuardar().addActionListener(e -> {
            JOptionPane.showMessageDialog(view, "Elementos guardados en el carrito.");
        });
    }

    private void refrescarTodo() {
        if (productoListaView != null) productoListaView.listarProductos(listarProductos());
        if (productoMod       != null) productoMod.listarProductos(listarProductos());
        if (productoElimView  != null) productoElimView.getBtnBuscar().doClick();
    }
}