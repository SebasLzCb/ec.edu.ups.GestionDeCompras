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

public class DAOManager {

    private UsuarioDAO usuarioDAO;
    private ProductoDAO productoDAO;
    private CarritoDAO carritoDAO;
    private RecuperacionDAO recuperacionDAO;
    private final MensajeInternacionalizacionHandler mensajeHandler;

    public DAOManager(MensajeInternacionalizacionHandler mensajeHandler) {
        this.mensajeHandler = mensajeHandler;
        initializeDAOs(mensajeHandler.get("login.storage.memory"), null);
    }

    public void initializeDAOs(String storageTypeKey, String baseFilePath) {
        String usuariosFilePath = null;
        String productosFilePath = null;
        String carritosFilePath = null;
        String recuperacionRespuestasFilePath = null;
        String recuperacionPreguntasFilePath = null;

        if (!storageTypeKey.equals(mensajeHandler.get("login.storage.memory"))) {
            if (baseFilePath == null || baseFilePath.isEmpty()) {
                baseFilePath = "data" + File.separator;
            } else if (!baseFilePath.endsWith(File.separator)) {
                baseFilePath += File.separator;
            }

            String fileExtension = storageTypeKey.equals(mensajeHandler.get("login.storage.file_system")) ? ".txt" : ".bin";
            usuariosFilePath = baseFilePath + "usuarios" + fileExtension;
            productosFilePath = baseFilePath + "productos" + fileExtension;
            carritosFilePath = baseFilePath + "carritos" + fileExtension;
            recuperacionRespuestasFilePath = baseFilePath + "recuperacion_respuestas" + fileExtension;
            recuperacionPreguntasFilePath = baseFilePath + "recuperacion_preguntas" + fileExtension;
        }

        if (storageTypeKey.equals(mensajeHandler.get("login.storage.memory"))) {
            this.usuarioDAO = new UsuarioDAOMemoria();
            this.productoDAO = new ProductoDAOMemoria();
            this.carritoDAO = new CarritoDAOMemoria();
            this.recuperacionDAO = new RecuperacionDAOMemoria(this.usuarioDAO);
        } else if (storageTypeKey.equals(mensajeHandler.get("login.storage.file_system"))) {
            this.usuarioDAO = new UsuarioDAOArchivoTexto(usuariosFilePath);
            this.productoDAO = new ProductoDAOArchivoTexto(productosFilePath);
            this.carritoDAO = new CarritoDAOArchivoTexto(carritosFilePath, this.productoDAO, this.usuarioDAO);
            this.recuperacionDAO = new RecuperacionDAOArchivoTexto(recuperacionPreguntasFilePath, recuperacionRespuestasFilePath, this.usuarioDAO);
        } else if (storageTypeKey.equals(mensajeHandler.get("login.storage.binary"))) {
            this.usuarioDAO = new UsuarioDAOArchivoBinario(usuariosFilePath);
            this.productoDAO = new ProductoDAOArchivoBinario(productosFilePath);
            this.carritoDAO = new CarritoDAOArchivoBinario(carritosFilePath, this.productoDAO, this.usuarioDAO);
            this.recuperacionDAO = new RecuperacionDAOArchivoBinario(recuperacionPreguntasFilePath, recuperacionRespuestasFilePath, this.usuarioDAO);
        } else {
            System.err.println("Tipo de almacenamiento desconocido: " + storageTypeKey + ". Usando memoria.");
            initializeDAOs(mensajeHandler.get("login.storage.memory"), null);
        }

        if (usuarioDAO.buscarPorUsername("admin") == null) {
            Usuario admin = new Usuario("admin", "12345", Rol.ADMINISTRADOR,
                    "Administrador General", LocalDate.of(1990,1,1),
                    "admin@ejemplo.com", "0999999999");
            usuarioDAO.crear(admin);

            if (storageTypeKey.equals(mensajeHandler.get("login.storage.file_system")) ||
                    storageTypeKey.equals(mensajeHandler.get("login.storage.binary"))) {
                recuperacionDAO.guardarRespuestas(admin,
                        new String[]{"¿Cuál es tu equipo favorito?", "¿Nombre de tu primera mascota?", "¿Ciudad donde naciste?"},
                        new String[]{"Ecuador", "Firulais", "Quito"}
                );
            }
        }
    }

    public UsuarioDAO getUsuarioDAO() {
        return usuarioDAO;
    }

    public ProductoDAO getProductoDAO() {
        return productoDAO;
    }

    public CarritoDAO getCarritoDAO() {
        return carritoDAO;
    }

    public RecuperacionDAO getRecuperacionDAO() {
        return recuperacionDAO;
    }
}