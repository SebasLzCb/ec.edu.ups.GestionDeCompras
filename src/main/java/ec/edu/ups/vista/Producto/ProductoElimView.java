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
    private JButton btnEliminar;
    private JTable tablaResultado;
    private DefaultTableModel modelResultado;

    public ProductoElimView(ProductoDAO dao,
                            MensajeInternacionalizacionHandler mensajeHandler) {
        super(mensajeHandler.get("producto.view.eliminar.titulo"), true, true, true, true);
        this.productoDAO    = dao;
        this.mensajeHandler = mensajeHandler;

        // Monta el panel generado por el .form
        setContentPane(panelPrincipal);
        setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
        setSize(600, 400);
        setClosable(true);
        setIconifiable(true);
        setResizable(true);

        // Inicializa modelo y tabla
        modelResultado = new DefaultTableModel();
        tablaResultado.setModel(modelResultado);

        // Carga textos y encabezados
        actualizarIdioma();
    }

    public void actualizarIdioma() {
        setTitle(mensajeHandler.get("producto.view.eliminar.titulo"));
        btnBuscar .setText(mensajeHandler.get("producto.view.eliminar.buscar"));
        btnEliminar.setText(mensajeHandler.get("producto.view.eliminar.eliminar"));

        comboFiltro.removeAllItems();
        comboFiltro.addItem(mensajeHandler.get("producto.view.eliminar.filtro.nombre"));
        comboFiltro.addItem(mensajeHandler.get("producto.view.eliminar.filtro.codigo"));

        modelResultado.setColumnIdentifiers(new Object[]{
                mensajeHandler.get("producto.view.eliminar.codigo"),
                mensajeHandler.get("producto.view.eliminar.nombre"),
                mensajeHandler.get("producto.view.eliminar.precio")
        });
    }

    // Getters para el controlador
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

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }
}
