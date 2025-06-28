package ec.edu.ups.vista.Usuario;

import ec.edu.ups.util.MensajeInternacionalizacionHandler;

import javax.swing.*;

public class UsuarioRegistroView extends JInternalFrame {

    private MensajeInternacionalizacionHandler mensajeHandler;

    private JPanel panelPrincipal;
    private JTextField txtUsuario;
    private JPasswordField txtPassword;
    private JButton btnCrear;
    private JButton btnCancelar;

    public UsuarioRegistroView(MensajeInternacionalizacionHandler mensajeHandler) {
        this.mensajeHandler = mensajeHandler;
        setContentPane(panelPrincipal);
        setClosable(true);
        setIconifiable(true);
        setResizable(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(400, 300);

        actualizarIdioma();
    }

    public void actualizarIdioma() {
        setTitle(mensajeHandler.get("usuario.view.registrar.titulo"));
        btnCrear.setText(mensajeHandler.get("usuario.view.registrar.crear"));
        btnCancelar.setText(mensajeHandler.get("usuario.view.registrar.cancelar"));
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
