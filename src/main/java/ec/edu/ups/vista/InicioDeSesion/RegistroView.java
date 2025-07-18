package ec.edu.ups.vista.InicioDeSesion;

import ec.edu.ups.util.MensajeInternacionalizacionHandler;
import ec.edu.ups.modelo.IRegistroView;

import javax.swing.*;
import java.time.LocalDate;
import java.util.List;

public class RegistroView extends JFrame implements IRegistroView { // IMPLEMENTA IRegistroView
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

        System.out.println("DEBUG: RegistroView constructor inicia.");

        setContentPane(panelPrincipal);
        System.out.println("DEBUG: setContentPane llamado.");

        // Verificación de Nulos inicial de componentes esenciales
        if (panelPrincipal == null) System.err.println("ERROR DEBUG: panelPrincipal es NULL en constructor de RegistroView.");
        if (btnCrear == null) System.err.println("ERROR DEBUG: btnCrear es NULL en constructor de RegistroView.");
        if (btnCancelar == null) System.err.println("ERROR DEBUG: btnCancelar es NULL en constructor de RegistroView.");

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setSize(600, 400);
        setResizable(false);

        // Asegurarse de que los spinners no son null antes de configurar el modelo
        if (spnDia != null) spnDia.setModel(new SpinnerNumberModel(1, 1, 31, 1));
        if (spnMes != null) spnMes.setModel(new SpinnerNumberModel(1, 1, 12, 1));
        if (spnAño != null) spnAño.setModel(new SpinnerNumberModel(2000, 1900, LocalDate.now().getYear(), 1));

        actualizarIdioma();
        System.out.println("DEBUG: RegistroView constructor finaliza.");
    }

    // AÑADIR ESTE MÉTODO - Implementación de createUIComponents()
    private void createUIComponents() {
        panelPrincipal = new JPanel();
    }

    @Override // Implementación de IRegistroView
    public void setPreguntas(List<String> preguntas) {
        if (cbxPregunta1 != null) cbxPregunta1.removeAllItems();
        if (cbxPregunta2 != null) cbxPregunta2.removeAllItems();
        if (cbxPregunta3 != null) cbxPregunta3.removeAllItems();

        String itemPorDefecto = mensajeHandler.get("usuario.view.registrar.seleccione_pregunta");
        if (cbxPregunta1 != null) cbxPregunta1.addItem(itemPorDefecto);
        if (cbxPregunta2 != null) cbxPregunta2.addItem(itemPorDefecto);
        if (cbxPregunta3 != null) cbxPregunta3.addItem(itemPorDefecto);

        for (String p : preguntas) {
            if (cbxPregunta1 != null) cbxPregunta1.addItem(p);
            if (cbxPregunta2 != null) cbxPregunta2.addItem(p);
            if (cbxPregunta3 != null) cbxPregunta3.addItem(p);
        }
    }

    @Override // Implementación de IRegistroView
    public void actualizarIdioma() {
        setTitle(mensajeHandler.get("usuario.view.registrar.titulo"));
        if (lblUsuario != null) lblUsuario.setText(mensajeHandler.get("usuario.view.registrar.username") + ":");
        if (lblPassword != null) lblPassword.setText(mensajeHandler.get("usuario.view.registrar.password") + ":");

        if (lblNombresComp != null) lblNombresComp.setText(mensajeHandler.get("usuario.view.registrar.nombres") + ":");
        if (lblCorreo != null) lblCorreo.setText(mensajeHandler.get("usuario.view.registrar.correo") + ":");
        if (lblTelefono != null) lblTelefono.setText(mensajeHandler.get("usuario.view.registrar.telefono") + ":");
        if (lblPreguntas != null) lblPreguntas.setText(mensajeHandler.get("usuario.view.registrar.preguntas") + ":");
        if (lblPregunta1 != null) lblPregunta1.setText(mensajeHandler.get("usuario.view.registrar.pregunta1") + ":");
        if (lblPregunta2 != null) lblPregunta2.setText(mensajeHandler.get("usuario.view.registrar.pregunta2") + ":");
        if (lblPregunta3 != null) lblPregunta3.setText(mensajeHandler.get("usuario.view.registrar.pregunta3") + ":");
        if (lblDia != null) lblDia.setText(mensajeHandler.get("usuario.view.registrar.dia") + ":");
        if (lblMes != null) lblMes.setText(mensajeHandler.get("usuario.view.registrar.mes") + ":");
        if (lblAño != null) lblAño.setText(mensajeHandler.get("usuario.view.registrar.ano") + ":");

        if (btnCrear != null) btnCrear.setText(mensajeHandler.get("usuario.view.registrar.crear"));
        if (btnCancelar != null) btnCancelar.setText(mensajeHandler.get("usuario.view.registrar.cancelar"));

        String itemPorDefecto = mensajeHandler.get("usuario.view.registrar.seleccione_pregunta");
        if (cbxPregunta1 != null && cbxPregunta1.getItemCount() > 0 && cbxPregunta1.getItemAt(0).contains("Seleccione")) {
            cbxPregunta1.insertItemAt(itemPorDefecto, 0);
            cbxPregunta1.removeItemAt(1);
            cbxPregunta1.setSelectedIndex(0);
        }
        if (cbxPregunta2 != null && cbxPregunta2.getItemCount() > 0 && cbxPregunta2.getItemAt(0).contains("Seleccione")) {
            cbxPregunta2.insertItemAt(itemPorDefecto, 0);
            cbxPregunta2.removeItemAt(1);
            cbxPregunta2.setSelectedIndex(0);
        }
        if (cbxPregunta3 != null && cbxPregunta3.getItemCount() > 0 && cbxPregunta3.getItemAt(0).contains("Seleccione")) {
            cbxPregunta3.insertItemAt(itemPorDefecto, 0);
            cbxPregunta3.removeItemAt(1);
            cbxPregunta3.setSelectedIndex(0);
        }
    }

    @Override // Implementación de IRegistroView
    public String getTxtUsuario() { return (txtUsuario != null) ? txtUsuario.getText().trim() : ""; }
    @Override // Implementación de IRegistroView
    public String getTxtPassword() { return (txtPassword != null) ? new String(txtPassword.getPassword()) : ""; }
    @Override // Implementación de IRegistroView
    public JComboBox<String> getCbxPregunta1() { return cbxPregunta1; }
    @Override // Implementación de IRegistroView
    public String getTxtRespuesta1() { return (txtRespuesta1 != null) ? txtRespuesta1.getText().trim() : ""; }
    @Override // Implementación de IRegistroView
    public JComboBox<String> getCbxPregunta2() { return cbxPregunta2; }
    @Override // Implementación de IRegistroView
    public String getTxtRespuesta2() { return (txtRespuesta2 != null) ? txtRespuesta2.getText().trim() : ""; }
    @Override // Implementación de IRegistroView
    public JComboBox<String> getCbxPregunta3() { return cbxPregunta3; }
    @Override // Implementación de IRegistroView
    public String getTxtRespuesta3() { return (txtRespuesta3 != null) ? txtRespuesta3.getText().trim() : ""; }
    @Override // Implementación de IRegistroView
    public JButton getBtnCrear() { return btnCrear; }
    @Override // Implementación de IRegistroView
    public JButton getBtnCancelar() { return btnCancelar; }

    @Override // Implementación de IRegistroView
    public String getTxtNombresCompletos() { return (txtNombresComp != null) ? txtNombresComp.getText().trim() : ""; }
    @Override // Implementación de IRegistroView
    public String getTxtCorreo() { return (txtCorreo != null) ? txtCorreo.getText().trim() : ""; }
    @Override // Implementación de IRegistroView
    public String getTxtTelefono() { return (txtTelefono != null) ? txtTelefono.getText().trim() : ""; }

    @Override // Implementación de IRegistroView
    public LocalDate getFechaNacimiento() {
        try {
            if (spnDia != null && spnMes != null && spnAño != null) {
                int dia = (Integer) spnDia.getValue();
                int mes = (Integer) spnMes.getValue();
                int año = (Integer) spnAño.getValue();
                return LocalDate.of(año, mes, dia);
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    @Override // Implementación de IRegistroView
    public void limpiarCampos() {
        if (txtUsuario != null) txtUsuario.setText("");
        if (txtPassword != null) txtPassword.setText("");
        if (txtNombresComp != null) txtNombresComp.setText("");
        if (txtCorreo != null) txtCorreo.setText("");
        if (txtTelefono != null) txtTelefono.setText("");
        if (txtRespuesta1 != null) txtRespuesta1.setText("");
        if (txtRespuesta2 != null) txtRespuesta2.setText("");
        if (txtRespuesta3 != null) txtRespuesta3.setText("");

        if (spnDia != null) spnDia.setValue(1);
        if (spnMes != null) spnMes.setValue(1);
        if (spnAño != null) spnAño.setValue(2000);

        if (cbxPregunta1 != null && cbxPregunta1.getItemCount() > 0) cbxPregunta1.setSelectedIndex(0);
        if (cbxPregunta2 != null && cbxPregunta2.getItemCount() > 0) cbxPregunta2.setSelectedIndex(0);
        if (cbxPregunta3 != null && cbxPregunta3.getItemCount() > 0) cbxPregunta3.setSelectedIndex(0);
    }

    @Override // Implementación de IRegistroView
    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }

    @Override // Implementación de IRegistroView (para JFrame)
    public void dispose() {
        super.dispose();
    }

    @Override // Implementación de IRegistroView (para JFrame)
    public void setVisible(boolean visible) {
        super.setVisible(visible);
    }
}