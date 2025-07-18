package ec.edu.ups.modelo;

import java.io.Serializable;

/**
 * La clase {@code Pregunta} representa una pregunta de seguridad en el sistema.
 * Se utiliza para la recuperación de contraseñas de usuario.
 * Implementa {@link Serializable} para permitir su persistencia en archivos binarios.
 */
public class Pregunta  implements Serializable {
    // Identificador único de la pregunta
    private int id;
    // Texto de la pregunta de seguridad
    private String pregunta;

    /**
     * Constructor para crear una nueva instancia de {@code Pregunta}.
     *
     * @param id El identificador único de la pregunta.
     * @param pregunta El texto de la pregunta de seguridad.
     */
    public Pregunta(int id, String pregunta) {
        this.id = id;
        this.pregunta = pregunta;
    }

    /**
     * Obtiene el identificador único de la pregunta.
     *
     * @return El ID de la pregunta.
     */
    public int getId() {
        return id;
    }

    /**
     * Establece el identificador único de la pregunta.
     *
     * @param id El nuevo ID de la pregunta.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtiene el texto de la pregunta de seguridad.
     *
     * @return El texto de la pregunta.
     */
    public String getPregunta() {
        return pregunta;
    }

    /**
     * Establece el texto de la pregunta de seguridad.
     *
     * @param pregunta El nuevo texto de la pregunta.
     */
    public void setPregunta(String pregunta) {
        this.pregunta = pregunta;
    }
}