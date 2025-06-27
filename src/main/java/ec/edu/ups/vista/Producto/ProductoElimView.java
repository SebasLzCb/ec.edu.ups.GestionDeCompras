package ec.edu.ups.vista.Producto;

import ec.edu.ups.dao.ProductoDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ProductoElimView extends JInternalFrame {

    private final ProductoDAO productoDAO;

    private JPanel panelPrincipal;
    private JComboBox<String> comboFiltro;
    private JTextField txtBusqueda;
    private JButton btnBuscar;
    private JTable tablaResultado;
    private JScrollBar scrollBar1;
    private JButton btnEliminar;
    private DefaultTableModel modelResultado;

    public ProductoElimView(ProductoDAO dao) {
        this.productoDAO = dao;

        setContentPane(panelPrincipal);

        setTitle("Eliminar Producto");
        setClosable(true);
        setIconifiable(true);
        setResizable(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(600, 400);
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
