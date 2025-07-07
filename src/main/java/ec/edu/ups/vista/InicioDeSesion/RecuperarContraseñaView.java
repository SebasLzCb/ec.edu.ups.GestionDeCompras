package ec.edu.ups.vista.InicioDeSesion;

import ec.edu.ups.util.MensajeInternacionalizacionHandler;

import javax.swing.*;
import java.util.List;

public class RecuperarContraseñaView extends JFrame {

    private final MensajeInternacionalizacionHandler mensajeHandler;

    private JPanel panelPrincipal;
    private JTextField txtUsuario;
    private JComboBox<String> cbxPregunta1;
    private JTextField txtRespuesta1;
    private JButton btnSiguiente;
    private JButton btnCancelar;

    public RecuperarContraseñaView(String username,
                                   List<String> preguntas,
                                   MensajeInternacionalizacionHandler mensajeHandler) {
        super(mensajeHandler.get("recuperar.titulo"));
        this.mensajeHandler = mensajeHandler;

        setContentPane(panelPrincipal);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(450, 400);
        setLocationRelativeTo(null);
        setResizable(false);

        txtUsuario.setText(username);
        txtUsuario.setEditable(false);
        setPreguntas(preguntas);
        actualizarIdioma();
    }

    public RecuperarContraseñaView(MensajeInternacionalizacionHandler mensajeHandler,
                                   List<String> preguntas) {
        this("", preguntas, mensajeHandler);
    }

    public void actualizarIdioma() {
        setTitle(mensajeHandler.get("recuperar.titulo"));
        btnSiguiente.setText(mensajeHandler.get("recuperar.siguiente"));
        btnCancelar.setText(mensajeHandler.get("recuperar.cancelar"));
    }

    public void setPreguntas(List<String> preguntas) {
        cbxPregunta1.removeAllItems();
        for (String preg : preguntas) {
            cbxPregunta1.addItem(preg);
        }
    }

    public JTextField getTxtUsuario() {
        return txtUsuario;
    }

    public String[] getPreguntas() {
        return new String[] {
                (String) cbxPregunta1.getSelectedItem()
        };
    }

    public String[] getRespuestas() {
        return new String[] {
                txtRespuesta1.getText().trim()
        };
    }

    public JButton getBtnSiguiente() {
        return btnSiguiente;
    }

    public JButton getBtnCancelar() {
        return btnCancelar;
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }
}
