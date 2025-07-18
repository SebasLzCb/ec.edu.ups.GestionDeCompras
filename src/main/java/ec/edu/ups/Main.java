package ec.edu.ups;

import ec.edu.ups.controlador.CarritoController;
import ec.edu.ups.controlador.ProductoController;
import ec.edu.ups.controlador.RecuperacionController;
import ec.edu.ups.controlador.UsuarioController;
import ec.edu.ups.dao.CarritoDAO;
import ec.edu.ups.dao.DAOManager;
import ec.edu.ups.dao.ProductoDAO;
import ec.edu.ups.dao.RecuperacionDAO;
import ec.edu.ups.dao.UsuarioDAO;
import ec.edu.ups.modelo.Rol;
import ec.edu.ups.modelo.Usuario;
import ec.edu.ups.util.MensajeInternacionalizacionHandler;
import ec.edu.ups.vista.InicioDeSesion.LoginView;
import ec.edu.ups.vista.InicioDeSesion.RecuperarContraseñaView;
import ec.edu.ups.vista.InicioDeSesion.RegistroView;
import ec.edu.ups.vista.Principal;
import ec.edu.ups.vista.Producto.ProductoAñadirView;
import ec.edu.ups.vista.Producto.ProductoElimView;
import ec.edu.ups.vista.Producto.ProductoListaView;
import ec.edu.ups.vista.Producto.ProductoModView;
import ec.edu.ups.vista.Carrito.CarritoAñadirView;
import ec.edu.ups.vista.Carrito.CarritoDetallesView;
import ec.edu.ups.vista.Carrito.CarritoElimView;
import ec.edu.ups.vista.Carrito.CarritoListaView;
import ec.edu.ups.vista.Carrito.CarritoModView;
import ec.edu.ups.vista.Usuario.UsuarioElimView;
import ec.edu.ups.vista.Usuario.UsuarioListaView;
import ec.edu.ups.vista.Usuario.UsuarioModView;
import ec.edu.ups.vista.Usuario.UsuarioRegistroView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.MessageFormat;

/**
 * La clase {@code Main} es el punto de entrada principal de la aplicación
 * "Sistema de Carrito de Compras En Línea".
 * Se encarga de inicializar todos los componentes del sistema (modelos, vistas, controladores, DAOs),
 * configurar la apariencia de la interfaz de usuario y gestionar el flujo de inicio de sesión
 * y la transición a la ventana principal de la aplicación.
 *
 * <p>Utiliza el patrón de diseño Inversión de Control (IoC) y Dependency Injection (DI)
 * al inicializar los controladores, pasándoles las dependencias necesarias.
 * También implementa la lógica para la persistencia configurable a través del {@link DAOManager}.</p>
 *
 * @author [Tu Nombre/Equipo]
 * @version 1.0
 * @since 2023-01-01
 */
public class Main {

    /**
     * Establece el Look and Feel de la interfaz de usuario para que coincida con el sistema operativo.
     * Esto proporciona una apariencia nativa a la aplicación Swing.
     */
    private static void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Método principal de la aplicación.
     * Es el punto de inicio desde donde se lanza la interfaz gráfica.
     * <p>
     * Pasos de inicialización:
     * <ol>
     * <li>Configura el Look and Feel.</li>
     * <li>Inicializa el manejador de internacionalización.</li>
     * <li>Crea el {@link DAOManager} para la gestión de la persistencia.</li>
     * <li>Instancia todas las vistas y controladores necesarios.</li>
     * <li>Configura las dependencias entre vistas y controladores.</li>
     * <li>Establece listeners para eventos clave, como el cierre de la ventana de login.</li>
     * <li>Muestra la ventana de login como primera interacción del usuario.</li>
     * </ol>
     * </p>
     *
     * @param args Argumentos de la línea de comandos (no utilizados en esta aplicación).
     */
    public static void main(String[] args) {
        // Asegura que la inicialización de la UI se realice en el Event Dispatch Thread (EDT)
        EventQueue.invokeLater(() -> {
            setLookAndFeel(); // Aplica el Look and Feel del sistema operativo

            // Inicialización del manejador de mensajes internacionalizados (idioma por defecto: español Ecuador)
            MensajeInternacionalizacionHandler mensajeHandler = new MensajeInternacionalizacionHandler("es", "EC");

            // Inicialización del gestor de DAOs, que se encargará de la persistencia configurable
            DAOManager daoManager = new DAOManager(mensajeHandler);

            // -------------------- Inicialización de Vistas y Controladores --------------------

            // Controlador de Recuperación y su vista
            RecuperacionController recuperacionController = new RecuperacionController(daoManager.getRecuperacionDAO(), daoManager.getUsuarioDAO(), mensajeHandler, daoManager);
            LoginView loginView = new LoginView(recuperacionController, mensajeHandler);

            RecuperarContraseñaView recuperarContraseniaView = new RecuperarContraseñaView(mensajeHandler, recuperacionController.obtenerPreguntasLocalizadas());
            recuperacionController.setRecuperarView(recuperarContraseniaView);

            // Vistas de Registro (JFrame y JInternalFrame)
            RegistroView registroFrame = new RegistroView(mensajeHandler); // JFrame de registro (se abre desde Login)
            UsuarioRegistroView registroViewInternal = new UsuarioRegistroView(mensajeHandler); // JInternalFrame de registro (se abre desde Principal para admins)

            // Asignación de vistas a controladores y entre vistas
            loginView.setRegistroFrame(registroFrame); // LoginView necesita conocer el JFrame de registro
            recuperacionController.setLoginView(loginView); // El controlador de recuperación necesita la LoginView para mostrar mensajes

            // Ventana Principal (JFrame MDI)
            Principal principal = new Principal(mensajeHandler);
            loginView.setPrincipal(principal); // LoginView necesita la ventana principal para mostrarla después del login

            // Vistas de gestión de Usuarios (JInternalFrame)
            UsuarioListaView listaView = new UsuarioListaView(mensajeHandler);
            UsuarioModView modView = new UsuarioModView(mensajeHandler);
            UsuarioElimView elimView = new UsuarioElimView(mensajeHandler);

            // Controlador de Usuarios
            UsuarioController usuarioController = new UsuarioController(
                    daoManager.getUsuarioDAO(), loginView, registroViewInternal, listaView, modView, elimView,
                    principal, recuperacionController, mensajeHandler, daoManager
            );
            usuarioController.setRegistroFrame(registroFrame); // El UsuarioController gestionará el JFrame de registro

            // Listener para el cierre de la ventana de Login
            // Esto asegura que la aplicación se inicie correctamente (o se cierre) después del proceso de login.
            loginView.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    Usuario actual = usuarioController.getUsuarioActual();
                    // Si el usuario no es null, significa que el login fue exitoso.
                    if (actual == null) {
                        // Si no hay usuario autenticado, cierra la aplicación
                        System.exit(0);
                    }

                    // -------------------- Inicialización de Vistas y Controladores (Post-Login) --------------------

                    // Vistas de gestión de Productos (JInternalFrame)
                    ProductoAñadirView prodAddView = new ProductoAñadirView(mensajeHandler);
                    ProductoListaView prodListView = new ProductoListaView(mensajeHandler);
                    ProductoModView prodModView = new ProductoModView(mensajeHandler);
                    ProductoElimView prodElimView = new ProductoElimView(daoManager.getProductoDAO(), mensajeHandler);

                    // Controlador de Productos
                    // Se le pasa el ProductoDAO obtenido del DAOManager, garantizando la persistencia seleccionada
                    new ProductoController(daoManager.getProductoDAO(), prodAddView, prodListView, prodModView, prodElimView, mensajeHandler);

                    // Vistas de gestión de Carritos (JInternalFrame)
                    CarritoAñadirView cartAddView = new CarritoAñadirView(mensajeHandler);
                    CarritoListaView cartListView = new CarritoListaView(mensajeHandler);
                    CarritoModView cartModView = new CarritoModView(mensajeHandler);
                    CarritoElimView cartElimView = new CarritoElimView(mensajeHandler);
                    CarritoDetallesView cartDetView = new CarritoDetallesView(mensajeHandler);

                    // Controlador de Carritos
                    // Se le pasan los DAOs de Carrito y Producto obtenidos del DAOManager
                    new CarritoController(
                            daoManager.getCarritoDAO(), daoManager.getProductoDAO(), cartAddView, cartListView, cartModView,
                            cartElimView, cartDetView, actual, mensajeHandler
                    );

                    // -------------------- Configuración Final de la Ventana Principal --------------------

                    principal.setVisible(true); // Muestra la ventana principal
                    // Muestra un mensaje de bienvenida al usuario autenticado
                    String plantilla = mensajeHandler.get("info.bienvenida");
                    String saludo = MessageFormat.format(plantilla, actual.getUsername());
                    principal.mostrarMensaje(saludo);

                    // Deshabilita menús de administrador si el usuario es de rol normal
                    if (actual.getRol() == Rol.USUARIO) {
                        principal.deshabilitarMenusAdministrador();
                    }

                    // Configuración de ActionListeners para los elementos del menú principal
                    // Enlaces entre el menú principal y la apertura de las vistas internas
                    principal.getMenuItemCrearProducto().addActionListener(ev -> mostrarVentana(principal, prodAddView));
                    principal.getMenuItemBuscarProducto().addActionListener(ev -> mostrarVentana(principal, prodListView));
                    principal.getMenuItemActualizarProducto().addActionListener(ev -> mostrarVentana(principal, prodModView));
                    principal.getMenuItemEliminarProducto().addActionListener(ev -> mostrarVentana(principal, prodElimView));

                    principal.getMenuItemCrearCarrito().addActionListener(ev -> mostrarVentana(principal, cartAddView));
                    principal.getMenuItemBuscarCarrito().addActionListener(ev -> mostrarVentana(principal, cartListView));
                    principal.getMenuItemModificarCarrito().addActionListener(ev -> mostrarVentana(principal, cartModView));
                    principal.getMenuItemEliminarCarrito().addActionListener(ev -> mostrarVentana(principal, cartElimView));
                    principal.getMenuItemVerDetallesCarrito().addActionListener(ev -> mostrarVentana(principal, cartDetView));

                    // Configuración de ActionListeners para cambiar el idioma de la aplicación
                    principal.getMenuItemIdiomaEspanol().addActionListener(ev -> principal.cambiarIdioma("es", "EC"));
                    principal.getMenuItemIdiomaIngles().addActionListener(ev -> principal.cambiarIdioma("en", "US"));
                    principal.getMenuItemIdiomaFrances().addActionListener(ev -> principal.cambiarIdioma("fr", "FR"));
                }
            });

            // -------------------- Inicio de la Aplicación --------------------
            loginView.setVisible(true); // Hace visible la ventana de login para iniciar la interacción
        });
    }

    /**
     * Muestra una ventana interna ({@code JInternalFrame}) dentro del {@link JDesktopPane} de la ventana principal.
     * Si la ventana ya está visible, la trae al frente y la selecciona. Si no está en el escritorio, la añade.
     *
     * @param principal La instancia de la ventana {@link Principal} que contiene el escritorio.
     * @param ventana La {@link JInternalFrame} a mostrar.
     */
    private static void mostrarVentana(Principal principal, JInternalFrame ventana) {
        if (!ventana.isVisible()) {
            // Si la ventana interna aún no ha sido añadida al desktop pane, se añade
            if (ventana.getParent() == null) {
                principal.getDesktopPane().add(ventana);
            }
            ventana.setVisible(true); // Hace visible la ventana
        }
        try {
            ventana.setSelected(true); // Intenta seleccionar la ventana para que obtenga el foco
        } catch (java.beans.PropertyVetoException e) {
            // Maneja PropertyVetoException, por ejemplo, si la ventana no puede ser seleccionada
            // Ignorado en este caso, ya que no es crítico para la funcionalidad principal.
        }
        ventana.toFront(); // Trae la ventana al frente de todas las demás ventanas internas
    }
}