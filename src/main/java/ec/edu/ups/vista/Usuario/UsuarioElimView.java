package ec.edu.ups.vista.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class UsuarioElimView extends JInternalFrame {

    private JPanel panelPrincipal;
    private JComboBox<String> cbxFiltro;
    private JTextField txtBuscar;
    private JButton btnBuscar;
    private JTable tblUsuarios;
    private JButton btnEliminar;
    private DefaultTableModel model;

    // Este componente lo agrega automáticamente el diseñador cuando usas JScrollPane
    private JScrollBar scrollBar1;

    public UsuarioElimView() {
        setContentPane(panelPrincipal);
        setTitle("Eliminar Usuario");
        setClosable(true);
        setIconifiable(true);
        setResizable(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(600, 400);

        // Inicializar modelo y tabla
        model = new DefaultTableModel(new Object[]{"Username", "Rol"}, 0);
        tblUsuarios.setModel(model);

        // Agregar filtro (puede hacerse en el diseñador también)
        cbxFiltro.addItem("Username");
    }

    // Getters
    public JTable getTableUsuarios() {
        return tblUsuarios;
    }

    public DefaultTableModel getTableModel() {
        return model;
    }

    public JButton getBtnEliminar() {
        return btnEliminar;
    }

    public JComboBox<String> getCbxFiltro() {
        return cbxFiltro;
    }

    public JTextField getTxtBuscar() {
        return txtBuscar;
    }

    public JButton getBtnBuscar() {
        return btnBuscar;
    }

    public JPanel getPanelPrincipal() {
        return panelPrincipal;
    }

    public JScrollBar getScrollBar1() {
        return scrollBar1;
    }

    public void setTableModel(DefaultTableModel model) {
        this.model = model;
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }
}
