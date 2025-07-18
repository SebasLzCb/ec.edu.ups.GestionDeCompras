package ec.edu.ups.dao;

import ec.edu.ups.modelo.Pregunta;
import ec.edu.ups.modelo.Respuesta;
import ec.edu.ups.modelo.Usuario; // Se importa Usuario

import java.util.List;

/**
 * La interfaz {@code RecuperacionDAO} define el contrato para las operaciones de acceso
 * a datos relacionadas con las preguntas y respuestas de seguridad utilizadas para la
 * recuperación de contraseñas de usuario.
 * Proporciona métodos para gestionar un banco de preguntas y almacenar/validar
 * las respuestas de los usuarios.
 */
public interface RecuperacionDAO {

    /**
     * Obtiene una lista de todas las preguntas disponibles en el banco de preguntas de seguridad.
     *
     * @return Una {@link List} de objetos {@link Pregunta} que representan el banco de preguntas.
     */
    List<Pregunta> getBancoPreguntas();

    /**
     * Valida una pregunta y respuesta de seguridad específica para un usuario dado.
     *
     * @param username El nombre de usuario del usuario cuyas respuestas se quieren validar.
     * @param pregunta La pregunta de seguridad a validar (texto de la pregunta).
     * @param respuesta La respuesta proporcionada por el usuario para la pregunta.
     * @return {@code true} si la combinación de usuario, pregunta y respuesta es válida;
     * {@code false} en caso contrario.
     */
    boolean validar(String username, String pregunta, String respuesta);

    /**
     * Obtiene una lista de todas las preguntas (como cadenas de texto) disponibles
     * en el banco de preguntas de seguridad.
     *
     * @return Una {@link List} de {@link String} con el texto de cada pregunta.
     */
    List<String> getPreguntas();

    /**
     * Obtiene una lista de las preguntas de seguridad que un usuario específico tiene asignadas.
     *
     * @param username El nombre de usuario del cual se desean obtener las preguntas.
     * @return Una {@link List} de {@link String} con las preguntas asignadas al usuario.
     * Retorna una lista vacía si el usuario no tiene preguntas asignadas.
     */
    List<String> getPreguntasUsuario(String username);

    /**
     * Guarda las respuestas de seguridad para un usuario.
     * Si el usuario ya tenía respuestas registradas, estas se sobrescriben.
     * Este método asocia un conjunto de preguntas y sus respectivas respuestas a un usuario.
     *
     * @param usuario El objeto {@link Usuario} al cual se le asociarán las respuestas.
     * @param preguntas Un array de {@link String} que contiene las preguntas seleccionadas por el usuario.
     * @param respuestas Un array de {@link String} que contiene las respuestas a las preguntas correspondientes.
     */
    void guardarRespuestas(Usuario usuario, String[] preguntas, String[] respuestas);

    /**
     * Obtiene una lista de todas las respuestas de seguridad registradas para un usuario específico.
     *
     * @param user El nombre de usuario del cual se desean obtener las respuestas.
     * @return Una {@link List} de objetos {@link Respuesta} asociadas al usuario.
     */
    List<Respuesta> getRespuestasUsuario(String user);

    /**
     * Valida si una {@link Respuesta} específica coincide con una respuesta almacenada para el usuario y pregunta dados.
     * Se utiliza para verificar una única respuesta de seguridad.
     *
     * @param r El objeto {@link Respuesta} a validar, que contiene el usuario, la pregunta y la respuesta.
     * @return {@code true} si la respuesta proporcionada es correcta para la pregunta y usuario;
     * {@code false} en caso contrario.
     */
    boolean validarRespuesta(Respuesta r);
}