package ec.edu.ups.vista.Usuario;

import javax.swing.*;

public class UsuarioRegistroView extends JInternalFrame {

    private JPanel panelPrincipal;
    private JTextField txtUsuario;
    private JPasswordField txtPassword;
    private JButton btnCrear;
    private JButton btnCancelar;

    public UsuarioRegistroView() {
        setContentPane(panelPrincipal);
        setTitle("Registrar Usuario");
        setClosable(true);
        setIconifiable(true);
        setResizable(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(400, 300);
    }

    public JTextField getTxtUsuario() {
        return txtUsuario;
    }

    public JPasswordField getTxtPassword() {
        return txtPassword;
    }

    public JButton getBtnCrear() {
        return btnCrear;
    }

    public JButton getBtnCancelar() {
        return btnCancelar;
    }

    public JPanel getPanelPrincipal() {
        return panelPrincipal;
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }

    public void limpiarCampos() {
        txtUsuario.setText("");
        txtPassword.setText("");
    }
}
