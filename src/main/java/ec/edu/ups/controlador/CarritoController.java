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
import ec.edu.ups.util.ValidacionUtils;
import ec.edu.ups.excepciones.ValidacionException;

import javax.swing.table.DefaultTableModel;
import java.util.Locale;

/**
 * La clase {@code CarritoController} es el controlador que maneja la lógica de negocio
 * para la gestión de carritos de compra. Actúa como intermediario entre las vistas
 * (GUI) y los modelos de datos (DAO), coordinando las operaciones de añadir,
 * listar, modificar, eliminar y visualizar detalles de carritos.
 *
 * <p>Utiliza las interfaces DAO para interactuar con la persistencia de datos
 * de {@link Carrito} y {@link Producto}. Además, se encarga de la validación
 * de entradas de usuario, la internacionalización de mensajes y el formato de datos
 * para su presentación en las vistas.</p>
 *
 * @author [Tu Nombre/Equipo]
 * @version 1.0
 * @since 2023-01-01
 */
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

    /**
     * Constructor de la clase {@code CarritoController}.
     * Inicializa las dependencias DAO, las vistas correspondientes a la gestión de carritos,
     * el usuario actual y el manejador de mensajes de internacionalización.
     * Configura los eventos de acción para los componentes de las vistas.
     *
     * @param carritoDAO           Objeto DAO para la persistencia de carritos.
     * @param productoDAO          Objeto DAO para la persistencia de productos.
     * @param añadirView           Vista para añadir productos al carrito.
     * @param listaView            Vista para listar carritos existentes.
     * @param modView              Vista para modificar carritos.
     * @param elimView             Vista para eliminar carritos.
     * @param detallesView         Vista para mostrar los detalles de un carrito.
     * @param usuarioActual        El usuario actualmente logeado en la aplicación.
     * @param mensajeHandler       Manejador para obtener mensajes internacionalizados.
     */
    public CarritoController(CarritoDAO carritoDAO,
                             ProductoDAO productoDAO,
                             CarritoAñadirView añadirView,
                             CarritoListaView listaView,
                             CarritoModView modView,
                             CarritoElimView elimView,
                             CarritoDetallesView detallesView,
                             Usuario usuarioActual,
                             MensajeInternacionalizacionHandler mensajeHandler) {
        this.carritoDAO = carritoDAO;
        this.productoDAO = productoDAO;
        this.añadirView = añadirView;
        this.listaView = listaView;
        this.modView = modView;
        this.elimView = elimView;
        this.detallesView = detallesView;
        this.usuarioActual = usuarioActual;
        this.mensajeHandler = mensajeHandler;
        this.carrito = new Carrito();
        this.carrito.setUsuario(usuarioActual);

        configurarEventosEnVistas();
    }

    /**
     * Configura los ActionListeners para los botones y elementos interactivos
     * de cada una de las vistas de carrito. Cada evento está asociado a un método
     * privado que implementa la lógica de negocio correspondiente.
     */
    private void configurarEventosEnVistas() {
        añadirView.getBtnBuscar().addActionListener(e -> buscarProducto());
        añadirView.getBtnAnadir().addActionListener(e -> anadirProducto());
        añadirView.getBtnGuardar().addActionListener(e -> guardarCarrito());
        añadirView.getBtnLimpiar().addActionListener(e -> limpiarFormulario());

        listaView.getBtnListar().addActionListener(e -> {
            listarCarritos();
            listaView.setVisible(true);
        });
        listaView.getBtnBuscar().addActionListener(e -> buscarCarrito());
        listaView.getBtnDetalles().addActionListener(e -> mostrarDetallesDelCarrito());

        elimView.getBtnBuscar().addActionListener(e -> buscarEnEliminar());
        elimView.getBtnEliminar().addActionListener(e -> eliminarCarrito());

        modView.getBtnBuscar().addActionListener(e -> buscarEnModificar());
        modView.getBtnActualizar().addActionListener(e -> actualizarCarrito());
    }

    /**
     * Busca un producto por su código en la base de datos a través del {@link ProductoDAO}.
     * Muestra el nombre y precio del producto encontrado en la vista de añadir carrito.
     * Si el producto no se encuentra o la entrada es inválida, muestra un mensaje de error.
     */
    private void buscarProducto() {
        String txt = añadirView.getTxtCodigo().getText().trim();
        try {
            ValidacionUtils.validarEnteroPositivo(txt, mensajeHandler.get("carrito.view.anadir.codigo"));
            Producto p = productoDAO.buscarPorCodigo(Integer.parseInt(txt));
            if (p != null) {
                añadirView.getTxtNombre().setText(p.getNombre());
                añadirView.getTxtPrecio().setText(String.valueOf(p.getPrecio()));
            } else {
                añadirView.mostrarMensaje(mensajeHandler.get("carrito.error.producto_no_encontrado"));
            }
        } catch (ValidacionException e) {
            añadirView.mostrarMensaje(mensajeHandler.get(e.getMessage()));
        } catch (NumberFormatException e) {
            añadirView.mostrarMensaje(mensajeHandler.get("carrito.error.codigo_invalido"));
        }
    }

    /**
     * Añade un producto al carrito temporal actual.
     * Obtiene el código del producto y la cantidad desde la vista de añadir.
     * Valida las entradas, busca el producto y lo agrega al {@link Carrito}.
     * Luego, actualiza la tabla de productos en la vista y recalcula los totales.
     * Muestra mensajes de error si las entradas son inválidas o el producto no existe.
     */
    private void anadirProducto() {
        String txtCod = añadirView.getTxtCodigo().getText().trim();
        String txtCant = (String) añadirView.getCbxCantidad().getSelectedItem();
        try {
            ValidacionUtils.validarEnteroPositivo(txtCod, mensajeHandler.get("carrito.view.anadir.codigo"));
            ValidacionUtils.validarEnteroPositivo(txtCant, mensajeHandler.get("carrito.view.anadir.cantidad"));

            Producto p = productoDAO.buscarPorCodigo(Integer.parseInt(txtCod));
            if (p == null) {
                añadirView.mostrarMensaje(mensajeHandler.get("carrito.error.producto_invalido"));
                return;
            }
            carrito.agregarProducto(p, Integer.parseInt(txtCant));
            cargarProductosEnTabla();
            mostrarTotales();
        } catch (ValidacionException e) {
            añadirView.mostrarMensaje(mensajeHandler.get(e.getMessage()));
        } catch (NumberFormatException e) {
            añadirView.mostrarMensaje(mensajeHandler.get("carrito.error.verifique_codigo_y_cantidad"));
        }
    }

    /**
     * Carga los ítems del carrito temporal actual en la tabla de la vista de añadir carrito.
     * Refresca el modelo de la tabla, mostrando el código, nombre, precio, cantidad y subtotal
     * de cada producto en el carrito. Utiliza {@link FormateadorUtils} para el formato de moneda.
     */
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

    /**
     * Muestra el subtotal, IVA y total del carrito actual en los campos de texto
     * correspondientes en la vista de añadir carrito.
     * Utiliza {@link FormateadorUtils} para formatear los valores monetarios
     * según la configuración regional.
     */
    private void mostrarTotales() {
        Locale loc = mensajeHandler.getLocale();
        añadirView.getTxtSubtotal().setText(FormateadorUtils.formatearMoneda(carrito.calcularSubtotal(), loc));
        añadirView.getTxtIva().setText(FormateadorUtils.formatearMoneda(carrito.calcularIVA(), loc));
        añadirView.getTxtTotal().setText(FormateadorUtils.formatearMoneda(carrito.calcularTotal(), loc));
    }

    /**
     * Guarda el carrito actual en la base de datos a través del {@link CarritoDAO}.
     * Antes de guardar, verifica que el carrito no esté vacío y reconstruye el objeto
     * {@link Carrito} a partir de los datos mostrados en la tabla de la vista.
     * Si hay errores de validación o productos inválidos, muestra mensajes de error.
     * Si el guardado es exitoso, muestra un mensaje de éxito y limpia el formulario.
     */
    private void guardarCarrito() {
        if (añadirView.getTblProductos().getRowCount() == 0) {
            añadirView.mostrarMensaje(mensajeHandler.get("carrito.error.agrega_productos_antes"));
            return;
        }
        Carrito nuevo = new Carrito();
        nuevo.setUsuario(usuarioActual);
        DefaultTableModel m = (DefaultTableModel) añadirView.getTblProductos().getModel();
        for (int i = 0; i < m.getRowCount(); i++) {
            try {
                int c = Integer.parseInt(m.getValueAt(i, 0).toString());
                int q = Integer.parseInt(m.getValueAt(i, 3).toString());
                Producto p = productoDAO.buscarPorCodigo(c);
                if (p == null) {
                    añadirView.mostrarMensaje(mensajeHandler.get("carrito.error.producto_invalido_en_tabla"));
                    return;
                }
                nuevo.agregarProducto(p, q);
            } catch (NumberFormatException ex) {
                añadirView.mostrarMensaje(mensajeHandler.get("carrito.error.producto_cantidad_invalida_tabla"));
                return;
            }
        }
        carritoDAO.crear(nuevo);
        añadirView.mostrarMensaje(mensajeHandler.get("carrito.success.carrito_guardado"));
        limpiarFormulario();
    }

    /**
     * Limpia todos los campos de entrada y la tabla de productos
     * en la vista de añadir carrito. También vacía el objeto {@link Carrito}
     * temporal que se está construyendo.
     */
    private void limpiarFormulario() {
        añadirView.getTxtCodigo().setText("");
        añadirView.getTxtNombre().setText("");
        añadirView.getTxtPrecio().setText("");
        añadirView.getTxtSubtotal().setText("");
        añadirView.getTxtIva().setText("");
        añadirView.getTxtTotal().setText("");
        ((DefaultTableModel) añadirView.getTblProductos().getModel()).setRowCount(0);
        carrito.vaciarCarrito();
    }

    /**
     * Lista todos los carritos existentes en la base de datos y los muestra
     * en la tabla de la vista de listar carritos.
     * Se muestra el código del carrito, el nombre de usuario asociado y el total.
     */
    private void listarCarritos() {
        DefaultTableModel m = (DefaultTableModel) listaView.getTablaCarrito().getModel();
        m.setRowCount(0);
        for (Carrito c : carritoDAO.listarTodos()) {
            m.addRow(new Object[]{c.getCodigo(), c.getUsuario().getUsername(), c.calcularTotal()});
        }
    }

    /**
     * Busca un carrito específico por su código en la base de datos.
     * Muestra el carrito encontrado (si existe) en la tabla de la vista de listar carritos
     * y el total del mismo. Si el carrito no se encuentra o la entrada es inválida,
     * muestra un mensaje de error.
     */
    private void buscarCarrito() {
        String txt = listaView.getTxtCodigo().trim();
        try {
            ValidacionUtils.validarEnteroPositivo(txt, mensajeHandler.get("carrito.view.listar.codigo"));
            Carrito c = carritoDAO.buscarPorCodigo(Integer.parseInt(txt));
            if (c != null) {
                DefaultTableModel m = (DefaultTableModel) listaView.getTablaCarrito().getModel();
                m.setRowCount(0);
                m.addRow(new Object[]{c.getCodigo(), c.getUsuario().getUsername(), c.calcularTotal()});
                listaView.getTxtTotal().setText(String.valueOf(c.calcularTotal()));
            } else {
                listaView.mostrarMensaje(mensajeHandler.get("carrito.error.carrito_no_encontrado"));
            }
        } catch (ValidacionException e) {
            listaView.mostrarMensaje(mensajeHandler.get(e.getMessage()));
        } catch (NumberFormatException e) {
            listaView.mostrarMensaje(mensajeHandler.get("carrito.error.codigo_invalido"));
        }
    }

    /**
     * Muestra los detalles completos de un carrito seleccionado en la tabla
     * de la vista de listar carritos. Obtiene el carrito por su código y
     * puebla la vista de detalles con información como la fecha de creación,
     * el usuario, el total y una tabla con los productos y sus cantidades.
     * Muestra un mensaje de error si no se ha seleccionado un carrito o si no se encuentra.
     */
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
                        mensajeHandler.get("carrito.view.listar.codigo"), c.getCodigo(),
                        mensajeHandler.get("carrito.view.listar.usuario"), c.getUsuario().getUsername(),
                        mensajeHandler.get("carrito.view.detalles.lbl_fecha"), fecha,
                        mensajeHandler.get("carrito.view.listar.total"), FormateadorUtils.formatearMoneda(c.calcularTotal(), loc)
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

    /**
     * Busca un carrito por su código en la base de datos para la operación de eliminación.
     * Muestra el resultado de la búsqueda en la vista de eliminar carrito.
     * Si la entrada es inválida, muestra un mensaje de error.
     */
    private void buscarEnEliminar() {
        String txt = elimView.getCodigoIngresado().trim();
        try {
            ValidacionUtils.validarEnteroPositivo(txt, mensajeHandler.get("carrito.view.eliminar.codigo"));
            elimView.mostrarResultado(carritoDAO.buscarPorCodigo(Integer.parseInt(txt)));
            elimView.setVisible(true);
        } catch (ValidacionException e) {
            elimView.mostrarMensaje(mensajeHandler.get(e.getMessage()));
        } catch (NumberFormatException e) {
            elimView.mostrarMensaje(mensajeHandler.get("carrito.error.codigo_invalido"));
        }
    }

    /**
     * Elimina un carrito de la base de datos dado su código.
     * Valida la entrada del código y realiza la eliminación a través del {@link CarritoDAO}.
     * Muestra un mensaje de éxito o de error según el resultado de la operación.
     * Limpia los campos de la vista de eliminar después de una eliminación exitosa.
     */
    private void eliminarCarrito() {
        String txt = elimView.getCodigoIngresado().trim();
        try {
            ValidacionUtils.validarEnteroPositivo(txt, mensajeHandler.get("carrito.view.eliminar.codigo"));
            carritoDAO.eliminar(Integer.parseInt(txt));
            elimView.mostrarMensaje(mensajeHandler.get("carrito.success.carrito_eliminado"));
            elimView.limpiarCampos();
            elimView.setVisible(true);
        } catch (ValidacionException e) {
            elimView.mostrarMensaje(mensajeHandler.get(e.getMessage()));
        } catch (NumberFormatException e) {
            elimView.mostrarMensaje(mensajeHandler.get("carrito.error.codigo_invalido"));
        }
    }

    /**
     * Busca un carrito por su código en la base de datos para la operación de modificación.
     * Muestra el total del carrito encontrado en la vista de modificar carrito.
     * Si el carrito no se encuentra o la entrada es inválida, muestra un mensaje de error.
     */
    private void buscarEnModificar() {
        String txt = modView.getTxtCodigo().getText().trim();
        try {
            ValidacionUtils.validarEnteroPositivo(txt, mensajeHandler.get("carrito.view.modificar.codigo"));
            Carrito c = carritoDAO.buscarPorCodigo(Integer.parseInt(txt));
            if (c != null) {
                modView.getTxtTotal().setText(String.valueOf(c.calcularTotal()));
            } else {
                modView.mostrarMensaje(mensajeHandler.get("carrito.error.carrito_no_encontrado"));
            }
            modView.setVisible(true);
        } catch (ValidacionException e) {
            modView.mostrarMensaje(mensajeHandler.get(e.getMessage()));
        } catch (NumberFormatException e) {
            modView.mostrarMensaje(mensajeHandler.get("carrito.error.codigo_invalido"));
        }
    }

    /**
     * Actualiza un carrito existente en la base de datos.
     * Valida la entrada del código, busca el carrito y, si lo encuentra,
     * realiza la actualización a través del {@link CarritoDAO}.
     * Muestra un mensaje de éxito o de error según el resultado de la operación.
     */
    private void actualizarCarrito() {
        String txt = modView.getTxtCodigo().getText().trim();
        try {
            ValidacionUtils.validarEnteroPositivo(txt, mensajeHandler.get("carrito.view.modificar.codigo"));
            Carrito c = carritoDAO.buscarPorCodigo(Integer.parseInt(txt));
            if (c != null) {
                // En este punto, si se permitiera modificar ítems del carrito en la vista de modificación,
                // se actualizaría el objeto 'c' con los nuevos ítems antes de llamar a carritoDAO.actualizar(c).
                // Dado que el código solo muestra el total, la actualización actual no modifica los ítems del carrito.
                // Si la modificación implicara solo cambios en metadatos del carrito (no ítems), este llamado sería válido.
                carritoDAO.actualizar(c);
                modView.mostrarMensaje(mensajeHandler.get("carrito.success.carrito_actualizado"));
            } else {
                modView.mostrarMensaje(mensajeHandler.get("carrito.error.carrito_no_encontrado"));
            }
            modView.setVisible(true);
        } catch (ValidacionException e) {
            modView.mostrarMensaje(mensajeHandler.get(e.getMessage()));
        } catch (NumberFormatException e) {
            modView.mostrarMensaje(mensajeHandler.get("carrito.error.codigo_invalido"));
        }
    }
}