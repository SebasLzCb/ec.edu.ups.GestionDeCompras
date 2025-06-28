package ec.edu.ups.controlador;

import ec.edu.ups.dao.CarritoDAO;
import ec.edu.ups.dao.ProductoDAO;
import ec.edu.ups.modelo.Carrito;
import ec.edu.ups.modelo.ItemCarrito;
import ec.edu.ups.modelo.Producto;
import ec.edu.ups.modelo.Usuario;
import ec.edu.ups.util.FormateadorUtils;
import ec.edu.ups.vista.Carrito.*;

import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.util.Locale;

public class CarritoController {

    private final CarritoDAO carritoDAO;
    private final ProductoDAO productoDAO;
    private final CarritoAñadirView carritoAñadirView;
    private final CarritoListaView carritoListaView;
    private final CarritoModView carritoModView;
    private final CarritoElimView carritoElimView;
    private final Usuario usuarioActual;
    private Carrito carrito;

    public CarritoController(CarritoDAO carritoDAO, ProductoDAO productoDAO,
                             CarritoAñadirView carritoAñadirView,
                             CarritoListaView carritoListaView,
                             CarritoModView carritoModView,
                             CarritoElimView carritoElimView,
                             Usuario usuarioActual) {

        this.carritoDAO = carritoDAO;
        this.productoDAO = productoDAO;
        this.carritoAñadirView = carritoAñadirView;
        this.carritoListaView = carritoListaView;
        this.carritoModView = carritoModView;
        this.carritoElimView = carritoElimView;
        this.usuarioActual = usuarioActual;
        this.carrito = new Carrito();
        this.carrito.setUsuario(usuarioActual);

        configurarEventos();
    }

    private void configurarEventos() {
        carritoAñadirView.getBtnBuscar().addActionListener(e -> buscarProducto());
        carritoAñadirView.getBtnAnadir().addActionListener(e -> agregarProducto());
        carritoAñadirView.getBtnGuardar().addActionListener(e -> guardarCarrito());
        carritoAñadirView.getBtnLimpiar().addActionListener(e -> limpiarFormulario());

        carritoListaView.getBtnBuscar().addActionListener(e -> buscarCarrito());
        carritoListaView.getBtnListar().addActionListener(e -> listarCarritos());

        carritoElimView.getBtnBuscar().addActionListener(e -> buscarEnEliminar());
        carritoElimView.getBtnEliminar().addActionListener(e -> eliminarCarrito());

        carritoModView.getBtnBuscar().addActionListener(e -> buscarEnModificar());
        carritoModView.getBtnActualizar().addActionListener(e -> actualizarCarrito());
    }

    private void buscarProducto() {
        try {
            int codigo = Integer.parseInt(carritoAñadirView.getTxtCodigo().getText());
            Producto producto = productoDAO.buscarPorCodigo(codigo);
            if (producto != null) {
                carritoAñadirView.getTxtNombre().setText(producto.getNombre());
                carritoAñadirView.getTxtPrecio().setText(String.valueOf(producto.getPrecio()));
            } else {
                carritoAñadirView.mostrarMensaje("Producto no encontrado.");
            }
        } catch (NumberFormatException e) {
            carritoAñadirView.mostrarMensaje("Código inválido.");
        }
    }

    private void agregarProducto() {
        try {
            int codigo = Integer.parseInt(carritoAñadirView.getTxtCodigo().getText());
            int cantidad = Integer.parseInt(carritoAñadirView.getCbxCantidad().getSelectedItem().toString());
            Producto producto = productoDAO.buscarPorCodigo(codigo);

            if (producto == null) {
                carritoAñadirView.mostrarMensaje("Producto inválido.");
                return;
            }

            carrito.agregarProducto(producto, cantidad);
            cargarProductosEnTabla();
            mostrarTotales();

        } catch (NumberFormatException e) {
            carritoAñadirView.mostrarMensaje("Verifique el código o la cantidad.");
        }
    }

    private void cargarProductosEnTabla() {
        DefaultTableModel modelo = (DefaultTableModel) carritoAñadirView.getTblProductos().getModel();
        modelo.setRowCount(0);
        Locale locale = Locale.getDefault();

        for (ItemCarrito item : carrito.obtenerItems()) {
            double subtotal = item.getSubtotal();
            modelo.addRow(new Object[]{
                    item.getProducto().getCodigo(),
                    item.getProducto().getNombre(),
                    item.getProducto().getPrecio(),
                    item.getCantidad(),
                    FormateadorUtils.formatearMoneda(subtotal, locale)
            });
        }
    }

    private void mostrarTotales() {
        Locale locale = Locale.getDefault();
        carritoAñadirView.getTxtSubtotal().setText(FormateadorUtils.formatearMoneda(carrito.calcularSubtotal(), locale));
        carritoAñadirView.getTxtIva().setText(FormateadorUtils.formatearMoneda(carrito.calcularIVA(), locale));
        carritoAñadirView.getTxtTotal().setText(FormateadorUtils.formatearMoneda(carrito.calcularTotal(), locale));
    }

    private void guardarCarrito() {
        if (carrito.estaVacio()) {
            carritoAñadirView.mostrarMensaje("Debe añadir productos antes de guardar.");
            return;
        }
        carritoDAO.crear(carrito);
        carritoAñadirView.mostrarMensaje("Carrito guardado correctamente.");
        carrito = new Carrito();
        carrito.setUsuario(usuarioActual);
        limpiarFormulario();
    }

    private void limpiarFormulario() {
        carritoAñadirView.getTxtCodigo().setText("");
        carritoAñadirView.getTxtNombre().setText("");
        carritoAñadirView.getTxtPrecio().setText("");
        carritoAñadirView.getTxtSubtotal().setText("");
        carritoAñadirView.getTxtIva().setText("");
        carritoAñadirView.getTxtTotal().setText("");
        ((DefaultTableModel) carritoAñadirView.getTblProductos().getModel()).setRowCount(0);
        carrito.vaciarCarrito();
    }

    private void listarCarritos() {
        List<Carrito> carritos = carritoDAO.listarTodos();
        DefaultTableModel model = (DefaultTableModel) carritoListaView.getTablaCarrito().getModel();
        model.setRowCount(0);
        for (Carrito c : carritos) {
            model.addRow(new Object[]{c.getCodigo(), c.getUsuario().getUsername(), c.calcularTotal()});
        }
    }

    private void buscarCarrito() {
        try {
            int codigo = Integer.parseInt(carritoListaView.getTxtCodigo().getText());
            Carrito c = carritoDAO.buscarPorCodigo(codigo);
            if (c != null) {
                DefaultTableModel model = (DefaultTableModel) carritoListaView.getTablaCarrito().getModel();
                model.setRowCount(0);
                model.addRow(new Object[]{c.getCodigo(), c.getUsuario().getUsername(), c.calcularTotal()});
                carritoListaView.getTxtTotal().setText(String.valueOf(c.calcularTotal()));
            } else {
                carritoListaView.mostrarMensaje("Carrito no encontrado.");
            }
        } catch (NumberFormatException e) {
            carritoListaView.mostrarMensaje("Código inválido.");
        }
    }

    private void buscarEnEliminar() {
        try {
            int codigo = Integer.parseInt(carritoElimView.getCodigoIngresado());
            Carrito c = carritoDAO.buscarPorCodigo(codigo);
            carritoElimView.mostrarResultado(c);
        } catch (NumberFormatException e) {
            carritoElimView.mostrarMensaje("Código inválido.");
        }
    }

    private void eliminarCarrito() {
        try {
            int codigo = Integer.parseInt(carritoElimView.getCodigoIngresado());
            carritoDAO.eliminar(codigo);
            carritoElimView.mostrarMensaje("Carrito eliminado.");
            carritoElimView.limpiarCampos();
        } catch (NumberFormatException e) {
            carritoElimView.mostrarMensaje("Código inválido.");
        }
    }

    private void buscarEnModificar() {
        try {
            int codigo = Integer.parseInt(carritoModView.getTxtCodigo().getText());
            Carrito carrito = carritoDAO.buscarPorCodigo(codigo);
            if (carrito != null) {
                carritoModView.getTxtTotal().setText(String.valueOf(carrito.calcularTotal()));
            } else {
                carritoModView.mostrarMensaje("Carrito no encontrado.");
            }
        } catch (NumberFormatException e) {
            carritoModView.mostrarMensaje("Código inválido.");
        }
    }

    private void actualizarCarrito() {
        try {
            int codigo = Integer.parseInt(carritoModView.getTxtCodigo().getText());
            Carrito carrito = carritoDAO.buscarPorCodigo(codigo);
            if (carrito != null) {
                carritoDAO.actualizar(carrito);
                carritoModView.mostrarMensaje("Carrito actualizado.");
            } else {
                carritoModView.mostrarMensaje("Carrito no encontrado.");
            }
        } catch (NumberFormatException e) {
            carritoModView.mostrarMensaje("Código inválido.");
        }
    }
}