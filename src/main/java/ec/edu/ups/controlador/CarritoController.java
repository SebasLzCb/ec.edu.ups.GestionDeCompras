package ec.edu.ups.controlador;

import ec.edu.ups.dao.CarritoDAO;
import ec.edu.ups.dao.ProductoDAO;
import ec.edu.ups.modelo.Carrito;
import ec.edu.ups.modelo.ItemCarrito;
import ec.edu.ups.modelo.Producto;
import ec.edu.ups.modelo.Usuario;
import ec.edu.ups.util.FormateadorUtils;
import ec.edu.ups.util.MensajeInternacionalizacionHandler;
import ec.edu.ups.vista.Carrito.*;

import javax.swing.table.DefaultTableModel;
import java.util.Locale;

public class CarritoController {

    private final CarritoDAO carritoDAO;
    private final ProductoDAO productoDAO;
    private final CarritoAñadirView añadirView;
    private final CarritoListaView listaView;
    private final CarritoModView modView;
    private final CarritoElimView elimView;
    private final CarritoDetallesView detallesView;
    private final Usuario usuarioActual;
    private final MensajeInternacionalizacionHandler mensajeHandler;
    private Carrito carrito;

    public CarritoController(CarritoDAO carritoDAO,
                             ProductoDAO productoDAO,
                             CarritoAñadirView añadirView,
                             CarritoListaView listaView,
                             CarritoModView modView,
                             CarritoElimView elimView,
                             CarritoDetallesView detallesView,
                             Usuario usuarioActual,
                             MensajeInternacionalizacionHandler mensajeHandler) {
        this.carritoDAO      = carritoDAO;
        this.productoDAO     = productoDAO;
        this.añadirView      = añadirView;
        this.listaView       = listaView;
        this.modView         = modView;
        this.elimView        = elimView;
        this.detallesView    = detallesView;
        this.usuarioActual   = usuarioActual;
        this.mensajeHandler  = mensajeHandler;
        this.carrito         = new Carrito();
        this.carrito.setUsuario(usuarioActual);

        configurarEventosEnVistas();
    }

    private void configurarEventosEnVistas() {
        añadirView.getBtnBuscar ().addActionListener(e -> buscarProducto());
        añadirView.getBtnAnadir().addActionListener(e -> anadirProducto());
        añadirView.getBtnGuardar().addActionListener(e -> guardarCarrito());
        añadirView.getBtnLimpiar().addActionListener(e -> limpiarFormulario());

        listaView.getBtnListar ().addActionListener(e -> {
            listarCarritos();
            listaView.setVisible(true);
        });
        listaView.getBtnBuscar ().addActionListener(e -> buscarCarrito());
        listaView.getBtnDetalles().addActionListener(e -> mostrarDetallesDelCarrito());

        elimView.getBtnBuscar () .addActionListener(e -> buscarEnEliminar());
        elimView.getBtnEliminar().addActionListener(e -> eliminarCarrito());

        modView.getBtnBuscar    ().addActionListener(e -> buscarEnModificar());
        modView.getBtnActualizar().addActionListener(e -> actualizarCarrito());
    }

    private void buscarProducto() {
        String txt = añadirView.getTxtCodigo().getText().trim();
        if (!txt.matches("\\d+")) {
            añadirView.mostrarMensaje(mensajeHandler.get("carrito.error.codigo_invalido"));
            return;
        }
        Producto p = productoDAO.buscarPorCodigo(Integer.parseInt(txt));
        if (p != null) {
            añadirView.getTxtNombre().setText(p.getNombre());
            añadirView.getTxtPrecio().setText(String.valueOf(p.getPrecio()));
        } else {
            añadirView.mostrarMensaje(mensajeHandler.get("carrito.error.producto_no_encontrado"));
        }
    }

    private void anadirProducto() {
        String txtCod = añadirView.getTxtCodigo().getText().trim();
        String txtCant= (String) añadirView.getCbxCantidad().getSelectedItem();
        if (!txtCod.matches("\\d+") || txtCant == null) {
            añadirView.mostrarMensaje(mensajeHandler.get("carrito.error.verifique_codigo_y_cantidad"));
            return;
        }
        Producto p = productoDAO.buscarPorCodigo(Integer.parseInt(txtCod));
        if (p == null) {
            añadirView.mostrarMensaje(mensajeHandler.get("carrito.error.producto_invalido"));
            return;
        }
        carrito.agregarProducto(p, Integer.parseInt(txtCant));
        cargarProductosEnTabla();
        mostrarTotales();
    }

    private void cargarProductosEnTabla() {
        DefaultTableModel m = (DefaultTableModel) añadirView.getTblProductos().getModel();
        m.setRowCount(0);
        Locale loc = mensajeHandler.getLocale();
        for (ItemCarrito it : carrito.obtenerItems()) {
            m.addRow(new Object[]{
                    it.getProducto().getCodigo(),
                    it.getProducto().getNombre(),
                    it.getProducto().getPrecio(),
                    it.getCantidad(),
                    FormateadorUtils.formatearMoneda(it.getSubtotal(), loc)
            });
        }
    }

    private void mostrarTotales() {
        Locale loc = mensajeHandler.getLocale();
        añadirView.getTxtSubtotal().setText(FormateadorUtils.formatearMoneda(carrito.calcularSubtotal(), loc));
        añadirView.getTxtIva()     .setText(FormateadorUtils.formatearMoneda(carrito.calcularIVA(),      loc));
        añadirView.getTxtTotal()   .setText(FormateadorUtils.formatearMoneda(carrito.calcularTotal(),    loc));
    }

    private void guardarCarrito() {
        if (añadirView.getTblProductos().getRowCount() == 0) {
            añadirView.mostrarMensaje(mensajeHandler.get("carrito.error.agrega_productos_antes"));
            return;
        }
        Carrito nuevo = new Carrito();
        nuevo.setUsuario(usuarioActual);
        DefaultTableModel m = (DefaultTableModel) añadirView.getTblProductos().getModel();
        for (int i = 0; i < m.getRowCount(); i++) {
            int c = Integer.parseInt(m.getValueAt(i, 0).toString());
            int q = Integer.parseInt(m.getValueAt(i, 3).toString());
            Producto p = productoDAO.buscarPorCodigo(c);
            nuevo.agregarProducto(p, q);
        }
        carritoDAO.crear(nuevo);
        añadirView.mostrarMensaje(mensajeHandler.get("carrito.success.carrito_guardado"));
        limpiarFormulario();
    }

    private void limpiarFormulario() {
        añadirView.getTxtCodigo().setText("");
        añadirView.getTxtNombre().setText("");
        añadirView.getTxtPrecio().setText("");
        añadirView.getTxtSubtotal().setText("");
        añadirView.getTxtIva()    .setText("");
        añadirView.getTxtTotal()  .setText("");
        ((DefaultTableModel)añadirView.getTblProductos().getModel()).setRowCount(0);
        carrito.vaciarCarrito();
    }

    private void listarCarritos() {
        DefaultTableModel m = (DefaultTableModel) listaView.getTablaCarrito().getModel();
        m.setRowCount(0);
        for (Carrito c : carritoDAO.listarTodos()) {
            m.addRow(new Object[]{c.getCodigo(), c.getUsuario().getUsername(), c.calcularTotal()});
        }
    }

    private void buscarCarrito() {
        String txt = listaView.getTxtCodigo().trim();
        if (!txt.matches("\\d+")) {
            listaView.mostrarMensaje(mensajeHandler.get("carrito.error.codigo_invalido"));
            return;
        }
        Carrito c = carritoDAO.buscarPorCodigo(Integer.parseInt(txt));
        if (c != null) {
            DefaultTableModel m = (DefaultTableModel) listaView.getTablaCarrito().getModel();
            m.setRowCount(0);
            m.addRow(new Object[]{c.getCodigo(), c.getUsuario().getUsername(), c.calcularTotal()});
            listaView.getTxtTotal().setText(String.valueOf(c.calcularTotal()));
        } else {
            listaView.mostrarMensaje(mensajeHandler.get("carrito.error.carrito_no_encontrado"));
        }
    }

    private void mostrarDetallesDelCarrito() {
        int fila = listaView.getTablaCarrito().getSelectedRow();
        if (fila < 0) {
            listaView.mostrarMensaje(mensajeHandler.get("carrito.error.seleccione_carrito"));
            return;
        }
        int codigo = (Integer) listaView.getTablaCarrito().getValueAt(fila, 0);
        Carrito c = carritoDAO.buscarPorCodigo(codigo);
        if (c == null) {
            listaView.mostrarMensaje(mensajeHandler.get("carrito.error.carrito_no_encontrado"));
            return;
        }

        Locale loc = mensajeHandler.getLocale();
        String fecha = FormateadorUtils.formatearFecha(c.getFechaCreacion().getTime(), loc);

        detallesView.setResumen(
                String.format("%s: %d  |  %s: %s  |  %s: %s  |  %s: %s",
                        mensajeHandler.get("carrito.view.detalles.lbl_codigo"),  c.getCodigo(),
                        mensajeHandler.get("carrito.view.detalles.lbl_usuario"), c.getUsuario().getUsername(),
                        mensajeHandler.get("carrito.view.detalles.lbl_fecha"),   fecha,
                        mensajeHandler.get("carrito.view.detalles.lbl_total"),   FormateadorUtils.formatearMoneda(c.calcularTotal(), loc)
                )
        );

        DefaultTableModel modelo = new DefaultTableModel(
                new Object[]{
                        mensajeHandler.get("carrito.view.detalles.col_producto"),
                        mensajeHandler.get("carrito.view.detalles.col_cantidad"),
                        mensajeHandler.get("carrito.view.detalles.col_precio"),
                        mensajeHandler.get("carrito.view.detalles.col_subtotal")
                }, 0
        );
        for (ItemCarrito it : c.obtenerItems()) {
            modelo.addRow(new Object[]{
                    it.getProducto().getCodigo(),
                    it.getCantidad(),
                    FormateadorUtils.formatearMoneda(it.getProducto().getPrecio(), loc),
                    FormateadorUtils.formatearMoneda(it.getSubtotal(), loc)
            });
        }
        detallesView.getTblDetalles().setModel(modelo);
        detallesView.setVisible(true);
    }

    private void buscarEnEliminar() {
        String txt = elimView.getCodigoIngresado().trim();
        if (!txt.matches("\\d+")) {
            elimView.mostrarMensaje(mensajeHandler.get("carrito.error.codigo_invalido"));
            return;
        }
        elimView.mostrarResultado(carritoDAO.buscarPorCodigo(Integer.parseInt(txt)));
        elimView.setVisible(true);
    }

    private void eliminarCarrito() {
        String txt = elimView.getCodigoIngresado().trim();
        if (!txt.matches("\\d+")) {
            elimView.mostrarMensaje(mensajeHandler.get("carrito.error.codigo_invalido"));
            return;
        }
        carritoDAO.eliminar(Integer.parseInt(txt));
        elimView.mostrarMensaje(mensajeHandler.get("carrito.success.carrito_eliminado"));
        elimView.limpiarCampos();
        elimView.setVisible(true);
    }

    private void buscarEnModificar() {
        String txt = modView.getTxtCodigo().getText().trim();
        if (!txt.matches("\\d+")) {
            modView.mostrarMensaje(mensajeHandler.get("carrito.error.codigo_invalido"));
            return;
        }
        Carrito c = carritoDAO.buscarPorCodigo(Integer.parseInt(txt));
        if (c != null) {
            modView.getTxtTotal().setText(String.valueOf(c.calcularTotal()));
        } else {
            modView.mostrarMensaje(mensajeHandler.get("carrito.error.carrito_no_encontrado"));
        }
        modView.setVisible(true);
    }

    private void actualizarCarrito() {
        String txt = modView.getTxtCodigo().getText().trim();
        if (!txt.matches("\\d+")) {
            modView.mostrarMensaje(mensajeHandler.get("carrito.error.codigo_invalido"));
            return;
        }
        Carrito c = carritoDAO.buscarPorCodigo(Integer.parseInt(txt));
        if (c != null) {
            carritoDAO.actualizar(c);
            modView.mostrarMensaje(mensajeHandler.get("carrito.success.carrito_actualizado"));
        } else {
            modView.mostrarMensaje(mensajeHandler.get("carrito.error.carrito_no_encontrado"));
        }
        modView.setVisible(true);
    }
}
