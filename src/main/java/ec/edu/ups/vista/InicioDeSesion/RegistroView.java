package ec.edu.ups.vista.InicioDeSesion;

import ec.edu.ups.util.MensajeInternacionalizacionHandler;

import javax.swing.*;
import java.util.List;

public class RegistroView extends JFrame {
    private final MensajeInternacionalizacionHandler mensajeHandler;

    private JPanel panelPrincipal;
    private JTextField txtUsuario;
    private JPasswordField txtPassword;
    private JLabel lblUsuario, lblPassword, lblCorreo, lblNombresComp, lblTelefono;
    private JButton btnCrear, btnCancelar;
    private JComboBox<String> cbxPregunta1, cbxPregunta2, cbxPregunta3;
    private JTextField txtNombresComp, txtTelefono, txtCorreo;
    private JTextField txtRespuesta1, txtRespuesta2, txtRespuesta3;
    private JLabel lblPregunta1, lblPregunta2, lblPregunta3, lblPreguntas;
    private JSpinner spnDia, spnMes, spnAño;
    private JLabel lblDia, lblMes, lblAño;

    public RegistroView(MensajeInternacionalizacionHandler mensajeHandler) {
        super(mensajeHandler.get("usuario.view.registrar.titulo"));
        this.mensajeHandler = mensajeHandler;
        setContentPane(panelPrincipal);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setSize(600, 400);
        setResizable(false);

        spnDia.setModel(new SpinnerNumberModel(1, 1, 31, 1));
        spnMes.setModel(new SpinnerNumberModel(1, 1, 12, 1));
        spnAño.setModel(new SpinnerNumberModel(2000, 1900, 2023, 1));

        actualizarIdioma();
    }

    public void setPreguntas(List<String> preguntas) {
        cbxPregunta1.removeAllItems();
        cbxPregunta2.removeAllItems();
        cbxPregunta3.removeAllItems();
        for (String p : preguntas) {
            cbxPregunta1.addItem(p);
            cbxPregunta2.addItem(p);
            cbxPregunta3.addItem(p);
        }
    }

    public void actualizarIdioma() {
        setTitle(mensajeHandler.get("usuario.view.registrar.titulo"));
        lblUsuario.setText(mensajeHandler.get("usuario.view.registrar.username") + ":");
        lblPassword.setText(mensajeHandler.get("usuario.view.registrar.password") + ":");
        lblPregunta1.setText(mensajeHandler.get("usuario.view.registrar.pregunta1") + ":");
        lblPregunta2.setText(mensajeHandler.get("usuario.view.registrar.pregunta2") + ":");
        lblPregunta3.setText(mensajeHandler.get("usuario.view.registrar.pregunta3") + ":");
        btnCrear.setText(mensajeHandler.get("usuario.view.registrar.crear"));
        btnCancelar.setText(mensajeHandler.get("usuario.view.registrar.cancelar"));
    }

    // getters...
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
