package ec.edu.ups.dao.impl;

import ec.edu.ups.dao.UsuarioDAO;
import ec.edu.ups.modelo.Rol;
import ec.edu.ups.modelo.Usuario;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * La clase {@code UsuarioDAOMemoria} es una implementación en memoria de la interfaz {@link UsuarioDAO}.
 * Almacena los objetos {@link Usuario} en una {@link List} en la memoria RAM,
 * lo que significa que los datos no persisten una vez que la aplicación se cierra.
 * Incluye datos iniciales para un usuario administrador y un usuario regular.
 *
 * @author [Tu Nombre/Equipo]
 * @version 1.0
 * @since 2023-01-01
 */
public class UsuarioDAOMemoria implements UsuarioDAO {

    private List<Usuario> usuarios;

    /**
     * Constructor de la clase {@code UsuarioDAOMemoria}.
     * Inicializa la lista de usuarios y crea un usuario "admin" y un usuario "user"
     * por defecto para pruebas.
     */
    public UsuarioDAOMemoria() {
        usuarios = new ArrayList<Usuario>();
        // Creación del usuario administrador por defecto
        crear(new Usuario("admin", "12345", Rol.ADMINISTRADOR,
                "Administrador General", LocalDate.of(1990,1,1),
                "admin@ejemplo.com", "0999999999"));
        // Creación de un usuario regular por defecto
        crear(new Usuario("user", "12345", Rol.USUARIO));
    }

    /**
     * Autentica un usuario verificando su nombre de usuario y contraseña en la lista en memoria.
     *
     * @param username El nombre de usuario a autenticar.
     * @param contrasenia La contraseña del usuario.
     * @return El objeto {@link Usuario} si las credenciales coinciden, o {@code null} en caso contrario.
     */
    @Override
    public Usuario autenticar(String username, String contrasenia) {
        for (Usuario usuario : usuarios) {
            if(usuario.getUsername().equals(username) && usuario.getContrasenia().equals(contrasenia)){
                return usuario;
            }
        }
        return null;
    }

    /**
     * Añade un nuevo objeto {@link Usuario} a la lista en memoria.
     *
     * @param usuario El objeto {@link Usuario} a ser creado.
     */
    @Override
    public void crear(Usuario usuario) {
        usuarios.add(usuario);
    }

    /**
     * Busca un objeto {@link Usuario} por su nombre de usuario en la lista en memoria.
     *
     * @param username El nombre de usuario del usuario a buscar.
     * @return El objeto {@link Usuario} si se encuentra, o {@code null} si no existe.
     */
    @Override
    public Usuario buscarPorUsername(String username) {
        for (Usuario usuario : usuarios) {
            if (usuario.getUsername().equals(username)) {
                return usuario;
            }
        }
        return null;
    }

    /**
     * Elimina un usuario de la lista en memoria utilizando su nombre de usuario.
     *
     * @param username El nombre de usuario del usuario a eliminar.
     */
    @Override
    public void eliminar(String username) {
        Iterator<Usuario> iterator = usuarios.iterator();
        while (iterator.hasNext()) {
            Usuario usuario = iterator.next();
            if (usuario.getUsername().equals(username)) {
                iterator.remove();
                break;
            }
        }
    }

    /**
     * Actualiza la información de un objeto {@link Usuario} existente en la lista en memoria.
     * La actualización se realiza buscando el usuario por su nombre de usuario y reemplazándolo.
     *
     * @param usuario El objeto {@link Usuario} con la información actualizada.
     */
    @Override
    public void actualizar(Usuario usuario) {
        for(int i = 0; i < usuarios.size(); i++){
            Usuario usuarioAux = usuarios.get(i);
            if(usuarioAux.getUsername().equals(usuario.getUsername())){
                usuarios.set(i, usuario);
                break;
            }
        }
    }

    /**
     * Devuelve una lista de todos los usuarios actualmente almacenados en memoria.
     *
     * @return Una {@link List} de todos los objetos {@link Usuario}.
     */
    @Override
    public List<Usuario> listarTodos() {
        return usuarios;
    }

    /**
     * Devuelve una lista de usuarios que tienen un rol específico.
     *
     * @param rol El {@link Rol} por el cual se desea filtrar los usuarios.
     * @return Una {@link List} de objetos {@link Usuario} que coinciden con el rol especificado.
     */
    @Override
    public List<Usuario> listarPorRol(Rol rol) {
        List<Usuario> usuariosEncontrados = new ArrayList<>();

        for (Usuario usuario : usuarios) {
            if (usuario.getRol().equals(rol)) {
                usuariosEncontrados.add(usuario);
            }
        }
        return usuariosEncontrados;
    }
}