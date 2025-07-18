package ec.edu.ups.dao;

import ec.edu.ups.modelo.Carrito;
import java.util.List;

/**
 * La interfaz {@code CarritoDAO} define el contrato para las operaciones de acceso
 * a datos relacionadas con la entidad {@link Carrito}.
 * Proporciona métodos abstractos para realizar operaciones CRUD (Crear, Leer, Actualizar, Eliminar)
 * y una operación de consulta para listar todos los carritos.
 * Cualquier clase que implemente esta interfaz debe proporcionar la lógica
 * para la persistencia de carritos en un determinado medio (memoria, archivo, base de datos, etc.).
 */
public interface CarritoDAO {
    /**
     * Crea y persiste un nuevo objeto {@link Carrito} en el medio de almacenamiento.
     *
     * @param carrito El objeto {@link Carrito} a ser creado.
     */
    void crear(Carrito carrito);

    /**
     * Busca y recupera un objeto {@link Carrito} utilizando su código único.
     *
     * @param codigo El código del carrito a buscar.
     * @return El objeto {@link Carrito} si se encuentra, o {@code null} si no existe un carrito con ese código.
     */
    Carrito buscarPorCodigo(int codigo);

    /**
     * Actualiza la información de un objeto {@link Carrito} existente en el medio de almacenamiento.
     * La actualización se realiza basándose en el código del objeto {@link Carrito} proporcionado.
     *
     * @param carrito El objeto {@link Carrito} con la información actualizada.
     */
    void actualizar(Carrito carrito);

    /**
     * Elimina un carrito del medio de almacenamiento utilizando su código.
     *
     * @param codigo El código del carrito a eliminar.
     */
    void eliminar(int codigo);

    /**
     * Lista y recupera todos los objetos {@link Carrito} almacenados en el medio de persistencia.
     *
     * @return Una {@link List} de todos los objetos {@link Carrito} disponibles.
     * Retorna una lista vacía si no hay carritos.
     */
    List<Carrito> listarTodos();
}