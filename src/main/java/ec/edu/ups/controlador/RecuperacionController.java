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

public class RecuperacionController {

    private RecuperacionDAO recuperacionDAO;
    private UsuarioDAO      usuarioDAO;
    private final MensajeInternacionalizacionHandler mensajeHandler;
    private final DAOManager daoManager;

    private LoginView loginView;
    private RecuperarContraseñaView recuperarView;

    public RecuperacionController(RecuperacionDAO recuperacionDAO,
                                  UsuarioDAO usuarioDAO,
                                  MensajeInternacionalizacionHandler mensajeHandler,
                                  DAOManager daoManager) {
        this.recuperacionDAO = recuperacionDAO;
        this.usuarioDAO      = usuarioDAO;
        this.mensajeHandler  = mensajeHandler;
        this.daoManager = daoManager;
    }

    public RecuperacionDAO getRecuperacionDAO() {
        return recuperacionDAO;
    }

    public void setRecuperacionDAO(RecuperacionDAO recuperacionDAO) {
        this.recuperacionDAO = recuperacionDAO;
    }

    public void setUsuarioDAO(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    public void setLoginView(LoginView loginView) {
        this.loginView = loginView;
    }

    public void setRecuperarView(RecuperarContraseñaView recuperarView) {
        this.recuperarView = recuperarView;

        recuperarView.getBtnSiguiente().addActionListener(e -> {
            String usr = recuperarView.getTxtUsuario().getText().trim();
            String pregunta = recuperarView.getPreguntas()[0];
            String respuesta = recuperarView.getRespuestas()[0];

            // Usar las DAOs actualizadas del DAOManager
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

    public List<String> obtenerPreguntasLocalizadas() {
        System.out.println("DEBUG (RecuperacionController - obtenerPreguntasLocalizadas): Obteniendo preguntas del banco con DAO: " + recuperacionDAO.getClass().getSimpleName());
        return recuperacionDAO.getBancoPreguntas().stream()
                .map(p -> mensajeHandler.get("recuperacion.pregunta." + p.getId()))
                .collect(Collectors.toList());
    }

    public void registrarPreguntas(Usuario usuario, List<Respuesta> respuestasList) {
        System.out.println("DEBUG (RecuperacionController - registrarPreguntas): Guardando respuestas para " + usuario.getUsername() + " con DAO: " + recuperacionDAO.getClass().getSimpleName());
        String[] preg = new String[respuestasList.size()];
        String[] resp = new String[respuestasList.size()];
        for (int i = 0; i < respuestasList.size(); i++) {
            preg[i] = respuestasList.get(i).getPregunta().getPregunta();
            resp[i] = respuestasList.get(i).getRespuesta();
        }
        recuperacionDAO.guardarRespuestas(usuario, preg, resp);
    }

    public void mostrarRecuperar(String username) {
        System.out.println("DEBUG (RecuperacionController - mostrarRecuperar): Intentando mostrar recuperación para " + username);
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