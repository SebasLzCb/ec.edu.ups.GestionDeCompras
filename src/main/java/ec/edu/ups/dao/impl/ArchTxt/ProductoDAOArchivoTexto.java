package ec.edu.ups.dao.impl.ArchTxt;

import ec.edu.ups.dao.ProductoDAO;
import ec.edu.ups.modelo.Producto;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ProductoDAOArchivoTexto implements ProductoDAO {

    private String rutaArchivo;
    private static final String DELIMITADOR = "\\|";

    public ProductoDAOArchivoTexto(String rutaArchivo) { // Constructor ahora recibe la ruta
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
                System.err.println("Error al crear el archivo de productos: " + e.getMessage());
            }
        }
    }

    private void guardarTodosLosProductos(List<Producto> productos) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(rutaArchivo))) {
            for (Producto producto : productos) {
                writer.write(productoATexto(producto));
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error al guardar productos en el archivo: " + e.getMessage());
        }
    }

    private List<Producto> cargarTodosLosProductos() {
        List<Producto> productos = new ArrayList<>();
        File archivo = new File(rutaArchivo);
        if (!archivo.exists() || archivo.length() == 0) {
            return productos;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                Producto producto = textoAProducto(linea);
                if (producto != null) {
                    productos.add(producto);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al cargar productos desde el archivo: " + e.getMessage());
        }
        return productos;
    }

    // Formato: codigo|nombre|precio
    private String productoATexto(Producto producto) {
        return String.join("|",
                String.valueOf(producto.getCodigo()),
                escapeString(producto.getNombre()),
                String.valueOf(producto.getPrecio())
        );
    }

    private Producto textoAProducto(String linea) {
        String[] partes = linea.split(DELIMITADOR);
        if (partes.length == 3) {
            try {
                int codigo = Integer.parseInt(partes[0]);
                String nombre = unescapeString(partes[1]);
                double precio = Double.parseDouble(partes[2]);
                return new Producto(codigo, nombre, precio);
            } catch (NumberFormatException e) {
                System.err.println("Error de formato en línea de producto: " + linea + " - " + e.getMessage());
            }
        }
        return null;
    }

    private String escapeString(String s) {
        return s.replace("|", "&#124;");
    }

    private String unescapeString(String s) {
        return s.replace("&#124;", "|");
    }

    @Override
    public void crear(Producto producto) {
        List<Producto> productos = cargarTodosLosProductos();
        if (productos.stream().noneMatch(p -> p.getCodigo() == producto.getCodigo())) {
            productos.add(producto);
            guardarTodosLosProductos(productos);
        } else {
            System.err.println("Error: El producto con código " + producto.getCodigo() + " ya existe. No se creará.");
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
        boolean actualizado = false;
        for (int i = 0; i < productos.size(); i++) {
            if (productos.get(i).getCodigo() == producto.getCodigo()) {
                productos.set(i, producto);
                actualizado = true;
                break;
            }
        }
        if (actualizado) {
            guardarTodosLosProductos(productos);
        } else {
            System.err.println("Producto con código " + producto.getCodigo() + " no encontrado para actualizar.");
        }
    }

    @Override
    public void eliminar(int codigo) {
        List<Producto> productos = cargarTodosLosProductos();
        Iterator<Producto> iterator = productos.iterator();
        boolean removido = false;
        while (iterator.hasNext()) {
            Producto producto = iterator.next();
            if (producto.getCodigo() == codigo) {
                iterator.remove();
                removido = true;
                break;
            }
        }
        if (removido) {
            guardarTodosLosProductos(productos);
        } else {
            System.err.println("Producto con código " + codigo + " no encontrado para eliminar.");
        }
    }

    @Override
    public List<Producto> listarTodos() {
        return cargarTodosLosProductos();
    }
}