package ec.edu.ups.util;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * La clase {@code MensajeInternacionalizacionHandler} gestiona la carga y acceso a mensajes
 * internacionalizados (textos de la interfaz de usuario, mensajes de error, etc.)
 * a través de archivos de propiedades (Resource Bundles).
 * Permite cambiar dinámicamente el idioma de la aplicación.
 */
public class MensajeInternacionalizacionHandler {

    // Bundle de recursos que contiene los mensajes para el idioma y país actuales
    private ResourceBundle bundle;
    // Objeto Locale que representa la configuración regional actual (idioma y país)
    private Locale locale;

    /**
     * Constructor de la clase {@code MensajeInternacionalizacionHandler}.
     * Inicializa el manejador con el lenguaje y país especificados,
     * cargando el Resource Bundle correspondiente.
     *
     * @param lenguaje El código del lenguaje (ej. "es", "en", "fr").
     * @param pais El código del país (ej. "EC", "US", "FR").
     */
    public MensajeInternacionalizacionHandler(String lenguaje, String pais) {
        this.locale = new Locale(lenguaje, pais);
        // Carga el Resource Bundle "mensajes" para el Locale dado
        this.bundle = ResourceBundle.getBundle("mensajes", locale);
    }

    /**
     * Obtiene el mensaje internacionalizado asociado a una clave dada.
     * Si la clave no se encuentra en el Resource Bundle, imprime un mensaje de error
     * en la consola y devuelve una cadena de marcador de posición (ej. "???clave???").
     *
     * @param clave La clave del mensaje a buscar en el Resource Bundle.
     * @return La cadena de texto del mensaje si se encuentra, o un marcador de error si no.
     */
    public String get(String clave) {
        try {
            return bundle.getString(clave);
        } catch (MissingResourceException mre) {
            System.err.println("⚠️ Missing resource key: " + clave);
            return "???" + clave + "???";
        }
    }

    /**
     * Cambia el lenguaje (y el país opcionalmente) de la aplicación,
     * recargando el Resource Bundle para el nuevo {@link Locale}.
     * Todas las llamadas posteriores a {@link #get(String)} utilizarán los mensajes del nuevo idioma.
     *
     * @param lenguaje El nuevo código del lenguaje (ej. "es", "en", "fr").
     * @param pais El nuevo código del país (ej. "EC", "US", "FR").
     */
    public void setLenguaje(String lenguaje, String pais) {
        this.locale = new Locale(lenguaje, pais);
        this.bundle = ResourceBundle.getBundle("mensajes", locale);
    }

    /**
     * Obtiene el objeto {@link Locale} actualmente configurado en el manejador.
     *
     * @return El {@link Locale} actual.
     */
    public Locale getLocale() {
        return locale;
    }
}