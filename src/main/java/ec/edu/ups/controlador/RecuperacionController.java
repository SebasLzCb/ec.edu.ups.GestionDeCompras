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

    private final RecuperacionDAO recuperacionDAO;
    private final UsuarioDAO      usuarioDAO;
    private final MensajeInternacionalizacionHandler mensajeHandler;

    private LoginView loginView;
    private RecuperarContraseñaView recuperarView;

    public RecuperacionController(RecuperacionDAO recuperacionDAO,
                                  UsuarioDAO usuarioDAO,
                                  MensajeInternacionalizacionHandler mensajeHandler) {
        this.recuperacionDAO = recuperacionDAO;
        this.usuarioDAO      = usuarioDAO;
        this.mensajeHandler  = mensajeHandler;
    }

    public RecuperacionController(RecuperacionDAO recuperacionDAO,
                                  MensajeInternacionalizacionHandler mensajeHandler) {
        this(recuperacionDAO, null, mensajeHandler);
    }

    public void setLoginView(LoginView loginView) {
        this.loginView = loginView;
    }

    public void setRecuperarView(RecuperarContraseñaView recuperarView) {
        this.recuperarView = recuperarView;

        // BOTÓN “Siguiente”
        recuperarView.getBtnSiguiente().addActionListener(e -> {
            String usr       = recuperarView.getTxtUsuario().getText().trim();
            String pregunta  = recuperarView.getPreguntas()[0];
            String respuesta = recuperarView.getRespuestas()[0];

            if (recuperacionDAO.validar(usr, pregunta, respuesta)) {
                // buscamos el usuario real (o uno dummy)
                Usuario u = (usuarioDAO != null)
                        ? usuarioDAO.buscarPorUsername(usr)
                        : new Usuario();

                CambiarContraseñaView cv = new CambiarContraseñaView(u, usuarioDAO, mensajeHandler);
                cv.actualizarIdioma();
                cv.setVisible(true);
                recuperarView.dispose();
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
        return recuperacionDAO.getBancoPreguntas().stream()
                .map(p -> mensajeHandler.get("recuperacion.pregunta." + p.getId()))
                .collect(Collectors.toList());
    }

    public void registrarPreguntas(String username, List<Respuesta> respuestasList) {
        String[] preg = new String[respuestasList.size()];
        String[] resp = new String[respuestasList.size()];
        for (int i = 0; i < respuestasList.size(); i++) {
            preg[i] = respuestasList.get(i).getPregunta().getPregunta();
            resp[i] = respuestasList.get(i).getRespuesta();
        }
        recuperacionDAO.guardarRespuestas(username, preg, resp);
    }

    public void mostrarRecuperar(String username) {
        List<String> hechas = recuperacionDAO.getPreguntasUsuario(username);
        if (hechas.isEmpty()) {
            loginView.mostrarMensaje(
                    mensajeHandler.get("error.recuperacion.noPreguntas")
            );
            return;
        }

        List<Integer> ids = recuperacionDAO.getBancoPreguntas().stream()
                .filter(p -> hechas.contains(p.getPregunta()))
                .map(Pregunta::getId)
                .collect(Collectors.toList());

        int elegido = ids.get(new Random().nextInt(ids.size()));
        String preguntaLocal = mensajeHandler.get("recuperacion.pregunta." + elegido);

        recuperarView.getTxtUsuario().setText(username);
        recuperarView.setPreguntas(List.of(preguntaLocal));
        recuperarView.actualizarIdioma();
        recuperarView.setVisible(true);
    }
}
