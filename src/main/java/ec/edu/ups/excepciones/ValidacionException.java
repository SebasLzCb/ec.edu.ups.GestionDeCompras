package ec.edu.ups.excepciones;

/**
 * La clase {@code ValidacionException} es una excepción personalizada utilizada
 * para indicar errores de validación de datos dentro de la aplicación.
 * Permite encapsular mensajes de error específicos relacionados con la validación
 * de entradas de usuario o de lógica de negocio, haciendo que el manejo de errores
 * sea más claro y específico.
 *
 * <p>Esta excepción puede ser lanzada cuando una validación falla, por ejemplo,
 * si un campo obligatorio está vacío, un formato es incorrecto, una cédula es inválida,
 * o una contraseña no cumple con los requisitos de seguridad.
 * El mensaje asociado a la excepción suele ser una clave para un mensaje internacionalizado.</p>
 */
public class ValidacionException extends Exception {

    /**
     * Constructor para crear una nueva {@code ValidacionException} con un mensaje detallado.
     * El mensaje típicamente es una clave para ser resuelta por el {@link ec.edu.ups.util.MensajeInternacionalizacionHandler}.
     *
     * @param message El mensaje de detalle (o clave de mensaje) de la excepción.
     */
    public ValidacionException(String message) {
        super(message);
    }

    /**
     * Constructor para crear una nueva {@code ValidacionException} con un mensaje detallado
     * y una causa raíz. Esto es útil para encapsular excepciones de bajo nivel
     * (como {@link java.lang.NumberFormatException} o {@link java.text.ParseException})
     * en una excepción de validación más significativa.
     *
     * @param message El mensaje de detalle (o clave de mensaje) de la excepción.
     * @param cause La causa raíz (otra excepción) que provocó esta excepción de validación.
     */
    public ValidacionException(String message, Throwable cause) {
        super(message, cause);
    }
}