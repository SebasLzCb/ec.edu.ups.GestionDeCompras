package ec.edu.ups.modelo;

import javax.swing.*;
import java.time.LocalDate;
import java.util.List;

public interface IRegistroView {

    // Getters para campos de texto y contraseña
    String getTxtUsuario();
    String getTxtPassword();
    String getTxtNombresCompletos();
    String getTxtCorreo();
    String getTxtTelefono();

    // Getter para la fecha de nacimiento (JSpinner)
    LocalDate getFechaNacimiento();

    // Getters para ComboBoxes de preguntas de seguridad
    JComboBox<String> getCbxPregunta1();
    JComboBox<String> getCbxPregunta2();
    JComboBox<String> getCbxPregunta3();

    // Getters para respuestas de seguridad
    String getTxtRespuesta1();
    String getTxtRespuesta2();
    String getTxtRespuesta3();

    // Métodos para botones de acción
    JButton getBtnCrear();
    JButton getBtnCancelar();

    // Métodos de acción y utilidades de la vista
    void setPreguntas(List<String> preguntas);
    void actualizarIdioma();
    void limpiarCampos();
    void mostrarMensaje(String mensaje);
    void dispose(); // Para cerrar la ventana/internal frame
    void setVisible(boolean visible); // Para hacer visible/invisible

    // Métodos para JInternalFrame específicos, si es necesario, o se manejan de otra forma
    // En este caso, como JFrame y JInternalFrame tienen setContentPane, se asume que
    // el controlador no necesita directamente los componentes del panel.
}