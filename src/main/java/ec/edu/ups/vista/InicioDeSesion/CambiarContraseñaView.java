package ec.edu.ups.vista.InicioDeSesion;

import ec.edu.ups.dao.UsuarioDAO;
import ec.edu.ups.modelo.Usuario;
import ec.edu.ups.util.MensajeInternacionalizacionHandler;
import ec.edu.ups.util.ValidacionUtils;
import ec.edu.ups.excepciones.ValidacionException;

import javax.swing.*;

public class CambiarContraseñaView extends JFrame {
    private final MensajeInternacionalizacionHandler mensajeHandler;
    private final UsuarioDAO usuarioDAO;
    private final Usuario usuario;

    private JPanel panelPrincipal;
    private JLabel lblNombre, lblNuevaCont, lblRepCont;
    private JTextField txtNombre;
    private JPasswordField txtNuevaContrasena, txtRepetirContrasena;
    private JButton btnConfirmar, btnCancelar;

    public CambiarContraseñaView(Usuario usuario,
                                 UsuarioDAO usuarioDAO,
                                 MensajeInternacionalizacionHandler mensajeHandler) {
        super(mensajeHandler.get("contraseña.cambiar.titulo"));
        this.usuario = usuario;
        this.usuarioDAO = usuarioDAO;
        this.mensajeHandler = mensajeHandler;

        setContentPane(panelPrincipal);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setResizable(false);

        txtNombre.setText(usuario.getUsername());
        txtNombre.setEditable(false);
        actualizarIdioma();
        btnConfirmar.addActionListener(e -> onConfirmar());
        btnCancelar.addActionListener(e -> dispose());
    }

    private void onConfirmar() {
        String nueva = new String(txtNuevaContrasena.getPassword()).trim();
        String repetir = new String(txtRepetirContrasena.getPassword()).trim();

        try {
            ValidacionUtils.validarCampoObligatorio(nueva, mensajeHandler.get("contraseña.cambiar.nueva"));
            ValidacionUtils.validarCampoObligatorio(repetir, mensajeHandler.get("contraseña.cambiar.repetir"));
            ValidacionUtils.validarContrasenia(nueva); // Validar complejidad de la nueva contraseña

            if (!nueva.equals(repetir)) {
                throw new ValidacionException("error.contrasenia.noCoinciden");
            }

            usuario.setContrasenia(nueva);
            usuarioDAO.actualizar(usuario);

            mostrarMensaje(mensajeHandler.get("usuario.cambiarContrasenia.ok"));
            dispose();
        } catch (ValidacionException e) {
            mostrarMensaje(mensajeHandler.get(e.getMessage()));
        } catch (Exception e) {
            mostrarMensaje(mensajeHandler.get("error.inesperado") + ": " + e.getMessage());
        }
    }

    public void actualizarIdioma() {
        setTitle(mensajeHandler.get("contraseña.cambiar.titulo"));
        lblNombre.setText(mensajeHandler.get("contraseña.cambiar.usuario") + ":");
        lblNuevaCont.setText(mensajeHandler.get("contraseña.cambiar.nueva") + ":");
        lblRepCont.setText(mensajeHandler.get("contraseña.cambiar.repetir") + ":");
        btnConfirmar.setText(mensajeHandler.get("contraseña.cambiar.confirmar"));
        btnCancelar.setText(mensajeHandler.get("contraseña.cambiar.cancelar"));
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }
}