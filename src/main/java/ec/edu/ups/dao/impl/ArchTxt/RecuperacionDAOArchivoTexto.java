package ec.edu.ups.dao.impl.ArchTxt;

import ec.edu.ups.dao.RecuperacionDAO;
import ec.edu.ups.dao.UsuarioDAO;
import ec.edu.ups.modelo.Pregunta;
import ec.edu.ups.modelo.Respuesta;
import ec.edu.ups.modelo.Usuario;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RecuperacionDAOArchivoTexto implements RecuperacionDAO {

    private String rutaArchivoPreguntas;
    private String rutaArchivoRespuestas;
    private final UsuarioDAO usuarioDAO;
    private static final String DELIMITADOR = "\\|";

    private transient final List<Pregunta> bancoPreguntas;

    public RecuperacionDAOArchivoTexto(String rutaArchivoPreguntas, String rutaArchivoRespuestas, UsuarioDAO usuarioDAO) {
        this.rutaArchivoPreguntas = rutaArchivoPreguntas; // Se mantiene por consistencia, pero no se usa activamente aquí
        this.rutaArchivoRespuestas = rutaArchivoRespuestas;
        this.usuarioDAO = usuarioDAO;
        this.bancoPreguntas = new ArrayList<>();
        inicializarBancoPreguntas();
        inicializarArchivos(); // Crea los archivos si no existen
    }

    private void inicializarBancoPreguntas() {
        bancoPreguntas.add(new Pregunta(1, "¿Cuál es tu equipo favorito?"));
        bancoPreguntas.add(new Pregunta(2, "¿Nombre de tu primera mascota?"));
        bancoPreguntas.add(new Pregunta(3, "¿Ciudad donde naciste?"));
        bancoPreguntas.add(new Pregunta(4, "¿Tu color preferido?"));
        bancoPreguntas.add(new Pregunta(5, "¿Modelo de tu primer auto?"));
        bancoPreguntas.add(new Pregunta(6, "¿Comida favorita?"));
        bancoPreguntas.add(new Pregunta(7, "¿Nombre de tu actor favorito?"));
        bancoPreguntas.add(new Pregunta(8, "¿Lugar de tus vacaciones favoritas?"));
        bancoPreguntas.add(new Pregunta(9, "¿Asignatura favorita en la escuela?"));
        bancoPreguntas.add(new Pregunta(10, "¿Nombre de tu mejor amigo de la infancia?"));
    }

    private void inicializarArchivos() {
        File archivoRespuestasFile = new File(rutaArchivoRespuestas);
        File dirRespuestas = archivoRespuestasFile.getParentFile();

        if (dirRespuestas != null && !dirRespuestas.exists()) dirRespuestas.mkdirs();

        try {
            if (!archivoRespuestasFile.exists()) archivoRespuestasFile.createNewFile();
        } catch (IOException e) {
            System.err.println("Error al crear archivo de respuestas de recuperación: " + e.getMessage());
        }
    }

    private void guardarRespuestasEnArchivo(List<Respuesta> respuestas) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(rutaArchivoRespuestas))) {
            for (Respuesta resp : respuestas) {
                writer.write(respuestaATexto(resp));
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error al guardar respuestas de seguridad en archivo: " + e.getMessage());
        }
    }

    private List<Respuesta> cargarTodasLasRespuestasDesdeArchivo() {
        List<Respuesta> respuestas = new ArrayList<>();
        File archivo = new File(rutaArchivoRespuestas);
        if (!archivo.exists() || archivo.length() == 0) {
            return respuestas;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(rutaArchivoRespuestas))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Respuesta respuesta = textoARespuesta(line);
                if (respuesta != null) {
                    respuestas.add(respuesta);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer respuestas de seguridad desde archivo: " + e.getMessage());
        }
        return respuestas;
    }

    private String respuestaATexto(Respuesta respuesta) {
        return String.join("|",
                escapeString(respuesta.getUsuario().getUsername()),
                String.valueOf(respuesta.getPregunta().getId()),
                escapeString(respuesta.getRespuesta())
        );
    }

    private Respuesta textoARespuesta(String line) {
        String[] partes = line.split(DELIMITADOR);
        if (partes.length == 3) {
            try {
                String username = unescapeString(partes[0]);
                int preguntaId = Integer.parseInt(partes[1]);
                String textoRespuesta = unescapeString(partes[2]);

                Usuario usuario = usuarioDAO.buscarPorUsername(username);
                Pregunta pregunta = bancoPreguntas.stream()
                        .filter(p -> p.getId() == preguntaId)
                        .findFirst()
                        .orElse(null);

                if (usuario != null && pregunta != null) {
                    return new Respuesta(usuario, pregunta, textoRespuesta);
                } else {
                    System.err.println("Error: Usuario o Pregunta no encontrados para la respuesta: " + line);
                }
            } catch (NumberFormatException e) {
                System.err.println("Error en formato de ID de pregunta en archivo de respuestas: " + line + " - " + e.getMessage());
            }
        }
        return null;
    }

    private String escapeString(String s) {
        return s.replace("|", "&#124;");
    }

    private String unescapeString(String s) {
        return s.replace("&#124;", "|");
    }

    @Override
    public List<Pregunta> getBancoPreguntas() {
        return new ArrayList<>(bancoPreguntas);
    }

    @Override
    public List<Respuesta> getRespuestasUsuario(String user) {
        List<Respuesta> todasLasRespuestas = cargarTodasLasRespuestasDesdeArchivo();
        return todasLasRespuestas.stream()
                .filter(r -> r.getUsuario().getUsername().equals(user))
                .collect(Collectors.toList());
    }

    @Override
    public boolean validarRespuesta(Respuesta r) {
        return getRespuestasUsuario(r.getUsuario().getUsername()).stream()
                .anyMatch(alm -> alm.getPregunta().getId() == r.getPregunta().getId()
                        && alm.getRespuesta().equalsIgnoreCase(r.getRespuesta()));
    }

    @Override
    public List<String> getPreguntas() {
        return bancoPreguntas.stream()
                .map(Pregunta::getPregunta)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getPreguntasUsuario(String username) {
        return getRespuestasUsuario(username).stream()
                .map(r -> r.getPregunta().getPregunta())
                .collect(Collectors.toList());
    }

    @Override
    public boolean validar(String username, String preguntaTexto, String respuestaDada) {
        List<Respuesta> respuestasDelUsuario = getRespuestasUsuario(username);
        return respuestasDelUsuario.stream()
                .anyMatch(r -> r.getPregunta().getPregunta().equals(preguntaTexto)
                        && r.getRespuesta().equalsIgnoreCase(respuestaDada.trim()));
    }

    @Override
    public void guardarRespuestas(Usuario usuario, String[] pregTextos, String[] respTextos) {
        List<Respuesta> todasLasRespuestas = cargarTodasLasRespuestasDesdeArchivo();

        todasLasRespuestas.removeIf(r -> r.getUsuario().getUsername().equals(usuario.getUsername()));

        for (int i = 0; i < pregTextos.length; i++) {
            String textoPregunta = pregTextos[i];
            Pregunta preguntaObj = bancoPreguntas.stream()
                    .filter(p -> p.getPregunta().equals(textoPregunta))
                    .findFirst()
                    .orElse(null);
            if (preguntaObj != null) {
                todasLasRespuestas.add(new Respuesta(usuario, preguntaObj, respTextos[i]));
            } else {
                System.err.println("Advertencia: Pregunta '" + textoPregunta + "' no encontrada en el banco de preguntas.");
            }
        }
        guardarRespuestasEnArchivo(todasLasRespuestas);
    }
}