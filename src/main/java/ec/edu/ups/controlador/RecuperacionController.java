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

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class RecuperacionController {

    private RecuperacionDAO recuperacionDAO;
    private UsuarioDAO      usuarioDAO;
    private final MensajeInternacionalizacionHandler mensajeHandler;

    private RecuperarContraseñaView recuperarView;
    private LoginView loginView;

    public RecuperacionController(RecuperacionDAO recuperacionDAO,
                                  UsuarioDAO usuarioDAO,
                                  MensajeInternacionalizacionHandler mensajeHandler) {
        this.recuperacionDAO = recuperacionDAO;
        this.usuarioDAO      = usuarioDAO;
        this.mensajeHandler  = mensajeHandler;
    }

    public void setUsuarioDAO(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    public void setRecuperacionDAO(RecuperacionDAO recuperacionDAO) {
        this.recuperacionDAO = recuperacionDAO;
    }

    public RecuperacionDAO getRecuperacionDAO() {
        return this.recuperacionDAO;
    }

    public void setLoginView(LoginView loginView) {
        this.loginView = loginView;
    }

    public void setRecuperarView(RecuperarContraseñaView recuperarView) {
        this.recuperarView = recuperarView;

        recuperarView.getBtnSiguiente().addActionListener(e -> {
            String usr       = recuperarView.getTxtUsuario().getText().trim();
            String pregunta  = recuperarView.getPreguntas()[0];
            String respuesta = recuperarView.getRespuestas()[0];

            if (recuperacionDAO == null || usuarioDAO == null) {
                recuperarView.mostrarMensaje(mensajeHandler.get("error.recuperacion.daos_no_inicializados"));
                return;
            }

            if (recuperacionDAO.validar(usr, pregunta, respuesta)) {
                Usuario u = usuarioDAO.buscarPorUsername(usr);
                if (u != null) {
                    // Asegurarse de que el usuarioDAO para la vista CambiarContraseñaView no sea nulo
                    CambiarContraseñaView cv = new CambiarContraseñaView(u, usuarioDAO, mensajeHandler);
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

    public List<String> obtenerPreguntasLocalizadas() {
        // Asegurarse de que recuperacionDAO no es nulo antes de usarlo
        if (recuperacionDAO == null) {
            // Esto no debería suceder con la inicialización temporal en Main
            System.err.println("ADVERTENCIA: recuperacionDAO es nulo en obtenerPreguntasLocalizadas al inicio.");
            return new java.util.ArrayList<>();
        }
        return recuperacionDAO.getBancoPreguntas().stream()
                .map(p -> mensajeHandler.get("recuperacion.pregunta." + p.getId()))
                .collect(Collectors.toList());
    }

    public void registrarPreguntas(Usuario usuario, List<Respuesta> respuestasList) {
        if (recuperacionDAO == null) {
            System.err.println("ADVERTENCIA: recuperacionDAO es nulo en registrarPreguntas.");
            return;
        }
        String[] preg = new String[respuestasList.size()];
        String[] resp = new String[respuestasList.size()];
        for (int i = 0; i < respuestasList.size(); i++) {
            preg[i] = respuestasList.get(i).getPregunta().getPregunta();
            resp[i] = respuestasList.get(i).getRespuesta();
        }
        recuperacionDAO.guardarRespuestas(usuario, preg, resp);
    }

    public void mostrarRecuperar(String username) {
        if (recuperacionDAO == null || usuarioDAO == null || loginView == null || recuperarView == null) {
            if (loginView != null) {
                loginView.mostrarMensaje(mensajeHandler.get("error.recuperacion.inicializacion_pendiente"));
            } else {
                System.err.println("ADVERTENCIA: No se pudo mostrar la recuperación. LoginView o DAOs no inicializados.");
            }
            return;
        }

        List<String> hechas = recuperacionDAO.getPreguntasUsuario(username);
        if (hechas.isEmpty()) {
            loginView.mostrarMensaje(
                    mensajeHandler.get("error.recuperacion.noPreguntas")
            );
            return;
        }

        List<Pregunta> bancoPreguntas = recuperacionDAO.getBancoPreguntas();
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

        recuperarView.getTxtUsuario().setText(username);
        recuperarView.setPreguntas(List.of(preguntaLocalizada));
        recuperarView.actualizarIdioma();
        recuperarView.setVisible(true);
    }
}