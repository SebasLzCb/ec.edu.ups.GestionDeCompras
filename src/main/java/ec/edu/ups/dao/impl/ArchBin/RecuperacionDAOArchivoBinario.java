package ec.edu.ups.dao.impl.ArchBin;

import ec.edu.ups.dao.RecuperacionDAO;
import ec.edu.ups.dao.UsuarioDAO;
import ec.edu.ups.modelo.Pregunta;
import ec.edu.ups.modelo.Respuesta;
import ec.edu.ups.modelo.Usuario;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RecuperacionDAOArchivoBinario implements RecuperacionDAO {

    private String rutaArchivoPreguntas; // No usada activamente para guardar/cargar banco, pero se mantiene
    private String rutaArchivoRespuestas;
    private final transient UsuarioDAO usuarioDAO; // Marcado como transient, no se serializa
    private transient final List<Pregunta> bancoPreguntas; // Marcado como transient, se inicializa al inicio

    public RecuperacionDAOArchivoBinario(String rutaArchivoPreguntas, String rutaArchivoRespuestas, UsuarioDAO usuarioDAO) {
        this.rutaArchivoPreguntas = rutaArchivoPreguntas;
        this.rutaArchivoRespuestas = rutaArchivoRespuestas;
        this.usuarioDAO = usuarioDAO;
        this.bancoPreguntas = new ArrayList<>();
        inicializarBancoPreguntas();
        inicializarArchivos();
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

        if (dirRespuestas != null && !dirRespuestas.exists()) {
            dirRespuestas.mkdirs(); // Crea directorios si no existen
        }

        try {
            if (!archivoRespuestasFile.exists()) archivoRespuestasFile.createNewFile();
        } catch (IOException e) {
            System.err.println("Error al crear archivo binario de respuestas de recuperación: " + e.getMessage());
        }
    }

    @Override
    public List<Pregunta> getBancoPreguntas() {
        return new ArrayList<>(bancoPreguntas);
    }

    @Override
    public List<Respuesta> getRespuestasUsuario(String username) {
        List<Respuesta> todasRespuestas = cargarTodasLasRespuestas();
        System.out.println("DEBUG (RecuperacionDAOArchivoBinario): Cargadas " + todasRespuestas.size() + " respuestas.");
        return todasRespuestas.stream()
                .filter(r -> r.getUsuario() != null && r.getUsuario().getUsername().equals(username))
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
        System.out.println("DEBUG (RecuperacionDAOArchivoBinario): Buscando preguntas para usuario: " + username);
        List<String> userQuestions = getRespuestasUsuario(username).stream()
                .map(r -> r.getPregunta().getPregunta())
                .collect(Collectors.toList());
        System.out.println("DEBUG (RecuperacionDAOArchivoBinario): Preguntas encontradas para " + username + ": " + userQuestions);
        return userQuestions;
    }

    @Override
    public boolean validar(String username, String pregunta, String respuesta) {
        System.out.println("DEBUG (RecuperacionDAOArchivoBinario): Validando respuesta para " + username + ", Pregunta: " + pregunta + ", Respuesta: " + respuesta);
        return getRespuestasUsuario(username).stream()
                .anyMatch(r -> r.getPregunta().getPregunta().equals(pregunta)
                        && r.getRespuesta().equalsIgnoreCase(respuesta));
    }

    @Override
    public void guardarRespuestas(Usuario usuario, String[] preg, String[] resp) {
        List<Respuesta> todasRespuestas = cargarTodasLasRespuestas();
        System.out.println("DEBUG (RecuperacionDAOArchivoBinario): Respuestas antes de guardar (" + todasRespuestas.size() + "):");
        todasRespuestas.forEach(r -> System.out.println("  - " + r.getUsuario().getUsername() + " | " + r.getPregunta().getId() + " | " + r.getRespuesta()));

        todasRespuestas.removeIf(r -> r.getUsuario().getUsername().equals(usuario.getUsername()));

        for (int i = 0; i < preg.length; i++) {
            String textoPregunta = preg[i];
            Pregunta preguntaObj = bancoPreguntas.stream()
                    .filter(p -> p.getPregunta().equals(textoPregunta))
                    .findFirst()
                    .orElse(null);
            if (preguntaObj != null) {
                todasRespuestas.add(new Respuesta(usuario, preguntaObj, resp[i]));
            } else {
                System.err.println("Advertencia: Pregunta '" + textoPregunta + "' no encontrada en el banco de preguntas.");
            }
        }
        guardarTodasLasRespuestas(todasRespuestas);
        System.out.println("DEBUG (RecuperacionDAOArchivoBinario): Respuestas guardadas. Total: " + todasRespuestas.size());
        todasRespuestas.forEach(r -> System.out.println("  - " + r.getUsuario().getUsername() + " | " + r.getPregunta().getId() + " | " + r.getRespuesta()));
    }

    private void guardarTodasLasRespuestas(List<Respuesta> respuestas) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(rutaArchivoRespuestas))) {
            oos.writeObject(respuestas);
        } catch (IOException e) {
            System.err.println("ERROR (RecuperacionDAOArchivoBinario): Error al guardar respuestas de recuperación: " + e.getMessage());
        }
    }

    private List<Respuesta> cargarTodasLasRespuestas() {
        List<Respuesta> respuestas = new ArrayList<>();
        File archivo = new File(rutaArchivoRespuestas);
        if (archivo.length() == 0) {
            return respuestas;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(rutaArchivoRespuestas))) {
            Object obj = ois.readObject();
            if (obj instanceof List) {
                List<Respuesta> deserializedList = (List<Respuesta>) obj;
                // Post-deserialización: Rehidratar Usuario y Pregunta con instancias "activas"
                List<Respuesta> rehydratedResponses = new ArrayList<>();
                for (Respuesta r : deserializedList) {
                    // El usuario se busca en el DAO activo para asegurar que sea la instancia correcta
                    Usuario rehydratedUser = usuarioDAO.buscarPorUsername(r.getUsuario().getUsername());
                    // La pregunta se busca en el banco de preguntas estático (no serializado)
                    Pregunta rehydratedQuestion = bancoPreguntas.stream()
                            .filter(p -> p.getId() == r.getPregunta().getId())
                            .findFirst().orElse(null);
                    if (rehydratedUser != null && rehydratedQuestion != null) {
                        rehydratedResponses.add(new Respuesta(rehydratedUser, rehydratedQuestion, r.getRespuesta()));
                    } else {
                        System.err.println("Advertencia (RecuperacionDAOArchivoBinario): No se pudo rehidratar Usuario/Pregunta para la respuesta: " + r.getUsuario().getUsername() + " | " + r.getPregunta().getId());
                    }
                }
                return rehydratedResponses;
            }
        } catch (FileNotFoundException e) {
            System.err.println("ERROR (RecuperacionDAOArchivoBinario): Archivo de respuestas no encontrado: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("ERROR (RecuperacionDAOArchivoBinario): Error al cargar respuestas desde archivo binario: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("ERROR (RecuperacionDAOArchivoBinario): Clase no encontrada al deserializar respuestas: " + e.getMessage());
        }
        return respuestas;
    }
}