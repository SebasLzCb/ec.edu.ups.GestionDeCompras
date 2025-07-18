package ec.edu.ups.dao.impl;

import ec.edu.ups.dao.ProductoDAO;
import ec.edu.ups.modelo.Producto;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * La clase {@code ProductoDAOMemoria} es una implementación en memoria de la interfaz {@link ProductoDAO}.
 * Almacena los objetos {@link Producto} en una {@link List} en la memoria RAM,
 * lo que significa que los datos no persisten una vez que la aplicación se cierra.
 *
 * @author [Tu Nombre/Equipo]
 * @version 1.0
 * @since 2023-01-01
 */
public class ProductoDAOMemoria implements ProductoDAO {

    private List<Producto> productos;

    /**
     * Constructor de la clase {@code ProductoDAOMemoria}.
     * Inicializa la lista de productos vacía.
     */
    public ProductoDAOMemoria() {
        productos = new ArrayList<Producto>();
    }

    /**
     * Añade un nuevo objeto {@link Producto} a la lista en memoria.
     *
     * @param producto El objeto {@link Producto} a ser creado.
     */
    @Override
    public void crear(Producto producto) {
        productos.add(producto);
    }

    /**
     * Busca un objeto {@link Producto} por su código en la lista en memoria.
     *
     * @param codigo El código del producto a buscar.
     * @return El objeto {@link Producto} si se encuentra, o {@code null} si no existe.
     */
    @Override
    public Producto buscarPorCodigo(int codigo) {
        for (Producto producto : productos) {
            if (producto.getCodigo() == codigo) {
                return producto;
            }
        }
        return null;
    }

    /**
     * Busca una lista de objetos {@link Producto} cuyo nombre comienza
     * con la cadena de texto especificada.
     *
     * @param nombre El nombre (o prefijo del nombre) del producto a buscar.
     * @return Una {@link List} de objetos {@link Producto} que coinciden con el nombre.
     */
    @Override
    public List<Producto> buscarPorNombre(String nombre) {
        List<Producto> productosEncontrados = new ArrayList<>();
        for (Producto producto : productos) {
            if (producto.getNombre().startsWith(nombre)) {
                productosEncontrados.add(producto);
            }
        }
        return productosEncontrados;
    }

    /**
     * Actualiza la información de un objeto {@link Producto} existente en la lista en memoria.
     * La actualización se realiza buscando el producto por su código y reemplazándolo.
     *
     * @param producto El objeto {@link Producto} con la información actualizada.
     */
    @Override
    public void actualizar(Producto producto) {
        for (int i = 0; i < productos.size(); i++) {
            if (productos.get(i).getCodigo() == producto.getCodigo()) {
                productos.set(i, producto);
                break;
            }
        }
    }

    /**
     * Elimina un producto de la lista en memoria utilizando su código.
     *
     * @param codigo El código del producto a eliminar.
     */
    @Override
    public void eliminar(int codigo) {
        Iterator<Producto> iterator = productos.iterator();
        while (iterator.hasNext()) {
            Producto producto = iterator.next();
            if (producto.getCodigo() == codigo) {
                iterator.remove();
            }
        }
    }

    /**
     * Devuelve una lista de todos los productos actualmente almacenados en memoria.
     *
     * @return Una {@link List} de todos los objetos {@link Producto}.
     */
    @Override
    public List<Producto> listarTodos() {
        return productos;
    }
}