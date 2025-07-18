package ec.edu.ups.modelo;

import javax.swing.*;
import java.time.LocalDate;
import java.util.List;

/**
 * La interfaz {@code IRegistroView} define un contrato para las vistas de registro de usuario
 * en la aplicación. Esto permite que diferentes implementaciones de vistas (por ejemplo,
 * un {@code JFrame} de registro o un {@code JInternalFrame} de registro de usuario interno)
 * puedan ser manejadas de manera uniforme por el controlador.
 *
 * <p>Proporciona métodos para obtener datos de entrada del usuario, interactuar con componentes
 * de la UI como botones y comboboxes, y controlar la visibilidad y el estado de la vista.</p>
 */
public interface IRegistroView {

    /**
     * Obtiene el texto ingresado en el campo de nombre de usuario.
     *
     * @return El nombre de usuario como una cadena de texto.
     */
    String getTxtUsuario();

    /**
     * Obtiene el texto ingresado en el campo de contraseña.
     *
     * @return La contraseña como una cadena de texto.
     */
    String getTxtPassword();

    /**
     * Obtiene el texto ingresado en el campo de nombres completos.
     *
     * @return Los nombres completos como una cadena de texto.
     */
    String getTxtNombresCompletos();

    /**
     * Obtiene el texto ingresado en el campo de correo electrónico.
     *
     * @return El correo electrónico como una cadena de texto.
     */
    String getTxtCorreo();

    /**
     * Obtiene el texto ingresado en el campo de número de teléfono.
     *
     * @return El número de teléfono como una cadena de texto.
     */
    String getTxtTelefono();

    /**
     * Obtiene la fecha de nacimiento seleccionada por el usuario.
     *
     * @return Un objeto {@link LocalDate} que representa la fecha de nacimiento.
     */
    LocalDate getFechaNacimiento();

    /**
     * Obtiene el JComboBox para la primera pregunta de seguridad.
     *
     * @return El {@link JComboBox} de la pregunta 1.
     */
    JComboBox<String> getCbxPregunta1();

    /**
     * Obtiene el JComboBox para la segunda pregunta de seguridad.
     *
     * @return El {@link JComboBox} de la pregunta 2.
     */
    JComboBox<String> getCbxPregunta2();

    /**
     * Obtiene el JComboBox para la tercera pregunta de seguridad.
     *
     * @return El {@link JComboBox} de la pregunta 3.
     */
    JComboBox<String> getCbxPregunta3();

    /**
     * Obtiene el texto ingresado en el campo de respuesta para la primera pregunta de seguridad.
     *
     * @return La respuesta 1 como una cadena de texto.
     */
    String getTxtRespuesta1();

    /**
     * Obtiene el texto ingresado en el campo de respuesta para la segunda pregunta de seguridad.
     *
     * @return La respuesta 2 como una cadena de texto.
     */
    String getTxtRespuesta2();

    /**
     * Obtiene el texto ingresado en el campo de respuesta para la tercera pregunta de seguridad.
     *
     * @return La respuesta 3 como una cadena de texto.
     */
    String getTxtRespuesta3();

    /**
     * Obtiene el botón de "Crear" o "Registrar" de la vista.
     *
     * @return El {@link JButton} de creación.
     */
    JButton getBtnCrear();

    /**
     * Obtiene el botón de "Cancelar" de la vista.
     *
     * @return El {@link JButton} de cancelar.
     */
    JButton getBtnCancelar();

    /**
     * Establece la lista de preguntas de seguridad disponibles en los JComboBoxes de la vista.
     *
     * @param preguntas Una lista de cadenas de texto que representan las preguntas.
     */
    void setPreguntas(List<String> preguntas);

    /**
     * Actualiza los textos de todos los componentes de la vista según el idioma configurado
     * en el manejador de internacionalización.
     */
    void actualizarIdioma();

    /**
     * Limpia todos los campos de entrada de la vista, restableciéndolos a su estado inicial.
     */
    void limpiarCampos();

    /**
     * Muestra un mensaje informativo o de error al usuario, típicamente en un cuadro de diálogo.
     *
     * @param mensaje La cadena de texto del mensaje a mostrar.
     */
    void mostrarMensaje(String mensaje);

    /**
     * Cierra o descarta la vista actual.
     */
    void dispose();

    /**
     * Establece la visibilidad de la vista.
     *
     * @param visible {@code true} para hacer la vista visible, {@code false} para ocultarla.
     */
    void setVisible(boolean visible);

}