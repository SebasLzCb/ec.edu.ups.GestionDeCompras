package ec.edu.ups.dao;

import ec.edu.ups.dao.impl.ArchBin.CarritoDAOArchivoBinario;
import ec.edu.ups.dao.impl.CarritoDAOMemoria;
import ec.edu.ups.dao.impl.ArchTxt.CarritoDAOArchivoTexto;
import ec.edu.ups.dao.impl.ArchBin.ProductoDAOArchivoBinario;
import ec.edu.ups.dao.impl.ProductoDAOMemoria;
import ec.edu.ups.dao.impl.ArchTxt.ProductoDAOArchivoTexto;
import ec.edu.ups.dao.impl.ArchBin.RecuperacionDAOArchivoBinario;
import ec.edu.ups.dao.impl.RecuperacionDAOMemoria;
import ec.edu.ups.dao.impl.ArchTxt.RecuperacionDAOArchivoTexto;
import ec.edu.ups.dao.impl.ArchBin.UsuarioDAOArchivoBinario;
import ec.edu.ups.dao.impl.UsuarioDAOMemoria;
import ec.edu.ups.dao.impl.ArchTxt.UsuarioDAOArchivoTexto;
import ec.edu.ups.modelo.Rol;
import ec.edu.ups.modelo.Usuario;
import ec.edu.ups.util.MensajeInternacionalizacionHandler;

import java.io.File;
import java.time.LocalDate;

/**
 * La clase {@code DAOManager} es responsable de gestionar y proporcionar las instancias
 * de los objetos DAO (Data Access Object) para las diferentes entidades del sistema
 * (Usuario, Producto, Carrito, Recuperacion).
 * Permite configurar y cambiar dinámicamente el tipo de almacenamiento de datos
 * (en memoria, archivos de texto o archivos binarios) en tiempo de ejecución.
 *
 * <p>Al inicializar los DAOs, también se asegura de que exista un usuario administrador
 * predefinido en la fuente de datos seleccionada.</p>
 */
public class DAOManager {

    private UsuarioDAO usuarioDAO;
    private ProductoDAO productoDAO;
    private CarritoDAO carritoDAO;
    private RecuperacionDAO recuperacionDAO;
    private final MensajeInternacionalizacionHandler mensajeHandler;

    /**
     * Constructor de la clase {@code DAOManager}.
     * Inicializa el manejador de mensajes internacionalizados y configura
     * los DAOs por defecto para usar almacenamiento en memoria.
     *
     * @param mensajeHandler Manejador para obtener mensajes internacionalizados,
     * necesario para obtener las claves de los tipos de almacenamiento.
     */
    public DAOManager(MensajeInternacionalizacionHandler mensajeHandler) {
        this.mensajeHandler = mensajeHandler;
        // Inicializa los DAOs por defecto en memoria al crear el gestor
        initializeDAOs(mensajeHandler.get("login.storage.memory"), null);
    }

    /**
     * Inicializa las implementaciones de los DAOs (UsuarioDAO, ProductoDAO, CarritoDAO, RecuperacionDAO)
     * basándose en el tipo de almacenamiento especificado.
     * <p>
     * Los tipos de almacenamiento soportados son:
     * <ul>
     * <li>Memoria ({@code "login.storage.memory"})</li>
     * <li>Archivos de Texto ({@code "login.storage.file_system"})</li>
     * <li>Archivos Binarios ({@code "login.storage.binary"})</li>
     * </ul>
     * Si se selecciona un tipo de archivo, se construye la ruta base y se crean los archivos
     * si no existen.
     * </p>
     * También se asegura de que el usuario "admin" exista en el sistema, creándolo si es necesario.
     *
     * @param storageTypeKey La clave internacionalizada que representa el tipo de almacenamiento deseado
     * (ej. "Memoria", "Archivos de texto", "Archivos binarios").
     * @param baseFilePath   La ruta base donde se almacenarán los archivos de datos si el tipo de almacenamiento
     * es de archivos. Puede ser {@code null} o vacío para usar una ruta por defecto ("data/").
     */
    public void initializeDAOs(String storageTypeKey, String baseFilePath) {
        String usuariosFilePath = null;
        String productosFilePath = null;
        String carritosFilePath = null;
        String recuperacionRespuestasFilePath = null;
        String recuperacionPreguntasFilePath = null;

        // Si el tipo de almacenamiento no es memoria, se preparan las rutas de los archivos
        if (!storageTypeKey.equals(mensajeHandler.get("login.storage.memory"))) {
            if (baseFilePath == null || baseFilePath.isEmpty()) {
                baseFilePath = "data" + File.separator; // Ruta por defecto
            } else if (!baseFilePath.endsWith(File.separator)) {
                baseFilePath += File.separator; // Asegura que termine con el separador de directorio
            }

            // Se crea el directorio si no existe
            File dataDir = new File(baseFilePath);
            if (!dataDir.exists()) {
                dataDir.mkdirs(); // Crea el directorio y los padres si no existen
            }

            String fileExtension = storageTypeKey.equals(mensajeHandler.get("login.storage.file_system")) ? ".txt" : ".bin";
            usuariosFilePath = baseFilePath + "usuarios" + fileExtension;
            productosFilePath = baseFilePath + "productos" + fileExtension;
            carritosFilePath = baseFilePath + "carritos" + fileExtension;
            recuperacionRespuestasFilePath = baseFilePath + "recuperacion_respuestas" + fileExtension;
            recuperacionPreguntasFilePath = baseFilePath + "recuperacion_preguntas" + fileExtension;
        }

        // Instanciación de los DAOs según el tipo de almacenamiento
        if (storageTypeKey.equals(mensajeHandler.get("login.storage.memory"))) {
            this.usuarioDAO = new UsuarioDAOMemoria();
            this.productoDAO = new ProductoDAOMemoria();
            this.carritoDAO = new CarritoDAOMemoria();
            this.recuperacionDAO = new RecuperacionDAOMemoria(this.usuarioDAO); // Dependencia de UsuarioDAO
        } else if (storageTypeKey.equals(mensajeHandler.get("login.storage.file_system"))) {
            this.usuarioDAO = new UsuarioDAOArchivoTexto(usuariosFilePath);
            this.productoDAO = new ProductoDAOArchivoTexto(productosFilePath);
            // Las DAOs de Carrito y Recuperacion necesitan otras DAOs para buscar entidades relacionadas
            this.carritoDAO = new CarritoDAOArchivoTexto(carritosFilePath, this.productoDAO, this.usuarioDAO);
            this.recuperacionDAO = new RecuperacionDAOArchivoTexto(recuperacionPreguntasFilePath, recuperacionRespuestasFilePath, this.usuarioDAO);
        } else if (storageTypeKey.equals(mensajeHandler.get("login.storage.binary"))) {
            this.usuarioDAO = new UsuarioDAOArchivoBinario(usuariosFilePath);
            this.productoDAO = new ProductoDAOArchivoBinario(productosFilePath);
            this.carritoDAO = new CarritoDAOArchivoBinario(carritosFilePath, this.productoDAO, this.usuarioDAO);
            this.recuperacionDAO = new RecuperacionDAOArchivoBinario(recuperacionPreguntasFilePath, recuperacionRespuestasFilePath, this.usuarioDAO);
        } else {
            // Manejo de tipo de almacenamiento desconocido, se recurre a memoria por seguridad
            System.err.println("Tipo de almacenamiento desconocido: " + storageTypeKey + ". Usando memoria.");
            initializeDAOs(mensajeHandler.get("login.storage.memory"), null);
        }

        // Asegurar que el usuario 'admin' exista
        // Se busca el usuario admin en el DAO recién inicializado
        if (usuarioDAO.buscarPorUsername("admin") == null) {
            Usuario admin = new Usuario("admin", "12345", Rol.ADMINISTRADOR,
                    "Administrador General", LocalDate.of(1990,1,1),
                    "admin@ejemplo.com", "0999999999");
            usuarioDAO.crear(admin);

            // Solo se guardan las respuestas de recuperación del admin si el almacenamiento es persistente
            if (storageTypeKey.equals(mensajeHandler.get("login.storage.file_system")) ||
                    storageTypeKey.equals(mensajeHandler.get("login.storage.binary"))) {
                recuperacionDAO.guardarRespuestas(admin,
                        new String[]{"¿Cuál es tu equipo favorito?", "¿Nombre de tu primera mascota?", "¿Ciudad donde naciste?"},
                        new String[]{"Ecuador", "Firulais", "Quito"}
                );
            }
        }
    }

    /**
     * Obtiene la instancia actual del {@link UsuarioDAO}.
     *
     * @return La implementación de {@link UsuarioDAO} actualmente configurada.
     */
    public UsuarioDAO getUsuarioDAO() {
        return usuarioDAO;
    }

    /**
     * Obtiene la instancia actual del {@link ProductoDAO}.
     *
     * @return La implementación de {@link ProductoDAO} actualmente configurada.
     */
    public ProductoDAO getProductoDAO() {
        return productoDAO;
    }

    /**
     * Obtiene la instancia actual del {@link CarritoDAO}.
     *
     * @return La implementación de {@link CarritoDAO} actualmente configurada.
     */
    public CarritoDAO getCarritoDAO() {
        return carritoDAO;
    }

    /**
     * Obtiene la instancia actual del {@link RecuperacionDAO}.
     *
     * @return La implementación de {@link RecuperacionDAO} actualmente configurada.
     */
    public RecuperacionDAO getRecuperacionDAO() {
        return recuperacionDAO;
    }
}