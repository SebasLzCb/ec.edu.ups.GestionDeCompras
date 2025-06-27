package ec.edu.ups.vista.Usuario;

import ec.edu.ups.modelo.Rol;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class UsuarioModView extends JInternalFrame {

    private JPanel panelPrincipal;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JComboBox<String> cbxRol;
    private JButton btnBuscar;
    private JButton btnActualizar;
    private JButton btnCancelar;
    private JTable tblUsuarios;
    private JScrollBar scrollBar1;
    private DefaultTableModel model;
    private JScrollPane scrollPane;

    public UsuarioModView() {
        setContentPane(panelPrincipal);
        setTitle("Modificar Usuario");
        setClosable(true);
        setIconifiable(true);
        setResizable(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(600, 400);

        cbxRol.addItem(Rol.ADMINISTRADOR.name());
        cbxRol.addItem(Rol.USUARIO.name());

        model = new DefaultTableModel(new Object[]{"Username", "Rol"}, 0);
        tblUsuarios.setModel(model);
    }

    public JTextField getTxtUsername() {
        return txtUsername;
    }

    public JPasswordField getTxtPassword() {
        return txtPassword;
    }

    public JComboBox<String> getCbxRol() {
        return cbxRol;
    }

    public JButton getBtnBuscar() {
        return btnBuscar;
    }

    public JButton getBtnActualizar() {
        return btnActualizar;
    }

    public JButton getBtnCancelar() {
        return btnCancelar;
    }

    public JTable getTableUsuarios() {
        return tblUsuarios;
    }

    public DefaultTableModel getTableModel() {
        return model;
    }

    public void setTableModel(DefaultTableModel model) {
        this.model = model;
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }

    public void limpiarCampos() {
        txtUsername.setText("");
        txtPassword.setText("");
        cbxRol.setSelectedIndex(0);
    }
}
