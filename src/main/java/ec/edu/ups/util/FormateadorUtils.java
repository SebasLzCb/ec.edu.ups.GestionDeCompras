package ec.edu.ups.util;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;

/**
 * La clase {@code FormateadorUtils} proporciona métodos de utilidad estáticos
 * para formatear valores numéricos (moneda) y fechas según una configuración regional ({@link Locale}).
 * Es útil para presentar datos de manera consistente y localizada en la interfaz de usuario.
 */
public class FormateadorUtils {

    /**
     * Formatea una cantidad numérica como una cadena de moneda según la configuración regional especificada.
     * Por ejemplo, para un Locale "es_EC", 123.45 se podría formatear como "$123.45".
     *
     * @param cantidad El valor numérico a formatear.
     * @param locale La {@link Locale} que define las reglas de formato de moneda (ej. Locale.US para USD).
     * @return Una cadena de texto que representa la cantidad formateada como moneda.
     */
    public static String formatearMoneda(double cantidad, Locale locale) {
        NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(locale);
        return formatoMoneda.format(cantidad);
    }

    /**
     * Formatea un objeto {@link Date} como una cadena de fecha según la configuración regional especificada.
     * Utiliza un formato de fecha de longitud "MEDIUM".
     * Por ejemplo, para un Locale "es_EC", una fecha se podría formatear como "15 mar. 2023".
     *
     * @param fecha El objeto {@link Date} a formatear.
     * @param locale La {@link Locale} que define las reglas de formato de fecha.
     * @return Una cadena de texto que representa la fecha formateada.
     */
    public static String formatearFecha(Date fecha, Locale locale) {
        DateFormat formato = DateFormat.getDateInstance(DateFormat.MEDIUM, locale);
        return formato.format(fecha);
    }

}