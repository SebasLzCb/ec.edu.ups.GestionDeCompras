package ec.edu.ups.modelo;

import java.io.Serializable;

public class Respuesta  implements Serializable {
    private Usuario usuario;
    private Pregunta pregunta;
    private String respuesta;

    public Respuesta(Usuario usuario, Pregunta pregunta, String respuesta) {
        this.usuario = usuario;
        this.pregunta = pregunta;
        this.respuesta = respuesta;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Pregunta getPregunta() {
        return pregunta;
    }

    public void setPregunta(Pregunta pregunta) {
        this.pregunta = pregunta;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }
}