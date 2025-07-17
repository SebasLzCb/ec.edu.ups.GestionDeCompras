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

public class RecuperacionDAOMemoria implements RecuperacionDAO {

    private final List<Pregunta> bancoPreguntas;
    private final List<String[]> datosRecuperacion;
    private final List<Respuesta> respuestas;
    private final UsuarioDAO usuarioDAO;

    public RecuperacionDAOMemoria(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
        this.bancoPreguntas = new ArrayList<>();
        this.datosRecuperacion = new ArrayList<>();
        this.respuestas = new ArrayList<>();
        inicializarBancoPreguntas();
        cargarRespuestasIniciales();
    }

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

    @Override
    public List<Pregunta> getBancoPreguntas() {
        return new ArrayList<>(bancoPreguntas);
    }

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

    @Override
    public boolean validarRespuesta(Respuesta r) {
        return getRespuestasUsuario(r.getUsuario().getUsername()).stream()
                .anyMatch(alm -> alm.getPregunta().getId() == r.getPregunta().getId()
                        && alm.getRespuesta().equalsIgnoreCase(r.getRespuesta()));
    }

    @Override
    public List<String> getPreguntas() {
        List<String> list = new ArrayList<>();
        for (Pregunta p : bancoPreguntas) {
            list.add(p.getPregunta());
        }
        return list;
    }

    @Override
    public List<String> getPreguntasUsuario(String username) {
        for (String[] entry : datosRecuperacion) {
            if (entry[0].equals(username)) {
                return Arrays.asList(entry[1], entry[3], entry[5]);
            }
        }
        return Collections.emptyList();
    }

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

    @Override
    // CORREGIDO: El método ahora usa el objeto Usuario para obtener el username
    public void guardarRespuestas(Usuario usuario, String[] preg, String[] resp) {
        String username = usuario.getUsername();

        datosRecuperacion.removeIf(entry -> entry[0].equals(username));
        respuestas.removeIf(r -> r.getUsuario().getUsername().equals(username));

        String[] nuevo = new String[1 + 3 * 2];
        nuevo[0] = username;
        for (int i = 0; i < 3; i++) {
            nuevo[1 + 2 * i] = preg[i];
            nuevo[2 + 2 * i] = resp[i];
        }
        datosRecuperacion.add(nuevo);

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