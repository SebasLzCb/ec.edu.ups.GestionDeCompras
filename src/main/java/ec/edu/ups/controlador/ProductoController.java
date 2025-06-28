package ec.edu.ups.controlador;

import ec.edu.ups.dao.ProductoDAO;
import ec.edu.ups.modelo.Producto;
import ec.edu.ups.vista.Producto.*;

import javax.swing.table.DefaultTableModel;
import java.util.List;

public class ProductoController {

    private final ProductoDAO productoDAO;
    private final ProductoAñadirView vistaAñadir;
    private final ProductoListaView vistaLista;
    private final ProductoModView vistaMod;
    private final ProductoElimView vistaElim;

    public ProductoController(ProductoDAO dao, ProductoAñadirView anadir, ProductoListaView lista,
                              ProductoModView mod, ProductoElimView elim) {

        this.productoDAO = dao;
        this.vistaAñadir = anadir;
        this.vistaLista = lista;
        this.vistaMod = mod;
        this.vistaElim = elim;

        configurarEventos();
        cargarTablaMod();
        cargarTablaEliminar();
    }

    private void configurarEventos() {
        vistaAñadir.getBtnAceptar().addActionListener(e -> crearProducto());

        vistaLista.getBtnBuscar().addActionListener(e -> buscarPorNombre());
        vistaLista.getBtnListar().addActionListener(e -> listarTodos());

        vistaMod.getBtnActualizar().addActionListener(e -> actualizarProducto());

        vistaElim.getBtnBuscar().addActionListener(e -> buscarEnEliminar());
        vistaElim.getBtnEliminar().addActionListener(e -> eliminarProducto());
    }

    private void crearProducto() {
        try {
            String cod = vistaAñadir.getTxtCodigo().getText().trim();
            String nom = vistaAñadir.getTxtNombre().getText().trim();
            String pre = vistaAñadir.getTxtPrecio().getText().trim();

            if (cod.isEmpty() || nom.isEmpty() || pre.isEmpty()) {
                vistaAñadir.mostrarMensaje("Todos los campos son obligatorios.");
                return;
            }

            int codigo = Integer.parseInt(cod);
            double precio = Double.parseDouble(pre);

            productoDAO.crear(new Producto(codigo, nom, precio));
            vistaAñadir.mostrarMensaje("Producto guardado correctamente.");
            vistaAñadir.limpiarCampos();

        } catch (NumberFormatException e) {
            vistaAñadir.mostrarMensaje("Código y precio deben ser numéricos.");
        }
    }

    private void listarTodos() {
        List<Producto> lista = productoDAO.listarTodos();
        vistaLista.cargarDatos(lista);
    }

    private void buscarPorNombre() {
        String texto = vistaLista.getTxtBuscar().getText().trim().toLowerCase();
        List<Producto> todos = productoDAO.listarTodos();

        List<Producto> encontrados = todos.stream()
                .filter(p -> p.getNombre().toLowerCase().contains(texto))
                .toList();

        vistaLista.cargarDatos(encontrados);
    }

    private void cargarTablaMod() {
        List<Producto> lista = productoDAO.listarTodos();
        vistaMod.listarProductos(lista);
    }

    private void actualizarProducto() {
        int fila = vistaMod.getTableProductos().getSelectedRow();
        if (fila == -1) {
            vistaMod.mostrarMensaje("Seleccione un producto para actualizar.");
            return;
        }

        try {
            int codigo = Integer.parseInt(vistaMod.getTableModel().getValueAt(fila, 0).toString());
            String nombre = vistaMod.getTableModel().getValueAt(fila, 1).toString();
            double precio = Double.parseDouble(vistaMod.getTableModel().getValueAt(fila, 2).toString());

            productoDAO.actualizar(new Producto(codigo, nombre, precio));
            vistaMod.mostrarMensaje("Producto actualizado correctamente.");
            cargarTablaMod();

        } catch (NumberFormatException e) {
            vistaMod.mostrarMensaje("Error en el formato de los datos.");
        }
    }

    private void cargarTablaEliminar() {
        DefaultTableModel model = vistaElim.getModelResultado();
        model.setRowCount(0);
        for (Producto p : productoDAO.listarTodos()) {
            model.addRow(new Object[]{p.getCodigo(), p.getNombre(), p.getPrecio()});
        }
    }

    private void buscarEnEliminar() {
        String filtro = vistaElim.getComboFiltro().getSelectedItem().toString();
        String texto = vistaElim.getTxtBusqueda().getText().trim().toLowerCase();

        List<Producto> lista = productoDAO.listarTodos();
        List<Producto> filtrados = lista.stream().filter(p -> {
            if (filtro.equals("Código")) {
                return String.valueOf(p.getCodigo()).contains(texto);
            } else {
                return p.getNombre().toLowerCase().contains(texto);
            }
        }).toList();

        DefaultTableModel model = vistaElim.getModelResultado();
        model.setRowCount(0);
        for (Producto p : filtrados) {
            model.addRow(new Object[]{p.getCodigo(), p.getNombre(), p.getPrecio()});
        }
    }

    private void eliminarProducto() {
        int fila = vistaElim.getTablaResultado().getSelectedRow();
        if (fila == -1) {
            vistaElim.mostrarMensaje("Seleccione un producto para eliminar.");
            return;
        }

        int codigo = Integer.parseInt(vistaElim.getTablaResultado().getValueAt(fila, 0).toString());
        productoDAO.eliminar(codigo);
        vistaElim.mostrarMensaje("Producto eliminado correctamente.");
        cargarTablaEliminar();
    }
}
