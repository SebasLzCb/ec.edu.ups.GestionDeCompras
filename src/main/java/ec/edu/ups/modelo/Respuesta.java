package ec.edu.ups.modelo;

import java.io.Serializable;

/**
 * La clase {@code Respuesta} representa la respuesta de un usuario a una pregunta de seguridad.
 * Se utiliza en el proceso de recuperación de contraseñas.
 * Implementa {@link Serializable} para permitir su persistencia en archivos binarios.
 */
public class Respuesta  implements Serializable {
    // El usuario al que pertenece esta respuesta
    private Usuario usuario;
    // La pregunta de seguridad a la que se refiere esta respuesta
    private Pregunta pregunta;
    // El texto de la respuesta proporcionada por el usuario
    private String respuesta;

    /**
     * Constructor para crear una nueva instancia de {@code Respuesta}.
     *
     * @param usuario El objeto {@link Usuario} al que pertenece la respuesta.
     * @param pregunta El objeto {@link Pregunta} a la que se responde.
     * @param respuesta El texto de la respuesta del usuario.
     */
    public Respuesta(Usuario usuario, Pregunta pregunta, String respuesta) {
        this.usuario = usuario;
        this.pregunta = pregunta;
        this.respuesta = respuesta;
    }

    /**
     * Obtiene el usuario asociado a esta respuesta.
     *
     * @return El objeto {@link Usuario}.
     */
    public Usuario getUsuario() {
        return usuario;
    }

    /**
     * Establece el usuario asociado a esta respuesta.
     *
     * @param usuario El nuevo objeto {@link Usuario}.
     */
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    /**
     * Obtiene la pregunta de seguridad asociada a esta respuesta.
     *
     * @return El objeto {@link Pregunta}.
     */
    public Pregunta getPregunta() {
        return pregunta;
    }

    /**
     * Establece la pregunta de seguridad asociada a esta respuesta.
     *
     * @param pregunta El nuevo objeto {@link Pregunta}.
     */
    public void setPregunta(Pregunta pregunta) {
        this.pregunta = pregunta;
    }

    /**
     * Obtiene el texto de la respuesta.
     *
     * @return El texto de la respuesta.
     */
    public String getRespuesta() {
        return respuesta;
    }

    /**
     * Establece el texto de la respuesta.
     *
     * @param respuesta El nuevo texto de la respuesta.
     */
    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }
}