package ec.edu.ups.vista.Producto;

import ec.edu.ups.dao.ProductoDAO;
import ec.edu.ups.util.MensajeInternacionalizacionHandler;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ProductoElimView extends JInternalFrame {

    private final ProductoDAO productoDAO;
    private final MensajeInternacionalizacionHandler mensajeHandler;

    private JPanel panelPrincipal;
    private JComboBox<String> comboFiltro;
    private JTextField txtBusqueda;
    private JButton btnBuscar;
    private JTable tablaResultado;
    private JButton btnEliminar;
    private DefaultTableModel modelResultado;

    public ProductoElimView(ProductoDAO dao, MensajeInternacionalizacionHandler mensajeHandler) {
        this.productoDAO = dao;
        this.mensajeHandler = mensajeHandler;

        setContentPane(panelPrincipal);
        setClosable(true);
        setIconifiable(true);
        setResizable(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(600, 400);

        comboFiltro.addItem(mensajeHandler.get("producto.view.eliminar.filtro.nombre"));
        comboFiltro.addItem(mensajeHandler.get("producto.view.eliminar.filtro.codigo"));

        modelResultado = new DefaultTableModel(new Object[]{
                mensajeHandler.get("producto.view.modificar.codigo"),
                mensajeHandler.get("producto.view.modificar.nombre"),
                mensajeHandler.get("producto.view.modificar.precio")
        }, 0);
        tablaResultado.setModel(modelResultado);

        actualizarIdioma();
    }

    public void actualizarIdioma() {
        setTitle(mensajeHandler.get("producto.view.eliminar.titulo"));
        btnBuscar.setText(mensajeHandler.get("producto.view.eliminar.buscar"));
        btnEliminar.setText(mensajeHandler.get("producto.view.eliminar.eliminar"));

        comboFiltro.removeAllItems();
        comboFiltro.addItem(mensajeHandler.get("producto.view.eliminar.filtro.nombre"));
        comboFiltro.addItem(mensajeHandler.get("producto.view.eliminar.filtro.codigo"));

        modelResultado.setColumnIdentifiers(new Object[]{
                mensajeHandler.get("producto.view.modificar.codigo"),
                mensajeHandler.get("producto.view.modificar.nombre"),
                mensajeHandler.get("producto.view.modificar.precio")
        });
    }

    public JTable getTablaResultado() {
        return tablaResultado;
    }

    public JComboBox<String> getComboFiltro() {
        return comboFiltro;
    }

    public JTextField getTxtBusqueda() {
        return txtBusqueda;
    }

    public JButton getBtnBuscar() {
        return btnBuscar;
    }

    public JButton getBtnEliminar() {
        return btnEliminar;
    }

    public DefaultTableModel getModelResultado() {
        return modelResultado;
    }

    public void setModelResultado(DefaultTableModel modelResultado) {
        this.modelResultado = modelResultado;
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }
}
