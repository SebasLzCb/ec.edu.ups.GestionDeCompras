package ec.edu.ups.util;

import ec.edu.ups.excepciones.ValidacionException;
import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidacionUtils {

    public static void validarCampoObligatorio(String campo, String nombreCampoUI) throws ValidacionException {
        if (campo == null || campo.trim().isEmpty()) {
            throw new ValidacionException("validacion.error.campo_obligatorio");
        }
    }

    public static void validarCorreo(String correo) throws ValidacionException {
        String regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(correo);
        if (!matcher.matches()) {
            throw new ValidacionException("validacion.error.correo_invalido");
        }
    }

    public static void validarContrasenia(String contrasenia) throws ValidacionException {
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[@_-]).{6,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(contrasenia);
        if (!matcher.matches()) {
            throw new ValidacionException("validacion.error.contrasenia_invalida");
        }
    }

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

        int provincia = digitos[0] * 10 + digitos[1];
        if (provincia < 1 || provincia > 24) {
            throw new ValidacionException("validacion.error.cedula_verificador");
        }

        int tercerDigito = digitos[2];
        if (tercerDigito >= 6 && tercerDigito != 9) {
            throw new ValidacionException("validacion.error.cedula_verificador");
        }

        if (tercerDigito < 6) {
            int[] coeficientes = {2, 1, 2, 1, 2, 1, 2, 1, 2};
            int suma = 0;
            for (int i = 0; i < 9; i++) {
                int producto = digitos[i] * coeficientes[i];
                if (producto >= 10) {
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
            throw new ValidacionException("validacion.error.cedula_solo_natural");
        }
    }

    // MODIFICADO: Ahora recibe String para parsear el input de la UI
    public static void validarEnteroPositivo(String valor, String nombreCampoUI) throws ValidacionException {
        validarCampoObligatorio(valor, nombreCampoUI); // Valida que no esté vacío
        try {
            int numero = Integer.parseInt(valor);
            if (numero <= 0) {
                throw new ValidacionException("validacion.error.numero_positivo");
            }
        } catch (NumberFormatException e) {
            throw new ValidacionException("validacion.error.entero_invalido"); // Nueva clave para error de formato
        }
    }

    // NUEVO MÉTODO: Validar Decimal Positivo
    public static void validarDecimalPositivo(String valor, String nombreCampoUI) throws ValidacionException {
        validarCampoObligatorio(valor, nombreCampoUI); // Valida que no esté vacío
        try {
            double numero = Double.parseDouble(valor);
            if (numero <= 0) {
                throw new ValidacionException("validacion.error.numero_positivo");
            }
        } catch (NumberFormatException e) {
            throw new ValidacionException("validacion.error.decimal_invalido"); // Nueva clave para error de formato
        }
    }
}