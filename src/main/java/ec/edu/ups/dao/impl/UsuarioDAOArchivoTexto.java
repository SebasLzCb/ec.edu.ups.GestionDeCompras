package ec.edu.ups.dao.impl;

import ec.edu.ups.dao.UsuarioDAO;
import ec.edu.ups.modelo.Rol;
import ec.edu.ups.modelo.Usuario;
import java.io.*; // Importa clases de I/O
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class UsuarioDAOArchivoTexto implements UsuarioDAO {

    private String rutaArchivo = "data/usuarios.txt"; // Ruta predefinida
    private String rutaDirectorio = "data"; // Directorio donde se guardará el archivo

    public UsuarioDAOArchivoTexto() {
        // Asegurarse de que el directorio exista al inicializar
        File directorio = new File(rutaDirectorio);
        if (!directorio.exists()) {
            directorio.mkdirs(); // Crea el directorio si no existe
        }
        // Puedes cargar los usuarios existentes al iniciar el DAO
        // o esperar a que se soliciten explícitamente.
    }

    private void guardarUsuariosEnArchivo(List<Usuario> usuarios) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(rutaArchivo))) { // FileWriter sobrescribe por defecto
            for (Usuario usuario : usuarios) {
                // Formato: username|contrasenia|rol|nombreCompleto|fechaNacimiento|correo|telefono
                writer.write(usuario.getUsername() + "|" +
                        usuario.getContrasenia() + "|" + // NOTA: Aquí se guarda en texto plano, en el futuro se usará un hash.
                        usuario.getRol().name() + "|" +
                        (usuario.getNombreCompleto() != null ? usuario.getNombreCompleto() : "") + "|" +
                        (usuario.getFechaNacimiento() != null ? usuario.getFechaNacimiento().toString() : "") + "|" +
                        (usuario.getCorreo() != null ? usuario.getCorreo() : "") + "|" +
                        (usuario.getTelefono() != null ? usuario.getTelefono() : ""));
                writer.newLine(); // Salto de línea para cada usuario
            }
        } catch (IOException e) { // Captura excepciones de I/O
            System.err.println("Error al guardar usuarios en archivo: " + e.getMessage());
            // Aquí podrías lanzar una excepción de negocio o logear el error
        }
    }

    private List<Usuario> cargarUsuariosDesdeArchivo() {
        List<Usuario> usuarios = new ArrayList<>();
        File archivo = new File(rutaArchivo);
        if (!archivo.exists()) {
            return usuarios; // Si el archivo no existe, retorna una lista vacía
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(rutaArchivo))) { // FileReader para leer texto
            String line;
            while ((line = reader.readLine()) != null) { // Lee línea por línea
                String[] partes = line.split("\\|");
                if (partes.length >= 3) { // Mínimo username, contrasenia, rol
                    Usuario usuario = new Usuario();
                    usuario.setUsername(partes[0]);
                    usuario.setContrasenia(partes[1]); // NOTA: Contraseña en texto plano
                    usuario.setRol(Rol.valueOf(partes[2]));
                    if (partes.length > 3 && !partes[3].isEmpty()) {
                        usuario.setNombreCompleto(partes[3]);
                    }
                    if (partes.length > 4 && !partes[4].isEmpty()) {
                        usuario.setFechaNacimiento(LocalDate.parse(partes[4]));
                    }
                    if (partes.length > 5 && !partes[5].isEmpty()) {
                        usuario.setCorreo(partes[5]);
                    }
                    if (partes.length > 6 && !partes[6].isEmpty()) {
                        usuario.setTelefono(partes[6]);
                    }
                    usuarios.add(usuario);
                }
            }
        } catch (FileNotFoundException e) { // Excepción si el archivo no existe
            System.err.println("Archivo de usuarios no encontrado: " + e.getMessage());
        } catch (IOException e) { // Excepción general de I/O
            System.err.println("Error al leer usuarios desde archivo: " + e.getMessage());
        } catch (IllegalArgumentException e) { // Para Rol.valueOf si el string no es un enum válido
            System.err.println("Error en formato de datos de usuario en archivo: " + e.getMessage());
        }
        return usuarios;
    }

    @Override
    public Usuario autenticar(String username, String contrasenia) {
        List<Usuario> usuarios = cargarUsuariosDesdeArchivo();
        for (Usuario usuario : usuarios) {
            if (usuario.getUsername().equals(username) && usuario.getContrasenia().equals(contrasenia)) {
                return usuario;
            }
        }
        return null;
    }

    @Override
    public void crear(Usuario usuario) {
        List<Usuario> usuarios = cargarUsuariosDesdeArchivo();
        // Verificar si el usuario ya existe antes de crearlo
        if (buscarPorUsername(usuario.getUsername()) != null) {
            System.err.println("Error: El usuario " + usuario.getUsername() + " ya existe.");
            return; // O lanzar una excepción de negocio
        }
        usuarios.add(usuario);
        guardarUsuariosEnArchivo(usuarios);
    }

    @Override
    public Usuario buscarPorUsername(String username) {
        List<Usuario> usuarios = cargarUsuariosDesdeArchivo();
        for (Usuario usuario : usuarios) {
            if (usuario.getUsername().equals(username)) {
                return usuario;
            }
        }
        return null;
    }

    @Override
    public void eliminar(String username) {
        List<Usuario> usuarios = cargarUsuariosDesdeArchivo();
        boolean removido = usuarios.removeIf(u -> u.getUsername().equals(username));
        if (removido) {
            guardarUsuariosEnArchivo(usuarios);
        } else {
            System.err.println("Usuario " + username + " no encontrado para eliminar.");
        }
    }

    @Override
    public void actualizar(Usuario usuario) {
        List<Usuario> usuarios = cargarUsuariosDesdeArchivo();
        boolean actualizado = false;
        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.get(i).getUsername().equals(usuario.getUsername())) {
                usuarios.set(i, usuario);
                actualizado = true;
                break;
            }
        }
        if (actualizado) {
            guardarUsuariosEnArchivo(usuarios);
        } else {
            System.err.println("Usuario " + usuario.getUsername() + " no encontrado para actualizar.");
        }
    }

    @Override
    public List<Usuario> listarTodos() {
        return cargarUsuariosDesdeArchivo();
    }

    @Override
    public List<Usuario> listarPorRol(Rol rol) {
        return cargarUsuariosDesdeArchivo().stream()
                .filter(u -> u.getRol().equals(rol))
                .collect(Collectors.toList());
    }
}