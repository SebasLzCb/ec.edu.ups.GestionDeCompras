package ec.edu.ups.vista.Usuario;

import ec.edu.ups.modelo.Rol;
import ec.edu.ups.util.MensajeInternacionalizacionHandler;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class UsuarioModView extends JInternalFrame {

    private MensajeInternacionalizacionHandler mensajeHandler;

    private JPanel panelPrincipal;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JComboBox<String> cbxRol;
    private JButton btnBuscar;
    private JButton btnActualizar;
    private JButton btnCancelar;
    private JTable tblUsuarios;
    private DefaultTableModel model;
    private JScrollPane scrollPane;

    public UsuarioModView(MensajeInternacionalizacionHandler mensajeHandler) {
        this.mensajeHandler = mensajeHandler;

        setContentPane(panelPrincipal);
        setClosable(true);
        setIconifiable(true);
        setResizable(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(600, 400);

        model = new DefaultTableModel(new Object[]{"Username", "Rol"}, 0);
        tblUsuarios.setModel(model);
    }

    public void actualizarIdioma() {
        if (mensajeHandler == null) return;

        setTitle(mensajeHandler.get("usuario.view.modificar.titulo"));

        if (btnBuscar != null) btnBuscar.setText(mensajeHandler.get("usuario.view.modificar.buscar"));
        if (btnActualizar != null) btnActualizar.setText(mensajeHandler.get("usuario.view.modificar.actualizar"));
        if (btnCancelar != null) btnCancelar.setText(mensajeHandler.get("usuario.view.modificar.cancelar"));

        if (cbxRol != null) {
            cbxRol.removeAllItems();
            cbxRol.addItem("ADMINISTRADOR");
            cbxRol.addItem("USUARIO");
        }
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
        tblUsuarios.setModel(model);
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
