package ec.edu.ups.vista.InicioDeSesion;

import ec.edu.ups.controlador.RecuperacionController;
import ec.edu.ups.util.MensajeInternacionalizacionHandler;
import ec.edu.ups.vista.Principal;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.List;

public class LoginView extends JFrame {
    private final MensajeInternacionalizacionHandler mensajeHandler;
    private final RecuperacionController recuperacionController;
    private RegistroView registroFrame; // Este es el JFrame de registro
    private Principal principal;

    private JPanel panelPrincipal;
    private JLabel lblUsuario;
    private JLabel lblContraseña;
    private JTextField txtUsername;
    private JPasswordField txtContrasenia;
    private JButton btnIniciarSesion;
    private JButton btnRegistrarse;
    private JButton btnOlvCont;
    private JComboBox<String> cbxIdioma;
    private JComboBox<String> cbxStorageType;
    private JLabel lblStorageType;

    public LoginView(RecuperacionController recuperacionController,
                     MensajeInternacionalizacionHandler mensajeHandler) {
        super(mensajeHandler.get("login.titulo"));
        this.mensajeHandler = mensajeHandler;
        this.recuperacionController = recuperacionController;

        setContentPane(panelPrincipal);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // CAMBIO AQUÍ: DISPOSE_ON_CLOSE
        pack();
        setLocationRelativeTo(null);
        setResizable(false);

        // Configuración del JComboBox de idioma
        cbxIdioma.setModel(new DefaultComboBoxModel<>(new String[]{
                "Español", "English", "Français"
        }));
        switch (mensajeHandler.getLocale().getLanguage()) {
            case "en" -> cbxIdioma.setSelectedIndex(1);
            case "fr" -> cbxIdioma.setSelectedIndex(2);
            default   -> cbxIdioma.setSelectedIndex(0);
        }
        cbxIdioma.addActionListener(e -> {
            int idx = cbxIdioma.getSelectedIndex();
            switch (idx) {
                case 1 -> mensajeHandler.setLenguaje("en", "US");
                case 2 -> mensajeHandler.setLenguaje("fr", "FR");
                default -> mensajeHandler.setLenguaje("es", "EC");
            }
            actualizarIdioma();
            if (registroFrame != null) {
                registroFrame.actualizarIdioma();
            }
            if (principal != null) {
                principal.cambiarIdioma(
                        mensajeHandler.getLocale().getLanguage(),
                        mensajeHandler.getLocale().getCountry()
                );
            }
        });

        // Inicialización y configuración del JComboBox de tipo de almacenamiento
        cbxStorageType.setModel(new DefaultComboBoxModel<>(new String[]{
                mensajeHandler.get("login.storage.memory"),
                mensajeHandler.get("login.storage.file_system")
        }));

        btnOlvCont.addActionListener(e -> {
            String user = txtUsername.getText().trim();
            if (user.isEmpty()) {
                mostrarMensaje(mensajeHandler.get("error.usuario.required"));
                return;
            }
            recuperacionController.mostrarRecuperar(user);
        });

        btnRegistrarse.addActionListener(e -> {
            if (registroFrame != null) {
                List<String> preguntas = recuperacionController.obtenerPreguntasLocalizadas();
                registroFrame.setPreguntas(preguntas);
                registroFrame.limpiarCampos();
                registroFrame.setVisible(true);
            } else {
                mostrarMensaje("Error: La ventana de registro (JFrame) no fue inicializada.");
            }
        });

        actualizarIdioma();
    }

    public void setRegistroFrame(RegistroView registroFrame) {
        this.registroFrame = registroFrame;
    }

    public RegistroView getRegistroFrame() {
        return registroFrame;
    }

    public void setPrincipal(Principal principal) {
        this.principal = principal;
    }

    @Override
    public void setVisible(boolean b) {
        super.setVisible(b);
        if (b) {
            actualizarIdioma();
        }
    }

    public void actualizarIdioma() {
        setTitle(mensajeHandler.get("login.titulo"));
        lblUsuario   .setText(mensajeHandler.get("login.usuario") + ":");
        lblContraseña.setText(mensajeHandler.get("login.contrasenia") + ":");
        btnIniciarSesion.setText(mensajeHandler.get("login.iniciar"));
        btnRegistrarse  .setText(mensajeHandler.get("login.registrarse"));
        btnOlvCont      .setText(mensajeHandler.get("login.olvidarContrasenia"));

        lblStorageType.setText(mensajeHandler.get("login.storage.label") + ":");
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        model.addElement(mensajeHandler.get("login.storage.memory"));
        model.addElement(mensajeHandler.get("login.storage.file_system"));
        cbxStorageType.setModel(model);
    }

    public JTextField getTxtUsername()        { return txtUsername; }
    public JPasswordField getTxtContrasenia() { return txtContrasenia; }
    public JButton getBtnIniciarSesion()      { return btnIniciarSesion; }
    public JButton getBtnRegistrar()          { return btnRegistrarse; }
    public JButton getBtnOlvCont()            { return btnOlvCont; }

    public String getSelectedStorageType() {
        return (String) cbxStorageType.getSelectedItem();
    }

    public void addLoginActionListener(ActionListener listener) {
        btnIniciarSesion.addActionListener(listener);
    }

    public void mostrarMensaje(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }
    public void limpiarCampos() {
        txtUsername.setText("");
        txtContrasenia.setText("");
        if (cbxStorageType.getItemCount() > 0) {
            cbxStorageType.setSelectedIndex(0);
        }
    }
}