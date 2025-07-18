package ec.edu.ups.dao.impl;

import ec.edu.ups.dao.CarritoDAO;
import ec.edu.ups.modelo.Carrito;

import java.util.List;

public class CarritoDAOArchivoTexto implements CarritoDAO {

    public CarritoDAOArchivoTexto() {
        // Constructor vacío por ahora
    }

    @Override
    public void crear(Carrito carrito) {
        System.out.println("DEBUG: Creando carrito en archivos de texto (placeholder): " + carrito.getCodigo());
        // Lógica real de creación en archivo se implementará aquí
    }

    @Override
    public Carrito buscarPorCodigo(int codigo) {
        System.out.println("DEBUG: Buscando carrito en archivos de texto (placeholder): " + codigo);
        // Lógica real de búsqueda en archivo se implementará aquí
        return null;
    }

    @Override
    public void actualizar(Carrito carrito) {
        System.out.println("DEBUG: Actualizando carrito en archivos de texto (placeholder): " + carrito.getCodigo());
        // Lógica real de actualización en archivo se implementará aquí
    }

    @Override
    public void eliminar(int codigo) {
        System.out.println("DEBUG: Eliminando carrito en archivos de texto (placeholder): " + codigo);
        // Lógica real de eliminación en archivo se implementará aquí
    }

    @Override
    public List<Carrito> listarTodos() {
        System.out.println("DEBUG: Listando todos los carritos en archivos de texto (placeholder)");
        // Lógica real de listado en archivo se implementará aquí
        return new java.util.ArrayList<>();
    }
}