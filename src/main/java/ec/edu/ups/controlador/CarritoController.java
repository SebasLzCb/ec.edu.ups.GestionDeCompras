package ec.edu.ups.controlador;

import ec.edu.ups.dao.CarritoDAO;
import ec.edu.ups.dao.ProductoDAO;
import ec.edu.ups.modelo.Carrito;
import ec.edu.ups.modelo.ItemCarrito;
import ec.edu.ups.modelo.Producto;
import ec.edu.ups.modelo.Usuario;
import ec.edu.ups.vista.Carrito.CarritoAñadirView;
import ec.edu.ups.vista.Carrito.CarritoListaView;
import ec.edu.ups.vista.Carrito.CarritoModView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class CarritoController {

    private final CarritoDAO carritoDAO;
    private final ProductoDAO productoDAO;
    private final CarritoAñadirView carritoAnadirView;
    private final CarritoListaView carritoListaView;
    private final CarritoModView carritoModView;
    private Carrito carrito;
    private final Usuario usuario;

    public CarritoController(CarritoDAO carritoDAO,
                             ProductoDAO productoDAO,
                             CarritoAñadirView carritoAnadirView,
                             CarritoListaView carritoListaView,
                             CarritoModView carritoModView,
                             Usuario usuario) {
        this.carritoDAO = carritoDAO;
        this.productoDAO = productoDAO;
        this.carritoAnadirView = carritoAnadirView;
        this.carritoListaView = carritoListaView;
        this.carritoModView = carritoModView;
        this.usuario = usuario;
        this.carrito = new Carrito();
        carrito.setUsuario(usuario);
        configurarEventos();
    }

    private void configurarEventos() {
        carritoAnadirView.getBtnAnadir().addActionListener(e -> anadirProducto());

        carritoAnadirView.getBtnGuardar().addActionListener(e -> guardarCarrito());

        carritoListaView.getBtnBuscar().addActionListener(e -> buscarPorCodigo());

        carritoListaView.getBtnListar().addActionListener(e -> listarTodos());

        carritoModView.getBtnBuscar().addActionListener(e -> buscarEnModificar());

        carritoModView.getBtnActualizar().addActionListener(e -> actualizarCarrito());
    }

    private void anadirProducto() {
        try {
            if (carritoAnadirView.getTxtCodigo().getText().isEmpty()) {
                carritoAnadirView.mostrarMensaje("Ingrese un código.");
                return;
            }

            int codigo = Integer.parseInt(carritoAnadirView.getTxtCodigo().getText());
            int cantidad = Integer.parseInt(carritoAnadirView.getCbxCantidad().getSelectedItem().toString());
            Producto producto = productoDAO.buscarPorCodigo(codigo);

            if (producto != null) {
                carrito.agregarProducto(producto, cantidad);
                cargarProductos(carritoAnadirView.getTblProductos(), carrito.obtenerItems());
                mostrarTotales(carritoAnadirView.getTxtSubtotal(), carritoAnadirView.getTxtIva(), carritoAnadirView.getTxtTotal(), carrito);
            } else {
                carritoAnadirView.mostrarMensaje("Producto no encontrado");
            }
        } catch (NumberFormatException ex) {
            carritoAnadirView.mostrarMensaje("Ingrese un código y cantidad válidos.");
        }
    }

    private void guardarCarrito() {
        carritoDAO.crear(carrito);
        carritoAnadirView.mostrarMensaje("Carrito guardado exitosamente");
        carrito = new Carrito();
        carrito.setUsuario(usuario);
    }

    private void buscarPorCodigo() {
        try {
            int codigo = Integer.parseInt(carritoListaView.getTxtCodigo().getText());
            Carrito buscado = carritoDAO.buscarPorCodigo(codigo);

            if (buscado != null) {
                cargarProductos(carritoListaView.getTablaCarrito(), buscado.obtenerItems());
                mostrarTotales(carritoListaView.getTxtTotal(), null, null, buscado);
            } else {
                carritoListaView.mostrarMensaje("Carrito no encontrado");
            }
        } catch (NumberFormatException ex) {
            carritoListaView.mostrarMensaje("Código inválido");
        }
    }

    private void listarTodos() {
        List<Carrito> todos = carritoDAO.listarTodos();
        if (!todos.isEmpty()) {
            Carrito ultimo = todos.get(todos.size() - 1);
            cargarProductos(carritoListaView.getTablaCarrito(), ultimo.obtenerItems());
            mostrarTotales(carritoListaView.getTxtTotal(), null, null, ultimo);
        } else {
            carritoListaView.mostrarMensaje("No hay carritos registrados.");
        }
    }

    private void buscarEnModificar() {
        try {
            int codigo = Integer.parseInt(carritoModView.getTxtCodigo().getText());
            carrito = carritoDAO.buscarPorCodigo(codigo);
            if (carrito != null) {
                cargarProductos(carritoModView.getTable1(), carrito.obtenerItems());
                mostrarTotales(carritoModView.getTxtTotal(), null, null, carrito);
            } else {
                carritoModView.mostrarMensaje("Carrito no encontrado.");
            }
        } catch (NumberFormatException ex) {
            carritoModView.mostrarMensaje("Código inválido.");
        }
    }

    private void actualizarCarrito() {
        if (carrito != null) {
            carritoDAO.actualizar(carrito);
            carritoModView.mostrarMensaje("Carrito actualizado correctamente.");
        } else {
            carritoModView.mostrarMensaje("Primero debe buscar un carrito.");
        }
    }

    private void cargarProductos(JTable tabla, List<ItemCarrito> items) {
        DefaultTableModel modelo = new DefaultTableModel(new Object[]{"Código", "Nombre", "Precio", "Cantidad", "Subtotal"}, 0);
        for (ItemCarrito item : items) {
            modelo.addRow(new Object[]{
                    item.getProducto().getCodigo(),
                    item.getProducto().getNombre(),
                    item.getProducto().getPrecio(),
                    item.getCantidad(),
                    item.getSubtotal()
            });
        }
        tabla.setModel(modelo);
    }

    private void mostrarTotales(JTextField txtTotal, JTextField txtIva, JTextField txtFinal, Carrito carrito) {
        if (txtTotal != null) txtTotal.setText(String.format("%.2f", carrito.calcularTotal()));
        if (txtIva != null) txtIva.setText(String.format("%.2f", carrito.calcularIVA()));
        if (txtFinal != null) txtFinal.setText(String.format("%.2f", carrito.calcularTotal()));
    }
}
