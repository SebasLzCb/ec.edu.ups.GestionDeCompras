package ec.edu.ups.vista;

import ec.edu.ups.util.MensajeInternacionalizacionHandler;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;

/**
 * La clase {@code Principal} representa la ventana principal de la aplicación,
 * funcionando como una interfaz de múltiples documentos (MDI) mediante el uso de un {@link MiJDesktopPane}.
 * Contiene un menú superior que permite al usuario acceder a diversas funcionalidades
 * como la gestión de productos, carritos, usuarios e idiomas.
 *
 * <p>Esta ventana es el punto central después del inicio de sesión,
 * desde donde se gestiona la apertura y visualización de las vistas internas.</p>
 *
 * @author [Tu Nombre/Equipo]
 * @version 1.0
 * @since 2023-01-01
 */
public class Principal extends JFrame {

    private MensajeInternacionalizacionHandler mensajeHandler;

    private JMenuBar menuBar;
    private JMenu menuProducto, menuCarrito, menuUsuario, menuIdioma;
    private JMenuItem
            menuItemCrearProducto,
            menuItemEliminarProducto,
            menuItemActualizarProducto,
            menuItemBuscarProducto,
            menuItemCrearCarrito,
            menuItemBuscarCarrito,
            menuItemModificarCarrito,
            menuItemVerDetallesCarrito,
            menuItemEliminarCarrito,
            menuItemRegistrarUsuario,
            menuItemListarUsuarios,
            menuItemModificarUsuario,
            menuItemEliminarUsuario,
            menuItemCerrarSesion,
            menuItemIdiomaEspanol,
            menuItemIdiomaIngles,
            menuItemIdiomaFrances;
    private MiJDesktopPane desktopPane;

    /**
     * Constructor de la clase {@code Principal}.
     * Inicializa la ventana principal, establece el manejador de mensajes para
     * la internacionalización, configura los componentes de la UI y maximiza la ventana.
     *
     * @param mensajeHandler El manejador de mensajes para la internacionalización de la UI.
     */
    public Principal(MensajeInternacionalizacionHandler mensajeHandler) {
        this.mensajeHandler = mensajeHandler;
        initComponents();
        setTitle(mensajeHandler.get("app.titulo")); // Establece el título de la ventana desde los mensajes internacionalizados
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximiza la ventana al iniciar
        setDefaultCloseOperation(EXIT_ON_CLOSE); // Cierra la aplicación al cerrar la ventana principal
    }

    /**
     * Inicializa y configura todos los componentes de la interfaz de usuario de la ventana principal,
     * incluyendo el JDesktopPane y la barra de menú con sus menús y elementos de menú.
     */
    private void initComponents() {
        desktopPane = new MiJDesktopPane(); // Crea una instancia de tu JDesktopPane personalizado
        setContentPane(desktopPane); // Establece el desktopPane como el contenido principal de la ventana

        menuBar = new JMenuBar(); // Crea la barra de menú

        // Menú "Producto" y sus ítems
        menuProducto = new JMenu(mensajeHandler.get("menu.producto"));
        menuItemCrearProducto    = new JMenuItem(mensajeHandler.get("producto.crear"));
        menuItemEliminarProducto = new JMenuItem(mensajeHandler.get("producto.eliminar"));
        menuItemActualizarProducto = new JMenuItem(mensajeHandler.get("producto.actualizar"));
        menuItemBuscarProducto   = new JMenuItem(mensajeHandler.get("producto.buscar"));
        menuProducto.add(menuItemCrearProducto);
        menuProducto.add(menuItemEliminarProducto);
        menuProducto.add(menuItemActualizarProducto);
        menuProducto.add(menuItemBuscarProducto);
        menuBar.add(menuProducto); // Añade el menú Producto a la barra de menú

        // Menú "Carrito" y sus ítems
        menuCarrito = new JMenu(mensajeHandler.get("menu.carrito"));
        menuItemCrearCarrito       = new JMenuItem(mensajeHandler.get("carrito.crear"));
        menuItemBuscarCarrito      = new JMenuItem(mensajeHandler.get("carrito.buscar"));
        menuItemModificarCarrito   = new JMenuItem(mensajeHandler.get("carrito.modificar"));
        menuItemVerDetallesCarrito = new JMenuItem(mensajeHandler.get("carrito.view.listar.detalles"));
        menuItemEliminarCarrito    = new JMenuItem(mensajeHandler.get("carrito.eliminar"));
        menuCarrito.add(menuItemCrearCarrito);
        menuCarrito.add(menuItemBuscarCarrito);
        menuCarrito.add(menuItemModificarCarrito);
        menuCarrito.add(menuItemVerDetallesCarrito);
        menuCarrito.add(menuItemEliminarCarrito);
        menuBar.add(menuCarrito); // Añade el menú Carrito a la barra de menú

        // Menú "Usuario" y sus ítems
        menuUsuario = new JMenu(mensajeHandler.get("menu.usuario"));
        menuItemRegistrarUsuario = new JMenuItem(mensajeHandler.get("usuario.registrar"));
        menuItemListarUsuarios   = new JMenuItem(mensajeHandler.get("usuario.listar"));
        menuItemModificarUsuario = new JMenuItem(mensajeHandler.get("usuario.modificar"));
        menuItemEliminarUsuario  = new JMenuItem(mensajeHandler.get("usuario.eliminar"));
        menuItemCerrarSesion     = new JMenuItem(mensajeHandler.get("usuario.cerrarSesion"));
        menuUsuario.add(menuItemRegistrarUsuario);
        menuUsuario.add(menuItemListarUsuarios);
        menuUsuario.add(menuItemModificarUsuario);
        menuUsuario.add(menuItemEliminarUsuario);
        menuUsuario.addSeparator(); // Añade un separador en el menú
        menuUsuario.add(menuItemCerrarSesion);
        menuBar.add(menuUsuario); // Añade el menú Usuario a la barra de menú

        // Menú "Idioma" y sus ítems
        menuIdioma = new JMenu(mensajeHandler.get("menu.idioma"));
        menuItemIdiomaEspanol = new JMenuItem(mensajeHandler.get("menu.idioma.es"));
        menuItemIdiomaIngles  = new JMenuItem(mensajeHandler.get("menu.idioma.en"));
        menuItemIdiomaFrances = new JMenuItem(mensajeHandler.get("menu.idioma.fr"));
        menuIdioma.add(menuItemIdiomaEspanol);
        menuIdioma.add(menuItemIdiomaIngles);
        menuIdioma.add(menuItemIdiomaFrances);
        menuBar.add(menuIdioma); // Añade el menú Idioma a la barra de menú

        setJMenuBar(menuBar); // Establece la barra de menú en la ventana principal
    }

    /**
     * Cambia el idioma de la interfaz de usuario de la aplicación.
     * Actualiza el {@link MensajeInternacionalizacionHandler} con el nuevo lenguaje y país,
     * y luego actualiza los textos de todos los componentes de la barra de menú.
     * Además, invoca el método `actualizarIdioma()` en todas las {@link JInternalFrame}
     * actualmente abiertas en el {@link JDesktopPane} para que también actualicen sus textos.
     *
     * @param lang El código del lenguaje (ej. "es", "en", "fr").
     * @param country El código del país (ej. "EC", "US", "FR").
     */
    public void cambiarIdioma(String lang, String country) {
        mensajeHandler.setLenguaje(lang, country); // Cambia el Locale en el manejador de mensajes

        // Actualiza los textos de los menús principales
        menuProducto.setText(mensaje("menu.producto"));
        menuCarrito.setText(mensaje("menu.carrito"));
        menuUsuario.setText(mensaje("menu.usuario"));
        menuIdioma.setText(mensaje("menu.idioma"));

        // Actualiza los textos de los ítems del menú Producto
        menuItemCrearProducto.setText(mensaje("producto.crear"));
        menuItemEliminarProducto.setText(mensaje("producto.eliminar"));
        menuItemActualizarProducto.setText(mensaje("producto.actualizar"));
        menuItemBuscarProducto.setText(mensaje("producto.buscar"));

        // Actualiza los textos de los ítems del menú Carrito
        menuItemCrearCarrito.setText(mensaje("carrito.crear"));
        menuItemBuscarCarrito.setText(mensaje("carrito.buscar"));
        menuItemModificarCarrito.setText(mensaje("carrito.modificar"));
        menuItemVerDetallesCarrito.setText(mensaje("carrito.view.listar.detalles"));
        menuItemEliminarCarrito.setText(mensaje("carrito.eliminar"));

        // Actualiza los textos de los ítems del menú Usuario
        menuItemRegistrarUsuario.setText(mensaje("usuario.registrar"));
        menuItemListarUsuarios.setText(mensaje("usuario.listar"));
        menuItemModificarUsuario.setText(mensaje("usuario.modificar"));
        menuItemEliminarUsuario.setText(mensaje("usuario.eliminar"));
        menuItemCerrarSesion.setText(mensaje("usuario.cerrarSesion"));

        // Actualiza los textos de los ítems del menú Idioma
        menuItemIdiomaEspanol.setText(mensaje("menu.idioma.es"));
        menuItemIdiomaIngles.setText(mensaje("menu.idioma.en"));
        menuItemIdiomaFrances.setText(mensaje("menu.idioma.fr"));

        // Recorre todas las ventanas internas abiertas e invoca su método 'actualizarIdioma()'
        for (JInternalFrame frame : desktopPane.getAllFrames()) {
            try {
                // Utiliza reflexión para llamar al método, asumiendo que todas las vistas internas lo tienen
                Method m = frame.getClass().getMethod("actualizarIdioma");
                m.invoke(frame);
            } catch (Exception ignored) {
                // Se ignora si una ventana interna no tiene el método, o si ocurre otro error de reflexión
            }
        }
    }

    /**
     * Método auxiliar para obtener un mensaje del {@link MensajeInternacionalizacionHandler}
     * de forma concisa.
     *
     * @param clave La clave del mensaje a obtener.
     * @return La cadena de texto del mensaje.
     */
    private String mensaje(String clave) {
        return mensajeHandler.get(clave);
    }

    /**
     * Muestra un mensaje al usuario en un cuadro de diálogo JOptionPane.
     *
     * @param mensaje La cadena de texto del mensaje a mostrar.
     */
    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }

    /**
     * Deshabilita o habilita ciertos elementos del menú para usuarios con rol "USUARIO".
     * Este método se utiliza para restringir el acceso a funcionalidades de administrador.
     * Por ejemplo, los usuarios normales no pueden crear, eliminar o actualizar productos.
     */
    public void deshabilitarMenusAdministrador() {
        menuItemCrearProducto.setEnabled(false);
        menuItemEliminarProducto.setEnabled(false);
        menuItemActualizarProducto.setEnabled(false);
        menuItemBuscarProducto.setEnabled(false); // Podrías querer dejar el buscar habilitado para usuarios
        menuItemCrearCarrito.setEnabled(true); // Los usuarios pueden crear carritos
        menuItemBuscarCarrito.setEnabled(false);
        menuItemModificarCarrito.setEnabled(false);
        menuItemVerDetallesCarrito.setEnabled(false);
        menuItemEliminarCarrito.setEnabled(false);
        menuItemRegistrarUsuario.setEnabled(false); // Los usuarios normales no pueden registrar otros usuarios
        menuItemListarUsuarios.setEnabled(false); // Los usuarios normales no pueden listar otros usuarios
        menuItemModificarUsuario.setEnabled(true); // Los usuarios sí pueden modificar su propia información (p.ej. contraseña)
        menuItemEliminarUsuario.setEnabled(false);
    }

    /**
     * Obtiene la instancia del {@link MiJDesktopPane} que actúa como escritorio MDI.
     *
     * @return El {@link MiJDesktopPane} de la ventana principal.
     */
    public MiJDesktopPane getDesktopPane() { return desktopPane; }

    /**
     * Obtiene el JMenuItem para la acción "Crear Producto".
     * @return El JMenuItem.
     */
    public JMenuItem getMenuItemCrearProducto() { return menuItemCrearProducto; }

    /**
     * Obtiene el JMenuItem para la acción "Eliminar Producto".
     * @return El JMenuItem.
     */
    public JMenuItem getMenuItemEliminarProducto() { return menuItemEliminarProducto; }

    /**
     * Obtiene el JMenuItem para la acción "Actualizar Producto".
     * @return El JMenuItem.
     */
    public JMenuItem getMenuItemActualizarProducto() { return menuItemActualizarProducto; }

    /**
     * Obtiene el JMenuItem para la acción "Buscar Producto".
     * @return El JMenuItem.
     */
    public JMenuItem getMenuItemBuscarProducto() { return menuItemBuscarProducto; }

    /**
     * Obtiene el JMenuItem para la acción "Crear Carrito".
     * @return El JMenuItem.
     */
    public JMenuItem getMenuItemCrearCarrito() { return menuItemCrearCarrito; }

    /**
     * Obtiene el JMenuItem para la acción "Buscar Carrito".
     * @return El JMenuItem.
     */
    public JMenuItem getMenuItemBuscarCarrito() { return menuItemBuscarCarrito; }

    /**
     * Obtiene el JMenuItem para la acción "Modificar Carrito".
     * @return El JMenuItem.
     */
    public JMenuItem getMenuItemModificarCarrito() { return menuItemModificarCarrito; }

    /**
     * Obtiene el JMenuItem para la acción "Ver Detalles del Carrito".
     * @return El JMenuItem.
     */
    public JMenuItem getMenuItemVerDetallesCarrito() { return menuItemVerDetallesCarrito; }

    /**
     * Obtiene el JMenuItem para la acción "Eliminar Carrito".
     * @return El JMenuItem.
     */
    public JMenuItem getMenuItemEliminarCarrito() { return menuItemEliminarCarrito; }

    /**
     * Obtiene el JMenuItem para la acción "Registrar Usuario".
     * @return El JMenuItem.
     */
    public JMenuItem getMenuItemRegistrarUsuario() { return menuItemRegistrarUsuario; }

    /**
     * Obtiene el JMenuItem para la acción "Listar Usuarios".
     * @return El JMenuItem.
     */
    public JMenuItem getMenuItemListarUsuarios() { return menuItemListarUsuarios; }

    /**
     * Obtiene el JMenuItem para la acción "Modificar Usuario".
     * @return El JMenuItem.
     */
    public JMenuItem getMenuItemModificarUsuario() { return menuItemModificarUsuario; }

    /**
     * Obtiene el JMenuItem para la acción "Eliminar Usuario".
     * @return El JMenuItem.
     */
    public JMenuItem getMenuItemEliminarUsuario() { return menuItemEliminarUsuario; }

    /**
     * Obtiene el JMenuItem para la acción "Cerrar Sesión".
     * @return El JMenuItem.
     */
    public JMenuItem getMenuItemCerrarSesion() { return menuItemCerrarSesion; }

    /**
     * Obtiene el JMenuItem para cambiar el idioma a "Español".
     * @return El JMenuItem.
     */
    public JMenuItem getMenuItemIdiomaEspanol() { return menuItemIdiomaEspanol; }

    /**
     * Obtiene el JMenuItem para cambiar el idioma a "Inglés".
     * @return El JMenuItem.
     */
    public JMenuItem getMenuItemIdiomaIngles() { return menuItemIdiomaIngles; }

    /**
     * Obtiene el JMenuItem para cambiar el idioma a "Francés".
     * @return El JMenuItem.
     */
    public JMenuItem getMenuItemIdiomaFrances() { return menuItemIdiomaFrances; }
}