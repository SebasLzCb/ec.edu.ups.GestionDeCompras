package ec.edu.ups.vista.InicioDeSesion;

import ec.edu.ups.controlador.RecuperacionController;
import ec.edu.ups.util.MensajeInternacionalizacionHandler;
import ec.edu.ups.vista.Principal;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

public class LoginView extends JFrame {
    private final MensajeInternacionalizacionHandler mensajeHandler;
    private final RecuperacionController recuperacionController;
    private RegistroView registroFrame;
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

    private JLabel lblRuta;
    private JTextField textRuta;
    private JButton btnSeleccionRuta;


    public LoginView(RecuperacionController recuperacionController,
                     MensajeInternacionalizacionHandler mensajeHandler) {
        super(mensajeHandler.get("login.titulo"));
        this.mensajeHandler = mensajeHandler;
        this.recuperacionController = recuperacionController;

        setContentPane(panelPrincipal);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setResizable(false);

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

        cbxStorageType.setModel(new DefaultComboBoxModel<>(new String[]{
                mensajeHandler.get("login.storage.memory"),
                mensajeHandler.get("login.storage.file_system"),
                mensajeHandler.get("login.storage.binary")
        }));

        cbxStorageType.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String tipoSeleccionadoKey = (String) cbxStorageType.getSelectedItem();
                boolean esArchivo = tipoSeleccionadoKey != null &&
                        (tipoSeleccionadoKey.equals(mensajeHandler.get("login.storage.file_system")) ||
                                tipoSeleccionadoKey.equals(mensajeHandler.get("login.storage.binary")));

                lblRuta.setVisible(esArchivo);
                textRuta.setVisible(esArchivo);
                btnSeleccionRuta.setVisible(esArchivo);

                if (esArchivo && textRuta.getText().isEmpty()) {
                    textRuta.setText("data" + File.separator);
                } else if (!esArchivo) {
                    textRuta.setText("");
                }
            }
        });
        cbxStorageType.setSelectedIndex(0);
        if (cbxStorageType.getActionListeners().length > 0) {
            cbxStorageType.getActionListeners()[0].actionPerformed(
                    new ActionEvent(cbxStorageType, ActionEvent.ACTION_PERFORMED, null)
            );
        }

        btnSeleccionRuta.addActionListener(new ActionListener() { // Usando su botón
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int opcion = fileChooser.showOpenDialog(LoginView.this);

                if (opcion == JFileChooser.APPROVE_OPTION) {
                    File directorioSeleccionado = fileChooser.getSelectedFile();
                    textRuta.setText(directorioSeleccionado.getAbsolutePath() + File.separator); // Usando su campo de texto
                }
            }
        });


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
                mostrarMensaje(mensajeHandler.get("error.registro_frame_no_inicializado"));
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
        lblUsuario.setText(mensajeHandler.get("login.usuario") + ":");
        lblContraseña.setText(mensajeHandler.get("login.contrasenia") + ":");
        btnIniciarSesion.setText(mensajeHandler.get("login.iniciar"));
        btnRegistrarse.setText(mensajeHandler.get("login.registrarse"));
        btnOlvCont.setText(mensajeHandler.get("login.olvidarContrasenia"));

        lblStorageType.setText(mensajeHandler.get("login.storage.label") + ":");
        lblRuta.setText(mensajeHandler.get("login.ruta_archivos") + ":"); // Usando su label
        btnSeleccionRuta.setText(mensajeHandler.get("login.boton.seleccionar_ruta")); // Usando su botón


        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        model.addElement(mensajeHandler.get("login.storage.memory"));
        model.addElement(mensajeHandler.get("login.storage.file_system"));
        model.addElement(mensajeHandler.get("login.storage.binary"));

        String seleccionActualKey = (String) cbxStorageType.getSelectedItem();
        cbxStorageType.setModel(model);

        if (seleccionActualKey != null && model.getIndexOf(seleccionActualKey) != -1) {
            cbxStorageType.setSelectedItem(seleccionActualKey);
        } else {
            cbxStorageType.setSelectedIndex(0);
        }

        if (cbxStorageType.getActionListeners().length > 0) {
            cbxStorageType.getActionListeners()[0].actionPerformed(
                    new ActionEvent(cbxStorageType, ActionEvent.ACTION_PERFORMED, null)
            );
        }
    }

    public JTextField getTxtUsername() {
        return txtUsername;
    }

    public JPasswordField getTxtContrasenia() {
        return txtContrasenia;
    }

    public JButton getBtnIniciarSesion() {
        return btnIniciarSesion;
    }

    public JButton getBtnRegistrar() {
        return btnRegistrarse;
    }

    public JButton getBtnOlvCont() {
        return btnOlvCont;
    }

    public String getSelectedStorageTypeKey() {
        return (String) cbxStorageType.getSelectedItem();
    }

    public String getRutaArchivos() {
        String ruta = textRuta.getText().trim();
        System.out.println("DEBUG (LoginView): Ruta de archivos obtenida de la UI: " + ruta);
        return ruta;
    }

    public void mostrarMensaje(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }

    public void limpiarCampos() {
        txtUsername.setText("");
        txtContrasenia.setText("");
        cbxStorageType.setSelectedIndex(0);
        textRuta.setText("data" + File.separator); // Usando su campo de texto
        lblRuta.setVisible(false); // Ocultar label
        textRuta.setVisible(false); // Ocultar campo de texto
        btnSeleccionRuta.setVisible(false); // Ocultar botón
    }
}