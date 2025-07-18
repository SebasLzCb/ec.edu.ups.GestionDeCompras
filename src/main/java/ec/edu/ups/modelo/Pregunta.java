package ec.edu.ups.modelo;

import java.io.Serializable;

public class Pregunta  implements Serializable {
    private int id;
    private String pregunta;

    public Pregunta(int id, String pregunta) {
        this.id = id;
        this.pregunta = pregunta;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPregunta() {
        return pregunta;
    }

    public void setPregunta(String pregunta) {
        this.pregunta = pregunta;
    }
}
