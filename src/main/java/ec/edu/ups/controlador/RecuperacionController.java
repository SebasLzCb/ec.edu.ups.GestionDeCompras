package ec.edu.ups.controlador;

import ec.edu.ups.dao.RecuperacionDAO;
import ec.edu.ups.dao.UsuarioDAO;
import ec.edu.ups.modelo.Pregunta;
import ec.edu.ups.modelo.Respuesta;
import ec.edu.ups.modelo.Usuario;
import ec.edu.ups.util.MensajeInternacionalizacionHandler;
import ec.edu.ups.vista.InicioDeSesion.CambiarContraseñaView;
import ec.edu.ups.vista.InicioDeSesion.LoginView;
import ec.edu.ups.vista.InicioDeSesion.RecuperarContraseñaView;
import ec.edu.ups.dao.DAOManager;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * La clase {@code RecuperacionController} gestiona la lógica de negocio
 * para la recuperación de contraseñas de usuario. Coordina las interacciones
 * entre las vistas de inicio de sesión y recuperación, y las capas de acceso a datos
 * para usuarios y preguntas de seguridad.
 *
 * <p>Es responsable de:</p>
 * <ul>
 * <li>Proveer preguntas de seguridad localizadas.</li>
 * <li>Registrar las respuestas a preguntas de seguridad de un usuario.</li>
 * <li>Validar las respuestas de seguridad para permitir el cambio de contraseña.</li>
 * <li>Manejar los mensajes de internacionalización para la interfaz de usuario.</li>
 * <li>Utilizar el {@link DAOManager} para obtener las implementaciones DAO actuales,
 * permitiendo la flexibilidad en el tipo de persistencia (memoria, archivos, etc.).</li>
 * </ul>
 */
public class RecuperacionController {

    private RecuperacionDAO recuperacionDAO;
    private UsuarioDAO      usuarioDAO;
    private final MensajeInternacionalizacionHandler mensajeHandler;
    private final DAOManager daoManager; // Agregado para gestionar las diferentes implementaciones DAO

    private LoginView loginView; // La vista de inicio de sesión
    private RecuperarContraseñaView recuperarView; // La vista de recuperación de contraseña

    /**
     * Constructor de la clase {@code RecuperacionController}.
     * Inicializa las dependencias DAO, el manejador de mensajes internacionalizados
     * y el gestor de DAOs para obtener las implementaciones actuales.
     *
     * @param recuperacionDAO   Objeto DAO para la persistencia de preguntas y respuestas de recuperación.
     * @param usuarioDAO        Objeto DAO para la persistencia de usuarios.
     * @param mensajeHandler    Manejador para obtener mensajes internacionalizados.
     * @param daoManager        Gestor de DAOs que proporciona las implementaciones actuales de persistencia.
     */
    public RecuperacionController(RecuperacionDAO recuperacionDAO,
                                  UsuarioDAO usuarioDAO,
                                  MensajeInternacionalizacionHandler mensajeHandler,
                                  DAOManager daoManager) {
        this.recuperacionDAO = recuperacionDAO;
        this.usuarioDAO      = usuarioDAO;
        this.mensajeHandler  = mensajeHandler;
        this.daoManager = daoManager;
    }

    /**
     * Obtiene la instancia actual del {@link RecuperacionDAO} utilizada por este controlador.
     *
     * @return La instancia de {@link RecuperacionDAO}.
     */
    public RecuperacionDAO getRecuperacionDAO() {
        return recuperacionDAO;
    }

    /**
     * Establece una nueva instancia del {@link RecuperacionDAO} para este controlador.
     * Esto permite cambiar la fuente de persistencia de las preguntas de recuperación dinámicamente.
     *
     * @param recuperacionDAO La nueva instancia de {@link RecuperacionDAO} a utilizar.
     */
    public void setRecuperacionDAO(RecuperacionDAO recuperacionDAO) {
        this.recuperacionDAO = recuperacionDAO;
    }

    /**
     * Establece una nueva instancia del {@link UsuarioDAO} para este controlador.
     * Esto permite cambiar la fuente de persistencia de usuarios dinámicamente.
     *
     * @param usuarioDAO La nueva instancia de {@link UsuarioDAO} a utilizar.
     */
    public void setUsuarioDAO(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    /**
     * Establece la vista de inicio de sesión ({@link LoginView}) que este controlador
     * utilizará para interactuar con el usuario.
     *
     * @param loginView La instancia de {@link LoginView}.
     */
    public void setLoginView(LoginView loginView) {
        this.loginView = loginView;
    }

    /**
     * Establece la vista de recuperación de contraseña ({@link RecuperarContraseñaView})
     * y configura sus ActionListeners.
     * <p>
     * Cuando se presiona el botón "Siguiente", valida la respuesta de seguridad del usuario.
     * Si es correcta, abre la vista para cambiar la contraseña. Si es incorrecta,
     * muestra un mensaje de error.
     * </p>
     *
     * @param recuperarView La instancia de {@link RecuperarContraseñaView}.
     */
    public void setRecuperarView(RecuperarContraseñaView recuperarView) {
        this.recuperarView = recuperarView;

        recuperarView.getBtnSiguiente().addActionListener(e -> {
            String usr = recuperarView.getTxtUsuario().getText().trim();
            String pregunta = recuperarView.getPreguntas()[0];
            String respuesta = recuperarView.getRespuestas()[0];

            // Usar las DAOs actualizadas del DAOManager para asegurar la consistencia del almacenamiento
            UsuarioDAO currentUsuarioDAO = daoManager.getUsuarioDAO();
            RecuperacionDAO currentRecuperacionDAO = daoManager.getRecuperacionDAO();
            System.out.println("DEBUG (RecuperacionController - Siguiente): Validando respuesta para " + usr + " con DAO: " + currentRecuperacionDAO.getClass().getSimpleName());


            if (currentRecuperacionDAO.validar(usr, pregunta, respuesta)) {
                Usuario u = currentUsuarioDAO.buscarPorUsername(usr);
                if (u != null) {
                    CambiarContraseñaView cv = new CambiarContraseñaView(u, currentUsuarioDAO, mensajeHandler);
                    cv.actualizarIdioma();
                    cv.setVisible(true);
                    recuperarView.dispose();
                }
            } else {
                recuperarView.mostrarMensaje(
                        mensajeHandler.get("error.recuperacion.respuesta_incorrecta")
                );
            }
        });

        recuperarView.getBtnCancelar().addActionListener(e -> {
            recuperarView.dispose();
        });
    }

    /**
     * Obtiene una lista de todas las preguntas de seguridad disponibles
     * en el banco de preguntas, traducidas al idioma actual de la aplicación.
     *
     * @return Una {@link List} de {@link String} con las preguntas de seguridad localizadas.
     */
    public List<String> obtenerPreguntasLocalizadas() {
        // Asegurarse de usar el DAO actual del DAOManager si la lista de preguntas puede cambiar según el almacenamiento
        System.out.println("DEBUG (RecuperacionController - obtenerPreguntasLocalizadas): Obteniendo preguntas del banco con DAO: " + recuperacionDAO.getClass().getSimpleName());
        return recuperacionDAO.getBancoPreguntas().stream()
                .map(p -> mensajeHandler.get("recuperacion.pregunta." + p.getId()))
                .collect(Collectors.toList());
    }

    /**
     * Registra las respuestas de seguridad para un usuario dado.
     * Este método es invocado normalmente después del registro de un nuevo usuario
     * para asociar sus preguntas y respuestas secretas.
     *
     * @param usuario        El objeto {@link Usuario} al que se le asignarán las respuestas.
     * @param respuestasList Una {@link List} de objetos {@link Respuesta} que contienen
     * las preguntas y respuestas de seguridad del usuario.
     */
    public void registrarPreguntas(Usuario usuario, List<Respuesta> respuestasList) {
        // Asegurarse de usar el DAO actual del DAOManager si el método se invoca con el DAOManager
        System.out.println("DEBUG (RecuperacionController - registrarPreguntas): Guardando respuestas para " + usuario.getUsername() + " con DAO: " + recuperacionDAO.getClass().getSimpleName());
        String[] preg = new String[respuestasList.size()];
        String[] resp = new String[respuestasList.size()];
        for (int i = 0; i < respuestasList.size(); i++) {
            preg[i] = respuestasList.get(i).getPregunta().getPregunta();
            resp[i] = respuestasList.get(i).getRespuesta();
        }
        recuperacionDAO.guardarRespuestas(usuario, preg, resp);
    }

    /**
     * Muestra la vista de recuperación de contraseña para un usuario específico.
     * Primero verifica si el usuario tiene preguntas de seguridad registradas.
     * Si las tiene, selecciona una pregunta de forma aleatoria de entre las registradas
     * para ese usuario, la muestra en la vista de recuperación y hace visible la vista.
     * Si el usuario no tiene preguntas registradas, muestra un mensaje de error.
     *
     * @param username El nombre de usuario para el cual se intenta recuperar la contraseña.
     */
    public void mostrarRecuperar(String username) {
        System.out.println("DEBUG (RecuperacionController - mostrarRecuperar): Intentando mostrar recuperación para " + username);
        // Es crucial usar el DAO actual del DAOManager para que la recuperación funcione con el almacenamiento seleccionado.
        RecuperacionDAO currentRecuperacionDAO = daoManager.getRecuperacionDAO();
        System.out.println("DEBUG (RecuperacionController - mostrarRecuperar): Usando DAO: " + currentRecuperacionDAO.getClass().getSimpleName());

        List<String> hechas = currentRecuperacionDAO.getPreguntasUsuario(username);
        System.out.println("DEBUG (RecuperacionController - mostrarRecuperar): Preguntas obtenidas para " + username + ": " + hechas);
        if (hechas.isEmpty()) {
            loginView.mostrarMensaje(
                    mensajeHandler.get("error.recuperacion.noPreguntas")
            );
            return;
        }

        List<Pregunta> bancoPreguntas = currentRecuperacionDAO.getBancoPreguntas();
        List<Pregunta> preguntasDelUsuario = bancoPreguntas.stream()
                .filter(p -> hechas.contains(p.getPregunta()))
                .collect(Collectors.toList());

        if (preguntasDelUsuario.isEmpty()) {
            loginView.mostrarMensaje(
                    mensajeHandler.get("error.recuperacion.noPreguntas")
            );
            return;
        }

        Pregunta preguntaElegida = preguntasDelUsuario.get(new Random().nextInt(preguntasDelUsuario.size()));
        String preguntaLocalizada = mensajeHandler.get("recuperacion.pregunta." + preguntaElegida.getId());
        System.out.println("DEBUG (RecuperacionController - mostrarRecuperar): Pregunta elegida para " + username + ": " + preguntaLocalizada);

        recuperarView.getTxtUsuario().setText(username);
        recuperarView.setPreguntas(List.of(preguntaLocalizada));
        recuperarView.actualizarIdioma();
        recuperarView.setVisible(true);
    }
}