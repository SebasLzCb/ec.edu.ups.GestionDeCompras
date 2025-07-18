package ec.edu.ups.dao.impl;

import ec.edu.ups.dao.RecuperacionDAO;
import ec.edu.ups.dao.UsuarioDAO;
import ec.edu.ups.modelo.Pregunta;
import ec.edu.ups.modelo.Respuesta;
import ec.edu.ups.modelo.Usuario;

import java.io.*; // Importar clases de I/O
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class RecuperacionDAOArchivoTexto implements RecuperacionDAO {

    private final UsuarioDAO usuarioDAO; // Necesario para buscar usuarios al cargar respuestas
    private final List<Pregunta> bancoPreguntas; // Banco de preguntas estático (no se lee de archivo)
    private final String rutaRespuestasArchivo = "data/respuestas_seguridad.txt"; // Archivo para respuestas de usuarios
    private final String rutaDirectorio = "data";

    public RecuperacionDAOArchivoTexto(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
        this.bancoPreguntas = new ArrayList<>();
        inicializarBancoPreguntas(); // Carga las preguntas estáticas

        // Asegurarse de que el directorio exista al inicializar
        File directorio = new File(rutaDirectorio);
        if (!directorio.exists()) {
            directorio.mkdirs();
        }
    }

    // Método para inicializar el banco de preguntas estáticas (similar a DAOMemoria)
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

    @Override
    public List<Pregunta> getBancoPreguntas() {
        return new ArrayList<>(bancoPreguntas); // Devuelve una copia del banco estático
    }

    // Métodos para guardar y cargar respuestas de seguridad en archivo de texto
    private void guardarRespuestasEnArchivo(List<Respuesta> respuestas) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(rutaRespuestasArchivo))) {
            for (Respuesta resp : respuestas) {
                // Formato: username|preguntaId|respuestaTexto
                writer.write(resp.getUsuario().getUsername() + "|" +
                        resp.getPregunta().getId() + "|" +
                        resp.getRespuesta());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error al guardar respuestas de seguridad en archivo: " + e.getMessage());
        }
    }

    private List<Respuesta> cargarTodasLasRespuestasDesdeArchivo() {
        List<Respuesta> respuestas = new ArrayList<>();
        File archivo = new File(rutaRespuestasArchivo);
        if (!archivo.exists()) {
            return respuestas;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(rutaRespuestasArchivo))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] partes = line.split("\\|");
                if (partes.length == 3) {
                    String username = partes[0];
                    int preguntaId = Integer.parseInt(partes[1]);
                    String respuestaTexto = partes[2];

                    Usuario usuario = usuarioDAO.buscarPorUsername(username); // Necesitamos el UsuarioDAO aquí
                    Pregunta pregunta = bancoPreguntas.stream()
                            .filter(p -> p.getId() == preguntaId)
                            .findFirst()
                            .orElse(null);

                    if (usuario != null && pregunta != null) {
                        respuestas.add(new Respuesta(usuario, pregunta, respuestaTexto));
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Archivo de respuestas de seguridad no encontrado: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error al leer respuestas de seguridad desde archivo: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Error en formato de ID de pregunta en archivo de respuestas: " + e.getMessage());
        }
        return respuestas;
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
        // Este método en la interfaz RecuperacionDAO es genérico,
        // pero la implementación específica del banco de preguntas se obtiene de getBancoPreguntas()
        // y luego se mapea al texto de la pregunta.
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

        // Eliminar respuestas antiguas para este usuario
        todasLasRespuestas.removeIf(r -> r.getUsuario().getUsername().equals(usuario.getUsername()));

        // Añadir las nuevas respuestas
        for (int i = 0; i < pregTextos.length; i++) {
            String textoPregunta = pregTextos[i];
            Pregunta preguntaObj = bancoPreguntas.stream()
                    .filter(p -> p.getPregunta().equals(textoPregunta))
                    .findFirst()
                    .orElse(null);
            if (preguntaObj != null) {
                todasLasRespuestas.add(new Respuesta(usuario, preguntaObj, respTextos[i]));
            } else {
                System.err.println("ADVERTENCIA: Pregunta no encontrada en el banco de preguntas: " + textoPregunta);
            }
        }
        guardarRespuestasEnArchivo(todasLasRespuestas);
    }
}