package ec.edu.ups.dao.impl;

import ec.edu.ups.dao.RecuperacionDAO;
import ec.edu.ups.dao.UsuarioDAO;
import ec.edu.ups.modelo.Pregunta;
import ec.edu.ups.modelo.Respuesta;
import ec.edu.ups.modelo.Usuario; // Se importa Usuario

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * La clase {@code RecuperacionDAOMemoria} es una implementación en memoria
 * de la interfaz {@link RecuperacionDAO}. Almacena las preguntas de seguridad
 * predefinidas y las respuestas de los usuarios en la memoria RAM.
 * Los datos no persisten una vez que la aplicación se cierra.
 *
 * @author [Tu Nombre/Equipo]
 * @version 1.0
 * @since 2023-01-01
 */
public class RecuperacionDAOMemoria implements RecuperacionDAO {

    private final List<Pregunta> bancoPreguntas;
    private final List<String[]> datosRecuperacion; // Almacena [username, preg1, resp1, preg2, resp2, preg3, resp3]
    private final List<Respuesta> respuestas; // Almacena objetos Respuesta
    private final UsuarioDAO usuarioDAO; // Para obtener el objeto Usuario al inicializar respuestas

    /**
     * Constructor de la clase {@code RecuperacionDAOMemoria}.
     * Inicializa el banco de preguntas de seguridad y carga algunas respuestas iniciales
     * para el usuario 'admin' si este existe.
     *
     * @param usuarioDAO El DAO de usuario necesario para buscar el usuario 'admin'
     * al cargar las respuestas iniciales.
     */
    public RecuperacionDAOMemoria(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
        this.bancoPreguntas = new ArrayList<>();
        this.datosRecuperacion = new ArrayList<>();
        this.respuestas = new ArrayList<>();
        inicializarBancoPreguntas();
        cargarRespuestasIniciales();
    }

    /**
     * Inicializa el banco de preguntas de seguridad predefinidas.
     * Estas preguntas son fijas y se utilizan para la recuperación de cuentas.
     */
    private void inicializarBancoPreguntas() {
        bancoPreguntas.add(new Pregunta(1,  "¿Cuál es tu equipo favorito?"));
        bancoPreguntas.add(new Pregunta(2,  "¿Nombre de tu primera mascota?"));
        bancoPreguntas.add(new Pregunta(3,  "¿Ciudad donde naciste?"));
        bancoPreguntas.add(new Pregunta(4,  "¿Tu color preferido?"));
        bancoPreguntas.add(new Pregunta(5,  "¿Modelo de tu primer auto?"));
        bancoPreguntas.add(new Pregunta(6,  "¿Comida favorita?"));
        bancoPreguntas.add(new Pregunta(7,  "¿Nombre de tu actor favorito?"));
        bancoPreguntas.add(new Pregunta(8,  "¿Lugar de tus vacaciones favoritas?"));
        bancoPreguntas.add(new Pregunta(9,  "¿Asignatura favorita en la escuela?"));
        bancoPreguntas.add(new Pregunta(10, "¿Nombre de tu mejor amigo de la infancia?"));
    }

    /**
     * Carga respuestas iniciales para el usuario "admin" si este existe.
     * Esto es útil para propósitos de prueba y configuración inicial.
     */
    private void cargarRespuestasIniciales() {
        Usuario admin = usuarioDAO.buscarPorUsername("admin");
        if (admin != null) {
            String[] preguntasAdmin = {
                    "¿Cuál es tu equipo favorito?",
                    "¿Nombre de tu primera mascota?",
                    "¿Ciudad donde naciste?"
            };
            String[] respuestasAdmin = {
                    "Ecuador",
                    "Firulais",
                    "Quito"
            };
            guardarRespuestas(admin, preguntasAdmin, respuestasAdmin);
        }
    }

    /**
     * Obtiene una lista de todas las preguntas disponibles en el banco de preguntas de seguridad.
     *
     * @return Una {@link List} de objetos {@link Pregunta} que representan el banco de preguntas.
     */
    @Override
    public List<Pregunta> getBancoPreguntas() {
        return new ArrayList<>(bancoPreguntas);
    }

    /**
     * Obtiene una lista de todas las respuestas de seguridad registradas para un usuario específico.
     *
     * @param user El nombre de usuario del cual se desean obtener las respuestas.
     * @return Una {@link List} de objetos {@link Respuesta} asociadas al usuario.
     */
    @Override
    public List<Respuesta> getRespuestasUsuario(String user) {
        List<Respuesta> resultado = new ArrayList<>();
        for (Respuesta r : respuestas) {
            if (r.getUsuario().getUsername().equals(user)) {
                resultado.add(r);
            }
        }
        return resultado;
    }

    /**
     * Valida si una {@link Respuesta} específica coincide con una respuesta almacenada para el usuario y pregunta dados.
     *
     * @param r El objeto {@link Respuesta} a validar, que contiene el usuario, la pregunta y la respuesta.
     * @return {@code true} si la respuesta proporcionada es correcta para la pregunta y usuario;
     * {@code false} en caso contrario.
     */
    @Override
    public boolean validarRespuesta(Respuesta r) {
        return getRespuestasUsuario(r.getUsuario().getUsername()).stream()
                .anyMatch(alm -> alm.getPregunta().getId() == r.getPregunta().getId()
                        && alm.getRespuesta().equalsIgnoreCase(r.getRespuesta()));
    }

    /**
     * Obtiene una lista de todas las preguntas (como cadenas de texto) disponibles
     * en el banco de preguntas de seguridad.
     *
     * @return Una {@link List} de {@link String} con el texto de cada pregunta.
     */
    @Override
    public List<String> getPreguntas() {
        List<String> list = new ArrayList<>();
        for (Pregunta p : bancoPreguntas) {
            list.add(p.getPregunta());
        }
        return list;
    }

    /**
     * Obtiene una lista de las preguntas de seguridad que un usuario específico tiene asignadas.
     *
     * @param username El nombre de usuario del cual se desean obtener las preguntas.
     * @return Una {@link List} de {@link String} con las preguntas asignadas al usuario.
     */
    @Override
    public List<String> getPreguntasUsuario(String username) {
        for (String[] entry : datosRecuperacion) {
            if (entry[0].equals(username)) {
                // Retorna las preguntas en las posiciones 1, 3 y 5 (preg1, preg2, preg3)
                return Arrays.asList(entry[1], entry[3], entry[5]);
            }
        }
        return Collections.emptyList();
    }

    /**
     * Valida una pregunta y respuesta de seguridad específica para un usuario dado.
     *
     * @param username El nombre de usuario del usuario cuyas respuestas se quieren validar.
     * @param pregunta La pregunta de seguridad a validar (texto de la pregunta).
     * @param respuesta La respuesta proporcionada por el usuario para la pregunta.
     * @return {@code true} si la combinación de usuario, pregunta y respuesta es válida;
     * {@code false} en caso contrario.
     */
    @Override
    public boolean validar(String username, String pregunta, String respuesta) {
        for (String[] entry : datosRecuperacion) {
            if (entry[0].equals(username)) {
                for (int i = 0; i < 3; i++) {
                    String pregAlm = entry[1 + 2 * i];
                    String respAlm = entry[2 + 2 * i];
                    if (pregAlm.equals(pregunta)
                            && respAlm.equalsIgnoreCase(respuesta.trim())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Guarda las respuestas de seguridad para un usuario.
     * Si el usuario ya tenía respuestas registradas, estas se eliminan y se agregan las nuevas.
     * Se guarda tanto en el formato interno (String[]) como en objetos Respuesta.
     *
     * @param usuario El objeto {@link Usuario} al cual se le asociarán las respuestas.
     * @param preg Un array de {@link String} que contiene las preguntas seleccionadas por el usuario.
     * @param resp Un array de {@link String} que contiene las respuestas a las preguntas correspondientes.
     */
    @Override
    public void guardarRespuestas(Usuario usuario, String[] preg, String[] resp) {
        String username = usuario.getUsername();

        // Eliminar respuestas antiguas para este usuario
        datosRecuperacion.removeIf(entry -> entry[0].equals(username));
        respuestas.removeIf(r -> r.getUsuario().getUsername().equals(username));

        // Agregar las nuevas respuestas en formato de array de Strings
        String[] nuevo = new String[1 + 3 * 2]; // 1 para username + 3 pares (pregunta, respuesta)
        nuevo[0] = username;
        for (int i = 0; i < 3; i++) {
            nuevo[1 + 2 * i] = preg[i];
            nuevo[2 + 2 * i] = resp[i];
        }
        datosRecuperacion.add(nuevo);

        // Agregar las nuevas respuestas como objetos Respuesta
        for (int i = 0; i < 3; i++) {
            String textoPregunta = preg[i];
            Pregunta preguntaObj = bancoPreguntas.stream()
                    .filter(p -> p.getPregunta().equals(textoPregunta))
                    .findFirst()
                    .orElse(null);
            if (preguntaObj != null) {
                respuestas.add(new Respuesta(usuario, preguntaObj, resp[i]));
            }
        }
    }
}