package ec.edu.ups.dao.impl.ArchBin;
import ec.edu.ups.dao.UsuarioDAO;
import ec.edu.ups.modelo.Rol;
import ec.edu.ups.modelo.Usuario;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * La clase {@code UsuarioDAOArchivoBinario} es una implementación de la interfaz {@link UsuarioDAO}
 * que persiste los objetos {@link Usuario} en un archivo binario.
 * Utiliza serialización de objetos de Java para guardar y cargar las listas de usuarios.
 */
public class UsuarioDAOArchivoBinario implements UsuarioDAO {

    private String rutaArchivo;

    /**
     * Constructor de la clase {@code UsuarioDAOArchivoBinario}.
     *
     * @param rutaArchivo La ruta completa del archivo binario donde se guardarán/cargarán los usuarios.
     */
    public UsuarioDAOArchivoBinario(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
        inicializarArchivo();
    }

    /**
     * Inicializa el archivo binario: crea el directorio si no existe y luego crea el archivo
     * si aún no existe. Muestra mensajes de error si ocurren problemas durante la creación.
     */
    private void inicializarArchivo() {
        File archivo = new File(rutaArchivo);
        File directorio = archivo.getParentFile();

        if (directorio != null && !directorio.exists()) {
            directorio.mkdirs(); // Crea el directorio y todos sus padres si no existen
        }

        if (!archivo.exists()) {
            try {
                archivo.createNewFile(); // Crea el archivo vacío si no existe
            } catch (IOException e) {
                System.err.println("Error al crear el archivo binario de usuarios: " + e.getMessage());
            }
        }
    }

    /**
     * Guarda la lista completa de usuarios en el archivo binario especificado por {@code rutaArchivo}.
     * Utiliza {@link ObjectOutputStream} para serializar la lista de objetos.
     *
     * @param usuarios La lista de objetos {@link Usuario} a guardar.
     */
    private void guardarTodosLosUsuarios(List<Usuario> usuarios) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(rutaArchivo))) {
            oos.writeObject(usuarios);
        } catch (IOException e) {
            System.err.println("Error al guardar usuarios en archivo binario: " + e.getMessage());
        }
    }

    /**
     * Carga todos los usuarios desde el archivo binario especificado por {@code rutaArchivo}.
     * Utiliza {@link ObjectInputStream} para deserializar la lista de objetos.
     * Maneja casos de archivo vacío o errores de lectura/deserialización.
     *
     * @return Una {@link List} de objetos {@link Usuario} cargados desde el archivo.
     * Retorna una lista vacía si el archivo está vacío o ocurre un error.
     */
    private List<Usuario> cargarTodosLosUsuarios() {
        List<Usuario> usuarios = new ArrayList<>();
        File archivo = new File(rutaArchivo);
        if (archivo.length() == 0) { // Archivo vacío, no hay nada que cargar
            return usuarios;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(rutaArchivo))) {
            Object obj = ois.readObject();
            if (obj instanceof List) {
                usuarios = (List<Usuario>) obj;
            }
        } catch (FileNotFoundException e) {
            System.err.println("Archivo de usuarios no encontrado: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error al cargar usuarios desde archivo binario: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("Clase Usuario no encontrada al deserializar: " + e.getMessage());
        }
        return usuarios;
    }

    /**
     * Autentica un usuario verificando su nombre de usuario y contraseña
     * en la lista de usuarios cargada desde el archivo binario.
     *
     * @param username El nombre de usuario a autenticar.
     * @param contrasenia La contraseña del usuario.
     * @return El objeto {@link Usuario} si las credenciales coinciden, o {@code null} en caso contrario.
     */
    @Override
    public Usuario autenticar(String username, String contrasenia) {
        List<Usuario> usuarios = cargarTodosLosUsuarios();
        for (Usuario usuario : usuarios) {
            if (usuario.getUsername().equals(username) && usuario.getContrasenia().equals(contrasenia)) {
                return usuario;
            }
        }
        return null;
    }

    /**
     * Crea y persiste un nuevo objeto {@link Usuario} en el archivo binario.
     * Carga todos los usuarios existentes, añade el nuevo si no existe ya un usuario
     * con el mismo nombre de usuario, y luego guarda la lista completa.
     *
     * @param usuario El objeto {@link Usuario} a ser creado.
     */
    @Override
    public void crear(Usuario usuario) {
        List<Usuario> usuarios = cargarTodosLosUsuarios();
        boolean found = false;
        for (Usuario u : usuarios) {
            if (u.getUsername().equals(usuario.getUsername())) {
                found = true;
                break;
            }
        }
        if (!found) {
            usuarios.add(usuario);
            guardarTodosLosUsuarios(usuarios);
        } else {
            System.err.println("Error: El usuario " + usuario.getUsername() + " ya existe. No se creará.");
        }
    }

    /**
     * Busca un objeto {@link Usuario} por su nombre de usuario en el archivo binario.
     *
     * @param username El nombre de usuario del usuario a buscar.
     * @return El objeto {@link Usuario} si se encuentra, o {@code null} si no existe.
     */
    @Override
    public Usuario buscarPorUsername(String username) {
        List<Usuario> usuarios = cargarTodosLosUsuarios();
        for (Usuario usuario : usuarios) {
            if (usuario.getUsername().equals(username)) {
                return usuario;
            }
        }
        return null;
    }

    /**
     * Elimina un usuario del archivo binario utilizando su nombre de usuario.
     * Carga todos los usuarios, remueve el que coincide con el nombre de usuario
     * y luego guarda la lista actualizada.
     *
     * @param username El nombre de usuario del usuario a eliminar.
     */
    @Override
    public void eliminar(String username) {
        List<Usuario> usuarios = cargarTodosLosUsuarios();
        Iterator<Usuario> iterator = usuarios.iterator();
        boolean removed = false;
        while (iterator.hasNext()) {
            Usuario usuario = iterator.next();
            if (usuario.getUsername().equals(username)) {
                iterator.remove();
                removed = true;
                break;
            }
        }
        if (removed) {
            guardarTodosLosUsuarios(usuarios);
        } else {
            System.err.println("Usuario " + username + " no encontrado para eliminar.");
        }
    }

    /**
     * Actualiza la información de un objeto {@link Usuario} existente en el archivo binario.
     * Carga todos los usuarios, busca el usuario por su nombre de usuario y lo reemplaza
     * con la versión actualizada. Luego, guarda la lista completa de usuarios.
     *
     * @param usuario El objeto {@link Usuario} con la información actualizada.
     */
    @Override
    public void actualizar(Usuario usuario) {
        List<Usuario> usuarios = cargarTodosLosUsuarios();
        boolean updated = false;
        for (int i = 0; i < usuarios.size(); i++) {
            Usuario usuarioAux = usuarios.get(i);
            if (usuarioAux.getUsername().equals(usuario.getUsername())) {
                usuarios.set(i, usuario);
                updated = true;
                break;
            }
        }
        if (updated) {
            guardarTodosLosUsuarios(usuarios);
        } else {
            System.err.println("Usuario " + usuario.getUsername() + " no encontrado para actualizar.");
        }
    }

    /**
     * Devuelve una lista de todos los usuarios actualmente almacenados en el archivo binario.
     *
     * @return Una {@link List} de todos los objetos {@link Usuario} cargados desde el archivo.
     */
    @Override
    public List<Usuario> listarTodos() {
        return cargarTodosLosUsuarios();
    }

    /**
     * Devuelve una lista de usuarios que tienen un rol específico,
     * cargándolos desde el archivo binario.
     *
     * @param rol El {@link Rol} por el cual se desea filtrar los usuarios.
     * @return Una {@link List} de objetos {@link Usuario} que coinciden con el rol especificado.
     */
    @Override
    public List<Usuario> listarPorRol(Rol rol) {
        List<Usuario> usuariosEncontrados = new ArrayList<>();
        List<Usuario> usuarios = cargarTodosLosUsuarios();
        for (Usuario usuario : usuarios) {
            if (usuario.getRol().equals(rol)) {
                usuariosEncontrados.add(usuario);
            }
        }
        return usuariosEncontrados;
    }
}