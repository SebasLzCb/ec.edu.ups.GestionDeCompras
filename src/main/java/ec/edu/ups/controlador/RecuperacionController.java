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

    // Getter para el DAO, útil para el controlador de usuario
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

            if (recuperacionDAO.validar(usr, pregunta, respuesta)) {
                Usuario u = usuarioDAO.buscarPorUsername(usr);
                if (u != null) {
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
        return recuperacionDAO.getBancoPreguntas().stream()
                .map(p -> mensajeHandler.get("recuperacion.pregunta." + p.getId()))
                .collect(Collectors.toList());
    }

    // CORREGIDO: El método ahora recibe y pasa un objeto Usuario
    public void registrarPreguntas(Usuario usuario, List<Respuesta> respuestasList) {
        String[] preg = new String[respuestasList.size()];
        String[] resp = new String[respuestasList.size()];
        for (int i = 0; i < respuestasList.size(); i++) {
            preg[i] = respuestasList.get(i).getPregunta().getPregunta();
            resp[i] = respuestasList.get(i).getRespuesta();
        }
        recuperacionDAO.guardarRespuestas(usuario, preg, resp);
    }

    public void mostrarRecuperar(String username) {
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