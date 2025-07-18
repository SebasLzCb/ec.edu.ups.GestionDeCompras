package ec.edu.ups.dao.impl.ArchBin;

import ec.edu.ups.dao.CarritoDAO;
import ec.edu.ups.dao.ProductoDAO; // Necesario para resolver Productos
import ec.edu.ups.dao.UsuarioDAO;   // Necesario para resolver Usuarios
import ec.edu.ups.modelo.Carrito;
import ec.edu.ups.modelo.ItemCarrito;
import ec.edu.ups.modelo.Producto; // Para asegurar que Producto es serializable al leer ItemCarrito
import ec.edu.ups.modelo.Usuario;   // Para asegurar que Usuario es serializable al leer Carrito

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * La clase {@code CarritoDAOArchivoBinario} es una implementación de la interfaz {@link CarritoDAO}
 * que persiste los objetos {@link Carrito} en un archivo binario.
 * Utiliza serialización de objetos de Java para guardar y cargar las listas de carritos.
 *
 * <p>Esta clase requiere instancias de {@link ProductoDAO} y {@link UsuarioDAO}
 * para resolver las dependencias de {@link Producto} y {@link Usuario}
 * dentro de los objetos {@link Carrito} e {@link ItemCarrito} al cargar los datos.</p>
 */
public class CarritoDAOArchivoBinario implements CarritoDAO {

    private String rutaArchivo;
    private transient ProductoDAO productoDAO; // transient porque es una dependencia, no parte de los datos
    private transient UsuarioDAO usuarioDAO;   // transient porque es una dependencia, no parte de los datos

    /**
     * Constructor de la clase {@code CarritoDAOArchivoBinario}.
     *
     * @param rutaArchivo La ruta completa del archivo binario donde se guardarán/cargarán los carritos.
     * @param productoDAO Una instancia de {@link ProductoDAO} para resolver objetos {@link Producto} al cargar.
     * @param usuarioDAO  Una instancia de {@link UsuarioDAO} para resolver objetos {@link Usuario} al cargar.
     */
    public CarritoDAOArchivoBinario(String rutaArchivo, ProductoDAO productoDAO, UsuarioDAO usuarioDAO) {
        this.rutaArchivo = rutaArchivo;
        this.productoDAO = productoDAO;
        this.usuarioDAO = usuarioDAO;
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
                System.err.println("Error al crear el archivo binario de carritos: " + e.getMessage());
            }
        }
    }

    /**
     * Guarda la lista completa de carritos en el archivo binario especificado por {@code rutaArchivo}.
     * Utiliza {@link ObjectOutputStream} para serializar la lista de objetos.
     *
     * @param carritos La lista de objetos {@link Carrito} a guardar.
     */
    private void guardarTodosLosCarritos(List<Carrito> carritos) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(rutaArchivo))) {
            oos.writeObject(carritos);
        } catch (IOException e) {
            System.err.println("Error al guardar carritos en archivo binario: " + e.getMessage());
        }
    }

    /**
     * Carga todos los carritos desde el archivo binario especificado por {@code rutaArchivo}.
     * Utiliza {@link ObjectInputStream} para deserializar la lista de objetos.
     * Maneja casos de archivo vacío o errores de lectura/deserialización.
     *
     * @return Una {@link List} de objetos {@link Carrito} cargados desde el archivo.
     * Retorna una lista vacía si el archivo está vacío o ocurre un error.
     */
    private List<Carrito> cargarTodosLosCarritos() {
        List<Carrito> carritos = new ArrayList<>();
        File archivo = new File(rutaArchivo);
        if (archivo.length() == 0) { // Archivo vacío, no hay nada que cargar
            return carritos;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(rutaArchivo))) {
            Object obj = ois.readObject();
            if (obj instanceof List) {
                carritos = (List<Carrito>) obj;
                // Post-deserialización: Se asume que Producto y Usuario dentro de Carrito e ItemCarrito
                // son serializables y se cargan correctamente. Si la lógica de tu aplicación requiere
                // que estas instancias sean las "activas" de los DAOs, se necesitaría un paso adicional
                // de "rehidratación" aquí (ej. buscar el Producto/Usuario por ID en sus respectivos DAOs
                // y reemplazar la instancia deserializada). Por simplicidad, se asume que la serialización
                // es suficiente para mantener las relaciones y la integridad de los objetos.
            }
        } catch (FileNotFoundException e) {
            System.err.println("Archivo de carritos no encontrado: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error al cargar carritos desde archivo binario: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("Clase no encontrada al deserializar carritos: " + e.getMessage());
        }
        return carritos;
    }

    /**
     * Crea y persiste un nuevo objeto {@link Carrito} en el archivo binario.
     * Carga todos los carritos existentes, añade el nuevo y luego guarda la lista completa.
     *
     * @param carrito El objeto {@link Carrito} a ser creado.
     */
    @Override
    public void crear(Carrito carrito) {
        List<Carrito> carritos = cargarTodosLosCarritos();
        carritos.add(carrito);
        guardarTodosLosCarritos(carritos);
    }

    /**
     * Busca y recupera un objeto {@link Carrito} por su código en el archivo binario.
     *
     * @param codigo El código del carrito a buscar.
     * @return El objeto {@link Carrito} si se encuentra, o {@code null} si no existe.
     */
    @Override
    public Carrito buscarPorCodigo(int codigo) {
        List<Carrito> carritos = cargarTodosLosCarritos();
        for (Carrito c : carritos) {
            if (c.getCodigo() == codigo) {
                return c;
            }
        }
        return null;
    }

    /**
     * Actualiza la información de un objeto {@link Carrito} existente en el archivo binario.
     * Carga todos los carritos, busca el carrito por su código y lo reemplaza con la versión actualizada.
     * Luego, guarda la lista completa de carritos.
     *
     * @param carrito El objeto {@link Carrito} con la información actualizada.
     */
    @Override
    public void actualizar(Carrito carrito) {
        List<Carrito> carritos = cargarTodosLosCarritos();
        for (int i = 0; i < carritos.size(); i++) {
            if (carritos.get(i).getCodigo() == carrito.getCodigo()) {
                carritos.set(i, carrito);
                // Si el carrito se encontró y actualizó, se guarda la lista y se sale.
                guardarTodosLosCarritos(carritos); // Guardar después de la actualización
                return;
            }
        }
        // Si el carrito no se encontró para actualizar, se guarda de todas formas la lista sin cambios,
        // o se podría lanzar una excepción si se espera que el carrito exista.
        // Por la implementación actual, si no se encuentra, simplemente no se hace nada y no se guarda.
        // Si quieres que siempre se guarde la lista (aunque no se haya actualizado nada), quita el 'return'.
        // Si quieres que solo se guarde si hubo un cambio, manténlo así.
        // Para este DAO, la llamada a guardarTodosLosCarritos(carritos) al final del bucle sería más consistente
        // con la intención de persistir el estado actualizado de la lista.
        guardarTodosLosCarritos(carritos);
    }

    /**
     * Elimina un carrito del archivo binario utilizando su código.
     * Carga todos los carritos, remueve el que coincide con el código y luego guarda la lista actualizada.
     *
     * @param codigo El código del carrito a eliminar.
     */
    @Override
    public void eliminar(int codigo) {
        List<Carrito> carritos = cargarTodosLosCarritos();
        carritos.removeIf(c -> c.getCodigo() == codigo);
        guardarTodosLosCarritos(carritos);
    }

    /**
     * Devuelve una lista de todos los carritos actualmente almacenados en el archivo binario.
     *
     * @return Una {@link List} de todos los objetos {@link Carrito} cargados desde el archivo.
     */
    @Override
    public List<Carrito> listarTodos() {
        return cargarTodosLosCarritos();
    }
}