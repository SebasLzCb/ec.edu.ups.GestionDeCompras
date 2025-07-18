package ec.edu.ups.dao.impl;

import ec.edu.ups.dao.CarritoDAO;
import ec.edu.ups.modelo.Carrito;

import java.util.ArrayList;
import java.util.List;

/**
 * La clase {@code CarritoDAOMemoria} es una implementación en memoria de la interfaz {@link CarritoDAO}.
 * Almacena los objetos {@link Carrito} en una {@link List} en la memoria RAM,
 * lo que significa que los datos no persisten una vez que la aplicación se cierra.
 *
 * @author [Tu Nombre/Equipo]
 * @version 1.0
 * @since 2023-01-01
 */
public class CarritoDAOMemoria implements CarritoDAO {

    private final List<Carrito> carritos;

    /**
     * Constructor de la clase {@code CarritoDAOMemoria}.
     * Inicializa la lista de carritos vacía.
     */
    public CarritoDAOMemoria() {
        this.carritos = new ArrayList<>();
    }

    /**
     * Añade un nuevo objeto {@link Carrito} a la lista en memoria.
     * Muestra un mensaje de depuración indicando el código del carrito guardado.
     *
     * @param carrito El objeto {@link Carrito} a ser creado.
     */
    @Override
    public void crear(Carrito carrito) {
        System.out.println("GUARDANDO carrito con código: " + carrito.getCodigo());
        carritos.add(carrito);
    }

    /**
     * Busca un objeto {@link Carrito} por su código en la lista en memoria.
     *
     * @param codigo El código del carrito a buscar.
     * @return El objeto {@link Carrito} si se encuentra, o {@code null} si no existe.
     */
    @Override
    public Carrito buscarPorCodigo(int codigo) {
        for (Carrito c : carritos) {
            if (c.getCodigo() == codigo) {
                return c;
            }
        }
        return null;
    }

    /**
     * Actualiza la información de un objeto {@link Carrito} existente en la lista en memoria.
     * La actualización se realiza buscando el carrito por su código y reemplazándolo.
     *
     * @param carrito El objeto {@link Carrito} con la información actualizada.
     */
    @Override
    public void actualizar(Carrito carrito) {
        for (int i = 0; i < carritos.size(); i++) {
            if (carritos.get(i).getCodigo() == carrito.getCodigo()) {
                carritos.set(i, carrito);
                return; // Se asume que el código es único y se actualiza uno a la vez
            }
        }
    }

    /**
     * Elimina un carrito de la lista en memoria utilizando su código.
     *
     * @param codigo El código del carrito a eliminar.
     */
    @Override
    public void eliminar(int codigo) {
        carritos.removeIf(c -> c.getCodigo() == codigo);
    }

    /**
     * Devuelve una nueva {@link List} que contiene todos los carritos
     * actualmente almacenados en memoria.
     * Se devuelve una copia para evitar modificaciones directas a la lista interna.
     *
     * @return Una {@link List} de todos los objetos {@link Carrito}.
     */
    @Override
    public List<Carrito> listarTodos() {
        return new ArrayList<>(carritos);
    }
}