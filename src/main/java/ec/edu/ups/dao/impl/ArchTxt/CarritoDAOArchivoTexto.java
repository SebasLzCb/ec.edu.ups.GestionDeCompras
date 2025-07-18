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

public class CarritoDAOArchivoTexto implements CarritoDAO {

    private String rutaArchivo;
    private ProductoDAO productoDAO;
    private UsuarioDAO usuarioDAO;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final String DELIMITADOR_PRINCIPAL = "\\;"; // Para partes principales del carrito
    private static final String DELIMITADOR_ITEMS = "\\|"; // Para separar ítems del carrito
    private static final String DELIMITADOR_ITEM_DETALLE = "\\,"; // Para separar código y cantidad de un ítem

    public CarritoDAOArchivoTexto(String rutaArchivo, ProductoDAO productoDAO, UsuarioDAO usuarioDAO) {
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
                System.err.println("Error al crear el archivo de carritos: " + e.getMessage());
            }
        }
    }

    private void guardarTodosLosCarritos(List<Carrito> carritos) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(rutaArchivo))) {
            for (Carrito carrito : carritos) {
                writer.write(carritoATexto(carrito));
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error al guardar carritos en el archivo: " + e.getMessage());
        }
    }

    private List<Carrito> cargarTodosLosCarritos() {
        List<Carrito> carritos = new ArrayList<>();
        File archivo = new File(rutaArchivo);
        if (!archivo.exists() || archivo.length() == 0) {
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

    // Formato: codigoCarrito;fechaCreacion;usernameUsuario;codigoProducto1,cantidad1|codigoProducto2,cantidad2|...
    private String carritoATexto(Carrito carrito) {
        StringBuilder sb = new StringBuilder();
        sb.append(carrito.getCodigo()).append(";");
        sb.append(dateFormat.format(carrito.getFechaCreacion().getTime())).append(";");
        sb.append(escapeString(carrito.getUsuario().getUsername())).append(";");

        List<String> itemsTexto = new ArrayList<>();
        for (ItemCarrito item : carrito.obtenerItems()) {
            itemsTexto.add(item.getProducto().getCodigo() + "," + item.getCantidad());
        }
        sb.append(String.join(DELIMITADOR_ITEMS, itemsTexto));

        return sb.toString();
    }

    private Carrito textoACarrito(String linea) {
        String[] partes = linea.split(DELIMITADOR_PRINCIPAL);
        if (partes.length >= 3) { // Minimo codigo;fecha;username
            try {
                int codigoCarrito = Integer.parseInt(partes[0]);
                Date fecha = dateFormat.parse(partes[1]);
                String username = unescapeString(partes[2]);

                Usuario usuario = usuarioDAO.buscarPorUsername(username);
                if (usuario == null) {
                    System.err.println("Usuario no encontrado para carrito (saltando): " + username);
                    return null;
                }

                Carrito carrito = new Carrito();
                carrito.setCodigo(codigoCarrito);
                GregorianCalendar cal = new GregorianCalendar();
                cal.setTime(fecha);
                carrito.setFechaCreacion(cal);
                carrito.setUsuario(usuario);

                if (partes.length > 3 && !partes[3].isEmpty()) { // Si hay ítems
                    String[] itemsStr = partes[3].split(DELIMITADOR_ITEMS);
                    for (String itemStr : itemsStr) {
                        String[] itemParts = itemStr.split(DELIMITADOR_ITEM_DETALLE);
                        if (itemParts.length == 2) {
                            int codProducto = Integer.parseInt(itemParts[0]);
                            int cantidad = Integer.parseInt(itemParts[1]);
                            Producto p = productoDAO.buscarPorCodigo(codProducto);
                            if (p != null) {
                                carrito.agregarProducto(p, cantidad);
                            } else {
                                System.err.println("Producto " + codProducto + " no encontrado para carrito " + codigoCarrito);
                            }
                        }
                    }
                }
                return carrito;
            } catch (NumberFormatException | ParseException e) {
                System.err.println("Error de formato en línea de carrito: " + linea + " - " + e.getMessage());
            }
        }
        return null;
    }

    private String escapeString(String s) {
        return s.replace(";", "&#59;").replace("|", "&#124;").replace(",", "&#44;");
    }

    private String unescapeString(String s) {
        return s.replace("&#59;", ";").replace("&#124;", "|").replace("&#44;", ",");
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