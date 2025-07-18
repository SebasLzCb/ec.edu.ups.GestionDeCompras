package ec.edu.ups.dao;

import ec.edu.ups.modelo.Producto;

import java.util.List;

/**
 * La interfaz {@code ProductoDAO} define el contrato para las operaciones de acceso
 * a datos relacionadas con la entidad {@link Producto}.
 * Proporciona métodos abstractos para realizar operaciones CRUD (Crear, Leer, Actualizar, Eliminar)
 * y otras operaciones de consulta específicas para los productos.
 * Cualquier clase que implemente esta interfaz debe proporcionar la lógica
 * para la persistencia de productos en un determinado medio (memoria, archivo, base de datos, etc.).
 */
public interface ProductoDAO {

    /**
     * Crea y persiste un nuevo objeto {@link Producto} en el medio de almacenamiento.
     *
     * @param producto El objeto {@link Producto} a ser creado.
     */
    void crear(Producto producto);

    /**
     * Busca y recupera un objeto {@link Producto} utilizando su código único.
     *
     * @param codigo El código del producto a buscar.
     * @return El objeto {@link Producto} si se encuentra, o {@code null} si no existe un producto con ese código.
     */
    Producto buscarPorCodigo(int codigo);

    /**
     * Busca y recupera una lista de objetos {@link Producto} que coincidan
     * parcialmente con un nombre dado.
     *
     * @param nombre El nombre (o parte del nombre) del producto a buscar.
     * @return Una {@link List} de objetos {@link Producto} que coinciden con el nombre.
     * Retorna una lista vacía si no se encuentran productos.
     */
    List<Producto> buscarPorNombre(String nombre);

    /**
     * Actualiza la información de un objeto {@link Producto} existente en el medio de almacenamiento.
     * La actualización se realiza basándose en el código del objeto {@link Producto} proporcionado.
     *
     * @param producto El objeto {@link Producto} con la información actualizada.
     */
    void actualizar(Producto producto);

    /**
     * Elimina un producto del medio de almacenamiento utilizando su código.
     *
     * @param codigo El código del producto a eliminar.
     */
    void eliminar(int codigo);

    /**
     * Lista y recupera todos los objetos {@link Producto} almacenados en el medio de persistencia.
     *
     * @return Una {@link List} de todos los objetos {@link Producto} disponibles.
     * Retorna una lista vacía si no hay productos.
     */
    List<Producto> listarTodos();

}