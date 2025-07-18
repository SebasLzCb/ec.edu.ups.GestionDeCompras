package ec.edu.ups.dao.impl.ArchBin;

import ec.edu.ups.dao.ProductoDAO;
import ec.edu.ups.modelo.Producto;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ProductoDAOArchivoBinario implements ProductoDAO {

    private String rutaArchivo;

    public ProductoDAOArchivoBinario(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
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
                System.err.println("Error al crear el archivo binario de productos: " + e.getMessage());
            }
        }
    }

    private void guardarTodosLosProductos(List<Producto> productos) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(rutaArchivo))) {
            oos.writeObject(productos);
        } catch (IOException e) {
            System.err.println("Error al guardar productos en archivo binario: " + e.getMessage());
        }
    }

    private List<Producto> cargarTodosLosProductos() {
        List<Producto> productos = new ArrayList<>();
        File archivo = new File(rutaArchivo);
        if (archivo.length() == 0) {
            return productos;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(rutaArchivo))) {
            Object obj = ois.readObject();
            if (obj instanceof List) {
                productos = (List<Producto>) obj;
            }
        } catch (FileNotFoundException e) {
            System.err.println("Archivo de productos no encontrado: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error al cargar productos desde archivo binario: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("Clase Producto no encontrada al deserializar: " + e.getMessage());
        }
        return productos;
    }

    @Override
    public void crear(Producto producto) {
        List<Producto> productos = cargarTodosLosProductos();
        boolean found = false;
        for (Producto p : productos) {
            if (p.getCodigo() == producto.getCodigo()) {
                found = true;
                break;
            }
        }
        if (!found) {
            productos.add(producto);
            guardarTodosLosProductos(productos);
        }
    }

    @Override
    public Producto buscarPorCodigo(int codigo) {
        List<Producto> productos = cargarTodosLosProductos();
        for (Producto producto : productos) {
            if (producto.getCodigo() == codigo) {
                return producto;
            }
        }
        return null;
    }

    @Override
    public List<Producto> buscarPorNombre(String nombre) {
        List<Producto> productosEncontrados = new ArrayList<>();
        List<Producto> productos = cargarTodosLosProductos();
        for (Producto producto : productos) {
            if (producto.getNombre().startsWith(nombre)) {
                productosEncontrados.add(producto);
            }
        }
        return productosEncontrados;
    }

    @Override
    public void actualizar(Producto producto) {
        List<Producto> productos = cargarTodosLosProductos();
        for (int i = 0; i < productos.size(); i++) {
            if (productos.get(i).getCodigo() == producto.getCodigo()) {
                productos.set(i, producto);
                break;
            }
        }
        guardarTodosLosProductos(productos);
    }

    @Override
    public void eliminar(int codigo) {
        List<Producto> productos = cargarTodosLosProductos();
        Iterator<Producto> iterator = productos.iterator();
        while (iterator.hasNext()) {
            Producto producto = iterator.next();
            if (producto.getCodigo() == codigo) {
                iterator.remove();
            }
        }
        guardarTodosLosProductos(productos);
    }

    @Override
    public List<Producto> listarTodos() {
        return cargarTodosLosProductos();
    }
}