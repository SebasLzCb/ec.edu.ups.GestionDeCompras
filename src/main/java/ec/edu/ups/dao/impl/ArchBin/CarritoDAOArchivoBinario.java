package ec.edu.ups.dao.impl.ArchBin;

import ec.edu.ups.dao.CarritoDAO;
import ec.edu.ups.dao.ProductoDAO; // Necesario para resolver Productos
import ec.edu.ups.dao.UsuarioDAO;   // Necesario para resolver Usuarios
import ec.edu.ups.modelo.Carrito;
import ec.edu.ups.modelo.Producto; // Para asegurar que Producto es serializable al leer ItemCarrito
import ec.edu.ups.modelo.Usuario;   // Para asegurar que Usuario es serializable al leer Carrito

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CarritoDAOArchivoBinario implements CarritoDAO {

    private String rutaArchivo;
    private transient ProductoDAO productoDAO; // transient porque es una dependencia, no parte de los datos
    private transient UsuarioDAO usuarioDAO;   // transient porque es una dependencia, no parte de los datos


    public CarritoDAOArchivoBinario(String rutaArchivo, ProductoDAO productoDAO, UsuarioDAO usuarioDAO) {
        this.rutaArchivo = rutaArchivo;
        this.productoDAO = productoDAO;
        this.usuarioDAO = usuarioDAO;
        inicializarArchivo();
    }

    private void inicializarArchivo() {
        File archivo = new File(rutaArchivo);
        File directorio = archivo.getParentFile();

        if (directorio != null && !directorio.exists()) {
            directorio.mkdirs();
        }

        if (!archivo.exists()) {
            try {
                archivo.createNewFile();
            } catch (IOException e) {
                System.err.println("Error al crear el archivo binario de carritos: " + e.getMessage());
            }
        }
    }

    private void guardarTodosLosCarritos(List<Carrito> carritos) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(rutaArchivo))) {
            oos.writeObject(carritos);
        } catch (IOException e) {
            System.err.println("Error al guardar carritos en archivo binario: " + e.getMessage());
        }
    }

    private List<Carrito> cargarTodosLosCarritos() {
        List<Carrito> carritos = new ArrayList<>();
        File archivo = new File(rutaArchivo);
        if (archivo.length() == 0) { // Archivo vacío
            return carritos;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(rutaArchivo))) {
            Object obj = ois.readObject();
            if (obj instanceof List) {
                carritos = (List<Carrito>) obj;
                // Post-deserialización: Reasociar objetos transitorios si es necesario
                // En Carrito, los objetos Producto y Usuario están dentro de ItemCarrito y Carrito.
                // Si Producto y Usuario no tienen todos sus campos serializados (ej. si no implementan Serializable bien),
                // o si la instancia de Producto/Usuario necesita ser la "activa" de la sesión,
                // se podría necesitar un paso de "rehidratación" aquí, usando productoDAO/usuarioDAO.
                // Por ahora, asumimos que al ser serializables, los objetos se cargan completos.
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

    @Override
    public void crear(Carrito carrito) {
        List<Carrito> carritos = cargarTodosLosCarritos();
        carritos.add(carrito);
        guardarTodosLosCarritos(carritos);
    }

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

    @Override
    public void actualizar(Carrito carrito) {
        List<Carrito> carritos = cargarTodosLosCarritos();
        for (int i = 0; i < carritos.size(); i++) {
            if (carritos.get(i).getCodigo() == carrito.getCodigo()) {
                carritos.set(i, carrito);
                return;
            }
        }
        guardarTodosLosCarritos(carritos);
    }

    @Override
    public void eliminar(int codigo) {
        List<Carrito> carritos = cargarTodosLosCarritos();
        carritos.removeIf(c -> c.getCodigo() == codigo);
        guardarTodosLosCarritos(carritos);
    }

    @Override
    public List<Carrito> listarTodos() {
        return cargarTodosLosCarritos();
    }
}