package ec.edu.ups.dao.impl.ArchTxt;

import ec.edu.ups.dao.CarritoDAO;
import ec.edu.ups.dao.ProductoDAO;
import ec.edu.ups.dao.UsuarioDAO;
import ec.edu.ups.modelo.Carrito;
import ec.edu.ups.modelo.ItemCarrito;
import ec.edu.ups.modelo.Producto;
import ec.edu.ups.modelo.Usuario;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

/**
 * La clase {@code CarritoDAOArchivoTexto} es una implementación de la interfaz {@link CarritoDAO}
 * que persiste los objetos {@link Carrito} en un archivo de texto plano.
 * Cada línea del archivo representa un carrito, y los datos del carrito
 * y sus ítems se codifican en un formato de texto específico.
 *
 * <p>Esta clase requiere instancias de {@link ProductoDAO} y {@link UsuarioDAO}
 * para resolver las dependencias de {@link Producto} y {@link Usuario}
 * dentro de los objetos {@link Carrito} e {@link ItemCarrito} al cargar los datos.</p>
 */
public class CarritoDAOArchivoTexto implements CarritoDAO {

    private String rutaArchivo;
    private ProductoDAO productoDAO; // Dependencia para buscar productos por código
    private UsuarioDAO usuarioDAO;   // Dependencia para buscar usuarios por username

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final String DELIMITADOR_PRINCIPAL = "\\;"; // Delimitador para las partes principales del carrito (código;fecha;username;items)
    private static final String DELIMITADOR_ITEMS = "\\|"; // Delimitador para separar ítems dentro de la cadena de ítems
    private static final String DELIMITADOR_ITEM_DETALLE = "\\,"; // Delimitador para separar código y cantidad dentro de un ítem (codigo,cantidad)

    /**
     * Constructor de la clase {@code CarritoDAOArchivoTexto}.
     *
     * @param rutaArchivo La ruta completa del archivo de texto donde se guardarán/cargarán los carritos.
     * @param productoDAO Una instancia de {@link ProductoDAO} para resolver objetos {@link Producto} al cargar.
     * @param usuarioDAO  Una instancia de {@link UsuarioDAO} para resolver objetos {@link Usuario} al cargar.
     */
    public CarritoDAOArchivoTexto(String rutaArchivo, ProductoDAO productoDAO, UsuarioDAO usuarioDAO) {
        this.rutaArchivo = rutaArchivo;
        this.productoDAO = productoDAO;
        this.usuarioDAO = usuarioDAO;
        inicializarArchivo();
    }

    /**
     * Inicializa el archivo de texto: crea el directorio si no existe y luego crea el archivo
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
                System.err.println("Error al crear el archivo de carritos: " + e.getMessage());
            }
        }
    }

    /**
     * Guarda la lista completa de carritos en el archivo de texto especificado por {@code rutaArchivo}.
     * Cada carrito se convierte a una línea de texto utilizando {@link #carritoATexto(Carrito)}.
     *
     * @param carritos La lista de objetos {@link Carrito} a guardar.
     */
    private void guardarTodosLosCarritos(List<Carrito> carritos) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(rutaArchivo))) {
            for (Carrito carrito : carritos) {
                writer.write(carritoATexto(carrito));
                writer.newLine(); // Añade un salto de línea después de cada carrito
            }
        } catch (IOException e) {
            System.err.println("Error al guardar carritos en el archivo: " + e.getMessage());
        }
    }

    /**
     * Carga todos los carritos desde el archivo de texto especificado por {@code rutaArchivo}.
     * Cada línea del archivo se lee y se convierte a un objeto {@link Carrito}
     * utilizando {@link #textoACarrito(String)}.
     * Maneja casos de archivo vacío o errores de lectura/formato.
     *
     * @return Una {@link List} de objetos {@link Carrito} cargados desde el archivo.
     * Retorna una lista vacía si el archivo está vacío o ocurre un error.
     */
    private List<Carrito> cargarTodosLosCarritos() {
        List<Carrito> carritos = new ArrayList<>();
        File archivo = new File(rutaArchivo);
        if (!archivo.exists() || archivo.length() == 0) { // Comprobar si el archivo está vacío o no existe
            return carritos;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                Carrito carrito = textoACarrito(linea);
                if (carrito != null) {
                    carritos.add(carrito);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al cargar carritos desde el archivo: " + e.getMessage());
        }
        return carritos;
    }

    /**
     * Convierte un objeto {@link Carrito} a una cadena de texto para su almacenamiento.
     * El formato de la cadena es:
     * {@code codigoCarrito;fechaCreacion;usernameUsuario;codigoProducto1,cantidad1|codigoProducto2,cantidad2|...}
     * Los caracteres delimitadores dentro de los datos (username) se escapan.
     *
     * @param carrito El objeto {@link Carrito} a convertir.
     * @return Una {@link String} que representa el carrito en formato de texto.
     */
    private String carritoATexto(Carrito carrito) {
        StringBuilder sb = new StringBuilder();
        sb.append(carrito.getCodigo()).append(";");
        sb.append(dateFormat.format(carrito.getFechaCreacion().getTime())).append(";");
        // Asegurarse de que el usuario no sea null antes de obtener el username
        sb.append(escapeString(carrito.getUsuario() != null ? carrito.getUsuario().getUsername() : "N/A")).append(";");

        List<String> itemsTexto = new ArrayList<>();
        for (ItemCarrito item : carrito.obtenerItems()) {
            itemsTexto.add(item.getProducto().getCodigo() + "," + item.getCantidad());
        }
        // Une los ítems con el delimitador específico de ítems
        sb.append(String.join(DELIMITADOR_ITEMS.replace("\\", ""), itemsTexto)); // Remueve el escape para join

        return sb.toString();
    }

    /**
     * Convierte una cadena de texto leída del archivo a un objeto {@link Carrito}.
     * Descompone la línea en sus partes principales y luego en los detalles de sus ítems.
     * Resuelve las dependencias de {@link Usuario} y {@link Producto} utilizando los DAOs proporcionados.
     * Maneja errores de formato y de datos faltantes (ej. usuario o producto no encontrados).
     *
     * @param linea La cadena de texto que representa un carrito.
     * @return Un objeto {@link Carrito} si la conversión es exitosa, o {@code null} si hay un error de formato o datos.
     */
    private Carrito textoACarrito(String linea) {
        // Usa el delimitador principal para dividir la línea
        String[] partes = linea.split(DELIMITADOR_PRINCIPAL);
        // Se esperan al menos 3 partes (codigo;fecha;username). Los ítems son la cuarta parte opcional.
        if (partes.length >= 3) {
            try {
                int codigoCarrito = Integer.parseInt(partes[0]);
                Date fecha = dateFormat.parse(partes[1]);
                String username = unescapeString(partes[2]);

                // Busca el usuario asociado al carrito utilizando el UsuarioDAO
                Usuario usuario = usuarioDAO.buscarPorUsername(username);
                if (usuario == null) {
                    System.err.println("Usuario no encontrado para carrito (saltando línea): " + username + " en línea: " + linea);
                    return null; // No se puede crear el carrito sin un usuario válido
                }

                Carrito carrito = new Carrito();
                carrito.setCodigo(codigoCarrito);
                GregorianCalendar cal = new GregorianCalendar();
                cal.setTime(fecha);
                carrito.setFechaCreacion(cal);
                carrito.setUsuario(usuario);

                // Si hay una cuarta parte (ítems) y no está vacía
                if (partes.length > 3 && !partes[3].isEmpty()) {
                    // Divide la cadena de ítems por el delimitador de ítems
                    String[] itemsStr = partes[3].split(DELIMITADOR_ITEMS.replace("\\", "")); // Remueve el escape para split
                    for (String itemStr : itemsStr) {
                        // Divide cada ítem por el delimitador de detalle (código,cantidad)
                        String[] itemParts = itemStr.split(DELIMITADOR_ITEM_DETALLE.replace("\\", "")); // Remueve el escape para split
                        if (itemParts.length == 2) {
                            int codProducto = Integer.parseInt(itemParts[0]);
                            int cantidad = Integer.parseInt(itemParts[1]);
                            // Busca el producto asociado al ítem utilizando el ProductoDAO
                            Producto p = productoDAO.buscarPorCodigo(codProducto);
                            if (p != null) {
                                carrito.agregarProducto(p, cantidad);
                            } else {
                                System.err.println("Producto " + codProducto + " no encontrado para carrito " + codigoCarrito + " (ítem omitido)");
                            }
                        }
                    }
                }
                return carrito;
            } catch (NumberFormatException | ParseException e) {
                System.err.println("Error de formato en línea de carrito: " + linea + " - " + e.getMessage());
            }
        }
        return null; // Retorna null si la línea no tiene el formato esperado
    }

    /**
     * Escapa caracteres que son delimitadores internos en la cadena de texto,
     * para que no interfieran con la estructura de la línea al guardarla.
     * Reemplaza ';', '|' y ',' con sus respectivas entidades HTML para evitar conflictos.
     *
     * @param s La cadena de texto a escapar.
     * @return La cadena de texto con los delimitadores escapados.
     */
    private String escapeString(String s) {
        return s.replace(";", "&#59;").replace("|", "&#124;").replace(",", "&#44;");
    }

    /**
     * Desescapa caracteres que fueron escapados previamente en una cadena de texto,
     * restaurando los delimitadores originales.
     * Reemplaza las entidades HTML de ';', '|' y ',' con sus caracteres reales.
     *
     * @param s La cadena de texto a desescapar.
     * @return La cadena de texto con los delimitadores restaurados.
     */
    private String unescapeString(String s) {
        return s.replace("&#59;", ";").replace("&#124;", "|").replace("&#44;", ",");
    }

    /**
     * Crea y persiste un nuevo objeto {@link Carrito} en el archivo de texto.
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
     * Busca y recupera un objeto {@link Carrito} por su código en el archivo de texto.
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
     * Actualiza la información de un objeto {@link Carrito} existente en el archivo de texto.
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
                guardarTodosLosCarritos(carritos); // Guarda la lista después de la actualización
                return; // Sale una vez que se ha encontrado y actualizado el carrito
            }
        }
        // Si el carrito no se encontró, no se realiza ninguna acción de guardado aquí,
        // asumiendo que no se debe persistir si no hubo un objeto a actualizar.
    }

    /**
     * Elimina un carrito del archivo de texto utilizando su código.
     * Carga todos los carritos, remueve el que coincide con el código y luego guarda la lista actualizada.
     *
     * @param codigo El código del carrito a eliminar.
     */
    @Override
    public void eliminar(int codigo) {
        List<Carrito> carritos = cargarTodosLosCarritos();
        // removeIf elimina todos los elementos que cumplen la condición.
        // Si el código es único, eliminará solo uno.
        boolean removido = carritos.removeIf(c -> c.getCodigo() == codigo);
        if (removido) {
            guardarTodosLosCarritos(carritos);
        } else {
            System.err.println("Carrito con código " + codigo + " no encontrado para eliminar.");
        }
    }

    /**
     * Devuelve una lista de todos los carritos actualmente almacenados en el archivo de texto.
     *
     * @return Una {@link List} de todos los objetos {@link Carrito} cargados desde el archivo.
     */
    @Override
    public List<Carrito> listarTodos() {
        return cargarTodosLosCarritos();
    }
}