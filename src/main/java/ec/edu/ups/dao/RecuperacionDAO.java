package ec.edu.ups.dao;

import ec.edu.ups.modelo.Pregunta;
import ec.edu.ups.modelo.Respuesta;
import ec.edu.ups.modelo.Usuario; // Se importa Usuario

import java.util.List;

public interface RecuperacionDAO {

    List<Pregunta> getBancoPreguntas();

    boolean validar(String username, String pregunta, String respuesta);

    List<String> getPreguntas();

    List<String> getPreguntasUsuario(String username);

    // CORREGIDO: Ahora recibe un objeto Usuario
    void guardarRespuestas(Usuario usuario, String[] preguntas, String[] respuestas);

    List<Respuesta> getRespuestasUsuario(String user);

    boolean validarRespuesta(Respuesta r);
}