package ec.edu.ups.controlador;

import ec.edu.ups.dao.ProductoDAO;
import ec.edu.ups.modelo.Producto;
import ec.edu.ups.vista.Carrito.CarritoAñadirView;
import ec.edu.ups.vista.Producto.ProductoAñadirView;
import ec.edu.ups.vista.Producto.ProductoElimView;
import ec.edu.ups.vista.Producto.ProductoListaView;
import ec.edu.ups.vista.Producto.ProductoModView;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

public class ProductoController {

    private final ProductoDAO productoDAO;
    private final ProductoAñadirView productoAnadirView;
    private final ProductoListaView productoListaView;
    private final CarritoAñadirView carritoAnadirView;
    private ProductoModView productoModView;
    private ProductoElimView productoElimView;

    public ProductoController(ProductoDAO productoDAO,
                              ProductoAñadirView productoAnadirView,
                              ProductoListaView productoListaView,
                              CarritoAñadirView carritoAnadirView) {
        this.productoDAO = productoDAO;
        this.productoAnadirView = productoAnadirView;
        this.productoListaView = productoListaView;
        this.carritoAnadirView = carritoAnadirView;
        configurarEventosEnVistas();
    }

    public void setProductoMod(ProductoModView productoModView) {
        this.productoModView = productoModView;

        productoModView.getBtnActualizar().addActionListener(e -> {
            int fila = productoModView.getTableProductos().getSelectedRow();
            if (fila >= 0) {
                int codigo = Integer.parseInt(productoModView.getTableModel().getValueAt(fila, 0).toString());
                String nombre = productoModView.getTableModel().getValueAt(fila, 1).toString();
                double precio = Double.parseDouble(productoModView.getTableModel().getValueAt(fila, 2).toString());
                productoDAO.actualizar(new Producto(codigo, nombre, precio));
                listarProductosMod();
            }
        });
    }

    public void setProductoElimView(ProductoElimView productoElimView) {
        this.productoElimView = productoElimView;

        productoElimView.getBtnEliminar().addActionListener(e -> {
            int fila = productoElimView.getTablaResultado().getSelectedRow();
            if (fila >= 0) {
                int codigo = Integer.parseInt(productoElimView.getTablaResultado().getValueAt(fila, 0).toString());
                productoDAO.eliminar(codigo);
                cargarProductosEnElimView();
                productoElimView.mostrarMensaje("Producto eliminado correctamente");
            }
        });

        productoElimView.getBtnBuscar().addActionListener(e -> buscarEnElimView());
    }

    private void configurarEventosEnVistas() {
        productoAnadirView.getBtnAceptar().addActionListener(e -> guardarProducto());

        productoListaView.getBtnBuscar().addActionListener(e -> buscarProductoPorNombre());

        productoListaView.getBtnListar().addActionListener(e -> listarTodos());

        carritoAnadirView.getBtnBuscar().addActionListener(e -> buscarProductoPorCodigo());
    }

    private void guardarProducto() {
        try {
            if (productoAnadirView.getTxtCodigo().getText().isEmpty() ||
                    productoAnadirView.getTxtNombre().getText().isEmpty() ||
                    productoAnadirView.getTxtPrecio().getText().isEmpty()) {
                productoAnadirView.mostrarMensaje("Todos los campos son obligatorios.");
                return;
            }

            int codigo = Integer.parseInt(productoAnadirView.getTxtCodigo().getText());
            String nombre = productoAnadirView.getTxtNombre().getText();
            double precio = Double.parseDouble(productoAnadirView.getTxtPrecio().getText());

            productoDAO.crear(new Producto(codigo, nombre, precio));
            productoAnadirView.mostrarMensaje("Producto guardado correctamente");
            productoAnadirView.limpiarCampos();
        } catch (NumberFormatException e) {
            productoAnadirView.mostrarMensaje("Ingrese valores válidos.");
        }
    }

    private void buscarProductoPorNombre() {
        String nombre = productoListaView.getTxtBuscar().getText().toLowerCase();
        List<Producto> encontrados = new ArrayList<>();

        for (Producto p : productoDAO.listarTodos()) {
            if (p.getNombre().toLowerCase().contains(nombre)) {
                encontrados.add(p);
            }
        }

        productoListaView.cargarDatos(encontrados);
    }

    private void listarTodos() {
        productoListaView.cargarDatos(productoDAO.listarTodos());
    }

    private void listarProductosMod() {
        productoModView.listarProductos(productoDAO.listarTodos());
    }

    private void buscarProductoPorCodigo() {
        try {
            int codigo = Integer.parseInt(carritoAnadirView.getTxtCodigo().getText());
            Producto producto = productoDAO.buscarPorCodigo(codigo);

            if (producto == null) {
                carritoAnadirView.mostrarMensaje("No se encontró el producto");
                carritoAnadirView.getTxtNombre().setText("");
                carritoAnadirView.getTxtPrecio().setText("");
            } else {
                carritoAnadirView.getTxtNombre().setText(producto.getNombre());
                carritoAnadirView.getTxtPrecio().setText(String.valueOf(producto.getPrecio()));
            }
        } catch (NumberFormatException e) {
            carritoAnadirView.mostrarMensaje("Código inválido.");
        }
    }

    private void cargarProductosEnElimView() {
        DefaultTableModel model = productoElimView.getModelResultado();
        model.setRowCount(0);
        for (Producto p : productoDAO.listarTodos()) {
            model.addRow(new Object[]{p.getCodigo(), p.getNombre(), p.getPrecio()});
        }
    }

    private void buscarEnElimView() {
        String filtro = productoElimView.getComboFiltro().getSelectedItem().toString();
        String busqueda = productoElimView.getTxtBusqueda().getText().toLowerCase();

        List<Producto> encontrados = new ArrayList<>();
        for (Producto p : productoDAO.listarTodos()) {
            if (filtro.equals("Código") && String.valueOf(p.getCodigo()).contains(busqueda)) {
                encontrados.add(p);
            } else if (filtro.equals("Nombre") && p.getNombre().toLowerCase().contains(busqueda)) {
                encontrados.add(p);
            }
        }

        DefaultTableModel model = productoElimView.getModelResultado();
        model.setRowCount(0);
        for (Producto p : encontrados) {
            model.addRow(new Object[]{p.getCodigo(), p.getNombre(), p.getPrecio()});
        }
    }
}
