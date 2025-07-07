package ec.edu.ups.vista.Usuario;

import ec.edu.ups.util.MensajeInternacionalizacionHandler;

import javax.swing.*;
import java.util.List;

public class UsuarioRegistroView extends JInternalFrame {

    private final MensajeInternacionalizacionHandler mensajeHandler;

    private JPanel panelPrincipal;
    private JTextField txtUsuario;
    private JPasswordField txtPassword;
    private JLabel lblUsuario;
    private JLabel lblPassword;
    private JButton btnCrear;
    private JButton btnCancelar;
    private JComboBox<String> cbxPregunta1;
    private JComboBox<String> cbxPregunta2;
    private JComboBox<String> cbxPregunta3;
    private JTextField txtNombresComp;
    private JTextField txtTelefono;
    private JTextField txtCorreo;
    private JTextField txtRespuesta1;
    private JTextField txtRespuesta2;
    private JTextField txtRespuesta3;
    private JLabel lblPregunta1;
    private JLabel lblPregunta2;
    private JLabel lblPregunta3;
    private JLabel lblPreguntas;
    private JLabel lblNombresComp;
    private JLabel lblTelefono;
    private JLabel lblCorreo;
    private JLabel lblDia;
    private JLabel lblMes;
    private JLabel lblAño;
    private JSpinner spnDia;
    private JSpinner spnMes;
    private JSpinner spnAño;

    public UsuarioRegistroView(MensajeInternacionalizacionHandler mensajeHandler) {
        super(mensajeHandler.get("usuario.view.registrar.titulo"));
        this.mensajeHandler = mensajeHandler;
        setContentPane(panelPrincipal);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
        setResizable(false);
        actualizarIdioma();
    }

    public void setPreguntas(List<String> preguntas) {
        cbxPregunta1.removeAllItems();
        cbxPregunta2.removeAllItems();
        cbxPregunta3.removeAllItems();
        for (String pregunta : preguntas) {
            cbxPregunta1.addItem(pregunta);
            cbxPregunta2.addItem(pregunta);
            cbxPregunta3.addItem(pregunta);
        }
    }

    public void actualizarIdioma() {
        setTitle(mensajeHandler.get("usuario.view.registrar.titulo"));
        lblUsuario.setText(mensajeHandler.get("usuario.view.registrar.username") + ":");
        lblPassword.setText(mensajeHandler.get("usuario.view.registrar.password") + ":");
        btnCrear.setText(mensajeHandler.get("usuario.view.registrar.crear"));
        btnCancelar.setText(mensajeHandler.get("usuario.view.registrar.cancelar"));
    }

    public String getTxtUsuario() { return txtUsuario.getText().trim(); }
    public String getTxtPassword() { return new String(txtPassword.getPassword()); }
    public JComboBox<String> getCbxPregunta1() { return cbxPregunta1; }
    public String getTxtRespuesta1() { return txtRespuesta1.getText().trim(); }
    public JComboBox<String> getCbxPregunta2() { return cbxPregunta2; }
    public String getTxtRespuesta2() { return txtRespuesta2.getText().trim(); }
    public JComboBox<String> getCbxPregunta3() { return cbxPregunta3; }
    public String getTxtRespuesta3() { return txtRespuesta3.getText().trim(); }
    public JButton getBtnCrear() { return btnCrear; }
    public JButton getBtnCancelar() { return btnCancelar; }

    public void limpiarCampos() {
        txtUsuario.setText("");
        txtPassword.setText("");
        txtRespuesta1.setText("");
        txtRespuesta2.setText("");
        txtRespuesta3.setText("");
        if (cbxPregunta1.getItemCount()>0) cbxPregunta1.setSelectedIndex(0);
        if (cbxPregunta2.getItemCount()>0) cbxPregunta2.setSelectedIndex(0);
        if (cbxPregunta3.getItemCount()>0) cbxPregunta3.setSelectedIndex(0);
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }
}
