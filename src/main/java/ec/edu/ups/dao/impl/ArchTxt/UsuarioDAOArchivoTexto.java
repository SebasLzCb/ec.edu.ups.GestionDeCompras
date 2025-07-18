package ec.edu.ups.dao.impl.ArchTxt;

import ec.edu.ups.dao.UsuarioDAO;
import ec.edu.ups.modelo.Rol;
import ec.edu.ups.modelo.Usuario;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class UsuarioDAOArchivoTexto implements UsuarioDAO {

    private String rutaArchivo;
    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
    private static final String DELIMITADOR = "\\|";

    public UsuarioDAOArchivoTexto(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
        inicializarArchivo();
    }

    private void inicializarArchivo() {
        File archivo = new File(rutaArchivo);
        File directorio = archivo.getParentFile();

        if (directorio != null && !directorio.exists()) {
            directorio.mkdirs();
        }
        if (!archivo.exists()) {
            try {
                archivo.createNewFile();
            } catch (IOException e) {
                System.err.println("Error al crear el archivo de usuarios: " + e.getMessage());
            }
        }
    }

    private void guardarUsuariosEnArchivo(List<Usuario> usuarios) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(rutaArchivo))) {
            for (Usuario usuario : usuarios) {
                writer.write(usuarioATexto(usuario));
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error al guardar usuarios en archivo: " + e.getMessage());
        }
    }

    private List<Usuario> cargarUsuariosDesdeArchivo() {
        List<Usuario> usuarios = new ArrayList<>();
        File archivo = new File(rutaArchivo);
        if (!archivo.exists() || archivo.length() == 0) { // Comprobar si el archivo está vacío
            return usuarios;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(rutaArchivo))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Usuario usuario = textoAUsuario(line);
                if (usuario != null) {
                    usuarios.add(usuario);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer usuarios desde archivo: " + e.getMessage());
        }
        return usuarios;
    }

    private String usuarioATexto(Usuario usuario) {
        String fechaNacimientoStr = (usuario.getFechaNacimiento() != null) ? usuario.getFechaNacimiento().format(formatter) : "";
        String nombreCompleto = (usuario.getNombreCompleto() != null) ? usuario.getNombreCompleto() : "";
        String correo = (usuario.getCorreo() != null) ? usuario.getCorreo() : "";
        String telefono = (usuario.getTelefono() != null) ? usuario.getTelefono() : "";

        return String.join("|",
                escapeString(usuario.getUsername()),
                escapeString(usuario.getContrasenia()),
                usuario.getRol().name(),
                escapeString(nombreCompleto),
                fechaNacimientoStr,
                escapeString(correo),
                escapeString(telefono)
        );
    }

    private Usuario textoAUsuario(String line) {
        String[] partes = line.split(DELIMITADOR);
        if (partes.length == 7) {
            try {
                Usuario usuario = new Usuario();
                usuario.setUsername(unescapeString(partes[0]));
                usuario.setContrasenia(unescapeString(partes[1]));
                usuario.setRol(Rol.valueOf(partes[2]));
                usuario.setNombreCompleto(unescapeString(partes[3]));
                usuario.setFechaNacimiento(partes[4].isEmpty() ? null : LocalDate.parse(partes[4], formatter));
                usuario.setCorreo(unescapeString(partes[5]));
                usuario.setTelefono(unescapeString(partes[6]));
                return usuario;
            } catch (IllegalArgumentException e) {
                System.err.println("Error en formato de datos de usuario en archivo: " + line + " - " + e.getMessage());
            }
        }
        return null;
    }

    // Métodos para escapar/desescapar el delimitador (|) si aparece en los datos
    private String escapeString(String s) {
        return s.replace("|", "&#124;");
    }

    private String unescapeString(String s) {
        return s.replace("&#124;", "|");
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
        if (usuarios.stream().noneMatch(u -> u.getUsername().equals(usuario.getUsername()))) { // Más limpio
            usuarios.add(usuario);
            guardarUsuariosEnArchivo(usuarios);
        } else {
            System.err.println("Error: El usuario " + usuario.getUsername() + " ya existe. No se creará.");
        }
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