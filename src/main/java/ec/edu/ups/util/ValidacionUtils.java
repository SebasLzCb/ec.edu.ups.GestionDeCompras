package ec.edu.ups.util;

import ec.edu.ups.excepciones.ValidacionException;
import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * La clase {@code ValidacionUtils} proporciona métodos estáticos para realizar
 * diversas validaciones de datos comunes en la aplicación.
 * Estos métodos lanzan una {@link ValidacionException} si el dato no cumple con el formato
 * o las reglas de negocio esperadas. Los mensajes de la excepción son claves para
 * ser resueltas por un manejador de internacionalización.
 */
public class ValidacionUtils {

    /**
     * Valida que un campo de texto no sea nulo ni esté vacío (después de eliminar espacios en blanco).
     *
     * @param campo El valor del campo a validar.
     * @param nombreCampoUI El nombre del campo en la interfaz de usuario (usado para mensajes, aunque el mensaje de la excepción es genérico).
     * @throws ValidacionException Si el campo es nulo o está vacío, con la clave "validacion.error.campo_obligatorio".
     */
    public static void validarCampoObligatorio(String campo, String nombreCampoUI) throws ValidacionException {
        if (campo == null || campo.trim().isEmpty()) {
            throw new ValidacionException("validacion.error.campo_obligatorio");
        }
    }

    /**
     * Valida que una cadena de texto tenga el formato de correo electrónico válido.
     *
     * @param correo La cadena de texto que representa el correo electrónico.
     * @throws ValidacionException Si el correo no cumple con el formato, con la clave "validacion.error.correo_invalido".
     */
    public static void validarCorreo(String correo) throws ValidacionException {
        String regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(correo);
        if (!matcher.matches()) {
            throw new ValidacionException("validacion.error.correo_invalido");
        }
    }

    /**
     * Valida la fortaleza de una contraseña.
     * La contraseña debe cumplir los siguientes requisitos:
     * <ul>
     * <li>Mínimo 6 caracteres de longitud.</li>
     * <li>Al menos una letra minúscula.</li>
     * <li>Al menos una letra mayúscula.</li>
     * <li>Al menos uno de los caracteres especiales: '@', '_', '-'.</li>
     * </ul>
     *
     * @param contrasenia La cadena de texto de la contraseña a validar.
     * @throws ValidacionException Si la contraseña no cumple con los criterios de fortaleza,
     * con la clave "validacion.error.contrasenia_invalida".
     */
    public static void validarContrasenia(String contrasenia) throws ValidacionException {
        // Expresión regular:
        // ^                 # Inicio de la cadena
        // (?=.*[a-z])       # Debe contener al menos una letra minúscula
        // (?=.*[A-Z])       # Debe contener al menos una letra mayúscula
        // (?=.*[@_-])       # Debe contener al menos uno de los caracteres @, _, -
        // .{6,}             # Debe tener al menos 6 caracteres de longitud
        // $                 # Fin de la cadena
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[@_-]).{6,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(contrasenia);
        if (!matcher.matches()) {
            throw new ValidacionException("validacion.error.contrasenia_invalida");
        }
    }

    /**
     * Valida una cadena de texto como una cédula ecuatoriana válida.
     * Realiza varias comprobaciones: longitud, solo dígitos, provincia válida,
     * tercer dígito válido y el dígito verificador.
     *
     * @param cedula La cadena de texto que representa la cédula.
     * @throws ValidacionException Si la cédula no cumple con el formato o las reglas de validación ecuatorianas,
     * con claves específicas como "validacion.error.cedula_formato",
     * "validacion.error.cedula_verificador" o "validacion.error.cedula_solo_natural".
     */
    public static void validarCedulaEcuatoriana(String cedula) throws ValidacionException {
        if (cedula == null || cedula.trim().length() != 10) {
            throw new ValidacionException("validacion.error.cedula_formato");
        }

        if (!cedula.matches("\\d+")) {
            throw new ValidacionException("validacion.error.cedula_formato");
        }

        int[] digitos = new int[10];
        for (int i = 0; i < 10; i++) {
            digitos[i] = Character.getNumericValue(cedula.charAt(i));
        }

        // Validar provincia (primeros dos dígitos)
        int provincia = digitos[0] * 10 + digitos[1];
        if (provincia < 1 || provincia > 24) { // Provincias válidas de 01 a 24
            throw new ValidacionException("validacion.error.cedula_verificador");
        }

        // Validar tercer dígito
        int tercerDigito = digitos[2];
        if (tercerDigito >= 6 && tercerDigito != 9) { // Debe ser menor a 6 o 9 (para extranjeros)
            throw new ValidacionException("validacion.error.cedula_verificador");
        }

        // Validar dígito verificador para personas naturales (tercer dígito < 6)
        if (tercerDigito < 6) {
            int[] coeficientes = {2, 1, 2, 1, 2, 1, 2, 1, 2};
            int suma = 0;
            for (int i = 0; i < 9; i++) {
                int producto = digitos[i] * coeficientes[i];
                if (producto >= 10) { // Si el producto es de dos dígitos, se resta 9
                    producto -= 9;
                }
                suma += producto;
            }

            int ultimoDigito = digitos[9];
            int digitoVerificador = (suma % 10 == 0) ? 0 : (10 - (suma % 10));

            if (ultimoDigito != digitoVerificador) {
                throw new ValidacionException("validacion.error.cedula_verificador");
            }
        } else if (tercerDigito == 6 || tercerDigito == 9) {
            // Manejo simplificado para cédulas públicas o extranjeras (aunque la validación completa es más compleja)
            // Aquí se asume que solo se aceptan cédulas de personas naturales por ahora
            throw new ValidacionException("validacion.error.cedula_solo_natural");
        }
    }

    /**
     * Valida que una cadena de texto represente un número entero positivo (mayor que 0).
     * Primero valida que el campo no esté vacío.
     *
     * @param valor El valor del campo a validar.
     * @param nombreCampoUI El nombre del campo en la interfaz de usuario (para mensajes).
     * @throws ValidacionException Si el campo es nulo/vacío, no es un número entero, o no es positivo.
     * Con claves "validacion.error.campo_obligatorio", "validacion.error.entero_invalido",
     * o "validacion.error.numero_positivo".
     */
    public static void validarEnteroPositivo(String valor, String nombreCampoUI) throws ValidacionException {
        validarCampoObligatorio(valor, nombreCampoUI); // Valida que no esté vacío
        try {
            int numero = Integer.parseInt(valor);
            if (numero <= 0) {
                throw new ValidacionException("validacion.error.numero_positivo");
            }
        } catch (NumberFormatException e) {
            throw new ValidacionException("validacion.error.entero_invalido"); // Clave para error de formato de entero
        }
    }

    /**
     * Valida que una cadena de texto represente un número decimal positivo (mayor que 0).
     * Primero valida que el campo no esté vacío.
     *
     * @param valor El valor del campo a validar.
     * @param nombreCampoUI El nombre del campo en la interfaz de usuario (para mensajes).
     * @throws ValidacionException Si el campo es nulo/vacío, no es un número decimal, o no es positivo.
     * Con claves "validacion.error.campo_obligatorio", "validacion.error.decimal_invalido",
     * o "validacion.error.numero_positivo".
     */
    public static void validarDecimalPositivo(String valor, String nombreCampoUI) throws ValidacionException {
        validarCampoObligatorio(valor, nombreCampoUI); // Valida que no esté vacío
        try {
            double numero = Double.parseDouble(valor);
            if (numero <= 0) {
                throw new ValidacionException("validacion.error.numero_positivo");
            }
        } catch (NumberFormatException e) {
            throw new ValidacionException("validacion.error.decimal_invalido"); // Clave para error de formato de decimal
        }
    }
}