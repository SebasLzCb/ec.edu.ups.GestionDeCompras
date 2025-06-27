package ec.edu.ups.vista.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class UsuarioListaView extends JInternalFrame {

    private JPanel panelPrincipal;
    private JTable tblUsuario;
    private DefaultTableModel model;
    private JTextField txtBuscar;
    private JButton btnBuscar;
    private JButton btnRefrescar;
    private JComboBox<String> cbxFiltro;
    private JScrollBar scrollBar1;

    public UsuarioListaView() {
        setContentPane(panelPrincipal);
        setTitle("Lista de Usuarios");
        setClosable(true);
        setIconifiable(true);
        setResizable(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(600, 400);

        // ✅ Inicializa modelo y tabla
        model = new DefaultTableModel(new Object[]{"Username", "Rol"}, 0);
        tblUsuario.setModel(model);

        // ✅ Carga filtros
        cbxFiltro.addItem("Username");
        cbxFiltro.addItem("Rol");
    }

    // 🧱 Getters para uso en el controlador
    public JComboBox<String> getComboFiltro() {
        return cbxFiltro;
    }

    public JTextField getTxtBuscar() {
        return txtBuscar;
    }

    public JButton getBtnBuscar() {
        return btnBuscar;
    }

    public JButton getBtnRefrescar() {
        return btnRefrescar;
    }

    public JTable getTableUsuarios() {
        return tblUsuario;
    }

    public DefaultTableModel getTableModel() {
        return model;
    }

    public void setTableModel(DefaultTableModel model) {
        this.model = model;
        tblUsuario.setModel(model);
    }

    public JPanel getPanel() {
        return panelPrincipal;
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }
}
