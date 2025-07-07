package ec.edu.ups.vista.Usuario;

import ec.edu.ups.util.MensajeInternacionalizacionHandler;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class UsuarioModView extends JInternalFrame {

    private final MensajeInternacionalizacionHandler mensajeHandler;

    private JPanel panelPrincipal;
    private JLabel lblUsername;
    private JTextField txtUsername;
    private JLabel lblPassword;
    private JPasswordField txtPassword;
    private JComboBox<String> cbxRol;
    private JButton btnBuscar;
    private JTable tblUsuarios;
    private JButton btnActualizar;
    private JButton btnCancelar;
    private JScrollPane scrollPane;

    private DefaultTableModel model;

    public UsuarioModView(MensajeInternacionalizacionHandler mensajeHandler) {
        super(mensajeHandler.get("usuario.view.modificar.titulo"), true, true, true, true);
        this.mensajeHandler = mensajeHandler;

        setContentPane(panelPrincipal);
        setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
        setSize(600, 400);
        setClosable(true);
        setIconifiable(true);
        setResizable(true);

        model = new DefaultTableModel();
        tblUsuarios.setModel(model);

        actualizarIdioma();
    }

    public void actualizarIdioma() {
        setTitle(mensajeHandler.get("usuario.view.modificar.titulo"));

        lblUsername.setText(mensajeHandler.get("usuario.view.modificar.username") + ":");
        lblPassword.setText(mensajeHandler.get("usuario.view.modificar.password") + ":");

        btnBuscar    .setText(mensajeHandler.get("usuario.view.modificar.buscar"));
        btnActualizar.setText(mensajeHandler.get("usuario.view.modificar.actualizar"));
        btnCancelar  .setText(mensajeHandler.get("usuario.view.modificar.cancelar"));

        cbxRol.removeAllItems();
        cbxRol.addItem(mensajeHandler.get("usuario.view.modificar.rol.admin"));
        cbxRol.addItem(mensajeHandler.get("usuario.view.modificar.rol.usuario"));

        model.setColumnIdentifiers(new Object[]{
            mensajeHandler.get("usuario.view.modificar.username"),
            mensajeHandler.get("usuario.view.modificar.rol")
        });
    }

    public String getTxtUsername()       { return txtUsername.getText().trim(); }
    public String getTxtPassword()       { return new String(txtPassword.getPassword()); }
    public JComboBox<String> getCbxRol() { return cbxRol; }
    public JButton getBtnBuscar()        { return btnBuscar; }
    public JButton getBtnActualizar()    { return btnActualizar; }
    public JButton getBtnCancelar()      { return btnCancelar; }
    public JTable getTblUsuarios()       { return tblUsuarios; }

    public DefaultTableModel getTableModel() {
        return model;
    }

    public void limpiarCampos() {
        txtUsername.setText("");
        txtPassword.setText("");
        if (cbxRol.getItemCount() > 0) cbxRol.setSelectedIndex(0);
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }

    private void createUIComponents() {
        scrollPane = new JScrollPane();
    }

}
