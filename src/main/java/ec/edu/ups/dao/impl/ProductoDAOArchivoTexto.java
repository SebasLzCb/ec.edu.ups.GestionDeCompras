package ec.edu.ups.dao.impl;

import ec.edu.ups.dao.ProductoDAO;
import ec.edu.ups.modelo.Producto;

import java.util.List;

public class ProductoDAOArchivoTexto implements ProductoDAO {

    public ProductoDAOArchivoTexto() {
        // Constructor vacío por ahora
    }

    @Override
    public void crear(Producto producto) {
        System.out.println("DEBUG: Creando producto en archivos de texto (placeholder): " + producto.getNombre());
        // Lógica real de creación en archivo se implementará aquí
    }

    @Override
    public Producto buscarPorCodigo(int codigo) {
        System.out.println("DEBUG: Buscando producto en archivos de texto (placeholder): " + codigo);
        // Lógica real de búsqueda en archivo se implementará aquí
        return null;
    }

    @Override
    public List<Producto> buscarPorNombre(String nombre) {
        System.out.println("DEBUG: Buscando producto por nombre en archivos de texto (placeholder): " + nombre);
        // Lógica real de búsqueda en archivo se implementará aquí
        return new java.util.ArrayList<>();
    }

    @Override
    public void actualizar(Producto producto) {
        System.out.println("DEBUG: Actualizando producto en archivos de texto (placeholder): " + producto.getNombre());
        // Lógica real de actualización en archivo se implementará aquí
    }

    @Override
    public void eliminar(int codigo) {
        System.out.println("DEBUG: Eliminando producto en archivos de texto (placeholder): " + codigo);
        // Lógica real de eliminación en archivo se implementará aquí
    }

    @Override
    public List<Producto> listarTodos() {
        System.out.println("DEBUG: Listando todos los productos en archivos de texto (placeholder)");
        // Lógica real de listado en archivo se implementará aquí
        return new java.util.ArrayList<>();
    }
}