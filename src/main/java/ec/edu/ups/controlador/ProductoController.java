package ec.edu.ups.controlador;

import ec.edu.ups.dao.ProductoDAO;
import ec.edu.ups.util.MensajeInternacionalizacionHandler;
import ec.edu.ups.modelo.Producto;
import ec.edu.ups.vista.Producto.ProductoAñadirView;
import ec.edu.ups.vista.Producto.ProductoListaView;
import ec.edu.ups.vista.Producto.ProductoModView;
import ec.edu.ups.vista.Producto.ProductoElimView;

import javax.swing.table.DefaultTableModel;
import java.util.List;

public class ProductoController {

    private final ProductoDAO dao;
    private final ProductoAñadirView añadirView;
    private final ProductoListaView listaView;
    private final ProductoModView modView;
    private final ProductoElimView elimView;
    private final MensajeInternacionalizacionHandler mensajeHandler;

    public ProductoController(ProductoDAO dao,
                              ProductoAñadirView añadirView,
                              ProductoListaView listaView,
                              ProductoModView modView,
                              ProductoElimView elimView,
                              MensajeInternacionalizacionHandler mensajeHandler) {
        this.dao              = dao;
        this.añadirView       = añadirView;
        this.listaView        = listaView;
        this.modView          = modView;
        this.elimView         = elimView;
        this.mensajeHandler   = mensajeHandler;

        configurarVistas();
        configurarEventos();
        recargarMod();
        recargarElim();
    }

    private void configurarVistas() {
        añadirView.setLocation(20, 20);
        listaView.setLocation(40, 40);
        modView.setLocation(60, 60);
        elimView.setLocation(80, 80);
    }

    private void configurarEventos() {
        // Crear producto
        añadirView.getBtnAceptar().addActionListener(e -> crearProducto());
        añadirView.getBtnLimpiar().addActionListener(e -> añadirView.limpiarCampos());

        // Listar y buscar por nombre
        listaView.getBtnListar().addActionListener(e ->
                listaView.cargarDatos(dao.listarTodos())
        );
        listaView.getBtnBuscar().addActionListener(e -> buscarPorNombre());

        // Modificar
        modView.getBtnBuscar().addActionListener(e -> recargarMod());
        modView.getBtnActualizar().addActionListener(e -> actualizarProducto());
        modView.getBtnCancelar().addActionListener(e -> modView.limpiarCampos());

        // Eliminar y buscar con filtro
        elimView.getBtnBuscar().addActionListener(e -> buscarEnEliminar());
        elimView.getBtnEliminar().addActionListener(e -> eliminarProducto());
    }

    private void crearProducto() {
        String sc = añadirView.getTxtCodigo().getText().trim();
        String sn = añadirView.getTxtNombre().getText().trim();
        String sp = añadirView.getTxtPrecio().getText().trim();

        if (!sc.matches("\\d+") || sn.isEmpty() || !sp.matches("\\d+(\\.\\d+)?")) {
            añadirView.mostrarMensaje(
                    mensajeHandler.get("producto.error.cod_precio_nombre")
            );
            return;
        }

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
        recargarMod();
        recargarElim();
    }

    private void buscarPorNombre() {
        String texto = listaView.getTxtBuscar().toLowerCase();
        List<Producto> filtrados = dao.listarTodos().stream()
                .filter(p -> p.getNombre().toLowerCase().contains(texto))
                .toList();
        listaView.cargarDatos(filtrados);
    }

    private void recargarMod() {
        DefaultTableModel m = (DefaultTableModel) modView.getTblProductos().getModel();
        m.setRowCount(0);
        for (Producto p : dao.listarTodos()) {
            m.addRow(new Object[]{p.getCodigo(), p.getNombre(), p.getPrecio()});
        }
    }

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

        if (!sc.matches("\\d+") || sn.isEmpty() || !sp.matches("\\d+(\\.\\d+)?")) {
            modView.mostrarMensaje(
                    mensajeHandler.get("producto.error.formato_invalido")
            );
            return;
        }

        dao.actualizar(new Producto(
                Integer.parseInt(sc),
                sn,
                Double.parseDouble(sp)
        ));
        modView.mostrarMensaje(
                mensajeHandler.get("producto.success.actualizado")
        );
        recargarMod();
        recargarElim();
    }

    private void recargarElim() {
        DefaultTableModel m = elimView.getModelResultado();
        m.setRowCount(0);
        for (Producto p : dao.listarTodos()) {
            m.addRow(new Object[]{p.getCodigo(), p.getNombre(), p.getPrecio()});
        }
    }

    private void buscarEnEliminar() {
        int filtroIndex = elimView.getComboFiltro().getSelectedIndex();
        String txt = elimView.getTxtBusqueda().getText().trim().toLowerCase();
        List<Producto> filtrados = dao.listarTodos().stream().filter(p ->
                filtroIndex == 1
                        ? String.valueOf(p.getCodigo()).contains(txt)
                        : p.getNombre().toLowerCase().contains(txt)
        ).toList();

        DefaultTableModel m = elimView.getModelResultado();
        m.setRowCount(0);
        for (Producto p : filtrados) {
            m.addRow(new Object[]{p.getCodigo(), p.getNombre(), p.getPrecio()});
        }
    }

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
        recargarElim();
        recargarMod();
    }
}
