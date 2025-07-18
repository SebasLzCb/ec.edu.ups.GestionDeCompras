package ec.edu.ups.dao;

import ec.edu.ups.modelo.Rol;
import ec.edu.ups.modelo.Usuario;

import java.util.List;

/**
 * La interfaz {@code UsuarioDAO} define el contrato para las operaciones de acceso
 * a datos relacionadas con la entidad {@link Usuario}.
 * Proporciona métodos abstractos para realizar operaciones CRUD (Crear, Leer, Actualizar, Eliminar)
 * y otras operaciones de consulta específicas para los usuarios.
 * Cualquier clase que implemente esta interfaz debe proporcionar la lógica
 * para la persistencia de usuarios en un determinado medio (memoria, archivo, base de datos, etc.).
 */
public interface UsuarioDAO {
    /**
     * Autentica un usuario en el sistema verificando su nombre de usuario y contraseña.
     *
     * @param username El nombre de usuario a autenticar.
     * @param contrasenia La contraseña del usuario.
     * @return El objeto {@link Usuario} si la autenticación es exitosa y las credenciales coinciden,
     * o {@code null} si no se encuentra el usuario o las credenciales son incorrectas.
     */
    Usuario autenticar(String username, String contrasenia);

    /**
     * Crea y persiste un nuevo objeto {@link Usuario} en el medio de almacenamiento.
     *
     * @param usuario El objeto {@link Usuario} a ser creado.
     */
    void crear(Usuario usuario);

    /**
     * Busca y recupera un objeto {@link Usuario} utilizando su nombre de usuario.
     *
     * @param username El nombre de usuario del usuario a buscar.
     * @return El objeto {@link Usuario} si se encuentra, o {@code null} si no existe un usuario con ese nombre de usuario.
     */
    Usuario buscarPorUsername(String username);

    /**
     * Elimina un usuario del medio de almacenamiento utilizando su nombre de usuario.
     *
     * @param username El nombre de usuario del usuario a eliminar.
     */
    void eliminar(String username);

    /**
     * Actualiza la información de un objeto {@link Usuario} existente en el medio de almacenamiento.
     * La actualización se realiza basándose en el nombre de usuario del objeto {@link Usuario} proporcionado.
     *
     * @param usuario El objeto {@link Usuario} con la información actualizada.
     */
    void actualizar(Usuario usuario);

    /**
     * Lista y recupera todos los objetos {@link Usuario} almacenados en el medio de persistencia.
     *
     * @return Una {@link List} de todos los objetos {@link Usuario} disponibles.
     * Retorna una lista vacía si no hay usuarios.
     */
    List<Usuario> listarTodos();

    /**
     * Lista y recupera todos los objetos {@link Usuario} que tienen un rol específico.
     *
     * @param rol El {@link Rol} por el cual se desea filtrar los usuarios.
     * @return Una {@link List} de objetos {@link Usuario} que coinciden con el rol especificado.
     * Retorna una lista vacía si no hay usuarios con ese rol.
     */
    List<Usuario> listarPorRol(Rol rol);
}