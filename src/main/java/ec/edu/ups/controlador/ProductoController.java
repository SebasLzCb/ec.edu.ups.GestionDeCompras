package ec.edu.ups.controlador;

import ec.edu.ups.dao.ProductoDAO;
import ec.edu.ups.util.MensajeInternacionalizacionHandler;
import ec.edu.ups.modelo.Producto;
import ec.edu.ups.vista.Producto.ProductoAñadirView;
import ec.edu.ups.vista.Producto.ProductoListaView;
import ec.edu.ups.vista.Producto.ProductoModView;
import ec.edu.ups.vista.Producto.ProductoElimView;
import ec.edu.ups.util.ValidacionUtils;
import ec.edu.ups.excepciones.ValidacionException;

import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.util.stream.Collectors; // Se importa Collectors para toList() si no es Java 16+

/**
 * La clase {@code ProductoController} es el controlador que gestiona la lógica de negocio
 * para las operaciones relacionadas con los productos en el sistema.
 * Actúa como intermediario entre las vistas de productos (añadir, listar, modificar, eliminar)
 * y el modelo de datos de productos, utilizando la interfaz {@link ProductoDAO}.
 * Implementa validaciones de entrada de usuario y maneja mensajes internacionalizados.
 */
public class ProductoController {

    private final ProductoDAO dao;
    private final ProductoAñadirView añadirView;
    private final ProductoListaView listaView;
    private final ProductoModView modView;
    private final ProductoElimView elimView;
    private final MensajeInternacionalizacionHandler mensajeHandler;

    /**
     * Constructor de la clase {@code ProductoController}.
     * Inicializa las dependencias DAO, las vistas correspondientes a la gestión de productos
     * y el manejador de mensajes de internacionalización.
     * Configura la ubicación de las vistas y los eventos de acción.
     *
     * @param dao             Objeto DAO para la persistencia de productos.
     * @param añadirView      Vista para añadir nuevos productos.
     * @param listaView       Vista para listar y buscar productos.
     * @param modView         Vista para modificar productos existentes.
     * @param elimView        Vista para eliminar productos.
     * @param mensajeHandler  Manejador para obtener mensajes internacionalizados.
     */
    public ProductoController(ProductoDAO dao,
                              ProductoAñadirView añadirView,
                              ProductoListaView listaView,
                              ProductoModView modView,
                              ProductoElimView elimView,
                              MensajeInternacionalizacionHandler mensajeHandler) {
        this.dao = dao;
        this.añadirView = añadirView;
        this.listaView = listaView;
        this.modView = modView;
        this.elimView = elimView;
        this.mensajeHandler = mensajeHandler;

        configurarVistas();
        configurarEventos();
        recargarMod(); // Recarga la tabla de modificar productos al iniciar
        recargarElim(); // Recarga la tabla de eliminar productos al iniciar
    }

    /**
     * Configura la posición inicial de las vistas internas de producto
     * dentro del JDesktopPane.
     */
    private void configurarVistas() {
        añadirView.setLocation(20, 20);
        listaView.setLocation(40, 40);
        modView.setLocation(60, 60);
        elimView.setLocation(80, 80);
    }

    /**
     * Configura los ActionListeners para los botones y elementos interactivos
     * de cada una de las vistas de productos. Cada evento está asociado a un método
     * privado que implementa la lógica de negocio correspondiente.
     */
    private void configurarEventos() {
        // Eventos para la vista Añadir Producto
        añadirView.getBtnAceptar().addActionListener(e -> crearProducto());
        añadirView.getBtnLimpiar().addActionListener(e -> añadirView.limpiarCampos());

        // Eventos para la vista Listar Producto
        listaView.getBtnListar().addActionListener(e ->
                listaView.cargarDatos(dao.listarTodos())
        );
        listaView.getBtnBuscar().addActionListener(e -> buscarPorNombre());

        // Eventos para la vista Modificar Producto
        modView.getBtnBuscar().addActionListener(e -> recargarMod()); // Recarga la tabla con todos los productos
        modView.getBtnActualizar().addActionListener(e -> actualizarProducto());
        modView.getBtnCancelar().addActionListener(e -> modView.limpiarCampos());

        // Eventos para la vista Eliminar Producto
        elimView.getBtnBuscar().addActionListener(e -> buscarEnEliminar());
        elimView.getBtnEliminar().addActionListener(e -> eliminarProducto());
    }

    /**
     * Crea un nuevo producto utilizando los datos ingresados en la vista {@code ProductoAñadirView}.
     * Realiza validaciones de campos obligatorios y formato de datos (entero positivo para código,
     * decimal positivo para precio). Si las validaciones son exitosas, el producto se crea
     * a través del {@link ProductoDAO} y se muestra un mensaje de éxito. En caso de error
     * de validación o excepción inesperada, se muestra un mensaje informativo en la vista.
     */
    private void crearProducto() {
        String sc = añadirView.getTxtCodigo().getText().trim();
        String sn = añadirView.getTxtNombre().getText().trim();
        String sp = añadirView.getTxtPrecio().getText().trim();

        try {
            ValidacionUtils.validarCampoObligatorio(sn, mensajeHandler.get("producto.view.anadir.nombre"));
            ValidacionUtils.validarEnteroPositivo(sc, mensajeHandler.get("producto.view.anadir.codigo"));
            ValidacionUtils.validarDecimalPositivo(sp, mensajeHandler.get("producto.view.anadir.precio"));

            Producto p = new Producto(
                    Integer.parseInt(sc),
                    sn,
                    Double.parseDouble(sp)
            );
            dao.crear(p);
            añadirView.mostrarMensaje(
                    mensajeHandler.get("producto.success.creado")
            );
            añadirView.limpiarCampos();
            recargarMod(); // Refresca la tabla de modificar después de crear
            recargarElim(); // Refresca la tabla de eliminar después de crear
        } catch (ValidacionException e) {
            añadirView.mostrarMensaje(mensajeHandler.get(e.getMessage()));
        } catch (Exception e) {
            añadirView.mostrarMensaje(mensajeHandler.get("error.inesperado") + ": " + e.getMessage());
        }
    }

    /**
     * Busca productos por nombre. Obtiene el texto de búsqueda de la vista {@code ProductoListaView},
     * filtra la lista completa de productos del {@link ProductoDAO} por aquellos que contengan
     * el texto en su nombre (ignorando mayúsculas/minúsculas), y luego carga los productos filtrados
     * en la tabla de la vista.
     */
    private void buscarPorNombre() {
        String texto = listaView.getTxtBuscar().toLowerCase();
        List<Producto> filtrados = dao.listarTodos().stream()
                .filter(p -> p.getNombre().toLowerCase().contains(texto))
                .toList(); // Usa .collect(Collectors.toList()) si no es Java 16+
        listaView.cargarDatos(filtrados);
    }

    /**
     * Recarga la tabla de productos en la vista de modificar producto ({@code ProductoModView})
     * con todos los productos disponibles en el {@link ProductoDAO}.
     * Limpia las filas existentes y las reemplaza con los datos más recientes.
     */
    private void recargarMod() {
        DefaultTableModel m = (DefaultTableModel) modView.getTblProductos().getModel();
        m.setRowCount(0);
        for (Producto p : dao.listarTodos()) {
            m.addRow(new Object[]{p.getCodigo(), p.getNombre(), p.getPrecio()});
        }
    }

    /**
     * Actualiza un producto existente seleccionado en la tabla de la vista {@code ProductoModView}.
     * Obtiene los datos de la fila seleccionada y realiza validaciones de formato
     * (código entero positivo, nombre no vacío, precio decimal positivo).
     * Si las validaciones son exitosas, el producto se actualiza a través del {@link ProductoDAO}
     * y se muestra un mensaje de éxito. En caso de error, se notifica al usuario.
     * La tabla de modificar y eliminar se recargan después de la actualización.
     */
    private void actualizarProducto() {
        int fila = modView.getTblProductos().getSelectedRow();
        if (fila < 0) {
            modView.mostrarMensaje(
                    mensajeHandler.get("producto.error.seleccione_producto")
            );
            return;
        }
        String sc = modView.getTblProductos().getValueAt(fila, 0).toString();
        String sn = modView.getTblProductos().getValueAt(fila, 1).toString();
        String sp = modView.getTblProductos().getValueAt(fila, 2).toString();

        try {
            ValidacionUtils.validarCampoObligatorio(sn, mensajeHandler.get("producto.view.modificar.nombre"));
            ValidacionUtils.validarEnteroPositivo(sc, mensajeHandler.get("producto.view.modificar.codigo"));
            ValidacionUtils.validarDecimalPositivo(sp, mensajeHandler.get("producto.view.modificar.precio"));

            dao.actualizar(new Producto(
                    Integer.parseInt(sc),
                    sn,
                    Double.parseDouble(sp)
            ));
            modView.mostrarMensaje(
                    mensajeHandler.get("producto.success.actualizado")
            );
            recargarMod(); // Refresca la tabla de modificar después de actualizar
            recargarElim(); // Refresca la tabla de eliminar después de actualizar
        } catch (ValidacionException e) {
            modView.mostrarMensaje(mensajeHandler.get(e.getMessage()));
        } catch (Exception e) {
            modView.mostrarMensaje(mensajeHandler.get("error.inesperado") + ": " + e.getMessage());
        }
    }

    /**
     * Recarga la tabla de productos en la vista de eliminar producto ({@code ProductoElimView})
     * con todos los productos disponibles en el {@link ProductoDAO}.
     * Limpia las filas existentes y las reemplaza con los datos más recientes.
     */
    private void recargarElim() {
        DefaultTableModel m = elimView.getModelResultado();
        m.setRowCount(0);
        for (Producto p : dao.listarTodos()) {
            m.addRow(new Object[]{p.getCodigo(), p.getNombre(), p.getPrecio()});
        }
    }

    /**
     * Busca productos para la vista de eliminación, aplicando un filtro por nombre o código.
     * Obtiene el texto de búsqueda y el índice del filtro de la vista {@code ProductoElimView}.
     * Realiza validaciones de entrada según el tipo de filtro (entero positivo para código,
     * campo obligatorio para nombre). Carga los productos filtrados en la tabla de la vista.
     */
    private void buscarEnEliminar() {
        int filtroIndex = elimView.getComboFiltro().getSelectedIndex();
        String txt = elimView.getTxtBusqueda().getText().trim().toLowerCase();
        List<Producto> filtrados;
        try {
            if (filtroIndex == 1) { // Búsqueda por código
                ValidacionUtils.validarEnteroPositivo(txt, mensajeHandler.get("producto.view.eliminar.codigo"));
                filtrados = dao.listarTodos().stream().filter(p ->
                        String.valueOf(p.getCodigo()).contains(txt)
                ).toList(); // Usa .collect(Collectors.toList()) si no es Java 16+
            } else { // Búsqueda por nombre (filtroIndex == 0)
                ValidacionUtils.validarCampoObligatorio(txt, mensajeHandler.get("producto.view.eliminar.nombre"));
                filtrados = dao.listarTodos().stream().filter(p ->
                        p.getNombre().toLowerCase().contains(txt)
                ).toList(); // Usa .collect(Collectors.toList()) si no es Java 16+
            }
        } catch (ValidacionException e) {
            elimView.mostrarMensaje(mensajeHandler.get(e.getMessage()));
            return;
        }

        DefaultTableModel m = elimView.getModelResultado();
        m.setRowCount(0);
        for (Producto p : filtrados) {
            m.addRow(new Object[]{p.getCodigo(), p.getNombre(), p.getPrecio()});
        }
    }

    /**
     * Elimina un producto seleccionado de la tabla en la vista {@code ProductoElimView}.
     * Obtiene el código del producto de la fila seleccionada y lo elimina a través
     * del {@link ProductoDAO}. Muestra un mensaje de éxito o de error si no se selecciona
     * ningún producto. Las tablas de eliminar y modificar se recargan después de la eliminación.
     */
    private void eliminarProducto() {
        int fila = elimView.getTablaResultado().getSelectedRow();
        if (fila < 0) {
            elimView.mostrarMensaje(
                    mensajeHandler.get("producto.error.seleccione_producto")
            );
            return;
        }
        int codigo = Integer.parseInt(
                elimView.getTablaResultado().getValueAt(fila, 0).toString()
        );
        dao.eliminar(codigo);
        elimView.mostrarMensaje(
                mensajeHandler.get("producto.success.eliminado")
        );
        recargarElim(); // Refresca la tabla de eliminar después de eliminar
        recargarMod(); // Refresca la tabla de modificar después de eliminar
    }
}