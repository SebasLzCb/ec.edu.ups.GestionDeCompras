package ec.edu.ups.util;

import ec.edu.ups.excepciones.ValidacionException;
import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidacionUtils {

    public static void validarCampoObligatorio(String campo, String nombreCampoKey) throws ValidacionException {
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
        if (cedula == null || cedula.length() != 10 || !cedula.matches("\\d+")) {
            throw new ValidacionException("validacion.error.cedula_formato");
        }

        try {
            int sum = 0;
            int[] coeficientes = {2, 1, 2, 1, 2, 1, 2, 1, 2};

            for (int i = 0; i < 9; i++) {
                int digit = Integer.parseInt(cedula.substring(i, i + 1));
                int temp = digit * coeficientes[i];
                if (temp >= 10) {
                    temp -= 9;
                }
                sum += temp;
            }

            int lastDigit = Integer.parseInt(cedula.substring(9, 10));
            int calculatedCheckDigit = sum % 10;
            if (calculatedCheckDigit != 0) {
                calculatedCheckDigit = 10 - calculatedCheckDigit;
            }

            if (calculatedCheckDigit != lastDigit) {
                throw new ValidacionException("validacion.error.cedula_verificador");
            }
        } catch (NumberFormatException e) {
            throw new ValidacionException("validacion.error.cedula_formato");
        }
    }

    public static void validarNumeroPositivo(double numero, String nombreCampoKey) throws ValidacionException {
        if (numero <= 0) {
            throw new ValidacionException("validacion.error.numero_positivo");
        }
    }
}